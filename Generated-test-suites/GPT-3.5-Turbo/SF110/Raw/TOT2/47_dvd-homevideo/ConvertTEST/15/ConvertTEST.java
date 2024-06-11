import org.junit.Test;
import static org.junit.Assert.*;

public class ConvertTest {

    @Test
    public void testConvertConstructorWithStringArgs() {
        Convert convert = new Convert("dvd-ntsc", "16:9", new GUI());
        
        assertEquals(29.97, convert.m_fps, 0.0);
        assertEquals("8500", convert.m_bitrate);
        assertEquals("dvd-ntsc", convert.m_Format);
        assertEquals("16:9", convert.m_AspectRatio);
        assertFalse(convert.m_Error);
        assertEquals(0, convert.thread_track);
    }

    @Test
    public void testConvertConstructorWithIntArgs() {
        Convert convert = new Convert(1, "dvd-ntsc", "16:9", new GUI());
        
        assertEquals(29.97, convert.m_fps, 0.0);
        assertEquals("8500", convert.m_bitrate);
        assertEquals("dvd-ntsc", convert.m_Format);
        assertEquals("16:9", convert.m_AspectRatio);
        assertFalse(convert.m_Error);
        assertEquals(0, convert.thread_track);
        assertEquals(1, convert.m_Quality);
    }

    @Test
    public void testInitWithThreadSelected() {
        Convert convert = new Convert("dvd-ntsc", "16:9", new GUI());
        convert.init();
        
        assertFalse(convert.m_Error);
    }

    @Test
    public void testInitWithThreadNotSelected() {
        Convert convert = new Convert("dvd-ntsc", "16:9", new GUI());
        convert.m_GUI.menuChkThread.setSelected(false);
        convert.init();
        
        assertFalse(convert.m_Error);
    }

    @Test
    public void testTranscode() {
        Convert convert = new Convert("dvd-ntsc", "16:9", new GUI());
        convert.Transcode();
        
        assertFalse(convert.m_Error);
    }

    @Test
    public void testMatrix() {
        Convert convert = new Convert("dvd-ntsc", "16:9", new GUI());
        convert.matrix();
        
        // Check if matrix.txt file is created
        File file = new File(convert.m_GUI.strOutputDir + "/matrix.txt");
        assertTrue(file.exists());
    }
}