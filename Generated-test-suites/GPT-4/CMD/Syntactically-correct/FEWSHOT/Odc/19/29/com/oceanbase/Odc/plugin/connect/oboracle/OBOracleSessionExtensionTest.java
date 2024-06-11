
package com.oceanbase.odc.plugin.connect.oboracle;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class OBOracleSessionExtensionTest {
    
    @Mock
    private Connection connection;
    
    @Mock
    private OceanBaseConnection oceanBaseConnection;

    private OBOracleSessionExtension obOracleSessionExtension;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        obOracleSessionExtension = new OBOracleSessionExtension();
    }
    
    @Test
    public void testKillQuery() {
        String connectionId = "testId";
        obOracleSessionExtension.killQuery(connection, connectionId);
        Mockito.verify(connection).createStatement().execute("KILL QUERY " + connectionId);
    }
    
    @Test
    public void testSwitchSchema() {        
        String schemaName = "test";
        obOracleSessionExtension.switchSchema(connection, schemaName);
        Mockito.verify(connection).createStatement().execute("ALTER SESSION SET CURRENT_SCHEMA=" + schemaName);
    }
    
    @Test
    public void testGetCurrentSchema() {        
        String currentSchema = "current";
        Mockito.when(connection.createStatement().executeQuery("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL").getResultSet().getString(1)).thenReturn(currentSchema);
        String actualSchema = obOracleSessionExtension.getCurrentSchema(connection);
        assertEquals(currentSchema, actualSchema);
    }

    @Test
    public void testGetConnectionId() {
        String expectedId = "123";
        Mockito.when(connection.createStatement().executeQuery("select userenv('sessionid') from dual").getResultSet().getString(1)).thenReturn(expectedId);
        String actualId = obOracleSessionExtension.getConnectionId(connection);
        assertEquals(expectedId, actualId);
        
        Mockito.doThrow(new SQLException()).when(connection.createStatement().executeQuery("select userenv('sessionid') from dual").getResultSet().getString(1)).thenReturn(expectedId);
        Mockito.when(oceanBaseConnection.getServerThreadId()).thenReturn(Long.valueOf(expectedId));
        
        actualId = obOracleSessionExtension.getConnectionId(oceanBaseConnection);
        assertEquals(expectedId, actualId);
    }
}
