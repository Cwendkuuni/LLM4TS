import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CaptureTest {
    private Capture capture;

    @Before
    public void setUp() {
        capture = new Capture();
    }

    @Test
    public void testInit() {
        boolean result = capture.init();
        assertTrue(result);
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