package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.ExtensionFactory;
import org.pf4j.ExtensionManager;
import org.pf4j.PluginWrapper;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

/**
 * Test class for OBOracleSessionExtension.
 *
 * @author yaobin
 */
public class OBOracleSessionExtensionTest {

    private static final String TEST_SCHEMA = "test_schema";
    private static final String TEST_CONNECTION_ID = "test_connection_id";
    private static final String TEST_CONNECTION_ID_2 = "test_connection_id_2";
    private ExtensionManager extensionManager;
    private OBOracleSessionExtension obOracleSessionExtension;
    private Connection connection;

    @Before
    public void setUp() throws Exception {
        ExtensionFactory extensionFactory = new ExtensionFactory();
        extensionManager = extensionFactory.getExtensionManager();
        PluginWrapper pluginWrapper = extensionFactory.loadPlugin("OBOracleSessionExtension");
        obOracleSessionExtension = (OBOracleSessionExtension) extensionManager.getExtension(SessionExtensionPoint.class, pluginWrapper);
        connection = JdbcOperationsUtil.getConnection();
    }

    @After
    public void tearDown() throws Exception {
        JdbcOperationsUtil.closeConnection(connection);
        extensionManager.shutdown();
    }

    @Test
    public void testKillQuery() throws SQLException {
        String queryId = "test_query_id";
        obOracleSessionExtension.killQuery(connection, queryId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String newSchema = TEST_SCHEMA;
        obOracleSessionExtension.switchSchema(connection, newSchema);
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        assertEquals(newSchema, currentSchema);
        obOracleSessionExtension.switchSchema(connection, TEST_SCHEMA);
        assertEquals(TEST_SCHEMA, currentSchema);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithSameSchema() throws SQLException {
        String newSchema = TEST_SCHEMA;
        obOracleSessionExtension.switchSchema(connection, newSchema);
        obOracleSessionExtension.switchSchema(connection, newSchema);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        assertNotNull(currentSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String connectionId = obOracleSessionExtension.getConnectionId(connection);
        assertEquals(TEST_CONNECTION_ID, connectionId);

        if (connection instanceof OceanBaseConnection) {
            connectionId = obOracleSessionExtension.getConnectionId(connection);
            assertEquals(TEST_CONNECTION_ID_2, connectionId);
        }
    }

    @Test
    public void testGetConnectionIdWithNullConnection() {
        assertNull(obOracleSessionExtension.getConnectionId(null));
    }
}