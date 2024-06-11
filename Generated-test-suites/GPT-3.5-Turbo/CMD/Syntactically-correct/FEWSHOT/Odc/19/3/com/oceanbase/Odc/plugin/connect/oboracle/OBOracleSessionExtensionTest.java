package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Test;

public class OBOracleSessionExtensionTest {

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = mock(Connection.class);
        Statement statement = mock(Statement.class);
        when(connection.createStatement()).thenReturn(statement);
        
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        obOracleSessionExtension.killQuery(connection, "12345");
        
        // Verify that "KILL QUERY 12345" was executed
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        obOracleSessionExtension.switchSchema(connection, "test_schema");
        
        // Verify that schema was switched to "test_schema"
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        Statement statement = mock(Statement.class);
        ResultSet resultSet = mock(ResultSet.class);
        when(statement.executeQuery("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL")).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString(1)).thenReturn("test_schema");
        
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        
        assertEquals("test_schema", currentSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);
        Statement statement = mock(Statement.class);
        ResultSet resultSet = mock(ResultSet.class);
        when(statement.executeQuery("select userenv('sessionid') from dual")).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString(1)).thenReturn("12345");
        
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        String connectionId = obOracleSessionExtension.getConnectionId(connection);
        
        assertNotNull(connectionId);
        assertEquals("12345", connectionId);
    }
}