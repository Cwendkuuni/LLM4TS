import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory obConsoleDataSourceFactory;

    @Before
    public void setUp() {
        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true);
    }

    @Test
    public void testGetJdbcUrl() {
        String jdbcUrl = obConsoleDataSourceFactory.getJdbcUrl();
        assertNotNull(jdbcUrl);
    }

    @Test
    public void testGetUsername() {
        String username = obConsoleDataSourceFactory.getUsername();
        assertNotNull(username);
    }

    @Test
    public void testGetPassword() {
        String password = obConsoleDataSourceFactory.getPassword();
        assertNotNull(password);
    }

    @Test
    public void testGetConnectType() {
        ConnectType connectType = obConsoleDataSourceFactory.getConnectType();
        assertNotNull(connectType);
    }

    @Test
    public void testGetDialectType() {
        DialectType dialectType = obConsoleDataSourceFactory.getDialectType();
        assertNotNull(dialectType);
    }

    @Test
    public void testDeepCopy() {
        CloneableDataSourceFactory cloneableDataSourceFactory = obConsoleDataSourceFactory.deepCopy();
        assertNotNull(cloneableDataSourceFactory);
    }

    @Test
    public void testResetUsername() {
        obConsoleDataSourceFactory.resetUsername(value -> "testUser");
        assertEquals("testUser", obConsoleDataSourceFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        obConsoleDataSourceFactory.resetPassword(value -> "testPassword");
        assertEquals("testPassword", obConsoleDataSourceFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        obConsoleDataSourceFactory.resetHost(value -> "localhost");
        assertEquals("localhost", obConsoleDataSourceFactory.getHost());
    }

    @Test
    public void testResetPort() {
        obConsoleDataSourceFactory.resetPort(value -> 3306);
        assertEquals(Integer.valueOf(3306), obConsoleDataSourceFactory.getPort());
    }

    @Test
    public void testResetSchema() {
        obConsoleDataSourceFactory.resetSchema(value -> "test_schema");
        assertEquals("test_schema", obConsoleDataSourceFactory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        obConsoleDataSourceFactory.resetParameters(value -> {
            Map<String, String> params = new HashMap<>();
            params.put("param1", "value1");
            return params;
        });
        assertEquals("value1", obConsoleDataSourceFactory.getJdbcParams().get("param1"));
    }
}