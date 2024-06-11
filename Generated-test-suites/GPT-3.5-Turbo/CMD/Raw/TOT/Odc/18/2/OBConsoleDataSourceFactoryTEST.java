import static org.junit.Assert.*;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testGetJdbcUrl() {
        // Typical Use Case
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("test_schema");
        Map<String, String> jdbcParams = new HashMap<>();
        jdbcParams.put("testParam", "true");
        connectionConfig.setJdbcUrlParameters(jdbcParams);
        
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String expectedJdbcUrl = "jdbc:mysql://localhost:3306/test_schema?testParam=true";
        assertEquals(expectedJdbcUrl, factory.getJdbcUrl());
        
        // Edge Case
        connectionConfig.setDefaultSchema(null);
        jdbcParams.put("testParam", null);
        
        expectedJdbcUrl = "jdbc:mysql://localhost:3306/?testParam=null";
        assertEquals(expectedJdbcUrl, factory.getJdbcUrl());
        
        // Error Scenario
        connectionConfig.setHost("");
        expectedException.expect(IllegalArgumentException.class);
        factory.getJdbcUrl();
    }

    @Test
    public void testGetDataSource() {
        // Typical Use Case
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDefaultSchema("test_schema");
        
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        DataSource dataSource = factory.getDataSource();
        assertNotNull(dataSource);
        
        // Edge Case
        connectionConfig.setDefaultSchema(null);
        
        factory.resetSchema((value) -> "test_schema");
        dataSource = factory.getDataSource();
        assertNotNull(dataSource);
        
        // Error Scenario
        connectionConfig.setHost("");
        expectedException.expect(IllegalArgumentException.class);
        factory.getDataSource();
    }

    @Test
    public void testGetDialectType() {
        // Typical Use Case
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        assertEquals(DialectType.OB_ORACLE, factory.getDialectType());
        
        // Edge Case
        connectionConfig.setDialectType(null);
        factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        assertNull(factory.getDialectType());
        
        // Error Scenario
        connectionConfig.setDialectType(null);
        expectedException.expect(NullPointerException.class);
        factory.getDialectType();
    }

    // Add more test cases for each method as required

}