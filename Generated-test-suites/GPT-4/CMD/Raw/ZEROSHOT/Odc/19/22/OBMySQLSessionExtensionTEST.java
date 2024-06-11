
package com.oceanbase.odc.plugin.connect.obmysql;

import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.pf4j.Extension;


@RunWith(MockitoJUnitRunner.class)
public class OBMySQLSessionExtensionTest {

    @Mock
    private Connection connection;
    
    private OBMySQLSessionExtension obMySQLSessionExtension;
    
    @Before
    public void setUp() {
        obMySQLSessionExtension = new OBMySQLSessionExtension();
    }
    
    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "1";
        
        obMySQLSessionExtension.killQuery(connection, connectionId);
        
        // There is no return so we verify if the method was called
        verify(connection).prepareStatement("KILL QUERY " + connectionId);
    }
    
    @Test
    public void testSwitchSchemaWhenSchemasAreEqual() throws SQLException {
        String schemaName = "mySchema";
        
        when(connection.getCatalog()).thenReturn(schemaName);
        
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        
        // There is no return so we verify if the method was not called
        verify(connection, times(0)).setCatalog(schemaName);
    }
    
    @Test
    public void testSwitchSchemaWhenSchemasAreDifferent() throws SQLException {
        String currentSchema = "currentSchema";
        String newSchema = "newSchema";
        
        when(connection.getCatalog()).thenReturn(currentSchema);
        
        obMySQLSessionExtension.switchSchema(connection, newSchema);
        
        verify(connection).setCatalog(newSchema);
    }
    
    @Test
    public void testGetCurrentSchema() throws SQLException {
        String schemaName = "mySchema";
        
        when(connection.getCatalog()).thenReturn(schemaName);
        
        String returnedSchema = obMySQLSessionExtension.getCurrentSchema(connection);
        
        assertEquals(schemaName, returnedSchema);
    }
    
    @Test
    public void testGetConnectionId() throws SQLException {
        String connectionId = "1";
        when(connection.toString()).thenReturn(connectionId);
        
        String returnedConnectionId = obMySQLSessionExtension.getConnectionId(connection);
        
        assertEquals(connectionId, returnedConnectionId);
    }
    
    @Test
    public void testGetConnectionIdWhenThereIsException() throws SQLException {
        String connectionId = "1";
        when(connection.toString()).thenReturn(connectionId);
        when(connection.getServerThreadId()).thenThrow(new SQLException());
        
        String returnedConnectionId = obMySQLSessionExtension.getConnectionId(connection);
        
        assertEquals(null, returnedConnectionId);
    }
}
