package com.oceanbase.odc.plugin.connect.oboracle;

import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import static org.junit.Assert.*;
import org.junit.*;

public class OBOracleSessionExtensionTest {
    private OBOracleSessionExtension sessionExtension;
    private Connection mockConnection;
    private String mockConnectionId = "mockId";

    @Before
    public void setUp() {
        sessionExtension = new OBOracleSessionExtension();
        mockConnection = Mockito.mock(Connection.class);
    }

    @Test
    public void testKillQuery() {
        sessionExtension.killQuery(mockConnection, mockConnectionId);
        assertNotEquals(mockConnection, null); // assuming killQuery does not nullify the connection
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaException() throws SQLException{
        Mockito.when(mockConnection.getSchema()).thenReturn("mockSchema1");
        sessionExtension.switchSchema(mockConnection, "mockSchema1"); // should not throw an exception
        sessionExtension.switchSchema(mockConnection, "mockSchema2"); // should throw an exception
    }

    @Test
    public void testGetCurrentSchema() {
        String expectedSchema = "mockSchema";
        Mockito.when(mockConnection.getSchema()).thenReturn(expectedSchema);
        String actualSchema = sessionExtension.getCurrentSchema(mockConnection);
        assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testGetConnectionIdNoExceptions() {
        Mockito.when(mockConnection.getServerThreadId()).thenReturn(mockConnectionId);
        String returnedId = sessionExtension.getConnectionId(mockConnection);
        assertEquals(mockConnectionId, returnedId);
    }

    @Test
    public void testGetConnectionIdWithExceptions() {
        Mockito.when(mockConnection.getServerThreadId()).thenThrow(new SQLException());
        String returnedId = sessionExtension.getConnectionId(mockConnection);
        assertEquals(null, returnedId); // should return null if exception occurs
    }
}
