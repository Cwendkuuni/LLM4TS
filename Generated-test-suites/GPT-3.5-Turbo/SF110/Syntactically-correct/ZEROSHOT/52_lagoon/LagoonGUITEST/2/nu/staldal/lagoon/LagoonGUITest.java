import org.junit.Test;
import static org.junit.Assert.*;

public class LagoonGUITest {

    @Test
    public void testBuild() {
        LagoonGUI gui = new LagoonGUI();
        gui.build(false);
        assertFalse(gui.loadButton.isEnabled());
        assertFalse(gui.saveButton.isEnabled());
        assertFalse(gui.buildButton.isEnabled());
        assertFalse(gui.forceButton.isEnabled());
        assertFalse(gui.sitemapFile.isEnabled());
        assertFalse(gui.sourceDir.isEnabled());
        assertFalse(gui.targetURL.isEnabled());
        gui.build(true);
    }

    @Test
    public void testExit() {
        LagoonGUI gui = new LagoonGUI();
        gui.exit();
        assertNull(gui.processor);
    }

    @Test
    public void testInit() {
        LagoonGUI gui = new LagoonGUI();
        gui.init();
        assertEquals("Initializing...", gui.statusLabel.getText());
        assertNull(gui.processor);
    }

    @Test
    public void testLoadPropertyFile() {
        LagoonGUI gui = new LagoonGUI();
        gui.loadPropertyFile();
    }

    @Test
    public void testLoadProperties() {
        LagoonGUI gui = new LagoonGUI();
        File propertyFile = new File("propertyFile.txt");
        gui.loadProperties(propertyFile);
    }

    @Test
    public void testSavePropertyFile() {
        LagoonGUI gui = new LagoonGUI();
        gui.savePropertyFile();
        assertEquals("Properties saved", gui.statusLabel.getText());
    }

    @Test
    public void testGetProperty() {
        Properties prop = new Properties();
        prop.setProperty("sitemapFile", "file1");
        prop.setProperty("sourceDir", "dir1");
        prop.setProperty("targetURL", "url1");

        try {
            String sitemapFile = LagoonGUI.getProperty(prop, "sitemapFile");
            assertEquals("file1", sitemapFile);
            String sourceDir = LagoonGUI.getProperty(prop, "sourceDir");
            assertEquals("dir1", sourceDir);
            String targetURL = LagoonGUI.getProperty(prop, "targetURL");
            assertEquals("url1", targetURL);
        } catch (LagoonException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testShowTime() {
        long ms1 = 5000L;
        long ms2 = 15000L;

        String time1 = LagoonGUI.showTime(ms1);
        assertEquals("in 5000 ms", time1);

        String time2 = LagoonGUI.showTime(ms2);
        assertEquals("in 15 s", time2);
    }
}