import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class ConvertTest {

    @Test
    public void testInit() {
        Convert convert = new Convert("dvd-ntsc", "16:9");
        assertEquals(true, convert.init());
    }

    @Test
    public void testRun() {
        Convert convert = new Convert("dvd-ntsc", "16:9");
        convert.run();
    }

    @Test
    public void testTranscode() {
        Convert convert = new Convert("dvd-ntsc", "16:9");
        convert.Transcode();
    }

    @Test
    public void testMatrix() {
        Convert convert = new Convert("dvd-ntsc", "16:9");
        convert.matrix();
        // Check if matrix.txt file exists
        // Your code to check existence of file goes here
    }
}