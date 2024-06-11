package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;

import com.oceanbase.jdbc.OceanBaseConnection;

public class OBMySQLSessionExtensionTest {
    private OBMySQLSessionExtension obMySqlSessionExtension;
    private Connection mockConn;

    @Before
    public void setup() throws Exception {
        obMySqlSessionExtension = new OBMySQLSessionExtension();
        mockConn = mock(Connection.class);
    }

    @Test
    public void testKillQuery() {
        obMySqlSessionExtension.killQuery(mockConn, "1234");
        verify(mockConn, times(1)).execute("KILL QUERY " + "1234");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        when(obMySqlSessionExtension.getCurrentSchema(mockConn)).thenReturn("schemaA");
        obMySqlSessionExtension.switchSchema(mockConn, "schemaB");
        verify(mockConn, times(1)).setCatalog("schemaB");
    }

    @Test
    public void testGetCurrentSchema() {
        String expectedScheme = "SampleScheme";
        when(obMySqlSessionExtension.getCurrentSchema(mockConn)).thenReturn(expectedScheme);
        String schema = obMySqlSessionExtension.getCurrentSchema(mockConn);
        assertEquals(schema, expectedScheme);
    }

    @Test
    public void testGetConnectionIdWhenExceptionIsThrown() throws SQLException {
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        String expectedId = "9999";
        when(oceanBaseConnection.getServerThreadId()).thenReturn(Long.parseLong(expectedId));
        String connectionId = obMySqlSessionExtension.getConnectionId(oceanBaseConnection);
        assertEquals(connectionId, expectedId);
    }
}