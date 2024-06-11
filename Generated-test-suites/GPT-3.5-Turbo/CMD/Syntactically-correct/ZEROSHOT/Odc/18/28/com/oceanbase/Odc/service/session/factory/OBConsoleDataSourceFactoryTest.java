import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory dataSourceFactory;

    @Before
    public void setUp() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("user");
        connectionConfig.setPassword("password");
        connectionConfig.setDialectType(DialectType.MYSQL);

        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = dataSourceFactory.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = dataSourceFactory.getJdbcUrl();
        assertNotNull(jdbcUrl);
        assertTrue(jdbcUrl.startsWith("jdbc:mysql://localhost:3306"));
    }

    @Test
    public void testGetUsername() {
        assertEquals("user", dataSourceFactory.getUsername());
    }

    @Test
    public void testGetPassword() {
        assertEquals("password", dataSourceFactory.getPassword());
    }

    @Test
    public void testGetConnectType() {
        assertEquals(ConnectType.DEFAULT, dataSourceFactory.getConnectType());
    }

    @Test
    public void testGetDialectType() {
        assertEquals(DialectType.MYSQL, dataSourceFactory.getDialectType());
    }

    @Test
    public void testGetDefaultSchema() {
        assertEquals("default_schema", dataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetUsername() {
        dataSourceFactory.resetUsername(s -> "new_user");
        assertEquals("new_user", dataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        dataSourceFactory.resetPassword(s -> "new_password");
        assertEquals("new_password", dataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        dataSourceFactory.resetHost(s -> "new_host");
        assertEquals("new_host", dataSourceFactory.getHost());
    }

    @Test
    public void testResetPort() {
        dataSourceFactory.resetPort(i -> 1234);
        assertEquals(1234, dataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        dataSourceFactory.resetSchema(s -> "new_schema");
        assertEquals("new_schema", dataSourceFactory.getSchema());
    }

    @Test
    public void testResetParameters() {
        Map<String, String> params = new HashMap<>();
        params.put("param1", "value1");
        dataSourceFactory.resetParameters(s -> params);

        assertEquals(params, dataSourceFactory.getParameters());
    }

}