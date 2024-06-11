
package nu.staldal.lagoon;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LagoonGUITest {

    @Test
    public void testBuild() {
        // Initialize LagoonGUI object
        LagoonGUI lagoonGUI = new LagoonGUI();

        // Test when force is false
        lagoonGUI.build(false);
        // Add assertions here

        // Test when force is true
        lagoonGUI.build(true);
        // Add assertions here
    }

    @Test
    public void testExit() {
        // Initialize LagoonGUI object
        LagoonGUI lagoonGUI = new LagoonGUI();

        lagoonGUI.exit();
        // Add assertions here
    }

    @Test
    public void testInit() {
        // Initialize LagoonGUI object
        LagoonGUI lagoonGUI = new LagoonGUI();

        lagoonGUI.init();
        // Add assertions here
    }

    @Test
    public void testLoadPropertyFile() {
        // Initialize LagoonGUI object
        LagoonGUI lagoonGUI = new LagoonGUI();

        lagoonGUI.loadPropertyFile();
        // Add assertions here
    }

    @Test
    public void testLoadProperties() {
        // Initialize LagoonGUI object
        LagoonGUI lagoonGUI = new LagoonGUI();

        // Test when propertyFile is null
        lagoonGUI.loadProperties(null);
        // Add assertions here

        // Test when propertyFile is not null
        lagoonGUI.loadProperties(new File("test.properties"));
        // Add assertions here
    }

    @Test
    public void testSavePropertyFile() {
        // Initialize LagoonGUI object
        LagoonGUI lagoonGUI = new LagoonGUI();

        lagoonGUI.savePropertyFile();
        // Add assertions here
    }

    @Test
    public void testGetProperty() {
        // Test when property name exists in the Properties object
        Properties properties = new Properties();
        properties.setProperty("sitemapFile", "test.sitemap");
        String value = LagoonGUI.getProperty(properties, "sitemapFile");
        assertEquals("test.sitemap", value);

        // Test when property name does not exist in the Properties object
        assertNull(LagoonGUI.getProperty(properties, "nonexistent"));

        // Test when property name exists but value is null
        properties.setProperty("nullProperty", null);
        assertNull(LagoonGUI.getProperty(properties, "nullProperty"));
    }

    @Test
    public void testShowTime() {
        // Test when ms is less than 10000
        String time = LagoonGUI.showTime(5000);
        assertEquals("in 5000 ms", time);

        // Test when ms is equal to 10000
        time = LagoonGUI.showTime(10000);
        assertEquals("in 10 s", time);

        // Test when ms is greater than 10000
        time = LagoonGUI.showTime(15000);
        assertEquals("in 15 s", time);
    }
}
