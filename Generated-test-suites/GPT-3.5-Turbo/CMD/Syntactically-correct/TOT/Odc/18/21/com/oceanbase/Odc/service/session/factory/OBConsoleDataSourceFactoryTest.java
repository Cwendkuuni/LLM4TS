import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory obConsoleDataSourceFactory;

    @Before
    public void setUp() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.OB_ORACLE);
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("admin");
        connectionConfig.setPassword("admin123");
        connectionConfig.setDefaultSchema("test_schema");

        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testGetJdbcUrl() {
        String expectedJdbcUrl = "jdbc:oracle:thin:@localhost:3306/test_schema";
        assertEquals(expectedJdbcUrl, obConsoleDataSourceFactory.getJdbcUrl());
    }

    @Test
    public void testGetDataSource() {
        assertNotNull(obConsoleDataSourceFactory.getDataSource());
    }

    @Test
    public void testGetDialectType() {
        assertEquals(DialectType.OB_ORACLE, obConsoleDataSourceFactory.getDialectType());
    }

    @Test
    public void testDeepCopy() {
        CloneableDataSourceFactory copiedFactory = obConsoleDataSourceFactory.deepCopy();
        assertNotSame(obConsoleDataSourceFactory, copiedFactory);
        assertEquals(obConsoleDataSourceFactory.getJdbcUrl(), copiedFactory.getJdbcUrl());
    }

    @Test
    public void testResetUsername() {
        obConsoleDataSourceFactory.resetUsername(s -> "new_admin");
        assertEquals("new_admin", obConsoleDataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        obConsoleDataSourceFactory.resetPassword(s -> "new_password");
        assertEquals("new_password", obConsoleDataSourceFactory.getPassword());
    }

    @Test
    public void testResetPort() {
        obConsoleDataSourceFactory.resetPort(i -> 8080);
        assertEquals(Integer.valueOf(8080), obConsoleDataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        obConsoleDataSourceFactory.resetSchema(s -> "new_schema");
        assertEquals("new_schema", obConsoleDataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        obConsoleDataSourceFactory.resetParameters(m -> {
            Map<String, String> newParams = new HashMap<>();
            newParams.put("param1", "value1");
            newParams.put("param2", "value2");
            return newParams;
        });
        assertEquals("value1", obConsoleDataSourceFactory.getJdbcParams().get("param1"));
        assertEquals("value2", obConsoleDataSourceFactory.getJdbcParams().get("param2"));
    }

    @Test
    public void testGetConnectType() {
        assertEquals(ConnectType.DEFAULT, obConsoleDataSourceFactory.getConnectType());
    }
}