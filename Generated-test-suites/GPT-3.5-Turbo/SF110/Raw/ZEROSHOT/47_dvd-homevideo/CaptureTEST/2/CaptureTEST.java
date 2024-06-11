import org.junit.Test;
import static org.junit.Assert.*;

public class CaptureTest {
    @Test
    public void testInit() {
        Capture capture = new Capture();
        boolean result = capture.init();
        assertFalse(result);
        
        capture = new Capture();
        GUI gui = new GUI();
        capture.m_GUI = gui;
        result = capture.init();
        assertFalse(result);
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