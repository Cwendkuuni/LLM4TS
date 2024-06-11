package com.oceanbase.odc.plugin.connect.oboracle;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

public class OBOracleSessionExtensionTest {

    @Mock
    private Connection mockConnection;
    @Mock
    private OceanBaseConnection oceanBaseConnection;
    @Mock
    private JdbcOperationsUtil mockJdbcOperationsUtil;

    private OBOracleSessionExtension underTest;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new OBOracleSessionExtension();
    }

    @Test
    public void testKillQuery() {
        String connectionId = "testConnectionId";

        underTest.killQuery(mockConnection, connectionId);
        verify(mockJdbcOperationsUtil, times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testSchema";
        String currentSchema = "currentSchema";

        when(mockJdbcOperationsUtil.getCurrentSchema(mockConnection)).thenReturn(currentSchema);

        underTest.switchSchema(mockConnection, schemaName);
        verify(mockJdbcOperationsUtil, times(1)).update("ALTER SESSION SET CURRENT_SCHEMA=" + schemaName);
    }

    @Test
    public void testGetConnectionId() {
        String querySql = "select userenv('sessionid') from dual";
        String sessionId = "testSessionId";

        when(mockJdbcOperationsUtil.queryForObject(querySql, String.class)).thenReturn(sessionId);
        
        assertEquals(sessionId, underTest.getConnectionId(mockConnection));
    }

    @Test
    public void testGetConnectionIdException() {
        String querySql = "select userenv('sessionid') from dual";
        String sessionId = "testSessionId";
        String oceanBaseConnectionId = "oceanBaseConnectionId";

        when(mockJdbcOperationsUtil.queryForObject(querySql, String.class)).thenThrow(new RuntimeException());
        when(oceanBaseConnection.getServerThreadId()).thenReturn(Long.valueOf(oceanBaseConnectionId));

        assertEquals(oceanBaseConnectionId, underTest.getConnectionId(mockConnection));
    }

    @Test
    public void testGetCurrentSchema() {
        String querySql = "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL";
        String expectedSchema = "testSchema";

        when(mockJdbcOperationsUtil.queryForObject(querySql, String.class)).thenReturn(expectedSchema);
        
        assertEquals(expectedSchema, underTest.getCurrentSchema(mockConnection));
    }
}
