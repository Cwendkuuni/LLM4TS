import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

public class ConvertTest {
    private Convert convert;

    @Before
    public void setUp() {
        GUI gui = new GUI();
        convert = new Convert("dvd-ntsc", "16:9", gui);
    }

    @Test
    public void testInitWithGUI() {
        assertFalse(convert.init());
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