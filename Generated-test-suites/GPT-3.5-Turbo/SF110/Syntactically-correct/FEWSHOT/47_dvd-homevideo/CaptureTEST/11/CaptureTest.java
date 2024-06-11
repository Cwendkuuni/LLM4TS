import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.io.FileReader;

public class CaptureTest {
    @Test
    public void testCapture() {
        Capture capture = new Capture();
        String expectedOutputDir = "dv/dv_file-";
        assertEquals(expectedOutputDir, capture.m_dvgrab);
        assertNull(capture.m_GUI);
        assertFalse(capture.m_Error);
        assertNull(capture.m_BaseErr);
        assertEquals(0, capture.m_Minutes);
        assertEquals(0, capture.m_Seconds);
        
        capture = new Capture(5, 30, new GUI());
        expectedOutputDir = "dv/dv_file-";
        assertEquals(expectedOutputDir, capture.m_dvgrab);
        assertNotNull(capture.m_GUI);
        assertFalse(capture.m_Error);
        assertNotNull(capture.m_BaseErr);
        assertEquals(5, capture.m_Minutes);
        assertEquals(30, capture.m_Seconds);
    }
    
    @Test
    public void testInit() {
        Capture capture = new Capture();
        capture.m_GUI = new GUI();
        assertFalse(capture.init());
        capture.m_GUI.menuChkThread.setSelected(true);
        assertTrue(capture.init());
        capture.m_GUI.ErrorCheck("");
        assertFalse(capture.init());
    }
    
    @Test
    public void testRun() {
        Capture capture = new Capture();
        capture.run();
        
        capture = new Capture();
        capture.m_GUI = new GUI();
        capture.run();
    }
    
    @Test
    public void testDV_Capture() {
        Capture capture = new Capture();
        capture.m_GUI = new GUI();
        File outputDir = new File(capture.m_GUI.strOutputDir);
        capture.DV_Capture();
        assertTrue(outputDir.exists());
        assertTrue(outputDir.isDirectory());
        
        capture.m_GUI.MessageBox("0", 2);
        assertFalse(capture.m_GUI.blnBegin);
        capture.DV_Capture();
        
        capture.m_GUI.MessageBox("It appears that your camcorder is not connected to your computer!", 0);
        assertFalse(outputDir.exists());
        file = new File(capture.m_GUI.strOutputDir);
        assertFalse(file.exists());
        
        capture.m_GUI.MessageBox("The Output Directory " + capture.m_GUI.strOutputDir + " already exists!"
            + "\nWould you like to delete it?");
        assertTrue(file.exists());
        capture.DV_Capture();
        assertFalse(file.exists());
        
        capture.m_GUI = new GUI();
        capture.m_GUI.strOutputDir = capture.m_GUI.strOutputDir + "/test";
        File createFile = new File(capture.m_GUI.strOutputDir);
        assertTrue(createFile.exists());
        assertFalse(createFile.isDirectory());
        capture.DV_Capture();
        assertFalse(createFile.exists());
        
        capture.m_GUI = new GUI();
        createFile = new File(capture.m_GUI.strOutputDir + "/DVD");
        CaptureOutFile.mkdir();
        capture.DV_Capture();
        
        createFile = new File(capture.m_GUI.strOutputDir + "/log");
        CaptureOutFile.mkdir();
        capture.DV_Capture();
        
        createFile = new File(capture.m_GUI.strOutputDir + "/dv");
        CaptureOutFile.mkdir();
        capture.DV_Capture();
        
        capture.m_GUI.txtAreaOutput.append("Error: no camera exists\n");
        assertNull(capture.m_GUI.prgCapture);
        assertNull(capture.m_GUI.lblCaptureProg);
        assertNull(capture.m_GUI.lblCapture);
        assertNull(capture.m_GUI.prgCapture);
        assertNull(capture.m_GUI.txtAreaOutput);
        
        capture.m_Minutes = 1;
        capture.m_Seconds = 30;
        capture.DV_Capture();
    }
}