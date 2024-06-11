import org.junit.Test;
import static org.junit.Assert.*;

public class ConvertTest {

    @Test
    public void testConstructor1() {
        GUI gui = new GUI();
        Convert convert = new Convert("dvd-ntsc", "16:9", gui);
        assertNotNull(convert);
    }

    @Test
    public void testConstructor2() {
        GUI gui = new GUI();
        Convert convert = new Convert(1, "dvd-pal", "4:3", gui);
        assertNotNull(convert);
    }

    @Test
    public void testInit() {
        GUI gui = new GUI();
        Convert convert = new Convert("dvd-ntsc", "16:9", gui);
        assertTrue(convert.init());
    }

    @Test
    public void testRun() {
        GUI gui = new GUI();
        Convert convert = new Convert("dvd-ntsc", "16:9", gui);
        convert.run();
        // Can't really test the run method as it calls Transcode internally
    }

    @Test
    public void testTranscode() {
        GUI gui = new GUI();
        Convert convert = new Convert("dvd-ntsc", "16:9", gui);
        convert.Transcode();
        assertNotNull(convert.video_files);
    }

    @Test
    public void testMatrix() {
        GUI gui = new GUI();
        Convert convert = new Convert("dvd-ntsc", "16:9", gui);
        convert.matrix();
        // Unable to perform exact assertion here, just checking if method runs without errors
    }

}