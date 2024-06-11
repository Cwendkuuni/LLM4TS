package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.impl.DefaultExtension;
import org.pf4j.impl.DefaultExtensionPoint;

import com.oceanbase.jdbc.JdbcOperations;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

@SuppressWarnings("unchecked")
public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        String connectionId = "connection_123";
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        SessionExtensionPoint point = new DefaultExtensionPoint();
        point.setExtension(extension);

        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(connection.unwrap(JdbcOperations.class)).thenReturn(jdbcOperations);

        extension.killQuery(connection, connectionId);

        String querySql = "KILL QUERY " + connectionId;
        when(jdbcOperations.execute(querySql)).thenReturn(1);
        extension.killQuery(connection, connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        SessionExtensionPoint point = new DefaultExtensionPoint();
        point.setExtension(extension);

        String schemaName = "test_schema";
        String currentSchema = "current_schema";
        when(extension.getCurrentSchema(connection)).thenReturn(currentSchema);

        extension.switchSchema(connection, schemaName);

        assertEquals(schemaName, currentSchema);
        assertEquals(schemaName, connection.getCatalog());
    }

    @Test
    public void testSwitchSchema_SameSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        SessionExtensionPoint point = new DefaultExtensionPoint();
        point.setExtension(extension);

        String schemaName = "test_schema";
        when(extension.getCurrentSchema(connection)).thenReturn(schemaName);

        extension.switchSchema(connection, schemaName);

        assertNull(connection.getCatalog());
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchema_Error() throws SQLException {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        SessionExtensionPoint point = new DefaultExtensionPoint();
        point.setExtension(extension);

        String schemaName = "test_schema";
        String querySql = "SELECT DATABASE()";
        when(extension.getCurrentSchema(connection)).thenThrow(new SQLException("Error getting current schema"));

        extension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();

        String querySql = "SELECT DATABASE()";
        String schemaName = "test_schema";
        when(extension.queryForObject(connection, querySql, String.class)).thenReturn(schemaName);

        assertEquals(schemaName, extension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();

        String querySql = "select connection_id()";
        String connectionId = "connection_123";
        when(extension.queryForObject(connection, querySql, String.class)).thenReturn(connectionId);

        assertEquals(connectionId, extension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionId_Null() {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();

        String connectionId = null;
        when(extension.getConnectionId(connection)).thenReturn(connectionId);

        assertNull(connectionId);
    }

    @Test
    public void testGetConnectionId_OceanBaseConnection() {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();

        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(connection.unwrap(OceanBaseConnection.class)).thenReturn(oceanBaseConnection);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(123L);

        assertEquals("123", extension.getConnectionId(connection));
    }
}