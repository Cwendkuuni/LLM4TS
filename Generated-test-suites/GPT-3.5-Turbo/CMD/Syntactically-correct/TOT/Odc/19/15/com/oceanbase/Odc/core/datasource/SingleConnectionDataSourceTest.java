import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    public void testGetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
    }

    @Test
    public void testGetConnectionInvalidUsernamePassword() {
        try {
            dataSource.getConnection("admin", "admin");
        } catch (SQLException e) {
            assertEquals("Invalid username or password", e.getMessage());
        }
    }

    @Test
    public void testResetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        dataSource.resetConnection();
        assertNull(dataSource.getConnection());
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
        assertTrue(connection.isClosed());
    }

    @Test
    public void testTryLock() {
        assertTrue(dataSource.tryLock());
    }

    @Test
    public void testGetConnectionProxy() {
        Connection connection = mock(Connection.class);
        Connection proxyConnection = dataSource.getConnectionProxy(connection);
        assertNotNull(proxyConnection);
    }

}