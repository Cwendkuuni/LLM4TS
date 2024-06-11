import static org.junit.Assert.*;
import org.junit.Test;

public class CaptureTest {
    @Test
    public void testInit() {
        Capture capture = new Capture();
        assertFalse(capture.init());
        
        GUI dvdGui = new GUI();
        Capture capture2 = new Capture(5, 30, dvdGui);
        assertFalse(capture2.init());
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