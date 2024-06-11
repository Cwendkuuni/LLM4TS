import static org.junit.Assert.*;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    // Test case for getJdbcUrl method
    @Test
    public void testGetJdbcUrl() {
        // Typical use case
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("test_schema");
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String jdbcUrl = factory.getJdbcUrl();
        assertEquals("jdbc:mysql://localhost:3306/test_schema", jdbcUrl);
        
        // Edge case
        connectionConfig.setDefaultSchema("");
        jdbcUrl = factory.getJdbcUrl();
        assertEquals("jdbc:mysql://localhost:3306", jdbcUrl);
    }

    // Test case for deepCopy method
    @Test
    public void testDeepCopy() {
        // Typical use case
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("test_schema");
        connectionConfig.setDialectType(DialectType.OB_MYSQL);
        
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        CloneableDataSourceFactory copy = factory.deepCopy();
        assertNotNull(copy);
        
        // Edge case
        connectionConfig.setDefaultSchema(null);
        copy = factory.deepCopy();
        assertNotNull(copy);
    }

    // Test case for resetParameters method
    @Test
    public void testResetParameters() {
        // Typical use case
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setJdbcUrlParameters(new HashMap<>());
        
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        factory.resetParameters((params) -> {
            params.put("test_param", "test_value");
            return params;
        });
        
        assertEquals("test_value", factory.getJdbcUrlParameters().get("test_param"));
        
        // Edge case
        factory.resetParameters((params) -> {
            return null;
        });
        assertNull(factory.getJdbcUrlParameters().get("test_param"));
    }
}