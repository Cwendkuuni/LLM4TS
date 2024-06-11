import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ConvertTest {
    
    private Convert convert;

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
        // Add assertions for the expected behavior of the run() method
    }

    @Test
    public void testTranscode() {
        convert.Transcode();
        // Add assertions for the expected behavior of the Transcode() method
    }

    @Test
    public void testMatrix() {
        convert.matrix();
        // Add assertions for the expected behavior of the matrix() method
    }
}