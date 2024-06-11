package com.oceanbase.odc.plugin.connect.oboracle.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.pf4j.ExtensionManager;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

@RunWith(PowerMockRunner.class)
public class OBOracleSessionExtensionTest {

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        extension.killQuery(connection, "test_connection_id");
        PowerMockito.verifyStatic(Connection.class);
        Connection.createStatement().execute("KILL QUERY test_connection_id");
    }

    @Test
    public void testSwitchSchema_SameSchema() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        String schemaName = "test_schema";
        String currentSchema = schemaName;
        PowerMockito.when(connection.getCurrentSchema()).thenReturn(currentSchema);
        extension.switchSchema(connection, schemaName);
        PowerMockito.verifyZeroInteractions(connection);
    }

    @Test
    public void testSwitchSchema_DifferentSchema() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        String schemaName = "test_schema";
        String currentSchema = "other_schema";
        PowerMockito.when(connection.getCurrentSchema()).thenReturn(currentSchema);
        PowerMockito.doAnswer(invocation -> {
            String alterSql = invocation.getArgumentAt(0, String.class);
            JdbcOperationsUtil.getJdbcOperations(connection).update(alterSql);
        }).when(connection).createStatement();
        extension.switchSchema(connection, schemaName);
        PowerMockito.verify(connection).createStatement();
        PowerMockito.verify(connection).createStatement().execute(PowerMockito.containsString("ALTER SESSION SET CURRENT_SCHEMA="));
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchema_Exception() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        String schemaName = "test_schema";
        PowerMockito.doThrow(new SQLException("Test exception")).when(connection).createStatement();
        extension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        String currentSchema = "test_schema";
        PowerMockito.when(connection.queryForObject(PowerMockito.anyString(), PowerMockito.eq(String.class))).thenReturn(currentSchema);
        assertEquals(currentSchema, extension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId_Normal() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        String connectionId = "test_connection_id";
        PowerMockito.when(connection.queryForObject(PowerMockito.anyString(), PowerMockito.eq(String.class))).thenReturn(connectionId);
        assertEquals(connectionId, extension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionId_NoQuery() {
        Connection connection = Mockito.mock(Connection.class);
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        assertNull(extension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionId_Exception() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        PowerMockito.doThrow(new SQLException("Test exception")).when(connection).queryForObject(PowerMockito.anyString(), PowerMockito.eq(String.class));
        assertThrows(SQLException.class, () -> extension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionId_OceanBaseConnection() {
        Connection connection = Mockito.mock(OceanBaseConnection.class);
        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        String connectionId = "12345678";
        PowerMockito.when(connection.getServerThreadId()).thenReturn(connectionId);
        assertEquals(connectionId, extension.getConnectionId(connection));
    }
}