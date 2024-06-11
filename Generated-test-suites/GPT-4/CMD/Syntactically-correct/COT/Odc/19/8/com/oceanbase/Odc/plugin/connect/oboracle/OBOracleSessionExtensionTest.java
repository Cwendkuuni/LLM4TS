package com.oceanbase.odc.plugin.connect.oboracle;
import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import java.sql.Connection;
import static org.mockito.ArgumentMatchers.anyString;

public class OBOracleSessionExtensionTest {

    @Test(expected = NullPointerException.class)
    public void testKillQuery_onNullConnection() {
        new OBOracleSessionExtension().killQuery(null, "connectionId");
    }
    
    @Test(expected = NullPointerException.class)
    public void testKillQuery_onNullConnectionId() {
        Connection connection = Mockito.mock(Connection.class);
        new OBOracleSessionExtension().killQuery(connection, null);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchema_onNullConnection() throws SQLException {
        new OBOracleSessionExtension().switchSchema(null, "schemaName");
    }
    
    @Test(expected = SQLException.class)
    public void testSwitchSchema_onNullSchemaName() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        new OBOracleSessionExtension().switchSchema(connection, null);
    }
    
    @Test(expected = NullPointerException.class)
    public void testGetCurrentSchema_onNullConnection() {
        new OBOracleSessionExtension().getCurrentSchema(null);
    }
    
    @Test(expected = NullPointerException.class)
    public void testGetConnectionId_onNullConnection() {
        new OBOracleSessionExtension().getConnectionId(null);
    }

    @Test
    public void testGetConnectionId_onException() {
        Connection connection = Mockito.mock(OceanBaseConnection.class);
        Mockito.when(connection.queryForObject(anyString(), anyString())).thenThrow(new RuntimeException());
        String connectionId = new OBOracleSessionExtension().getConnectionId(connection);
        assertNotNull(connectionId);
    }
}