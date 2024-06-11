
import org.junit.Test;
import static org.junit.Assert.*;

public class CaptureTest {

    @Test
    public void testConstructor1() {
        Capture capture = new Capture();
        assertNotNull(capture);
    }

    @Test
    public void testConstructor2() {
        GUI gui = new GUI();
        Capture capture = new Capture(5, 30, gui);
        assertEquals(5, capture.m_Minutes);
        assertEquals(30, capture.m_Seconds);
        assertEquals(gui, capture.m_GUI);
    }

    @Test
    public void testInit() {
        GUI gui = new GUI();
        Capture capture = new Capture(3, 45, gui);
        boolean result = capture.init();
        assertTrue(result);
    }

    @Test
    public void testRun() {
        Capture capture = new Capture();
        capture.run(); // No assert as it's a void method
    }

    @Test
    public void testDV_Capture() {
        GUI gui = new GUI();
        Capture capture = new Capture(2, 15, gui);
        capture.DV_Capture();
        assertFalse(capture.m_Error);
    }
}

