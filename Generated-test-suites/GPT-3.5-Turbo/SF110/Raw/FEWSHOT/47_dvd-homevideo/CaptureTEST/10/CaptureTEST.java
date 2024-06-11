import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class CaptureTest {
    @Test
    public void testInit() {
        Capture capture = new Capture();
        assertTrue(capture.init());
        
        GUI gui = new GUI();
        Capture capture2 = new Capture(5, 10, gui);
        assertFalse(capture2.init());
    }
    
    @Test
    public void testRun() {
        Capture capture = new Capture();
        capture.run();
        
        GUI gui = new GUI();
        Capture capture2 = new Capture(5, 10, gui);
        capture2.run();
    }
    
    @Test
    public void testDV_Capture() {
        Capture capture = new Capture();
        capture.DV_Capture();
    }
}