package com.oceanbase.odc.plugin.connect.obmysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pf4j.ExtensionPoint;

public class OBMySQLSessionExtensionTest {

    @Mock
    Connection mockConnection;

    OBMySQLSessionExtension obMySQLSessionExtension;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        obMySQLSessionExtension = new OBMySQLSessionExtension();
    }

    @Test
    public void testKillQuery() throws SQLException {
        obMySQLSessionExtension.killQuery(mockConnection, "12345");
        // verify that execute method is called with the correct query
        verify(JdbcOperationsUtil.getJdbcOperations(mockConnection)).execute("KILL QUERY 12345");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        obMySQLSessionExtension.switchSchema(mockConnection, "test_schema");
        // verify that setCatalog method is called with the correct schemaName
        verify(mockConnection).setCatalog("test_schema");
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        // mock the queryForObject method to return a test schema name
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject("SELECT DATABASE()", String.class)).thenReturn("test_schema");
        String currentSchema = obMySQLSessionExtension.getCurrentSchema(mockConnection);
        assertEquals("test_schema", currentSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        // mock the queryForObject method to return a test connection id
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject("select connection_id()", String.class)).thenReturn("54321");
        String connectionId = obMySQLSessionExtension.getConnectionId(mockConnection);
        assertEquals("54321", connectionId);
    }

    @Test
    public void testGetConnectionIdWithOceanBaseConnection() throws SQLException {
        // mock the queryForObject method to throw an exception
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject("select connection_id()", String.class)).thenThrow(new SQLException());
        // create a mock OceanBaseConnection
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(9999L);
        // pass in the mock OceanBaseConnection
        String connectionId = obMySQLSessionExtension.getConnectionId(oceanBaseConnection);
        assertEquals("9999", connectionId);
    }
}