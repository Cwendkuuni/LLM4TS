import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

public class LagoonGUITest {

    private LagoonGUI lagoonGUI;

    @Before
    public void setUp() {
        lagoonGUI = new LagoonGUI();
    }

    @Test
    public void testBuild() {
        lagoonGUI.build(false);
        assertEquals("Ready", lagoonGUI.statusLabel.getText());

        lagoonGUI.build(true);
        assertEquals("Ready", lagoonGUI.statusLabel.getText());
    }

    @Test
    public void testExit() {
        lagoonGUI.exit();
        assertFalse(lagoonGUI.isActive());
    }

    @Test
    public void testInit() {
        lagoonGUI.init();
        assertEquals("Ready", lagoonGUI.statusLabel.getText());
    }

    @Test
    public void testLoadPropertyFile() {
        lagoonGUI.loadPropertyFile();
        assertEquals("Initializing...", lagoonGUI.statusLabel.getText());
    }

    @Test
    public void testLoadProperties() {
        lagoonGUI.loadProperties(new File("test.properties"));
        assertEquals("testValue", lagoonGUI.sitemapFile.getValue());
        assertEquals(".", lagoonGUI.sourceDir.getValue());
        assertEquals(".", lagoonGUI.targetURL.getValue());
        assertNull(lagoonGUI.password);
    }

    @Test
    public void testSavePropertyFile() {
        lagoonGUI.savePropertyFile();
        assertEquals("Properties saved", lagoonGUI.statusLabel.getText());
    }
}