import org.junit.Test;
import static org.junit.Assert.*;

public class CaptureTest {

    @Test
    public void testInit() {
        Capture capture = new Capture();
        assertFalse(capture.init());
    }

    @Test
    public void testRun() {
        Capture capture = new Capture();
        capture.run();
    }

    @Test
    public void testDV_Capture() {
        Capture capture = new Capture();
        capture.DV_Capture();
    }
}