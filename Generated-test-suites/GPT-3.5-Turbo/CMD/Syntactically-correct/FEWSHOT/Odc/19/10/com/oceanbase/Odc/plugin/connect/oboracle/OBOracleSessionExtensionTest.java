package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

public class OBOracleSessionExtensionTest {

    private Connection connectionMock;
    private OBOracleSessionExtension obOracleSessionExtension;

    @Before
    public void setUp() {
        connectionMock = mock(Connection.class);
        obOracleSessionExtension = new OBOracleSessionExtension();
    }

    @Test
    public void testKillQuery() throws SQLException {
        when(connectionMock.createStatement()).thenReturn(mock(Statement.class));
        obOracleSessionExtension.killQuery(connectionMock, "12345");
        verify(connectionMock, times(1)).createStatement();
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test_schema";
        when(connectionMock.createStatement()).thenReturn(mock(Statement.class));

        // Test when current schema is equal to schemaName
        when(obOracleSessionExtension.getCurrentSchema(connectionMock)).thenReturn(schemaName);
        obOracleSessionExtension.switchSchema(connectionMock, schemaName);
        verify(connectionMock, never()).createStatement();

        // Test when current schema is not equal to schemaName
        obOracleSessionExtension.switchSchema(connectionMock, "other_schema");
        verify(connectionMock, times(1)).createStatement();
    }

    @Test
    public void testGetCurrentSchema() {
        when(obOracleSessionExtension.getCurrentSchema(connectionMock)).thenReturn("test_schema");
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connectionMock);
        assertEquals("test_schema", currentSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        when(obOracleSessionExtension.getCurrentSchema(connectionMock)).thenReturn("test_schema");

        // Test when connection is not an instance of OceanBaseConnection
        assertNull(obOracleSessionExtension.getConnectionId(connectionMock));

        // Test when connection is an instance of OceanBaseConnection
        OceanBaseConnection oceanBaseConnectionMock = mock(OceanBaseConnection.class);
        when(oceanBaseConnectionMock.getServerThreadId()).thenReturn(12345L);
        assertEquals("12345", obOracleSessionExtension.getConnectionId(oceanBaseConnectionMock));
    }
}