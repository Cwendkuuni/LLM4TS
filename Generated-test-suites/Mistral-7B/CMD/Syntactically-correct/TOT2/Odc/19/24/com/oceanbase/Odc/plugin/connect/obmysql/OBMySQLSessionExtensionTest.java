package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.mockito.Mockito;

public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        String connectionId = "test_connection_id";
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.killQuery(connection, connectionId);
        verify(connection, times(1)).createStatement();
        verify(connection, times(1)).executeUpdate("KILL QUERY " + connectionId);
        assertNotNull(connection);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.switchSchema(connection, schemaName);
        verify(connection, times(1)).setCatalog(schemaName);
        assertNotNull(connection);
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        String expectedSchema = "test_schema";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(eq("SELECT DATABASE()"), eq(String.class))).thenReturn(expectedSchema);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        String actualSchema = extension.getCurrentSchema(connection);
        assertNotNull(actualSchema);
        assertNotNull(expectedSchema);
        assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        String actualConnectionId = extension.getConnectionId(connection);
        assertNotNull(actualConnectionId);
    }

    @Test(expected = NullPointerException.class)
    public void testKillQueryNullConnection() {
        String connectionId = "test_connection_id";
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.killQuery(null, connectionId);
    }

    @Test(expected = NullPointerException.class)
    public void testKillQueryEmptyConnectionId() {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.killQuery(connection, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwitchSchemaNullSchemaName() {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.switchSchema(connection, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwitchSchemaEmptySchemaName() {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.switchSchema(connection, "");
    }

    @Test(expected = SQLException.class)
    public void testGetCurrentSchemaNullConnection() {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        Connection connection = null;
        String actualSchema = extension.getCurrentSchema(connection);
        assertNull(actualSchema);
    }

    @Test(expected = NullPointerException.class)
    public void testGetConnectionIdNullConnection() {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        Connection connection = null;
        String actualConnectionId = extension.getConnectionId(connection);
        assertNull(actualConnectionId);
    }

    @Test
    public void testKillQueryWithException() {
        Connection connection = mock(Connection.class);
        String connectionId = "test_connection_id";
        when(JdbcOperationsUtil.getJdbcOperations(connection).createStatement()).thenThrow(new SQLException("Test Exception"));
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        try {
            extension.killQuery(connection, connectionId);
            fail();
        } catch (SQLException e) {
            assertEquals("Test Exception", e.getMessage());
        }
    }

    @Test
    public void testSwitchSchemaWithException() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";
        when(JdbcOperationsUtil.getJdbcOperations(connection).setCatalog(schemaName)).thenThrow(new SQLException("Test Exception"));
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        try {
            extension.switchSchema(connection, schemaName);
            fail();
        } catch (SQLException e) {
            assertEquals("Test Exception", e.getMessage());
        }
    }
}