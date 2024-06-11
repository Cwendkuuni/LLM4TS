package com.oceanbase.odc.plugin.connect.obmysql;

import java.sql.Connection;
import java.sql.SQLException;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;

public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() throws SQLException {
        Connection mockConnection = Mockito.mock(Connection.class);
        OBMySQLSessionExtension testObj = new OBMySQLSessionExtension();
        testObj.killQuery(mockConnection, "1001");
        Mockito.verify(mockConnection, Mockito.times(1)).createStatement();
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection mockConnection = Mockito.mock(Connection.class);
        OBMySQLSessionExtension testObj = new OBMySQLSessionExtension();
        testObj.switchSchema(mockConnection, "test");
        Mockito.verify(mockConnection, Mockito.times(1)).setCatalog("test");
    }
    
    @Test
    public void testGetCurrentSchema() {
        Connection mockConnection = Mockito.mock(Connection.class);
        OBMySQLSessionExtension testObj = new OBMySQLSessionExtension();
        assertEquals("test", testObj.getCurrentSchema(mockConnection));
    }

    @Test
    public void testGetConnectionId() {
        Connection mockConnection = Mockito.mock(Connection.class);
        OBMySQLSessionExtension testObj = new OBMySQLSessionExtension();
        assertEquals("1001", testObj.getConnectionId(mockConnection));
    }

    @Test
    public void testGetConnectionIdOceanBaseConnection() {
        OceanBaseConnection mockConnection = Mockito.mock(OceanBaseConnection.class);
        Mockito.when(mockConnection.getServerThreadId()).thenReturn(1001L);
        OBMySQLSessionExtension testObj = new OBMySQLSessionExtension();
        assertEquals("1001", testObj.getConnectionId(mockConnection));
    }

}