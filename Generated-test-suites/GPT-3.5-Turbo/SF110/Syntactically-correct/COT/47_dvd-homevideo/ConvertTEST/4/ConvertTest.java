import org.junit.*;
import static org.junit.Assert.*;

public class ConvertTest {

    @Test
    public void testInit() {
        Convert convert = new Convert("dvd-ntsc", "16:9", new GUI());
        assertFalse(convert.init());
    }

    @Test
    public void testRun() {
        Convert convert = new Convert("dvd-ntsc", "16:9", new GUI());
        convert.run();
    }

    @Test
    public void testTranscode() {
        Convert convert = new Convert("dvd-ntsc", "16:9", new GUI());
        convert.transcode();
    }

    @Test
    public void testMatrix() {
        Convert convert = new Convert("dvd-ntsc", "16:9", new GUI());
        convert.matrix();
    }
}