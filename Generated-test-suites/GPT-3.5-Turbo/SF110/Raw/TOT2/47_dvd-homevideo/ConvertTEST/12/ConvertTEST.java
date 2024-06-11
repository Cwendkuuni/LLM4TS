import org.junit.*;
import static org.junit.Assert.*;

public class ConvertTest {
    Convert convert;

    @Before
    public void setUp() {
        convert = new Convert("dvd-ntsc", "16:9", new GUI());
    }

    @Test
    public void testInit() {
        assertTrue(convert.init());
    }

    @Test
    public void testRun() {
        convert.run();
        // Do assertions related to run method
    }

    @Test
    public void testTranscode() {
        convert.Transcode();
        // Do assertions related to Transcode method
    }

    @Test
    public void testMatrix() {
        convert.matrix();
        // Do assertions related to matrix method
    }

    @Test(expected = IOException.class)
    public void testMatrixIOException() throws IOException {
        // Simulate IOException in matrix method
        convert.matrix();
    }

    @After
    public void tearDown() {
        convert = null;
    }
}