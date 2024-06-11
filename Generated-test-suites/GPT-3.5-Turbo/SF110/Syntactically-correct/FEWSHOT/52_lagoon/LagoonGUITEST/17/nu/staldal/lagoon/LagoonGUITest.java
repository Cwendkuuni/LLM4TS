
package nu.staldal.lagoon;

import static org.junit.Assert.*;
import org.junit.Test;

public class LagoonGUITest {

    @Test
    public void testBuild() {
        // Setup
        LagoonGUI gui = new LagoonGUI();
        gui.sitemapFile.setValue("testSitemap.xml");
        gui.sourceDir.setValue("testSource");
        gui.targetURL.setValue("testURL");
        
        // Test
        gui.build(false);
        
        // Verify
        assertEquals("Not initialized", gui.statusLabel.getText());
    }
    
    @Test
    public void testExit() {
        // Setup
        LagoonGUI gui = new LagoonGUI();
        
        // Test
        gui.exit();
        
        // Verify
        // No assert needed
    }
    
    @Test
    public void testInit() {
        // Setup
        LagoonGUI gui = new LagoonGUI();
        
        // Test
        gui.init();
        
        // Verify
        // No assert needed
    }
    
    @Test
    public void testLoadProperties() {
        // Setup
        LagoonGUI gui = new LagoonGUI();
        File propertyFile = new File("testProperties.properties");
        
        // Test
        gui.loadProperties(propertyFile);
        
        // Verify
        assertEquals("testSitemap.xml", gui.sitemapFile.getValue());
        assertEquals("testSource", gui.sourceDir.getValue());
        assertEquals("testURL", gui.targetURL.getValue());
    }
    
    @Test
    public void testSavePropertyFile() {
        // Setup
        LagoonGUI gui = new LagoonGUI();
        File propertyFile = new File("testProperties.properties");
        
        // Test
        gui.savePropertyFile();
        
        // Verify
        assertEquals("Properties saved", gui.statusLabel.getText());
        // Check if the property file is created and has the correct contents
        Properties prop = new Properties();
        FileInputStream fis = new FileInputStream(propertyFile);
        prop.load(fis);
        fis.close();
        assertEquals("testSitemap.xml", prop.getProperty("sitemapFile"));
        assertEquals("testSource", prop.getProperty("sourceDir"));
        assertEquals("testURL", prop.getProperty("targetURL"));
        
        // Cleanup
        propertyFile.delete();
    }
    
    @Test
    public void testGetProperty() {
        // Setup
        Properties prop = new Properties();
        prop.setProperty("key", "value");
        
        // Test
        String result = getProperty(prop, "key");
        
        // Verify
        assertEquals("value", result);
    }
    
    @Test(expected = LagoonException.class)
    public void testGetProperty_keyNotFound() {
        // Setup
        Properties prop = new Properties();
        
        // Test
        getProperty(prop, "key");
        
        // Verify
        // LagoonException is expected
    }
    
    @Test
    public void testShowTime_lessThan10Sec() {
        // Test
        String result = showTime(5000);
        
        // Verify
        assertEquals("in 5000 ms", result);
    }
    
    @Test
    public void testShowTime_moreThan10Sec() {
        // Test
        String result = showTime(15000);
        
        // Verify
        assertEquals("in 15 s", result);
    }
    
}
