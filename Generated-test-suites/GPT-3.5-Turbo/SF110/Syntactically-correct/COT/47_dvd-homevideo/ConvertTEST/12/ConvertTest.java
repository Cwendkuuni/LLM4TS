
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
        boolean result = convert.init();
        assertFalse(result);
    }

    @Test
    public void testRun() {
        convert.run();
        // add assertions here
    }

    @Test
    public void testTranscode() {
        convert.Transcode();
        // add assertions here
    }

    @Test
    public void testMatrix() {
        convert.matrix();
        // add assertions here
    }
}
