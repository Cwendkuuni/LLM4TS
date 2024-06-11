package com.oceanbase.odc.plugin.connect.obmysql.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.oceanbase.jdbc.OperationContext;
import com.oceanbase.jdbc.core.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension.CurrentSchemaResultSetMapper;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension.KillQueryResultSetMapper;

public class OBMySQLSessionExtensionTest {

    private Connection connection;
    private JdbcOperationsUtil jdbcOperationsUtil;

    @Before
    public void setUp() throws SQLException {
        connection = mock(Connection.class);
        jdbcOperationsUtil = mock(JdbcOperationsUtil.class);
    }

    @Test
    public void testKillQuery() throws SQLException {
        OceanBaseConnection obConnection = (OceanBaseConnection) connection;
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(new OperationContext() {
            @Override
            public int executeUpdate(String sql) throws SQLException {
                return 1;
            }
        });
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.setJdbcOperationsUtil(jdbcOperationsUtil);
        extension.killQuery(connection, "connectionId");
        verifyKillQueryCall(connection, "KILL QUERY connectionId");
    }

    @Test(expected = SQLException.class)
    public void testKillQuery_exception() throws SQLException {
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenThrow(new SQLException("Test Exception"));
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.killQuery(connection, "connectionId");
    }

    @Test
    public void testSwitchSchema_sameSchema() throws SQLException {
        String currentSchema = "testSchema";
        when(jdbcOperationsUtil.queryForObject(connection, "SELECT DATABASE()", String.class)).thenReturn(currentSchema);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.switchSchema(connection, currentSchema);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchema_differentSchema_sqlException() throws SQLException {
        when(jdbcOperationsUtil.queryForObject(connection, "SELECT DATABASE()", String.class)).thenReturn("testSchema");
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenThrow(new SQLException("Test Exception"));
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.switchSchema(connection, "newSchema");
    }

    @Test
    public void testSwitchSchema_differentSchema_success() throws SQLException {
        when(jdbcOperationsUtil.queryForObject(connection, "SELECT DATABASE()", String.class)).thenReturn("testSchema");
        when(connection.setCatalog("newSchema")).thenReturn(true);
        when(jdbcOperationsUtil.queryForObject(connection, "SELECT DATABASE()", String.class)).thenReturn("newSchema");
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        extension.switchSchema(connection, "newSchema");
        verifySchemaSwitchCall(connection, "newSchema");
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String currentSchema = "testSchema";
        when(jdbcOperationsUtil.queryForObject(connection, "SELECT DATABASE()", String.class)).thenReturn(currentSchema);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        assertEquals(currentSchema, extension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId_OceanBaseConnection() throws SQLException {
        String connectionId = "connectionId";
        when(jdbcOperationsUtil.queryForObject(connection, "select connection_id()", String.class)).thenReturn(null);
        when(obConnection.getServerThreadId()).thenReturn(connectionId);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        assertEquals(connectionId, extension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionId_Exception() throws SQLException {
        when(jdbcOperationsUtil.queryForObject(connection, "select connection_id()", String.class)).thenThrow(new SQLException("Test Exception"));
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        String connectionId = ((OceanBaseConnection) connection).getServerThreadId() + "";
        assertEquals(connectionId, extension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionId_null() throws SQLException {
        when(jdbcOperationsUtil.queryForObject(connection, "select connection_id()", String.class)).thenReturn(null);
        OBMySQLSessionExtension extension = new OBMySQLSessionExtension();
        assertNull(extension.getConnectionId(connection));
    }

    private void verifyKillQueryCall(Connection connection, String sql) {
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                assertEquals(1, args.length);
                String arg = (String) args[0];
                assertEquals(sql, arg);
                return 1;
            }
        });
    }

    private void verifySchemaSwitchCall(Connection connection, String schemaName) {
        when(connection.getCatalog()).thenAnnotatedReturnValue(Optional.of(schemaName));
        when(jdbcOperationsUtil.queryForObject(connection, "SELECT DATABASE()", String.class)).thenReturn(schemaName);
    }
}