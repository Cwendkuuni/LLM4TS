package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.pf4j.Extension;
import org.springframework.jdbc.core.JdbcOperations;

import com.oceanbase.jdbc.OceanBaseConnection;

public class OBOracleSessionExtensionTest {

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = mock(Connection.class);
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        OBOracleSessionExtension sessionExt = new OBOracleSessionExtension();

        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperations);
        
        sessionExt.killQuery(connection, "1");
        
        verify(jdbcOperations, times(1)).execute("KILL QUERY 1");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        OBOracleSessionExtension sessionExt = new OBOracleSessionExtension();

        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperations);
        
        sessionExt.switchSchema(connection, "newSchema");
        
        verify(jdbcOperations, times(1)).update("ALTER SESSION SET CURRENT_SCHEMA=\"newSchema\"");
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        OBOracleSessionExtension sessionExt = new OBOracleSessionExtension();

        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperations);
        when(jdbcOperations.queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class)).thenReturn("schema");
        
        String currentSchema = sessionExt.getCurrentSchema(connection);
        
        assertEquals("schema", currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(OceanBaseConnection.class);
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        OBOracleSessionExtension sessionExt = new OBOracleSessionExtension();

        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(1L);
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperations);
        when(jdbcOperations.queryForObject("select userenv('sessionid') from dual", String.class)).thenReturn("1");
        
        String connectionId = sessionExt.getConnectionId(connection);
        
        assertEquals("1", connectionId);
    }
}