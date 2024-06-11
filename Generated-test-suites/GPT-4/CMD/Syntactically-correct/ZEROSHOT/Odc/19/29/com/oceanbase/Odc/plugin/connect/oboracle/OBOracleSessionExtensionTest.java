package com.oceanbase.odc.plugin.connect.oboracle;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.sql.Connection;

import static org.mockito.Mockito.*;

public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension extension;
    private Connection mockedConnection;

    @Before
    public void setUp() throws Exception {
        extension = new OBOracleSessionExtension();
        mockedConnection = Mockito.mock(Connection.class);
    }

    @Test
    public void testKillQuery() {
        String connectionId = "123";
        extension.killQuery(mockedConnection, connectionId);
        verify(mockedConnection, times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "TestSchema";
        when(extension.getCurrentSchema(mockedConnection)).thenReturn("oldSchema");
        extension.switchSchema(mockedConnection, schemaName);
        verify(mockedConnection, times(1)).update("ALTER SESSION SET CURRENT_SCHEMA=" + schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        String querySql = "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL";
        extension.getCurrentSchema(mockedConnection);
        verify(mockedConnection, times(1)).queryForObject(querySql, String.class);
    }

    @Test
    public void testGetConnectionId() {
        String querySql = "select userenv('sessionid') from dual";
        extension.getConnectionId(mockedConnection);
        verify(mockedConnection, times(1)).queryForObject(querySql, String.class);
    }

}