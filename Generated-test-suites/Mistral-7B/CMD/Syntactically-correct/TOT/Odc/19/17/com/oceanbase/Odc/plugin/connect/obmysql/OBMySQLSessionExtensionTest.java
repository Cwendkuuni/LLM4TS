package com.oceanbase.odc.plugin.connect.obmysql.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.sql.Connection;
import java.sql.SQLException;

import org.pf4j.Extension;
import org.pf4j.ExtensionManager;
import org.pf4j.PluginWrapper;

import com.oceanbase.jdbc.datasource.OceanBaseDataSource;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension.SessionExtensionPoint;

@Extension
public class OBMySQLSessionExtensionTest {

    @org.junit.BeforeClass
    public static void setup() {
        ExtensionManager extensionManager = new ExtensionManager();
        PluginWrapper pluginWrapper = new PluginWrapper("OBMySQLSessionExtensionTest", OBMySQLSessionExtension.class);
        extensionManager.loadExtensions(pluginWrapper);
    }

    @org.junit.Test
    public void testKillQuery() throws SQLException {
        SessionExtensionPoint obMySQLSessionExtension = extensionManager.getExtension(SessionExtensionPoint.class);
        Connection connection = new OceanBaseDataSource().getConnection();
        String connectionId = obMySQLSessionExtension.getConnectionId(connection);
        obMySQLSessionExtension.killQuery(connection, connectionId);
        // Simulate query being killed
        assertEquals("", JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT 1", String.class));
    }

    @org.junit.Test
    public void testSwitchSchema_ValidSchema() throws SQLException {
        SessionExtensionPoint obMySQLSessionExtension = extensionManager.getExtension(SessionExtensionPoint.class);
        Connection connection = new OceanBaseDataSource().getConnection();
        String schemaName = "test_schema";
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
        assertEquals(schemaName, currentSchema);
    }

    @org.junit.Test(expected = SQLException.class)
    public void testSwitchSchema_InvalidSchema() throws SQLException {
        SessionExtensionPoint obMySQLSessionExtension = extensionManager.getExtension(SessionExtensionPoint.class);
        Connection connection = new OceanBaseDataSource().getConnection();
        String schemaName = "non_existent_schema";
        obMySQLSessionExtension.switchSchema(connection, schemaName);
    }

    @org.junit.Test
    public void testGetCurrentSchema() throws SQLException {
        SessionExtensionPoint obMySQLSessionExtension = extensionManager.getExtension(SessionExtensionPoint.class);
        Connection connection = new OceanBaseDataSource().getConnection();
        String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
        assertEquals(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class), currentSchema);
    }

    @org.junit.Test
    public void testGetConnectionId() throws SQLException {
        SessionExtensionPoint obMySQLSessionExtension = extensionManager.getExtension(SessionExtensionPoint.class);
        Connection connection = new OceanBaseDataSource().getConnection();
        String connectionId = obMySQLSessionExtension.getConnectionId(connection);
        // OceanBaseConnection is not used in tests, but we need to ensure the method is not throwing an exception
        if (connection instanceof OceanBaseConnection) {
            assertEquals(((OceanBaseConnection) connection).getServerThreadId(), connectionId);
        } else {
            assertThrows(Exception.class, () -> JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT connection_id()", String.class));
            assertEquals(null, connectionId);
        }
    }
}