import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.File;

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
        File outputDir = new File(capture.m_GUI.strOutputDir);
        assertFalse(outputDir.exists());

        capture.DV_Capture();

        assertTrue(outputDir.exists());
        assertTrue(outputDir.isDirectory());

        outputDir = new File(capture.m_GUI.strOutputDir + "/DVD");
        assertTrue(outputDir.exists());
        assertTrue(outputDir.isDirectory());

        outputDir = new File(capture.m_GUI.strOutputDir + "/log");
        assertTrue(outputDir.exists());
        assertTrue(outputDir.isDirectory());

        outputDir = new File(capture.m_GUI.strOutputDir + "/dv");
        assertTrue(outputDir.exists());
        assertTrue(outputDir.isDirectory());
    }

}