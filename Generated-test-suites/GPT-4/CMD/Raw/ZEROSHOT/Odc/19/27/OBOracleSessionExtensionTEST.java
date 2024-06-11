
package com.oceanbase.odc.plugin.connect.oboracle;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension obo;
    private Connection connection;

    @Before
    public void setUp() throws Exception {
        obo = new OBOracleSessionExtension();
        connection = Mockito.mock(Connection.class);
    }

    @Test
    public void test_killQuery() {
        String connectionId = "1234";
        obo.killQuery(connection, connectionId);
        verify(connection, times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void test_switchSchema_SameSchema() throws Exception {
        String schemaName = "TestSchema";
        when(obo.getCurrentSchema(connection)).thenReturn(schemaName);
        obo.switchSchema(connection, schemaName);
        verify(connection, never()).update(anyString());
    }

    @Test
    public void test_switchSchema_DifferentSchema() throws Exception {
        String schemaName1 = "TestSchema1";
        String schemaName2 = "TestSchema2";
        when(obo.getCurrentSchema(connection)).thenReturn(schemaName1);
        obo.switchSchema(connection, schemaName2);
        verify(connection, times(1)).update("ALTER SESSION SET CURRENT_SCHEMA=" +
                StringUtils.quoteOracleIdentifier(schemaName2));
    }
    
    @Test
    public void test_getCurrentSchema() {
        String querySql = "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL";
        obo.getCurrentSchema(connection);
        verify(connection, times(1)).queryForObject(querySql, String.class);
    }

    @Test
    public void test_getConnectionId_OceanBaseConnection() throws Exception {
        String querySql = "select userenv('sessionid') from dual";
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(1L);
        obo.getConnectionId(oceanBaseConnection);
        verify(oceanBaseConnection, times(1)).queryForObject(querySql, String.class);
    }

    @Test
    public void test_getConnectionId_nonOceanBaseConnection() {
        String querySql = "select userenv('sessionid') from dual";
        obo.getConnectionId(connection);
        verify(connection, times(1)).queryForObject(querySql, String.class);
    }
}
