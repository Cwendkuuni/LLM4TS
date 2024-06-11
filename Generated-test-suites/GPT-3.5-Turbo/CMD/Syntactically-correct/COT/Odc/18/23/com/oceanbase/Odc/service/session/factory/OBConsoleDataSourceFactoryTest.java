
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    @Test
    public void testGetJdbcUrl() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDialectType(DialectType.OB_MYSQL);

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        String jdbcUrl = factory.getJdbcUrl();

        assertNotNull(jdbcUrl);
        assertTrue(jdbcUrl.startsWith("jdbc:mysql://"));
        assertTrue(jdbcUrl.contains("localhost"));
        assertTrue(jdbcUrl.contains("3306"));
    }

    @Test
    public void testGetUsername() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setUsername("root");
        connectionConfig.setDialectType(DialectType.OB_MYSQL);

        String username = OBConsoleDataSourceFactory.getUsername(connectionConfig);

        assertNotNull(username);
        assertEquals("root", username);
    }

    // Add more test cases for other public methods in OBConsoleDataSourceFactory class

    @Test
    public void testResetPort() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDialectType(DialectType.OB_MYSQL);

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        factory.resetPort(p -> p + 1);

        assertEquals(3307, factory.getPort().intValue());
    }

    @Test
    public void testResetSchema() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDefaultSchema("schema");
        connectionConfig.setDialectType(DialectType.OB_MYSQL);

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        factory.resetSchema(s -> s + "_new");

        assertEquals("schema_new", factory.getDefaultSchema());
    }

    // Add test cases for edge cases and exception handling scenarios

    @Test(expected = IllegalArgumentException.class)
    public void testResetPortNegative() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setDialectType(DialectType.OB_MYSQL);

        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
        factory.resetPort(p -> -1);
    }

}
