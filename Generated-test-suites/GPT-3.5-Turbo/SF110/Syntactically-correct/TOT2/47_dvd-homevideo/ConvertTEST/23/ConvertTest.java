import org.junit.Test;
import static org.junit.Assert.*;

public class ConvertTest {

    @Test
    public void testInit() {
        // Typical use case
        Convert convert = new Convert("dvd-ntsc", "16:9", new GUI());
        assertTrue(convert.init());
    }

    @Test
    public void testRun() {
        // Edge case
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI());
        convert.run();
    }

    @Test
    public void testTranscode() {
        // Typical use case
        Convert convert = new Convert("dvd-ntsc", "16:9", new GUI());
        convert.Transcode();
    }

    @Test
    public void testMatrix() {
        // Error scenario
        Convert convert = new Convert("dvd-ntsc", "16:9", new GUI());
        convert.matrix();
    }

}