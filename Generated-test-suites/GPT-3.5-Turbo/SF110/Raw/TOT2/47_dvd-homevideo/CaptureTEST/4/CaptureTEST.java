import org.junit.Test;
import static org.junit.Assert.*;

public class CaptureTest {
    
    @Test
    public void testCaptureConstructor1() {
        Capture capture = new Capture();
        assertNotNull(capture);
    }
    
    @Test
    public void testCaptureConstructor2() {
        GUI gui = new GUI();
        Capture capture = new Capture(10, 30, gui);
        assertNotNull(capture);
        assertEquals(10, capture.m_Minutes);
        assertEquals(30, capture.m_Seconds);
        assertEquals(gui, capture.m_GUI);
    }
    
    @Test
    public void testInitThreadSelected() {
        GUI gui = new GUI();
        Capture capture = new Capture(5, 0, gui);
        assertTrue(capture.init());
    }
    
    @Test
    public void testInitThreadNotSelected() {
        GUI gui = new GUI();
        Capture capture = new Capture(5, 0, gui);
        gui.menuChkThread.setSelected(false);
        assertFalse(capture.init());
    }
    
    @Test
    public void testRun() {
        GUI gui = new GUI();
        Capture capture = new Capture(2, 0, gui);
        capture.run();
    }
    
    @Test
    public void testDV_CaptureExistingDir() {
        GUI gui = new GUI();
        Capture capture = new Capture(2, 0, gui);
        capture.DV_Capture();
    }
    
    @Test
    public void testDV_CaptureExistingFile() {
        GUI gui = new GUI();
        Capture capture = new Capture(2, 0, gui);
        gui.strOutputDir = "path/to/existing/file";
        capture.DV_Capture();
    }
}