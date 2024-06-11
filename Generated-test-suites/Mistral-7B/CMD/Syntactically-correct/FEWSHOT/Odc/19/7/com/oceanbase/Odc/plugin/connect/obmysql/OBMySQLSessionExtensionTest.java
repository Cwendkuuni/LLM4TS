package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

@RunWith(MockitoJUnitRunner.class)
public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = mock(Connection.class);
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        sessionExtension.setJdbcOperations(jdbcOperations);

        String connectionId = "12345";
        when(jdbcOperations.execute(eq("KILL QUERY " + connectionId))).thenReturn(1);

        sessionExtension.killQuery(connection, connectionId);
    }

    @Test(expected = SQLException.class)
    public void testKillQueryWithException() throws SQLException {
        Connection connection = mock(Connection.class);
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        sessionExtension.setJdbcOperations(jdbcOperations);

        String connectionId = "12345";
        when(jdbcOperations.execute(eq("KILL QUERY " + connectionId))).thenThrow(new SQLException("Test Exception"));

        sessionExtension.killQuery(connection, connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        sessionExtension.setJdbcOperations(jdbcOperations);

        String schemaName = "testSchema";
        String currentSchema = "oldSchema";

        when(jdbcOperations.queryForObject(eq("SELECT DATABASE()"), eq(String.class))).thenReturn(currentSchema);
        when(connection.setCatalog(eq(schemaName))).thenReturn(true);

        sessionExtension.switchSchema(connection, schemaName);

        assertEquals(schemaName, getCurrentSchema(connection));
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithException() throws SQLException {
        Connection connection = mock(Connection.class);
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        sessionExtension.setJdbcOperations(jdbcOperations);

        String schemaName = "testSchema";
        String currentSchema = "oldSchema";

        when(jdbcOperations.queryForObject(eq("SELECT DATABASE()"), eq(String.class))).thenReturn(currentSchema);
        when(connection.setCatalog(eq(schemaName))).thenThrow(new SQLException("Test Exception"));

        sessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        sessionExtension.setJdbcOperations(jdbcOperations);

        String currentSchema = "testSchema";
        when(jdbcOperations.queryForObject(eq("SELECT DATABASE()"), eq(String.class))).thenReturn(currentSchema);

        assertEquals(currentSchema, sessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionIdFromConnection() {
        Connection connection = mock(Connection.class);
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        sessionExtension.setJdbcOperations(jdbcOperations);

        String connectionId = "12345";
        when(jdbcOperations.queryForObject(eq("select connection_id()"), eq(String.class))).thenReturn(Optional.ofNullable(connectionId));

        assertEquals(connectionId, sessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdFromOceanBaseConnection() {
        Connection connection = mock(OceanBaseConnection.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();

        String connectionId = "12345";
        when(connection.getServerThreadId()).thenReturn(Long.parseLong(connectionId));

        assertEquals(connectionId, sessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdNullFromConnection() {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();

        assertNull(sessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdNullFromOceanBaseConnection() {
        Connection connection = mock(OceanBaseConnection.class);
        when(connection.getServerThreadId()).thenReturn(null);

        assertNull(sessionExtension.getConnectionId(connection));
    }

    private String getCurrentSchema(Connection connection) {
        try {
            return new OBMySQLSessionExtension().getCurrentSchema(connection);
        } catch (SQLException e) {
            return "";
        }
    }
}