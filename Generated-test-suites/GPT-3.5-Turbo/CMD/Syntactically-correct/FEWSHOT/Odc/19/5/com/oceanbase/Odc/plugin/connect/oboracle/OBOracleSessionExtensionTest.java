package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.mockito.Mockito;
import java.sql.Connection;
import java.sql.SQLException;

public class OBOracleSessionExtensionTest {

    @Test
    public void testKillQuery() {
        Connection connection = Mockito.mock(Connection.class);
        OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();
        
        String connectionId = "12345";
        sessionExtension.killQuery(connection, connectionId);
        
        // Verify that the execute method was called with the correct query
        Mockito.verify(JdbcOperationsUtil.getJdbcOperations(connection)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();
        
        String schemaName = "test_schema";
        sessionExtension.switchSchema(connection, schemaName);
        
        // Verify that the update method was called with the correct SQL statement
        Mockito.verify(JdbcOperationsUtil.getJdbcOperations(connection)).update("ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName));
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = Mockito.mock(Connection.class);
        OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();
        
        String expectedSchema = "test_schema";
        
        // Mock the queryForObject method to return the expected schema
        Mockito.when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(expectedSchema);
        
        assertEquals(expectedSchema, sessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = Mockito.mock(Connection.class);
        OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();
        
        String expectedConnectionId = "12345";
        
        // Mock the queryForObject method to return the expected connectionId
        Mockito.when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(Mockito.anyString(), Mockito.eq(String.class))).thenReturn(expectedConnectionId);
        
        assertEquals(expectedConnectionId, sessionExtension.getConnectionId(connection));
    }
}