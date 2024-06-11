import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class OBOracleSessionExtensionTest {
   
    private OBOracleSessionExtension extension;

    @Before
    public void setUp() {
        extension = new OBOracleSessionExtension();
    }
    
    @Test(expected = NullPointerException.class)
    public void testKillQueryNullConnection() {
        extension.killQuery(null, "testId");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testKillQueryNullId() {
        extension.killQuery(mock(Connection.class), null);
    }

    @Test(expected = NullPointerException.class)
    public void testSwitchSchemaNullConnection() throws SQLException {
        extension.switchSchema(null, "testSchema");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSwitchSchemaNullSchema() throws SQLException {
        extension.switchSchema(mock(Connection.class), null);
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaSameSchema() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        when(extension.getCurrentSchema(mockConnection)).thenReturn("testSchema");
        extension.switchSchema(mockConnection, "testSchema");
    }

    @Test(expected = NullPointerException.class)
    public void testGetCurrentSchemaNullConnection() {
        extension.getCurrentSchema(null);
    }

    @Test(expected = NullPointerException.class)
    public void testGetConnectionIdNullConnection() {
        extension.getConnectionId(null);
    }

    @Test
    public void testGetConnectionIdNotOceanBaseConnection() {
        String result = extension.getConnectionId(mock(Connection.class));
        assertNull(result);
    }
}