import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.*;

public class CaptureTest {

    private Capture capture;

    @Before
    public void setUp() {
        capture = new Capture();
    }

    @After
    public void tearDown() {
        capture = null;
    }

    @Test
    public void testInit() {
        assertFalse(capture.init());
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