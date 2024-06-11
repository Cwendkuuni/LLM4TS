import org.junit.*;
import static org.junit.Assert.*;

public class CaptureTest {

    @Test
    public void testInit_menuChkThreadSelected_ReturnsFalse() {
        Capture capture = new Capture();
        capture.m_GUI = new GUI();
        capture.m_Thread = null;

        boolean result = capture.init();

        assertFalse(result);
    }

    @Test
    public void testInit_menuChkThreadNotSelected_ReturnsTrue() {
        Capture capture = new Capture();
        capture.m_GUI = new GUI();
        capture.m_Thread = null;
        capture.m_Error = false;
        capture.m_GUI.ErrorCheckResult = false;

        boolean result = capture.init();

        assertTrue(result);
    }

    @Test
    public void testInit_menuChkThreadNotSelected_ErrorCheckReturnsTrue_ReturnsTrue() {
        Capture capture = new Capture();
        capture.m_GUI = new GUI();
        capture.m_Thread = null;
        capture.m_Error = false;
        capture.m_GUI.ErrorCheckResult = true;

        boolean result = capture.init();

        assertTrue(result);
    }

    @Test
    public void testRun() {
        Capture capture = new Capture();
        capture.m_GUI = new GUI();

        capture.run();
    }

    @Test
    public void testDV_Capture_CreateOutDirExistsAndIsDirectory_ConfirmDeleteSelected_DeleteDirAndReturnFalse() {
        Capture capture = new Capture();
        capture.m_GUI = new GUI();
        capture.m_GUI.menuChkThread = new CheckBox();
        capture.m_GUI.menuChkThread.setSelected(true);
        capture.m_Error = false;
        capture.m_GUI.ErrorCheckResult = false;
        capture.m_GUI.strOutputDir = "/test/output";

        capture.DV_Capture();

        assertFalse(capture.m_Error);
    }

    @Test
    public void testDV_Capture_CreateOutDirExistsAndIsDirectory_ConfirmDeleteNotSelected_ReturnFalse() {
        Capture capture = new Capture();
        capture.m_GUI = new GUI();
        capture.m_GUI.menuChkThread = new CheckBox();
        capture.m_GUI.menuChkThread.setSelected(true);
        capture.m_Error = false;
        capture.m_GUI.ErrorCheckResult = false;
        capture.m_GUI.strOutputDir = "/test/output";

        capture.DV_Capture();

        assertTrue(capture.m_Error);
    }

    @Test
    public void testDV_Capture_CreateOutDirExistsAndIsFile_ConfirmDeleteSelected_DeleteFileAndReturnFalse() {
        Capture capture = new Capture();
        capture.m_GUI = new GUI();
        capture.m_GUI.menuChkThread = new CheckBox();
        capture.m_GUI.menuChkThread.setSelected(true);
        capture.m_Error = false;
        capture.m_GUI.ErrorCheckResult = false;
        capture.m_GUI.strOutputDir = "/test/output";

        capture.DV_Capture();

        assertFalse(capture.m_Error);
    }

    @Test
    public void testDV_Capture_CreateOutDirExistsAndIsFile_ConfirmDeleteNotSelected_ReturnFalse() {
        Capture capture = new Capture();
        capture.m_GUI = new GUI();
        capture.m_GUI.menuChkThread = new CheckBox();
        capture.m_GUI.menuChkThread.setSelected(true);
        capture.m_Error = false;
        capture.m_GUI.ErrorCheckResult = false;
        capture.m_GUI.strOutputDir = "/test/output";

        capture.DV_Capture();

        assertTrue(capture.m_Error);
    }

    @Test
    public void testDV_Capture_CreateOutDirDoesNotExist_CreatesDirectoriesAndExecutesDvgrab() {
        Capture capture = new Capture();
        capture.m_GUI = new GUI();
        capture.m_GUI.menuChkThread = new CheckBox();
        capture.m_GUI.menuChkThread.setSelected(false);
        capture.m_GUI.blnBegin = true;
        capture.m_Error = false;
        capture.m_GUI.strOutputDir = "/test/output";

        capture.DV_Capture();

        File outputDir = new File("/test/output");
        assertTrue(outputDir.exists());
        assertTrue(outputDir.isDirectory());

        File dvdDir = new File("/test/output/DVD");
        assertTrue(dvdDir.exists());
        assertTrue(dvdDir.isDirectory());

        File logDir = new File("/test/output/log");
        assertTrue(logDir.exists());
        assertTrue(logDir.isDirectory());

        File dvDir = new File("/test/output/dv");
        assertTrue(dvDir.exists());
        assertTrue(dvDir.isDirectory());
    }

    @Test
    public void testDV_Capture_CreateOutDirDoesNotExist_IOExceptionOccurs_ReturnsTrue() {
        Capture capture = new Capture();
        capture.m_GUI = new GUI();
        capture.m_GUI.menuChkThread = new CheckBox();
        capture.m_GUI.menuChkThread.setSelected(false);
        capture.m_GUI.blnBegin = true;
        capture.m_Error = false;
        capture.m_GUI.strOutputDir = "/test/output";

        capture.DV_Capture();

        assertTrue(capture.m_Error);
    }

    @Test
    public void testDV_Capture_CreateOutDirDoesNotExist_NullPointerExceptionOccurs_ReturnsTrue() {
        Capture capture = new Capture();
        capture.m_GUI = new GUI();
        capture.m_GUI.menuChkThread = new CheckBox();
        capture.m_GUI.menuChkThread.setSelected(false);
        capture.m_GUI.blnBegin = true;
        capture.m_Error = false;
        capture.m_GUI.strOutputDir = "/test/output";

        capture.DV_Capture();

        assertTrue(capture.m_Error);
    }

    @Test
    public void testDV_Capture_CreateOutDirDoesNotExist_IllegalArgumentExceptionOccurs_ReturnsTrue() {
        Capture capture = new Capture();
        capture.m_GUI = new GUI();
        capture.m_GUI.menuChkThread = new CheckBox();
        capture.m_GUI.menuChkThread.setSelected(false);
        capture.m_GUI.blnBegin = true;
        capture.m_Error = false;
        capture.m_GUI.strOutputDir = "/test/output";

        capture.DV_Capture();

        assertTrue(capture.m_Error);
    }

    @Test
    public void testDV_Capture_CreateOutDirDoesNotExist_ExceptionOccurs_ReturnsTrue() {
        Capture capture = new Capture();
        capture.m_GUI = new GUI();
        capture.m_GUI.menuChkThread = new CheckBox();
        capture.m_GUI.menuChkThread.setSelected(false);
        capture.m_GUI.blnBegin = true;
        capture.m_Error = false;
        capture.m_GUI.strOutputDir = "/test/output";

        capture.DV_Capture();

        assertTrue(capture.m_Error);
    }

}