import org.junit.*;
import java.awt.*;
import java.io.*;
import static org.junit.Assert.*;

public class LagoonGUITest {

    @Test
    public void testBuild() {
        LagoonGUI appFrame = new LagoonGUI();
        appFrame.show();
        
        // Initialize the input values
        appFrame.sitemapFile.setValue("testSitemap.xml");
        appFrame.sourceDir.setValue(".");
        appFrame.targetURL.setValue("http://www.example.com");
        
        // Call the build method
        appFrame.build(false);
        
        // Assert that the build was successful
        assertEquals("Ready", appFrame.statusLabel.getText());
        
        // Clean up the created files
        File sitemapFile = new File("testSitemap.xml");
        if (sitemapFile.exists()) {
            sitemapFile.delete();
        }
    }

    @Test
    public void testExit() {
        LagoonGUI appFrame = new LagoonGUI();
        appFrame.show();
        
        // Call the exit method
        appFrame.exit();
    }

    @Test
    public void testInit() {
        LagoonGUI appFrame = new LagoonGUI();
        appFrame.show();
        
        // Initialize the input values
        appFrame.sitemapFile.setValue("testSitemap.xml");
        appFrame.sourceDir.setValue(".");
        appFrame.targetURL.setValue("http://www.example.com");
        
        // Call the init method
        appFrame.init();
        
        // Assert that the processor is not null
        assertNotNull(appFrame.processor);
    }

    @Test
    public void testLoadProperties() {
        LagoonGUI appFrame = new LagoonGUI();
        appFrame.show();
        
        // Create a test property file
        File propertyFile = new File("test.properties");
        try {
            PrintWriter writer = new PrintWriter(propertyFile);
            writer.println("sitemapFile=testSitemap.xml");
            writer.println("sourceDir=.");
            writer.println("targetURL=http://www.example.com");
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        // Call the loadProperties method
        appFrame.loadProperties(propertyFile);
        
        // Assert that the values are loaded correctly
        assertEquals("testSitemap.xml", appFrame.sitemapFile.getValue());
        assertEquals(".", appFrame.sourceDir.getValue());
        assertEquals("http://www.example.com", appFrame.targetURL.getValue());
        
        // Clean up the created files
        if (propertyFile.exists()) {
            propertyFile.delete();
        }
    }

    @Test
    public void testSavePropertyFile() {
        LagoonGUI appFrame = new LagoonGUI();
        appFrame.show();
        
        // Initialize the input values
        appFrame.sitemapFile.setValue("testSitemap.xml");
        appFrame.sourceDir.setValue(".");
        appFrame.targetURL.setValue("http://www.example.com");
        
        // Call the savePropertyFile method
        appFrame.savePropertyFile();
        
        // Check if the property file is created
        File propertyFile = new File("test.properties");
        assertTrue(propertyFile.exists());
        
        // Read the property file and check the values
        try {
            FileInputStream fis = new FileInputStream(propertyFile);
            Properties prop = new Properties();
            prop.load(fis);
            fis.close();
            
            assertEquals("testSitemap.xml", prop.getProperty("sitemapFile"));
            assertEquals(".", prop.getProperty("sourceDir"));
            assertEquals("http://www.example.com", prop.getProperty("targetURL"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Clean up the created files
        if (propertyFile.exists()) {
            propertyFile.delete();
        }
    }

    @Test
    public void testGetProperty() {
        Properties prop = new Properties();
        prop.setProperty("testProp", "testValue");
        
        try {
            // Call the getProperty method with an existing property
            String value = LagoonGUI.getProperty(prop, "testProp");
            
            // Assert that the correct value is returned
            assertEquals("testValue", value);
            
            // Call the getProperty method with a non-existing property
            LagoonGUI.getProperty(prop, "nonExistingProp");
            
            // If getProperty method does not throw an exception, the test fails
            fail("Expected LagoonException was not thrown");
        } catch (LagoonException e) {
            // Expected LagoonException was thrown, the test passes
        }
    }

    @Test
    public void testShowTime() {
        // Test with time less than 10 seconds
        long time1 = 5000L;
        
        // Call the showTime method
        String result1 = LagoonGUI.showTime(time1);
        
        // Assert that the correct time string is returned
        assertEquals("in 5000 ms", result1);
        
        // Test with time more than 10 seconds
        long time2 = 12000L;
        
        // Call the showTime method
        String result2 = LagoonGUI.showTime(time2);
        
        // Assert that the correct time string is returned
        assertEquals("in 12 s", result2);
    }
}