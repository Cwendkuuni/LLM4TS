package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;

public class OBOracleSessionExtensionTest {

    private Connection connection = mock(OceanBaseConnection.class);
    private OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();

    @Test
    public void testKillQuery() {
        sessionExtension.killQuery(connection, "123");
        // Verify the functionality of killQuery method here
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class)).thenReturn("SCHEMA1");
        
        sessionExtension.switchSchema(connection, "SCHEMA1");
        // Verify the functionality of switchSchema method here
    }

    @Test
    public void testGetCurrentSchema() {
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class)).thenReturn("SCHEMA1");
        
        String schema = sessionExtension.getCurrentSchema(connection);
        assertEquals("SCHEMA1", schema);
    }

    @Test
    public void testGetConnectionId() {
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select userenv('sessionid') from dual", String.class)).thenReturn("12345");
        
        String connectionId = sessionExtension.getConnectionId(connection);
        assertEquals("12345", connectionId);
    }

    @Test
    public void testGetConnectionIdException() {
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select userenv('sessionid') from dual", String.class)).thenThrow(new RuntimeException());
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(12345L);
        
        String connectionId = sessionExtension.getConnectionId(connection);
        assertEquals("12345", connectionId);
    }

}