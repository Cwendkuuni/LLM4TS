import org.junit.*;
import static org.junit.Assert.*;

public class CaptureTest {

    @Test
    public void testInit_threadSelected() {
        Capture capture = new Capture();
        capture.m_GUI = new GUI();
        capture.m_GUI.menuChkThread.setSelected(true);
        assertFalse(capture.init());
    }
    
    @Test
    public void testInit_threadNotSelected_noError() {
        Capture capture = new Capture();
        capture.m_GUI = new GUI();
        capture.m_GUI.menuChkThread.setSelected(false);
        capture.init();
        assertFalse(capture.m_Error);
        assertNull(capture.m_Thread);
    }
    
    @Test
    public void testInit_threadNotSelected_withError() {
        Capture capture = new Capture();
        capture.m_GUI = new GUI();
        capture.m_GUI.menuChkThread.setSelected(false);
        capture.m_GUI.strOutputDir = "output/dir";
        capture.m_Error = true;
        assertTrue(capture.init());
    }
    
    @Test
    public void testRun() {
        Capture capture = new Capture();
        capture.DV_Capture();
    }
    
    @Test
    public void testDV_Capture_directoryExists_responseYes_IOError() {
        Capture capture = new Capture();
        capture.m_GUI = new GUI();
        capture.m_GUI.strOutputDir = "output/dir";
        capture.m_GUI.MessageBox("yes");
        capture.DV_Capture();
        assertTrue(capture.m_Error);
    }
    
    @Test
    public void testDV_Capture_directoryExists_responseYes_interrrupted() {
        Capture capture = new Capture();
        capture.m_GUI = new GUI();
        capture.m_GUI.strOutputDir = "output/dir";
        capture.m_GUI.MessageBox("yes");
        capture.DV_Capture();
        assertTrue(capture.m_Error);
    }
    
    @Test
    public void testDV_Capture_directoryExists_responseNo() {
        Capture capture = new Capture();
        capture.m_GUI = new GUI();
        capture.m_GUI.strOutputDir = "output/dir";
        capture.m_GUI.MessageBox("no");
        capture.DV_Capture();
        assertFalse(capture.m_Error);
    }
    
    @Test
    public void testDV_Capture_fileExists_responseYes_IOError() {
        Capture capture = new Capture();
        capture.m_GUI = new GUI();
        capture.m_GUI.strOutputDir = "output/file";
        capture.m_GUI.MessageBox("yes");
        capture.DV_Capture();
        assertTrue(capture.m_Error);
    }
    
    @Test
    public void testDV_Capture_fileExists_responseYes_interrrupted() {
        Capture capture = new Capture();
        capture.m_GUI = new GUI();
        capture.m_GUI.strOutputDir = "output/file";
        capture.m_GUI.MessageBox("yes");
        capture.DV_Capture();
        assertFalse(capture.m_Error);
    }
    
    @Test
    public void testDV_Capture_fileExists_responseNo() {
        Capture capture = new Capture();
        capture.m_GUI = new GUI();
        capture.m_GUI.strOutputDir = "output/file";
        capture.m_GUI.MessageBox("no");
        capture.DV_Capture();
        assertFalse(capture.m_Error);
    }
}