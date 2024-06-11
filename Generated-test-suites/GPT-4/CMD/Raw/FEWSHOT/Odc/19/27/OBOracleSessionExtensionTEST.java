
package com.oceanbase.odc.plugin.connect.oboracle;

import java.sql.Connection;
import java.sql.SQLException;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;

import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class OBOracleSessionExtensionTest {

    OBOracleSessionExtension extension = new OBOracleSessionExtension();

    @Test
    public void testKillQuery() {
        Connection mockConnection = Mockito.mock(Connection.class);
        JdbcOperationsUtil mockJdbcOperations = Mockito.mock(JdbcOperationsUtil.class);
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection)).thenReturn(mockJdbcOperations);
        extension.killQuery(mockConnection, "42");
        verify(mockJdbcOperations).execute("KILL QUERY 42");
    }

    @Test
    public void testSwitchSchema() {
        try {
            Connection mockConnection = Mockito.mock(Connection.class);
            JdbcOperationsUtil mockJdbcOperations = Mockito.mock(JdbcOperationsUtil.class);
            when(mockJdbcOperations.queryForObject(anyString(), any())).thenReturn("old_schema");
            when(JdbcOperationsUtil.getJdbcOperations(mockConnection)).thenReturn(mockJdbcOperations);
            extension.switchSchema(mockConnection, "new_schema");
            verify(mockJdbcOperations).update("ALTER SESSION SET CURRENT_SCHEMA=\"new_schema\"");
        } catch (SQLException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testGetCurrentSchema() {
        Connection mockConnection = Mockito.mock(Connection.class);
        JdbcOperationsUtil mockJdbcOperations = Mockito.mock(JdbcOperationsUtil.class);
        when(mockJdbcOperations.queryForObject(anyString(), any())).thenReturn("current_schema");
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection)).thenReturn(mockJdbcOperations);
        assertEquals("current_schema", extension.getCurrentSchema(mockConnection));
    }

    @Test
    public void testGetConnectionId() {
        Connection mockConnection = Mockito.mock(OceanBaseConnection.class);
        JdbcOperationsUtil mockJdbcOperations = Mockito.mock(JdbcOperationsUtil.class);
        when(mockJdbcOperations.queryForObject(anyString(), any())).thenThrow(new RuntimeException());
        when(((OceanBaseConnection) mockConnection).getServerThreadId()).thenReturn(42L);
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection)).thenReturn(mockJdbcOperations);
        assertEquals("42", extension.getConnectionId(mockConnection));
    }
}
