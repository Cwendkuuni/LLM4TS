import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConvertTest {
    private Convert convert;

    @Before
    public void setUp() {
        GUI dvdGUI = new GUI();
        convert = new Convert("dvd-ntsc", "16:9", dvdGUI);
    }

    @Test
    public void testInit() {
        boolean result = convert.init();
        assertFalse(result);
    }

    @Test
    public void testRun() {
        convert.run();
    }

    @Test
    public void testTranscode() {
        convert.Transcode();
    }

    @Test
    public void testMatrix() {
        convert.matrix();
    }
}