
package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;

@RunWith(MockitoJUnitRunner.class)
public class OBOracleSessionExtensionTest {
    @Mock
    Connection connection;

    @Mock
    OceanBaseConnection oceanBaseConnection;

    @Test
    public void testKillQuery() {
        JdbcOperationsUtil jdbcOperationsUtilMock = mock(JdbcOperationsUtil.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtilMock);

        new OBOracleSessionExtension().killQuery(connection, "1");
        verify(jdbcOperationsUtilMock).execute("KILL QUERY 1");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        JdbcOperationsUtil jdbcOperationsUtilMock = mock(JdbcOperationsUtil.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtilMock);
        when(jdbcOperationsUtilMock.queryForObject(anyString(), any())).thenReturn("oldSchema");

        new OBOracleSessionExtension().switchSchema(connection, "newSchema");
        verify(jdbcOperationsUtilMock).update("ALTER SESSION SET CURRENT_SCHEMA=\"newSchema\"");
    }

    @Test
    public void testGetCurrentSchema() {
        JdbcOperationsUtil jdbcOperationsUtilMock = mock(JdbcOperationsUtil.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtilMock);
        when(jdbcOperationsUtilMock.queryForObject(anyString(), any())).thenReturn("currentSchema");

        assertEquals("currentSchema", new OBOracleSessionExtension().getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() {
        JdbcOperationsUtil jdbcOperationsUtilMock = mock(JdbcOperationsUtil.class);  
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtilMock);
        when(jdbcOperationsUtilMock.queryForObject(anyString(), eq(String.class))).thenReturn("12345");
        
        assertEquals("12345", new OBOracleSessionExtension().getConnectionId(connection));

        when(jdbcOperationsUtilMock.queryForObject(anyString(), eq(String.class))).thenThrow(new RuntimeException());
        when(oceanBaseConnection.getServerThreadId()).thenReturn(12345L);
       
        assertEquals("12345", new OBOracleSessionExtension().getConnectionId(oceanBaseConnection));
    }
}
