
package com.oceanbase.odc.plugin.connect.obmysql;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class OBMySQLSessionExtensionTest {
    Connection mockConnection;
    OBMySQLSessionExtension obMySQLSessionExtension;

    @Before
    public void setUp() {
        mockConnection = Mockito.mock(Connection.class);
        obMySQLSessionExtension = new OBMySQLSessionExtension();
    }

    @Test
    public void testKillQuery() {
        String connectionId = "1234";
        obMySQLSessionExtension.killQuery(mockConnection, connectionId);

        verify(mockConnection, times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException{
        String schemaName = "testSchema";
        when(obMySQLSessionExtension.getCurrentSchema(mockConnection)).thenReturn("otherSchema");
        
        obMySQLSessionExtension.switchSchema(mockConnection, schemaName);

        verify(mockConnection, times(1)).setCatalog(schemaName);
    }

    @Test
    public void testSwitchSchemaSameSchema() throws SQLException {
        String schemaName = "testSchema";
        when(obMySQLSessionExtension.getCurrentSchema(mockConnection)).thenReturn(schemaName);

        obMySQLSessionExtension.switchSchema(mockConnection, schemaName);

        verify(mockConnection, times(0)).setCatalog(any());
    }

    @Test
    public void testGetCurrentSchema() {
        String expectedSchema = "currentSchema";
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject("SELECT DATABASE()", String.class)).
                thenReturn(expectedSchema);

        String actualSchema = obMySQLSessionExtension.getCurrentSchema(mockConnection);

        assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testGetConnectionId() {
        String expectedConnectionId = "1234";
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject(anyString(), eq(String.class))).
                thenReturn(expectedConnectionId);

        String actualConnectionId = obMySQLSessionExtension.getConnectionId(mockConnection);

        assertEquals(expectedConnectionId, actualConnectionId);
    }
}
