package com.oceanbase.odc.plugin.connect.oboracle;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;

@RunWith(MockitoJUnitRunner.class)
public class OBOracleSessionExtensionTest {

    @Mock
    private Connection connection;

    @Mock
    private OceanBaseConnection oceanBaseConnection;

    private OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();

    @Test
    public void testKillQuery() {
        doNothing().when(JdbcOperationsUtil.getJdbcOperations(connection)).execute(anyString());

        sessionExtension.killQuery(connection, "123");

        verify(JdbcOperationsUtil.getJdbcOperations(connection), times(1)).execute("KILL QUERY 123");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        when(sessionExtension.getCurrentSchema(connection)).thenReturn("oldSchema");

        doNothing().when(JdbcOperationsUtil.getJdbcOperations(connection)).update(anyString());

        sessionExtension.switchSchema(connection, "newSchema");

        verify(JdbcOperationsUtil.getJdbcOperations(connection), times(1))
            .update("ALTER SESSION SET CURRENT_SCHEMA=\"newSchema\"");
    }

    @Test
    public void testGetConnectionId() {
        when(JdbcOperationsUtil.getJdbcOperations(connection))
            .queryForObject(anyString(), eq(String.class))
            .thenReturn("123");

        assertEquals(sessionExtension.getConnectionId(connection), "123");
    }

    @Test
    public void testGetConnectionId_OceanBaseConnection() {
        when(JdbcOperationsUtil.getJdbcOperations(oceanBaseConnection))
            .queryForObject(anyString(), eq(String.class))
            .thenThrow(new RuntimeException());

        when(oceanBaseConnection.getServerThreadId()).thenReturn(Long.valueOf(123));

        assertEquals(sessionExtension.getConnectionId(oceanBaseConnection), "123");
    }
}