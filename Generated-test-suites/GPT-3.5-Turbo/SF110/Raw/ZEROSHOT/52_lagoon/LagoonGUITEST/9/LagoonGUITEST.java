import org.junit.Before;
import org.junit.Test;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.Properties;

import static org.junit.Assert.*;

public class LagoonGUITest {

    private LagoonGUI lagoonGUI;

    @Before
    public void setUp() {
        lagoonGUI = new LagoonGUI();
    }

    @Test
    public void testBuild() {
        lagoonGUI.build(false);
        // Add assertions for expected behavior
    }

    @Test
    public void testInit() {
        lagoonGUI.init();
        // Add assertions for expected behavior
    }

    @Test
    public void testLoadPropertyFile() {
        lagoonGUI.loadPropertyFile();
        // Add assertions for expected behavior
    }

    @Test
    public void testLoadProperties() {
        File propertyFile = new File("test.properties");
        lagoonGUI.loadProperties(propertyFile);
        // Add assertions for expected behavior
    }

    @Test
    public void testSavePropertyFile() {
        lagoonGUI.savePropertyFile();
        // Add assertions for expected behavior
    }

    @Test
    public void testGetProperty() {
        Properties prop = new Properties();
        prop.setProperty("testProperty", "testValue");

        try {
            String value = LagoonGUI.getProperty(prop, "testProperty");
            assertEquals("testValue", value);
        } catch (LagoonException e) {
            fail("Exception should not be thrown");
        }

        try {
            LagoonGUI.getProperty(prop, "nonexistentProperty");
            fail("Exception should be thrown");
        } catch (LagoonException e) {
            assertEquals("Property nonexistentProperty not specified", e.getMessage());
        }
    }

    @Test
    public void testShowTime() {
        String timeShort = LagoonGUI.showTime(5000L);
        assertEquals("in 5000 ms", timeShort);

        String timeLong = LagoonGUI.showTime(15000L);
        assertEquals("in 15 s", timeLong);
    }
}