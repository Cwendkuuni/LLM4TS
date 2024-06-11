import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;

public class CaptureTest {
    
    @Test
    public void testConstructor1() {
        Capture capture = new Capture();
        assertEquals("Capture Error - ", capture.m_BaseErr);
        assertEquals("dvgrab --autosplit --size 0 --format raw --opendml --buffers 200 dv/dv_file-", capture.m_dvgrab);
    }
    
    @Test
    public void testConstructor2() {
        GUI gui = new GUI();
        Capture capture = new Capture(10, 30, gui);
        assertEquals("Capture Error - ", capture.m_BaseErr);
        assertEquals("dvgrab --autosplit --size 0 --format raw --opendml --buffers 200 dv/dv_file-", capture.m_dvgrab);
        assertEquals(10, capture.m_Minutes);
        assertEquals(30, capture.m_Seconds);
        assertEquals(gui, capture.m_GUI);
    }
    
    @Test
    public void testInitWithThread() {
        GUI gui = new GUI();
        gui.menuChkThread.setSelected(true);
        Capture capture = new Capture(10, 30, gui);
        assertFalse(capture.init());
        assertNotNull(capture.m_Thread);
        assertTrue(capture.m_Thread.isAlive());
    }
    
    @Test
    public void testInitWithoutThread() {
        GUI gui = new GUI();
        Capture capture = new Capture(10, 30, gui);
        assertFalse(capture.init());
        assertNotNull(capture.m_Thread);
        assertFalse(capture.m_Thread.isAlive());
    }
    
    @Test
    public void testRun() {
        GUI gui = new GUI();
        Capture capture = new Capture(10, 30, gui);
        capture.run();
        // No assertions, method only calls DV_Capture()
    }
    
    @Test
    public void testDV_Capture_ExistingOutputDir() {
        GUI gui = new GUI();
        Capture capture = new Capture(10, 30, gui);
        capture.m_GUI.strOutputDir = "existingDir";
        File existingDir = new File("existingDir");
        assertTrue(existingDir.mkdir());
        assertEquals(0, capture.m_GUI.MessageBoxCount);
        
        capture.DV_Capture();
        
        assertFalse(capture.m_GUI.blnBegin);
        assertTrue(capture.m_Error);
        assertEquals(1, capture.m_GUI.MessageBoxCount);
        assertTrue(capture.m_Thread.isAlive());
        assertEquals(1, capture.m_GUI.txtAreaOutputAppendCount);
    }
    
    @Test
    public void testDV_Capture_ExistingOutputDirFile() {
        GUI gui = new GUI();
        Capture capture = new Capture(10, 30, gui);
        capture.m_GUI.strOutputDir = "existingDir";
        File existingDir = new File("existingDir");
        DocumentCreateFile(existingDir, "file");
        assertEquals(0, capture.m_GUI.MessageBoxCount);
        
        capture.DV_Capture();
        
        assertFalse(capture.m_GUI.blnBegin);
        assertTrue(capture.m_Thread.isAlive());
        assertEquals(1, capture.m_GUI.MessageBoxCount);
        assertEquals(1, capture.m_GUI.txtAreaOutputAppendCount);
    }
    
    @Test
    public void testDV_Capture_NewOutputDir() {
        GUI gui = new GUI();
        Capture capture = new Capture(10, 30, gui);
        capture.m_GUI.strOutputDir = "newDir";
        assertEquals(0, capture.m_GUI.MessageBoxCount);
        
        capture.DV_Capture();
        
        assertEquals(1, capture.m_GUI.MessageBoxCount);
        assertFalse(capture.m_GUI.blnBegin);
        assertTrue(capture.m_Thread.isAlive());
        assertTrue(new File("newDir").exists());
        assertTrue(new File("newDir/DVD").exists());
        assertTrue(new File("newDir/log").exists());
        assertTrue(new File("newDir/dv").exists());
        assertEquals(1, capture.m_GUI.txtAreaOutputAppendCount);
        assertEquals(1, capture.m_GUI.lblCaptureEnabledCount);
        assertEquals(1, capture.m_GUI.prgCaptureEnabledCount);
        assertEquals(1, capture.m_GUI.lblCaptureProgEnabledCount);
    }
    
    @Test
    public void testDV_Capture_IOError() {
        GUI gui = new GUI();
        Capture capture = new Capture(10, 30, gui);
        capture.m_GUI.strOutputDir = "newDir";
        DocumentCreateFile(new File("newDir"), "DVD");
        assertEquals(0, capture.m_GUI.MessageBoxCount);
        
        capture.DV_Capture();
        
        assertFalse(capture.m_GUI.blnBegin);
        assertTrue(capture.m_Thread.isAlive());
        assertEquals(1, capture.m_GUI.MessageBoxCount);
        assertTrue(capture.m_Error);
        assertEquals(1, capture.m_GUI.SaveStackTracePrintTraceCount);
        assertEquals(1, capture.m_GUI.txtAreaOutputAppendCount);
    }
    
    @Test
    public void testDV_Capture_CommandNotFound() {
        GUI gui = new GUI();
        Capture capture = new Capture(10, 30, gui);
        capture.m_GUI.strOutputDir = "newDir";
        capture.m_dvgrab = "/bin/sh: dvgrab: command not found";
        assertEquals(0, capture.m_GUI.MessageBoxCount);
        
        capture.DV_Capture();
        
        assertFalse(capture.m_GUI.blnBegin);
        assertTrue(capture.m_Thread.isAlive());
        assertEquals(1, capture.m_GUI.MessageBoxCount);
        assertTrue(capture.m_Error);
        assertEquals(1, capture.m_GUI.txtAreaOutputAppendCount);
    }
    
    @Test
    public void testDV_Capture_NoCamera() {
        GUI gui = new GUI();
        Capture capture = new Capture(10, 30, gui);
        capture.m_GUI.strOutputDir = "newDir";
        capture.m_GUI.MessageBox("Error: no camera exists");
        assertEquals(0, capture.m_GUI.MessageBoxCount);
        
        capture.DV_Capture();
        
        assertFalse(capture.m_GUI.blnBegin);
        assertTrue(capture.m_Thread.isAlive());
        assertEquals(1, capture.m_GUI.MessageBoxCount);
        assertTrue(capture.m_Error);
        assertEquals(1, capture.m_GUI.SaveStackTracePrintTraceCount);
        assertEquals(1, capture.m_GUI.txtAreaOutputAppendCount);
    }
    
    // Helper method to create file in directory
    private void DocumentCreateFile(File directory, String fileName) {
        File file = new File(directory, fileName);
        try {
            assertTrue(file.createNewFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}