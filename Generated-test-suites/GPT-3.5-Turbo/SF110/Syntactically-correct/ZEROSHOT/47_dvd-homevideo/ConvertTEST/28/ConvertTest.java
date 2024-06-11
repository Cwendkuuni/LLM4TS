
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ConvertTest {

    private Convert convert;

    @Before
    public void setUp() {
        convert = new Convert("dvd-ntsc", "16:9", new GUI());
    }

    @Test
    public void testInit() {
        assertFalse(convert.init());
    }

    @Test
    public void testTranscode() {
        convert.Transcode();
        // Add assertions here
    }

    @Test
    public void testMatrix() {
        convert.matrix();
        // Add assertions here
    }
}
