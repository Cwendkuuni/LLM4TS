import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    private Connection connectionMock;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
        connectionMock = mock(Connection.class);
    }

    @Test
    public void testGetConnection_WhenConnectionIsNull_ShouldCreateNewConnection() throws SQLException {
        Connection createdConnection = dataSource.getConnection();
        assertNotNull(createdConnection);
    }

    @Test
    public void testGetConnection_WhenConnectionIsClosedAndAutoReconnectIsFalse_ShouldThrowSQLException() throws SQLException {
        dataSource.setAutoReconnect(false);
        dataSource.setConnection(connectionMock);

        when(connectionMock.isClosed()).thenReturn(true);

        try {
            dataSource.getConnection();
            fail("SQLException should have been thrown");
        } catch (SQLException e) {
            // Expected exception
        }
    }

    @Test
    public void testResetConnection_ShouldCloseExistingConnectionAndReset() throws SQLException {
        dataSource.setConnection(connectionMock);
        assertNotNull(dataSource.getConnection());

        dataSource.resetConnection();

        verify(connectionMock).close();
        assertNull(dataSource.getConnection());
        assertNull(dataSource.getLock());
    }

    @Test
    public void testAddInitializer_ShouldAddConnectionInitializerToList() {
        ConnectionInitializer initializerMock = mock(ConnectionInitializer.class);

        dataSource.addInitializer(initializerMock);

        assertTrue(dataSource.getInitializerList().contains(initializerMock));
    }

    @Test
    public void testClose_ShouldCloseConnectionWithoutError() throws SQLException {
        dataSource.setConnection(connectionMock);

        dataSource.close();

        verify(connectionMock).close();
    }

    @Test
    public void testTryLock_WhenLockIsAvailable_ShouldReturnTrue() {
        assertTrue(dataSource.tryLock());
    }

    @Test
    public void testTryLock_WhenTimeoutOccurs_ShouldThrowRuntimeException() {
        dataSource.setTimeOutMillis(0);

        try {
            dataSource.tryLock();
            fail("RuntimeException should have been thrown");
        } catch (RuntimeException e) {
            // Expected exception
        }
    }

    @Test
    public void testInnerCreateConnection_WhenConnectionIsNull_ShouldCreateNewConnection() throws SQLException {
        Connection createdConnection = dataSource.innerCreateConnection();
        assertNotNull(createdConnection);
    }

    @Test
    public void testInnerCreateConnection_WhenConnectionIsNotNull_ShouldThrowIllegalStateException() throws SQLException {
        dataSource.setConnection(connectionMock);

        try {
            dataSource.innerCreateConnection();
            fail("IllegalStateException should have been thrown");
        } catch (IllegalStateException e) {
            // Expected exception
        }
    }
}