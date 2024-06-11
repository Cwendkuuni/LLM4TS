package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperations;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;

public class OBOracleSessionExtensionTest {
    OBOracleSessionExtension extension;
    Connection mockConnection;
    OceanBaseConnection oceanBaseConnection;
    JdbcOperations jdbcOperations;

    @Before
    public void setUp(){
        extension = new OBOracleSessionExtension();
        mockConnection = Mockito.mock(Connection.class);
        oceanBaseConnection = Mockito.mock(OceanBaseConnection.class);
        jdbcOperations = Mockito.mock(JdbcOperations.class);

        when(JdbcOperationsUtil.getJdbcOperations(mockConnection)).thenReturn(jdbcOperations);
        when(JdbcOperationsUtil.getJdbcOperations(oceanBaseConnection)).thenReturn(jdbcOperations);
    }

    @Test
    public void testKillQuery() {
        extension.killQuery(mockConnection, "101");
        verify(jdbcOperations, times(1)).execute("KILL QUERY 101");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        when(jdbcOperations.queryForObject(anyString(), anyString().getClass()))
            .thenReturn("DIFFERENT_SCHEMA", "SAME_SCHEMA");
        extension.switchSchema(mockConnection, "DIFFERENT_SCHEMA");
        verify(jdbcOperations, times(1)).update(anyString());

        extension.switchSchema(mockConnection, "SAME_SCHEMA");
        verify(jdbcOperations, times(1)).update(anyString());
    }

    @Test
    public void testGetCurrentSchema() {
        when(jdbcOperations.queryForObject(anyString(), anyString().getClass()))
            .thenReturn("SAMPLE_SCHEMA");
        String schema = extension.getCurrentSchema(mockConnection);
        assertEquals("SAMPLE_SCHEMA", schema);
    }

    @Test
    public void testGetConnectionId() {
        when(jdbcOperations.queryForObject(anyString(), anyString().getClass()))
            .thenReturn("101")
            .thenThrow(new RuntimeException());
        when(oceanBaseConnection.getServerThreadId()).thenReturn(999L);

        String connId = extension.getConnectionId(mockConnection);
        assertEquals("101", connId);

        connId = extension.getConnectionId(oceanBaseConnection);
        assertEquals("999", connId);
    }
}