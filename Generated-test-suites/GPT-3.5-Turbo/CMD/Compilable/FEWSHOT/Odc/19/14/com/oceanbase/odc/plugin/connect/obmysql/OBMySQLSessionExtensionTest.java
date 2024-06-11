package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

import org.junit.Test;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;

public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();

        extension.killQuery(connection, "12345");

        // Add assertion here
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();

        when(extension.getCurrentSchema(connection)).thenReturn("default");

        extension.switchSchema(connection, "new_schema");

        // Add assertion here
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();

        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class)).thenReturn("default");

        String currentSchema = extension.getCurrentSchema(connection);
        assertEquals("default", currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(OceanBaseConnection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();

        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class)).thenThrow(new SQLException());
        when(((OceanBaseConnection)connection).getServerThreadId()).thenReturn(12345L);

        String connectionId = extension.getConnectionId(connection);
        assertEquals("12345", connectionId);
    }
}