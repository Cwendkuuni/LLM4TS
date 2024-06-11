import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

    @Test
    public void testGetConnection() throws SQLException {
        // Test case for getConnection method
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        // Test case for getConnection method with username and password
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        String username = "testUser";
        String password = "testPassword";
        Connection connection = dataSource.getConnection(username, password);
        assertNotNull(connection);
    }

    @Test
    public void testResetConnection() throws SQLException {
        // Test case for resetConnection method
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.resetConnection();
        assertNull(dataSource.getConnection());
    }

    @Test
    public void testClose() throws SQLException {
        // Test case for close method
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.close();
        assertNull(dataSource.getConnection());
    }

    // Add more test cases for other methods

}