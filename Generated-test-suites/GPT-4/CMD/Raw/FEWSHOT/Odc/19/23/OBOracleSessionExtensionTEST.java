package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.anyString;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;

public class OBOracleSessionExtensionTest {

    @Test
    public void testKillQuery() {
        Connection mockConnection = mock(Connection.class);
        JdbcOperationsUtil mockJdbcOperationsUtil = mock(JdbcOperationsUtil.class);

        when(JdbcOperationsUtil.getJdbcOperations(mockConnection)).thenReturn(mockJdbcOperationsUtil);

        OBOracleSessionExtension instance = new OBOracleSessionExtension();
        instance.killQuery(mockConnection, "12345");

        verify(mockJdbcOperationsUtil).execute("KILL QUERY 12345");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        JdbcOperationsUtil mockJdbcOperationsUtil = mock(JdbcOperationsUtil.class);

        when(JdbcOperationsUtil.getJdbcOperations(mockConnection)).thenReturn(mockJdbcOperationsUtil);
        when(mockJdbcOperationsUtil.queryForObject(anyString(), eq(String.class))).thenReturn("Test_Schema");

        OBOracleSessionExtension instance = new OBOracleSessionExtension();
        instance.switchSchema(mockConnection, "Test_Schema");

        verify(mockJdbcOperationsUtil).update("ALTER SESSION SET CURRENT_SCHEMA=Test_Schema");
    }

    @Test
    public void testGetCurrentSchema() {
        Connection mockConnection = mock(Connection.class);
        JdbcOperationsUtil mockJdbcOperationsUtil = mock(JdbcOperationsUtil.class);

        when(JdbcOperationsUtil.getJdbcOperations(mockConnection)).thenReturn(mockJdbcOperationsUtil);
        when(mockJdbcOperationsUtil.queryForObject(anyString(), eq(String.class))).thenReturn("Test_Schema");

        OBOracleSessionExtension instance = new OBOracleSessionExtension();
        String result = instance.getCurrentSchema(mockConnection);

        assertEquals("Test_Schema", result);
    }

    @Test
    public void testGetConnectionId() {
        Connection mockConnection = mock(Connection.class);
        JdbcOperationsUtil mockJdbcOperationsUtil = mock(JdbcOperationsUtil.class);
        OceanBaseConnection mockOceanBaseConnection = mock(OceanBaseConnection.class);

        when(JdbcOperationsUtil.getJdbcOperations(mockConnection)).thenReturn(mockJdbcOperationsUtil);
        when(mockJdbcOperationsUtil.queryForObject(anyString(), eq(String.class))).thenReturn("12345");
        when(mockOceanBaseConnection.getServerThreadId()).thenReturn(67890L);

        OBOracleSessionExtension instance = new OBOracleSessionExtension();
        String result = instance.getConnectionId(mockConnection);

        assertEquals("12345", result);
        result = instance.getConnectionId(mockOceanBaseConnection);
        assertEquals("67890", result);
    }
}