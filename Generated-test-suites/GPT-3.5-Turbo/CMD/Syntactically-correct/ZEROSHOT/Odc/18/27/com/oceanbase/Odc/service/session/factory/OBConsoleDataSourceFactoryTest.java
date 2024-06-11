import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory dataSourceFactory;
    private ConnectionConfig connectionConfig;
    
    @Before
    public void setup() {
        connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(1521);
        connectionConfig.setUsername("user");
        connectionConfig.setPassword("password");
        
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }
    
    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = dataSourceFactory.getJdbcUrl();
        assertEquals("jdbc:oracle://localhost:1521/default", jdbcUrl);
    }
    
    @Test
    public void testGetDataSource() {
        DataSource dataSource = dataSourceFactory.getDataSource();
        assertNotNull(dataSource);
    }
    
    @Test
    public void testGetDialectType() {
        assertEquals(DialectType.OB_ORACLE, dataSourceFactory.getDialectType());
    }
    
    @Test
    public void testDeepCopy() {
        CloneableDataSourceFactory clone = dataSourceFactory.deepCopy();
        assertNotNull(clone);
        assertNotSame(dataSourceFactory, clone);
    }
    
    @Test
    public void testResetUsername() {
        dataSourceFactory.resetUsername(value -> "newUser");
        assertEquals("newUser", dataSourceFactory.getUsername());
    }
    
    @Test
    public void testResetPassword() {
        dataSourceFactory.resetPassword(value -> "newPassword");
        assertEquals("newPassword", dataSourceFactory.getPassword());
    }
    
    @Test
    public void testResetHost() {
        dataSourceFactory.resetHost(value -> "newHost");
        assertEquals("newHost", dataSourceFactory.getConnectionConfig().getHost());
    }
    
    @Test
    public void testResetPort() {
        dataSourceFactory.resetPort(value -> 3306);
        assertEquals(3306, dataSourceFactory.getConnectionConfig().getPort());
    }
    
    @Test
    public void testResetSchema() {
        dataSourceFactory.resetSchema(value -> "newSchema");
        assertEquals("newSchema", dataSourceFactory.getDefaultSchema());
    }
    
    @Test
    public void testResetParameters() {
        dataSourceFactory.resetParameters(value -> {
            Map<String, String> map = new HashMap<>();
            map.put("key1", "value1");
            return map;
        });
        assertEquals("value1", dataSourceFactory.getJdbcParams().get("key1"));
    }
}