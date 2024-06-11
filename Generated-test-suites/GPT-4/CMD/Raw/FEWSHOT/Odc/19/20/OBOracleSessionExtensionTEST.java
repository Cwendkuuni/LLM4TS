package com.oceanbase.odc.plugin.connect.oboracle;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;

public class OBOracleSessionExtensionTest {

    @Test
    public void testKillQuery() {
        Connection mockConnection = mock(Connection.class);
        JdbcOperationsUtil operationsUtil = mock(JdbcOperationsUtil.class);
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection)).thenReturn(operationsUtil);

        String connectionId = "100";
        new OBOracleSessionExtension().killQuery(mockConnection, connectionId);

        verify(operationsUtil, times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        JdbcOperationsUtil operationsUtil = mock(JdbcOperationsUtil.class);
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection)).thenReturn(operationsUtil);

        String schemaName = "NewSchema";
        new OBOracleSessionExtension().switchSchema(mockConnection, schemaName);

        verify(operationsUtil, times(1)).update("ALTER SESSION SET CURRENT_SCHEMA=" + schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        Connection mockConnection = mock(Connection.class);
        JdbcOperationsUtil operationsUtil = mock(JdbcOperationsUtil.class);
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection)).thenReturn(operationsUtil);

        new OBOracleSessionExtension().getCurrentSchema(mockConnection);

        verify(operationsUtil, times(1)).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class);
    }

    @Test
    public void testGetConnectionId() {
        Connection mockConnection = mock(OceanBaseConnection.class);
        JdbcOperationsUtil operationsUtil = mock(JdbcOperationsUtil.class);
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection)).thenReturn(operationsUtil);

        new OBOracleSessionExtension().getConnectionId(mockConnection);

        verify(operationsUtil, times(1)).queryForObject("select userenv('sessionid') from dual", String.class);
    }
}