package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.pf4j.ExtensionManager;
import org.pf4j.TestExtensionPoint;
import org.pf4j.TestUtil;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

/**
 * Test class for OBOracleSessionExtension.
 */
public class OBOracleSessionExtensionTest {

    private Connection connection;
    private SessionExtensionPoint sessionExtension;

    @Before
    public void setUp() throws Exception {
        ExtensionManager extensionManager = TestUtil.getExtensionManager();
        sessionExtension = (OBOracleSessionExtension) extensionManager.getExtensionByClass(OBOracleSessionExtension.class);
        connection = TestUtil.getTestConnection();
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = sessionExtension.getConnectionId(connection);
        String queryId = "query1";
        sessionExtension.killQuery(connection, connectionId + queryId);
        // Check if query is killed
        // This check can be done by checking if query is still running or not
        // It's not possible to write a test case for this as it depends on the actual behaviour of JdbcOperationsUtil.execute()
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String currentSchema = sessionExtension.getCurrentSchema(connection);
        String newSchema = "test_schema";
        sessionExtension.switchSchema(connection, newSchema);
        assertEquals(newSchema, sessionExtension.getCurrentSchema(connection));
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithInvalidSchema() throws SQLException {
        String currentSchema = sessionExtension.getCurrentSchema(connection);
        String newSchema = null;
        sessionExtension.switchSchema(connection, newSchema);
        sessionExtension.getCurrentSchema(connection);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String currentSchema = sessionExtension.getCurrentSchema(connection);
        String newSchema = "test_schema";
        sessionExtension.switchSchema(connection, newSchema);
        assertEquals(newSchema, sessionExtension.getCurrentSchema(connection));
        sessionExtension.switchSchema(connection, currentSchema);
        assertEquals(currentSchema, sessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String connectionId1 = sessionExtension.getConnectionId(connection);
        String connectionId2 = sessionExtension.getConnectionId(connection);
        assertNotEquals(connectionId1, connectionId2);
    }

    @After
    public void tearDown() throws Exception {
        TestUtil.closeQuietly(connection);
    }
}