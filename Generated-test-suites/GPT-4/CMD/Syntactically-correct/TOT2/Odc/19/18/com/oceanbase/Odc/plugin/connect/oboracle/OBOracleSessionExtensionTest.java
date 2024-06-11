
package com.oceanbase.odc.plugin.connect.oboracle;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OBOracleSessionExtensionTest {
    @Mock
    private Connection connection;

    private OBOracleSessionExtension obOracleSessionExtension;

    @Before
    public void setup() {
        obOracleSessionExtension = new OBOracleSessionExtension();
    }
 
    @Test
    public void testKillQuery() {
        String connectionId = "123";
        obOracleSessionExtension.killQuery(connection, connectionId);
        verify(connection).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test_schema";
        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn(schemaName);
        obOracleSessionExtension.switchSchema(connection, schemaName);
        verify(connection, times(0)).update("ALTER SESSION SET CURRENT_SCHEMA=" + schemaName);

        String newSchemaName="new_schema";
        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn(schemaName);
        obOracleSessionExtension.switchSchema(connection, newSchemaName);
        verify(connection).update("ALTER SESSION SET CURRENT_SCHEMA=" + newSchemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        String schemaName = "test_schema";
        when(connection.queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class)).thenReturn(schemaName);
        String returnedSchema = obOracleSessionExtension.getCurrentSchema(connection);
        assertEquals(schemaName, returnedSchema);
    }

    @Test
    public void testGetConnectionId() {
        String connectionId = "123";
        when(connection.queryForObject("select userenv('sessionid') from dual", String.class)).thenReturn(connectionId);
        String returnedConnectionId = obOracleSessionExtension.getConnectionId(connection);
        assertEquals(connectionId, returnedConnectionId);
    }

    @Test
    public void testGetConnectionIdWithException() {
        when(connection.queryForObject("select userenv('sessionid') from dual", String.class)).thenThrow(new RuntimeException());
        String returnedConnectionId = obOracleSessionExtension.getConnectionId(connection);
        assertNull(returnedConnectionId);
    }
}
