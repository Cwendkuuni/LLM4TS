import org.junit.*;
import static org.junit.Assert.*;

public class LagoonGUITest {

    @Test
    public void testWindowOpened() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.windowOpened(null);
        // Add assertions
    }

    @Test
    public void testWindowClosing() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.windowClosing(null);
        // Add assertions
    }

    @Test
    public void testWindowClosed() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.windowClosed(null);
        // Add assertions
    }

    @Test
    public void testWindowIconified() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.windowIconified(null);
        // Add assertions
    }

    @Test
    public void testWindowDeiconified() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.windowDeiconified(null);
        // Add assertions
    }

    @Test
    public void testWindowActivated() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.windowActivated(null);
        // Add assertions
    }

    @Test
    public void testWindowDeactivated() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.windowDeactivated(null);
        // Add assertions
    }

    @Test
    public void testBuild() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.build(true);
        // Add assertions
    }

    @Test
    public void testExit() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.exit();
        // Add assertions
    }

    @Test
    public void testInit() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.init();
        // Add assertions
    }

    @Test
    public void testLoadPropertyFile() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.loadPropertyFile();
        // Add assertions
    }

    @Test
    public void testLoadProperties() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.loadProperties(null);
        // Add assertions
    }

    @Test
    public void testSavePropertyFile() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.savePropertyFile();
        // Add assertions
    }

    @Test
    public void testGetProperty() throws LagoonException {
        Properties prop = new Properties();
        String value = LagoonGUI.getProperty(prop, "sitemapFile");
        // Add assertions
    }

    @Test
    public void testShowTime() {
        long ms = 1000L;
        String time = LagoonGUI.showTime(ms);
        // Add assertions
    }
}