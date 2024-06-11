package com.oceanbase.odc.plugin.connect.oboracle;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import java.sql.Connection;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OBOracleSessionExtensionTest {

    @Mock
    private Connection mockConnection;

    @Test
    public void killQueryTest() {
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        obOracleSessionExtension.killQuery(mockConnection, "123");
        verify(mockConnection).execute("KILL QUERY 123");
    }

    @Test
    public void switchSchemaTest() throws Exception {
        when(mockConnection.getCurrentSchema()).thenReturn("oldSchema");
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        obOracleSessionExtension.switchSchema(mockConnection, "newSchema");
        verify(mockConnection).update("ALTER SESSION SET CURRENT_SCHEMA=newSchema");
    }

    @Test
    public void getCurrentSchemaTest() {
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();
        obOracleSessionExtension.getCurrentSchema(mockConnection);
        verify(mockConnection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class);
    }

    @Test
    public void getConnectionIdTest() {
        OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();

        // for normal case
        when(mockConnection.queryForObject("select userenv('sessionid') from dual", String.class)).thenReturn("123");
        assertEquals("123", obOracleSessionExtension.getConnectionId(mockConnection));

        // for exception case 
        when(mockConnection.queryForObject("select userenv('sessionid') from dual", String.class)).thenThrow(new RuntimeException());
        when(mockConnection.getServerThreadId()).thenReturn((long)456);
        assertEquals("456", obOracleSessionExtension.getConnectionId(mockConnection));
    }
}