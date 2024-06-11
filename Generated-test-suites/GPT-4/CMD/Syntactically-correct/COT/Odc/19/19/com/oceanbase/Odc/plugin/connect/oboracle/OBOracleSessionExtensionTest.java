package com.oceanbase.odc.plugin.connect.oboracle;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OBOracleSessionExtensionTest {

    OBOracleSessionExtension extension = new OBOracleSessionExtension();
    Connection mockConnection = mock(Connection.class);
    OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);

    @Test
    public void testKillQuery() {
        extension.killQuery(mockConnection, "testConnection");
    }

    @Test
    public void testSwitchSchema() {
        extension.switchSchema(mockConnection, "testSchema");
    }

    @Test
    public void testSwitchSchemaSame() {
        String currentSchema = extension.getCurrentSchema(mockConnection);
        extension.switchSchema(mockConnection, currentSchema);
    }

    @Test
    public void testGetCurrentSchema() {
        String schema = extension.getCurrentSchema(mockConnection);
        assertNotNull(schema);
    }

    @Test
    public void testGetConnectionId() {
        String connectionId = extension.getConnectionId(mockConnection);
        assertNotNull(connectionId);
    }

    @Test
    public void testGetConnectionIdWithException() {
        when(extension.getConnectionId(oceanBaseConnection)).thenThrow(new RuntimeException());
        String connectionId = extension.getConnectionId(oceanBaseConnection);
        assertNotNull(connectionId);
    }
}