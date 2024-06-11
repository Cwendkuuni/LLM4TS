import static org.junit.Assert.*;
import org.junit.Test;

public class CaptureTest {
    
    @Test
    public void testCaptureConstructor1() {
        Capture capture = new Capture();
        assertNotNull(capture);
    }
    
    @Test
    public void testCaptureConstructor2() {
        GUI gui = new GUI();
        Capture capture = new Capture(10, 30, gui);
        assertNotNull(capture);
    }
    
    @Test
    public void testInit() {
        GUI gui = new GUI();
        Capture capture = new Capture(10, 30, gui);
        assertFalse(capture.init());
    }    
}