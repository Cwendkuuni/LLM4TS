import org.junit.Test;
import static org.junit.Assert.*;

public class CaptureTest {
    
    @Test
    public void testInitWithThread() {
        Capture capture = new Capture();
        assertFalse(capture.init());
    }
    
    @Test
    public void testInitWithoutThread() {
        Capture capture = new Capture(5, 30, new GUI());
        assertFalse(capture.init());
    }
    
    @Test
    public void testRun() {
        Capture capture = new Capture();
        capture.run();
    }
    
    @Test
    public void testDV_Capture() {
        Capture capture = new Capture(3, 0, new GUI());
        capture.DV_Capture();
    }
    
    @Test
    public void testDV_CaptureWithExistingDirectory() {
        Capture capture = new Capture(2, 30, new GUI());
        capture.DV_Capture();
    }
    
    @Test
    public void testDV_CaptureWithExistingFile() {
        Capture capture = new Capture(1, 0, new GUI());
        capture.DV_Capture();
    }
}