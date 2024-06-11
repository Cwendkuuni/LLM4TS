package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

public class OBOracleSessionExtensionTest {

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        String connectionId = "123";
        obOracleSessionExtension.killQuery(connection, connectionId);
        // Add verification/assertion here
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        String schemaName = "test_schema";
        // Implement mocking for getCurrentSchema method
        // Mockito.when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn("current_schema");
        
        obOracleSessionExtension.switchSchema(connection, schemaName);
        // Add verification/assertion here
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        // Implement mocking for JdbcOperationsUtil.getJdbcOperations(connection).queryForObject method
        // Mockito.when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class)).thenReturn("test_schema");
        
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        assertEquals("test_schema", currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        // Implement mocking for JdbcOperationsUtil.getJdbcOperations(connection).queryForObject method
        // Mockito.when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select userenv('sessionid') from dual", String.class)).thenReturn("123");
        
        String connectionId = obOracleSessionExtension.getConnectionId(connection);
        assertNotNull(connectionId);
        assertEquals("123", connectionId);
    }
}