package com.oceanbase.odc.plugin.connect.obmysql;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Connection;
import java.sql.SQLFeatureNotSupportedException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OBMySQLSessionExtensionTest {

    @Mock
    private Connection mockConnection;

    private OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();

    @Test
    public void testKillQuery() {
        String connectionId = "1";
        obMySQLSessionExtension.killQuery(mockConnection, connectionId);
        verify(mockConnection).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testSchema";
        String currentSchema = "currentSchema";
        when(obMySQLSessionExtension.getCurrentSchema(mockConnection)).thenReturn(currentSchema);
        obMySQLSessionExtension.switchSchema(mockConnection, schemaName);
        verify(mockConnection).setCatalog(schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        String expectedSchema = "testSchema";
        when(obMySQLSessionExtension.getCurrentSchema(mockConnection)).thenReturn(expectedSchema);
        String actualSchema = obMySQLSessionExtension.getCurrentSchema(mockConnection);
        assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testGetConnectionId() {
        String expectedConnectionId = "1";
        when(obMySQLSessionExtension.getConnectionId(mockConnection)).thenReturn(expectedConnectionId);
        String actualConnectionId = obMySQLSessionExtension.getConnectionId(mockConnection);
        assertEquals(expectedConnectionId, actualConnectionId);
    }

    @Test
    public void testGetConnectionIdWithOceanBaseConnection() {
        String expectedConnectionId = "2";
        when(((OceanBaseConnection)mockConnection).getServerThreadId()).thenReturn(expectedConnectionId);
        String actualConnectionId = obMySQLSessionExtension.getConnectionId(mockConnection);
        assertEquals(expectedConnectionId, actualConnectionId);
    }
}