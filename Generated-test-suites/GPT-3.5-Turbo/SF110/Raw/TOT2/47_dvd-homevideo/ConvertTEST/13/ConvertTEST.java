import static org.junit.Assert.*;
import org.junit.Test;

public class ConvertTest {

    @Test
    public void testConstructor1() {
        Convert convert = new Convert("dvd-ntsc", "16:9", new GUI());
        assertEquals(29.97, convert.m_fps, 0.0);
    }

    @Test
    public void testConstructor2() {
        Convert convert = new Convert(1, "dvd-pal", "4:3", new GUI());
        assertEquals(25.0, convert.m_fps, 0.0);
        assertEquals(1, convert.m_Quality);
    }

    @Test
    public void testInit() {
        GUI gui = new GUI();
        Convert convert = new Convert("dvd-ntsc", "16:9", gui);
        assertFalse(convert.init());
    }

    @Test
    public void testRun() {
        Convert convert = new Convert("dvd-ntsc", "16:9", new GUI());
        convert.run(); // Just checking if there are no exceptions thrown
    }

    @Test
    public void testTranscode() {
        Convert convert = new Convert("dvd-ntsc", "16:9", new GUI());
        convert.Transcode(); // Just checking if there are no exceptions thrown
    }

    @Test
    public void testMatrix() {
        Convert convert = new Convert("dvd-ntsc", "16:9", new GUI());
        convert.matrix(); // Just checking if there are no exceptions thrown
    }

    // Add more test cases for edge cases and error scenarios as needed

}