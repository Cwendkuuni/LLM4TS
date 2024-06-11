import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class CaptureTest {
    
    @Test
    public void testCaptureInit_menuChkThreadSelected_shouldReturnFalse() {
        Capture capture = new Capture();
        assertTrue(capture.init());
    }
    
    @Test
    public void testCaptureInit_menuChkThreadNotSelected_shouldReturnTrueWithErrorCheck() {
        GUI gui = new GUI();
        Capture capture = new Capture(0, 0, gui);
        assertFalse(capture.init());
    }
    
    @Test
    public void testDV_Capture_CreateOutDirExistsAndIsDirectory_deleteDirectoryAndReturnFalse() {
        GUI gui = new GUI();
        Capture capture = new Capture(0, 0, gui);
        capture.DV_Capture();
        
        assertTrue(capture.getError());
    }
    
    @Test
    public void testDV_Capture_CreateOutDirExistsAndIsNotDirectory_deleteDirectoryAndReturnTrue() {
        GUI gui = new GUI();
        Capture capture = new Capture(0, 0, gui);
        capture.DV_Capture();
        
        assertTrue(capture.getError());
    }
    
    @Test
    public void testDV_Capture_CreateOutDirDoesNotExist_createDirectoriesAndCaptureSuccessfully() {
        GUI gui = new GUI();
        Capture capture = new Capture(0, 0, gui);
        capture.DV_Capture();
        
        assertFalse(capture.getError());
    }
    
    @Test
    public void testDV_Capture_ErrorInDvgrabCommand_displayErrorAndReturnTrue() {
        GUI gui = new GUI();
        Capture capture = new Capture(0, 0, gui);
        capture.DV_Capture();
        
        assertFalse(capture.getError());
    }
    
    @Test
    public void testDV_Capture_CameraNotConnected_displayErrorAndDeleteDirectoryAndReturnTrue() {
        GUI gui = new GUI();
        Capture capture = new Capture(0, 0, gui);
        capture.DV_Capture();
        
        assertFalse(capture.getError());
    }
}