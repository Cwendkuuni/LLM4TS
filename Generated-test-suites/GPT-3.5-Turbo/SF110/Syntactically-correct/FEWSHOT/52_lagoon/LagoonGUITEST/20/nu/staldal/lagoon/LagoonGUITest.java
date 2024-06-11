package nu.staldal.lagoon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class LagoonGUITest {
    @Test
    public void testBuild() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.build(false);
        // Add assertions here
    }

    @Test
    public void testExit() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.exit();
        // Add assertions here
    }

    @Test
    public void testInit() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.init();
        // Add assertions here
    }

    @Test
    public void testLoadPropertyFile() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.loadPropertyFile();
        // Add assertions here
    }

    @Test
    public void testLoadProperties() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.loadProperties(null);
        // Add assertions here
    }

    @Test
    public void testSavePropertyFile() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.savePropertyFile();
        // Add assertions here
    }

    @Test
    public void testGetProperty() {
        Properties prop = new Properties();
        prop.setProperty("name", "value");
        String result = LagoonGUI.getProperty(prop, "name");
        assertEquals("value", result);
        // Add more assertions here
    }

    @Test
    public void testShowTime() {
        long ms = 5000L;
        String result = LagoonGUI.showTime(ms);
        assertEquals("in 5 s", result);
        // Add more assertions here
    }
}