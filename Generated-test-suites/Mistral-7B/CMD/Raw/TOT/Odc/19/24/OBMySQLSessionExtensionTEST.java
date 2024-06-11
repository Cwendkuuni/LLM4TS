package com.oceanbase.odc.plugin.connect.obmysql.test;

import org.junit.Before;
import org.junit.Test;
import org.pf4j.ExtensionManager;
import org.pf4j.ExtensionPoint;
import org.pf4j.ExtensionPointProvider;
import org.pf4j.impl.DefaultExtensionManager;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test class for OBMySQLSessionExtension.
 */
public class OBMySQLSessionExtensionTest {

    private ExtensionPoint<SessionExtensionPoint> sessionExtensionPoint;
    private ExtensionManager extensionManager;

    @Before
    public void setup() throws Exception {
        extensionManager = new DefaultExtensionManager();
        extensionManager.loadExtensions("classpath:/META-INF/pf4j-extensions.xml");
        sessionExtensionPoint = extensionManager.getExtensionPoints(SessionExtensionPoint.class);
    }

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = JdbcOperationsUtil.getTestConnection();
        OBMySQLSessionExtension extension = (OBMySQLSessionExtension) sessionExtensionPoint.getExtension(0);
        extension.killQuery(connection, "12345");

        // Verify that the query has been killed
        String querySql = "SELECT COUNT(*) FROM information_schema.processlist WHERE command = 'KILL QUERY' AND argument = '12345'";
        int count = JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(querySql, int.class);
        assertEquals(1, count);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = JdbcOperationsUtil.getTestConnection();
        OBMySQLSessionExtension extension = (OBMySQLSessionExtension) sessionExtensionPoint.getExtension(0);

        // Test switching to a valid schema
        extension.switchSchema(connection, "test_schema");
        String currentSchema = extension.getCurrentSchema(connection);
        assertEquals("test_schema", currentSchema);

        // Test switching back to the original schema
        extension.switchSchema(connection, "master");
        currentSchema = extension.getCurrentSchema(connection);
        assertEquals("master", currentSchema);

        // Test switching to an invalid schema
        try {
            extension.switchSchema(connection, "non_existent_schema");
            assert false;
        } catch (SQLException e) {
            // Expected
        }
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = JdbcOperationsUtil.getTestConnection();
        OBMySQLSessionExtension extension = (OBMySQLSessionExtension) sessionExtensionPoint.getExtension(0);

        String currentSchema = extension.getCurrentSchema(connection);
        assertNull(currentSchema);

        extension.switchSchema(connection, "test_schema");
        currentSchema = extension.getCurrentSchema(connection);
        assertEquals("test_schema", currentSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = JdbcOperationsUtil.getTestConnection();
        OBMySQLSessionExtension extension = (OBMySQLSessionExtension) sessionExtensionPoint.getExtension(0);

        String connectionId = extension.getConnectionId(connection);
        assertNull(connectionId);

        if (connection instanceof OceanBaseConnection) {
            connectionId = extension.getConnectionId(connection);
            assertEquals(((OceanBaseConnection) connection).getServerThreadId(), connectionId);
        }
    }

    @Test(expected = ClassCastException.class)
    public void testGetExtensionWithWrongType() throws Exception {
        ExtensionPoint<String> stringExtensionPoint = extensionManager.getExtensionPoints(String.class);
        stringExtensionPoint.getExtension(0);
    }
}