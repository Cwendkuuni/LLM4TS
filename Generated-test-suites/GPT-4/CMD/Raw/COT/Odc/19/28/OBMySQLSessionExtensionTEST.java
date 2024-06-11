
package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

public class OBMySQLSessionExtensionTest {
    private final OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
    private final Connection mockConnection = mock(Connection.class);

    @Test
    public void killQueryTest() {
        String connectionId = "12345";
        obMySQLSessionExtension.killQuery(mockConnection, connectionId);

        verify(mockConnection).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void switchSchemaTest() throws SQLException {
        String schemaName = "schema";
        when(obMySQLSessionExtension.getCurrentSchema(mockConnection)).thenReturn(schemaName);
        
        obMySQLSessionExtension.switchSchema(mockConnection, schemaName);
       
        verify(mockConnection, never()).setCatalog(anyString());
    }
    
    @Test
    public void switchSchemaTest_differentSchema() throws SQLException {
        String currentSchema = "currentSchema";
        String newSchema = "newSchema";
        when(obMySQLSessionExtension.getCurrentSchema(mockConnection)).thenReturn(currentSchema);

        obMySQLSessionExtension.switchSchema(mockConnection, newSchema);

        verify(mockConnection).setCatalog(newSchema);
    }

    @Test
    public void getCurrentSchemaTest() throws SQLException {
        String expectedSchema = "schema";
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject("SELECT DATABASE()", String.class)).thenReturn(expectedSchema);

        String actualSchema = obMySQLSessionExtension.getCurrentSchema(mockConnection);

        assertEquals(expectedSchema, actualSchema);
    }

    @Test 
    public void getConnectionIdTest() throws SQLException {
        String expectedConnId = "12345";
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject("select connection_id()", String.class)).thenReturn(expectedConnId);
        
        String actualConnId = obMySQLSessionExtension.getConnectionId(mockConnection);
        
        verify(mockConnection).queryForObject("select connection_id()", String.class);
        assertEquals(expectedConnId, actualConnId);
    }

    @Test
    public void getConnectionIdTest_OceanBaseConnectionException() throws SQLException {
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject("select connection_id()", String.class)).thenThrow(new RuntimeException());
        
        String actualConnId = obMySQLSessionExtension.getConnectionId(mockConnection);
        
        assertNotNull(actualConnId);
    }
}
