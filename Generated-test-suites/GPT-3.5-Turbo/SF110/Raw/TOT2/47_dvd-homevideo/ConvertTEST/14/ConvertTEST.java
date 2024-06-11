import static org.junit.Assert.*;
import org.junit.Test;

public class ConvertTest {

    @Test
    public void testInit() {
        Convert convert = new Convert("dvd-ntsc", "16:9", new GUI());
        boolean result = convert.init();
        assertFalse(result);
    }
    
    @Test
    public void testTranscode() {
        Convert convert = new Convert("dvd-pal", "4:3", new GUI());
        convert.Transcode();
        String[] videoFiles = convert.video_files;
        assertNotNull(videoFiles);
    }
    
    @Test
    public void testMatrix() {
        Convert convert = new Convert("dvd-ntsc", "16:9", new GUI());
        convert.matrix();
        // Check if matrix.txt file is created successfully
    }
}