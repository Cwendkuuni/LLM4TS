package com.oceanbase.odc.plugin.connect.oboracle;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Connection;
import java.sql.SQLException;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperations;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;

@RunWith(MockitoJUnitRunner.class)
public class OBOracleSessionExtensionTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private JdbcOperations mockJdbcOperations;

    @Mock
    private OceanBaseConnection oceanBaseConnection;

    @InjectMocks
    private OBOracleSessionExtension oBOracleSessionExtension;

    private String connectionId = "1234";
    private String currentSchema = "schema";
    private String newSchema = "newSchema";

    @Before
    public void setUp() {
        when(mockConnection instanceof OceanBaseConnection).thenReturn(true);
        when(mockConnection.getServerThreadId()).thenReturn(1234L);
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection)).thenReturn(mockJdbcOperations);
    }

    @Test
    public void testKillQuery() {
        oBOracleSessionExtension.killQuery(mockConnection, connectionId);
        verify(mockJdbcOperations).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Mockito.when(oBOracleSessionExtension.getCurrentSchema(mockConnection)).thenReturn(currentSchema).thenReturn(newSchema);
        oBOracleSessionExtension.switchSchema(mockConnection, newSchema);
        verify(mockJdbcOperations).update("ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(newSchema));
        assertEquals(newSchema, oBOracleSessionExtension.getCurrentSchema(mockConnection));
    }

    @Test
    public void testGetCurrentSchema() {
        when(mockJdbcOperations.queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class)).thenReturn(currentSchema);
        assertEquals(currentSchema, oBOracleSessionExtension.getCurrentSchema(mockConnection));
    }

    @Test
    public void testGetConnectionId() {
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject("select userenv('sessionid') from dual", String.class)).thenReturn(connectionId);
        assertEquals(connectionId, oBOracleSessionExtension.getConnectionId(mockConnection));
    }

    @Test
    public void testGetConnectionIdWithException() {
        doThrow(new RuntimeException()).when(mockJdbcOperations).queryForObject("select userenv('sessionid') from dual", String.class);
        String connectionId = oceanBaseConnection.getServerThreadId() + "";
        assertEquals(connectionId, oBOracleSessionExtension.getConnectionId(mockConnection));
    }
}