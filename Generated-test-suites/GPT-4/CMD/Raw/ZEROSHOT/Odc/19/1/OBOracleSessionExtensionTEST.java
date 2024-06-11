
package com.oceanbase.odc.plugin.connect.oboracle;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.pf4j.Extension;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension obOracleSessionExtension;
    private Connection mockedConnection;

    @Before
    public void setUp() {
        obOracleSessionExtension = new OBOracleSessionExtension();
        mockedConnection = mock(Connection.class);
    }

    @Test
    public void testKillQuery() {
        String connectionId = "1234";
        obOracleSessionExtension.killQuery(mockedConnection, connectionId);
        verify(mockedConnection, times(1)).createStatement()
            .execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testSchema";
        obOracleSessionExtension.switchSchema(mockedConnection, schemaName);
        verify(mockedConnection, times(1)).createStatement()
            .execute("ALTER SESSION SET CURRENT_SCHEMA=" + schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        String expectedSchema = "testSchema";
        assertEquals(expectedSchema, obOracleSessionExtension.getCurrentSchema(mockedConnection));
    }

    @Test
    public void testGetConnectionId() {
        String expectedConnectionId = "1234";
        assertEquals(expectedConnectionId, obOracleSessionExtension.getConnectionId(mockedConnection));
    }

    @Test
    public void testGetConnectionIdWithException() {
        when(obOracleSessionExtension.getConnectionId(mockedConnection)).thenThrow(Exception.class);
        assertNull(obOracleSessionExtension.getConnectionId(mockedConnection));
    }
}
