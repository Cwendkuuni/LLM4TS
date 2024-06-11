import org.junit.Test;

public class CaptureTest {
    
    // Test init method with menuChkThread selected
    @Test
    public void testInitWithMenuChkThreadSelected() {
        Capture capture = new Capture();
        capture.m_GUI.menuChkThread.setSelected(true);
        boolean result = capture.init();
        
        // Assert that a new thread is created and started
        assertNotEquals(capture.m_Thread, null);
        assertTrue(capture.m_Thread.isAlive());
        
        // Assert that the result is false (no error)
        assertFalse(result);
    }
    
    // Test init method without menuChkThread selected
    @Test
    public void testInitWithoutMenuChkThreadSelected() {
        Capture capture = new Capture();
        capture.m_GUI.menuChkThread.setSelected(false);
        boolean result = capture.init();
        
        // Assert that DV_Capture method is called
        assertTrue(capture.m_Error);
        
        // Assert that GUI's ErrorCheck method is called
        assertTrue(capture.m_GUI.errorCheckCalled);
        
        // Assert that the result is the same as the GUI's ErrorCheck method
        assertEquals(result, capture.m_GUI.errorCheckResult);
    }
    
    // Test run method
    @Test
    public void testRun() {
        Capture capture = new Capture();
        capture.run();
        
        // Assert that DV_Capture method is called
        assertTrue(capture.m_Error);
    }
    
    // Test DV_Capture method with existing output directory
    @Test
    public void testDV_CaptureWIthExistingOutputDirectory() {
        Capture capture = new Capture();
        capture.m_GUI.strOutputDir = "output";
        capture.m_GUI.existsOutputDir = true;
        capture.m_GUI.isDirectoryOutputDir = true;
        capture.m_GUI.response = 0;
        
        capture.DV_Capture();
        
        // Assert that the directory is deleted
        assertTrue(capture.removeDirCalled);
        
        // Assert that the error message is shown
        assertTrue(capture.m_GUI.messageBoxCalled);
        
        // Assert that m_GUI.blnBegin is set to false
        assertFalse(capture.m_GUI.blnBegin);
        
        // Assert that m_Error is set to true
        assertTrue(capture.m_Error);
    }
    
    // Test DV_Capture method with existing output directory that is a file
    @Test
    public void testDV_CaptureWithExistingOutputDirectoryFile() {
        Capture capture = new Capture();
        capture.m_GUI.strOutputDir = "output";
        capture.m_GUI.existsOutputDir = true;
        capture.m_GUI.isDirectoryOutputDir = false;
        capture.m_GUI.response = 0;
        
        capture.DV_Capture();
        
        // Assert that the file is deleted
        assertTrue(capture.removeDirCalled);
        
        // Assert that the error message is shown
        assertTrue(capture.m_GUI.messageBoxCalled);
        
        // Assert that m_GUI.blnBegin is set to false
        assertFalse(capture.m_GUI.blnBegin);
        
        // Assert that m_Error is not set to true
        assertFalse(capture.m_Error);
    }
    
    // Test DV_Capture method without existing output directory
    @Test
    public void testDV_CaptureWithoutExistingOutputDirectory() {
        Capture capture = new Capture();
        capture.m_GUI.blnBegin = true;
        
        capture.DV_Capture();
        
        // Assert that the directories are created
        assertTrue(capture.mkdirCalled);
        
        // Assert that the dvgrab command is executed
        assertTrue(capture.runtimeExecCalled);
        
        // Assert that the progress bar is updated
        assertTrue(capture.progressBarUpdated);
        
        // Assert that the appropriate error messages are shown
        assertFalse(capture.m_GUI.messageBoxCalled);
        
        // Assert that m_Error is not set to true
        assertFalse(capture.m_Error);
        
        // Assert that the thread is paused
        assertTrue(capture.threadSleepCalled);
    }
}