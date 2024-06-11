import static org.junit.Assert.*;
import org.junit.Test;

public class CaptureTest {
    
    // Test case for the default constructor
    @Test
    public void testDefaultConstructor() {
        Capture capture = new Capture();
        assertNotNull(capture);
        assertEquals("Capture Error - ", capture.m_BaseErr);
        assertEquals("dvgrab --autosplit --size 0 --format raw --opendml --buffers 200 dv/dv_file-", capture.m_dvgrab);
    }
    
    // Test case for the parameterized constructor
    @Test
    public void testParameterizedConstructor() {
        int minutes = 10;
        int seconds = 30;
        GUI gui = new GUI();
        
        Capture capture = new Capture(minutes, seconds, gui);
        
        assertNotNull(capture);
        assertEquals("Capture Error - ", capture.m_BaseErr);
        assertEquals("dvgrab --autosplit --size 0 --format raw --opendml --buffers 200 dv/dv_file-", capture.m_dvgrab);
        assertEquals(minutes, capture.m_Minutes);
        assertEquals(seconds, capture.m_Seconds);
        assertEquals(gui, capture.m_GUI);
    }
    
    // Test case for the init() method when m_GUI.menuChkThread.isSelected() returns true
    @Test
    public void testInitWithMenuChkThreadSelected() {
        GUI gui = new GUI();
        gui.menuChkThread.setSelected(true);
        Capture capture = new Capture();
        capture.m_GUI = gui;
        
        boolean result = capture.init();
        
        assertFalse(result);
        assertTrue(capture.m_Thread.isAlive());
    }
    
    // Test case for the init() method when m_GUI.menuChkThread.isSelected() returns false
    @Test
    public void testInitWithoutMenuChkThreadSelected() {
        GUI gui = new GUI();
        gui.menuChkThread.setSelected(false);
        Capture capture = new Capture();
        capture.m_GUI = gui;
        
        boolean result = capture.init();
        
        assertTrue(result);
        assertFalse(capture.m_Thread.isAlive());
    }
    
    // Test case for the run() method
    @Test
    public void testRun() {
        Capture capture = new Capture();
        capture.DV_Capture();
    }
    
    // Test case for the DV_Capture() method with existing output directory
    @Test
    public void testDV_CaptureWithExistingOutputDirectory() {
        GUI gui = new GUI();
        Capture capture = new Capture();
        capture.m_GUI = gui;
        capture.m_GUI.strOutputDir = "output-dir";
        
        capture.DV_Capture();
        
        // Test that appropriate error messages are displayed
        assertTrue(capture.m_Error);
        assertEquals("Please choose a different name for your\ntitle or move the directory to another location.", gui.getMessage());
        assertFalse(gui.blnBegin);
    }
    
    // Test case for the DV_Capture() method with existing file with same output directory name
    @Test
    public void testDV_CaptureWithExistingFileWithSameOutputDirectoryName() {
        GUI gui = new GUI();
        Capture capture = new Capture();
        capture.m_GUI = gui;
        capture.m_GUI.strOutputDir = "output-dir";
        File file = new File(capture.m_GUI.strOutputDir);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        capture.DV_Capture();
        
        // Test that appropriate error messages are displayed
        assertTrue(capture.m_Error);
        assertEquals("Please choose a different name for your\ntitle or move the file to another location.", gui.getMessage());
        assertFalse(gui.blnBegin);
    }
    
    // Test case for the DV_Capture() method with non-existing output directory
    @Test
    public void testDV_CaptureWithNonExistingOutputDirectory() {
        GUI gui = new GUI();
        Capture capture = new Capture();
        capture.m_GUI = gui;
        capture.m_GUI.strOutputDir = "output-dir";
        
        capture.DV_Capture();
        
        File directory = new File(capture.m_GUI.strOutputDir);
        
        // Test that the required directories are created
        assertTrue(directory.exists());
        assertTrue(new File(capture.m_GUI.strOutputDir + "/DVD").exists());
        assertTrue(new File(capture.m_GUI.strOutputDir + "/log").exists());
        assertTrue(new File(capture.m_GUI.strOutputDir + "/dv").exists());
    }
    
    // Test case for the DV_Capture() method with dvgrab command not found error
    @Test
    public void testDV_CaptureWithDvgrabCommandNotFoundError() {
        GUI gui = new GUI();
        Capture capture = new Capture();
        capture.m_GUI = gui;
        capture.m_GUI.strOutputDir = "output-dir";
        capture.m_dvgrab = "dvgrab_cmd_not_found";
        
        capture.DV_Capture();
        
        // Test that appropriate error messages are displayed
        assertTrue(capture.m_Error);
        assertEquals("Could not locate dvgrab in your path.\nPlease install all necessary dependencies\nand rerun dvd-homevideo.", gui.getMessage());
    }
    
    // Test case for the DV_Capture() method with no camera exists error
    @Test
    public void testDV_CaptureWithNoCameraExistsError() {
        GUI gui = new GUI();
        Capture capture = new Capture();
        capture.m_GUI = gui;
        capture.m_GUI.strOutputDir = "output-dir";
        capture.m_dvgrab = "dvgrab_no_camera_exists";
        
        capture.DV_Capture();
        
        // Test that appropriate error messages are displayed
        assertTrue(capture.m_Error);
        assertEquals("It appears that your camcorder is not connected to your computer!", gui.getMessage());
    }
    
    // Test case for the DV_Capture() method with IO Error
    @Test
    public void testDV_CaptureWithIOError() {
        GUI gui = new GUI();
        Capture capture = new Capture();
        capture.m_GUI = gui;
        capture.m_GUI.strOutputDir = "output-dir";
        
        // Set invalid command to cause IO Error
        capture.m_dvgrab = "invalid_command";
        
        capture.DV_Capture();
        
        // Test that appropriate error messages are displayed
        assertTrue(capture.m_Error);
        assertTrue(gui.getMessage().contains("IO Error"));
    }
    
    // Test case for the DV_Capture() method with NullPointerException
    @Test
    public void testDV_CaptureWithNullPointerException() {
        GUI gui = new GUI();
        Capture capture = new Capture();
        capture.m_GUI = gui;
        capture.m_GUI.strOutputDir = "output-dir";
        capture.m_dvgrab = null;
        
        capture.DV_Capture();
        
        // Test that appropriate error messages are displayed
        assertTrue(capture.m_Error);
        assertTrue(gui.getMessage().contains("Error executing Runtime.getRuntime().exec()"));
    }
    
    // Test case for the DV_Capture() method with IllegalArgumentException
    @Test
    public void testDV_CaptureWithIllegalArgumentException() {
        GUI gui = new GUI();
        Capture capture = new Capture();
        capture.m_GUI = gui;
        capture.m_GUI.strOutputDir = "output-dir";
        capture.m_dvgrab = "dvgrab --illegal-arg";
        
        capture.DV_Capture();
        
        // Test that appropriate error messages are displayed
        assertTrue(capture.m_Error);
        assertTrue(gui.getMessage().contains("Illegal argument sent to Runtime.getRuntime().exec()"));
    }
    
    // Test case for the DV_Capture() method with unknown error
    @Test
    public void testDV_CaptureWithUnknownError() {
        GUI gui = new GUI();
        Capture capture = new Capture();
        capture.m_GUI = gui;
        capture.m_GUI.strOutputDir = "output-dir";
        capture.m_dvgrab = "dvgrab --unknown-error";
        
        capture.DV_Capture();
        
        // Test that appropriate error messages are displayed
        assertTrue(capture.m_Error);
        assertTrue(gui.getMessage().contains("Unknown Error occurred"));
    }
}