import org.junit.Before;
import org.junit.Test;

import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LagoonGUITest {

    private LagoonGUI lagoonGUI;

    @Before
    public void setUp() {
        lagoonGUI = new LagoonGUI();
    }

    @Test
    public void testBuild() {
        lagoonGUI.build(false);
        // Add assertions here
    }

    @Test
    public void testExit() {
        lagoonGUI.exit();
        // Add assertions here
    }

    @Test
    public void testInit() {
        lagoonGUI.init();
        // Add assertions here
    }

    @Test
    public void testLoadPropertyFile() {
        lagoonGUI.loadPropertyFile();
        // Add assertions here
    }

    @Test
    public void testLoadProperties() {
        File propertyFile = new File("test.properties");
        lagoonGUI.loadProperties(propertyFile);
        // Add assertions here
    }

    @Test
    public void testSavePropertyFile() {
        lagoonGUI.savePropertyFile();
        // Add assertions here
    }

    @Test
    public void testGetProperty() {
        // Add assertions here
    }

    @Test
    public void testShowTime() {
        long ms = 5000;
        String expected = "in 5 s";
        String result = LagoonGUI.showTime(ms);
        assertEquals(expected, result);
    }

    @Test
    public void testWindowOpened() {
        WindowEvent event = new WindowEvent(lagoonGUI, WindowEvent.WINDOW_OPENED);
        lagoonGUI.windowOpened(event);
        // Add assertions here
    }

    @Test
    public void testWindowClosing() {
        WindowEvent event = new WindowEvent(lagoonGUI, WindowEvent.WINDOW_CLOSING);
        lagoonGUI.windowClosing(event);
        // Add assertions here
    }

    @Test
    public void testWindowClosed() {
        WindowEvent event = new WindowEvent(lagoonGUI, WindowEvent.WINDOW_CLOSED);
        lagoonGUI.windowClosed(event);
        // Add assertions here
    }

    @Test
    public void testWindowIconified() {
        WindowEvent event = new WindowEvent(lagoonGUI, WindowEvent.WINDOW_ICONIFIED);
        lagoonGUI.windowIconified(event);
        // Add assertions here
    }

    @Test
    public void testWindowDeiconified() {
        WindowEvent event = new WindowEvent(lagoonGUI, WindowEvent.WINDOW_DEICONIFIED);
        lagoonGUI.windowDeiconified(event);
        // Add assertions here
    }

    @Test
    public void testWindowActivated() {
        WindowEvent event = new WindowEvent(lagoonGUI, WindowEvent.WINDOW_ACTIVATED);
        lagoonGUI.windowActivated(event);
        // Add assertions here
    }

    @Test
    public void testWindowDeactivated() {
        WindowEvent event = new WindowEvent(lagoonGUI, WindowEvent.WINDOW_DEACTIVATED);
        lagoonGUI.windowDeactivated(event);
        // Add assertions here
    }
}