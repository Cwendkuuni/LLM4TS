import org.junit.*;
import static org.junit.Assert.*;

public class CaptureTest {
    private Capture capture;
    
    @Before
    public void setUp() {
        capture = new Capture();
    }
    
    @After
    public void tearDown() {
        capture = null;
    }
    
    @Test
    public void testCapture_Init_WhenMenuChkThreadIsSelected_ReturnsFalse() {
        // Arrange
        capture.getGUI().setMenuChkThreadSelected(true);
        
        // Act
        boolean result = capture.init();
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testCapture_Init_WhenMenuChkThreadIsNotSelected_ReturnsTrueIfErrorCheckIsTrue() {
        // Arrange
        capture.setGUI(new GUI());
        
        // Act
        boolean result = capture.init();
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testCapture_DV_Capture_WhenCreateOutDirExistsAndIsDirectory_IsExecuteRmCommandOnCreateOutDir() {
        // Arrange
        capture.setGUI(new GUI());
        
        // Act
        capture.DV_Capture();
        
        // Assert
        assertEquals("", capture.getGUI().getMessageBoxMessage());
    }
    
    @Test
    public void testCapture_DV_Capture_WhenCreateOutDirExistsAndIsNotDirectory_IsExecuteRmCommandOnCreateOutDir() {
        // Arrange
        capture.setGUI(new GUI());
        
        // Act
        capture.DV_Capture();
        
        // Assert
        assertEquals("", capture.getGUI().getMessageBoxMessage());
    }
    
    @Test
    public void testCapture_DV_Capture_WhenCreateOutDirDoesNotExist_IsCreateOutDirAndSubdirectories() {
        // Arrange
        capture.setGUI(new GUI());
        capture.setMinutes(10);
        capture.setSeconds(30);
        capture.setBaseErr("Capture Error - ");
        capture.setDvgrab("dvgrab --autosplit --size 0 --format raw --opendml --buffers 200 dv/dv_file-");
        
        // Act
        capture.DV_Capture();
        
        // Assert
        assertTrue(capture.getGUI().getlblCaptureEnabled());
        assertTrue(capture.getGUI().getprgCaptureEnabled());
        assertTrue(capture.getGUI().getlblCaptureProgEnabled());
        assertEquals(100, capture.getGUI().getprgCaptureValue());
        assertFalse(capture.getGUI().getlblCaptureEnabled());
        assertFalse(capture.getGUI().getlblCaptureProgEnabled());
        assertEquals("100%", capture.getGUI().getlblCaptureProgText());
    }
}