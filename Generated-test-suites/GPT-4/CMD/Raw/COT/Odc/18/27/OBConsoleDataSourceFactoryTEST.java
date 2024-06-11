import org.junit.Test;
import static org.junit.Assert.*;

public class OBConsoleDataSourceFactoryTest {
    OBConsoleDataSourceFactory factoryUnderTest; // This will be initialized in each test

    @Test
    public void testGetUsername() {
        // In this method, we will initialize the factory with a ConnectionConfig with a known username
        ConnectionConfig config = new ConnectionConfig();
        config.setUsername("Test user");

        // Then construct our factory
        factoryUnderTest = new OBConsoleDataSourceFactory(config, null);

        // Now we can call getUsername() and assert that the output matches the username from config
        assertEquals("Test user", factoryUnderTest.getUsername());

        // Several of your test methods will follow a similar pattern,
        // but remember to test edge cases and exception cases where applicable!
    }
    // ... Additional test methods would go here ...
}