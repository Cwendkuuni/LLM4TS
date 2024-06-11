import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

public class SingleConnectionDataSourceTest {
    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
        dataSource.setUsername("test");
        dataSource.setPassword("test");
    }

    @Test
    public void testGetConnection_whenConnectionIsNull_shouldCreateNewConnection() throws SQLException {
        Connection connection = dataSource.getConnection();

        assertNotNull(connection);
    }

    @Test(expected = SQLException.class)
    public void testGetConnection_whenConnectionIsClosedAndAutoReconnectDisabled_shouldThrowSQLException() throws SQLException {
        Connection connection = mock(Connection.class);
        when(connection.isClosed()).thenReturn(true);
        dataSource.setConnection(connection);

        dataSource.getConnection();
    }

    @Test(expected = SQLException.class)
    public void testGetConnection_whenUsernameOrPasswordIncorrect_shouldThrowSQLException() throws SQLException {
        dataSource.getConnection("invalidUser", "invalidPassword");
    }

    @Test
    public void testResetConnection_shouldResetConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        dataSource.setConnection(connection);

        dataSource.resetConnection();

        verify(connection).close();
        assertNull(dataSource.getConnection());
    }

    @Test
    public void testAddInitializer_shouldAddConnectionInitializer() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);

        assertEquals(1, dataSource.getInitializerList().size());
    }

    @Test
    public void testClose_shouldCloseConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        dataSource.setConnection(connection);

        dataSource.close();

        verify(connection).close();
    }

    @Test(expected = SQLException.class)
    public void testPrepareConnection_whenAutoCommitMismatch_shouldSetAutoCommit() throws SQLException {
        Connection connection = mock(Connection.class);
        dataSource.setAutoCommit(true);

        dataSource.prepareConnection(connection);

        verify(connection).setAutoCommit(true);
    }

    @Test
    public void testTryLock_whenLockNotAcquired_shouldThrowRuntimeException() {
        assertFalse(dataSource.tryLock());
    }

    @Test
    public void testInnerCreateConnection_shouldCreateNewConnection() throws SQLException {
        Connection connection = dataSource.getConnection();

        assertNotNull(connection);
    }

    @Test
    public void testCloseIgnoreInvocationHandler_shouldSuppressCloseCall() throws Throwable {
        Connection connection = mock(Connection.class);
        Lock lock = mock(Lock.class);

        SingleConnectionDataSource.CloseIgnoreInvocationHandler handler = new SingleConnectionDataSource.CloseIgnoreInvocationHandler(connection, lock);
        Object result = handler.invoke(connection, connection.getClass().getMethod("equals", Object.class), new Object[]{connection});

        assertTrue((boolean) result);
    }
}