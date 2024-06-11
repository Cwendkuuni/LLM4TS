package com.oceanbase.odc.plugin.connect.oboracle;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OBOracleSessionExtensionTest {

    @Mock
    private Connection connection;

    @Mock
    private JdbcOperationsUtil jdbcOperationsUtil;

    @Mock
    private OceanBaseConnection oceanBaseConnection;

    private OBOracleSessionExtension obOracleSessionExtension;

    @Before
    public void setUp() {
        obOracleSessionExtension = new OBOracleSessionExtension();
    }

    @Test
    public void testKillQuery() {
        String connectionId = "testConnectionId";
        obOracleSessionExtension.killQuery(connection, connectionId);
        verify(jdbcOperationsUtil, times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testSchema";
        when(jdbcOperationsUtil.queryForObject(anyString(), any())).thenReturn(schemaName);
        obOracleSessionExtension.switchSchema(connection, schemaName);
        verify(jdbcOperationsUtil, times(1)).update("ALTER SESSION SET CURRENT_SCHEMA=" + schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        String querySql = "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL";
        when(jdbcOperationsUtil.queryForObject(querySql, String.class)).thenReturn("testSchema");
        String schema = obOracleSessionExtension.getCurrentSchema(connection);
        verify(jdbcOperationsUtil, times(1)).queryForObject(querySql, String.class);
        assertEquals("testSchema", schema);
    }

    @Test
    public void testGetConnectionId() {
        String querySql = "SELECT userenv('sessionid') FROM DUAL";
        when(jdbcOperationsUtil.queryForObject(querySql, String.class)).thenReturn("testConnectionId");
        String connectionId = obOracleSessionExtension.getConnectionId(connection);
        verify(jdbcOperationsUtil, times(1)).queryForObject(querySql, String.class);
        assertEquals("testConnectionId", connectionId);
    }

    @Test
    public void testGetConnectionId_withOceanBaseConnection() {
        String querySql = "SELECT userenv('sessionid') FROM DUAL";
        Mockito.when(jdbcOperationsUtil.queryForObject(querySql, String.class)).thenThrow(new RuntimeException());
        when(oceanBaseConnection.getServerThreadId()).thenReturn(123L);
        String connectionId = obOracleSessionExtension.getConnectionId(oceanBaseConnection);
        assertEquals("123", connectionId);
    }
}
