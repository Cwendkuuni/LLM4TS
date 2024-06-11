import org.junit.Assert;
import org.junit.Test; 

public class LagoonGUITest {

    @Test
    public void testBuild() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.build(false);
        Assert.assertTrue(lagoonGUI.loadButton.isEnabled());
        Assert.assertTrue(lagoonGUI.saveButton.isEnabled());
        Assert.assertTrue(lagoonGUI.forceButton.isEnabled());
        Assert.assertTrue(lagoonGUI.sitemapFile.isEnabled());
        Assert.assertTrue(lagoonGUI.sourceDir.isEnabled());
        Assert.assertTrue(lagoonGUI.targetURL.isEnabled());
        Assert.assertEquals("Ready", lagoonGUI.statusLabel.getText());
    }
    
    @Test
    public void testExit() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.exit();
        // Check if the application exits without any errors
    }
    
    @Test
    public void testInit() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.init();
        // No specific assertions, just check if the method executes without any errors
    }
    
    @Test
    public void testLoadPropertyFile() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.loadPropertyFile();
        // No specific assertions, just check if the method executes without any errors
    }
    
    @Test
    public void testLoadProperties() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.loadProperties(new File("test.properties"));
        Assert.assertEquals("value1", lagoonGUI.sitemapFile.getValue());
        Assert.assertEquals("value2", lagoonGUI.sourceDir.getValue());
        Assert.assertEquals("value3", lagoonGUI.targetURL.getValue());
        Assert.assertNull(lagoonGUI.password);
    }
    
    @Test
    public void testSavePropertyFile() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.sitemapFile.setValue("value1");
        lagoonGUI.sourceDir.setValue("value2");
        lagoonGUI.targetURL.setValue("value3");
        lagoonGUI.savePropertyFile();
        // No specific assertions, just check if the method executes without any errors
    }
    
    @Test
    public void testGetProperty() {
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");
        properties.setProperty("property3", "value3");

        String property1 = getProperty(properties, "property1");
        String property2 = getProperty(properties, "property2");
        String property3 = getProperty(properties, "property3");
      
        Assert.assertEquals("value1", property1);
        Assert.assertEquals("value2", property2);
        Assert.assertEquals("value3", property3);
    }
    
    @Test
    public void testShowTime() {
        long ms1 = 5000L;
        long ms2 = 15000L;
      
        String time1 = showTime(ms1);
        String time2 = showTime(ms2);
      
        Assert.assertEquals("in 5000 ms", time1);
        Assert.assertEquals("in 15 s", time2);
    }
}