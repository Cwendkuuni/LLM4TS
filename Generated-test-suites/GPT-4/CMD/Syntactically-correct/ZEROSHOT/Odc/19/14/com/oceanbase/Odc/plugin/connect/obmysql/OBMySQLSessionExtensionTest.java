package com.oceanbase.odc.plugin.connect.obmysql;

import org.junit.*;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import java.sql.Connection;
import java.sql.SQLException;

public class OBMySQLSessionExtensionTest {

    private OBMySQLSessionExtension obMySqlSessionExt;
    private Connection mockConnection;

    @Before
    public void setUp() {
        obMySqlSessionExt = new OBMySQLSessionExtension();
        mockConnection = Mockito.mock(Connection.class);
    }

    @Test
    public void testKillQuery() {
        String connectionID = "123";

        obMySqlSessionExt.killQuery(mockConnection, connectionID);

        verify(mockConnection, times(1)).execute("KILL QUERY " + connectionID);
    }

    @Test
    public void testSwitchSchema() throws SQLException {   
        String schemaNameSame = "schemaName";
        String schemaNameDifferent = "differentName";

        when(obMySqlSessionExt.getCurrentSchema(mockConnection)).thenReturn(schemaNameSame);

        obMySqlSessionExt.switchSchema(mockConnection, schemaNameSame);
        verify(mockConnection, times(0)).setCatalog(schemaNameSame);

        obMySqlSessionExt.switchSchema(mockConnection, schemaNameDifferent);
        verify(mockConnection, times(1)).setCatalog(schemaNameDifferent);
    }

    @Test
    public void testGetCurrentSchema() {
      
        String querySql = "SELECT DATABASE()";

        obMySqlSessionExt.getCurrentSchema(mockConnection);
        
        verify(mockConnection, times(1)).queryForObject(querySql, String.class);
    }

    @Test
    public void testGetConnectionId() {
      
        String querySql = "select connection_id()";

        obMySqlSessionExt.getConnectionId(mockConnection);
        
        verify(mockConnection, times(1)).queryForObject(querySql, String.class);
    }
}
