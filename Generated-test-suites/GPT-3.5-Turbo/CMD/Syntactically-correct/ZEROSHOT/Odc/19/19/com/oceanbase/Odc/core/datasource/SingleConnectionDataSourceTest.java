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
    public void testGetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        assertTrue(connection.isValid(0));
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        Connection connection = dataSource.getConnection("username", "password");
        assertNotNull(connection);
        assertTrue(connection.isValid(0));
    }

    @Test
    public void testResetConnection() {
        assertDoesNotThrow(() -> dataSource.resetConnection());
    }

    @Test
    public void testClose() {
        assertDoesNotThrow(() -> dataSource.close());
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        assertEquals(1, dataSource.getInitializerList().size());
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        dataSource.setAutoCommit(true);
        dataSource.prepareConnection(connection);
        verify(connection, times(1)).setAutoCommit(true);
    }

}