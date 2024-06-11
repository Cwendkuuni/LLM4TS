import static org.junit.Assert.*;
import org.junit.Test;

public class LagoonGUITest {

    @Test
    public void testBuild() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        // Test build method with force set to false
        lagoonGUI.build(false);
        // Assert expected results
        
    }

    @Test
    public void testExit() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        // Test exit method
        lagoonGUI.exit();
        // Assert expected results
        
    }

    @Test
    public void testInit() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        // Test init method
        lagoonGUI.init();
        // Assert expected results
        
    }

    @Test
    public void testLoadPropertyFile() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        // Test loadPropertyFile method
        lagoonGUI.loadPropertyFile();
        // Assert expected results
        
    }

    @Test
    public void testLoadProperties() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        // Test loadProperties method
        lagoonGUI.loadProperties(null);
        // Assert expected results
        
    }

    @Test
    public void testSavePropertyFile() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        // Test savePropertyFile method 
        lagoonGUI.savePropertyFile();
        // Assert expected results
        
    }

    @Test
    public void testGetProperty() throws LagoonException {
        Properties prop = new Properties();
        // Test getProperty method
        String result = LagoonGUI.getProperty(prop, "testProperty");
        // Assert expected results
        
    }

    @Test
    public void testShowTime() {
        long ms = 10000L;
        // Test showTime method
        String result = LagoonGUI.showTime(ms);
        // Assert expected results
        
    }
}