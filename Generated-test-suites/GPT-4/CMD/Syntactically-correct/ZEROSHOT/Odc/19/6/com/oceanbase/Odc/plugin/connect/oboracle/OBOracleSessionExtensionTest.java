package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.oceanbase.jdbc.OceanBaseConnection;

public class OBOracleSessionExtensionTest {

    private static OBOracleSessionExtension extension;
    private static Connection mockConnection;
    
    @BeforeClass
    public static void setup() {
        extension = new OBOracleSessionExtension();
        mockConnection = mock(OceanBaseConnection.class);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "new_schema";
        extension.switchSchema(mockConnection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        String expectedSchema = "current_schema";
        when(mockConnection.getSchema()).thenReturn(expectedSchema);
        String actualSchema = extension.getCurrentSchema(mockConnection);
        assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testGetConnectionId() {
        String expectedId = "1234";
        when(((OceanBaseConnection) mockConnection).getServerThreadId()).thenReturn(Long.parseLong(expectedId));
        String actualId = extension.getConnectionId(mockConnection);
        assertEquals(expectedId, actualId);
    }

    @Test
    public void testKillQuery() {
        String connectionId = "5678";
        extension.killQuery(mockConnection, connectionId);
    }
}