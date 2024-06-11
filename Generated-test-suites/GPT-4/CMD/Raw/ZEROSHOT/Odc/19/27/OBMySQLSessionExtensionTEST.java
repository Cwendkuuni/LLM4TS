
package com.oceanbase.odc.plugin.connect.obmysql;

import org.junit.Before;
import org.junit.Test;
import java.sql.Connection;
import java.sql.SQLException;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;

public class OBMySQLSessionExtensionTest {
    private OBMySQLSessionExtension obMySQLSessionExtension;
    private Connection connection;
    private String querySql, connectionId, schemaName, currentSchema;

    @Before
    public void setUp() {
        obMySQLSessionExtension = new OBMySQLSessionExtension();
        connection = mock(Connection.class);
        querySql = "SELECT DATABASE()";
        connectionId = "1";
        schemaName = "testSchema";
        currentSchema = "testSchema";
    }

    @Test
    public void testKillQuery() {
        obMySQLSessionExtension.killQuery(connection, connectionId);
        // Verifying that the killQuery method has been called
        verify(connection, times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema_SameSchema() throws SQLException {
        when(obMySQLSessionExtension.getCurrentSchema(connection)).thenReturn(currentSchema);
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        // No actions should have been performed on the connection
        verify(connection, never()).setCatalog(anyString());
    }

    @Test
    public void testSwitchSchema_DifferentSchema() throws SQLException {
        when(obMySQLSessionExtension.getCurrentSchema(connection)).thenReturn("anotherSchema");
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        // setCatalog should have been called with the new schema
        verify(connection).setCatalog(schemaName);
    }

    @Test
    public void testGettCurrentSchema(){
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(querySql, String.class)).thenReturn(currentSchema);
        String result = obMySQLSessionExtension.getCurrentSchema(connection);
        assertEquals(currentSchema, result);
    }

    @Test
    public void testGetConnectionId(){
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(querySql, String.class)).thenReturn(connectionId);
        String result = obMySQLSessionExtension.getConnectionId(connection);
        assertEquals(connectionId, result);
    }
}
