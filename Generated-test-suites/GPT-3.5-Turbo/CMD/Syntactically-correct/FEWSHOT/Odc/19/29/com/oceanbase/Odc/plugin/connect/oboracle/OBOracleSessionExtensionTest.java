package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

public class OBOracleSessionExtensionTest {

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        String connectionId = "12345";

        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        extension.killQuery(connection, connectionId);

        verify(connection, times(1)).prepareStatement("KILL QUERY 12345");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";
        String currentSchema = "current_schema";

        when(extension.getCurrentSchema(connection)).thenReturn(currentSchema);

        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        extension.switchSchema(connection, schemaName);

        verify(connection, times(1)).prepareStatement("ALTER SESSION SET CURRENT_SCHEMA = 'test_schema'");
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        String queryResult = "test_schema";

        when(jdbcOperations.queryForObject(anyString(), eq(String.class))).thenReturn(queryResult);

        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        String currentSchema = extension.getCurrentSchema(connection);

        assertNotNull(currentSchema);
        assertEquals("test_schema", currentSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);

        when(jdbcOperations.queryForObject(anyString(), eq(String.class))).thenThrow(new SQLException());
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(12345);

        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        String connectionId = extension.getConnectionId(connection);

        assertNotNull(connectionId);
        assertEquals("12345", connectionId);
    }
}