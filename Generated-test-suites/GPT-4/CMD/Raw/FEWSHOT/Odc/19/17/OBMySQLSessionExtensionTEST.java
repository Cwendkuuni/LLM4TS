package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.sql.Connection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;

@RunWith(MockitoJUnitRunner.class)
public class OBMySQLSessionExtensionTest {
    
    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        sessionExtension.killQuery(connection, "100");
        verify(connection).execute("KILL QUERY 100");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        when(connection.getCatalog()).thenReturn("old_schema");
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        sessionExtension.switchSchema(connection, "new_schema");
        verify(connection).setCatalog("new_schema");
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        assertEquals("SELECT DATABASE()", sessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void getConnectionId_WithException_catchesAndProceeds() {
        Connection connection = mock(OceanBaseConnection.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select connection_id()", String.class)).thenThrow(new RuntimeException());
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(1L);
        assertEquals("1", sessionExtension.getConnectionId(connection));
    }

}