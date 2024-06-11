
import org.junit.Before; 
import org.junit.Test; 
import org.junit.Assert; 

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class OBOracleSessionExtensionTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private OceanBaseConnection mockOceanBaseConnection;
    
    private OBOracleSessionExtension obOracleSessionExtension;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        obOracleSessionExtension = new OBOracleSessionExtension();
    }
    
    @Test
    public void testSwitchSchema() throws Exception{
        String schemaName = "test_schema";
        Mockito.when(mockConnection.getSchema()).thenReturn(schemaName);
        obOracleSessionExtension.switchSchema(mockConnection, schemaName);
        assert(mockConnection.getSchema().equals(schemaName));
    }
    
    @Test
    public void testKillQuery() {
        String connectionId = "test_id";
        obOracleSessionExtension.killQuery(mockConnection, connectionId);
    }  

    @Test 
    public void testGetCurrentSchema() {
        String schemaName = "test_schema";
        obOracleSessionExtension.getCurrentSchema(mockConnection);
        assert(mockConnection.getSchema().equals(schemaName));
    }
    
    @Test 
    public void testGetConnectionId() {
        String connectionId = "test_id";
        obOracleSessionExtension.getConnectionId(mockConnection);
        assert(connectionId);
    }

    @Test
    public void getConnectionId_WithOceanBaseConnection(){
        String connectionId = "test_id";
        Mockito.when(mockOceanBaseConnection.getServerThreadId()).thenReturn(Long.parseLong(connectionId));
        obOracleSessionExtension.getConnectionId(mockOceanBaseConnection);
        assert(connectionId);
    }

    @Test(expected = Exception.class)
    public void getConnectionId_WithException() {
        Mockito.when(mockConnection.getSchema()).thenThrow(new Exception());
        obOracleSessionExtension.getConnectionId(mockConnection);
    }
}
