import org.junit.Test;
import static org.junit.Assert.*;

public class LagoonGUITest {
    
    @Test
    public void testMainWindowTitle() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        assertEquals("Lagoon", lagoonGUI.getTitle());
    }
    
    @Test
    public void testButtonEnabledStatus() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        assertTrue(lagoonGUI.loadButton.isEnabled());
        assertTrue(lagoonGUI.saveButton.isEnabled());
        assertTrue(lagoonGUI.buildButton.isEnabled());
        assertTrue(lagoonGUI.forceButton.isEnabled());
        assertTrue(lagoonGUI.exitButton.isEnabled());
        
        lagoonGUI.loadButton.setEnabled(false);
        assertFalse(lagoonGUI.loadButton.isEnabled());
        lagoonGUI.saveButton.setEnabled(false);
        assertFalse(lagoonGUI.saveButton.isEnabled());
        lagoonGUI.buildButton.setEnabled(false);
        assertFalse(lagoonGUI.buildButton.isEnabled());
        lagoonGUI.forceButton.setEnabled(false);
        assertFalse(lagoonGUI.forceButton.isEnabled());
        lagoonGUI.exitButton.setEnabled(false);
        assertFalse(lagoonGUI.exitButton.isEnabled());
    }
    
    @Test
    public void testInputComponentValues() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        assertEquals("", lagoonGUI.sitemapFile.getValue());
        assertEquals(".", lagoonGUI.sourceDir.getValue());
        assertEquals(".", lagoonGUI.targetURL.getValue());
        
        lagoonGUI.sitemapFile.setValue("sitemap.xml");
        lagoonGUI.sourceDir.setValue("/src");
        lagoonGUI.targetURL.setValue("http://example.com");
        
        assertEquals("sitemap.xml", lagoonGUI.sitemapFile.getValue());
        assertEquals("/src", lagoonGUI.sourceDir.getValue());
        assertEquals("http://example.com", lagoonGUI.targetURL.getValue());
    }
    
    @Test
    public void testProgressAreaEditableStatus() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        assertFalse(lagoonGUI.progressArea.isEditable());
    }
    
    @Test
    public void testStatusLabelText() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        assertEquals("Not initialized", lagoonGUI.statusLabel.getText());
        
        lagoonGUI.build(false);
        assertEquals("Ready", lagoonGUI.statusLabel.getText());
    }
    
    @Test
    public void testInitProcessor() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        assertNull(lagoonGUI.processor);
        
        lagoonGUI.init();
        assertNotNull(lagoonGUI.processor);
    }
    
    @Test
    public void testBuildMethod() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        
        // Test with uninitialized processor
        lagoonGUI.build(false);
        assertEquals("Not initialized", lagoonGUI.statusLabel.getText());
        
        // Test with initialized processor
        lagoonGUI.init();
        lagoonGUI.build(false);
        assertEquals("Ready", lagoonGUI.statusLabel.getText());
        
        // Test with force build
        lagoonGUI.build(true);
        assertEquals("Ready", lagoonGUI.statusLabel.getText());
    }
    
    @Test
    public void testExitMethod() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        
        lagoonGUI.exit();
        // Unable to test System.exit(0);
    }
    
    @Test
    public void testLoadPropertyFileMethod() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        
        // Unable to test FileDialog
    }
    
    @Test
    public void testLoadPropertiesMethod() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        
        // Unable to test FileInputStream and File
    }
    
    @Test
    public void testSavePropertyFileMethod() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        
        // Unable to test FileDialog and FileOutputStream
    }
    
    @Test
    public void testGetPropertyMethod() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        
        Properties prop = new Properties();
        prop.setProperty("sitemapFile", "sitemap.xml");
        prop.setProperty("sourceDir", "/src");
        prop.setProperty("targetURL", "http://example.com");
        
        try {
            assertEquals("sitemap.xml", LagoonGUI.getProperty(prop, "sitemapFile"));
            assertEquals("/src", LagoonGUI.getProperty(prop, "sourceDir"));
            assertEquals("http://example.com", LagoonGUI.getProperty(prop, "targetURL"));
        } catch (LagoonException e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        try {
            LagoonGUI.getProperty(prop, "invalidProperty");
            fail("Expected exception not thrown");
        } catch (LagoonException e) {
            assertEquals("Property invalidProperty not specified", e.getMessage());
        }
    }
    
    @Test
    public void testShowTimeMethod() {
        assertEquals("in 5000 ms", LagoonGUI.showTime(5000));
        assertEquals("in 10 s", LagoonGUI.showTime(10000));
    }
    
}