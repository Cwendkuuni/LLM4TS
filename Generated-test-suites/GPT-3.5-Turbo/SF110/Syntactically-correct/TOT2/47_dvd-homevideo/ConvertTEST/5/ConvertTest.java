import static org.junit.Assert.*;
import org.junit.Test;

public class ConvertTest {

    Convert convert;

    @Test
    public void testInit() {
        // Test positive scenario
        convert = new Convert("dvd-ntsc", "16:9", new GUI());
        assertTrue(convert.init());
        
        // Test negative scenario
        convert = new Convert("mp4", "4:3", new GUI());
        assertFalse(convert.init());
    }

    @Test
    public void testRun() {
        convert = new Convert("dvd-ntsc", "16:9", new GUI());
        convert.run();
        // Validate any desired state changes after run
    }

    @Test
    public void testTranscode() {
        convert = new Convert("dvd-ntsc", "16:9", new GUI());
        convert.Transcode();
        // Validate any desired state changes after Transcode
    }

    @Test
    public void testMatrix() {
        convert = new Convert("dvd-ntsc", "16:9", new GUI());
        convert.matrix();
        // Validate matrix.txt creation
    }
}