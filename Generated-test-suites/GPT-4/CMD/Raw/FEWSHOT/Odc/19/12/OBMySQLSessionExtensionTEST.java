package com.oceanbase.odc.plugin.connect.obmysql;

import java.sql.Connection;

import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OBMySQLSessionExtensionTest {
    
    private OBMySQLSessionExtension oBMySQLSessionExtension = new OBMySQLSessionExtension();
    
    @Test
    public void killQueryTest() {
        Connection connection = Mockito.mock(Connection.class);
        JdbcOperationsUtil ops = Mockito.mock(JdbcOperationsUtil.class);

        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(ops);

        oBMySQLSessionExtension.killQuery(connection, "1234");

        verify(ops).execute("KILL QUERY 1234");
    }

    @Test
    public void switchSchemaTest() throws Exception {
        Connection connection = Mockito.mock(Connection.class);
        String schemaName = "mySchema";

        when(connection.getCatalog()).thenReturn("anotherSchema");

        oBMySQLSessionExtension.switchSchema(connection, schemaName);

        verify(connection).setCatalog(schemaName);
    }

    @Test
    public void getCurrentSchemaTest() {
        Connection connection = Mockito.mock(Connection.class);
        JdbcOperationsUtil ops = Mockito.mock(JdbcOperationsUtil.class);

        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(ops);
        when(ops.queryForObject("SELECT DATABASE()", String.class)).thenReturn("mySchema");

        String result = oBMySQLSessionExtension.getCurrentSchema(connection);

        assertEquals("mySchema", result);
    }
    
    @Test
    public void getConnectionIdTest() {
        Connection connection = Mockito.mock(Connection.class);
        JdbcOperationsUtil ops = Mockito.mock(JdbcOperationsUtil.class);

        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(ops);
        when(ops.queryForObject("select connection_id()", String.class)).thenReturn("1234");

        String result = oBMySQLSessionExtension.getConnectionId(connection);

        assertEquals("1234", result);
    }
}