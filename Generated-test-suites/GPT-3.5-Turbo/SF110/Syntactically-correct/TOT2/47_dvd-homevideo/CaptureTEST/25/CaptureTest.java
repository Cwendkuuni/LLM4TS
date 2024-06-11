import org.junit.Test;
import static org.junit.Assert.*;

public class CaptureTest {
    
    @Test
    public void testInit() {
        Capture capture = new Capture(5, 30, new GUI());
        assertTrue(capture.init());
    }
    
    @Test
    public void testRun() {
        Capture capture = new Capture();
        capture.run();
        // No assertion needed as it should run without error
    }
    
    @Test
    public void testDV_Capture() {
        Capture capture = new Capture(10, 0, new GUI());
        capture.DV_Capture();
        // No assertion needed as it should run without error
    }
}