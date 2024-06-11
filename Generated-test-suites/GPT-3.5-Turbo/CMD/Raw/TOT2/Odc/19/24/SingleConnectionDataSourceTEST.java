import org.junit.Test;
import static org.junit.Assert.*;

public class SingleConnectionDataSourceTest {

    @Test
    public void testGetConnection() {
        // Test normal getConnection() functionality
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        assertNotNull(dataSource.getConnection());
    }

    @Test
    public void testGetConnectionWithCredentials() {
        // Test getConnection(username, password) with valid credentials
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        String username = "user";
        String password = "password";
        assertNotNull(dataSource.getConnection(username, password));
    }

    @Test
    public void testResetConnection() {
        // Test resetting the connection
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        try {
            dataSource.resetConnection();
            assertNotNull(dataSource.getConnection());
        } catch (SQLException e) {
            fail("Error resetting connection: " + e.getMessage());
        }
    }

    @Test
    public void testAddInitializer() {
        // Test adding a new ConnectionInitializer
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        ConnectionInitializer initializer = new SomeConnectionInitializer();
        dataSource.addInitializer(initializer);
        assertTrue(dataSource.getInitializerList().contains(initializer));
    }

    @Test
    public void testClose() {
        // Test closing the connection
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.close();
        assertNull(dataSource.getConnection());
    }

    @Test
    public void testPrepareConnection() {
        // Test preparing the connection with autoCommit setting
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection connection = dataSource.getConnection();
        try {
            dataSource.prepareConnection(connection);
            assertTrue(connection.getAutoCommit() == dataSource.getAutoCommit());
        } catch (SQLException e) {
            fail("Error preparing connection: " + e.getMessage());
        }
    }

}