package nu.staldal.lagoon;

import org.junit.*;
import static org.junit.Assert.*;

public class LagoonGUITest {

    @Test
    public void testBuild() {
        LagoonGUI gui = new LagoonGUI();
        gui.build(false);
        // Add assertions as needed
    }

    @Test
    public void testExit() {
        LagoonGUI gui = new LagoonGUI();
        gui.exit();
        // Add assertions as needed
    }

    @Test
    public void testInit() {
        LagoonGUI gui = new LagoonGUI();
        gui.init();
        // Add assertions as needed
    }

    @Test
    public void testLoadPropertyFile() {
        LagoonGUI gui = new LagoonGUI();
        gui.loadPropertyFile();
        // Add assertions as needed
    }

    @Test
    public void testLoadProperties() {
        LagoonGUI gui = new LagoonGUI();
        gui.loadProperties(null);
        // Add assertions as needed
    }

    @Test
    public void testSavePropertyFile() {
        LagoonGUI gui = new LagoonGUI();
        gui.savePropertyFile();
        // Add assertions as needed
    }

    @Test
    public void testGetProperty() throws LagoonException {
        LagoonGUI gui = new LagoonGUI();
        String property = LagoonGUI.getProperty(null, null);
        // Add assertions as needed
    }

    @Test
    public void testShowTime() {
        long ms = 1000L;
        String time = LagoonGUI.showTime(ms);
        // Add assertions as needed
    }
}