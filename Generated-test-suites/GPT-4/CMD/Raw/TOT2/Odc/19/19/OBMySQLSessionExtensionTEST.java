
package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import java.sql.Connection;
import java.sql.SQLException;

@RunWith(MockitoJUnitRunner.class)
public class OBMySQLSessionExtensionTest {
  
    @Mock
    private Connection connection;
    
    @InjectMocks
    private OBMySQLSessionExtension oBMySQLSessionExtension;

    @Test
    public void testKillQuery() {
        String connectionId = "123";
        oBMySQLSessionExtension.killQuery(connection, connectionId);
        verify(connection, times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "new_schema";
        
        // Testing the scenario when schema is not already set.
        Mockito.when(connection.getCatalog()).thenReturn("old_schema");
        oBMySQLSessionExtension.switchSchema(connection, schemaName);
        verify(connection, times(1)).setCatalog(schemaName);
        
        // Testing the scenario when schema is already set.
        Mockito.when(connection.getCatalog()).thenReturn(schemaName);
        oBMySQLSessionExtension.switchSchema(connection, schemaName);
        verify(connection, times(0)).setCatalog(schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        String expectedSchema = "my_schema";
        Mockito.when(connection.queryForObject("SELECT DATABASE()", String.class)).thenReturn(expectedSchema);
        String actualSchema = oBMySQLSessionExtension.getCurrentSchema(connection);
        assertEquals(expectedSchema, actualSchema);
    }
    
    @Test
    public void testGetConnectionId() {
        String connectionId = "123";

        // Testing when no exception happens.
        Mockito.when(connection.queryForObject("select connection_id()", String.class)).thenReturn(connectionId);
        assertEquals(connectionId, oBMySQLSessionExtension.getConnectionId(connection));

        // Testing when exception happens and connection is of type OceanBaseConnection.
        Mockito.when(connection.queryForObject("select connection_id()", String.class)).thenThrow(new RuntimeException());
        Mockito.when(connection.getServerThreadId()).thenReturn(Long.valueOf(connectionId));
        assertEquals(connectionId, oBMySQLSessionExtension.getConnectionId(
            new OceanBaseConnectionImpl(MOCK_PARAMETERS)));
    }
}
