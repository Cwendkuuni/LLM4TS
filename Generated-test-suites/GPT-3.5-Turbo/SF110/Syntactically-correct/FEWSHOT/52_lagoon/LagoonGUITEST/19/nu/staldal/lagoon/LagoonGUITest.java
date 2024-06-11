
package nu.staldal.lagoon;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LagoonGUITest {
    
    private LagoonGUI lagoonGUI;
    
    @Before
    public void setUp() {
        lagoonGUI = new LagoonGUI();
    }
    
    @Test
    public void testBuild() {
        lagoonGUI.build(false);
        // Add assertions here
    }
    
    @Test
    public void testExit() {
        lagoonGUI.exit();
        // Add assertions here
    }
    
    @Test
    public void testInit() {
        lagoonGUI.init();
        // Add assertions here
    }
    
    @Test
    public void testLoadPropertyFile() {
        lagoonGUI.loadPropertyFile();
        // Add assertions here
    }
    
    @Test
    public void testLoadProperties() {
        lagoonGUI.loadProperties(null);
        // Add assertions here
    }
    
    @Test
    public void testSavePropertyFile() {
        lagoonGUI.savePropertyFile();
        // Add assertions here
    }
    
    @Test
    public void testGetProperty() {
        String result = LagoonGUI.getProperty(null, "name");
        assertNull(result);
        // Add more assertions here
    }
    
    @Test
    public void testShowTime() {
        String result = LagoonGUI.showTime(5000);
        assertEquals("in 5 s", result);
        // Add more assertions here
    }
}
