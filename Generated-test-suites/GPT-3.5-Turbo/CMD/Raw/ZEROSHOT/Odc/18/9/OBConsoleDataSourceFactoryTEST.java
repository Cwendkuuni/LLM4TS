import static org.junit.Assert.*;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testGetUsername() {
        // Given
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("testUser");
        
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        
        // When
        String username = factory.getUsername();
        
        // Then
        assertEquals("testUser", username);
    }
    
    @Test
    public void testGetJdbcParams() {
        // Given
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        
        // When
        Map<String, String> jdbcParams = factory.getJdbcParams(connectionConfig);
        
        // Then
        assertNotNull(jdbcParams);
        assertEquals("64000000", jdbcParams.get("maxAllowedPacket"));
        assertEquals("true", jdbcParams.get("allowMultiQueries"));
        assertEquals("5000", jdbcParams.get("connectTimeout"));
    }
    
    @Test
    public void testGetDataSource() {
        // Given
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        
        // When
        DataSource dataSource = factory.getDataSource();
        
        // Then
        assertNotNull(dataSource);
    }

    // Add more test cases as needed
    
}