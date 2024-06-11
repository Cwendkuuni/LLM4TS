import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.sql.DataSource;

import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testGetJdbcUrl() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(5432);
        connectionConfig.setDefaultSchema("test_schema");
        
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        
        String expectedUrl = "jdbc:postgresql://localhost:5432/test_schema?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=convertToNull&noDatetimeStringSync=true&jdbcCompliantTruncation=false&sendConnectionAttributes=false&useSSL=false";
        String actualUrl = factory.getJdbcUrl();
        
        assertEquals(expectedUrl, actualUrl);
    }

    @Test
    public void testGetDataSource() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(5432);
        connectionConfig.setDefaultSchema("test_schema");
        
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        
        DataSource dataSource = factory.getDataSource();
        
        assertNotNull(dataSource);
    }

    @Test
    public void testGetDialectType() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MYSQL);
        
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        
        DialectType expectedDialectType = DialectType.MYSQL;
        DialectType actualDialectType = factory.getDialectType();
        
        assertEquals(expectedDialectType, actualDialectType);
    }

    @Test
    public void testDeepCopy() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(5432);
        connectionConfig.setDefaultSchema("test_schema");
        
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        
        CloneableDataSourceFactory copiedFactory = factory.deepCopy();
        
        assertNotNull(copiedFactory);
        assertNotSame(factory, copiedFactory);
    }

    @Test
    public void testResetUsername() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("user");
        
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        
        factory.resetUsername(value -> "new_user");
        
        assertEquals("new_user", factory.getUsername());
    }

    @Test
    public void testResetPassword() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setPassword("password");
        
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        
        factory.resetPassword(value -> "new_password");
        
        assertEquals("new_password", factory.getPassword());
    }

    @Test
    public void testResetHost() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        
        factory.resetHost(value -> "new_host");
        
        assertEquals("new_host", factory.getHost());
    }

    @Test
    public void testResetPort() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setPort(5432);
        
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        
        factory.resetPort(value -> 3306);
        
        assertEquals(Integer.valueOf(3306), factory.getPort());
    }

    @Test
    public void testResetSchema() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDefaultSchema("test_schema");
        
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        
        factory.resetSchema(value -> "new_schema");
        
        assertEquals("new_schema", factory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        Map<String, String> params = new HashMap<>();
        params.put("param1", "value1");
        connectionConfig.setJdbcUrlParameters(params);
        
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        
        Map<String, String> newParams = new HashMap<>();
        newParams.put("param2", "value2");
        
        factory.resetParameters(value -> newParams);
        
        assertEquals(newParams, factory.getJdbcParams());
    }

}