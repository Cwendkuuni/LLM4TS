import org.junit.*;
import static org.junit.Assert.*;

public class CaptureTest {
    
    private Capture capture;
    
    @Before
    public void setUp() {
        capture = new Capture(5, 30, new GUI());
    }
    
    @Test
    public void testInitMenuChkThreadSelected() {
        capture.init();
        assertTrue(capture.m_Thread.isAlive());
    }
    
    @Test
    public void testInitMenuChkThreadNotSelected() {
        capture.m_GUI.menuChkThread.setSelected(false);
        assertTrue(capture.init());
    }
    
    @Test
    public void testRun() {
        capture.run();
    }
    
    @Test
    public void testDV_CaptureExistingDirectory() {
        File existingDir = new File("existingDir");
        existingDir.mkdir();
        capture.m_GUI.strOutputDir = "existingDir";
        capture.DV_Capture();
        
        assertFalse(capture.m_Error);
    }
    
    @Test
    public void testDV_CaptureNonExistingDirectory() {
        File nonexistingDir = new File("nonexistingDir");
        capture.m_GUI.strOutputDir = "nonexistingDir";
        capture.DV_Capture();
        
        assertTrue(nonexistingDir.exists());
    }
    
    @Test
    public void testDV_CaptureNoCameraExists() {
        // Simulate no camera connected error scenario
        capture.m_GUI.textBoxOutput = "Error: no camera exists";
        capture.DV_Capture();
        
        assertTrue(capture.m_Error);
    }
    
    @Test(expected = IOException.class)
    public void testDV_CaptureIOException() {
        // Simulate IO error scenario
        capture.m_GUI.blnBegin = true;
        capture.m_GUI.strOutputDir = "invalidDir";
        capture.DV_Capture();
    }
    
    @Test(expected = InterruptedException.class)
    public void testDV_CaptureInterruptedException() {
        // Simulate interrupted exception scenario
        capture.m_GUI.blnBegin = true;
        capture.m_GUI.strOutputDir = System.getProperty("user.home");
        capture.DV_Capture();
    }
    
}