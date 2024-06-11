package com.oceanbase.odc.plugin.connect.obmysql.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.ExtensionManager;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

/**
 * Test class for OBMySQLSessionExtension
 */
public class OBMySQLSessionExtensionTest {

    private SessionExtensionPoint sessionExtension;
    private ExtensionManager extensionManager;

    @Before
    public void setUp() throws Exception {
        extensionManager = new ExtensionManager();
        extensionManager.loadExtensions("path/to/your/extension.xml");
        sessionExtension = (OBMySQLSessionExtension) extensionManager.getExtensionByClassName(OBMySQLSessionExtension.class.getName());
    }

    @Test
    public void testKillQuery() {
        Connection connection = getMockConnection();
        String connectionId = "test_connection_id";
        sessionExtension.killQuery(connection, connectionId);
        // add verification code here
    }

    @Test
    public void testSwitchSchemaSuccess() throws SQLException {
        Connection connection = getMockConnection();
        String schemaName = "test_schema_name";
        sessionExtension.switchSchema(connection, schemaName);
        String currentSchema = sessionExtension.getCurrentSchema(connection);
        assertEquals(schemaName, currentSchema);
    }

    @Test
    public void testSwitchSchemaSameSchema() throws SQLException {
        Connection connection = getMockConnection();
        String schemaName = "test_schema_name";
        sessionExtension.switchSchema(connection, schemaName);
        String currentSchema = sessionExtension.getCurrentSchema(connection);
        assertEquals(schemaName, currentSchema);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaFailure() throws SQLException {
        Connection connection = getMockConnection();
        String schemaName = "test_schema_name";
        sessionExtension.switchSchema(connection, schemaName);
        sessionExtension.switchSchema(connection, schemaName); // second call should throw an exception
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = getMockConnection();
        String currentSchema = sessionExtension.getCurrentSchema(connection);
        assertNotNull(currentSchema);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionIdNull() throws SQLException {
        Connection connection = getMockConnection();
        sessionExtension.getConnectionId(null);
    }

    @Test
    public void testGetConnectionIdOceanBaseConnection() throws SQLException {
        Connection connection = getMockOceanBaseConnection();
        String connectionId = sessionExtension.getConnectionId(connection);
        assertNotNull(connectionId);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionIdException() throws SQLException {
        Connection connection = getMockConnection();
        assertThrows(SQLException.class, () -> sessionExtension.getConnectionId(connection));
    }

    private Connection getMockConnection() throws SQLException {
        // create mock connection here
        return null;
    }

    private Connection getMockOceanBaseConnection() throws SQLException {
        // create mock OceanBaseConnection here
        return null;
    }
}