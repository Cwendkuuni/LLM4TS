import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

    @Test
    public void testGetConnectionWhenConnectionIsNull() {
        // Test the case when connection is null, should create a new connection
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        assertNotNull(dataSource.getConnection());
    }

    @Test
    public void testGetConnectionWhenConnectionIsClosed() {
        // Test the case when connection is closed, should reset and create a new connection
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection mockConnection = mock(Connection.class);
        when(mockConnection.isClosed()).thenReturn(true);
        dataSource.setConnection(mockConnection);
        assertNotNull(dataSource.getConnection());
    }

    @Test
    public void testGetConnectionWithUsernamePassword() {
        // Test the case when getting connection with valid username and password
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.setUsername("user");
        dataSource.setPassword("pass");
        assertEquals(dataSource.getConnection("user", "pass"), dataSource.getConnection());
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidUsernamePassword() throws SQLException {
        // Test the case when getting connection with invalid username and password, should throw SQLException
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.getUsername("user");
        dataSource.getPassword("pass");
        dataSource.getConnection("invalid", "credentials");
    }

    @Test
    public void testResetConnection() {
        // Test the case when resetting connection, should reset the connection and lock
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        Connection mockConnection = mock(Connection.class);
        dataSource.setConnection(mockConnection);
        dataSource.setLock(mock(Lock.class));
        dataSource.resetConnection();
        assertNull(dataSource.getConnection());
        assertNull(dataSource.getLock());
    }

    // Add more test cases for other methods if necessary

}