
import org.junit.Test;
import static org.junit.Assert.*;

public class CaptureTest {
    
    @Test
    public void testInit_threadSelected() {
        Capture capture = new Capture();
        capture.init();
        assertNotNull(capture.m_Thread);
    }
    
    @Test
    public void testInit_threadNotSelected() {
        Capture capture = new Capture();
        capture.m_GUI = new GUI();
        boolean result = capture.init();
        assertFalse(result);
        assertFalse(capture.m_Error);
    }
    
    @Test
    public void testInit_errorCheckReturnsTrue() {
        Capture capture = new Capture();
        capture.m_GUI = new GUI();
        capture.m_GUI.strOutputDir = "testDir";
        
        boolean result = capture.init();
        
        assertTrue(result);
        assertTrue(capture.m_Error);
    }
    
    @Test
    public void testRun() {
        Capture capture = new Capture();
        capture.run();
    }
    
    @Test
    public void testDV_Capture_outputDirExistsAndIsDirectory_responseEqualsZero() {
        Capture capture = new Capture();
        capture.m_GUI = new GUI();
        capture.m_GUI.menuChkThread = new Menu();
        capture.m_GUI.strOutputDir = "testDir";
        capture.m_GUI.blnBegin = true;
        capture.m_GUI.MessageBox = (message, response) -> 0;
        
        capture.DV_Capture();
        
        assertNotNull(capture.m_Thread);
    }
    
    @Test
    public void testDV_Capture_outputDirExistsAndIsDirectory_responseNotEqualsZero() {
        Capture capture = new Capture();
        capture.m_GUI = new GUI();
        capture.m_GUI.menuChkThread = new Menu();
        capture.m_GUI.strOutputDir = "testDir";
        capture.m_GUI.blnBegin = true;
        capture.m_GUI.MessageBox = (message, response) -> 1;
        
        capture.DV_Capture();
        
        assertNotNull(capture.m_Thread);
        assertFalse(capture.m_Error);
    }
    
    @Test
    public void testDV_Capture_outputDirExistsAndIsNotDirectory_responseEqualsZero() {
        Capture capture = new Capture();
        capture.m_GUI = new GUI();
        capture.m_GUI.menuChkThread = new Menu();
        capture.m_GUI.strOutputDir = "testDir";
        capture.m_GUI.blnBegin = true;
        capture.m_GUI.MessageBox = (message, response) -> 0;
        
        capture.DV_Capture();
        
        assertFalse(capture.m_Error);
    }
    
    @Test
    public void testDV_Capture_outputDirExistsAndIsNotDirectory_responseNotEqualsZero() {
        Capture capture = new Capture();
        capture.m_GUI = new GUI();
        capture.m_GUI.menuChkThread = new Menu();
        capture.m_GUI.strOutputDir = "testDir";
        capture.m_GUI.blnBegin = true;
        capture.m_GUI.MessageBox = (message, response) -> 1;
        
        capture.DV_Capture();
        
        assertFalse(capture.m_Error);
    }
}
