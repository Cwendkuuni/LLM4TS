
package com.oceanbase.odc.plugin.connect.obmysql;

import org.junit.Test;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;

import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.jdbc.OceanBaseConnection;

public class OBMySQLSessionExtensionTest {

    @Mock 
    private JdbcOperationsUtil jdbcOperationsUtil;

    @Mock
    private Connection connection;

    @Mock
    private OceanBaseConnection oceanBaseConnection;

    private OBMySQLSessionExtension obMySQLSessionExtension;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        obMySQLSessionExtension = new OBMySQLSessionExtension();
    }
    
    @Test
    public void testKillQuery() {
        String connectionId = "12345";
        obMySQLSessionExtension.killQuery(connection, connectionId);
        verify(jdbcOperationsUtil, times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String currentSchema = "dbName";
        when(obMySQLSessionExtension.getCurrentSchema(connection)).thenReturn(currentSchema);
        obMySQLSessionExtension.switchSchema(connection, "newDbName");
        verify(connection, times(1)).setCatalog(anyString());
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String querySql = "SELECT DATABASE()";
        obMySQLSessionExtension.getCurrentSchema(connection);
        verify(jdbcOperationsUtil, times(1)).queryForObject(querySql, String.class);
    }

    @Test
    public void testGetConnectionId() {
        String querySql = "select connection_id()";
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(12345L);
        obMySQLSessionExtension.getConnectionId(connection);
        verify(jdbcOperationsUtil, times(1)).queryForObject(querySql, String.class);
    }
}
