package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.impl.DefaultExtensionPoint;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import com.oceanbase.odc.plugin.connect.obmysql.JdbcOperationsUtil;

@SuppressWarnings("unchecked")
public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();

        JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        when(jdbcOperationsUtil.execute(connection, "KILL QUERY 123")).thenReturn(true);
        when(connection.getMetaData()).thenReturn(jdbcOperationsUtil);

        extension.killQuery(connection, "123");

        // No exception is thrown
    }

    @Test(expected = SQLException.class)
    public void testKillQueryWithException() {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();

        JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        when(jdbcOperationsUtil.execute(connection, "KILL QUERY 123")).thenThrow(new SQLException("Error killing query"));
        when(connection.getMetaData()).thenReturn(jdbcOperationsUtil);

        extension.killQuery(connection, "123");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();

        String currentSchema = "currentSchema";
        when(connection.getCatalog()).thenReturn(currentSchema);

        String newSchema = "newSchema";
        extension.switchSchema(connection, newSchema);

        assertEquals(newSchema, connection.getCatalog());
    }

    @Test
    public void testSwitchSchemaWithSameSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();

        String currentSchema = "currentSchema";
        when(connection.getCatalog()).thenReturn(currentSchema);

        extension.switchSchema(connection, currentSchema);

        assertEquals(currentSchema, connection.getCatalog());
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        String currentSchema = "currentSchema";

        JdbcOperationsUtil jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        when(jdbcOperationsUtil.queryForObject(connection, "SELECT DATABASE()", String.class)).thenReturn(currentSchema);
        when(connection.getMetaData()).thenReturn(jdbcOperationsUtil);

        assertEquals(currentSchema, extension.getCurrentSchema(connection));
    }

    @Test
    public void testGetCurrentSchemaWithNullConnection() {
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();

        assertNull(extension.getCurrentSchema(null));
    }

    @Test
    public void testGetConnectionIdWithValidConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();

        String connectionId = "connectionId";
        when(connection.getMetaData()).thenReturn(new DefaultExtensionPoint());
        when(connection.getAutoCommit()).thenReturn(true);
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(123L);

        assertEquals(connectionId, extension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdWithInvalidConnection() {
        OBMySQLSessionExtension extension = newOBMySQLSessionExtension();

        assertNull(extension.getConnectionId(null));
    }
}