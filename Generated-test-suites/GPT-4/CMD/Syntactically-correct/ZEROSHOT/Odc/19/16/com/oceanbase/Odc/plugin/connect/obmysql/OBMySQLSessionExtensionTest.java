package com.oceanbase.odc.plugin.connect.obmysql;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;

public class OBMySQLSessionExtensionTest {
    private OBMySQLSessionExtension obMySQLSessionExtension;
    private Connection connection;
    private JdbcOperationsUtil.JdbcOperations jdbcOperations;

    @Before
    public void setUp() {
        obMySQLSessionExtension = new OBMySQLSessionExtension();
        connection = Mockito.mock(OceanBaseConnection.class);
        jdbcOperations = Mockito.mock(JdbcOperationsUtil.JdbcOperations.class);
        Mockito.when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperations);
    }

    @Test
    public void testKillQuery() {
        String connectionId = "1234";
        obMySQLSessionExtension.killQuery(connection, connectionId);
        Mockito.verify(jdbcOperations).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schema = "testSchema";
        Mockito.when(connection.getCatalog()).thenReturn(schema);
        obMySQLSessionExtension.switchSchema(connection, schema);
        Mockito.verify(connection).setCatalog(schema);
    }

    @Test
    public void testGetCurrentSchema() {
        String querySql = "SELECT DATABASE()";
        Mockito.when(jdbcOperations.queryForObject(querySql, String.class)).thenReturn("testSchema");
        String resultSchema = obMySQLSessionExtension.getCurrentSchema(connection);
        Assert.assertEquals("testSchema", resultSchema);
    }

    @Test
    public void testGetConnectionId() {
        String querySql = "select connection_id()";
        Mockito.when(jdbcOperations.queryForObject(querySql, String.class)).thenReturn("1234");
        String result = obMySQLSessionExtension.getConnectionId(connection);
        Assert.assertEquals("1234", result);
    }

    @Test
    public void testGetConnectionIdExceptionCase() {
        String querySql = "select connection_id()";
        Mockito.when(jdbcOperations.queryForObject(querySql, String.class)).thenThrow(new RuntimeException());
        Mockito.when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn((long) 5678);
        String result = obMySQLSessionExtension.getConnectionId(connection);
        Assert.assertEquals("5678", result);
    }
}
