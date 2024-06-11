
import static org.junit.Assert.*;
import org.junit.Test;

public class LagoonGUITest {

    @Test
    public void testMain() {
        try {
            // Test the main method with arguments
            String[] args = {"test.properties"};
            LagoonGUI.main(args);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testBuild() {
        // Test build method
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.build(false);
    }

    @Test
    public void testWindowClosing() {
        // Test window closing method
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.windowClosing(null);
    }

    @Test
    public void testExit() {
        // Test exit method
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.exit();
    }

    @Test
    public void testInit() {
        // Test init method
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.init();
    }

    @Test
    public void testLoadPropertyFile() {
        // Test loadPropertyFile method
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.loadPropertyFile();
    }

    @Test
    public void testSavePropertyFile() {
        // Test savePropertyFile method
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.savePropertyFile();
    }

    @Test
    public void testGetProperty() {
        // Test getProperty method with exception scenario
        try {
            Properties prop = new Properties();
            LagoonGUI.getProperty(prop, "invalidProperty");
            fail("Expected LagoonException not thrown");
        } catch (LagoonException e) {
            // Expected
        }
    }

    @Test
    public void testShowTime() {
        // Test showTime method with ms less than 10000
        long ms = 5000L;
        String result = LagoonGUI.showTime(ms);
        assertEquals("in 5000 ms", result);
        
        // Test showTime method with ms greater than 10000
        ms = 15000L;
        result = LagoonGUI.showTime(ms);
        assertEquals("in 15 s", result);
    }
}
