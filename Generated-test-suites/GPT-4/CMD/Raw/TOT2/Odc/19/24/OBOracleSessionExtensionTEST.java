package com.oceanbase.odc.plugin.connect.oboracle;

import java.sql.Connection;

import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OBOracleSessionExtensionTest {

    private final OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
    private final Connection connection = Mockito.mock(Connection.class);
    private final String queryIdentifier = "1";
    private final String schemaName = "schemaExample";
 
    @Test
    public void testKillQuery() {
        obOracleSessionExtension.killQuery(connection, queryIdentifier);
        verify(connection).execute("KILL QUERY " + queryIdentifier);
    }

    @Test
    public void testSwitchSchema() throws Exception {
        when(connection.getCurrentSchema()).thenReturn(schemaName);
        obOracleSessionExtension.switchSchema(connection, schemaName);
        verify(connection).update("ALTER SESSION SET CURRENT_SCHEMA=" + schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        obOracleSessionExtension.getCurrentSchema(connection);
        verify(connection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class);
    }

    @Test
    public void testGetConnectionId() {
        obOracleSessionExtension.getConnectionId(connection);
        verify(connection).queryForObject("select userenv('sessionid') from dual", String.class);
    }

}