
package com.oceanbase.odc.plugin.connect.obmysql;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;


public class OBMySQLSessionExtensionTest {

    @Mock
    private Connection connection;
    @Mock
    private OceanBaseConnection oceanBaseConnection;
    
    private OBMySQLSessionExtension obMySQLSessionExtension;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        obMySQLSessionExtension = new OBMySQLSessionExtension();
    }

    @Test
    public void testKillQuery() {
        String connectionId = "123";
        obMySQLSessionExtension.killQuery(connection, connectionId);
        verify(connectionId, times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test";
        when(obMySQLSessionExtension.getCurrentSchema(connection)).thenReturn(schemaName);
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        verify(connection, never()).setCatalog(schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        String connectionId = "123";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT DATABASE()", String.class))
                .thenReturn(connectionId);
        String result = obMySQLSessionExtension.getCurrentSchema(connection);
        assertEquals(connectionId, result);
    }

    @Test
    public void testGetConnectionId() {
        String connectionId = "123";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class))
                .thenReturn(connectionId);
        String result = obMySQLSessionExtension.getConnectionId(connection);
        assertEquals(connectionId, result);

        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class))
                .thenThrow(new RuntimeException());
        when(oceanBaseConnection.getServerThreadId()).thenReturn(Long.parseLong(connectionId));
        result = obMySQLSessionExtension.getConnectionId(connection);
        assertEquals(connectionId, result);
    }
}
