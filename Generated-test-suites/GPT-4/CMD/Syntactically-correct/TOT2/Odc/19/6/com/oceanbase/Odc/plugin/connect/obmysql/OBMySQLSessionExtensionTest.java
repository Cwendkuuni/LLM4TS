
package com.oceanbase.odc.plugin.connect.obmysql;

import java.sql.Connection;
import java.sql.SQLException;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class OBMySQLSessionExtensionTest {
    private OBMySQLSessionExtension sesExt;
    private String connectionId;
    private String schemaName;
    
    @Mock
    private Connection conn;
    @Mock
    private JdbcOperationsUtil jdbcOps;
    @Mock
    private OceanBaseConnection oceanConn;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.sesExt = new OBMySQLSessionExtension();
        this.connectionId = "1";
        this.schemaName = "testSchema";
    }

    @Test
    public void testKillQuery() {
        sesExt.killQuery(conn, connectionId);
        verify(jdbcOps).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema_sameSchema() throws SQLException {
        when(sesExt.getCurrentSchema(conn)).thenReturn(schemaName);
        sesExt.switchSchema(conn, schemaName);
        verifyNoInteractions(conn);
    }

    @Test
    public void testSwitchSchema_differentSchema() throws SQLException {
        when(sesExt.getCurrentSchema(conn)).thenReturn("different" + schemaName);
        sesExt.switchSchema(conn, schemaName);
        verify(conn, times(1)).setCatalog(schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        String querySql = "SELECT DATABASE()";
        sesExt.getCurrentSchema(conn);
        verify(jdbcOps).queryForObject(querySql, String.class);
    }

    @Test
    public void testGetConnectionId_noException() {
        String querySql = "select connection_id()";
        sesExt.getConnectionId(conn);
        verify(jdbcOps).queryForObject(querySql, String.class);
    }

    @Test
    public void testGetConnectionId_withException() {
        String querySql = "select connection_id()";
        when(jdbcOps.queryForObject(querySql, String.class)).thenThrow(new RuntimeException());
        sesExt.getConnectionId(oceanConn);
        verify(oceanConn).getServerThreadId();
    }
}
