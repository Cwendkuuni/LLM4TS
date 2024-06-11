import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
    }

    @Test
    public void testGetConnection_validConnection() {
        assertNotNull(dataSource.getConnection());
    }

    @Test
    public void testGetConnection_autoReconnectTrue() {
        dataSource.setAutoReconnect(true);
        assertNotNull(dataSource.getConnection());
    }

    @Test
    public void testGetConnection_invalidConnection() {
        dataSource.setConnection(null);
        assertThrows(SQLException.class, () -> dataSource.getConnection());
    }

    @Test
    public void testGetConnectionByUsernamePassword_validCredentials() {
        String username = "user";
        String password = "password";
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        assertNotNull(dataSource.getConnection(username, password));
    }

    @Test
    public void testGetConnectionByUsernamePassword_invalidCredentials() {
        String username = "user";
        String password = "password";
        String invalidUsername = "invalidUser";
        String invalidPassword = "invalidPassword";
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        assertThrows(SQLException.class, () -> dataSource.getConnection(invalidUsername, invalidPassword));
    }

    @Test
    public void testResetConnection() throws SQLException {
        Connection mockedConnection = mock(Connection.class);
        dataSource.setConnection(mockedConnection);

        dataSource.resetConnection();

        assertNull(dataSource.getConnection());
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);

        dataSource.addInitializer(initializer);

        assertEquals(1, dataSource.getInitializerList().size());
    }
}