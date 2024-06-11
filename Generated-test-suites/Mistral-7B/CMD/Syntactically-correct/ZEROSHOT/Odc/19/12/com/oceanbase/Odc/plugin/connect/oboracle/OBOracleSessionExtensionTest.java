package com.oceanbase.odc.plugin.connect.oboracle.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension.SessionExtensionPoint;

public class OBOracleSessionExtensionTest {

    private JdbcOperations jdbcOperations;
    private OBOracleSessionExtension obOracleSessionExtension;
    private Connection connection;

    @Before
    public void setup() {
        jdbcOperations = mock(JdbcOperations.class);
        obOracleSessionExtension = new OBOracleSessionExtension();
        obOracleSessionExtension.setJdbcOperations(jdbcOperations);
        connection = mock(Connection.class);
    }

    @Test
    public void testKillQuery() throws SQLException {
        when(jdbcOperations.execute(Mockito.anyString())).thenReturn(true);
        obOracleSessionExtension.killQuery(connection, "testConnectionId");
        Mockito.verify(jdbcOperations).execute("KILL QUERY testConnectionId");
    }

    @Test(expected = SQLException.class)
    public void testKillQueryWithException() throws SQLException {
        when(jdbcOperations.execute(Mockito.anyString())).thenThrow(new SQLException("Test Exception"));
        obOracleSessionExtension.killQuery(connection, "testConnectionId");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String newSchemaName = "testSchema";
        when(jdbcOperations.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(newSchemaName);
        obOracleSessionExtension.switchSchema(connection, newSchemaName);
        Mockito.verify(jdbcOperations).update("ALTER SESSION SET CURRENT_SCHEMA=" + newSchemaName);
    }

    @Test
    public void testSwitchSchemaWithCurrentSchemaSame() throws SQLException {
        String currentSchema = "testSchema";
        when(jdbcOperations.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(currentSchema);
        obOracleSessionExtension.switchSchema(connection, currentSchema);
        Mockito.verify(jdbcOperations, Mockito.times(0)).update("ALTER SESSION SET CURRENT_SCHEMA=" + currentSchema);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithException() throws SQLException {
        when(jdbcOperations.update(Mockito.anyString())).thenThrow(new SQLException("Test Exception"));
        obOracleSessionExtension.switchSchema(connection, "testSchema");
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String currentSchema = "testSchema";
        when(jdbcOperations.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(currentSchema);
        assertEquals(currentSchema, obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String connectionId = "testConnectionId";
        when(jdbcOperations.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(connectionId);
        assertEquals(connectionId, obOracleSessionExtension.getConnectionId(connection));

        when(connection instanceof OceanBaseConnection).thenReturn(true);
        String oceanBaseConnectionThreadId = "testThreadId";
        when(connection.getClass().getMethod("getServerThreadId")).thenReturn(oceanBaseConnectionThreadId);
        assertEquals(oceanBaseConnectionThreadId, obOracleSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdWithNullConnectionId() throws SQLException {
        when(jdbcOperations.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(null);
        assertEquals("", obOracleSessionExtension.getConnectionId(connection));

        when(connection instanceof OceanBaseConnection).thenReturn(true);
        String oceanBaseConnectionThreadId = "testThreadId";
        when(connection.getClass().getMethod("getServerThreadId")).thenReturn(oceanBaseConnectionThreadId);
        assertEquals(oceanBaseConnectionThreadId, obOracleSessionExtension.getConnectionId(connection));
    }
}