
import org.junit.Test;
import java.sql.Connection;
import java.sql.SQLException;

public class SingleConnectionDataSourceTest {

    @Test
    public void testGetConnection() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        try {
            Connection connection = dataSource.getConnection();
            assert connection != null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetConnectionWithUsernamePassword() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        try {
            Connection connection = dataSource.getConnection("username", "password");
            assert connection != null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testResetConnection() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        try {
            dataSource.resetConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAddInitializer() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        ConnectionInitializer initializer = connection -> {
            // do something
        };
        dataSource.addInitializer(initializer);
        assert dataSource.getInitializerList().contains(initializer);
    }

    @Test
    public void testClose() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.close();
        // How to verify this?
    }

}
