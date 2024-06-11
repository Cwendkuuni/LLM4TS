package com.oceanbase.odc.plugin.connect.oboracle;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.pf4j.Extension;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperations;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;

public class OBOracleSessionExtensionTest {

    @Mock
    private Connection connection;

    @Mock
    private OceanBaseConnection oceanBaseConnection;

    @Mock
    private JdbcOperations jdbcOperations;

    private OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();

    @Test
    public void testKillQuery() {
        String connectionId = "1";
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperations);
        obOracleSessionExtension.killQuery(connection, connectionId);
        verify(jdbcOperations, times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test";
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperations);
        obOracleSessionExtension.switchSchema(connection, schemaName);      
        verify(jdbcOperations, times(1)).update("ALTER SESSION SET CURRENT_SCHEMA=" + schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperations);
        when(jdbcOperations.queryForObject(Mockito.anyString(), Mockito.<Class<String>>any())).thenReturn("test");
        obOracleSessionExtension.getCurrentSchema(connection);
        verify(jdbcOperations, times(1)).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class);
    }

    @Test
    public void testGetConnectionId() {
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperations);
        when(jdbcOperations.queryForObject(Mockito.anyString(), Mockito.<Class<String>>any())).thenReturn("test");
        obOracleSessionExtension.getConnectionId(connection);
        verify(jdbcOperations, times(1)).queryForObject("select userenv('sessionid') from dual", String.class);
    }
}