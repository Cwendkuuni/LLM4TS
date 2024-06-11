import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CaptureTest {
    
    @Test
    public void testInit() {
        Capture capture = new Capture();
        boolean result = capture.init();
        assertEquals(true, result);
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