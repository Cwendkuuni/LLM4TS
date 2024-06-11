package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

public class OBMySQLSessionExtensionTest {

    private JdbcOperations jdbcOperations;
    private OBMySQLSessionExtension obMySQLSessionExtension;
    private Connection connection;

    @Before
    public void setUp() {
        jdbcOperations = Mockito.mock(JdbcOperations.class);
        obMySQLSessionExtension = new OBMySQLSessionExtension();
        connection = Mockito.mock(Connection.class);
        obMySQLSessionExtension.setJdbcOperations(jdbcOperations);
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "12345";
        when(jdbcOperations.execute(eq("KILL QUERY " + connectionId))).thenReturn(1);
        obMySQLSessionExtension.killQuery(connection, connectionId);
        verify(jdbcOperations).execute(eq("KILL QUERY " + connectionId));
    }

    @Test(expected = SQLException.class)
    public void testKillQuery_exception() throws SQLException {
        String connectionId = "12345";
        when(jdbcOperations.execute(eq("KILL QUERY " + connectionId))).thenThrow(new SQLException());
        obMySQLSessionExtension.killQuery(connection, connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test_schema";
        String currentSchema = "current_schema";
        when(jdbcOperations.queryForObject(eq("SELECT DATABASE()"), eq(String.class))).thenReturn(currentSchema);
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        verify(connection).setCatalog(schemaName);
        assertEquals(schemaName, connection.getCatalog());
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchema_exception() throws SQLException {
        String schemaName = "test_schema";
        when(jdbcOperations.queryForObject(eq("SELECT DATABASE()"), eq(String.class))).thenThrow(new SQLException());
        obMySQLSessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testgetCurrentSchema() throws SQLException {
        String currentSchema = "test_schema";
        when(jdbcOperations.queryForObject(eq("SELECT DATABASE()"), eq(String.class))).thenReturn(currentSchema);
        assertEquals(currentSchema, obMySQLSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId_JdbcOperations() throws SQLException {
        String connectionId = "12345";
        when(jdbcOperations.queryForObject(eq("select connection_id()"), eq(String.class))).thenReturn(connectionId);
        assertEquals(connectionId, obMySQLSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionId_OceanBaseConnection() {
        String connectionId = "12345";
        when(connection.getClass()).thenReturn(OceanBaseConnection.class);
        String serverThreadId = "67890";
        when(connection.getServerThreadId()).thenReturn(serverThreadId.toCharArray());
        assertEquals(connectionId, obMySQLSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionId_null() {
        assertNull(obMySQLSessionExtension.getConnectionId(null));
    }
}