
package com.oceanbase.odc.plugin.connect.oboracle;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.sql.Connection;
import java.sql.SQLException;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OBOracleSessionExtensionTest {

    OBOracleSessionExtension obOracleSessionExtension;

    @Mock
    Connection mockConnection;
    @Mock
    OceanBaseConnection oceanBaseConnection;

    @Before
    public void setUp() {
        obOracleSessionExtension = new OBOracleSessionExtension();
    }

    @Test
    public void killQueryTest() {
        String connectionId = "connection1";
        obOracleSessionExtension.killQuery(mockConnection, connectionId);
        verify(mockConnection, times(1)).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void switchSchemaTest_sameSchema() throws SQLException {
        String schema = "testSchema";
        when(obOracleSessionExtension.getCurrentSchema(mockConnection)).thenReturn(schema);
        obOracleSessionExtension.switchSchema(mockConnection, schema);
        verify(mockConnection, never()).update(anyString());
    }

    @Test
    public void switchSchemaTest_differentSchema() throws SQLException {
        String schema = "testSchema";
        when(obOracleSessionExtension.getCurrentSchema(mockConnection)).thenReturn("differentSchema");
        obOracleSessionExtension.switchSchema(mockConnection, schema);
        verify(mockConnection, times(1)).update("ALTER SESSION SET CURRENT_SCHEMA=" + schema);
    }

    @Test
    public void getCurrentSchemaTest() {
        String sql = "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL";
        obOracleSessionExtension.getCurrentSchema(mockConnection);
        verify(mockConnection, times(1)).queryForObject(sql, String.class);
    }

    @Test
    public void getConnectionIdTest_NoException() {
        String sql = "select userenv('sessionid') from dual";
        obOracleSessionExtension.getConnectionId(mockConnection);
        verify(mockConnection, times(1)).queryForObject(sql, String.class);
    }

    @Test
    public void getConnectionIdTest_CatchException() {
        String sql = "select userenv('sessionid') from dual";
        when(mockConnection.queryForObject(sql, String.class)).thenThrow(new RuntimeException());
        obOracleSessionExtension.getConnectionId(oceanBaseConnection);
        verify(oceanBaseConnection, times(1)).getServerThreadId();
    }
}
