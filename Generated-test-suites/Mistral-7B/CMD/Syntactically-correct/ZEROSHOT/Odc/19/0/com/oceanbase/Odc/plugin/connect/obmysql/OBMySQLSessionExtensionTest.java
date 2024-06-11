package com.oceanbase.odc.plugin.connect.obmysql.test;

import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pf4j.ExtensionManager;
import org.pf4j.PluginManager;
import org.pf4j.TestExtension;
import org.pf4j.TestPluginManager;
import org.pf4j.extension.api.ExtensionPoint;
import org.pf4j.test.JUnit4JarRunner;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(JUnit4JarRunner.class)
public class OBMySQLSessionExtensionTest {

    private ExtensionManager extensionManager;
    private SessionExtensionPoint sessionExtension;

    @Before
    public void setup() {
        PluginManager pluginManager = new TestPluginManager();
        extensionManager = new ExtensionManager(pluginManager);
        sessionExtension = new OBMySQLSessionExtension();
        extensionManager.addExtension(sessionExtension);
    }

    @After
    public void teardown() {
        extensionManager.clearExtensions();
    }

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "mock_connection_id";

        doNothing().when(connection).createStatement();
        doNothing().when(connection).execute(anyString());

        sessionExtension.killQuery(connection, connectionId);

        verify(connection).createStatement();
        verify(connection).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws Exception {
        Connection connection = mock(Connection.class);
        String schemaName = "mock_schema_name";

        doReturn("mock_current_schema").when(connection).getCatalog();

        sessionExtension.switchSchema(connection, schemaName);

        verify(connection).setCatalog(schemaName);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithSameSchema() throws Exception {
        Connection connection = mock(Connection.class);
        String schemaName = "mock_schema_name";

        doReturn("mock_schema_name").when(connection).getCatalog();

        sessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String currentSchema = "mock_current_schema";

        doReturn(currentSchema).when(connection).getCatalog();

        String result = sessionExtension.getCurrentSchema(connection);

        assertEquals(currentSchema, result);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "mock_connection_id";

        doReturn(connectionId).when(connection).queryForObject(eq("select connection_id()"), anyClass());

        String result = sessionExtension.getConnectionId(connection);

        assertEquals(connectionId, result);
    }

    @Test
    public void testGetConnectionIdWithoutQueryForObject() throws SQLException {
        Connection connection = mock(Connection.class);

        when(connection.isClosed()).thenReturn(false);

        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(connection).isInstanceOf(OceanBaseConnection.class).thenReturn(true);
        when(connection).unwrap(OceanBaseConnection.class).thenReturn(oceanBaseConnection);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(1L);

        String result = sessionExtension.getConnectionId(connection);

        assertEquals("1", result);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionIdWithQueryForObjectFailure() throws SQLException {
        Connection connection = mock(Connection.class);

        doThrow(new SQLException()).when(connection).queryForObject(eq("select connection_id()"), anyClass());

        String result = sessionExtension.getConnectionId(connection);
    }

    @Test
    public void testGetExtensions() {
        List<ExtensionPoint> extensions = extensionManager.getExtensions(SessionExtensionPoint.class);
        assertEquals(1, extensions.size());
        assertEquals(sessionExtension, extensions.get(0));
    }
}