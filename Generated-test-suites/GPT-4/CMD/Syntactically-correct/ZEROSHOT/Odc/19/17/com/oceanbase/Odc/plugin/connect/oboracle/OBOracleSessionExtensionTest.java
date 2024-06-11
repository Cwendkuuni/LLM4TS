package com.oceanbase.odc.plugin.connect.oboracle;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension obOracleSessionExtension;
    private Connection mockConnection;

    @Before
    public void setUp() {
        obOracleSessionExtension = new OBOracleSessionExtension();
        mockConnection = mock(Connection.class);
    }

    @Test
    public void testKillQuery() {
        String connectionId = "testId";
        doNothing().when(JdbcOperationsUtil.getJdbcOperations(mockConnection)).execute("KILL QUERY " + connectionId);
        obOracleSessionExtension.killQuery(mockConnection, connectionId);
        verify(JdbcOperationsUtil.getJdbcOperations(mockConnection), times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testSchema";
        doNothing().when(JdbcOperationsUtil.getJdbcOperations(mockConnection)).update("ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName));
        when(obOracleSessionExtension.getCurrentSchema(mockConnection)).thenReturn(schemaName);
        obOracleSessionExtension.switchSchema(mockConnection, schemaName);
        verify(JdbcOperationsUtil.getJdbcOperations(mockConnection), times(0)).update(anyString());
    }

    @Test
    public void testGetCurrentSchema() {
        String expectedSchema = "testSchema";
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class)).thenReturn(expectedSchema);
        String actualSchema = obOracleSessionExtension.getCurrentSchema(mockConnection);
        assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String expectedConnectionId = "testId";
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject("select userenv('sessionid') from dual", String.class)).thenReturn(expectedConnectionId);
        String actualConnectionId = obOracleSessionExtension.getConnectionId(mockConnection);
        assertEquals(expectedConnectionId, actualConnectionId);
    }

    @After
    public void tearDown() {
        obOracleSessionExtension = null;
        mockConnection = null;
    }
}
