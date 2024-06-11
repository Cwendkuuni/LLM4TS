import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testgetJdbcUrl() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(1521);
        connectionConfig.setDefaultSchema("test");
        Map<String, String> jdbcParams = new HashMap<>();
        jdbcParams.put("param1", "value1");
        connectionConfig.setJdbcUrlParameters(jdbcParams);
        
        OBConsoleDataSourceFactory obFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

        String expectedUrl = "jdbc:oracle:thin:@//localhost:1521/test?param1=value1";
        assertEquals(expectedUrl, obFactory.getJdbcUrl());
    }

    @Test
    public void testgetUsername() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setUsername("admin");
        
        OBConsoleDataSourceFactory obFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

        String expectedUsername = "\"admin\"";
        assertEquals(expectedUsername, obFactory.getUsername());
    }

    @Test
    public void testgetDataSource() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(1521);
        connectionConfig.setDefaultSchema("test");
        Map<String, String> jdbcParams = new HashMap<>();
        jdbcParams.put("param1", "value1");
        connectionConfig.setJdbcUrlParameters(jdbcParams);

        OBConsoleDataSourceFactory obFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        DataSource dataSource = obFactory.getDataSource();
        
        assertNotNull(dataSource);
    }

    @Test
    public void testResetUsername() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setUsername("admin");
        
        OBConsoleDataSourceFactory obFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        obFactory.resetUsername(s -> s.toUpperCase());
        
        assertEquals("ADMIN", obFactory.getUsername());
    }

    @Test
    public void testResetPort() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setPort(1521);
        
        OBConsoleDataSourceFactory obFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        obFactory.resetPort(p -> p*2);
        
        assertEquals(3042, obFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setDefaultSchema("test");
        
        OBConsoleDataSourceFactory obFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        obFactory.resetSchema(s -> s.toUpperCase());
        
        assertEquals("TEST", obFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        Map<String, String> jdbcParams = new HashMap<>();
        jdbcParams.put("param1", "value1");
        connectionConfig.setJdbcUrlParameters(jdbcParams);
        
        OBConsoleDataSourceFactory obFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
        Map<String, String> newParams = new HashMap<>();
        newParams.put("param2", "value2");
        obFactory.resetParameters(p -> newParams);
        
        assertEquals(newParams, obFactory.getJdbcParams());
    }
}