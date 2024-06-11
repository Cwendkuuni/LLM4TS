package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Test;
import org.junit.Before;
import org.mockito.Mockito;

public class OBMySQLSessionExtensionTest {

    private OBMySQLSessionExtension obMySQLExtension;
    private Connection connection;

    @Before
    public void setUp() {
        obMySQLExtension = new OBMySQLSessionExtension();
        connection = Mockito.mock(Connection.class);
    }

    /* Test method for killQuery */
    @Test
    public void testKillQuery() {
        String connectionId = "SampleConnectionId";
        obMySQLExtension.killQuery(connection, connectionId);
    }
    
    /* Test method for switchSchema */
    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "SampleSchema";
        obMySQLExtension.switchSchema(connection, schemaName);
        verify(connection, times(1)).setCatalog(schemaName);
    }
    
    /* Test method for getCurrentSchema */
    @Test
    public void testGetCurrentSchema() {
        String returnedSchema = obMySQLExtension.getCurrentSchema(connection);
        assertNotNull(returnedSchema);
    }
    
    /* Test method for getConnectionId */
    @Test
    public void testGetConnectionId() {
        String connectionId = obMySQLExtension.getConnectionId(connection);
        assertNotNull(connectionId);
    }    
    
    /* Test method for switchSchema with the same schema name, no action required */
    @Test
    public void testSwitchSchemaSameSchema() throws SQLException {
        String schemaName = "SampleSchema";
        when(connection.getCatalog()).thenReturn(schemaName);
        obMySQLExtension.switchSchema(connection, schemaName);
        verify(connection, times(0)).setCatalog(anyString());
    }
    
    /* Test method for getConnectionId when exception occurs */
    @Test
    public void testGetConnectionIdException() throws SQLException {
        Connection connection2 = Mockito.mock(OceanBaseConnection.class);
        when(connection2.prepareCall(anyString())).thenThrow(SQLException.class);
        String connectionId = obMySQLExtension.getConnectionId(connection2);
        assertNotNull(connectionId);
    }      
}