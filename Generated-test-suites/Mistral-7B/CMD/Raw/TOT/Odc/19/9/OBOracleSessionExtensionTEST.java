package com.oceanbase.odc.plugin.connect.oboracle.test;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.pf4j.ExtensionManager;
import org.pf4j.impl.DefaultExtensionPoint;
import org.pf4j.impl.DefaultExtensionPointFactory;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

public class OBOracleSessionExtensionTest {

    private SessionExtensionPoint sessionExtension;
    private Connection connection;

    @Before
    public void setup() throws SQLException {
        ExtensionManager extensionManager = new ExtensionManager();
        DefaultExtensionPointFactory factory = new DefaultExtensionPointFactory();
        sessionExtension = (OBOracleSessionExtension) factory.createExtension(OBOracleSessionExtension.class, extensionManager);
        connection = JdbcOperationsUtil.getConnection();
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = sessionExtension.getConnectionId(connection);
        sessionExtension.killQuery(connection, connectionId);
        assertFalse("Query not killed", JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT COUNT(*) FROM DUAL WHERE ROWNUM = 1", Integer.class) > 0);
    }

    @Test(expected = SQLException.class)
    public void testKillQueryInvalidConnectionId() throws SQLException {
        String connectionId = "invalid_connection_id";
        sessionExtension.killQuery(connection, connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test_schema";
        sessionExtension.switchSchema(connection, schemaName);
        assertEquals(schemaName, sessionExtension.getCurrentSchema(connection));
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaInvalidSchemaName() throws SQLException {
        String schemaName = null;
        sessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String currentSchema = sessionExtension.getCurrentSchema(connection);
        assertNotNull(currentSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String connectionId = sessionExtension.getConnectionId(connection);
        assertNotNull(connectionId);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionIdNullConnection() throws SQLException {
        sessionExtension.getConnectionId(null);
    }
}