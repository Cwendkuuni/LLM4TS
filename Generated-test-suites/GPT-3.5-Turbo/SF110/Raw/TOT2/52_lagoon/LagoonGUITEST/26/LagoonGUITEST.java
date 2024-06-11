package nu.staldal.lagoon;

import org.junit.Test;
import static org.junit.Assert.*;

public class LagoonGUITest {

    private LagoonGUI lagoonGUI;

    @Before
    public void setUp() {
        lagoonGUI = new LagoonGUI();
    }

    @Test
    public void testMain() {
        try {
            String[] args = {"test.properties"};
            LagoonGUI.main(args);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testWindowOpened() {
        // Test windowOpened method here
    }

    @Test
    public void testWindowClosing() {
        // Test windowClosing method here
    }

    @Test
    public void testWindowClosed() {
        // Test windowClosed method here
    }

    @Test
    public void testWindowIconified() {
        // Test windowIconified method here
    }

    @Test
    public void testWindowDeiconified() {
        // Test windowDeiconified method here
    }

    @Test
    public void testWindowActivated() {
        // Test windowActivated method here
    }

    @Test
    public void testWindowDeactivated() {
        // Test windowDeactivated method here
    }

    @Test
    public void testBuild() {
        // Test build method here
    }

    @Test
    public void testExit() {
        // Test exit method here
    }

    @Test
    public void testInit() {
        // Test init method here
    }

    @Test
    public void testLoadPropertyFile() {
        // Test loadPropertyFile method here
    }

    @Test
    public void testLoadProperties() {
        // Test loadProperties method here
    }

    @Test
    public void testSavePropertyFile() {
        // Test savePropertyFile method here
    }

    @Test
    public void testGetProperty() {
        // Test getProperty method here
    }

    @Test
    public void testShowTime() {
        // Test showTime method here
    }
}