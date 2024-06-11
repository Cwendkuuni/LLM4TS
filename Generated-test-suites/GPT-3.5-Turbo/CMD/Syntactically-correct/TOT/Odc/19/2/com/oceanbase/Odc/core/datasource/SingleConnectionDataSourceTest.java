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
        dataSource.setUsername("testUser");
        dataSource.setPassword("testPassword");
    }

    @Test
    public void testGetConnection_Success() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
    }

    @Test
    public void testGetConnection_InvalidUsernamePassword() {
        assertThrows(SQLException.class, () -> dataSource.getConnection("invalidUser", "invalidPassword"));
    }

    @Test
    public void testResetConnection_Success() throws SQLException {
        Connection connection = dataSource.getConnection();
        dataSource.resetConnection();
        Connection newConnection = dataSource.getConnection();
        assertNotSame(connection, newConnection);
    }

    @Test
    public void testCloseConnection_Success() throws SQLException {
        Connection connection = dataSource.getConnection();
        dataSource.close();
        assertTrue(connection.isClosed());
    }

    @Test
    public void testAddInitializer_Success() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        assertEquals(1, dataSource.getInitializerList().size());
    }

    @Test
    public void testInitConnection_Exception() throws SQLException {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        doThrow(new RuntimeException()).when(initializer).init(any(Connection.class));
        dataSource.addInitializer(initializer);
        assertThrows(SQLException.class, () -> dataSource.getConnection());
    }

    @Test
    public void testGetConnectionProxy_Success() {
        Connection connection = mock(Connection.class);
        assertNotNull(dataSource.getConnectionProxy(connection));
    }

    @Test
    public void testInnerCreateConnection_Success() throws SQLException {
        Connection connection = dataSource.innerCreateConnection();
        assertNotNull(connection);
    }

}