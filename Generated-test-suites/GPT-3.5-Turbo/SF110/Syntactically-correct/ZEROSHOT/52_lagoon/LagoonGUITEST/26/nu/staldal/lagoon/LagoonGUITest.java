import org.junit.*;
import static org.junit.Assert.*;

public class LagoonGUITest {

    @Test
    public void testWindowOpened() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.windowOpened(null);
    }

    @Test
    public void testWindowClosing() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.windowClosing(null);
    }

    @Test
    public void testWindowClosed() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.windowClosed(null);
    }

    @Test
    public void testWindowIconified() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.windowIconified(null);
    }

    @Test
    public void testWindowDeiconified() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.windowDeiconified(null);
    }

    @Test
    public void testWindowActivated() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.windowActivated(null);
    }

    @Test
    public void testWindowDeactivated() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.windowDeactivated(null);
    }

    @Test
    public void testBuild() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.build(false);
    }

    @Test
    public void testExit() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.exit();
    }

    @Test
    public void testInit() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.init();
    }

    @Test
    public void testLoadPropertyFile() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.loadPropertyFile();
    }

    @Test
    public void testLoadProperties() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.loadProperties(null);
    }

    @Test
    public void testSavePropertyFile() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.savePropertyFile();
    }

    @Test
    public void testGetProperty() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        try {
            lagoonGUI.getProperty(null, "name");
            fail("Expected LagoonException");
        } catch (LagoonException e) {
            // expected behavior
        }
    }

    @Test
    public void testShowTime() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        String result = lagoonGUI.showTime(5000L);
        assertEquals("in 5000 ms", result);

        result = lagoonGUI.showTime(15000L);
        assertEquals("in 15 s", result);
    }
    
}