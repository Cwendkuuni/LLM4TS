
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

public class OBMySQLSessionExtensionTest {

    private OBMySQLSessionExtension obMySqlSessionExtension;
    private Connection mockConnection;

    @Before
    public void setUp() {
        obMySqlSessionExtension = new OBMySQLSessionExtension();
        mockConnection = mock(Connection.class);
    }

    @Test
    public void testKillQuery() {
        String connectionId = "test-connection-id";
        obMySqlSessionExtension.killQuery(mockConnection, connectionId);
        verify(mockConnection).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test-schema";
        obMySqlSessionExtension.switchSchema(mockConnection, schemaName);
        verify(mockConnection).setCatalog(schemaName);
    }

    @Test
    public void testGetCurrentSchema() {
        String expectedSchemaName = "test-schema";
        when(obMySqlSessionExtension.getCurrentSchema(mockConnection)).thenReturn(expectedSchemaName);
        String result = obMySqlSessionExtension.getCurrentSchema(mockConnection);
        assertEquals(expectedSchemaName, result);
    }

    @Test
    public void testGetConnectionId() {
        String expectedConnectionId = "test-connection-id";
        when(obMySqlSessionExtension.getConnectionId(mockConnection)).thenReturn(expectedConnectionId);
        String result = obMySqlSessionExtension.getConnectionId(mockConnection);
        assertEquals(expectedConnectionId, result);
    }
    
    // tests to handle exception scenarios
    @Test(expected = SQLException.class)
    public void testSwitchSchemaWithSQLException() throws SQLException {
        doThrow(SQLException.class).when(mockConnection).setCatalog(anyString());
        obMySqlSessionExtension.switchSchema(mockConnection, "newSchema");
    }

    @Test(expected = RuntimeException.class)
    public void testGetConnectionIdWithException() {
        when(obMySqlSessionExtension.getConnectionId(mockConnection)).thenThrow(new RuntimeException());
        obMySqlSessionExtension.getConnectionId(mockConnection);
    }
}
