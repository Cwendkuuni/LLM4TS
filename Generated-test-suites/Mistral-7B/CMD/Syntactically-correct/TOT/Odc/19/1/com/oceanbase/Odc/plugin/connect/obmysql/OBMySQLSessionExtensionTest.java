package com.oceanbase.odc.plugin.connect.obmysql.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

class OBMySQLSessionExtensionTest {

    private SessionExtensionPoint extension;
    private JdbcOperations jdbcOperationsMock;

    @Before
    public void init() {
        jdbcOperationsMock = Mockito.mock(JdbcOperations.class);
        extension = new OBMySQLSessionExtension() {
            {
                setJdbcOperations(jdbcOperationsMock);
            }
        };
    }

    @Test
    void testKillQuery() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        String connectionId = "1234";
        when(jdbcOperationsMock.execute(eq("KILL QUERY " + connectionId))).thenReturn(1);

        extension.killQuery(connection, connectionId);
        verify(jdbcOperationsMock).execute("KILL QUERY " + connectionId);
    }

    @Test
    void testKillQuery_connectionError() {
        Connection connection = Mockito.mock(Connection.class);
        String connectionId = "1234";
        when(jdbcOperationsMock.execute(eq("KILL QUERY " + connectionId))).thenThrow(new SQLException());

        assertThrows(SQLException.class, () -> extension.killQuery(connection, connectionId));
        verify(jdbcOperationsMock).execute("KILL QUERY " + connectionId);
    }

    @Test
    void testSwitchSchema_sameSchema() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        String schemaName = "testSchema";
        String currentSchema = schemaName;
        when(jdbcOperationsMock.queryForObject(eq("SELECT DATABASE()"), eq(String.class))).thenReturn(currentSchema);

        extension.switchSchema(connection, schemaName);
        verifyNoInteractions(jdbcOperationsMock);
    }

    @Test
    void testSwitchSchema_differentSchema() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        String schemaName = "testSchema";
        String currentSchema = "defaultSchema";
        when(jdbcOperationsMock.queryForObject(eq("SELECT DATABASE()"), eq(String.class))).thenReturn(currentSchema);

        extension.switchSchema(connection, schemaName);
        verify(connection).setCatalog(schemaName);
    }

    @Test
    void testGetCurrentSchema() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        String currentSchema = "testSchema";
        when(jdbcOperationsMock.queryForObject(eq("SELECT DATABASE()"), eq(String.class))).thenReturn(currentSchema);

        assertEquals(currentSchema, extension.getCurrentSchema(connection));
        verify(jdbcOperationsMock).queryForObject(eq("SELECT DATABASE()"), eq(String.class));
    }

    @Test
    void testGetConnectionId_connectionId() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        String connectionId = "1234";

        assertEquals(connectionId, extension.getConnectionId(connection));
    }

    @Test
    void testGetConnectionId_noConnectionId() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);

        assertEquals(null, extension.getConnectionId(connection));
    }
}