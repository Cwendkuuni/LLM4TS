import org.junit.Test;
import static org.junit.Assert.*;

public class ConvertTest {

    @Test
    public void testInit() {
        // Typical use case
        Convert convert = new Convert("dvd-ntsc", "16:9", new GUI());
        assertTrue(convert.init());
    }

    @Test
    public void testTranscode() {
        // Typical use case
        Convert convert = new Convert("dvd-ntsc", "16:9", new GUI());
        convert.Transcode();
        assertNotNull(convert.video_files);
    }

    @Test
    public void testMatrix() {
        // Typical use case
        Convert convert = new Convert("dvd-ntsc", "16:9", new GUI());
        convert.matrix();
        // Assert matrix file is created successfully
        File matrixFile = new File(convert.m_GUI.strOutputDir + "/" + "matrix.txt");
        assertTrue(matrixFile.exists());
    }

    @Test(expected = InterruptedException.class)
    public void testInitWithThreadJoinException() throws InterruptedException {
        // Exception handling scenario
        Convert convert = new Convert("dvd-ntsc", "16:9", new GUI());
        convert.m_GUI.menuChkThread.setSelected(true);
        convert.init();
    }

    @Test(expected = NullPointerException.class)
    public void testTranscodeWithNullPointerException() {
        // Edge case with potential NullPointerException
        Convert convert = new Convert("dvd-ntsc", "16:9", new GUI());
        convert.video_files = null;
        convert.Transcode();
    }
}