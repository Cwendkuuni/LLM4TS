package nu.staldal.lagoon;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.Assert.*;

public class LagoonGUITest {

    LagoonGUI lagoonGUI;

    @Before
    public void setUp() {
        lagoonGUI = new LagoonGUI();
    }

    @After
    public void tearDown() {
        lagoonGUI = null;
    }
    
    
    // Test for the main method
    @Test
    public void testMain() throws Exception {
        final LagoonGUI appFrame = new LagoonGUI();
        appFrame.show();
        String[] args = {"propertyFile"};
        assertDoesNotThrow(() -> {
            appFrame.main(args);
        });
    }

    // Test for the build method
    @Test
    public void testBuild() {
        lagoonGUI.build(false);
        // verify that all the buttons and input fields are disabled during the build process
        assertFalse(lagoonGUI.loadButton.isEnabled());
        assertFalse(lagoonGUI.saveButton.isEnabled());
        assertFalse(lagoonGUI.buildButton.isEnabled());
        assertFalse(lagoonGUI.forceButton.isEnabled());
        assertFalse(lagoonGUI.sitemapFile.isEnabled());
        assertFalse(lagoonGUI.sourceDir.isEnabled());
        assertFalse(lagoonGUI.targetURL.isEnabled());
    }

    // Test for the init method
    @Test
    public void testInit() {
        assertDoesNotThrow(() -> {
            lagoonGUI.init();
        });
    }

    // Test for the exit method
    @Test
    public void testExit() {
        assertDoesNotThrow(() -> {
            lagoonGUI.exit();
        });
    }

    // Test for the loadPropertyFile method
    @Test
    public void testLoadPropertyFile() {
        assertDoesNotThrow(() -> {
            lagoonGUI.loadPropertyFile();
        });
    }

    // Test for the savePropertyFile method
    @Test
    public void testSavePropertyFile() {
        assertDoesNotThrow(() -> {
            lagoonGUI.savePropertyFile();
        });
    }

    // Test for the windowOpened method
    @Test
    public void testWindowOpened() {
        lagoonGUI.windowOpened(null);
    }

    // Test for the windowClosing method
    @Test
    public void testWindowClosing() {
        lagoonGUI.windowClosing(null);
    }

    // Test for the windowClosed method
    @Test
    public void testWindowClosed() {
        lagoonGUI.windowClosed(null);
    }

    // Test for the windowIconified method
    @Test
    public void testWindowIconified() {
        lagoonGUI.windowIconified(null);
    }

    // Test for the windowDeiconified method
    @Test
    public void testWindowDeiconified() {
        lagoonGUI.windowDeiconified(null);
    }

    // Test for the windowActivated method
    @Test
    public void testWindowActivated() {
        lagoonGUI.windowActivated(null);
    }

    // Test for the windowDeactivated method
    @Test
    public void testWindowDeactivated() {
        lagoonGUI.windowDeactivated(null);
    }

    // Test for the getProperty method
    @Test
    public void testGetProperty() throws LagoonException {
        // test case with existing property
        Properties prop = new Properties();
        prop.setProperty("sitemapFile", "sitemap.xml");
        String value = LagoonGUI.getProperty(prop, "sitemapFile");
        assertEquals("sitemap.xml", value);

        // test case with non-existing property
        prop = new Properties();
        assertThrows(LagoonException.class, () -> {
            LagoonGUI.getProperty(prop, "nonExistingProperty");
        });
    }

    // Test for the showTime method
    @Test
    public void testShowTime() {
        // test case with value less than 10,000 ms
        long ms = 5000L;
        String time = LagoonGUI.showTime(ms);
        assertEquals("in 5000 ms", time);

        // test case with value greater than or equal to 10,000 ms
        ms = 15000L;
        time = LagoonGUI.showTime(ms);
        assertEquals("in 15 s", time);
    }
}