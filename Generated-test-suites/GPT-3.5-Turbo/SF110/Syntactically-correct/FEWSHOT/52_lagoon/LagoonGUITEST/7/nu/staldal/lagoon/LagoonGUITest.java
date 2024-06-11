package nu.staldal.lagoon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LagoonGUITest {

    private LagoonGUI lagoonGUI;
    private ByteArrayOutputStream outContent;
    private ByteArrayOutputStream errContent;
    
    @Before
    public void setUp() {
        lagoonGUI = new LagoonGUI();
        outContent = new ByteArrayOutputStream();
        errContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }
    
    @After
    public void cleanUp() {
        System.setOut(null);
        System.setErr(null);
    }
    
    @Test
    public void testWindowOpened() {
        lagoonGUI.windowOpened(new WindowEvent(lagoonGUI, WindowEvent.WINDOW_OPENED));
    }
    
    @Test
    public void testWindowClosing() {
        lagoonGUI.windowClosing(new WindowEvent(lagoonGUI, WindowEvent.WINDOW_CLOSING));
    }
    
    @Test
    public void testWindowClosed() {
        lagoonGUI.windowClosed(new WindowEvent(lagoonGUI, WindowEvent.WINDOW_CLOSED));
    }
    
    @Test
    public void testWindowIconified() {
        lagoonGUI.windowIconified(new WindowEvent(lagoonGUI, WindowEvent.WINDOW_ICONIFIED));
    }
    
    @Test
    public void testWindowDeiconified() {
        lagoonGUI.windowDeiconified(new WindowEvent(lagoonGUI, WindowEvent.WINDOW_DEICONIFIED));
    }
    
    @Test
    public void testWindowActivated() {
        lagoonGUI.windowActivated(new WindowEvent(lagoonGUI, WindowEvent.WINDOW_ACTIVATED));
    }
    
    @Test
    public void testWindowDeactivated() {
        lagoonGUI.windowDeactivated(new WindowEvent(lagoonGUI, WindowEvent.WINDOW_DEACTIVATED));
    }
    
    @Test
    public void testBuild() {
        lagoonGUI.build(false);
        assertTrue(outContent.toString().contains("--- Start build ---"));
    }
    
    @Test
    public void testExit() {
        lagoonGUI.exit();
        assertTrue(outContent.toString().contains("Error destroying LagoonProcessor"));
    }
    
    @Test
    public void testInit() {
        lagoonGUI.init();
        assertTrue(outContent.toString().contains("Error destroying LagoonProcessor"));
    }
    
    @Test
    public void testLoadPropertyFile() {
        lagoonGUI.loadPropertyFile();
        assertTrue(outContent.toString().contains("Error reading property file"));
    }
    
    @Test
    public void testLoadProperties() {
        lagoonGUI.loadProperties(null);
        assertTrue(outContent.toString().contains("Error reading property file"));
    }
    
    @Test
    public void testSavePropertyFile() {
        lagoonGUI.savePropertyFile();
        assertTrue(outContent.toString().contains("Error writing property file"));
    }
    
    @Test
    public void testGetProperty() {
        try {
            LagoonGUI.getProperty(null, "");
        } catch (Exception e) {
            assertEquals("nu.staldal.lagoon.LagoonException: Property  not specified", e.toString());
        }
    }
    
    @Test
    public void testShowTime() {
        assertEquals("in 100 ms", LagoonGUI.showTime(100));
        assertEquals("in 10 s", LagoonGUI.showTime(10000));
    }
}