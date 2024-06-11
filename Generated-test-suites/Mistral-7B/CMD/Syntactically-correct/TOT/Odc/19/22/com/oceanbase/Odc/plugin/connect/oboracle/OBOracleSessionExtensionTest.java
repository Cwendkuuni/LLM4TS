package com.oceanbase.odc.plugin.connect.oboracle.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.PluginWrapper;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;

@Extension
public class OBOracleSessionExtensionTest {

    @Test
    public void testKillQuery() throws SQLException {
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        PluginWrapper pluginWrapper = mock(PluginWrapper.class);
        Connection connection = mock(Connection.class);

        when(pluginWrapper.getExtension(SessionExtensionPoint.class)).thenReturn(extension);
        when(extension.killQuery(connection, "connectionId123")).thenReturn();

        extension.killQuery(connection, "connectionId123");

        // Positive test case

        // Test with null connection and connectionId
        extension.killQuery(null, null);
        extension.killQuery(connection, null);
        extension.killQuery(null, "connectionId");

        // Test with empty connectionId
        extension.killQuery(connection, "");

        // Test with invalid SQL query
        String invalidQuery = "KILL QUERY abc";
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenThrow(new SQLException("Error executing query: " + invalidQuery));
        assertThrows(SQLException.class, () -> extension.killQuery(connection, "connectionId123"));
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        PluginWrapper pluginWrapper = mock(PluginWrapper.class);
        Connection connection = mock(Connection.class);

        when(pluginWrapper.getExtension(SessionExtensionPoint.class)).thenReturn(extension);

        String currentSchema = "currentSchema";
        String schemaName = "newSchema";

        when(extension.getCurrentSchema(connection)).thenReturn(currentSchema);
        when(extension.switchSchema(connection, schemaName)).thenReturn();

        extension.switchSchema(connection, schemaName);

        // Positive test case

        // Test with null connection and schemaName
        extension.switchSchema(null, null);
        extension.switchSchema(connection, null);

        // Test with same schemaName
        extension.switchSchema(connection, currentSchema);

        // Test with invalid SQL query
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenThrow(new SQLException("Error executing query: ALTER SESSION SET CURRENT_SCHEMA=" + schemaName));
        assertThrows(SQLException.class, () -> extension.switchSchema(connection, schemaName));
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        PluginWrapper pluginWrapper = mock(PluginWrapper.class);
        Connection connection = mock(Connection.class);

        when(pluginWrapper.getExtension(SessionExtensionPoint.class)).thenReturn(extension);

        String schemaName = "schemaName";
        when(extension.getCurrentSchema(connection)).thenReturn(schemaName);

        assertEquals(schemaName, extension.getCurrentSchema(connection));

        // Test with null connection
        assertNull(extension.getCurrentSchema(null));

        // Test with no result SQL query
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(null);
        assertNull(extension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        PluginWrapper pluginWrapper = mock(PluginWrapper.class);
        Connection connection = mock(Connection.class);

        when(pluginWrapper.getExtension(SessionExtensionPoint.class)).thenReturn(extension);

        String connectionId = "connectionId123";
        when(extension.getConnectionId(connection)).thenReturn(connectionId);

        assertEquals(connectionId, extension.getConnectionId(connection));

        // Test with null connection
        Connection nullConnection = mock(Connection.class);
        assertNull(extension.getConnectionId(nullConnection));
    }
}