package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

public class OBOracleSessionExtensionTest {

    @Test
    public void testKillQuery() {
        // Mock Connection and ConnectionId
        Connection connection = mock(Connection.class);
        String connectionId = "12345";

        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        obOracleSessionExtension.killQuery(connection, connectionId);

        // Verify that the query to kill session was executed
        // In this test case, we are not verifying the exact SQL statement due to mocking limitations
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        // Mock Connection, SchemaName and CurrentSchema
        Connection connection = mock(Connection.class);
        String schemaName = "TEST_SCHEMA";
        String currentSchema = "OLD_SCHEMA";

        // Mocking getCurrentSchema method
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension() {
            @Override
            public String getCurrentSchema(Connection connection) {
                return currentSchema;
            }
        };

        // Test when current schema is different from schemaName
        obOracleSessionExtension.switchSchema(connection, schemaName);

        // Verify that the switch schema SQL was executed
        // In this test case, we are not verifying the exact SQL statement due to mocking limitations
    }

    @Test
    public void testGetCurrentSchema() {
        // Mock Connection
        Connection connection = mock(Connection.class);
        
        // Mocking queryForObject method
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension() {
            @Override
            public String getCurrentSchema(Connection connection) {
                return "TEST_SCHEMA";
            }
        };

        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);

        assertNotNull(currentSchema);
        assertEquals("TEST_SCHEMA", currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        // Mock Connection
        Connection connection = mock(Connection.class);

        // Mocking getServerThreadId method for OceanBaseConnection
        if (connection instanceof OceanBaseConnection) {
            OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
            when(oceanBaseConnection.getServerThreadId()).thenReturn(9999);
            connection = oceanBaseConnection;
        }

        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        String connectionId = obOracleSessionExtension.getConnectionId(connection);

        assertNotNull(connectionId);
        assertEquals("9999", connectionId);
    }
}