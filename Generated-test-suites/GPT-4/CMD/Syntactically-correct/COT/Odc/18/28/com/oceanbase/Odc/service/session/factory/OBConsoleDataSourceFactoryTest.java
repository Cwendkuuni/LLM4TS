package com.oceanbase.odc.service.session.factory;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.Before;

public class OBConsoleDataSourceFactoryTest {
    private OBConsoleDataSourceFactory obConsoleDataSourceFactory;
    private ConnectionConfig connectionConfig;

    @Before
    public void setUp() {
        connectionConfig = new ConnectionConfig();
        //Setup your connection Config class. Depends on your implementation
        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testGetUsername() {
        assertEquals("Expected username should match with the actual", obConsoleDataSourceFactory.getUsername(), "{Your Username}");
    }

    @Test
    public void testGetPassword() {
        assertEquals("Expected password should match with the actual", obConsoleDataSourceFactory.getPassword(), "{Your Password}");
    }

    @Test
    public void testGetConnectType() {
        assertEquals("Expected connectType should match with the actual", obConsoleDataSourceFactory.getConnectType(), "{Your ConnectType}");
    }

    @Test
    public void testResetUsername() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = str -> str.replace("{Old_Username}", "{New_Username}");
        obConsoleDataSourceFactory.resetUsername(mapper);
        assertEquals("Reset username should match with the new username", obConsoleDataSourceFactory.getUsername(), "{New_Username}");
    }

    @Test
    public void testResetPassword() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = str -> str.replace("{Old_Password}", "{New_Password}");
        obConsoleDataSourceFactory.resetPassword(mapper);
        assertEquals("Reset password should match with the new password", obConsoleDataSourceFactory.getPassword(), "{New_Password}");
    }

    @Test
    public void testResetHost() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = str -> str.replace("{Old_Host}", "{New_Host}");
        obConsoleDataSourceFactory.resetHost(mapper);
        assertEquals("Reset host should match with the new host", obConsoleDataSourceFactory.getHost(), "{New_Host}");
    }

    @Test
    public void testResetPort() {
        CloneableDataSourceFactory.ValueMapper<Integer> mapper = integer -> integer;
        obConsoleDataSourceFactory.resetPort(mapper);
        assertTrue("Reset port should be positive", obConsoleDataSourceFactory.getPort() > 0);
    }

    @Test
    public void testResetSchema() {
        CloneableDataSourceFactory.ValueMapper<String> mapper = str -> str.replace("{Old_Schema}", "{New_Schema}");
        obConsoleDataSourceFactory.resetSchema(mapper);
        assertEquals("Reset schema should match with the new schema", obConsoleDataSourceFactory.getSchema(), "{New_Schema}");
    }

    @Test
    public void testResetParameters() {
        CloneableDataSourceFactory.ValueMapper<Map<String, String>> mapper = map -> new HashMap<>(map);
        obConsoleDataSourceFactory.resetParameters(mapper);
        assertEquals("Reset parameters should match with the new parameters", obConsoleDataSourceFactory.getParameters(), "{Your New Parameters}");
    }

    //TODO Create all other test cases as per Your requirements.
}