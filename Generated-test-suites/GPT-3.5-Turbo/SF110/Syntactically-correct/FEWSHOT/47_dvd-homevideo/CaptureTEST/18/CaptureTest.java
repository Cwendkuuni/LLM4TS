
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CaptureTest {
    private Capture capture;
    private GUI gui;
    
    @Before
    public void setUp() {
        capture = new Capture();
        gui = new GUI();
        gui.menuChkThread.setSelected(true);
        capture.m_GUI = gui;
    }
    
    @Test
    public void testInit_withThreadSelected() {
        capture.init();
        assertNotNull(capture.m_Thread);
        assertTrue(capture.m_Thread.isAlive());
        assertFalse(capture.m_Error);
    }
    
    @Test
    public void testInit_withoutThreadSelected() {
        gui.menuChkThread.setSelected(false);
        capture.init();
        assertFalse(capture.m_Error);
    }
    
    @Test
    public void testRun() {
        capture.run();
        assertFalse(capture.m_Error);
    }
    
    @Test
    public void testDV_Capture() throws IOException, InterruptedException {
        capture.m_GUI.strOutputDir = "test_directory";
        File createOutDir = new File(capture.m_GUI.strOutputDir);
        
        // Test when directory exists
        createOutDir.mkdirs();
        capture.DV_Capture();
        assertFalse(capture.m_Error);
        assertTrue(capture.m_GUI.blnBegin);
        createOutDir.delete();
        
        // Test when directory is a file
        createOutDir.createNewFile();
        capture.DV_Capture();
        assertFalse(capture.m_Error);
        assertFalse(capture.m_GUI.blnBegin);
        createOutDir.delete();
        
        // Test successful execution of dvgrab_cmd
        createOutDir.mkdir();
        capture.DV_Capture();
        assertFalse(capture.m_Error);
        Thread.sleep(5000);
        assertTrue(new File(capture.m_GUI.strOutputDir + "/DVD").exists());
        assertTrue(new File(capture.m_GUI.strOutputDir + "/log").exists());
        assertTrue(new File(capture.m_GUI.strOutputDir + "/dv").exists());
        createOutDir.delete();
        
        // Test when dvgrab_cmd throws IOException
        createOutDir.mkdir();
        capture.m_dvgrab = "invalid_command";
        capture.DV_Capture();
        assertTrue(capture.m_Error);
        assertFalse(new File(capture.m_GUI.strOutputDir + "/DVD").exists());
        assertFalse(new File(capture.m_GUI.strOutputDir + "/log").exists());
        assertFalse(new File(capture.m_GUI.strOutputDir + "/dv").exists());
        createOutDir.delete();
        
        // Test when dvgrab_cmd throws InterruptedException
        createOutDir.mkdir();
        FileWriter writer = new FileWriter(capture.m_GUI.strOutputDir + "/log/dvgrab.log");
        writer.write("Error: no camera exists");
        writer.close();
        capture.DV_Capture();
        assertTrue(capture.m_Error);
        assertFalse(new File(capture.m_GUI.strOutputDir + "/DVD").exists());
        assertFalse(new File(capture.m_GUI.strOutputDir + "/log").exists());
        assertFalse(new File(capture.m_GUI.strOutputDir + "/dv").exists());
        createOutDir.delete();
    }
}
