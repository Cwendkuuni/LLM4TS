package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;

import org.junit.Before;
import org.junit.Test;

public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension extension;
    private Connection mockConnection;
    
    @Before
    public void setUp() {
        extension = new OBOracleSessionExtension();
        mockConnection = mock(Connection.class);
    }

    @Test
    public void testKillQuery() throws Exception {
        String connectionId = "1234567890";
        extension.killQuery(mockConnection, connectionId);
        // Verify more logic if necessary
    }

    @Test
    public void testSwitchSchema() throws Exception {
        String schemaName = "TEST_SCHEMA";
        extension.switchSchema(mockConnection, schemaName);
        // Add assertions and verifications depending on expected system behavior
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwitchSchema_NullSchema() throws Exception {
        extension.switchSchema(mockConnection, null);
    }

    @Test
    public void testGetCurrentSchema() throws Exception {
        String schemaName = extension.getCurrentSchema(mockConnection);
        //Again, add assertion for schema name depending on expected schema
    }

    @Test
    public void testGetConnectionId() throws Exception {
        String connectionId = extension.getConnectionId(mockConnection);
        //An assertion for connectionId based on expected connectionId 
    }

    @Test(expected=IllegalArgumentException.class)
    public void testGetConnectionIdNullConn() throws Exception {
         extension.getConnectionId(null);
    }
}