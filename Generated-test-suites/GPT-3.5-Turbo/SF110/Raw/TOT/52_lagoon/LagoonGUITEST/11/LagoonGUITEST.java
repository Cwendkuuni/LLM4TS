import org.junit.Before;
import org.junit.Test;
import java.awt.event.WindowEvent;
import nu.staldal.lagoon.LagoonGUI;

public class LagoonGUITest {

    LagoonGUI lagoonGUI;

    @Before
    public void setUp() {
        lagoonGUI = new LagoonGUI();
    }

    // Test for windowOpened method
    @Test
    public void testWindowOpened() {
        lagoonGUI.windowOpened(new WindowEvent(lagoonGUI, WindowEvent.WINDOW_OPENED));
    }

    // Test for windowClosing method
    @Test
    public void testWindowClosing() {
        lagoonGUI.windowClosing(new WindowEvent(lagoonGUI, WindowEvent.WINDOW_CLOSING));
    }

    // Test for windowClosed method
    @Test
    public void testWindowClosed() {
        lagoonGUI.windowClosed(new WindowEvent(lagoonGUI, WindowEvent.WINDOW_CLOSED));
    }

    // Test for windowIconified method
    @Test
    public void testWindowIconified() {
        lagoonGUI.windowIconified(new WindowEvent(lagoonGUI, WindowEvent.WINDOW_ICONIFIED));
    }

    // Test for windowDeiconified method
    @Test
    public void testWindowDeiconified() {
        lagoonGUI.windowDeiconified(new WindowEvent(lagoonGUI, WindowEvent.WINDOW_DEICONIFIED));
    }

    // Test for windowActivated method
    @Test
    public void testWindowActivated() {
        lagoonGUI.windowActivated(new WindowEvent(lagoonGUI, WindowEvent.WINDOW_ACTIVATED));
    }

    // Test for windowDeactivated method
    @Test
    public void testWindowDeactivated() {
        lagoonGUI.windowDeactivated(new WindowEvent(lagoonGUI, WindowEvent.WINDOW_DEACTIVATED));
    }

    // Test for build method with force = false
    @Test
    public void testBuildFalse() {
        lagoonGUI.build(false);
    }

    // Test for build method with force = true
    @Test
    public void testBuildTrue() {
        lagoonGUI.build(true);
    }

    // Test for exit method
    @Test
    public void testExit() {
        lagoonGUI.exit();
    }

    // Test for init method
    @Test
    public void testInit() {
        lagoonGUI.init();
    }

    // Test for loadPropertyFile method
    @Test
    public void testLoadPropertyFile() {
        lagoonGUI.loadPropertyFile();
    }

    // Test for loadProperties method
    @Test
    public void testLoadProperties() {
        lagoonGUI.loadProperties(new File("test.properties"));
    }

    // Test for savePropertyFile method
    @Test
    public void testSavePropertyFile() {
        lagoonGUI.savePropertyFile();
    }

    // Test for getProperty method
    @Test
    public void testGetProperty() {
        Properties prop = new Properties();
        prop.setProperty("testKey", "testValue");
        try {
            LagoonGUI.getProperty(prop, "testKey");
        } catch (LagoonException e) {
            fail("LagoonException thrown");
        }
    }

    // Test for showTime method
    @Test
    public void testShowTime() {
        long ms = 5000L;
        assertEquals("in 5 s", LagoonGUI.showTime(ms));
    }

}