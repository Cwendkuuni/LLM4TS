
package com.oceanbase.odc.plugin.connect.oboracle;

import org.junit.Test;
import org.mockito.Mockito;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OBOracleSessionExtensionTest {
    private final OBOracleSessionExtension oracleSessionExtension = new OBOracleSessionExtension();
    private final Connection connection = Mockito.mock(Connection.class);
    
    @Test
    public void testKillQuery() {
        String connectionId = "connectionId";
        oracleSessionExtension.killQuery(connection, connectionId);
        verify(connection, Mockito.times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String currentSchemaName = "currentSchemaName";
        String newSchemaName = "newSchemaName";

        when(oracleSessionExtension.getCurrentSchema(connection)).thenReturn(currentSchema, newSchemaName);
        oracleSessionExtension.switchSchema(connection, newSchemaName);

        verify(connection, Mockito.times(1)).update("ALTER SESSION SET CURRENT_SCHEMA=" + currentSchemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        String expectedSchema = "expectedSchema";
        when(oracleSessionExtension.getCurrentSchema(connection)).thenReturn(expectedSchema);

        String actualSchema = oracleSessionExtension.getCurrentSchema(connection);

        assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testGetConnectionId() {
        String expectedConnectionId = "expectedConnectionId";
        when(oracleSessionExtension.getConnectionId(connection)).thenReturn(expectedConnectionId);

        String actualConnectionId = oracleSessionExtension.getConnectionId(connection);

        assertEquals(expectedConnectionId, actualConnectionId);
    }
}

