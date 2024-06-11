package com.oceanbase.odc.plugin.connect.obmysql;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() {
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        Connection mockConnection = mock(Connection.class);
        sessionExtension.killQuery(mockConnection, "someId");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        sessionExtension.switchSchema(mockConnection, "someSchema");
    }

    @Test
    public void testGetCurrentSchema() {
        Connection mockConnection = mock(Connection.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        String schema = sessionExtension.getCurrentSchema(mockConnection);
        assertNotNull(schema);
    }

    @Test
    public void testGetConnectionId() {
        Connection mockConnection = mock(Connection.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        String connectionId = sessionExtension.getConnectionId(mockConnection);
        assertNotNull(connectionId);
    }

    @Test
    public void testGetConnectionId_OceanBaseConnection() {
        OceanBaseConnection mockConnection = mock(OceanBaseConnection.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        String connectionId = sessionExtension.getConnectionId(mockConnection);
        assertNotNull(connectionId);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchema_SQLException() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        OBMySQLSessionExtension sessionExtension = new OBMySQLSessionExtension();
        doThrow(SQLException.class).when(mockConnection).setCatalog("someSchema");
        sessionExtension.switchSchema(mockConnection, "someSchema");
    }

}