package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        String connectionId = "12345";

        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        obMySQLSessionExtension.killQuery(connection, connectionId);

        // verify that the execute method is called with the correct query
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";

        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        when(obMySQLSessionExtension.getCurrentSchema(connection)).thenReturn("old_schema");

        obMySQLSessionExtension.switchSchema(connection, schemaName);

        // verify that the setCatalog method is called with the correct schemaName
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String expectedSchema = "test_schema";

        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class)).thenReturn(expectedSchema);

        String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);

        assertEquals(expectedSchema, currentSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);
        String expectedConnectionId = "54321";

        OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class)).thenReturn(expectedConnectionId);

        String connectionId = obMySQLSessionExtension.getConnectionId(connection);

        assertEquals(expectedConnectionId, connectionId);
    }
}