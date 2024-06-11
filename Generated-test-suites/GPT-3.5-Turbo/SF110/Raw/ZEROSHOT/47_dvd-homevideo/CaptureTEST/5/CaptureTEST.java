import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CaptureTest {
    
    Capture capture;
    
    @Before
    public void setUp() {
        GUI gui = new GUI();
        capture = new Capture();
    }
    
    @Test
    public void testInit() {
        boolean result = capture.init();
        assertFalse(result);
    }
    
    @Test
    public void testRun() {
        capture.run();
    }
    
    @Test
    public void testDV_Capture() {
        capture.DV_Capture();
    }
}