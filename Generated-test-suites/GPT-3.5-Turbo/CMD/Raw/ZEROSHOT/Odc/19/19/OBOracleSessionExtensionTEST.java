package com.oceanbase.odc.plugin.connect.oboracle;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

public class OBOracleSessionExtensionTest {

    @Test
    public void testKillQuery() {
        Connection connection = mock(Connection.class);
        String connectionId = "123";
        
        OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();
        sessionExtension.killQuery(connection, connectionId);
        
        // Verify that the execute method was called with the correct query
        verify(JdbcOperationsUtil.getJdbcOperations(connection)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "TEST_SCHEMA";
        
        OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();
        sessionExtension.switchSchema(connection, schemaName);
        
        // Verify that the update method was called with the correct alter SQL
        verify(JdbcOperationsUtil.getJdbcOperations(connection)).update("ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName));
    }

    @Test
    public void testGetCurrentSchema() {
        Connection connection = mock(Connection.class);
        
        OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();
        sessionExtension.getCurrentSchema(connection);
        
        // Verify that the queryForObject method was called with the correct query
        verify(JdbcOperationsUtil.getJdbcOperations(connection)).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class);
    }

    @Test
    public void testGetConnectionId() {
        Connection connection = mock(Connection.class);
        
        OBOracleSessionExtension sessionExtension = new OBOracleSessionExtension();
        sessionExtension.getConnectionId(connection);
        
        // Verify that the queryForObject method was called with the correct query
        verify(JdbcOperationsUtil.getJdbcOperations(connection)).queryForObject("select userenv('sessionid') from dual", String.class);
    }

}