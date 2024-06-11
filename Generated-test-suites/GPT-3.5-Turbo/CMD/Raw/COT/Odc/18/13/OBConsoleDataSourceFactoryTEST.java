
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory obFactory;

    @Before
    public void setUp() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setHost("localhost");
        connectionConfig.setPort(3306);
        connectionConfig.setUsername("user1");
        connectionConfig.setPassword("password1");
        connectionConfig.setDefaultSchema("schema1");

        obFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testGetJdbcUrl() {
        String expectedUrl = "jdbc:mysql://localhost:3306/schema1?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=convertToNull&noDatetimeStringSync=true&jdbcCompliantTruncation=false&sendConnectionAttributes=false";
        assertEquals(expectedUrl, obFactory.getJdbcUrl());
    }

    @Test
    public void testGetUsername() {
        String expectedUsername = "user1";
        assertEquals(expectedUsername, OBConsoleDataSourceFactory.getUsername(obFactory.connectionConfig));
    }

    @Test
    public void testGetPassword() {
        String expectedPassword = "password1";
        assertEquals(expectedPassword, OBConsoleDataSourceFactory.getPassword(obFactory.connectionConfig));
    }

    @Test
    public void testGetJdbcParams() {
        String expectedValue = "64000000";
        assertEquals(expectedValue, OBConsoleDataSourceFactory.getJdbcParams(obFactory.connectionConfig).get("maxAllowedPacket"));
    }

    @Test
    public void testDefaultSchema() {
        String expectedSchema = "schema1";
        assertEquals(expectedSchema, OBConsoleDataSourceFactory.getDefaultSchema(obFactory.connectionConfig));
    }

    @Test
    public void testGetDataSource() {
        assertNotNull(obFactory.getDataSource());
    }

    @Test
    public void testGetDialectType() {
        assertNotNull(obFactory.getDialectType());
    }

    @Test
    public void testDeepCopy() {
        assertNotNull(obFactory.deepCopy());
    }

    @Test
    public void testResetUsername() {
        obFactory.resetUsername((username) -> "newUser");
        assertEquals("newUser", obFactory.getUsername());
    }

    @Test
    public void testResetPassword() {
        obFactory.resetPassword((password) -> "newPassword");
        assertEquals("newPassword", obFactory.getPassword());
    }

    @Test
    public void testResetHost() {
        obFactory.resetHost((host) -> "newHost");
        assertEquals("newHost", obFactory.connectionConfig.getHost());
    }

    @Test
    public void testResetPort() {
        obFactory.resetPort((port) -> 8080);
        assertEquals(8080, (int) obFactory.connectionConfig.getPort());
    }

    @Test
    public void testResetSchema() {
        obFactory.resetSchema((schema) -> "newSchema");
        assertEquals("newSchema", obFactory.connectionConfig.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        obFactory.resetParameters((params) -> new HashMap<>());
        assertEquals(0, obFactory.parameters.size());
    }

    @Test
    public void testGetConnectType() {
        assertNotNull(obFactory.getConnectType());
    }
}
