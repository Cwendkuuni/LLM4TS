package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.mockito.MocksContract;

import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import com.oceanbase.jdbc.JdbcOperations;

@MocksContract(initializationMode = MocksContract.INIT_MOCKS_LAZILY)
public class OBMySQLSessionExtensionTest {

    private JdbcOperations jdbcOperations = mock(JdbcOperations.class);
    private Connection connection = mock(Connection.class);

    @Test
    public void testKillQuery() throws SQLException {
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        when(connection.createStatement()).thenReturn(jdbcOperations.createStatement());

        obMySQLSessionExtension.killQuery(connection, "connectionId");
        when(jdbcOperations.execute(eq("KILL QUERY connectionId"))).thenReturn(1);
        assertEquals(1, obMySQLSessionExtension.killQuery(connection, "connectionId"));
    }

    @Test(expected = SQLException.class)
    public void testKillQueryThrowsException() throws SQLException {
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        when(connection.createStatement()).thenReturn(jdbcOperations.createStatement());

        obMySQLSessionExtension.killQuery(connection, "connectionId");
        when(jdbcOperations.execute(eq("KILL QUERY connectionId"))).thenThrow(new SQLException());
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        when(jdbcOperations.queryForObject(eq("SELECT DATABASE()"), eq(String.class)))
                .thenReturn("currentSchema");

        obMySQLSessionExtension.switchSchema(connection, "newSchema");
        when(connection.setCatalog("newSchema")).thenReturn(true);
        assertEquals("newSchema", obMySQLSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testSwitchSchemaNoChange() throws SQLException {
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        when(jdbcOperations.queryForObject(eq("SELECT DATABASE()"), eq(String.class)))
                .thenReturn("currentSchema");

        obMySQLSessionExtension.switchSchema(connection, "currentSchema");
        assertNull(obMySQLSessionExtension.getCurrentSchema(connection));
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaThrowsException() throws SQLException {
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        when(jdbcOperations.queryForObject(eq("SELECT DATABASE()"), eq(String.class)))
                .thenReturn("currentSchema");

        obMySQLSessionExtension.switchSchema(connection, "newSchema");
        when(connection.setCatalog("newSchema")).thenThrow(new SQLException());
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        when(jdbcOperations.queryForObject(eq("SELECT DATABASE()"), eq(String.class)))
                .thenReturn("currentSchema");

        assertEquals("currentSchema", obMySQLSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();

        assertEquals("connectionId", obMySQLSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdNull() {
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();

        assertNull(obMySQLSessionExtension.getConnectionId(null));
    }

    @Test
    public void testGetConnectionIdOceanBaseConnection() {
        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();

        String serverThreadId = "threadId";
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        when(connection.getServerThreadId()).thenReturn(serverThreadId);

        assertEquals(serverThreadId, obMySQLSessionExtension.getConnectionId(connection));
    }
}