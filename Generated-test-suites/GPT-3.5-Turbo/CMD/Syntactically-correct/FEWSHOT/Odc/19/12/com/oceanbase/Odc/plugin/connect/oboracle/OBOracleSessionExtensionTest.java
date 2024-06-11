package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;

public class OBOracleSessionExtensionTest {

    @Test
    public void testKillQuery() {
        Connection connection = Mockito.mock(Connection.class);
        String connectionId = "123";
        
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        obOracleSessionExtension.killQuery(connection, connectionId);
        
        Mockito.verify(JdbcOperationsUtil.getJdbcOperations(connection)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        String schemaName = "schema";
        
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        obOracleSessionExtension.switchSchema(connection, schemaName);
        
        Mockito.verify(JdbcOperationsUtil.getJdbcOperations(connection)).update("ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName));
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = Mockito.mock(Connection.class);
        String expectedSchema = "schema";
        
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        Mockito.when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(Mockito.anyString(), Mockito.any())).thenReturn(expectedSchema);
        
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        
        assertEquals(expectedSchema, currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = Mockito.mock(OceanBaseConnection.class);
        String expectedId = "123";
        
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        Mockito.when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(Mockito.anyString(), Mockito.any())).thenThrow(new SQLException());
        Mockito.when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(Long.parseLong(expectedId));
        
        String connectionId = obOracleSessionExtension.getConnectionId(connection);
        
        assertEquals(expectedId, connectionId);
    }
}