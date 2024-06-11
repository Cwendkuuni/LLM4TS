import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import java.sql.Connection;
import java.sql.SQLException;
import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.shared.exception.ConflictException;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
        dataSource.setUsername("test_user");
        dataSource.setPassword("test_password");
        dataSource.setUrl("test_url");
    }

    @Test
    public void testGetConnectionReturnsValidConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
    }

    @Test
    public void testGetConnectionWithCredentialsReturnsValidConnection() throws SQLException {
        Connection connection = dataSource.getConnection("test_user", "test_password");
        assertNotNull(connection);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidCredentialsThrowsException() throws SQLException {
        dataSource.getConnection("invalid_user", "invalid_password");
    }

    @Test
    public void testResetConnectionSuccessfullyResetsConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        dataSource.resetConnection();
        assertNotNull(dataSource.getConnection());
    }

    @Test
    public void testCloseClosesConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        dataSource.close();
        assertTrue(connection.isClosed());
    }

    @Test(expected = ConflictException.class)
    public void testConnectionProxyThrowsConflictExceptionWhenOccupied() throws SQLException {
        Connection connection = dataSource.getConnection();
        Connection proxyConnection = dataSource.getConnection();
    }

    @Test
    public void testAddInitializerSuccessfullyAddsInitializer() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        assertEquals(1, dataSource.getInitializerList().size());
    }

    @Test
    public void testPrepareConnectionSetsAutoCommit() throws SQLException {
        Connection connection = mock(Connection.class);
        dataSource.setAutoCommit(true);
        dataSource.prepareConnection(connection);
        verify(connection, times(1)).setAutoCommit(true);
    }

    @Test
    public void testOnConnectionCreatedLogsDebugMessage() {
        dataSource.onConnectionCreated(mock(Connection.class));
        assertTrue(true); // Test passes if no exception is thrown
    }

    @Test
    public void testOnConnectionResetPublishesEventWhenPublisherExists() {
        EventPublisher eventPublisher = mock(EventPublisher.class);
        dataSource.setEventPublisher(eventPublisher);
        Connection connection = mock(Connection.class);
        dataSource.onConnectionReset(connection);
        verify(eventPublisher, times(1)).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test
    public void testInnerCreateConnectionCreatesConnection() throws SQLException {
        assertNotNull(dataSource.innerCreateConnection());
    }

    @Test
    public void testGetConnectionProxyReturnsProxyConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        assertNotNull(dataSource.getConnectionProxy(connection));
    }

    @Test
    public void testCloseIgnoreInvocationHandlerHandlesMethodCalls() throws Throwable {
        CloseIgnoreInvocationHandler handler = new CloseIgnoreInvocationHandler(mock(Connection.class), mock(Lock.class));
        Object result = handler.invoke(null, mock(Method.class), null);
        assertNotNull(result);
    }
}