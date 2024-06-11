import org.junit.*;

public class CaptureTest {
    
    @Test
    public void testCaptureConstructor1() {
        Capture capture = new Capture();
        Assert.assertEquals("", capture.m_BaseErr);
        Assert.assertEquals("dvgrab --autosplit --size 0 --format raw --opendml --buffers 200 dv/dv_file-", capture.m_dvgrab);
        Assert.assertEquals(0, capture.m_Minutes);
        Assert.assertEquals(0, capture.m_Seconds);
        Assert.assertNull(capture.m_GUI);
    }
    
    @Test
    public void testCaptureConstructor2() {
        GUI gui = new GUI();
        Capture capture = new Capture(10, 30, gui);
        Assert.assertEquals("", capture.m_BaseErr);
        Assert.assertEquals("dvgrab --autosplit --size 0 --format raw --opendml --buffers 200 dv/dv_file-", capture.m_dvgrab);
        Assert.assertEquals(10, capture.m_Minutes);
        Assert.assertEquals(30, capture.m_Seconds);
        Assert.assertEquals(gui, capture.m_GUI);
    }
    
    @Test
    public void testInitWithMenuChkThreadSelected() {
        GUI gui = new GUI();
        gui.menuChkThread.setSelected(true);
        Capture capture = new Capture(10, 30, gui);
        Assert.assertFalse(capture.init());
    }
    
    @Test
    public void testInitWithoutMenuChkThreadSelectedWithError() {
        GUI gui = new GUI();
        Capture capture = new Capture(10, 30, gui);
        capture.m_Error = true;
        Assert.assertTrue(capture.init());
    }
    
    @Test
    public void testInitWithoutMenuChkThreadSelectedWithoutError() {
        GUI gui = new GUI();
        Capture capture = new Capture(10, 30, gui);
        Assert.assertFalse(capture.init());
    }
    
    @Test
    public void testRun() {
        Capture capture = new Capture();
        capture.run();
        // We cannot test the result of run() as it is a void method
    }
    
    @Test
    public void testDV_CaptureWithExistingOutputDir() {
        GUI gui = new GUI();
        Capture capture = new Capture(10, 30, gui);
        capture.m_GUI.strOutputDir = "existing_dir";
        File existingDir = new File(capture.m_GUI.strOutputDir);
        existingDir.mkdir();
        capture.DV_Capture();
        Assert.assertTrue(capture.m_GUI.MessageBoxCalled("The Output Directory existing_dir already exists!\nWould you like to delete it?"));
        Assert.assertTrue(existingDir.exists());
        existingDir.delete();
    }
    
    @Test
    public void testDV_CaptureWithExistingOutputDirAndDeleteOption() {
        GUI gui = new GUI();
        Capture capture = new Capture(10, 30, gui);
        capture.m_GUI.strOutputDir = "existing_dir";
        File existingDir = new File(capture.m_GUI.strOutputDir);
        existingDir.mkdir();
        capture.m_GUI.response = 0;
        capture.DV_Capture();
        Assert.assertFalse(existingDir.exists());
    }
    
    @Test
    public void testDV_CaptureWithExistingOutputDirAndCancelOption() {
        GUI gui = new GUI();
        Capture capture = new Capture(10, 30, gui);
        capture.m_GUI.strOutputDir = "existing_dir";
        File existingDir = new File(capture.m_GUI.strOutputDir);
        existingDir.mkdir();
        capture.m_GUI.response = 1;
        capture.DV_Capture();
        Assert.assertTrue(capture.m_GUI.MessageBoxCalled("Please choose a different name for your\ntitle or move the directory to another location."));
        Assert.assertFalse(capture.m_GUI.blnBegin);
        Assert.assertFalse(capture.m_Error);
        Assert.assertTrue(existingDir.exists());
        existingDir.delete();
    }
    
    @Test
    public void testDV_CaptureWithExistingOutputFile() {
        GUI gui = new GUI();
        Capture capture = new Capture(10, 30, gui);
        capture.m_GUI.strOutputDir = "existing_file";
        File existingFile = new File(capture.m_GUI.strOutputDir);
        try {
            existingFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        capture.DV_Capture();
        Assert.assertTrue(capture.m_GUI.MessageBoxCalled("There exists a file with the Output directory name\nat this location!  Would you like to delete it?"));
        Assert.assertTrue(existingFile.exists());
        existingFile.delete();
    }
    
    @Test
    public void testDV_CaptureWithExistingOutputFileAndDeleteOption() {
        GUI gui = new GUI();
        Capture capture = new Capture(10, 30, gui);
        capture.m_GUI.strOutputDir = "existing_file";
        File existingFile = new File(capture.m_GUI.strOutputDir);
        try {
            existingFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        capture.m_GUI.response = 0;
        capture.DV_Capture();
        Assert.assertFalse(existingFile.exists());
    }
    
    @Test
    public void testDV_CaptureWithExistingOutputFileAndCancelOption() {
        GUI gui = new GUI();
        Capture capture = new Capture(10, 30, gui);
        capture.m_GUI.strOutputDir = "existing_file";
        File existingFile = new File(capture.m_GUI.strOutputDir);
        try {
            existingFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        capture.m_GUI.response = 1;
        capture.DV_Capture();
        Assert.assertTrue(capture.m_GUI.MessageBoxCalled("Please choose a different name for your\ntitle or move the file to another location."));
        Assert.assertFalse(capture.m_GUI.blnBegin);
        Assert.assertFalse(capture.m_Error);
        Assert.assertTrue(existingFile.exists());
        existingFile.delete();
    }
    
    @Test
    public void testDV_CaptureWithValidOutputDir() {
        GUI gui = new GUI();
        Capture capture = new Capture(10, 30, gui);
        capture.m_GUI.strOutputDir = "new_dir";
        File newDir = new File(capture.m_GUI.strOutputDir);
        capture.DV_Capture();
        Assert.assertTrue(newDir.exists());
        Assert.assertTrue(newDir.isDirectory());
        newDir.delete();
    }
    
    // More tests for DV_Capture() method...
    
}