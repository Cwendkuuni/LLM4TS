import org.junit.Test;
import static org.junit.Assert.*;

public class CaptureTest {

    @Test
    public void testInit() {
        Capture capture = new Capture();
        boolean result = capture.init();
        assertTrue(result);
        
        capture = new Capture(5, 10, new GUI());
        result = capture.init();
        assertTrue(result);
    }
    
    @Test
    public void testRun() {
        Capture capture = new Capture();
        capture.run();
        
        capture = new Capture(5, 10, new GUI());
        capture.run();
    }
    
    @Test
    public void testDV_Capture() {
        Capture capture = new Capture();
        capture.DV_Capture();
        
        capture = new Capture(5, 10, new GUI());
        capture.DV_Capture();
    }
    
    @Test
    public void testDV_Capture_Exceptions() {
        Capture capture = new Capture();
        
        // Test IOException
        try {
            capture.DV_Capture();
            fail("Expected IOException");
        } catch (IOException e) {
            // Exception caught as expected
        }
        
        // Test InterruptedException
        try {
            capture.DV_Capture();
            fail("Expected InterruptedException");
        } catch (InterruptedException e) {
            // Exception caught as expected
        }
    }
}