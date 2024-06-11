package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension obOracleSessionExtension;
    private Connection mockConnection;

    @Before
    public void setUp() {
        obOracleSessionExtension = new OBOracleSessionExtension();
        mockConnection = mock(Connection.class);
    }

    @Test
    public void testKillQuery() throws SQLException {
        obOracleSessionExtension.killQuery(mockConnection, "1234");
        verify(JdbcOperationsUtil.getJdbcOperations(mockConnection), times(1)).execute("KILL QUERY 1234");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        when(obOracleSessionExtension.getCurrentSchema(mockConnection)).thenReturn("currentSchema");
        obOracleSessionExtension.switchSchema(mockConnection, "newSchema");
        verify(JdbcOperationsUtil.getJdbcOperations(mockConnection), times(1)).update("ALTER SESSION SET CURRENT_SCHEMA='newSchema'");
    }

    @Test
    public void testSwitchSchemaSameSchema() throws SQLException {
        when(obOracleSessionExtension.getCurrentSchema(mockConnection)).thenReturn("sameSchema");
        obOracleSessionExtension.switchSchema(mockConnection, "sameSchema");
        verify(JdbcOperationsUtil.getJdbcOperations(mockConnection), never()).update(anyString());
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject(anyString(), eq(String.class))).thenReturn("currentSchema");
        assertEquals("currentSchema", obOracleSessionExtension.getCurrentSchema(mockConnection));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject("select userenv('sessionid') from dual", String.class)).thenReturn("1234");
        assertEquals("1234", obOracleSessionExtension.getConnectionId(mockConnection));
    }

    @Test
    public void testGetConnectionIdException() throws SQLException {
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(JdbcOperationsUtil.getJdbcOperations(oceanBaseConnection).queryForObject(anyString(), eq(String.class))).thenThrow(new SQLException());
        when(oceanBaseConnection.getServerThreadId()).thenReturn(5678L);
        assertEquals("5678", obOracleSessionExtension.getConnectionId(oceanBaseConnection));
    }
}