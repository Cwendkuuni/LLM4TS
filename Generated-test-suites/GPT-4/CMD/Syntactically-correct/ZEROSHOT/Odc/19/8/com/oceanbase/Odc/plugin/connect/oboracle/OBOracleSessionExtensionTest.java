package com.oceanbase.odc.plugin.connect.oboracle;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension obOracleSessionExtension;
    private Connection mockConnection;
    private OceanBaseConnection mockOceanBaseConnection;

    @Before
    public void setUp() {
        obOracleSessionExtension = new OBOracleSessionExtension();
        mockConnection = mock(Connection.class);
        mockOceanBaseConnection = mock(OceanBaseConnection.class);
    }

    @Test
    public void testKillQuery() {
        String connectionId = "1010";
        obOracleSessionExtension.killQuery(mockConnection, connectionId);
        verify(mockConnection).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testSchema";
        when(obOracleSessionExtension.getCurrentSchema(mockConnection)).thenReturn(schemaName);
        obOracleSessionExtension.switchSchema(mockConnection, schemaName);
        verify(mockConnection, never()).update(anyString());
      
        String newSchema = "newSchema";
        obOracleSessionExtension.switchSchema(mockConnection, newSchema);
        verify(mockConnection).update("ALTER SESSION SET CURRENT_SCHEMA=" + newSchema);
    }

    @Test
    public void testGetCurrentSchema() {
        obOracleSessionExtension.getCurrentSchema(mockConnection);
        verify(mockConnection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class);
    }

    @Test
    public void testGetConnectionId() {
        obOracleSessionExtension.getConnectionId(mockConnection);
        
        String connectionId = "2020";
        when(mockConnection.queryForObject(anyString(), eq(String.class))).thenThrow(new RuntimeException());
        when(mockOceanBaseConnection.getServerThreadId() + "").thenReturn(connectionId);
        
        String result = obOracleSessionExtension.getConnectionId(mockOceanBaseConnection);
        assertEquals(connectionId, result);
    }
}
