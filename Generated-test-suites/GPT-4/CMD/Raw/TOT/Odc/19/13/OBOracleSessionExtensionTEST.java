import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.jdbc.OceanBaseConnection;

public class OBOracleSessionExtensionTest {
    private OBOracleSessionExtension testObject;
    private Connection mockConnection;

    @Before
    public void setup() {
        testObject = new OBOracleSessionExtension();
        mockConnection = mock(Connection.class);
    }

    @Test
    public void testKillQuery() {
        testObject.killQuery(mockConnection, "12345");

        verify(mockConnection, times(1)).execute("KILL QUERY 12345");
    }

    @Test
    public void testSwitchSchema_SameSchema() throws Exception {
        when(mockConnection.getCurrentSchema()).thenReturn("SCHEMA_NAME");

        testObject.switchSchema(mockConnection, "SCHEMA_NAME");

        verify(mockConnection, never()).update(anyString());
    }

    @Test
    public void testSwitchSchema_DifferentSchema() throws Exception {
        when(mockConnection.getCurrentSchema()).thenReturn("SCHEMA_NAME");

        testObject.switchSchema(mockConnection, "NEW_SCHEMA");

        verify(mockConnection, times(1)).update("ALTER SESSION SET CURRENT_SCHEMA=" + "NEW_SCHEMA");
    }

    @Test
    public void testGetCurrentSchema() {
        testObject.getCurrentSchema(mockConnection);

        verify(mockConnection, times(1)).queryForObject(anyString(), eq(String.class));
    }

    @Test
    public void testGetConnectionId_DefaultCase() {
        when(mockConnection.queryForObject(anyString(), eq(String.class))).thenReturn("12345");

        String connectionId = testObject.getConnectionId(mockConnection);

        assertEquals("12345", connectionId);
    }

    @Test
    public void testGetConnectionId_ErrorCase() {
        when(mockConnection.queryForObject(anyString(), eq(String.class))).thenThrow(new RuntimeException());
        when(((OceanBaseConnection) mockConnection).getServerThreadId()).thenReturn(12345L);

        String connectionId = testObject.getConnectionId(mockConnection);

        assertEquals("12345", connectionId);
    }
}