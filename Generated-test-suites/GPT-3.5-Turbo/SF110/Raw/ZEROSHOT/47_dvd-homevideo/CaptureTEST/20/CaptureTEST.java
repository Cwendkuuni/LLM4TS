import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.*;

public class CaptureTest {
    
    private Capture capture;
    
    @Before
    public void setUp() {
        capture = new Capture();
    }
    
    @Test
    public void testInit_withMenuChkThreadSelected() {
        capture.m_GUI = new GUI();
        capture.m_GUI.menuChkThread.setSelected(true);
        assertFalse(capture.init());
        assertNotNull(capture.m_Thread);
    }
    
    @Test
    public void testInit_withoutMenuChkThreadSelected() {
        capture.m_GUI = new GUI();
        capture.m_GUI.menuChkThread.setSelected(false);
        assertTrue(capture.init());
    }
    
    @Test
    public void testRun() {
        capture.DV_Capture();
    }
    
    @Test
    public void testDV_Capture_whenCreateOutDirExistsAndIsDirectory() throws IOException, InterruptedException {
        capture.m_GUI = new GUI();
        File createOutDir = new File(capture.m_GUI.strOutputDir);
        createOutDir.mkdirs();
        
        int response = capture.m_GUI.MessageBox("The Output Directory " + capture.m_GUI.strOutputDir + " already exists!" + "\nWould you like to delete it?");
        assertEquals(0, response);
        
        String rm = "rm -fr " + capture.m_GUI.strOutputDir;
        String[] rm_cmd = { "/bin/sh", "-c", rm };
        Process remove_dir = Runtime.getRuntime().exec(rm_cmd, null);
        remove_dir.waitFor();
    }
    
    @Test
    public void testDV_Capture_whenCreateOutDirExistsButIsNotDirectory() throws IOException, InterruptedException {
        capture.m_GUI = new GUI();
        File createOutDir = new File(capture.m_GUI.strOutputDir);
        createOutDir.createNewFile();
        
        int response = capture.m_GUI.MessageBox("There exists a file with the Output directory name\nat this location!  Would you like to delete it?");
        assertEquals(0, response);
        
        String rm = "rm -fr " + capture.m_GUI.strOutputDir;
        String[] rm_cmd = { "/bin/sh", "-c", rm };
        Process remove_dir = Runtime.getRuntime().exec(rm_cmd, null);
        remove_dir.waitFor();
    }
    
    @Test
    public void testDV_Capture_whenCreateOutDirDoesNotExist() throws IOException, InterruptedException {
        capture.m_GUI = new GUI();
        File createOutDir = new File(capture.m_GUI.strOutputDir);
        
        assertFalse(createOutDir.exists());
        
        createOutDir.mkdir();
        createOutDir = new File(capture.m_GUI.strOutputDir + "/DVD");
        createOutDir.mkdir();
        createOutDir = new File(capture.m_GUI.strOutputDir + "/log");
        createOutDir.mkdir();
        createOutDir = new File(capture.m_GUI.strOutputDir + "/dv");
        createOutDir.mkdir();
        
        String[] dvgrab_cmd = { "/bin/sh", "-c", capture.m_dvgrab };
        Process p = Runtime.getRuntime().exec(dvgrab_cmd, null, new File(capture.m_GUI.strOutputDir));
        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        BufferedReader err_in = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        BufferedWriter out = new BufferedWriter(new FileWriter(capture.m_GUI.strOutputDir + "/log/dvgrab.log"));
        BufferedWriter err_out = new BufferedWriter(new FileWriter(capture.m_GUI.strOutputDir + "/log/dvgrab.err"));
        assertFalse(err_in.ready());
        assertNotNull(in.readLine());
        
        in.close();
        out.close();
        err_in.close();
        err_out.close();
        p.destroy();
    }
}