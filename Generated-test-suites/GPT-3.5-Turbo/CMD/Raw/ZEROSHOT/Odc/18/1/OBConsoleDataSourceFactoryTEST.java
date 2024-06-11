import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testGetDataSource() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        
        DataSource dataSource = factory.getDataSource();
        
        assertNotNull(dataSource);
    }

    @Test
    public void testGetJdbcUrl() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        
        String jdbcUrl = factory.getJdbcUrl();
        
        assertNotNull(jdbcUrl);
    }

    @Test
    public void testGetDialectType() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        
        DialectType dialectType = factory.getDialectType();
        
        assertNotNull(dialectType);
    }

    @Test
    public void testDeepCopy() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        
        CloneableDataSourceFactory copiedFactory = factory.deepCopy();
        
        assertNotNull(copiedFactory);
    }

    @Test
    public void testResetUsername() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        
        factory.resetUsername(value -> "newUsername");
        
        assertEquals("newUsername", factory.getUsername());
    }

    @Test
    public void testResetPassword() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        
        factory.resetPassword(value -> "newPassword");
        
        assertEquals("newPassword", factory.getPassword());
    }

    @Test
    public void testResetHost() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        
        factory.resetHost(value -> "newHost");
        
        assertEquals("newHost", factory.getUsername());
    }

    @Test
    public void testResetPort() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        
        factory.resetPort(value -> 3306);
        
        assertEquals(3306, factory.getPort().intValue());
    }

    @Test
    public void testResetSchema() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        
        factory.resetSchema(value -> "newSchema");
        
        assertEquals("newSchema", factory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        
        factory.resetParameters(value -> {
            Map<String, String> params = new HashMap<>();
            params.put("key1", "value1");
            return params;
        });
        
        assertEquals("value1", factory.getParameters().get("key1"));
    }

}