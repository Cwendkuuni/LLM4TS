import org.junit.Test;
import static org.junit.Assert.*;

public class LagoonGUITest {

    @Test
    public void testMainWindow() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        assertNotNull(lagoonGUI);
    }

    @Test
    public void testLoadProperties() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.loadProperties(new File("test.properties"));
        // Assert the properties were loaded correctly
    }

    @Test
    public void testSaveProperties() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.savePropertyFile();
        // Assert the properties were saved correctly
    }

    @Test
    public void testBuild() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.build(false);
        // Assert the build process completed successfully
    }

    @Test
    public void testForceBuild() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.build(true);
        // Assert the build process completed successfully with force option
    }

    @Test
    public void testExit() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.exit();
        // Assert the application exited successfully
    }

    @Test
    public void testInit() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.init();
        // Assert the initialization process completed successfully
    }

    @Test
    public void testWindowEvents() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.windowOpened(null);
        // Assert the windowOpened method executed correctly
        lagoonGUI.windowClosing(null);
        // Assert the windowClosing method executed correctly
        lagoonGUI.windowClosed(null);
        // Assert the windowClosed method executed correctly
        lagoonGUI.windowIconified(null);
        // Assert the windowIconified method executed correctly
        lagoonGUI.windowDeiconified(null);
        // Assert the windowDeiconified method executed correctly
        lagoonGUI.windowActivated(null);
        // Assert the windowActivated method executed correctly
        lagoonGUI.windowDeactivated(null);
        // Assert the windowDeactivated method executed correctly
    }

    @Test
    public void testGetProperty() throws LagoonException {
        Properties prop = new Properties();
        prop.put("key", "value");
        
        String result = LagoonGUI.getProperty(prop, "key");
        assertEquals("value", result);

        try {
            LagoonGUI.getProperty(prop, "invalidKey");
            fail("Expected LagoonException to be thrown");
        } catch (LagoonException e) {
            // Exception was correctly thrown
        }
    }

    @Test
    public void testShowTime() {
        long ms = 5000L;
        String result = LagoonGUI.showTime(ms);
        assertEquals("in 5 s", result);

        ms = 500L;
        result = LagoonGUI.showTime(ms);
        assertEquals("in 500 ms", result);
    }
}