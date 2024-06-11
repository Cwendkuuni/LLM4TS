package nu.staldal.lagoon;

import org.junit.*;
import java.io.*;

import static org.junit.Assert.*;

public class LagoonGUITest {

    @Test
    public void testBuild() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        boolean force = false;
        lagoonGUI.build(force);

        // Assert statements
        assertEquals("Not initialized", lagoonGUI.statusLabel.getText());
        assertTrue(lagoonGUI.loadButton.isEnabled());
        assertTrue(lagoonGUI.saveButton.isEnabled());
        assertTrue(lagoonGUI.buildButton.isEnabled());
        assertTrue(lagoonGUI.forceButton.isEnabled());
        assertTrue(lagoonGUI.sitemapFile.isEnabled());
        assertTrue(lagoonGUI.sourceDir.isEnabled());
        assertTrue(lagoonGUI.targetURL.isEnabled());
    }

    @Test
    public void testInit() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.init();

        // Assert statements
        assertEquals("Initializing...", lagoonGUI.statusLabel.getText());
        assertNull(lagoonGUI.processor);
    }

    @Test
    public void testLoadPropertyFile() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.loadPropertyFile();

        // Assert statements
        // Add assertions here
    }

    @Test
    public void testLoadProperties() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        File propertyFile = new File("filepath");
        lagoonGUI.loadProperties(propertyFile);

        // Assert statements
        // Add assertions here
    }

    @Test
    public void testSavePropertyFile() {
        LagoonGUI lagoonGUI = new LagoonGUI();
        lagoonGUI.savePropertyFile();

        // Assert statements
        assertEquals("Properties saved", lagoonGUI.statusLabel.getText());
    }

    @Test
    public void testGetPropertyValue() {
        Properties prop = new Properties();
        String name = "test";
        String expectedValue = "value";
        prop.setProperty(name, expectedValue);

        // Call method
        String actualValue = LagoonGUI.getProperty(prop, name);

        // Assert statements
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testShowTime() {
        long ms = 5000;

        // Call method
        String actualTime = LagoonGUI.showTime(ms);

        // Assert statements
        assertEquals("in 5 s", actualTime);
    }
}