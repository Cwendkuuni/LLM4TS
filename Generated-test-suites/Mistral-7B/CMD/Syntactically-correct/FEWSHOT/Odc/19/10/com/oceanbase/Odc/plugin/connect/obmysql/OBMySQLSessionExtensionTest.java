package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.ExtensionManager;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

@SuppressWarnings("unchecked")
public class OBMySQLSessionExtensionTest {

    private ExtensionManager extensionManager = mock(ExtensionManager.class);
    private JdbcOperations jdbcOperations = mock(JdbcOperations.class);
    private OBMySQLSessionExtension extension = new OBMySQLSessionExtension();

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "1234";
        when(jdbcOperations.execute(connection, "KILL QUERY " + connectionId)).thenReturn(null);
        when(extension.getJdbcOperations(connection)).thenReturn(jdbcOperations);

        extension.killQuery(connection, connectionId);
    }

    @Test(expected = SQLException.class)
    public void testKillQuery_Exception() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "1234";
        when(jdbcOperations.execute(connection, "KILL QUERY " + connectionId)).thenThrow(new SQLException("Failed to kill query"));
        when(extension.getJdbcOperations(connection)).thenReturn(jdbcOperations);

        extension.killQuery(connection, connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "testSchema";
        String currentSchema = "currentSchema";
        when(extension.getCurrentSchema(connection)).thenReturn(currentSchema);
        when(jdbcOperations.execute(connection, "SET SCHEMA " + schemaName)).thenReturn(null);
        when(extension.getCurrentSchema(connection)).thenReturn(schemaName);

        extension.switchSchema(connection, schemaName);

        assertEquals(schemaName, currentSchema);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchema_Failed() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "testSchema";
        String currentSchema = "currentSchema";
        when(extension.getCurrentSchema(connection)).thenReturn(currentSchema);
        when(jdbcOperations.execute(connection, "SET SCHEMA " + schemaName)).thenThrow(new SQLException("Failed to switch schema"));
        when(extension.getCurrentSchema(connection)).thenReturn(currentSchema);

        extension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        String schemaName = "testSchema";
        when(extension.getCurrentSchema(connection)).thenReturn(schemaName);

        assertEquals(schemaName, extension.getCurrentSchema(connection));
    }

    @Test
    public void testGetCurrentSchema_Null() {
        Connection connection = mock(Connection.class);
        when(extension.getCurrentSchema(connection)).thenReturn(null);

        assertNull(extension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(Connection.class);
        String connectionId = "1234";
        when(extension.getConnectionId(connection)).thenReturn(connectionId);

        assertEquals(connectionId, extension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionId_Null() {
        Connection connection = mock(Connection.class);
        when(extension.getConnectionId(connection)).thenReturn(null);

        assertNull(extension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionId_OceanBaseConnection() throws SQLException {
        Connection connection = mock(OceanBaseConnection.class);
        String connectionId = "1234";
        when(extension.getConnectionId(connection)).thenReturn(connectionId);

        assertEquals(connectionId, extension.getConnectionId(connection));
    }

    @Test
    public void testGetJdbcOperations() {
        Connection connection = mock(Connection.class);
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(extension.getJdbcOperations(connection)).thenReturn(jdbcOperations);

        assertEquals(jdbcOperations, extension.getJdbcOperations(connection));
    }

    @Test
    public void testGetJdbcOperations_Null() {
        Connection connection = mock(Connection.class);
        when(extension.getJdbcOperations(connection)).thenReturn(null);

        assertNull(extension.getJdbcOperations(connection));
    }

    @Test
    public void testExtension_Register() {
        extensionManager.registerExtension(extension);

        assertEquals(extension, extensionManager.findExtension(SessionExtensionPoint.class));
    }
}