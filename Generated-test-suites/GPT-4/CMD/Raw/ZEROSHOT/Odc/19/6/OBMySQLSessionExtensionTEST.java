
package com.oceanbase.odc.plugin.connect.obmysql;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class OBMySQLSessionExtensionTest {

    private Connection connection;
    private OBMySQLSessionExtension classUnderTest;

    @Before
    public void setUp() {
        connection = mock(Connection.class);
        classUnderTest = new OBMySQLSessionExtension();
    }

    @Test
    public void testKillQuery() {
        String connectionId = "1234";
        classUnderTest.killQuery(connection, connectionId);
        verify(connection, times(1)).createStatement().execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema_sameSchemaDoesNothing() throws SQLException {
        String schemaName = "testSchema";
        when(classUnderTest.getCurrentSchema(connection)).thenReturn(schemaName);
        classUnderTest.switchSchema(connection, schemaName);
        verify(connection, never()).setCatalog(anyString());
    }

    @Test
    public void testSwitchSchema_differentSchemaChangesCatalog() throws SQLException {
        String currentSchema = "currentSchema";
        String newSchema = "newSchema";
        when(classUnderTest.getCurrentSchema(connection)).thenReturn(currentSchema);
        classUnderTest.switchSchema(connection, newSchema);
        verify(connection, times(1)).setCatalog(newSchema);
    }

    @Test
    public void testGetCurrentSchema() {
        String expectedSchema = "currentSchema";
        when(classUnderTest.getCurrentSchema(connection)).thenReturn(expectedSchema);
        String actualSchema = classUnderTest.getCurrentSchema(connection);
        assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testGetConnectionId() {
        String expectedId = "1234";
        when(classUnderTest.getConnectionId(connection)).thenReturn(expectedId);
        String actualId = classUnderTest.getConnectionId(connection);
        assertEquals(expectedId, actualId);
    }

    @Test
    public void getConnectionId_exceptionReturnsNull() throws SQLException {
        when(classUnderTest.getConnectionId(any(Connection.class))).thenThrow(new RuntimeException());
        assertNull(classUnderTest.getConnectionId(connection));
    }
}
