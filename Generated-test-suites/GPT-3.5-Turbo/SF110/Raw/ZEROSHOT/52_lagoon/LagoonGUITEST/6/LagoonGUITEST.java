package nu.staldal.lagoon;

import org.junit.*;
import static org.junit.Assert.*;

public class LagoonGUITest {
    
    @Test
    public void testBuild() {
        LagoonGUI gui = new LagoonGUI();
        gui.build(false);
        // add assertion here
    }
    
    @Test
    public void testExit() {
        LagoonGUI gui = new LagoonGUI();
        gui.exit();
        // add assertion here
    }
    
    @Test
    public void testInit() {
        LagoonGUI gui = new LagoonGUI();
        gui.init();
        // add assertion here
    }
    
    @Test
    public void testLoadPropertyFile() {
        LagoonGUI gui = new LagoonGUI();
        gui.loadPropertyFile();
        // add assertion here
    }
    
    @Test
    public void testLoadProperties() {
        LagoonGUI gui = new LagoonGUI();
        File propertyFile = new File("path/to/property/file");
        gui.loadProperties(propertyFile);
        // add assertion here
    }
    
    @Test
    public void testSavePropertyFile() {
        LagoonGUI gui = new LagoonGUI();
        gui.savePropertyFile();
        // add assertion here
    }
    
    @Test
    public void testGetProperty() {
        Properties prop = new Properties();
        String name = "testProperty";
        prop.setProperty(name, "testValue");
        String value = LagoonGUI.getProperty(prop, name);
        assertEquals("testValue", value);
    }
    
    @Test
    public void testShowTime() {
        long ms = 5000;
        String time = LagoonGUI.showTime(ms);
        assertEquals("in 5 s", time);
    }
}