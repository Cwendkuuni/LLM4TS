
package com.oceanbase.odc.plugin.connect.oboracle;

import org.junit.Test;
import org.junit.Before;
import org.mockito.Mockito;
import java.sql.Connection;
import com.oceanbase.jdbc.OceanBaseConnection;

import static org.mockito.Mockito.*;

public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension obOracleSessionExtension;
    private Connection connectionMock;
    private OceanBaseConnection obcMock;

    @Before
    public void setUp() {
        obOracleSessionExtension = new OBOracleSessionExtension();
        connectionMock = Mockito.mock(Connection.class);
        obcMock = Mockito.mock(OceanBaseConnection.class);
    }

    @Test
    public void testKillQuery() {
        obOracleSessionExtension.killQuery(connectionMock, "testConnectionId");
        verify(connectionMock, times(1)).execute("KILL QUERY testConnectionId");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        obOracleSessionExtension.switchSchema(connectionMock, "testSchema");
        verify(connectionMock, times(1)).update("ALTER SESSION SET CURRENT_SCHEMA='testSchema'");
    }

    @Test
    public void testGetCurrentSchema() {
        obOracleSessionExtension.getCurrentSchema(connectionMock);
        verify(connectionMock, times(1)).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class);
    }

    @Test
    public void testGetConnectionId_usingOBConnection() {
        when(connectionMock instanceof OceanBaseConnection).thenReturn(true);
        when(obcMock.getServerThreadId()).thenReturn(123L);
        obOracleSessionExtension.getConnectionId(obcMock);
        verify(obcMock, times(2)).queryForObject("select userenv('sessionid') from dual", String.class);
    }

    // Continue with tests for edge cases and exception scenarios
    
}
