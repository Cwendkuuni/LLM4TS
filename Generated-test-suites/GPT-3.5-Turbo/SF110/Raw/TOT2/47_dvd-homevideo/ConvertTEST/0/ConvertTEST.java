import org.junit.Test;
import static org.junit.Assert.*;

public class ConvertTest {

    @Test
    public void testInit() {
        // Test typical case
        Convert convert = new Convert("dvd-ntsc", "16:9", new GUI());
        assertTrue(convert.init());
        
        // Test error case
        Convert convertError = new Convert("dvd-pal", "4:3", new GUI());
        assertFalse(convertError.init());
    }

    @Test
    public void testRun() {
        // Not much to test since it just calls Transcode
    }

    @Test
    public void testTranscode() {
        // Test for different quality levels
        Convert convert = new Convert(0, "dvd-ntsc", "16:9", new GUI());
        convert.Transcode();
        assertNotNull(convert.video_files);

        Convert convertHighQuality = new Convert(2, "dvd-pal", "4:3", new GUI());
        convertHighQuality.Transcode();
        assertNotNull(convertHighQuality.video_files);

        // Test exception handling scenario
        Convert convertException = new Convert(1, "dvd-ntsc", "16:9", new GUI());
        try {
            convertException.Transcode();
        } catch (StringIndexOutOfBoundsException e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void testMatrix() {
        Convert convert = new Convert("dvd-ntsc", "16:9", new GUI());
        convert.matrix(); // Just testing for any exceptions

        // Potential edge case: invalid file path
        Convert convertInvalidPath = new Convert("dvd-pal", "4:3", new GUI());
        try {
            convertInvalidPath.matrix();
        } catch (IOException e) {
            fail("Should not throw an exception");
        }
    }
}