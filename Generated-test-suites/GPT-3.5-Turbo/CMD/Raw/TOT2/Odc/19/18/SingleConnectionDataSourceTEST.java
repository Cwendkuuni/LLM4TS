import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Before;
import org.junit.Test;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource singleConnectionDataSource;

    @Before
    public void setUp() {
        singleConnectionDataSource = new SingleConnectionDataSource();
    }

    @Test
    public void testGetConnectionWhenConnectionIsNull() throws SQLException {
        Connection connection = singleConnectionDataSource.getConnection();
        assertNotNull(connection);
    }

    @Test
    public void testGetConnectionWhenConnectionIsClosedAndAutoReconnectIsFalse() {
        singleConnectionDataSource.setAutoReconnect(false);
        assertThrows(SQLException.class, () -> {
            Connection connection = singleConnectionDataSource.getConnection();
        });
    }

    @Test
    public void testGetConnectionWhenInvalidUsernameOrPassword() {
        assertThrows(SQLException.class, () -> {
            Connection connection = singleConnectionDataSource.getConnection("username", "password");
        });
    }

    @Test
    public void testResetConnection() throws SQLException {
        singleConnectionDataSource.resetConnection();
        assertNull(singleConnectionDataSource.getConnection());
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        singleConnectionDataSource.addInitializer(initializer);
        assertTrue(singleConnectionDataSource.getInitializerList().contains(initializer));
    }

    @Test
    public void testCloseConnection() throws SQLException {
        Connection connection = singleConnectionDataSource.getConnection();
        singleConnectionDataSource.close();
        assertTrue(connection.isClosed());
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        singleConnectionDataSource.prepareConnection(connection);
        verify(connection).setAutoCommit(singleConnectionDataSource.getAutoCommit());
    }

    /**
     * Additional test cases for edge cases and exception handling scenarios can be added here
     */

}