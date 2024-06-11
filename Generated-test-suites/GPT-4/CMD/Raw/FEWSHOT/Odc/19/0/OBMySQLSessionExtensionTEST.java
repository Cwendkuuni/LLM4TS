package com.oceanbase.odc.plugin.connect.obmysql;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import org.springframework.jdbc.core.JdbcOperations;

public class OBMySQLSessionExtensionTest {
    private OBMySQLSessionExtension extension;
    private JdbcOperationsUtil jdbcOperationsUtil;
    private Connection connection;
    private JdbcOperations operations;

    @Before
    public void setUp() {
        extension = new OBMySQLSessionExtension();
        jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        connection = mock(Connection.class);
        operations = mock(JdbcOperations.class);

        when(jdbcOperationsUtil.getJdbcOperations(any(Connection.class))).thenReturn(operations);
    }

    @Test
    public void testKillQuery() {
        extension.killQuery(connection, "1234");
        verify(operations, times(1)).execute("KILL QUERY 1234");
    }

    @Test
    public void testGetCurrentSchema() {
        when(operations.queryForObject("SELECT DATABASE()", String.class)).thenReturn("schemaName");
        String currentSchema = extension.getCurrentSchema(connection);
        assertEquals("schemaName", currentSchema);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        when(extension.getCurrentSchema(connection)).thenReturn("oldSchema");
        extension.switchSchema(connection, "newSchema");
        verify(connection, times(1)).setCatalog("newSchema");
    }

    @Test
    public void testGetConnectionId_FromJdbcOperations() {
        when(operations.queryForObject("select connection_id()", String.class)).thenReturn("1234");
        String connectionId = extension.getConnectionId(connection);
        assertEquals("1234", connectionId);
    }

    @Test
    public void testGetConnectionId_FromOceanBaseConnection() {
        OceanBaseConnection obConnection = mock(OceanBaseConnection.class);
        when(obConnection.getServerThreadId()).thenReturn(1234L);
        String connectionId = extension.getConnectionId(obConnection);
        assertEquals("1234", connectionId);
    }
}