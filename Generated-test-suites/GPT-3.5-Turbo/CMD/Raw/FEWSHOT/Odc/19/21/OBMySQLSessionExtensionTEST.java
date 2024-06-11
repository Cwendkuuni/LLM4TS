package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

public class OBMySQLSessionExtensionTest {

    private OBMySQLSessionExtension obMySQLSessionExtension;

    private Connection connectionMock;

    @Before
    public void setup() {
        obMySQLSessionExtension = new OBMySQLSessionExtension();
        connectionMock = mock(Connection.class);
    }

    @Test
    public void testKillQuery() {
        String connectionId = "1";
        when(JdbcOperationsUtil.getJdbcOperations(connectionMock).execute("KILL QUERY " + connectionId)).thenReturn(true);
        obMySQLSessionExtension.killQuery(connectionMock, connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test";
        when(obMySQLSessionExtension.getCurrentSchema(connectionMock)).thenReturn("oldSchema");
        obMySQLSessionExtension.switchSchema(connectionMock, schemaName);
        assertEquals(schemaName, connectionMock.getCatalog());
    }

    @Test
    public void testGetCurrentSchema() {
        when(JdbcOperationsUtil.getJdbcOperations(connectionMock).queryForObject("SELECT DATABASE()", String.class)).thenReturn("test");
        assertEquals("test", obMySQLSessionExtension.getCurrentSchema(connectionMock));
    }

    @Test
    public void testGetConnectionId() {
        String connectionId = "1";
        when(JdbcOperationsUtil.getJdbcOperations(connectionMock).queryForObject("select connection_id()", String.class)).thenReturn(connectionId);
        assertEquals(connectionId, obMySQLSessionExtension.getConnectionId(connectionMock));
    }

    @Test
    public void testGetConnectionIdAlternativePath() {
        OceanBaseConnection oceanBaseConnectionMock = mock(OceanBaseConnection.class);
        when(oceanBaseConnectionMock.getServerThreadId()).thenReturn(2L);
        when(JdbcOperationsUtil.getJdbcOperations(connectionMock).queryForObject("select connection_id()", String.class)).thenThrow(new RuntimeException());
        assertEquals("2", obMySQLSessionExtension.getConnectionId(oceanBaseConnectionMock));
    }
}