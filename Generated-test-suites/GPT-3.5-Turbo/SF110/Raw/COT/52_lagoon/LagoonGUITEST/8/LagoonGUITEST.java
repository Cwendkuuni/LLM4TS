
import org.junit.*;
import static org.junit.Assert.*;

public class LagoonGUITest {
    
    @Test
    public void testMain() throws Exception {
        // Test if main method runs without errors
        LagoonGUI.main(new String[] {});
        
        // Test if main method loads properties file correctly
        LagoonGUI.main(new String[] {"test.properties"});
    }
    
    @Test
    public void testWindowOpened() {
        // Test if windowOpened method runs without errors
        LagoonGUI gui = new LagoonGUI();
        gui.windowOpened(null);
    }
    
    @Test
    public void testWindowClosing() {
        // Test if windowClosing method runs without errors
        LagoonGUI gui = new LagoonGUI();
        gui.windowClosing(null);
    }
    
    @Test
    public void testWindowClosed() {
        // Test if windowClosed method runs without errors
        LagoonGUI gui = new LagoonGUI();
        gui.windowClosed(null);
    }
    
    @Test
    public void testWindowIconified() {
        // Test if windowIconified method runs without errors
        LagoonGUI gui = new LagoonGUI();
        gui.windowIconified(null);
    }
    
    @Test
    public void testWindowDeiconified() {
        // Test if windowDeiconified method runs without errors
        LagoonGUI gui = new LagoonGUI();
        gui.windowDeiconified(null);
    }
    
    @Test
    public void testWindowActivated() {
        // Test if windowActivated method runs without errors
        LagoonGUI gui = new LagoonGUI();
        gui.windowActivated(null);
    }
    
    @Test
    public void testWindowDeactivated() {
        // Test if windowDeactivated method runs without errors
        LagoonGUI gui = new LagoonGUI();
        gui.windowDeactivated(null);
    }
    
    @Test
    public void testBuild() {
        // Test if build method runs without errors
        LagoonGUI gui = new LagoonGUI();
        gui.build(false);
        gui.build(true);
    }
    
    @Test
    public void testExit() {
        // Test if exit method runs without errors
        LagoonGUI gui = new LagoonGUI();
        gui.exit();
    }
    
    @Test
    public void testInit() {
        // Test if init method runs without errors
        LagoonGUI gui = new LagoonGUI();
        gui.init();
    }
    
    @Test
    public void testLoadPropertyFile() {
        // Test if loadPropertyFile method runs without errors
        LagoonGUI gui = new LagoonGUI();
        gui.loadPropertyFile();
    }
    
    @Test
    public void testLoadProperties() {
        // Test if loadProperties method runs without errors
        LagoonGUI gui = new LagoonGUI();
        gui.loadProperties(new File("test.properties"));
    }
    
    @Test
    public void testSavePropertyFile() {
        // Test if savePropertyFile method runs without errors
        LagoonGUI gui = new LagoonGUI();
        gui.savePropertyFile();
    }
    
    @Test
    public void testGetProperty() throws LagoonException {
        // Test if getProperty method returns correct value
        Properties prop = new Properties();
        prop.setProperty("test", "value");
        assertEquals("value", LagoonGUI.getProperty(prop, "test"));
        
        // Test if getProperty throws exception for missing property
        try {
            LagoonGUI.getProperty(prop, "missing");
            fail("Expected LagoonException to be thrown");
        } catch (LagoonException e) {
            assertEquals("Property missing not specified", e.getMessage());
        }
    }
    
    @Test
    public void testShowTime() {
        // Test if showTime method returns correct value
        assertEquals("in 500 ms", LagoonGUI.showTime(500));
        assertEquals("in 5 s", LagoonGUI.showTime(5000));
    }
}
