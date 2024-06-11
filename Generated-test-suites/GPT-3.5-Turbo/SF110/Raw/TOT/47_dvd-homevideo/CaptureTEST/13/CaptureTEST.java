import org.junit.*;
import static org.junit.Assert.*;

public class CaptureTest {

    @Test
    public void testInitWithMenuChkThreadSelected() {
        GUI gui = new GUI();
        Capture capture = new Capture(10, 30, gui);
        capture.init();
        // TODO: Asserts
    }

    @Test
    public void testInitWithoutMenuChkThreadSelected() {
        GUI gui = new GUI();
        Capture capture = new Capture(10, 30, gui);
        capture.init();
        // TODO: Asserts
    }

    @Test
    public void testRun() {
        GUI gui = new GUI();
        Capture capture = new Capture(10, 30, gui);
        capture.run();
        // TODO: Asserts
    }

    @Test
    public void testDV_CaptureWithExistingOutputDir() {
        GUI gui = new GUI();
        Capture capture = new Capture(10, 30, gui);
        capture.DV_Capture();
        // TODO: Asserts
    }

    @Test
    public void testDV_CaptureWithExistingFileWithSameNameAsOutputDir() {
        GUI gui = new GUI();
        Capture capture = new Capture(10, 30, gui);
        capture.DV_Capture();
        // TODO: Asserts
    }

    @Test
    public void testDV_CaptureWithValidConditions() {
        GUI gui = new GUI();
        Capture capture = new Capture(10, 30, gui);
        capture.DV_Capture();
        // TODO: Asserts
    }
}