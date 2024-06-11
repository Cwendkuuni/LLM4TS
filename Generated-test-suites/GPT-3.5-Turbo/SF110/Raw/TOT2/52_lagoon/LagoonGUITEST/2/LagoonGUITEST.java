import org.junit.Test;
import nu.staldal.lagoon.LagoonGUI;

import java.io.File;

import static org.junit.Assert.*;

public class LagoonGUITest {

    @Test
    public void testMain() {
        // Test main method
        String[] args = {"test.properties"};
        assertDoesNotThrow(() -> LagoonGUI.main(args));
    }

    @Test
    public void testWindowEvents() {
        // Test window event methods
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.windowOpened(null);
        lagoonGUI.windowClosing(null);
        lagoonGUI.windowClosed(null);
        lagoonGUI.windowIconified(null);
        lagoonGUI.windowDeiconified(null);
        lagoonGUI.windowActivated(null);
        lagoonGUI.windowDeactivated(null);
    }

    @Test
    public void testBuild() {
        // Test build method
        LagoonGUI lagoonGUI = new LagoonGUI();
        assertDoesNotThrow(() -> lagoonGUI.build(false));
    }

    @Test
    public void testExit() {
        // Test exit method
        LagoonGUI lagoonGUI = new LagoonGUI();
        assertDoesNotThrow(() -> lagoonGUI.exit());
    }

    @Test
    public void testInit() {
        // Test init method
        LagoonGUI lagoonGUI = new LagoonGUI();
        assertDoesNotThrow(() -> lagoonGUI.init());
    }

    @Test
    public void testLoadProperties() {
        // Test loadProperties method
        LagoonGUI lagoonGUI = new LagoonGUI();
        assertDoesNotThrow(() -> lagoonGUI.loadProperties(new File("test.properties")));
    }

    @Test
    public void testSavePropertyFile() {
        // Test savePropertyFile method
        LagoonGUI lagoonGUI = new LagoonGUI();
        assertDoesNotThrow(() -> lagoonGUI.savePropertyFile());
    }

    @Test
    public void testGetProperty() {
        // Test getProperty method
        assertThrows(LagoonException.class, () -> LagoonGUI.getProperty(null, "test"));
    }

    @Test
    public void testShowTime() {
        // Test showTime method
        assertEquals("in 5000 ms", LagoonGUI.showTime(5000));
        assertEquals("in 5 s", LagoonGUI.showTime(5000));
    }

}