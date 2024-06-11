import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OBConsoleDataSourceFactoryTest {
    private OBConsoleDataSourceFactory factory;
    private ConnectionConfig config;
    private Boolean autoCommit;

    @Before
    public void setUp() {
        // initialize your objects here.
        config = new ConnectionConfig();
        autoCommit = true;
        factory = new OBConsoleDataSourceFactory(config, autoCommit);
    }

    @Test
    public void testOBConsoleDataSourceFactoryConstructors() {
        assertNotNull(factory);
    }

    @Test
    public void testGetJdbcUrl() {
        // We cannot assert exact url as it depends on various parameters. Only null-check can be performed.
        assertNotNull(factory.getJdbcUrl());
    }

    @Test
    public void testGetUsername() {
        String expectedUsername = "testUsername";
        // Assume that testUsername is set in connectionConfig.
        assertEquals(expectedUsername, OBConsoleDataSourceFactory.getUsername(config));
    }

    @Test
    public void testGetPassword() {
        String expectedPassword = "testPassword";
        // Assume that testPassword is set in connectionConfig.
        assertEquals(expectedPassword, OBConsoleDataSourceFactory.getPassword(config));
    }

    // ... Do similar tests for other methods ...

    @Test(expected = IllegalArgumentException.class)
    public void testResetPortWithNegativeValue() {
        factory.resetPort(value -> -10); // or use Mockito to mock the mapper if convenient
    }

    // ... Do tests for edge cases and exceptions here ...

}