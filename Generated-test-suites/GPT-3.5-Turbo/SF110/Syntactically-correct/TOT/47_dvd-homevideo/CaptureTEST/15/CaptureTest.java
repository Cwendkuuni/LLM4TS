import org.junit.*;
import static org.junit.Assert.*;

public class CaptureTest {
    
    @Test
    public void testInit_WhenThreadIsSelected_ShouldReturnFalse() {
        Capture capture = new Capture();
        capture.m_GUI = new GUI();
        capture.m_GUI.menuChkThread.setSelected(true);
        boolean result = capture.init();
        assertFalse(result);
    }
    
    @Test
    public void testInit_WhenThreadIsNotSelectedAndErrorCheckReturnsFalse_ShouldReturnFalse() {
        Capture capture = new Capture();
        capture.m_GUI = new GUI();
        capture.m_GUI.menuChkThread.setSelected(false);
        capture.m_Error = false;
        capture.m_GUI.ErrorCheck(capture.m_GUI.strOutputDir + "/log/dvgrab.log");
        boolean result = capture.init();
        assertFalse(result);
    }
    
    @Test
    public void testInit_WhenThreadIsNotSelectedAndErrorCheckReturnsTrue_ShouldReturnTrue() {
        Capture capture = new Capture();
        capture.m_GUI = new GUI();
        capture.m_GUI.menuChkThread.setSelected(false);
        capture.m_Error = true;
        capture.m_GUI.ErrorCheck(capture.m_GUI.strOutputDir + "/log/dvgrab.log");
        boolean result = capture.init();
        assertTrue(result);
    }
    
    @Test
    public void testRun_ShouldCallDV_CaptureMethod() {
        // TODO: Implement
    }
    
    @Test
    public void testDV_Capture_WhenOutputDirectoryExists_ShouldAskToDeleteDirectoryAndDeleteIt() {
        // TODO: Implement
    }
    
    @Test
    public void testDV_Capture_WhenOutputDirectoryExistsAndDeletionIsCancelled_ShouldShowMessageBoxAndSetErrorToTrue() {
        // TODO: Implement
    }
    
    @Test
    public void testDV_Capture_WhenOutputDirectoryDoesNotExistButFileWithSameNameExists_ShouldAskToDeleteFileAndDeleteIt() {
        // TODO: Implement
    }
    
    @Test
    public void testDV_Capture_WhenOutputDirectoryDoesNotExistButFileWithSameNameExistsAndDeletionIsCancelled_ShouldShowMessageBoxAndSetErrorToTrue() {
        // TODO: Implement
    }
    
    @Test
    public void testDV_Capture_WhenOutputDirectoryDoesNotExist_ShouldCreateOutputDirectoryAndSubdirectories() {
        // TODO: Implement
    }
    
    @Test
    public void testDV_Capture_WhenDVGrabCommandNotFound_ShouldShowMessageBoxAndSetErrorToTrue() {
        // TODO: Implement
    }
    
    @Test
    public void testDV_Capture_WhenCamcorderNotConnected_ShouldShowMessageBoxAndSetErrorToTrue() {
        // TODO: Implement
    }
    
    @Test
    public void testDV_Capture_WhenIOExceptionOccurs_ShouldShowMessageBoxAndSetErrorToTrue() {
        // TODO: Implement
    }
    
    @Test
    public void testDV_Capture_WhenNullPointerExceptionOccurs_ShouldShowMessageBoxAndSetErrorToTrue() {
        // TODO: Implement
    }
    
    @Test
    public void testDV_Capture_WhenIllegalArgumentExceptionOccurs_ShouldShowMessageBoxAndSetErrorToTrue() {
        // TODO: Implement
    }
    
    @Test
    public void testDV_Capture_WhenUnknownErrorOccurs_ShouldShowMessageBoxAndSetErrorToTrue() {
        // TODO: Implement
    }
}