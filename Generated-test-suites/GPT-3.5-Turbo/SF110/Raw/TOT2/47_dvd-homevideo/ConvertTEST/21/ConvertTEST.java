import org.junit.*;
import static org.junit.Assert.*;

public class ConvertTest {

    Convert converter;

    @Before
    public void setUp() {
        GUI gui = new GUI();
        converter = new Convert("dvd-ntsc", "4:3", gui);
    }

    @Test
    public void testInit() {
        assertTrue(converter.init());
    }

    @Test
    public void testTranscode() {
        converter.Transcode();
        assertNotNull(converter.video_files);
    }

    @Test
    public void testMatrix() {
        converter.matrix();
        assertNotNull(converter.video_files);
    }

    @After
    public void tearDown() {
        converter = null;
    }
}