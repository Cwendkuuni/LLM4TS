import org.junit.Test;
import static org.junit.Assert.*;

public class CaptureTest {

    @Test
    public void testInit() {
        Capture capture = new Capture();
        assertFalse(capture.init());
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

    @Test
    public void testConstructor1() {
        int min = 10;
        int sec = 30;
        GUI gui = new GUI();
        Capture capture = new Capture(min, sec, gui);
        assertEquals(min, capture.m_Minutes);
        assertEquals(sec, capture.m_Seconds);
        assertEquals(gui, capture.m_GUI);
    }

    @Test
    public void testConstructor2() {
        Capture capture = new Capture();
        assertNotNull(capture);
    }
}