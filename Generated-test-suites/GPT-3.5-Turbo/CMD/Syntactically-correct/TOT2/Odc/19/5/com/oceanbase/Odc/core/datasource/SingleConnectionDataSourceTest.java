import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;

    @Before
    public void setup() {
        dataSource = new SingleConnectionDataSource();
        dataSource.setUsername("username");
        dataSource.setPassword("password");
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
    }

    @Test
    public void testGetConnectionWithInvalidCredentials() {
        assertThrows(SQLException.class, () -> dataSource.getConnection("invalid", "credentials"));
    }

    @Test
    public void testResetConnection() {
        assertDoesNotThrow(() -> dataSource.resetConnection());
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        assertTrue(dataSource.getInitializerList().contains(initializer));
    }

    @Test
    public void testClose() {
        assertDoesNotThrow(() -> dataSource.close());
    }

    @Test
    public void testCreateConnectionProxy() {
        Connection connection = mock(Connection.class);
        Connection proxy = dataSource.getConnectionProxy(connection);
        assertNotNull(proxy);
    }

    @Test
    public void testInitConnection() {
        Connection connection = mock(Connection.class);
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        
        dataSource.addInitializer(initializer);
        assertDoesNotThrow(() -> dataSource.initConnection(connection));
    }

}