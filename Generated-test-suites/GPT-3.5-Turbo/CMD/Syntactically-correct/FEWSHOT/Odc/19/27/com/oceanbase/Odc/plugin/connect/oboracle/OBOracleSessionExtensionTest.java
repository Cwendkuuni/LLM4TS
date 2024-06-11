package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.common.util.StringUtils;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension obOracleSessionExtension;
    private Connection mockConnection;

    @Before
    public void setUp() {
        obOracleSessionExtension = new OBOracleSessionExtension();
        mockConnection = mock(Connection.class);
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "1234";
        JdbcOperationsUtil jdbcOperations = mock(JdbcOperationsUtil.class);
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection)).thenReturn(jdbcOperations);

        obOracleSessionExtension.killQuery(mockConnection, connectionId);

        verify(jdbcOperations).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "TEST_SCHEMA";
        String currentSchema = "CURRENT_SCHEMA";
        JdbcOperationsUtil jdbcOperations = mock(JdbcOperationsUtil.class);
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection)).thenReturn(jdbcOperations);
        when(obOracleSessionExtension.getCurrentSchema(mockConnection)).thenReturn(currentSchema);

        obOracleSessionExtension.switchSchema(mockConnection, schemaName);

        verify(jdbcOperations).update("ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName));
    }

    @Test
    public void testGetCurrentSchema() {
        String expectedSchema = "TEST_SCHEMA";
        JdbcOperationsUtil jdbcOperations = mock(JdbcOperationsUtil.class);
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection)).thenReturn(jdbcOperations);
        when(jdbcOperations.queryForObject(anyString(), eq(String.class))).thenReturn(expectedSchema);

        String actualSchema = obOracleSessionExtension.getCurrentSchema(mockConnection);

        assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testGetConnectionId() {
        String expectedConnectionId = "1234";
        JdbcOperationsUtil jdbcOperations = mock(JdbcOperationsUtil.class);
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection)).thenReturn(jdbcOperations);
        when(jdbcOperations.queryForObject(anyString(), eq(String.class))).thenThrow(new RuntimeException());
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(mockConnection instanceof OceanBaseConnection).thenReturn(true);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(1234);

        String actualConnectionId = obOracleSessionExtension.getConnectionId(mockConnection);

        assertEquals(expectedConnectionId, actualConnectionId);
    }
}