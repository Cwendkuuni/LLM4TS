
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

public class SingleConnectionDataSourceTest {

    @Test
    public void testGetConnection() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource(false);
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource(false);
        Connection connection = dataSource.getConnection("username", "password");
        assertNotNull(connection);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionInvalidCredentials() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource(false);
        dataSource.getConnection("invalid_username", "invalid_password");
    }

    @Test
    public void testResetConnection() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource(false);
        dataSource.resetConnection();
        assertNull(dataSource.getConnection());
    }

    @Test
    public void testAddInitializer() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource(false);
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        verify(initializer).init(any(Connection.class));
    }

    @Test
    public void testClose() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource(false);
        assertNotNull(dataSource.getConnection());
        dataSource.close();
        assertNull(dataSource.getConnection());
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource(false);
        Connection connection = mock(Connection.class);
        dataSource.prepareConnection(connection);
        verify(connection).setAutoCommit(any(Boolean.class));
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionProxyTimeout() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource(false);
        Connection connection = dataSource.getConnection();
        ((CloseIgnoreInvocationHandler) Proxy.getInvocationHandler(connection)).invoke(null, null, null);
    }

    @Test
    public void testInitConnection() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource(false);
        Connection connection = mock(Connection.class);
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        dataSource.initConnection(connection);
        verify(initializer).init(connection);
    }

    @Test
    public void testOnConnectionCreated() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource(false);
        dataSource.onConnectionCreated(mock(Connection.class));
    }

    @Test
    public void testOnConnectionReset() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource(false);
        dataSource.setEventPublisher(mock(EventPublisher.class));
        assertNotNull(dataSource.getEventPublisher());
        dataSource.onConnectionReset(mock(Connection.class));
    }

    @Test(expected = SQLException.class)
    public void testInnerCreateConnectionFailed() throws SQLException {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource(false);
        Connection connection = mock(Connection.class);
        dataSource.setConnection(connection);
        doThrow(SQLException.class).when(connection).getAutoCommit();
        dataSource.innerCreateConnection();
    }

}
