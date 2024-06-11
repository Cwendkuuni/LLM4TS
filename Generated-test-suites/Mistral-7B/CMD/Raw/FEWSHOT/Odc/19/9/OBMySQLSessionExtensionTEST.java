package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

public class OBMySQLSessionExtensionTest {

    private JdbcOperations jdbcOperations;
    private Connection connection;

    @Before
    public void setup() {
        jdbcOperations = mock(JdbcOperations.class);
        connection = mock(Connection.class);
    }

    @Test
    public void testKillQuery() throws SQLException {
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        obMySQLSessionExtension.killQuery(connection, "connectionId");
        when(jdbcOperations.execute(Mockito.eq("KILL QUERY connectionId"))).thenReturn(1);
        obMySQLSessionExtension.setJdbcOperations(jdbcOperations);
        assertEquals(1, obMySQLSessionExtension.killQuery(connection, "connectionId"));
    }

    @Test(expected = SQLException.class)
    public void testKillQuery_throwsException() throws SQLException {
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        obMySQLSessionExtension.killQuery(connection, "connectionId");
        when(jdbcOperations.execute(Mockito.eq("KILL QUERY connectionId"))).thenThrow(new SQLException("KILL QUERY error."));
        obMySQLSessionExtension.setJdbcOperations(jdbcOperations);
        obMySQLSessionExtension.killQuery(connection, "connectionId");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        String schemaName = "testSchema";
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        when(connection.getCatalog()).thenReturn(Objects.isNull(schemaName) ? null : schemaName);
        obMySQLSessionExtension.setJdbcOperations(jdbcOperations);
        assertEquals(schemaName, obMySQLSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testSwitchSchema_alreadyOnSchema() throws SQLException {
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        String schemaName = "testSchema";
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        when(connection.getCatalog()).thenReturn(schemaName);
        obMySQLSessionExtension.setJdbcOperations(jdbcOperations);
        assertEquals(schemaName, obMySQLSessionExtension.getCurrentSchema(connection));
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchema_throwsException() throws SQLException {
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        String schemaName = "testSchema";
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        when(jdbcOperations.queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenThrow(new SQLException("Query error."));
        obMySQLSessionExtension.setJdbcOperations(jdbcOperations);
        obMySQLSessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        String schemaName = "testSchema";
        when(jdbcOperations.queryForObject(Mockito.eq("SELECT DATABASE()"), Mockito.eq(String.class))).thenReturn(schemaName);
        obMySQLSessionExtension.setJdbcOperations(jdbcOperations);
        assertEquals(schemaName, obMySQLSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        String connectionId = "123";
        when(connection.isWrapperFor(OceanBaseConnection.class)).thenReturn(true);
        when(connection.getServerThreadId()).thenReturn(123);
        obMySQLSessionExtension.setJdbcOperations(jdbcOperations);
        assertEquals(connectionId, obMySQLSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionId_noOceanBaseConnection() throws SQLException {
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        obMySQLSessionExtension.setJdbcOperations(jdbcOperations);
        assertNull(obMySQLSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionId_executionException() throws SQLException {
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        String connectionId = null;
        when(connection.isWrapperFor(OceanBaseConnection.class)).thenReturn(false);
        try {
            connectionId = obMySQLSessionExtension.getConnectionId(connection);
        } catch (Exception e) {
            connectionId = e.getMessage();
        }
        assertEquals("select connection_id()", connectionId);
    }
}