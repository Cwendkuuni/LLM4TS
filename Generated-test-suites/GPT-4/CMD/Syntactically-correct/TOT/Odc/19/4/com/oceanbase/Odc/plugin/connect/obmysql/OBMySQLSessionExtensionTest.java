package com.oceanbase.odc.plugin.connect.obmysql;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.sql.Connection;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.oceanbase.jdbc.OceanBaseConnection;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OBMySQLSessionExtensionTest{

    @Mock
    private Connection connection;
    @Mock
    private OceanBaseConnection oceanBaseConnection;
    private OBMySQLSessionExtension oBMySQLSessionExtension;

    @Before
    public void setUp() throws Exception {
        oBMySQLSessionExtension = new OBMySQLSessionExtension();
    }

    @Test
    public void testKillQuery() {
        doNothing().when(connection).execute("KILL QUERY " + "1");
        oBMySQLSessionExtension.killQuery(connection, "1");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        when(connection.getCatalog()).thenReturn("schema1");
        oBMySQLSessionExtension.switchSchema(connection, "schema1");
        oBMySQLSessionExtension.switchSchema(connection, "schema2");
    }

    @Test
    public void testGetCurrentSchema() {
        when(connection.queryForObject("SELECT DATABASE()", String.class)).thenReturn("schema1");
        String schema = oBMySQLSessionExtension.getCurrentSchema(connection);
        assertEquals("schema1", schema);
    }

    @Test
    public void testGetConnectionIdForException() {
        when(connection.queryForObject("select connection_id()", String.class)).thenThrow(new RuntimeException());
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(3333L);
        String connectionId = oBMySQLSessionExtension.getConnectionId(connection);
        assertEquals("3333", connectionId);
    }

    @Test
    public void testGetConnectionId() {
        when(connection.queryForObject("select connection_id()", String.class)).thenReturn("1");
        String connectionId = oBMySQLSessionExtension.getConnectionId(connection);
        assertEquals("1", connectionId);
    }
}