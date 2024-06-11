import static org.junit.Assert.*;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testGetJdbcUrl() {
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true);
        String jdbcUrl = factory.getJdbcUrl();
        assertNotNull(jdbcUrl);
        // Add more specific test cases for different scenarios
    }

    @Test
    public void testGetUsername() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setUsername("testuser");
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String username = factory.getUsername();
        assertNotNull(username);
        assertEquals("\"testuser\"", username);
        // Add more specific test cases for different scenarios
    }

    @Test
    public void testGetPassword() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setPassword("test123");
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String password = factory.getPassword();
        assertNotNull(password);
        assertEquals("test123", password);
        // Add more specific test cases for different scenarios
    }

    // Add more test cases for other methods in OBConsoleDataSourceFactory class

}