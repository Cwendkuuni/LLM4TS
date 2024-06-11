import org.junit.Assert;
import org.junit.Test;

public class LagoonGUITest {


    // Test for the main method
    @Test
    public void testMain() throws Exception {
        String[] args = { "test.properties" };
        LagoonGUI.main(args);
    }

    // Test for the LagoonGUI constructor
    @Test
    public void testConstructor() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        Assert.assertNotNull(lagoonGUI);
    }

    // Test for the build method with force = false
    @Test
    public void testBuild() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.build(false);
    }

    // Test for the build method with force = true
    @Test
    public void testBuildForce() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.build(true);
    }

    // Test for the exit method
    @Test
    public void testExit() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.exit();
    }

    // Test for the init method
    @Test
    public void testInit() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.init();
    }

    // Test for the loadPropertyFile method
    @Test
    public void testLoadPropertyFile() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.loadPropertyFile();
    }

    // Test for the loadProperties method
    @Test
    public void testLoadProperties() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        File propertyFile = new File("test.properties");
        lagoonGUI.loadProperties(propertyFile);
    }

    // Test for the savePropertyFile method
    @Test
    public void testSavePropertyFile() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.savePropertyFile();
    }

    // Test for the getProperty method
    @Test
    public void testGetProperty() throws LagoonException {
        Properties prop = new Properties();
        prop.setProperty("sitemapFile", "test.sitemap");
        String value = LagoonGUI.getProperty(prop, "sitemapFile");
        Assert.assertEquals("test.sitemap", value);
    }

    // Test for the showTime method
    @Test
    public void testShowTime() {
        long ms = 5000;
        String time = LagoonGUI.showTime(ms);
        Assert.assertEquals("in 5 s", time);
    }
    

}