package com.oceanbase.odc.plugin.connect.oboracle;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;

@RunWith(MockitoJUnitRunner.class)
public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension obOracleSessionExtension;

    @Mock
    private Connection mockConnection;

    @Before
    public void setup() {
        obOracleSessionExtension = new OBOracleSessionExtension();
    }

    @Test
    public void testKillQuery() {
        String connectionId = "12345";
        
        obOracleSessionExtension.killQuery(mockConnection, connectionId);

        verify(JdbcOperationsUtil.getJdbcOperations(mockConnection)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "TEST_SCHEMA";
        when(obOracleSessionExtension.getCurrentSchema(mockConnection)).thenReturn("OTHER_SCHEMA");

        obOracleSessionExtension.switchSchema(mockConnection, schemaName);

        verify(JdbcOperationsUtil.getJdbcOperations(mockConnection)).update("ALTER SESSION SET CURRENT_SCHEMA=\"TEST_SCHEMA\"");
    }

    @Test
    public void testGetCurrentSchema() {
        String expectedSchema = "TEST_SCHEMA";
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class)).thenReturn(expectedSchema);

        String result = obOracleSessionExtension.getCurrentSchema(mockConnection);

        assertEquals(expectedSchema, result);
    }

    @Test
    public void testGetConnectionId() {
        String expectedConnectionId = "54321";
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject("select userenv('sessionid') from dual", String.class)).thenReturn(expectedConnectionId);

        String result = obOracleSessionExtension.getConnectionId(mockConnection);

        assertEquals(expectedConnectionId, result);
    }

    @Test
    public void testGetConnectionIdWithOceanBaseConnection() {
        String expectedConnectionId = "54321";
        when(JdbcOperationsUtil.getJdbcOperations(mockConnection).queryForObject("select userenv('sessionid') from dual", String.class)).thenThrow(new RuntimeException());
        when(((OceanBaseConnection) mockConnection).getServerThreadId()).thenReturn(54321L);

        String result = obOracleSessionExtension.getConnectionId(mockConnection);

        assertEquals(expectedConnectionId, result);
    }
}