import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import com.oceanbase.odc.common.event.EventPublisher;


import org.junit.Before;
import org.junit.Test;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource(true);
        dataSource.setUsername("testUser");
        dataSource.setPassword("testPassword");
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
    }

    @Test
    public void testGetConnectionWithUsernamePassword() throws SQLException {
        Connection connection = dataSource.getConnection("testUser", "testPassword");
        assertNotNull(connection);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidUsernamePassword() throws SQLException {
        dataSource.getConnection("invalidUser", "invalidPassword");
    }

    @Test
    public void testResetConnection() throws SQLException{
        Connection connection = dataSource.getConnection();
        dataSource.resetConnection();
        Connection newConnection = dataSource.getConnection();

        assertNotEquals(connection, newConnection);
    }

    @Test
    public void testClose() throws SQLException {
        Connection connection = dataSource.getConnection();
        dataSource.close();
        assertTrue(connection.isClosed());
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionWhenConnectionOccupied() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        when(mockConnection.isClosed()).thenReturn(false);
        when(mockConnection.isValid(anyInt())).thenReturn(true);

        dataSource.getConnectionProxy(mockConnection);
        dataSource.getConnectionProxy(mockConnection);
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer mockInitializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(mockInitializer);

        assertEquals(1, dataSource.getInitializerList().size());
        assertTrue(dataSource.getInitializerList().contains(mockInitializer));
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        when(mockConnection.getAutoCommit()).thenReturn(false);

        dataSource.prepareConnection(mockConnection);
        verify(mockConnection, times(1)).setAutoCommit(false);
    }

    @Test
    public void testCloseIgnoreInvocationHandlerMethods() throws Throwable {
        Connection mockConnection = mock(Connection.class);
        dataSource.new CloseIgnoreInvocationHandler(mockConnection, mock(Lock.class)).invoke(mockConnection, mockConnection.getClass().getMethod("isClosed"), null);

        verify(mockConnection, times(1)).isClosed();
    }

}