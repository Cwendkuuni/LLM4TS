import static org.junit.Assert.*;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    // Test case for getJdbcUrl method
    @Test
    public void testGetJdbcUrl() {
        // Typical use case
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(1521);
        connectionConfig.setDefaultSchema("test_schema");

        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String expectedUrl = "jdbc:oracle://localhost:1521/test_schema";
        assertEquals(expectedUrl, dataSourceFactory.getJdbcUrl());

        // Edge case
        connectionConfig.setDefaultSchema(null);
        expectedUrl = "jdbc:oracle://localhost:1521/user";
        assertEquals(expectedUrl, dataSourceFactory.getJdbcUrl());
    }

    // Test case for getUsername method
    @Test
    public void testGetUsername() {
        // Typical use case
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("test_user");

        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String expectedUsername = "test_user";
        assertEquals(expectedUsername, dataSourceFactory.getUsername());

        // Edge case
        connectionConfig.setOBTenantName("tenant1");
        expectedUsername = "test_user@tenant1";
        assertEquals(expectedUsername, dataSourceFactory.getUsername());
    }

    // Test case for getPassword method
    @Test
    public void testGetPassword() {
        // Typical use case
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setPassword("test_password");

        OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String expectedPassword = "test_password";
        assertEquals(expectedPassword, dataSourceFactory.getPassword());
    }

    // Add more test cases for other methods...

}