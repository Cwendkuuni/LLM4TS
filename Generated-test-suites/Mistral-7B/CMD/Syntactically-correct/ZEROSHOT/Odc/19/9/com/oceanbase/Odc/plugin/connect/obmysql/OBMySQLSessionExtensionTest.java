package com.oceanbase.odc.plugin.connect.obmysql.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.ExtensionManager;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

@SuppressWarnings("deprecation")
public class OBMySQLSessionExtensionTest {

    private Connection connection;
    private SessionExtensionPoint extension;

    @Before
    public void setUp() throws SQLException {
        connection = mock(Connection.class);
        extension = new OBMySQLSessionExtension();
        ExtensionManager manager = mock(ExtensionManager.class);
        when(extension.getContext()).thenReturn(manager);
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "test_connection_id";
        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));

        extension.killQuery(connection, connectionId);

        String querySql = "KILL QUERY " + connectionId;
        JdbcOperationsUtil jdbcOperations = JdbcOperationsUtil.getJdbcOperations(connection);
        assertEquals(querySql, jdbcOperations.getLastExecutedQuery());
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test_schema";
        String currentSchema = "old_schema";

        when(connection.getCatalog()).thenReturn(currentSchema);
        when(connection.setCatalog(schemaName)).thenReturn(connection);

        extension.switchSchema(connection, schemaName);

        assertEquals(schemaName, connection.getCatalog());
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaAlreadyOnSchema() throws SQLException {
        String schemaName = "test_schema";
        String currentSchema = schemaName;

        when(connection.getCatalog()).thenReturn(currentSchema);

        extension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String currentSchema = "test_schema";

        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));
        when(connection.createStatement().executeQuery(anyString())).thenReturn(mock(java.sql.ResultSet.class));

        when(connection.createStatement().executeQuery(anyString()).next()).thenReturn(true);
        when(connection.createStatement().executeQuery(anyString()).getString("DATABASE())"))
                .thenReturn(currentSchema);

        assertEquals(currentSchema, extension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String connectionId = "test_connection_id";

        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));

        when(connection.createStatement().executeQuery(anyString())).thenReturn(mock(java.sql.ResultSet.class));
        when(connection.createStatement().executeQuery(anyString()).next()).thenReturn(false);

        when(connection instanceof OceanBaseConnection).thenReturn(true);

        extension.getConnectionId(connection);

        assertEquals(connectionId, extension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdNotOceanBaseConnection() throws SQLException {
        when(connection.createStatement()).thenReturn(mock(java.sql.Statement.class));

        when(connection.createStatement().executeQuery(anyString())).thenReturn(mock(java.sql.ResultSet.class));
        when(connection.createStatement().executeQuery(anyString()).next()).thenReturn(false);

        when(connection instanceof OceanBaseConnection).thenReturn(false);

        extension.getConnectionId(connection);

        String querySql = "select connection_id()";
        String connectionId = JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(querySql, String.class);
        assertEquals(connectionId, extension.getConnectionId(connection));
    }
}