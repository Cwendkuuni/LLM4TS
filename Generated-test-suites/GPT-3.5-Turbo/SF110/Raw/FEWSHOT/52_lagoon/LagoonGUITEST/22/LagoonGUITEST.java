package nu.staldal.lagoon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

public class LagoonGUITest {

    private LagoonGUI lagoonGui;

    @Before
    public void setUp() {
        lagoonGui = new LagoonGUI();
    }

    @Test
    public void testBuild() {
        lagoonGui.build(false);
        assertEquals("Building...", lagoonGui.getStatusLabel().getText());
    }

    @Test
    public void testExit() {
        lagoonGui.exit();
        assertFalse(lagoonGui.isActive());
    }

    @Test
    public void testInit() {
        lagoonGui.init();
        assertTrue(lagoonGui.getProcessor() == null);
    }

    @Test
    public void testLoadPropertyFile() {
        File propertyFile = new File("test.properties");
        lagoonGui.loadProperties(propertyFile);
        assertEquals("test.sitemap", lagoonGui.getSitemapFile().getValue());
        assertEquals(".", lagoonGui.getSourceDir().getValue());
        assertEquals(".", lagoonGui.getTargetURL().getValue());
    }

    @Test
    public void testSavePropertyFile() {
        File propertyFile = new File("test.properties");
        try {
            propertyFile.createNewFile();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        lagoonGui.setSitemapFile(new InputComponent(null, "Sitemap file", "test.sitemap", true));
        lagoonGui.setSourceDir(new InputComponent(null, "Source directory", ".", true));
        lagoonGui.setTargetURL(new InputComponent(null, "Target URL", ".", true));
        lagoonGui.setPassword("password");
        lagoonGui.savePropertyFile();
        try {
            FileInputStream fis = new FileInputStream(propertyFile);
            Properties prop = new Properties();
            prop.load(fis);
            fis.close();
            assertEquals("test.sitemap", prop.getProperty("sitemapFile"));
            assertEquals(".", prop.getProperty("sourceDir"));
            assertEquals(".", prop.getProperty("targetURL"));
            assertEquals("password", prop.getProperty("password"));
            propertyFile.delete();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetProperty() {
        Properties prop = new Properties();
        prop.setProperty("test", "value");
        try {
            String value = lagoonGui.getProperty(prop, "test");
            assertEquals("value", value);
        } catch (LagoonException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = LagoonException.class)
    public void testGetPropertyException() throws LagoonException {
        Properties prop = new Properties();
        try {
            lagoonGui.getProperty(prop, "test");
        } finally {
            // Test passed because exception was thrown
        }
    }

    @Test
    public void testShowTime() {
        String time = lagoonGui.showTime(5000);
        assertEquals("in 5 s", time);
    }

}