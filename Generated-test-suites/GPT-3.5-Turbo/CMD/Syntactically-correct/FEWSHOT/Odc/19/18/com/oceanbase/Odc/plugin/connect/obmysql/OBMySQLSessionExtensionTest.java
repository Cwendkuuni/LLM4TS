package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;
import org.slf4j.Logger;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;

@Extension
@Slf4j
public class OBMySQLSessionExtensionTest {
    
    private OBMySQLSessionExtension obMySQLSessionExtension;
    private Connection mockConnection;

    @Before
    public void setup() {
        obMySQLSessionExtension = new OBMySQLSessionExtension();
        mockConnection = mock(Connection.class);
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "123456";
        obMySQLSessionExtension.killQuery(mockConnection, connectionId);
        verify(JdbcOperationsUtil.getJdbcOperations(mockConnection), times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        when(obMySQLSessionExtension.getCurrentSchema(mockConnection)).thenReturn("schema1");
        
        obMySQLSessionExtension.switchSchema(mockConnection, "schema2");
        verify(mockConnection, times(1)).setCatalog("schema2");
        
        obMySQLSessionExtension.switchSchema(mockConnection, "schema1");
        verify(mockConnection, never()).setCatalog(anyString());
    }

    @Test
    public void testGetCurrentSchema() {
        String expectedSchema = "testSchema";
        
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject("SELECT DATABASE()", String.class)).thenReturn(expectedSchema);
        
        String currentSchema = obMySQLSessionExtension.getCurrentSchema(mockConnection);
        assertEquals(expectedSchema, currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        String expectedConnectionId = "56789";

        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject("select connection_id()", String.class)).thenReturn(expectedConnectionId);
        
        String connectionId = obMySQLSessionExtension.getConnectionId(mockConnection);
        assertEquals(expectedConnectionId, connectionId);
        
        OceanBaseConnection mockOceanBaseConnection = mock(OceanBaseConnection.class);
        when(mockConnection instanceof OceanBaseConnection).thenReturn(true);
        when(mockOceanBaseConnection.getServerThreadId()).thenReturn(99999L);
        
        connectionId = obMySQLSessionExtension.getConnectionId(mockConnection);
        assertEquals("99999", connectionId);
    }
}