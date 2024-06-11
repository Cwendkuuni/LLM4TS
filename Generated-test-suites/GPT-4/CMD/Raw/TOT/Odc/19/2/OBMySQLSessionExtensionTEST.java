import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

public class OBMySQLSessionExtensionTest {

    private OBMySQLSessionExtension extension;
    private Connection mockConnection;
    private String connectionId = "12345";
    private String schemaName = "testSchema";

    @Before
    public void setUp() {
        extension = new OBMySQLSessionExtension();
        mockConnection = mock(Connection.class);
    }

    @Test
    public void testKillQuery() {
        extension.killQuery(mockConnection, connectionId);
        verify(mockConnection).execute(String.format("KILL QUERY %s", connectionId));
    }

    @Test
    public void testSwitchSchema_alreadyCurrent() throws SQLException {
        when(extension.getCurrentSchema(mockConnection)).thenReturn(schemaName);
        extension.switchSchema(mockConnection, schemaName);
        verify(mockConnection, never()).setCatalog(anyString());
    }

    @Test
    public void testSwitchSchema_differentSchema() throws SQLException {
        when(extension.getCurrentSchema(mockConnection)).thenReturn("differentSchema");
        extension.switchSchema(mockConnection, schemaName);
        verify(mockConnection).setCatalog(schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        String expectedSchema = "testSchema";
        when(extension.getCurrentSchema(mockConnection)).thenReturn(expectedSchema);
        String actualSchema = extension.getCurrentSchema(mockConnection);
        assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testGetConnectionId_noExceptionThrown() {
        when(extension.getConnectionId(mockConnection)).thenReturn(connectionId);
        String actualId = extension.getConnectionId(mockConnection);
        assertEquals(connectionId, actualId);
    }

    @Test
    public void testGetConnectionId_exceptionThrown() {
        when(extension.getConnectionId(any())).thenThrow(new Exception());
        when(((OceanBaseConnection) mockConnection).getServerThreadId()).thenReturn(123L);
        String actualId = extension.getConnectionId(mockConnection);
        assertEquals("123", actualId);
    }
}