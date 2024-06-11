import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import java.awt.*;

public class LagoonGUITest {

    @Test
    public void testGUIInitialization() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        assertNotNull(lagoonGUI);
        assertEquals("Lagoon", lagoonGUI.getTitle());
    }
    
    @Test
    public void testWindowClosing() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        WindowEvent windowEvent = new WindowEvent(lagoonGUI, WindowEvent.WINDOW_CLOSING);
        lagoonGUI.windowClosing(windowEvent);
        assertFalse(lagoonGUI.isActive());
    }
    
    @Test
    public void testBuild() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.build(false);
        // TODO: Add assertions for expected behavior during build
    }
    
    @Test
    public void testExit() {
      LagoonGUI lagoonGUI = new LagoonGUI();
      lagoonGUI.exit();
      assertFalse(lagoonGUI.isActive());
    }
    
    @Test
    public void testInit() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.init();
        // TODO: Add assertions for expected behavior during initialization
    }
    
    @Test
    public void testLoadPropertyFile() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.loadPropertyFile();
        // TODO: Add assertions for expected behavior during property file loading
    }
    
    @Test
    public void testSavePropertyFile() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.savePropertyFile();
        // TODO: Add assertions for expected behavior during property file saving
    }
    
    @Test
    public void testGetProperty() {
        Properties prop = new Properties();
        prop.setProperty("sitemapFile", "test.xml");
        prop.setProperty("sourceDir", "src");
        
        try {
            String sitemapFile = LagoonGUI.getProperty(prop, "sitemapFile");
            assertEquals("test.xml", sitemapFile);
            
            String sourceDir = LagoonGUI.getProperty(prop, "sourceDir");
            assertEquals("src", sourceDir);
        } catch (LagoonException e) {
            // Handle LagoonException if thrown
        }
    }
    
    @Test
    public void testShowTime() {
        String timeString = LagoonGUI.showTime(5000);
        assertEquals("in 5000 ms", timeString);
        
        timeString = LagoonGUI.showTime(15000);
        assertEquals("in 15 s", timeString);
    }
}

