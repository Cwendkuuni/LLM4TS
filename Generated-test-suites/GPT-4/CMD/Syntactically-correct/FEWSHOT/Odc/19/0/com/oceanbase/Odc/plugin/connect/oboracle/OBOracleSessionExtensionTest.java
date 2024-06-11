package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;

@RunWith(MockitoJUnitRunner.class)
public class OBOracleSessionExtensionTest {

    @Mock
    private Connection conMock; 

    @Mock
    private OceanBaseConnection obConMock;

    private OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();


    @Test
    public void testGetConnectionId() {

        String connectionId = "12345";

        when(obConMock.getServerThreadId()).thenReturn(Long.parseLong(connectionId));
        assertEquals(connectionId, obOracleSessionExtension.getConnectionId(obConMock));

        assertEquals(connectionId, obOracleSessionExtension.getConnectionId(conMock));
    }

    @Test
    public void testGetCurrentSchema() {

        String schema = "OBOracle";
        
        when(JdbcOperationsUtil.getJdbcOperations(conMock).queryForObject(anyString(), eq(String.class))).thenReturn(schema); 

        assertEquals(schema, obOracleSessionExtension.getCurrentSchema(conMock)); 
      
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String currentSchema = "currentSchema";
        String newSchema = "newSchema";
        
        when(obOracleSessionExtension.getCurrentSchema(conMock)).thenReturn(currentSchema);

        obOracleSessionExtension.switchSchema(conMock, newSchema);
        
        verify(JdbcOperationsUtil.getJdbcOperations(conMock)).update(anyString());
    } 

    @Test
    public void testKillQuery() {
        String connectionId = "12345";

        obOracleSessionExtension.killQuery(conMock, connectionId);

        verify(JdbcOperationsUtil.getJdbcOperations(conMock)).execute(anyString());
    }
}