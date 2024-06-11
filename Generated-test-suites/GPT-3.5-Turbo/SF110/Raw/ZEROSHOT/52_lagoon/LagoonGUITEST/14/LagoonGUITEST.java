package nu.staldal.lagoon;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LagoonGUITest {
    
    private LagoonGUI lagoonGUI;
    
    @Before
    public void setUp() {
        lagoonGUI = new LagoonGUI();
    }
    
    @After
    public void tearDown() {
        lagoonGUI = null;
    }
    
    @Test
    public void testBuild() {
        // Test build method with force false
        lagoonGUI.build(false);
        // Assert statements for the expected results
        
    }
    
    @Test
    public void testExit() {
        // Test exit method
        lagoonGUI.exit();
        // Assert statements for the expected results
        
    }
    
    @Test
    public void testInit() {
        // Test init method
        lagoonGUI.init();
        // Assert statements for the expected results
        
    }
    
    @Test
    public void testLoadPropertyFile() {
        // Test loadPropertyFile method
        lagoonGUI.loadPropertyFile();
        // Assert statements for the expected results
        
    }
    
    @Test
    public void testLoadProperties() {
        // Test loadProperties method
        lagoonGUI.loadProperties(null);
        // Assert statements for the expected results
        
    }
    
    @Test
    public void testSavePropertyFile() {
        // Test savePropertyFile method
        lagoonGUI.savePropertyFile();
        // Assert statements for the expected results
        
    }
    
    @Test
    public void testGetProperty() {
        // Test getProperty method
        String name = "";
        Properties prop = new Properties();
        try {
            LagoonGUI.getProperty(prop, name);
        } catch (LagoonException e) {
            // Assert statements for the expected results
            
        }
    }
    
    @Test
    public void testShowTime() {
        // Test showTime method
        long ms = 0;
        String result = LagoonGUI.showTime(ms);
        // Assert statements for the expected results
        
    }
}