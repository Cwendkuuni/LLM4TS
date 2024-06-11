import static org.junit.Assert.*;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    // Test case for getJdbcUrl method
    @Test
    public void testGetJdbcUrl() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("test_schema");
        Map<String, String> parameters = new HashMap<>();
        parameters.put("user", "root");
        parameters.put("password", "password");
        connectionConfig.setJdbcUrlParameters(parameters);

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String jdbcUrl = factory.getJdbcUrl();

        assertEquals("jdbc:mysql://localhost:3306/test_schema?user=root&password=password", jdbcUrl);
    }

    // Test case for getUsername method
    @Test
    public void testGetUsername() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("user");
        connectionConfig.setDialectType(DialectType.OB_ORACLE);

        String username = OBConsoleDataSourceFactory.getUsername(connectionConfig);

        assertEquals("\"user\"", username);
    }

    // Test case for getPassword method
    @Test
    public void testGetPassword() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setPassword("password");

        String password = OBConsoleDataSourceFactory.getPassword(connectionConfig);

        assertEquals("password", password);
    }

    // Additional test cases can be added here for other methods in OBConsoleDataSourceFactory

}