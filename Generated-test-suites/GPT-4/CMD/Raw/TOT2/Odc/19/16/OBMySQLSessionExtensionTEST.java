import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

public class OBMySQLSessionExtensionTest {

    private Connection mockConnection; 
    private OBMySQLSessionExtension extensionUnderTest;
    
    @Before
    public void setup() {
        mockConnection = mock(Connection.class);
        extensionUnderTest = new OBMySQLSessionExtension();
    }
    
    @Test
    public void testKillQuery() {
        extensionUnderTest.killQuery(mockConnection, "1234");
    }
    
    @Test(expected = SQLException.class)
    public void testSwitchSchema() throws SQLException {
        when(mockConnection.getCatalog()).thenReturn("schemaName");
        extensionUnderTest.switchSchema(mockConnection, "schemaName");
        extensionUnderTest.switchSchema(mockConnection, "newSchemaName");
    }
    
    @Test
    public void testGetCurrentSchema() {
        extensionUnderTest.getCurrentSchema(mockConnection);
    }
    
    @Test
    public void testGetConnectionId() {
        when(mockConnection.toString()).thenReturn("connection");
        extensionUnderTest.getConnectionId(mockConnection);
    }
    
    @Test(expected = RuntimeException.class)
    public void testGetConnectionIdException() {
        when(mockConnection.toString()).thenThrow(new RuntimeException());
        extensionUnderTest.getConnectionId(mockConnection);
    }
}