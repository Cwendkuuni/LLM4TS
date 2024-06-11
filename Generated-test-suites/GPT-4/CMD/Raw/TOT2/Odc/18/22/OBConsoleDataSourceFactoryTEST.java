import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {
    private OBConsoleDataSourceFactory obConsoleDataSourceFactory;
    private ConnectionConfig connectionConfig;

    @Before
    public void setUp() {
        connectionConfig = new ConnectionConfig();
        // setup the connectionConfig object with appropriate data
        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testGetUsername() {
        String expectedUsername = // the expected username based on the provided connectionConfig;
        assertEquals(expectedUsername, obConsoleDataSourceFactory.getUsername());
    }

    // Write similar tests for all other public methods, checking edge cases for the values being passed and returned

}