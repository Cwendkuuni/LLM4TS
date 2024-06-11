import org.junit.Test;

public class CaptureTest {

    @Test
    public void testInit_menuChkThreadIsSelectedTrue() {
        // Setup
        Capture capture = new Capture();
        capture.m_GUI.menuChkThread.setSelected(true);

        // Exercise
        boolean result = capture.init();

        // Verify
        // ... add assertions here
    }
}