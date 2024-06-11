
package nu.staldal.lagoon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

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
        lagoonGUI.loadProperties(new File("test.properties"));
        // Add assertions here
    }
    
    @Test
    public void testSavePropertyFile() {
        lagoonGUI.savePropertyFile();
        // Add assertions here
    }
    
    @Test
    public void testGetProperty() {
        Properties prop = new Properties();
        prop.setProperty("testProperty", "testValue");
        String value = LagoonGUI.getProperty(prop, "testProperty");
        assertEquals("testValue", value);
    }
    
    @Test(expected = LagoonException.class)
    public void testGetProperty_NonExistingProperty() {
        Properties prop = new Properties();
        LagoonGUI.getProperty(prop, "nonExistingProperty");
    }
    
    @Test
    public void testShowTime_LessThan10Seconds() {
        String time = LagoonGUI.showTime(5000L);
        assertEquals("in 5000 ms", time);
    }
    
    @Test
    public void testShowTime_MoreThan10Seconds() {
        String time = LagoonGUI.showTime(15000L);
        assertEquals("in 15 s", time);
    }
}
