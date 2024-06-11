
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource(true); // Initialize with autoReconnect=true
        dataSource.setUsername("test_user");
        dataSource.setPassword("test_password");
        dataSource.setLoginTimeout(10);
        dataSource.setEventPublisher(mock(EventPublisher.class));
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        assertTrue(connection.isValid(10)); // Assuming database connection timeout is 10 seconds
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() {
        assertThrows(SQLException.class, () -> dataSource.getConnection("invalid_user", "invalid_password"));
        assertDoesNotThrow(() -> dataSource.getConnection("test_user", "test_password"));
    }

    @Test
    public void testResetConnection() {
        assertDoesNotThrow(() -> dataSource.resetConnection());
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        assertEquals(1, dataSource.getInitializerList().size());
    }

    @Test
    public void testClose() {
        Connection connection = mock(Connection.class);
        dataSource.setConnection(connection);
        assertDoesNotThrow(() -> dataSource.close());
        verify(connection, times(1)).close();
    }

    @Test
    public void testPrepareConnection() {
        Connection connection = mock(Connection.class);
        dataSource.setAutoCommit(true); // Set autoCommit to true
        dataSource.prepareConnection(connection);
        verify(connection, times(1)).setAutocommit(true);
    }

    @Test
    public void testTryLock() {
        assertTrue(dataSource.tryLock()); // Assuming lock acquisition is successful
    }

    @Test
    public void testConnectionProxy() {
        Connection connection = mock(Connection.class);
        Lock lock = mock(Lock.class);
        when(lock.tryLock(anyLong(), any(TimeUnit.class))).thenReturn(true); // Mock successful lock acquisition
        Connection proxyConnection = dataSource.getConnectionProxy(connection, lock);
        assertEquals("java.lang.reflect.Proxy", proxyConnection.getClass().getName());
    }

    // Additional test cases for edge cases and exceptions can be added here

}
