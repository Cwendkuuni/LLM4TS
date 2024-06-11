
package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

public class OBOracleSessionExtensionTest {

    @Test
    public void testKillQuery() {
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        Connection mockConnection = mock(Connection.class);
        String connectionId = "12345";

        obOracleSessionExtension.killQuery(mockConnection, connectionId);

        // Verify that the execute method is called with the correct argument
        verify(JdbcOperationsUtil.getJdbcOperations(mockConnection), times(1)).execute("KILL QUERY 12345");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        Connection mockConnection = mock(Connection.class);
        String schemaName = "test_schema";

        // Test when currentSchema is different from schemaName
        when(obOracleSessionExtension.getCurrentSchema(mockConnection)).thenReturn("other_schema");

        obOracleSessionExtension.switchSchema(mockConnection, schemaName);

        // Verify that the update method is called with the correct alterSql
        verify(JdbcOperationsUtil.getJdbcOperations(mockConnection), times(1)).update("ALTER SESSION SET CURRENT_SCHEMA='test_schema'");

        // Test when currentSchema is equal to schemaName
        when(obOracleSessionExtension.getCurrentSchema(mockConnection)).thenReturn("test_schema");

        obOracleSessionExtension.switchSchema(mockConnection, schemaName);

        // Verify that no update is called when currentSchema is equal to schemaName
        verify(JdbcOperationsUtil.getJdbcOperations(mockConnection), never()).update(anyString());
    }

    @Test
    public void testGetCurrentSchema() {
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        Connection mockConnection = mock(Connection.class);

        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject(anyString(), eq(String.class))).thenReturn("test_schema");

        String currentSchema = obOracleSessionExtension.getCurrentSchema(mockConnection);

        assertEquals("test_schema", currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        Connection mockConnection = mock(Connection.class);

        // Test when queryForObject succeeds
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject(anyString(), eq(String.class))).thenReturn("12345");

        String connectionId = obOracleSessionExtension.getConnectionId(mockConnection);
        assertEquals("12345", connectionId);

        // Test when queryForObject throws an exception and connection is an instance of OceanBaseConnection
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject(anyString(), eq(String.class))).thenThrow(new RuntimeException());
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(mockConnection instanceof OceanBaseConnection).thenReturn(true);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(6789);

        connectionId = obOracleSessionExtension.getConnectionId(mockConnection);
        assertEquals("6789", connectionId);
    }
}

