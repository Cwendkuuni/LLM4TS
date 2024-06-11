import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {
    private SingleConnectionDataSource dataSource;

    @Test
    public void testGetConnectionReturnsConnection() throws SQLException {
        dataSource = new SingleConnectionDataSource();
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        dataSource = new SingleConnectionDataSource();
        Connection connection = dataSource.getConnection("user", "password");
        assertNotNull(connection);
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource = new SingleConnectionDataSource();
        dataSource.resetConnection();
        assertNull(dataSource.getConnection());
    }

    @Test
    public void testAddInitializer() {
        dataSource = new SingleConnectionDataSource();
        ConnectionInitializer connectionInitializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(connectionInitializer);
        assertTrue(dataSource.getInitializerList().contains(connectionInitializer));
    }

    @Test
    public void testCloseConnection() throws SQLException {
        dataSource = new SingleConnectionDataSource();
        Connection connection = dataSource.getConnection();
        dataSource.close();
        assertTrue(connection.isClosed());
    }

    @Test
    public void testPrepareConnectionSetsAutoCommit() throws SQLException {
        dataSource = new SingleConnectionDataSource();
        Connection connection = mock(Connection.class);
        dataSource.setAutoCommit(true);
        dataSource.prepareConnection(connection);
        verify(connection, times(1)).setAutoCommit(true);
    }

    @Test
    public void testTryLockReturnsTrue() {
        dataSource = new SingleConnectionDataSource();
        assertTrue(dataSource.tryLock());
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionProxyThrowsConflictException() {
        dataSource = new SingleConnectionDataSource();
        Connection connection = mock(Connection.class);
        dataSource.getConnectionProxy(connection);
        dataSource.getConnectionProxy(connection);
    }

    @Test
    public void testInitConnection() throws SQLException {
        dataSource = new SingleConnectionDataSource();
        Connection connection = mock(Connection.class);
        ConnectionInitializer connectionInitializer = mock(ConnectionInitializer.class);
        when(connectionInitializer.init(connection)).thenThrow(new Exception());
        dataSource.addInitializer(connectionInitializer);
        assertThrows(SQLException.class, () -> dataSource.initConnection(connection));
    }

    @Test
    public void testOnConnectionReset() {
        dataSource = new SingleConnectionDataSource();
        EventPublisher eventPublisher = mock(EventPublisher.class);
        dataSource.setEventPublisher(eventPublisher);
        Connection connection = mock(Connection.class);
        dataSource.onConnectionReset(connection);
        verify(eventPublisher, times(1)).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        dataSource = new SingleConnectionDataSource(true);
        assertThrows(SQLException.class, () -> dataSource.innerCreateConnection());
    }

    @Test
    public void testCloseIgnoreInvocationHandlerCloseMethod() throws Throwable {
        Lock lock = mock(Lock.class);
        Connection connection = mock(Connection.class);
        CloseIgnoreInvocationHandler handler = new CloseIgnoreInvocationHandler(connection, lock);
        Method method = CloseIgnoreInvocationHandler.class.getDeclaredMethod("close", null);
        method.setAccessible(true);
        method.invoke(handler, null);
        verify(lock, times(1)).unlock();
    }
}