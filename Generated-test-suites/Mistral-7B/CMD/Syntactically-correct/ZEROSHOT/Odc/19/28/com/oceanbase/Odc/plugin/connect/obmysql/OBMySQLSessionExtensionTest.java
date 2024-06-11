import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.pf4j.ExtensionManager;
import org.pf4j.PluginWrapper;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(PowerMockRunner.class)
public class OBMySQLSessionExtensionTest {

    private ExtensionManager extensionManager;
    private PluginWrapper pluginWrapper;
    private OBMySQLSessionExtension obMySQLSessionExtension;

    @Before
    public void setUp() throws Exception {
        extensionManager = PowerMockito.mock(ExtensionManager.class);
        pluginWrapper = PowerMockito.mock(PluginWrapper.class);
        obMySQLSessionExtension = new OBMySQLSessionExtension();
        PowerMockito.when(extensionManager.getExtension(PluginWrapper.class, "com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension"))
                .thenReturn(pluginWrapper);
        PowerMockito.when(pluginWrapper.getInstances(SessionExtensionPoint.class))
                .thenReturn(new SessionExtensionPoint[]{obMySQLSessionExtension});
    }

    @After
    public void tearDown() throws Exception {
        PowerMockito.resetAll();
    }

    @Test
    public void testKillQuery() throws Exception {
        Connection connection = DriverManager.getConnection("jdbc:oceanbase:observer:127.0.0.1:2181/oceanbase:1521");
        String connectionId = "123";
        obMySQLSessionExtension.killQuery(connection, connectionId);
        // Verify the query execution
        Mockito.verify(connection, Mockito.times(1)).createStatement();
        Mockito.verify(connection, Mockito.times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws Exception {
        Connection connection = DriverManager.getConnection("jdbc:oceanbase:observer:127.0.0.1:2181/oceanbase:1521");
        String schemaName = "testSchema";
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
        assertEquals(schemaName, currentSchema);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithInvalidSchema() throws Exception {
        Connection connection = DriverManager.getConnection("jdbc:oceanbase:observer:127.0.0.1:2181/oceanbase:1521");
        String schemaName = null;
        obMySQLSessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws Exception {
        Connection connection = DriverManager.getConnection("jdbc:oceanbase:observer:127.0.0.1:2181/oceanbase:1521");
        String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
        assertNotNull(currentSchema);
        assertEquals("oceanbase", currentSchema);
    }

    @Test
    public void testGetConnectionId() throws Exception {
        Connection connection = DriverManager.getConnection("jdbc:oceanbase:observer:127.0.0.1:2181/oceanbase:1521");
        String connectionId = obMySQLSessionExtension.getConnectionId(connection);
        assertNull(connectionId);

        connection = DriverManager.getConnection("jdbc:oceanbase:observer:127.0.0.1:2181/oceanbase:1521", new Properties() {{
            setProperty("ob.serverThreadId", "123");
        }});
        connectionId = obMySQLSessionExtension.getConnectionId(connection);
        assertEquals("123", connectionId);

        connection = (Connection) PowerMockito.mock(OceanBaseConnection.class);
        Mockito.when(connection.getServerThreadId()).thenReturn(123);
        connectionId = obMySQLSessionExtension.getConnectionId(connection);
        assertEquals("123", connectionId);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionIdWithNullConnection() throws Exception {
        obMySQLSessionExtension.getConnectionId(null);
    }
}