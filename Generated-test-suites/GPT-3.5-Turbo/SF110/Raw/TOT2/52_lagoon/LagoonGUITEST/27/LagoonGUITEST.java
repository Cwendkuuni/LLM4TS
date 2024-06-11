import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class LagoonGUITest {

    @Test
    public void testMain() {
        String[] args = {"test.properties"};
        assertDoesNotThrow(() -> {
            LagoonGUI.main(args);
        });
    }

    @Test
    public void testLagoonGUI() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        assertNotNull(lagoonGUI);
    }

    @Test
    public void testBuild() {
        // Test for when processor is null
    }

    @Test
    public void testInit() {
        // Test for AuthenticationMissingException
        // Test for AuthenticationException
        // Test for FileNotFoundException
        // Test for IOException
        // Test for LagoonException
    }

    @Test
    public void testExit() {
        // Test for IOException when destroying LagoonProcessor
    }

    @Test
    public void testLoadProperties() {
        // Test for LagoonException
        // Test for IOException
    }

    @Test
    public void testGetProperty() {
        // Test for when value is null
    }
}

