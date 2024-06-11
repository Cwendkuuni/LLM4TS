package com.oceanbase.odc.plugin.connect.obmysql;

import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OBMySQLSessionExtensionTest {

    OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();
    Connection mockConnection = mock(Connection.class);
    static final String CONNECTION_ID = "myConnectionId";
    static final String SCHEMA_NAME = "mySchema";

    @Test
    public void testKillQuery() {
        obMySQLSessionExtension.killQuery(mockConnection, CONNECTION_ID);
    }

    @Test
    public void testSwitchSchemaNoChange() throws SQLException {
        when(obMySQLSessionExtension.getCurrentSchema(mockConnection)).thenReturn(SCHEMA_NAME);
        obMySQLSessionExtension.switchSchema(mockConnection, SCHEMA_NAME);
        verify(mockConnection, times(0)).setCatalog(SCHEMA_NAME);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        when(obMySQLSessionExtension.getCurrentSchema(mockConnection)).thenReturn("otherSchema");
        obMySQLSessionExtension.switchSchema(mockConnection, SCHEMA_NAME);
        verify(mockConnection, times(1)).setCatalog(SCHEMA_NAME);
    }

    @Test
    public void testGetCurrentSchema() {
        when(obMySQLSessionExtension.getCurrentSchema(mockConnection)).thenReturn(SCHEMA_NAME);
        String currentSchema = obMySQLSessionExtension.getCurrentSchema(mockConnection);
        assertEquals(SCHEMA_NAME, currentSchema);
        verify(mockConnection, times(1)).toString();
    }

    @Test
    public void testGetConnectionId() {
        when(obMySQLSessionExtension.getConnectionId(mockConnection)).thenReturn(CONNECTION_ID);
        String connectionId = obMySQLSessionExtension.getConnectionId(mockConnection);
        assertEquals(CONNECTION_ID, connectionId);
    }
}
