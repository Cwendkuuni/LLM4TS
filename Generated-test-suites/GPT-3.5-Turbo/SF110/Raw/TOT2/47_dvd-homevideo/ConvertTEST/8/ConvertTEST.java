import org.junit.Test;
import static org.junit.Assert.*;

public class ConvertTest {

    Convert convert;

    @Test
    public void testInit() {
        convert = new Convert("dvd-ntsc", "16:9", new GUI());
        assertTrue(convert.init());
    }

    @Test
    public void testTranscode() {
        convert = new Convert("dvd-ntsc", "16:9", new GUI());
        convert.Transcode();
        assertNotNull(convert.video_files);
    }

    @Test
    public void testMatrix() {
        convert = new Convert("dvd-ntsc", "16:9", new GUI());
        convert.matrix();
        // Check if matrix.txt was created successfully
        File file = new File("matrix.txt");
        assertTrue(file.exists());
    }
}