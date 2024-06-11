package nu.staldal.lagoon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LagoonGUITest {
    @Test
    public void testBuild() {
        LagoonGUI gui = new LagoonGUI();
        gui.init();
        gui.build(false);
        assertFalse(gui.loadButton.isEnabled());
        assertFalse(gui.saveButton.isEnabled());
        assertFalse(gui.buildButton.isEnabled());
        assertFalse(gui.forceButton.isEnabled());
        assertFalse(gui.sitemapFile.isEnabled());
        assertFalse(gui.sourceDir.isEnabled());
        assertFalse(gui.targetURL.isEnabled());
        assertEquals("Ready", gui.statusLabel.getText());
        assertTrue(gui.loadButton.isEnabled());
        assertTrue(gui.saveButton.isEnabled());
        assertTrue(gui.buildButton.isEnabled());
        assertTrue(gui.forceButton.isEnabled());
        assertTrue(gui.sitemapFile.isEnabled());
        assertTrue(gui.sourceDir.isEnabled());
        assertTrue(gui.targetURL.isEnabled());
    }

    @Test
    public void testExit() {
        LagoonGUI gui = new LagoonGUI();
        gui.exit();
        assertNull(gui.processor);
    }

    @Test
    public void testLoadPropertyFile() {
        LagoonGUI gui = new LagoonGUI();
        gui.loadPropertyFile();
        //Assert something
    }

    @Test
    public void testSavePropertyFile() {
        LagoonGUI gui = new LagoonGUI();
        gui.savePropertyFile();
        //Assert something
    }

    @Test
    public void testGetProperty() {
        Properties prop = new Properties();
        prop.setProperty("testProperty", "testValue");
        String value = LagoonGUI.getProperty(prop, "testProperty");
        assertEquals("testValue", value);
    }

    @Test(expected = LagoonException.class)
    public void testGetPropertyMissingProperty() {
        Properties prop = new Properties();
        LagoonGUI.getProperty(prop, "missingProperty");
    }

    @Test
    public void testShowTime() {
        String time = LagoonGUI.showTime(1000);
        assertEquals("in 1 s", time);
    }
}