package com.oceanbase.odc.plugin.connect.oboracle;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;

@RunWith(MockitoJUnitRunner.class)
public class OBOracleSessionExtensionTest {

    @Mock
    private Connection mockConnection;

    private OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();

    @Test
    public void testKillQuery_Success() {
        String connectionId = "123";
        obOracleSessionExtension.killQuery(mockConnection, connectionId);
        verify(JdbcOperationsUtil.getJdbcOperations(mockConnection)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema_Success() throws SQLException {
        String schemaName = "schema";
        when(obOracleSessionExtension.getCurrentSchema(mockConnection)).thenReturn("old_schema");
        obOracleSessionExtension.switchSchema(mockConnection, schemaName);
        verify(JdbcOperationsUtil.getJdbcOperations(mockConnection)).update("ALTER SESSION SET CURRENT_SCHEMA='schema'");
    }

    @Test
    public void testGetCurrentSchema_Success() {
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class)).thenReturn("schema");
        assertEquals("schema", obOracleSessionExtension.getCurrentSchema(mockConnection));
    }

    @Test
    public void testGetConnectionId_Success() {
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject("select userenv('sessionid') from dual", String.class)).thenReturn("id");
        assertEquals("id", obOracleSessionExtension.getConnectionId(mockConnection));
    }

    @Test
    public void testGetConnectionId_HandleSQLException() {
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject("select userenv('sessionid') from dual", String.class)).thenThrow(new SQLException());
        when(mockConnection instanceof OceanBaseConnection).thenReturn(true);
        when(((OceanBaseConnection) mockConnection).getServerThreadId()).thenReturn(123L);
        assertEquals("123", obOracleSessionExtension.getConnectionId(mockConnection));
    }
}