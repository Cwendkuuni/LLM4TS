package nu.staldal.lagoon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.Label;
import java.awt.Panel;
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
    private LagoonGUI lagoonGUI;

    @Before
    public void setup() {
        lagoonGUI = new LagoonGUI();
    }

    @Test
    public void testWindowOpened() {
        lagoonGUI.windowOpened(null);
    }

    @Test
    public void testWindowClosed() {
        lagoonGUI.windowClosed(null);
    }

    @Test
    public void testWindowIconified() {
        lagoonGUI.windowIconified(null);
    }

    @Test
    public void testWindowDeiconified() {
        lagoonGUI.windowDeiconified(null);
    }

    @Test
    public void testWindowActivated() {
        lagoonGUI.windowActivated(null);
    }

    @Test
    public void testWindowDeactivated() {
        lagoonGUI.windowDeactivated(null);
    }

    @Test
    public void testBuild() {
        lagoonGUI.build(false);

        assertFalse(lagoonGUI.loadButton.isEnabled());
        assertFalse(lagoonGUI.saveButton.isEnabled());
        assertFalse(lagoonGUI.buildButton.isEnabled());
        assertFalse(lagoonGUI.forceButton.isEnabled());
        assertFalse(lagoonGUI.sitemapFile.isEnabled());
        assertFalse(lagoonGUI.sourceDir.isEnabled());
        assertFalse(lagoonGUI.targetURL.isEnabled());

        lagoonGUI.build(true);

        assertFalse(lagoonGUI.loadButton.isEnabled());
        assertFalse(lagoonGUI.saveButton.isEnabled());
        assertFalse(lagoonGUI.buildButton.isEnabled());
        assertFalse(lagoonGUI.forceButton.isEnabled());
        assertFalse(lagoonGUI.sitemapFile.isEnabled());
        assertFalse(lagoonGUI.sourceDir.isEnabled());
        assertFalse(lagoonGUI.targetURL.isEnabled());
    }

    @Test
    public void testExit() {
        lagoonGUI.exit();
    }

    @Test
    public void testInit() {
        lagoonGUI.init();
    }

    @Test
    public void testLoadPropertyFile() {
        lagoonGUI.loadPropertyFile();
    }

    @Test
    public void testSavePropertyFile() {
        lagoonGUI.savePropertyFile();
    }

    @Test
    public void testGetProperty() throws LagoonException {
        Properties prop = new Properties();
        prop.setProperty("sitemapFile", "test.sitemap");
        prop.setProperty("sourceDir", "src");
        prop.setProperty("targetURL", "http://localhost:8080");

        assertEquals("test.sitemap", LagoonGUI.getProperty(prop, "sitemapFile"));
        assertEquals("src", LagoonGUI.getProperty(prop, "sourceDir"));
        assertEquals("http://localhost:8080", LagoonGUI.getProperty(prop, "targetURL"));
    }

    @Test(expected = LagoonException.class)
    public void testGetPropertyNotFound() throws LagoonException {
        Properties prop = new Properties();
        LagoonGUI.getProperty(prop, "nonExistentProperty");
    }

    @Test
    public void testShowTime() {
        assertEquals("in 5000 ms", LagoonGUI.showTime(5000));
        assertEquals("in 10 s", LagoonGUI.showTime(10000));
        assertEquals("in 60 s", LagoonGUI.showTime(60000));
    }
}