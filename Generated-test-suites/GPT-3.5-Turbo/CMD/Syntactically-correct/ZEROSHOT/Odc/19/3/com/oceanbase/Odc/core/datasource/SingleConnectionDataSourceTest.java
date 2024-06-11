import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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
        assertFalse(connection.isClosed());
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        String username = "user";
        String password = "pass";
        Connection connection = dataSource.getConnection(username, password);
        assertNotNull(connection);
        assertFalse(connection.isClosed());
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.resetConnection();
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        assertFalse(connection.isClosed());
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        dataSource.resetConnection(); // Call reset to invoke initializer
        verify(initializer).init(dataSource.getConnection());
    }

    @Test
    public void testClose() throws SQLException {
        Connection connection = dataSource.getConnection();
        dataSource.close();
        assertTrue(connection.isClosed());
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        dataSource.setAutoCommit(true);
        dataSource.prepareConnection(connection);
        assertTrue(connection.getAutoCommit());
    }

    @Test
    public void testInnerCreateConnection() {
        try {
            Connection connection = dataSource.getConnection();
            assertNotNull(connection);
            assertFalse(connection.isClosed());
        } catch (SQLException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

}