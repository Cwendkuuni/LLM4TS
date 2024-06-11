import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pf4j.ExtensionFactory;
import org.pf4j.PluginWrapper;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension obOracleSessionExtension;
    private ExtensionFactory extensionFactory;
    private PluginWrapper pluginWrapper;
    private Connection connection;

    @Before
    public void setUp() throws Exception {
        extensionFactory = mock(ExtensionFactory.class);
        pluginWrapper = mock(PluginWrapper.class);
        when(pluginWrapper.getExtensionPoint(SessionExtensionPoint.class)).thenReturn(obOracleSessionExtension = new OBOracleSessionExtension());
        when(extensionFactory.getPluginWrapper(OBOracleSessionExtension.class)).thenReturn(pluginWrapper);
        connection = mock(Connection.class);
    }

    @After
    public void tearDown() throws Exception {
        obOracleSessionExtension = null;
        extensionFactory = null;
        pluginWrapper = null;
        connection = null;
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "12345";
        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));
        obOracleSessionExtension.killQuery(connection, connectionId);
        PowerMockito.verifyStatic();
        JdbcOperationsUtil.getJdbcOperations(connection).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testSchema";
        String currentSchema = "oldSchema";
        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));

        obOracleSessionExtension.switchSchema(connection, schemaName);
        PowerMockito.verifyStatic();
        JdbcOperationsUtil.getJdbcOperations(connection).update("ALTER SESSION SET CURRENT_SCHEMA=testSchema");

        String newSchema = obOracleSessionExtension.getCurrentSchema(connection);
        assertEquals(schemaName, newSchema);
    }

    @Test
    public void testGetCurrentSchema() {
        String schemaName = "testSchema";
        String queryResult = "testSchema";
        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));
        when(connection.createStatement().executeQuery(StringUtils.quoteOracleIdentifier("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL"))).thenReturn(mock(java.sql.ResultSet.class));
        when(connection.createStatement().next()).thenReturn(true);
        when(connection.createStatement().getString(1)).thenReturn(queryResult);

        String newSchema = obOracleSessionExtension.getCurrentSchema(connection);
        assertEquals(schemaName, newSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String connectionId = "12345";
        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));
        when(connection.createStatement().executeQuery(StringUtils.quoteOracleIdentifier("select userenv('sessionid') from dual"))).thenReturn(mock(java.sql.ResultSet.class));
        when(connection.createStatement().next()).thenReturn(true);
        when(connection.createStatement().getString(1)).thenReturn(connectionId);

        String newConnectionId = obOracleSessionExtension.getConnectionId(connection);
        assertEquals(connectionId, newConnectionId);
    }

    @Test
    public void testGetConnectionIdWhenNoConnectionId() throws SQLException {
        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));
        when(connection.createStatement().executeQuery(StringUtils.quoteOracleIdentifier("select userenv('sessionid') from dual"))).thenReturn(null);

        String newConnectionId = obOracleSessionExtension.getConnectionId(connection);
        assertNull(newConnectionId);
    }

    @Test
    public void testGetConnectionIdWhenOceanBaseConnection() throws SQLException {
        String serverThreadId = "67890";
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));

        String newConnectionId = obOracleSessionExtension.getConnectionId(connection);
        assertEquals(serverThreadId, newConnectionId);
    }

}