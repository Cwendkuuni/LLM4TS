import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import javax.sql.DataSource;

public class OBConsoleDataSourceFactoryTest {

    private ConnectionConfig connectionConfig;
    private OBConsoleDataSourceFactory obConsoleDataSourceFactory;

    @Before
    public void setUp() {
        // Mock ConnectionConfig to return necessary values
        connectionConfig = Mockito.mock(ConnectionConfig.class);
        obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testGetUsername() {
        String expectedUsername = "testUsername";
        Mockito.when(connectionConfig.getUsername()).thenReturn(expectedUsername);
        String actualUsername = obConsoleDataSourceFactory.getUsername();
        assertEquals(expectedUsername, actualUsername);
    }

    @Test
    public void testGetPassword() {
        String expectedPassword = "testPassword";
        Mockito.when(connectionConfig.getPassword()).thenReturn(expectedPassword);
        String actualPassword = obConsoleDataSourceFactory.getPassword();
        assertEquals(expectedPassword, actualPassword);
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = obConsoleDataSourceFactory.getDataSource();
        assertNotNull(dataSource);
        // Add more assertions here based on the expected states and behaviors of the DataSource
    }

    @Test
    public void testDeepCopy() {
        OBConsoleDataSourceFactory copy = obConsoleDataSourceFactory.deepCopy();
        assertNotSame(copy, obConsoleDataSourceFactory);
        // Add more assertions here to verify that the copy is a deep copy
    }

    // Continue writing tests for other methods...
}