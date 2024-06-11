package com.oceanbase.odc.plugin.connect.oboracle;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.anyString;
import org.springframework.jdbc.core.JdbcOperations;

public class OBOracleSessionExtensionTest {
   
    private OBOracleSessionExtension sessionExtension;
    private Connection mockConnection;
    private JdbcOperations mockJdbcOperations;
   
    @Before
    public void setup() {
        sessionExtension = new OBOracleSessionExtension();
        mockConnection = Mockito.mock(OceanBaseConnection.class);
        mockJdbcOperations = Mockito.mock(JdbcOperations.class);
    }
   
    @Test
    public void testKillQuery() {
        sessionExtension.killQuery(mockConnection, "1001");
        verify(mockJdbcOperations).execute("KILL QUERY " + "1001");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection)).thenReturn(mockJdbcOperations);
        when(mockJdbcOperations.queryForObject(anyString(), String.class)).thenReturn("schema1");
        sessionExtension.switchSchema(mockConnection, "schema1");
        verify(mockJdbcOperations).update("ALTER SESSION SET CURRENT_SCHEMA=" + "schema1");
    }

    @Test
    public void testGetCurrentSchema() {
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection)).thenReturn(mockJdbcOperations);
        when(mockJdbcOperations.queryForObject(anyString(), String.class)).thenReturn("schema1");
        assertEquals("schema1", sessionExtension.getCurrentSchema(mockConnection));
    }

    @Test
    public void testGetConnectionId() {
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection)).thenReturn(mockJdbcOperations);
        when(mockJdbcOperations.queryForObject(anyString(), String.class)).thenReturn("1001");
        assertEquals("1001", sessionExtension.getConnectionId(mockConnection));
    }
}