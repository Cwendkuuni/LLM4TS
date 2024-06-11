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
        dataSource.setPassword("testPass");
    }

    @Test
    public void testGetConnection_WhenConnectionIsNull() throws SQLException {
        Connection connection = dataSource.getConnection();

        assertNotNull(connection);
    }

    @Test
    public void testGetConnection_WhenConnectionIsClosedAndAutoReconnectIsTrue() throws SQLException {
        Connection connection = dataSource.getConnection();
        connection.close();

        Connection newConnection = dataSource.getConnection();

        assertNotNull(newConnection);
    }

    @Test(expected = SQLException.class)
    public void testGetConnection_InvalidUsernameOrPassword() throws SQLException {
        dataSource.getConnection("invalidUser", "invalidPass");
    }

    @Test
    public void testResetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        dataSource.resetConnection();

        Connection newConnection = dataSource.getConnection();

        assertNotEquals(connection, newConnection);
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);

        assertTrue(dataSource.getInitializerList().contains(initializer));
    }

    @Test
    public void testClose() throws SQLException {
        Connection connection = dataSource.getConnection();
        dataSource.close();

        verify(connection).close();
    }

}