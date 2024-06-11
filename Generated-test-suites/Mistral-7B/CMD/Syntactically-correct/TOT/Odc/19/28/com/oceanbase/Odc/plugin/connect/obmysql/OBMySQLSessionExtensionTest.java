import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.pf4j.ExtensionManager;
import org.pf4j.PluginWrapper;
import org.pf4j.impl.DefaultExtensionPoint;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OBMySQLSessionExtensionTest {

    private ExtensionManager extensionManager;
    private PluginWrapper pluginWrapper;

    @Before
    public void setup() {
        extensionManager = mock(ExtensionManager.class);
        pluginWrapper = mock(PluginWrapper.class);
        when(extensionManager.getExtension(OBMySQLSessionExtension.class.getName())).thenReturn(pluginWrapper);
    }

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        String connectionId = "12345";

        when(pluginWrapper.getInstance()).thenReturn(sessionExtension);
        when(connection.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));

        sessionExtension.killQuery(connection, connectionId);

        Mockito.verify(connection.createStatement()).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema_success() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        String schemaName = "testSchema";

        when(pluginWrapper.getInstance()).thenReturn(sessionExtension);
        when(connection.getCatalog()).thenReturn("oldSchema");

        sessionExtension.switchSchema(connection, schemaName);

        Mockito.verify(connection).setCatalog(schemaName);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchema_failed() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        String schemaName = "testSchema";

        when(pluginWrapper.getInstance()).thenReturn(sessionExtension);
        when(connection.getCatalog()).thenThrow(new SQLException());

        sessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testgetCurrentSchema_success() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        String currentSchema = "testSchema";

        when(pluginWrapper.getInstance()).thenReturn(sessionExtension);
        when(connection.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));
        when(connection.createStatement().executeQuery("SELECT DATABASE()")).thenReturn(Mockito.mock(java.sql.ResultSet.class));

        when(connection.createStatement().executeQuery("SELECT DATABASE()").next()).thenReturn(true);
        when(connection.createStatement().executeQuery("SELECT DATABASE()").getString(1)).thenReturn(currentSchema);

        String actualSchema = sessionExtension.getCurrentSchema(connection);

        assertEquals(currentSchema, actualSchema);
    }

    @Test
    public void testgetCurrentSchema_failure() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();

        when(pluginWrapper.getInstance()).thenReturn(sessionExtension);
        when(connection.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));

        when(connection.createStatement().executeQuery("SELECT DATABASE()")).thenThrow(new SQLException());

        String actualSchema = sessionExtension.getCurrentSchema(connection);

        assertNull(actualSchema);
    }

    @Test
    public void testgetConnectionId_success() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        String connectionId = "12345";

        when(pluginWrapper.getInstance()).thenReturn(sessionExtension);
        when(connection.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));
        when(connection.createStatement().executeQuery("select connection_id()")).thenReturn(Mockito.mock(java.sql.ResultSet.class));

        when(connection.createStatement().executeQuery("select connection_id()").next()).thenReturn(true);
        when(connection.createStatement().executeQuery("select connection_id()").getString(1)).thenReturn(connectionId);

        String actualConnectionId = sessionExtension.getConnectionId(connection);

        assertEquals(connectionId, actualConnectionId);
    }

    @Test
    public void testgetConnectionId_failure() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();

        when(pluginWrapper.getInstance()).thenReturn(sessionExtension);
        when(connection.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));

        when(connection.createStatement().executeQuery("select connection_id()")).thenThrow(new SQLException());

        String actualConnectionId = sessionExtension.getConnectionId(connection);

        assertNull(actualConnectionId);
    }

    @Test
    public void testgetConnectionId_obBaseConnection() throws SQLException {
        Connection connection = Mockito.mock(OceanBaseConnection.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        String connectionId = "12345";

        when(pluginWrapper.getInstance()).thenReturn(sessionExtension);

        when(connection.getServerThreadId()).thenReturn(Long.parseLong(connectionId));

        String actualConnectionId = sessionExtension.getConnectionId(connection);

        assertEquals(connectionId, actualConnectionId);
    }

    @Test
    public void testKillQuery_obBaseConnection() throws SQLException {
        Connection connection = Mockito.mock(OceanBaseConnection.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        String connectionId = "12345";

        when(pluginWrapper.getInstance()).thenReturn(sessionExtension);

        sessionExtension.killQuery(connection, connectionId);

        Mockito.verifyNoMoreInteractions(connection);
    }

    @Test
    public void testSwitchSchema_obBaseConnection() throws SQLException {
        Connection connection = Mockito.mock(OceanBaseConnection.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        String schemaName = "testSchema";

        when(pluginWrapper.getInstance()).thenReturn(sessionExtension);

        sessionExtension.switchSchema(connection, schemaName);

        Mockito.verifyNoMoreInteractions(connection);
    }

    @Test
    public void testgetCurrentSchema_obBaseConnection() throws SQLException {
        Connection connection = Mockito.mock(OceanBaseConnection.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        String currentSchema = "testSchema";

        when(pluginWrapper.getInstance()).thenReturn(sessionExtension);

        when(connection.getSessionContext()).thenReturn(Mockito.mock(DefaultExtensionPoint.class));

        String actualSchema = sessionExtension.getCurrentSchema(connection);

        assertEquals(currentSchema, actualSchema);
    }
}