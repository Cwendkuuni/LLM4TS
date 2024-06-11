package com.oceanbase.odc.plugin.connect.obmysql;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Test;
import org.mockito.Mockito;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

public class OBMySQLSessionExtensionTest {
    @Test
    public void testKillQuery() {
        String connectionId = "12345";
        OBMySQLSessionExtension ext = new OBMySQLSessionExtension();
        Connection mockConnection = Mockito.mock(Connection.class);
        JdbcOperationsUtil mockJdbcOps = Mockito.mock(JdbcOperationsUtil.class);
        
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection)).thenReturn(mockJdbcOps);
        
        ext.killQuery(mockConnection, connectionId);
        
        verify(mockJdbcOps).execute("KILL QUERY " + connectionId);
    }
    
    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test_schema";
        OBMySQLSessionExtension ext = new OBMySQLSessionExtension();
        Connection mockConnection = Mockito.mock(Connection.class);
        
        when(ext.getCurrentSchema(mockConnection)).thenReturn(schemaName);
        
        ext.switchSchema(mockConnection, schemaName);

        verify(mockConnection, Mockito.never()).setCatalog(schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        OBMySQLSessionExtension ext = new OBMySQLSessionExtension();
        Connection mockConnection = Mockito.mock(Connection.class);
        JdbcOperationsUtil mockJdbcOps = Mockito.mock(JdbcOperationsUtil.class);
        
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection)).thenReturn(mockJdbcOps);
        when(mockJdbcOps.queryForObject("SELECT DATABASE()", String.class)).thenReturn("test_schema");
        
        String currentSchema = ext.getCurrentSchema(mockConnection);
        
        assertEquals("test_schema", currentSchema);
    }
    
    @Test
    public void testGetConnectionId() {
        OBMySQLSessionExtension ext = new OBMySQLSessionExtension();
        Connection mockConnection = Mockito.mock(OceanBaseConnection.class);
        JdbcOperationsUtil mockJdbcOps = Mockito.mock(JdbcOperationsUtil.class);
        
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection)).thenReturn(mockJdbcOps);
        when(mockJdbcOps.queryForObject("select connection_id()", String.class)).thenThrow(new RuntimeException());
        
        String connectionId = "12345";
        when(((OceanBaseConnection) mockConnection).getServerThreadId()).thenReturn(Long.parseLong(connectionId));
        
        assertEquals(connectionId, ext.getConnectionId(mockConnection));
    }
}