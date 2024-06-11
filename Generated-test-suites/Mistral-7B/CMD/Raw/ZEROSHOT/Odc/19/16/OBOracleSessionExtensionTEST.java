package com.oceanbase.odc.plugin.connect.oboracle.test;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.ExtensionManager;
import org.pf4j.ExtensionPoint;
import org.pf4j.impl.DefaultExtensionManager;
import org.pf4j.impl.DefaultExtensionPointRegistry;
import org.pf4j.impl.DefaultExtensionRegistry;
import org.pf4j.impl.DefaultServiceProxyFactory;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import com.oceanbase.odc.plugin.connect.oboracle. OBOracleSessionExtension.SessionExtensionPointImpl;

/**
 * Test class for OBOracleSessionExtension.
 */
@SuppressWarnings("unchecked")
public class OBOracleSessionExtensionTest {

    private ExtensionManager extensionManager;
    private Connection connection;

    @Before
    public void setup() throws Exception {
        DefaultExtensionRegistry extensionRegistry = new DefaultExtensionRegistry();
        extensionRegistry.scan(OBOracleSessionExtension.class.getPackage().getName());
        extensionManager = new DefaultExtensionManager(extensionRegistry, new DefaultServiceProxyFactory());
        ExtensionPoint<SessionExtensionPoint> point = new DefaultExtensionPointRegistry<SessionExtensionPoint>(SessionExtensionPoint.class);
        extensionManager.registerExtensionPoint(point);
        connection = JdbcOperationsUtil.getConnection();
    }

    @After
    public void tearDown() throws Exception {
        connection.close();
        extensionManager.shutdown();
    }

    @Test
    public void testKillQuery() throws SQLException {
        OBOracleSessionExtension extension = (OBOracleSessionExtension) extensionManager.getExtensionByPoint(SessionExtensionPoint.class, OBOracleSessionExtension.class);
        String connectionId = UUID.randomUUID().toString();
        extension.killQuery(connection, connectionId);
        assertFalse("Query should be killed.", JdbcOperationsUtil.isQueryRunning(connection, connectionId));
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        OBOracleSessionExtension extension = (OBOracleSessionExtension) extensionManager.getExtensionByPoint(SessionExtensionPoint.class, OBOracleSessionExtension.class);
        String newSchemaName = "test_schema";
        extension.switchSchema(connection, newSchemaName);
        String currentSchema = extension.getCurrentSchema(connection);
        assertEquals("Switched to new schema.", newSchemaName, currentSchema);
        extension.switchSchema(connection, "prev_schema");
        extension.switchSchema(connection, newSchemaName);
        extension.switchSchema(connection, "prev_schema");
        extension.switchSchema(connection, newSchemaName);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithInvalidSchemaName() throws SQLException {
        OBOracleSessionExtension extension = (OBOracleSessionExtension) extensionManager.getExtensionByPoint(SessionExtensionPoint.class, OBOracleSessionExtension.class);
        extension.switchSchema(connection, "");
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        OBOracleSessionExtension extension = (OBOracleSessionExtension) extensionManager.getExtensionByPoint(SessionExtensionPoint.class, OBOracleSessionExtension.class);
        String currentSchema = extension.getCurrentSchema(connection);
        assertNotNull("Current schema should not be null.", currentSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        OBOracleSessionExtension extension = (OBOracleSessionExtension) extensionManager.getExtensionByPoint(SessionExtensionPoint.class, OBOracleSessionExtension.class);
        String connectionId = extension.getConnectionId(connection);
        assertNotNull("Connection id should not be null.", connectionId);
    }

    @Test
    public void testGetConnectionIdFromOceanBaseConnection() throws SQLException {
        OBOracleSessionExtension extension = (OBOracleSessionExtension) extensionManager.getExtensionByPoint(SessionExtensionPoint.class, OBOracleSessionExtension.class);
        OceanBaseConnection oceanBaseConnection = (OceanBaseConnection) connection;
        String connectionId = extension.getConnectionId(oceanBaseConnection);
        assertNotNull("Connection id should not be null.", connectionId);
        assertEquals("Connection id from OceanBaseConnection should be the same as that from the Extension.", connectionId, oceanBaseConnection.getServerThreadId());
    }

    @Test(expected = SQLException.class)
    public void testExecuteKillQueryWithNullConnectionId() throws SQLException {
        OBOracleSessionExtension extension = (OBOracleSessionExtension) extensionManager.getExtensionByPoint(SessionExtensionPoint.class, OBOracleSessionExtension.class);
        extension.killQuery(null, UUID.randomUUID().toString());
    }

    @Test(expected = SQLException.class)
    public void testExecuteKillQueryWithEmptyConnectionId() throws SQLException {
        OBOracleSessionExtension extension = (OBOracleSessionExtension) extensionManager.getExtensionByPoint(SessionExtensionPoint.class, OBOracleSessionExtension.class);
        extension.killQuery(connection, "");
    }

    @Test(expected = SQLException.class)
    public void testExecuteKillQueryWithInvalidConnectionId() throws SQLException {
        OBOracleSessionExtension extension = (OBOracleSessionExtension) extensionManager.getExtensionByPoint(SessionExtensionPoint.class, OBOracleSessionExtension.class);
        extension.killQuery(connection, "invalid_connection_id");
    }
}

class SessionExtensionPointImpl implements SessionExtensionPoint {
    // Empty implementation for testing
}