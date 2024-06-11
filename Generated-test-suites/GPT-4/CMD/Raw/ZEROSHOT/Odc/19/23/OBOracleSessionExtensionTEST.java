
package com.oceanbase.odc.plugin.connect.oboracle;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

public class OBOracleSessionExtensionTest {
    private OBOracleSessionExtension sessionExtension;
    private Connection mockConnection;

    @Before
    public void setUp() {
        sessionExtension = new OBOracleSessionExtension();
        mockConnection = Mockito.mock(Connection.class);
    }

    @Test
    public void testKillQuery() {
        try {
            sessionExtension.killQuery(mockConnection, "123");
        } catch (Exception e) {
            assertNull(e);
        }
    }

    @Test
    public void testSwitchSchema() {
        try {
            when(mockConnection.getCatalog()).thenReturn("oldSchema");
            sessionExtension.switchSchema(mockConnection, "newSchema");
        } catch (Exception e) {
            assertNull(e);
        }
    }

    @Test
    public void testGetCurrentSchema() {
        String expectedSchema = "mySchema";
        when(mockConnection.getCatalog()).thenReturn(expectedSchema);
        String actualSchema = sessionExtension.getCurrentSchema(mockConnection);
        assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testGetConnectionId() {
        String expectedId = "123";
        when(mockConnection.getClientInfo("sessionId")).thenReturn(expectedId);
        String actualId = sessionExtension.getConnectionId(mockConnection);
        assertEquals(expectedId, actualId);
    }
}
