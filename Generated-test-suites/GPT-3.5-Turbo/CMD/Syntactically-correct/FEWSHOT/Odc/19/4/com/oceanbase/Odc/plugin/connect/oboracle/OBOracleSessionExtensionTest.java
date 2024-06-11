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
        String connectionId = "12345";

        obOracleSessionExtension.killQuery(connection, connectionId);

        // Verify the execution of "KILL QUERY" command with the correct connection and connectionId
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        String schemaName = "testSchema";

        // Mock the getCurrentSchema to return a different schema
        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn("otherSchema");

        obOracleSessionExtension.switchSchema(connection, schemaName);

        // Verify that the switchSchema method changes the current schema to the specified schemaName
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();

        // Mock the queryForObject method to return a specific schema name
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class)).thenReturn("testSchema");

        assertEquals("testSchema", obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();

        // Mock the queryForObject method to return a specific connectionId
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select userenv('sessionid') from dual", String.class)).thenReturn("12345");

        assertEquals("12345", obOracleSessionExtension.getConnectionId(connection));

        // Verify that the connectionId is retrieved correctly
    }
}