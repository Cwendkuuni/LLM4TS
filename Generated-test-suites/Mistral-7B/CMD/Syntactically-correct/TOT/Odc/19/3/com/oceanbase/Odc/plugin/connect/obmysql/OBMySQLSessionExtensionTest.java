package com.oceanbase.odc.plugin.connect.obmysql.test;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import org.junit.Before;
import org.junit.Test;
import org.pf4j.ExtensionManager;
import org.pf4j.PluginWrapper;
import org.pf4j.TestExtension;
import org.pf4j.TestExtensionManager;
import org.pf4j.util.PropertiesUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OBMySQLSessionExtensionTest {

    private SessionExtensionPoint obMySQLSessionExtension;
    private Connection connection;
    private Properties properties;

    @Before
    public void setup() throws Exception {
        ExtensionManager extensionManager = new TestExtensionManager();
        PluginWrapper pluginWrapper = new PluginWrapper("COM.OCEANBASE.ODC.PLUGIN.CONNECT.OBMYSQL.OBMYSQLSESSIONEXTENSION");
        properties = PropertiesUtil.loadPropertiesFromResource("test-application.properties");
        extensionManager.loadPlugin(pluginWrapper, properties);
        obMySQLSessionExtension = extensionManager.getExtension(SessionExtensionPoint.class);
        connection = createConnection();
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "test_connection_id";
        obMySQLSessionExtension.killQuery(connection, connectionId);
        assertNull(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT COUNT(*) FROM information_schema.processlist WHERE PROCESSID = " + connectionId, Integer.class));
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test_schema";
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        assertEquals(schemaName, JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class));
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithInvalidSchema() throws SQLException {
        String schemaName = "";
        obMySQLSessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
        assertEquals(getCurrentSchemaFromConnection(), currentSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String connectionId = obMySQLSessionExtension.getConnectionId(connection);
        assertEquals(connectionIdFromConnection(), connectionId);
    }

    @Test
    public void testGetConnectionIdFromOceanBaseConnection() throws SQLException {
        String connectionId = "";
        if (connection instanceof OceanBaseConnection) {
            connectionId = obMySQLSessionExtension.getConnectionId(connection);
        }
        assertEquals(connectionIdFromOceanBaseConnection(), connectionId);
    }

    private Connection createConnection() throws SQLException {
        OceanBaseConnection oceanBaseConnection = new OceanBaseConnection(PropertiesUtil.loadPropertiesFromResource("test-connection.properties").getProperty("url"));
        oceanBaseConnection.connect();
        return oceanBaseConnection;
    }

    private String getCurrentSchemaFromConnection() throws SQLException {
        return JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class);
    }

    private String connectionIdFromConnection() throws SQLException {
        return JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class);
    }

    private String connectionIdFromOceanBaseConnection() throws SQLException {
        return ((OceanBaseConnection) connection).getServerThreadId() + "";
    }
}