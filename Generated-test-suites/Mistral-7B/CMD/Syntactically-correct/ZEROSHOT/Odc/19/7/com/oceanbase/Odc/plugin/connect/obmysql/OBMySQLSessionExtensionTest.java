import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pf4j.ExtensionManager;
import org.pf4j.ExtensionPoint;
import org.pf4j.PluginManager;
import org.pf4j.TestExtension;
import org.pf4j.TestExtensionManager;
import org.pf4j.TestPluginManager;
import org.pf4j.junit.JUnit3TestExtension;
import org.pf4j.util.ClasspathScanner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.sql.Connection;
import java.sql.SQLException;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({OBMySQLSessionExtension.class})
@PowerMockIgnore({"javax.management.*", "org.mortbay.jetty.*", "org.apache.commons.dbcp.*", "org.apache.commons.dbcp2.*"})
public class OBMySQLSessionExtensionTest {

    private ExtensionPoint extensionPoint;
    private OBMySQLSessionExtension obMySQLSessionExtension;
    private ExtensionManager extensionManager;

    @Before
    public void setUp() throws Exception {
        extensionManager = new TestExtensionManager();
        PluginManager pluginManager = new TestPluginManager(extensionManager);
        ClasspathScanner scanner = new ClasspathScanner(pluginManager);
        scanner.scanPackageOfClass("com.oceanbase.odc.plugin.connect.obmysql");
        extensionPoint = extensionManager.getExtensionPoint(SessionExtensionPoint.class);
        obMySQLSessionExtension = (OBMySQLSessionExtension) extensionPoint.getExtensions().get(0);
    }

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "12345";
        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));

        obMySQLSessionExtension.killQuery(connection, connectionId);
        verify(connection.createStatement()).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";
        String currentSchema = "current_schema";

        when(obMySQLSessionExtension.getCurrentSchema(connection)).thenReturn(currentSchema);

        obMySQLSessionExtension.switchSchema(connection, schemaName);

        verify(connection).setCatalog(schemaName);
        assertEquals("test_schema", obMySQLSessionExtension.getCurrentSchema(connection));
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithSameSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";
        String currentSchema = "test_schema";

        when(obMySQLSessionExtension.getCurrentSchema(connection)).thenReturn(currentSchema);

        obMySQLSessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String querySql = "SELECT DATABASE()";
        String schemaName = "test_schema";

        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));
        when(connection.createStatement().executeQuery(querySql)).thenReturn(mock(java.sql.ResultSet.class));
        when(connection.createStatement().next()).thenReturn(true);
        when(connection.createStatement().getString(1)).thenReturn(schemaName);

        assertEquals(schemaName, obMySQLSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "12345";

        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));
        when(connection.createStatement().executeQuery("select connection_id()")).thenReturn(mock(java.sql.ResultSet.class));
        when(connection.createStatement().next()).thenReturn(true);
        when(connection.createStatement().getString(1)).thenReturn(connectionId);

        assertEquals(connectionId, obMySQLSessionExtension.getConnectionId(connection));

        assertNull(obMySQLSessionExtension.getConnectionId(null));
    }

    @Test
    public void testGetConnectionIdForOceanBaseConnection() throws SQLException {
        Connection connection = mock(OceanBaseConnection.class);
        String connectionId = "12345";

        when(connection.getServerThreadId()).thenReturn(connectionId);

        assertEquals(connectionId, obMySQLSessionExtension.getConnectionId(connection));

        assertNull(obMySQLSessionExtension.getConnectionId(null));
    }

    @After
    public void tearDown() {
        PowerMockito.restoreOriginalMethods();
    }
}