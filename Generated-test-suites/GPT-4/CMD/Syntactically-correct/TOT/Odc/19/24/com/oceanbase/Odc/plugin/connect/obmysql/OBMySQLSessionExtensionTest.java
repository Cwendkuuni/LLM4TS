package com.oceanbase.odc.plugin.connect.obmysql;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;

@RunWith(MockitoJUnitRunner.class)
public class OBMySQLSessionExtensionTest {

    @InjectMocks
    private OBMySQLSessionExtension obMySQLSessionExtension;
    
    @Mock 
    private Connection connection;
    
    @Mock
    private OceanBaseConnection oceanBaseConnection;
    
    @Mock
    private JdbcOperationsUtil jdbcOperationsUtil;

    @Test
    public void testKillQuery() {
        String connectionId = "1";
        obMySQLSessionExtension.killQuery(connection, connectionId);
        verify(jdbcOperationsUtil).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testSchema";
        when(obMySQLSessionExtension.getCurrentSchema(connection)).thenReturn(schemaName);
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        verify(connection, times(1)).setCatalog(anyString());
    }

    @Test
    public void testGetCurrentSchema() {
        String querySql = "SELECT DATABASE()";
        when(jdbcOperationsUtil.queryForObject(querySql, String.class)).thenReturn("testSchema");
        String result = obMySQLSessionExtension.getCurrentSchema(connection);
        assertEquals("testSchema", result);
    }

    @Test
    public void testGetConnectionId() {
        String querySql = "select connection_id()";
        when(jdbcOperationsUtil.queryForObject(querySql, String.class)).thenReturn("1");
        String result = obMySQLSessionExtension.getConnectionId(connection);
        assertEquals("1", result);

        when(jdbcOperationsUtil.queryForObject(querySql, String.class)).thenThrow(new RuntimeException());
        when(oceanBaseConnection.getServerThreadId()).thenReturn(1L);
        result = obMySQLSessionExtension.getConnectionId(oceanBaseConnection);
        assertEquals("1", result);
    }
}