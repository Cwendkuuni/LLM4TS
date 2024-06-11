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
    }

    @Test
    public void testGetConnection_NullConnection_ReturnsNewConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
    }

    @Test(expected = SQLException.class)
    public void testGetConnection_ClosedOrInvalidConnection_ThrowsSQLException() throws SQLException {
        Connection connection = mock(Connection.class);
        when(connection.isClosed()).thenReturn(true);
        dataSource.connection = connection;

        dataSource.getConnection();
    }

    @Test(expected = SQLException.class)
    public void testGetConnection_InvalidUsernamePassword_ThrowsSQLException() throws SQLException {
        String username = "admin";
        String password = "password";

        dataSource.getConnection(username, password);
    }

    @Test
    public void testResetConnection_ResetsConnection() throws SQLException {
        dataSource.resetConnection();
        assertNull(dataSource.connection);
    }

    @Test
    public void testAddInitializer_AddsConnectionInitializer() {
        ConnectionInitializer connectionInitializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(connectionInitializer);

        assertEquals(1, dataSource.initializerList.size());
    }

    @Test
    public void testClose_ClosesConnection() throws Exception {
        Connection connection = mock(Connection.class);
        dataSource.connection = connection;
        
        dataSource.close();

        verify(connection).close();
    }
}

