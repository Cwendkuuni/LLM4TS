package com.oceanbase.odc.plugin.connect.obmysql;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.util.JdbcOperationsUtil;

import static org.junit.Assert.*;

public class OBMySQLSessionExtensionTest {

    @InjectMocks
    private OBMySQLSessionExtension obMySQLSessionExtension;
    
    @Mock
    private Connection mockConnection;
    
    @Mock
    private OceanBaseConnection mockOceanBaseConnection;
   
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testKillQuery() {
        obMySQLSessionExtension.killQuery(mockConnection, "testId");
        Mockito.verify(mockConnection, Mockito.times(1)).execute("KILL QUERY testId");
    }
    
    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "schema";
        Mockito.when(mockConnection.getCatalog()).thenReturn(schemaName);
        obMySQLSessionExtension.switchSchema(mockConnection, schemaName);
        Mockito.verify(mockConnection, Mockito.times(0)).setCatalog(anyString());

        obMySQLSessionExtension.switchSchema(mockConnection, "newSchema");
        Mockito.verify(mockConnection, Mockito.times(1)).setCatalog("newSchema");
    }
    
    @Test
    public void testGetCurrentSchema() {
        String schema = "schema";
        Mockito.when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject("SELECT DATABASE()", String.class)).thenReturn(schema);
        String currentSchema = obMySQLSessionExtension.getCurrentSchema(mockConnection);
        assertEquals(schema, currentSchema);
    }
    
    @Test
    public void testGetConnectionId() {
        Mockito.when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject("select connection_id()", String.class)).thenReturn(null);
        Mockito.when(mockOceanBaseConnection.getServerThreadId()).thenReturn(1L);
        String connectionId = obMySQLSessionExtension.getConnectionId(mockConnection);
        assertEquals("1", connectionId);

        Mockito.when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject("select connection_id()", String.class)).thenReturn("1");
        connectionId = obMySQLSessionExtension.getConnectionId(mockConnection);
        assertEquals("1", connectionId);
    }
}