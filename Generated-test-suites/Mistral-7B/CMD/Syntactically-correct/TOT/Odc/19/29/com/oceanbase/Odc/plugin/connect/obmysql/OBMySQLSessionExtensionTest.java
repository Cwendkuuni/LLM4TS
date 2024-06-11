package com.oceanbase.odc.plugin.connect.obmysql;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.pf4j.Extension;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class OBMySQLSessionExtensionTest {

    private Connection connection;
    private OBMySQLSessionExtension obMySQLSessionExtension;

    @Before
    public void setup() {
        connection = mock(Connection.class);
        obMySQLSessionExtension = new OBMySQLSessionExtension();
    }

    @Test
    public void testKillQuerySuccess() throws SQLException {
        when(connection.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));
        obMySQLSessionExtension.killQuery(connection, "connectionId");
        Mockito.verify(connection.createStatement()).execute(Mockito.eq("KILL QUERY connectionId"));
    }

    @Test(expected = SQLException.class)
    public void testKillQueryException() throws SQLException {
        when(connection.createStatement()).thenThrow(new SQLException("Test Exception"));
        obMySQLSessionExtension.killQuery(connection, "connectionId");
    }

    @Test
    public void testSwitchSchemaSuccess() throws SQLException {
        String schemaName = "test_schema";
        when(connection.getCatalog()).thenReturn("old_schema");
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        assertEquals(schemaName, connection.getCatalog());
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaException() throws SQLException {
        String schemaName = "test_schema";
        when(connection.createStatement()).thenThrow(new SQLException("Test Exception"));
        obMySQLSessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchemaSuccess() throws SQLException {
        when(connection.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));
        when(connection.createStatement().executeQuery(anyString())).thenReturn(Mockito.mock(java.sql.ResultSet.class));
        when(connection.createStatement().executeQuery(anyString()).next()).thenReturn(true);
        when(connection.createStatement().executeQuery(anyString()).getString("DATABASE")).thenReturn("test_database");
        assertEquals("test_database", obMySQLSessionExtension.getCurrentSchema(connection));
    }

    @Test(expected = SQLException.class)
    public void testGetCurrentSchemaException() throws SQLException {
        when(connection.createStatement()).thenThrow(new SQLException("Test Exception"));
        obMySQLSessionExtension.getCurrentSchema(connection);
    }

    @Test
    public void testGetConnectionIdSuccess() throws SQLException {
        when(connection.createStatement()).thenReturn(Mockito.mock(java.sql.Statement.class));
        when(connection.createStatement().executeQuery(anyString())).thenReturn(Mockito.mock(java.sql.ResultSet.class));
        when(connection.createStatement().executeQuery(anyString()).next()).thenReturn(true);
        when(connection.createStatement().executeQuery(anyString()).getString("connection_id()")).thenReturn("test_connection_id");
        assertEquals("test_connection_id", obMySQLSessionExtension.getConnectionId(connection));
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionIdException() throws SQLException {
        when(connection.createStatement()).thenThrow(new SQLException("Test Exception"));
        obMySQLSessionExtension.getConnectionId(connection);
    }
}