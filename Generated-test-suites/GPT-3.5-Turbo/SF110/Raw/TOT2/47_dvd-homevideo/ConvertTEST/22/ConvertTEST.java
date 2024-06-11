import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;

public class ConvertTest {

    @Test
    public void testConvertFormatAspectRatioGUI() {
        Convert convert = new Convert("dvd-ntsc", "16:9", new GUI());
        assertNotNull(convert);
    }
    
    @Test
    public void testConvertQualityFormatAspectRatioGUI() {
        Convert convert = new Convert(1, "dvd-pal", "4:3", new GUI());
        assertNotNull(convert);
    }
    
    @Test
    public void testInitWithThreadJoin() {
        Convert convert = new Convert("dvd-ntsc", "16:9", new GUI());
        boolean result = convert.init();
        assertFalse(result);
    }
    
    @Test
    public void testTranscode() {
        Convert convert = new Convert("dvd-ntsc", "16:9", new GUI());
        convert.Transcode();
        assertNotNull(convert.video_files);
    }
    
    @Test
    public void testMatrix() {
        Convert convert = new Convert("dvd-pal", "4:3", new GUI());
        convert.matrix();
        File file = new File(convert.m_GUI.strOutputDir + "/matrix.txt");
        assertTrue(file.exists());
    }
}