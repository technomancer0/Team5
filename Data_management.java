package management;

import java.sql.*;
import javax.sql.DataSource;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Service
public class Data_management {

    @Autowired
    private DataSource dataSource;

    private Connection conn;
    public static Statement st = null;
    public static PreparedStatement ps = null;

    public void connect() {
        try {
            conn = dataSource.getConnection();
            st = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // creates a new row in the company table 
    // used to create a new profile 
    public void insert_company(int id, String name, String password) {
        try {
            String sql = "INSERT INTO clients VALUES (?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setString(3, password);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting company: " + e.getMessage());
        } finally {
            try {
                if (ps != null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // creates blank rows on all financial tables
    // a unique company id is used to identify the profile to which the data belongs
    // a time id is used to differentiate between multiple row of data that belonging to the same company
    public void create_finance_report(int a, int b, String c) {

        int id = a;
        int t_id = b;
        String time = c;

        try {
            String sql1 = "INSERT INTO total_earnings VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql1);
            ps.setInt(1, id);
            ps.setNull(2, java.sql.Types.DOUBLE);
            ps.setNull(3, java.sql.Types.DOUBLE);
            ps.setNull(4, java.sql.Types.DOUBLE);
            ps.setNull(5, java.sql.Types.DOUBLE);
            ps.setString(6, time);
            ps.setInt(7, t_id);
            ps.setInt(8, 0);
            ps.executeUpdate();
            ps.close();


            String sql2 = "INSERT INTO total_expenses VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql2);
            ps.setInt(1, id);
            ps.setNull(2, java.sql.Types.DOUBLE);
            ps.setNull(3, java.sql.Types.DOUBLE);
            ps.setNull(4, java.sql.Types.DOUBLE);
            ps.setNull(5, java.sql.Types.DOUBLE);
            ps.setString(6, time);
            ps.setInt(7, t_id);
            ps.setInt(8, 0);
            ps.executeUpdate();
            ps.close();

            String sql3 = "INSERT INTO financial_history VALUES (?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql3);
            ps.setInt(1, id);
            ps.setString(2, time);
            ps.setNull(3, java.sql.Types.DOUBLE);
            ps.setNull(4, java.sql.Types.DOUBLE);
            ps.setNull(5, java.sql.Types.DOUBLE);
            ps.setInt(6, t_id);
            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // used to change the amount earned for q1 of a total_expenses row 
    public void alter_total_expenses_q1(int c_id, int t_id, double val) {
        int id = c_id;
        int time = t_id;
        double value = val;
        int var = 0;

        var = check_complete(id, time, "total_expenses", "complete_status");

        if (var == 0) {

            update_qs(id, time, value, "total_expenses", "expenses_q1");
        }

    }

    // used to change the amount earned for q3 of a total_expenses row 
    public void alter_total_expenses_q2(int c_id, int t_id, double val) {
        int id = c_id;
        int time = t_id;
        double value = val;
        int var = 0;

        var = check_complete(id, time, "total_expenses", "complete_status");

        if (var == 0) {

            update_qs(id, time, value, "total_expenses", "expenses_q2");
        }

    }

    // used to change the amount earned for q3 of a total_expenses row
    public void alter_total_expenses_q3(int c_id, int t_id, double val) {
        int id = c_id;
        int time = t_id;
        double value = val;
        int var = 0;

        var = check_complete(id, time, "total_expenses", "complete_status");

        if (var == 0) {

            update_qs(id, time, value, "total_expenses", "expenses_q3");
        }

    }

    // used to change the amount earned for q4 of a total_expense row
    public void alter_total_expenses_q4(int c_id, int t_id, double val) {
        int id = c_id;
        int time = t_id;
        double value = val;
        int var = 0;

        var = check_complete(id, time, "total_expenses", "complete_status");

        if (var == 0) {

            update_qs(id, time, value, "total_expenses", "expenses_q4");
        }

    }

    // used to change the complete status of a total_expense row
    public void alter_total_expenses_complete(int c_id, int t_id, int val) {
        int id = c_id;
        int time = t_id;
        int value = val;
        int var = 0;

        update_qs(id, time, value, "total_expenses", "complete_status");

        var = check_complete(id, time, "total_expenses", "complete_status");

        if (var == 1) {
            double a = retrieve_finance_data(id, time, "total_expenses", "expenses_q1");
            double b = retrieve_finance_data(id, time, "total_expenses", "expenses_q2");
            double c = retrieve_finance_data(id, time, "total_expenses", "expenses_q3");
            double d = retrieve_finance_data(id, time, "total_expenses", "expenses_q4");

            double t_expn = a + b + c + d;

            alter_finance_history(id, time, t_expn, "expenses");
        }

        var = check_complete(id, time, "total_earnings", "complete_status");

        if (var == 1) {

            double a = retrieve_finance_data(id, time, "financial_history", "revenue");
            double b = retrieve_finance_data(id, time, "financial_history", "expenses");

            double net = a - b;

            alter_finance_history(id, time, net, "net_revenue");

        }
    }

    public void alter_total_expenses_time(int c_id, int t_id, String t) {
        int id = c_id;
        int time = t_id;
        String period = t;
        int var = 0;

        var = check_complete(id, time, "total_expenses", "complete_status");

        if (var == 0) {

            update_time(id, time, period, "total_expenses", "time_period");
        }
    }

    public void alter_total_earnings_q1(int c_id, int t_id, double val) {
        int id = c_id;
        int time = t_id;
        double value = val;
        int var = 0;

        var = check_complete(id, time, "total_earnings", "complete_status");

        if (var == 0) {

            update_qs(id, time, value, "total_earnings", "earnings_q1");
        }
    }

    public void alter_total_earnings_q2(int c_id, int t_id, double val) {
        int id = c_id;
        int time = t_id;
        double value = val;
        int var = 0;

        var = check_complete(id, time, "total_earnings", "complete_status");

        if (var == 0) {

            update_qs(id, time, value, "total_earnings", "earnings_q2");
        }
    }

    public void alter_total_earnings_q3(int c_id, int t_id, double val) {
        int id = c_id;
        int time = t_id;
        double value = val;
        int var = 0;

        var = check_complete(id, time, "total_earnings", "complete_status");

        if (var == 0) {

            update_qs(id, time, value, "total_earnings", "earnings_q3");
        }
    }

    public void alter_total_earnings_q4(int c_id, int t_id, double val) {
        int id = c_id;
        int time = t_id;
        double value = val;
        int var = 0;

        var = check_complete(id, time, "total_earnings", "complete_status");

        if (var == 0) {

            update_qs(id, time, value, "total_earnings", "earnings_q4");
        }
    }

    public void alter_total_earnings_complete(int c_id, int t_id, int val) {
        int id = c_id;
        int time = t_id;
        int value = val;
        int var = 0;

        update_qs(id, time, value, "total_earnings", "complete_status");

        var = check_complete(id, time, "total_earnings", "complete_status");

        if (var == 1) {
            double a = retrieve_finance_data(id, time, "total_earnings", "earnings_q1");
            double b = retrieve_finance_data(id, time, "total_earnings", "earnings_q2");
            double c = retrieve_finance_data(id, time, "total_earnings", "earnings_q3");
            double d = retrieve_finance_data(id, time, "total_earnings", "earnings_q4");

            double t_earn = a + b + c + d;

            alter_finance_history(id, time, t_earn, "revenue");
        }

        var = check_complete(id, time, "total_expenses", "complete_status");

        if (var == 1) {

            double a = retrieve_finance_data(id, time, "financial_history", "revenue");
            double b = retrieve_finance_data(id, time, "financial_history", "expenses");

            double net = a - b;

            alter_finance_history(id, time, net, "net_revenue");

        }
    }

    public void alter_total_earnings_time(int c_id, int t_id, String t) {
        int id = c_id;
        int time = t_id;
        String period = t;
        int var = 0;

        var = check_complete(id, time, "total_earnings", "complete_status");

        if (var == 0) {

            update_time(id, time, period, "total_earnings", "time_period");
        }
    }

    public void alter_finance_history(int c_id, int t_id, double val, String c) {
        int id = c_id;
        int time = t_id;
        double value = val;
        String column = c;

        try {
            st.executeUpdate("UPDATE financial_history SET " + column + " =" + value + " WHERE client_id = " + id + " AND time_id = " + time);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void alter_finance_history_time(int c_id, int t_id, String t) {
        int id = c_id;
        int time_id = t_id;
        String time = t;

        try {
            st.executeUpdate("Update financial_history SET time_period =" + time + " WHERE client_id =" + id + " AND time_id =" + time_id);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void create_expense_q1(int c_id, int t_id, int ex_id, String ext, double val) {
        int id = c_id;
        int time_id = t_id;
        int expense_id = ex_id;
        String type = ext;
        double value = val;

        create_qs_expenses(id, 1, type, expense_id, value, time_id);

        double a = retrieve_finance_data(id, time_id, "total_expenses", "expenses_q1");
        double b = (a + value);

        alter_total_expenses_q1(id, time_id, b);

    }

    public void create_expense_q2(int c_id, int t_id, int ex_id, String ext, double val) {
        int id = c_id;
        int time_id = t_id;
        int expense_id = ex_id;
        String type = ext;
        double value = val;

        create_qs_expenses(id, 2, type, expense_id, value, time_id);

        double a = retrieve_finance_data(id, time_id, "total_expenses", "expenses_q2");
        double b = (a + value);

        alter_total_expenses_q2(id, time_id, b);

    }

    public void create_expense_q3(int c_id, int t_id, int ex_id, String ext, double val) {
        int id = c_id;
        int time_id = t_id;
        int expense_id = ex_id;
        String type = ext;
        double value = val;

        create_qs_expenses(id, 3, type, expense_id, value, time_id);

        double a = retrieve_finance_data(id, time_id, "total_expenses", "expenses_q3");
        double b = (a + value);

        alter_total_expenses_q3(id, time_id, b);
    }

    public void create_expense_q4(int c_id, int t_id, int ex_id, String ext, double val) {
        int id = c_id;
        int time_id = t_id;
        int expense_id = ex_id;
        String type = ext;
        double value = val;

        create_qs_expenses(id, 4, type, expense_id, value, time_id);

        double a = retrieve_finance_data(id, time_id, "total_expenses", "expenses_q4");
        double b = (a + value);

        alter_total_expenses_q4(id, time_id, b);
    }

    public void delete_expense_q1(int c_id, int t_id, int exp_d) {
        int id = c_id;
        int time_id = t_id;
        int expense_id = exp_d;

        double a = retrieve_specific(id, time_id, 1, expense_id, "expenses", "expense_amount", "expense_id");
        double b = retrieve_finance_data(id, time_id, "total_expenses", "expenses_q1");
        double c = b - a;

        delete_qs_expenses(id, time_id, expense_id, 1);

        alter_total_expenses_q1(id, time_id, c);
    }

    public void delete_expense_q2(int c_id, int t_id, int exp_d) {
        int id = c_id;
        int time_id = t_id;
        int expense_id = exp_d;

        double a = retrieve_specific(id, time_id, 2, expense_id, "expenses", "expense_amount", "expense_id");
        double b = retrieve_finance_data(id, time_id, "total_expenses", "expenses_q2");
        double c = b - a;

        delete_qs_expenses(id, time_id, expense_id, 2);

        alter_total_expenses_q2(id, time_id, c);
    }

    public void delete_expense_q3(int c_id, int t_id, int exp_d) {
        int id = c_id;
        int time_id = t_id;
        int expense_id = exp_d;

        double a = retrieve_specific(id, time_id, 3, expense_id, "expenses", "expense_amount", "expense_id");
        double b = retrieve_finance_data(id, time_id, "total_expenses", "expenses_q3");
        double c = b - a;

        delete_qs_expenses(id, time_id, expense_id, 3);

        alter_total_expenses_q3(id, time_id, c);
    }

    public void delete_expense_q4(int c_id, int t_id, int exp_d) {
        int id = c_id;
        int time_id = t_id;
        int expense_id = exp_d;

        double a = retrieve_specific(id, time_id, 4, expense_id, "expenses", "expense_amount", "expense_id");
        double b = retrieve_finance_data(id, time_id, "total_expenses", "expenses_q4");
        double c = b - a;

        delete_qs_expenses(id, time_id, expense_id, 4);

        alter_total_expenses_q4(id, time_id, c);
    }

    public void create_revenue_q1(int c_id, int sd, double val, int num, int t_id, String n) {
        int id = c_id;
        int time_id = t_id;
        int sale_id = sd;
        String name = n;
        int amount = num;
        double value = val;

        create_qs_earnings(id, 1, sale_id, value, amount, time_id, name);
        double a = retrieve_finance_data(id, time_id, "total_earnings", "earnings_q1");
        double b = amount * value;
        double c = b + a;

        alter_total_earnings_q1(id, time_id, c);
    }

    public void create_revenue_q2(int c_id, int sd, double val, int num, int t_id, String n) {
        int id = c_id;
        int time_id = t_id;
        int sale_id = sd;
        String name = n;
        int amount = num;
        double value = val;

        create_qs_earnings(id, 2, sale_id, value, amount, time_id, name);
        double a = retrieve_finance_data(id, time_id, "total_earnings", "earnings_q2");
        double b = amount * value;
        double c = b + a;

        alter_total_earnings_q2(id, time_id, c);
    }

    public void create_revenue_q3(int c_id, int sd, double val, int num, int t_id, String n) {
        int id = c_id;
        int time_id = t_id;
        int sale_id = sd;
        String name = n;
        int amount = num;
        double value = val;

        create_qs_earnings(id, 3, sale_id, value, amount, time_id, name);
        double a = retrieve_finance_data(id, time_id, "total_earnings", "earnings_q3");
        double b = amount * value;
        double c = b + a;

        alter_total_earnings_q3(id, time_id, c);
    }

    public void create_revenue_q4(int c_id, int sd, double val, int num, int t_id, String n) {
        int id = c_id;
        int time_id = t_id;
        int sale_id = sd;
        String name = n;
        int amount = num;
        double value = val;

        create_qs_earnings(id, 4, sale_id, value, amount, time_id, name);
        double a = retrieve_finance_data(id, time_id, "total_earnings", "earnings_q4");
        double b = amount * value;
        double c = b + a;

        alter_total_earnings_q4(id, time_id, c);
    }

    public void delete_revenue_q1(int c_id, int t_id, int s_d) {
        int id = c_id;
        int time_id = t_id;
        int sale_id = s_d;

        double a = retrieve_specific(id, time_id, 1, sale_id, "revenue", "item_price", "sale_id");
        double b = retrieve_specific(id, time_id, 1, sale_id, "revenue", "sales_quanity", "sale_id");
        double c = a * b;
        double d = retrieve_finance_data(id, time_id, "total_earnings", "earnings_q1");
        double e = d - c;

        delete_qs_earnings(id, time_id, sale_id, 1);

        alter_total_earnings_q1(id, time_id, e);
    }

    public void delete_revenue_q2(int c_id, int t_id, int s_d) {
        int id = c_id;
        int time_id = t_id;
        int sale_id = s_d;

        double a = retrieve_specific(id, time_id, 2, sale_id, "revenue", "item_price", "sale_id");
        double b = retrieve_specific(id, time_id, 2, sale_id, "revenue", "sales_quanity", "sale_id");
        double c = a * b;
        double d = retrieve_finance_data(id, time_id, "total_earnings", "earnings_q2");
        double e = d - c;

        delete_qs_earnings(id, time_id, sale_id, 2);

        alter_total_earnings_q2(id, time_id, e);
    }

    public void delete_revenue_q3(int c_id, int t_id, int s_d) {
        int id = c_id;
        int time_id = t_id;
        int sale_id = s_d;

        double a = retrieve_specific(id, time_id, 3, sale_id, "revenue", "item_price", "sale_id");
        double b = retrieve_specific(id, time_id, 3, sale_id, "revenue", "sales_quanity", "sale_id");
        double c = a * b;
        double d = retrieve_finance_data(id, time_id, "total_earnings", "earnings_q3");
        double e = d - c;

        delete_qs_earnings(id, time_id, sale_id, 3);

        alter_total_earnings_q3(id, time_id, e);
    }

    public void delete_revenue_q4(int c_id, int t_id, int s_d) {
        int id = c_id;
        int time_id = t_id;
        int sale_id = s_d;

        double a = retrieve_specific(id, time_id, 4, sale_id, "revenue", "item_price", "sale_id");
        double b = retrieve_specific(id, time_id, 4, sale_id, "revenue", "sales_quanity", "sale_id");
        double c = a * b;
        double d = retrieve_finance_data(id, time_id, "total_earnings", "earnings_q4");
        double e = d - c;

        delete_qs_earnings(id, time_id, sale_id, 4);

        alter_total_earnings_q4(id, time_id, e);
    }

    public void update_time(int c_id, int t_id, String val, String t, String c) {
        int id = c_id;
        int time = t_id;
        String value = val;
        String table = t;
        String column = c;

        try {
            st.executeUpdate("UPDATE " + table + " SET " + column + " =" + value + " WHERE client_id = " + id + " AND time_id = " + time);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public double retrieve_specific(int c_id, int t_id, int q, int s_id, String t, String c, String s) {
        int id = c_id;
        int time_id = t_id;
        int specific = s_id;
        int quarter = q;
        String table = t;
        String column = c;
        String identify = s;
        double var = 0;

        ResultSet result = null;

        try {
            result = st.executeQuery("SELECT " + column + " FROM " + table + " WHERE client_id =" + id + " AND time_id =" + time_id + " And quarter_period =" + quarter + " And " + identify + " =" + specific);

            if (result.next()) {
                var = result.getInt(1);
                System.out.println("Integer value: " + var);
            } else {
                System.out.println("No data found.");
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return var;
    }

    // used to create the on the expenses table
    public void create_qs_expenses(int c_id, int q, String expt, int expd, double val, int t_id) {
        int id = c_id;
        int time_id = t_id;
        int quarter = q;
        String expn_type = expt;
        int expn_id = expd;
        Double amount = val;

        try {

            String sql1 = "INSERT INTO expenses VALUES (?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql1);
            ps.setInt(1, id);
            ps.setInt(2, quarter);
            ps.setString(3, expn_type);
            ps.setInt(4, expn_id);
            ps.setDouble(5, amount);
            ps.setInt(6, time_id);
            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete_qs_expenses(int c_id, int t_id, int exp_d, int q) {
        int id = c_id;
        int time_id = t_id;
        int expns_id = exp_d;
        int quarter = q;

        try {
            st.executeUpdate("DELETE FROM expenses WHERE client_id =" + id + " AND time_id =" + time_id + " AND expense_id =" + expns_id + " AND quarter_period=" + quarter);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void create_qs_earnings(int c_id, int q, int sd, double val, int num, int t_id, String n) {
        int id = c_id;
        int time_id = t_id;
        int quarter = q;
        int sale_id = sd;
        Double amount = val;
        int quantity = num;
        String name = n;

        try {

            String sql1 = "INSERT INTO revenue VALUES (?, ?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql1);
            ps.setInt(1, id);
            ps.setInt(2, quarter);
            ps.setInt(3, sale_id);
            ps.setDouble(4, amount);
            ps.setInt(5, quantity);
            ps.setInt(6, time_id);
            ps.setString(7, name);
            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete_qs_earnings(int c_id, int t_id, int s_d, int q) {
        int id = c_id;
        int time_id = t_id;
        int sale_id = s_d;
        int quarter = q;

        try {
            st.executeUpdate("DELETE FROM revenue WHERE client_id =" + id + " AND time_id =" + time_id + " AND sale_id =" + sale_id + " AND quarter_period=" + quarter);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void update_qs(int c_id, int t_id, double val, String t, String c) {
        int id = c_id;
        int time = t_id;
        double value = val;
        String table = t;
        String column = c;

        try {
            st.executeUpdate("UPDATE " + table + " SET " + column + " =" + value + "WHERE client_id =" + id + " AND time_id =" + time);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public double retrieve_finance_data(int c_id, int t_id, String t, String c) {
        int id = c_id;
        int time = t_id;
        String table = t;
        String column = c;
        double var = 0;

        ResultSet result = null;

        try {
            result = st.executeQuery("SELECT " + column + " FROM " + table + " WHERE client_id =" + id + " AND time_id =" + time);

            if (result.next()) {
                var = result.getInt(1);
                System.out.println("Integer value: " + var);
            } else {
                System.out.println("No data found.");
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return var;
    }

    // checks for the completion of a finance report so that it can be viewed ad financial history
    // is also used to prevent changes from being made to rows labeled as complete
    public int check_complete(int c_id, int t_id, String t, String c) {
        int id = c_id;
        int time = t_id;
        String table = t;
        String column = c;
        int var = 0;

        ResultSet result = null;

        try {
            result = st.executeQuery("SELECT " + column + " FROM " + table + " WHERE client_id =" + id + " AND time_id =" + time);

            if (result.next()) {
                var = result.getInt(1);
                System.out.println("Integer value: " + var);
            } else {
                System.out.println("No data found.");
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return var;
    }

    // deletes every table containing a specified company id and time id, with the exception of the client table
    //this method is used to delete all financial information from a certain time period (using the time_id variable)
    public void delete_finance_report(int c_id, int t_id) {
        int id = c_id;
        int time = t_id;
        try {
            st.executeUpdate("DELETE from total_earnings Where client_id =" + id + " AND time_id =" + time);
            st.executeUpdate("DELETE from total_expenses Where client_id =" + id + " AND time_id =" + time);
            st.executeUpdate("DELETE from financial_history Where client_id =" + id + " AND time_id =" + time);
            st.executeUpdate("DELETE from expenses Where client_id =" + id + " AND time_id =" + time);
            st.executeUpdate("DELETE from revenue Where client_id =" + id + " AND time_id =" + time);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // deletes all tables associated containing company id
    // the method is meant to be used as a way to delete the user profile and all it's information
    public void delete_company(int id) {
        int num = id;

        try {
            st.executeUpdate("DELETE from total_earnings Where client_id =" + num);
            st.executeUpdate("DELETE from total_expenses Where client_id =" + num);
            st.executeUpdate("DELETE from financial_history Where client_id =" + num);
            st.executeUpdate("DELETE from expenses Where client_id =" + num);
            st.executeUpdate("DELETE from revenue Where client_id =" + num);
            st.executeUpdate("DELETE from clients Where client_id =" + num);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {

        Data_management m = new Data_management();
        m.connect();

        //m.alter_total_expenses_q1(1, 1, 0);
        //m.alter_total_expenses_q2(1, 1, 0);
        //m.alter_total_expenses_q3(1, 1, 0);
        //m.alter_total_expenses_q4(1, 1, 0);
        //m.alter_total_earnings_q1(1, 1, 2);
        //m.alter_total_earnings_q2(1, 1, 3);
        //m.alter_total_earnings_q3(1, 1, 4);
        //m.alter_total_earnings_q4(1, 1, 5);

        //m.create_qs_earnings(1, 1, 23, 3.4, 2, 1, "paper");
        //m.delete_qs_earnings(1, 1, 23, 1);

        //m.create_expense_q1(1, 1, 28, "other", 10.4);
        //m.create_expense_q2(1, 1, 10, "each", 5.4);
        //m.create_expense_q3(1, 1, 948, "another", 7.3);
        //m.create_expense_q4(1, 1, 48, "north", 8.6);

        //m.delete_expense_q1(1, 1, 28);
        //m.delete_expense_q2(1, 1, 10);
        //m.delete_expense_q3(1, 1, 948);
        //m.delete_expense_q4(1, 1, 48);

        //m.create_revenue_q1(1, 25, 8.3, 2, 1, "cool");
        //m.create_revenue_q2(1, 29, 4.1, 3, 1, "cold");
        //m.create_revenue_q3(1, 4, 1.2, 4, 1, "old");
        //m.create_revenue_q4(1, 45, 7.3, 1, 1, "yor");

        //m.delete_revenue_q1(1, 1, 25);
        //m.delete_revenue_q2(1, 1, 29);
        //m.delete_revenue_q3(1, 1, 4);
        //m.delete_revenue_q4(1, 1, 45);

        //m.alter_total_expenses_time(1, 1, "2025");
        //m.alter_total_expenses_complete(1, 1, 0);
        //m.alter_total_earnings_time(1, 1, "2025");
        //m.alter_total_earnings_complete(1, 1, 0);
        //m.alter_finance_history_time(1, 1, "2025");

        //m.delete_company(1);
        //m.insert_company(1, "c1", "so");
        //m.create_finance_report(1, 1, "2024");

    }
}

@CrossOrigin(origins = "http://localhost:3000") // Replace with your frontend's URL
@RestController
@RequestMapping("/api")
public class DataManagementController {

    @Autowired
    private Data_management dataManagement;

    // Endpoint to insert a company
    @PostMapping("/insertCompany")
    public String insertCompany(@RequestParam int id, @RequestParam String name, @RequestParam String password) {
        dataManagement.insert_company(id, name, password);
        return "Company inserted successfully!";
    }

    // Endpoint to create a finance report
    @PostMapping("/createFinanceReport")
    public String createFinanceReport(@RequestParam int companyId, @RequestParam int timeId, @RequestParam String timePeriod) {
        dataManagement.create_finance_report(companyId, timeId, timePeriod);
        return "Finance report created successfully!";
    }

    // Endpoint to alter total expenses for a specific quarter
    @PostMapping("/alterTotalExpenses")
    public String alterTotalExpenses(@RequestParam int companyId, @RequestParam int timeId, @RequestParam double value, @RequestParam String quarter) {
        switch (quarter.toLowerCase()) {
            case "q1":
                dataManagement.alter_total_expenses_q1(companyId, timeId, value);
                break;
            case "q2":
                dataManagement.alter_total_expenses_q2(companyId, timeId, value);
                break;
            case "q3":
                dataManagement.alter_total_expenses_q3(companyId, timeId, value);
                break;
            case "q4":
                dataManagement.alter_total_expenses_q4(companyId, timeId, value);
                break;
            default:
                return "Invalid quarter specified!";
        }
        return "Total expenses updated successfully!";
    }

    // Endpoint to alter total earnings for a specific quarter
    @PostMapping("/alterTotalEarnings")
    public String alterTotalEarnings(@RequestParam int companyId, @RequestParam int timeId, @RequestParam double value, @RequestParam String quarter) {
        switch (quarter.toLowerCase()) {
            case "q1":
                dataManagement.alter_total_earnings_q1(companyId, timeId, value);
                break;
            case "q2":
                dataManagement.alter_total_earnings_q2(companyId, timeId, value);
                break;
            case "q3":
                dataManagement.alter_total_earnings_q3(companyId, timeId, value);
                break;
            case "q4":
                dataManagement.alter_total_earnings_q4(companyId, timeId, value);
                break;
            default:
                return "Invalid quarter specified!";
        }
        return "Total earnings updated successfully!";
    }

    // Endpoint to create an expense for a specific quarter
    @PostMapping("/createExpense")
    public String createExpense(@RequestParam int companyId, @RequestParam int timeId, @RequestParam int expenseId, @RequestParam String type, @RequestParam double value, @RequestParam String quarter) {
        switch (quarter.toLowerCase()) {
            case "q1":
                dataManagement.create_expense_q1(companyId, timeId, expenseId, type, value);
                break;
            case "q2":
                dataManagement.create_expense_q2(companyId, timeId, expenseId, type, value);
                break;
            case "q3":
                dataManagement.create_expense_q3(companyId, timeId, expenseId, type, value);
                break;
            case "q4":
                dataManagement.create_expense_q4(companyId, timeId, expenseId, type, value);
                break;
            default:
                return "Invalid quarter specified!";
        }
        return "Expense created successfully!";
    }

    // Endpoint to delete an expense for a specific quarter
    @DeleteMapping("/deleteExpense")
    public String deleteExpense(@RequestParam int companyId, @RequestParam int timeId, @RequestParam int expenseId, @RequestParam String quarter) {
        switch (quarter.toLowerCase()) {
            case "q1":
                dataManagement.delete_expense_q1(companyId, timeId, expenseId);
                break;
            case "q2":
                dataManagement.delete_expense_q2(companyId, timeId, expenseId);
                break;
            case "q3":
                dataManagement.delete_expense_q3(companyId, timeId, expenseId);
                break;
            case "q4":
                dataManagement.delete_expense_q4(companyId, timeId, expenseId);
                break;
            default:
                return "Invalid quarter specified!";
        }
        return "Expense deleted successfully!";
    }

    // Endpoint to create revenue for a specific quarter
    @PostMapping("/createRevenue")
    public String createRevenue(@RequestParam int companyId, @RequestParam int saleId, @RequestParam double value, @RequestParam int quantity, @RequestParam int timeId, @RequestParam String name, @RequestParam String quarter) {
        switch (quarter.toLowerCase()) {
            case "q1":
                dataManagement.create_revenue_q1(companyId, saleId, value, quantity, timeId, name);
                break;
            case "q2":
                dataManagement.create_revenue_q2(companyId, saleId, value, quantity, timeId, name);
                break;
            case "q3":
                dataManagement.create_revenue_q3(companyId, saleId, value, quantity, timeId, name);
                break;
            case "q4":
                dataManagement.create_revenue_q4(companyId, saleId, value, quantity, timeId, name);
                break;
            default:
                return "Invalid quarter specified!";
        }
        return "Revenue created successfully!";
    }

    // Endpoint to delete revenue for a specific quarter
    @DeleteMapping("/deleteRevenue")
    public String deleteRevenue(@RequestParam int companyId, @RequestParam int timeId, @RequestParam int saleId, @RequestParam String quarter) {
        switch (quarter.toLowerCase()) {
            case "q1":
                dataManagement.delete_revenue_q1(companyId, timeId, saleId);
                break;
            case "q2":
                dataManagement.delete_revenue_q2(companyId, timeId, saleId);
                break;
            case "q3":
                dataManagement.delete_revenue_q3(companyId, timeId, saleId);
                break;
            case "q4":
                dataManagement.delete_revenue_q4(companyId, timeId, saleId);
                break;
            default:
                return "Invalid quarter specified!";
        }
        return "Revenue deleted successfully!";
    }

    // Endpoint to delete a finance report
    @DeleteMapping("/deleteFinanceReport")
    public String deleteFinanceReport(@RequestParam int companyId, @RequestParam int timeId) {
        dataManagement.delete_finance_report(companyId, timeId);
        return "Finance report deleted successfully!";
    }

    // Endpoint to delete a company and all its data
    @DeleteMapping("/deleteCompany")
    public String deleteCompany(@RequestParam int companyId) {
        dataManagement.delete_company(companyId);
        return "Company deleted successfully!";
    }
}

// Application properties
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/app_data
spring.datasource.username=root
spring.datasource.password=rootpassword
spring.jpa.hibernate.ddl-auto=update


