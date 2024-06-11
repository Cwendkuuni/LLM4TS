package com.oceanbase.odc.plugin.connect.oboracle.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.ExtensionManager;
import org.pf4j.TestExtensionManager;
import org.pf4j.util.PropertiesUtil;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension.SessionExtensionPointImpl;

@SuppressWarnings("unchecked")
public class OBOracleSessionExtensionTest {

    private ExtensionManager extensionManager;
    private SessionExtensionPoint sessionExtensionPoint;

    @Before
    public void setup() {
        PropertiesUtil.setProperty("odc.plugin.class.path", "com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension");
        extensionManager = new TestExtensionManager();
        sessionExtensionPoint = (SessionExtensionPoint) extensionManager.getExtensions(SessionExtensionPoint.class.getName()).get(0);
    }

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "test_connection_id";
        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));

        sessionExtensionPoint.killQuery(connection, connectionId);

        assertEquals("KILL QUERY test_connection_id", JdbcOperationsUtil.getJdbcOperations(connection).getLastExecutedStatement(),
                "The last executed statement should be 'KILL QUERY test_connection_id'.");
    }

    @Test(expected = SQLException.class)
    public void testKillQueryInvalidConnection() throws SQLException {
        Connection connection = null;
        String connectionId = "test_connection_id";

        sessionExtensionPoint.killQuery(connection, connectionId);

        // The test should throw SQLException because the connection is null.
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));
        when(connection.isClosed()).thenReturn(false);
        when(connection.isValid(1)).thenReturn(true);
        when(connection.isWrapperFor(OceanBaseConnection.class)).thenReturn(true);
        when(connection.unwrap(OceanBaseConnection.class)).thenReturn(oceanBaseConnection);

        sessionExtensionPoint.switchSchema(connection, schemaName);

        String currentSchema = sessionExtensionPoint.getCurrentSchema(connection);
        assertEquals(schemaName, currentSchema, "The current schema should be set to 'test_schema'.");
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaNullConnection() throws SQLException {
        Connection connection = null;
        String schemaName = "test_schema";

        sessionExtensionPoint.switchSchema(connection, schemaName);

        // The test should throw SQLException because the connection is null.
    }

    @Test
    public void testSwitchSchemaSameSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));
        when(connection.isClosed()).thenReturn(false);
        when(connection.isValid(1)).thenReturn(true);
        when(connection.isWrapperFor(OceanBaseConnection.class)).thenReturn(true);
        when(connection.unwrap(OceanBaseConnection.class)).thenReturn(oceanBaseConnection);
        when(sessionExtensionPoint.getCurrentSchema(connection)).thenReturn(schemaName);

        sessionExtensionPoint.switchSchema(connection, schemaName);

        // Switching to the same schema should not change anything.
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        String expectedSchema = "test_schema";
        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));

        String actualSchema = sessionExtensionPoint.getCurrentSchema(connection);

        assertEquals(expectedSchema, actualSchema, "The current schema should be returned correctly.");
    }

    @Test(expected = SQLException.class)
    public void testGetCurrentSchemaNullConnection() throws SQLException {
        Connection connection = null;

        sessionExtensionPoint.getCurrentSchema(connection);

        // The test should throw SQLException because the connection is null.
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);
        String expectedConnectionId = "test_connection_id";

        sessionExtensionPoint.getConnectionId(connection);

        assertEquals(expectedConnectionId, sessionExtensionPoint.getConnectionId(connection),
                "The connection ID should be returned correctly.");

        // Test with OceanBaseConnection.
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(connection.isWrapperFor(OceanBaseConnection.class)).thenReturn(true);
        when(connection.unwrap(OceanBaseConnection.class)).thenReturn(oceanBaseConnection);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(123);

        assertEquals("123", sessionExtensionPoint.getConnectionId(connection),
                "The connection ID should be returned correctly for OceanBaseConnection.");
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionIdNullConnection() throws SQLException {
        Connection connection = null;

        sessionExtensionPoint.getConnectionId(connection);

        // The test should throw SQLException because the connection is null.
    }
}

class SessionExtensionPointImpl extends OBOracleSessionExtension {
    // Empty implementation.
}