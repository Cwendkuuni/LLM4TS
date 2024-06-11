
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

    @Test
    public void testSingleConnectionDataSourceGetConnection() throws SQLException {
        // Create SingleConnectionDataSource object
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();

        // Test getConnection method
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        assertFalse(connection.isClosed());
    }

    @Test
    public void testSingleConnectionDataSourceGetConnectionWithUsernameAndPassword() throws SQLException {
        // Create SingleConnectionDataSource object
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();

        // Test getConnection with username and password method
        Connection connection = dataSource.getConnection("username", "password");
        assertNotNull(connection);
        assertFalse(connection.isClosed());
    }

    @Test(expected = SQLException.class)
    public void testSingleConnectionDataSourceGetConnectionWithInvalidUsernameAndPassword() throws SQLException {
        // Create SingleConnectionDataSource object
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();

        // Test getConnection with invalid username and password method
        dataSource.getConnection("invalidUsername", "invalidPassword");
    }

    @Test
    public void testSingleConnectionDataSourceResetConnection() throws SQLException {
        // Create SingleConnectionDataSource object
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();

        // Reset the connection
        dataSource.resetConnection();
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        assertFalse(connection.isClosed());
    }

    @Test
    public void testSingleConnectionDataSourceAddInitializer() {
        // Create SingleConnectionDataSource object
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();

        // Create mock ConnectionInitializer
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);

        // Add initializer
        dataSource.addInitializer(initializer);
        assertEquals(1, dataSource.getInitializerList().size());
    }

    @Test
    public void testSingleConnectionDataSourceClose() throws SQLException {
        // Create SingleConnectionDataSource object
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();

        // Close the connection
        dataSource.close();
        assertNull(dataSource.getConnection());
    }

    @Test(expected = SQLException.class)
    public void testSingleConnectionDataSourceInnerCreateConnectionWhenConnectionIsNotNull() throws SQLException {
        // Create SingleConnectionDataSource object with connection
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.getConnection();

        // Try to create a new connection when connection is not null
        dataSource.innerCreateConnection();
    }

    // Add more test cases for edge cases and exceptions as needed

}
