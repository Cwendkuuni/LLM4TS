import org.junit.*;
import static org.junit.Assert.*;

public class ConvertTest {

    @Test
    public void testInit() {
        Convert convert = new Convert("dvd-ntsc", "16:9", new GUI());
        boolean result = convert.init();
        assertTrue(result);
    }

    @Test
    public void testRun() {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI());
        convert.run();
        String[] video_files = convert.video_files;
        assertNotNull(video_files);
    }

    @Test
    public void testTranscode() {
        Convert convert = new Convert("dvd-ntsc", "16:9", new GUI());
        convert.Transcode();
        String m_transcode = convert.m_transcode;
        assertNotNull(m_transcode);
    }

    @Test
    public void testMatrix() {
        Convert convert = new Convert("dvd-ntsc", "4:3", new GUI());
        convert.matrix();
        String matrixPath = convert.m_GUI.strOutputDir + "/matrix.txt";
        File matrixFile = new File(matrixPath);
        assertTrue(matrixFile.exists());
    }
}