import static org.junit.Assert.*;
import org.junit.Test;

public class ConvertTest {
    
    @Test
    public void testConstructor1() {
        String format = "dvd-ntsc";
        String aspectRatio = "16:9";
        GUI DVD_GUI = new GUI();
        Convert convert = new Convert(format, aspectRatio, DVD_GUI);
        assertNotNull(convert);
        assertEquals(format, convert.m_Format);
        assertEquals(aspectRatio, convert.m_AspectRatio);
        assertEquals(DVD_GUI, convert.m_GUI);
    }
    
    @Test
    public void testConstructor2() {
        int quality = 1;
        String format = "dvd-pal";
        String aspectRatio = "4:3";
        GUI DVD_GUI = new GUI();
        Convert convert = new Convert(quality, format, aspectRatio, DVD_GUI);
        assertNotNull(convert);
        assertEquals(quality, convert.m_Quality);
        assertEquals(format, convert.m_Format);
        assertEquals(aspectRatio, convert.m_AspectRatio);
        assertEquals(DVD_GUI, convert.m_GUI);
    }
    
    @Test
    public void testInit() {
        String format = "dvd-ntsc";
        String aspectRatio = "16:9";
        GUI DVD_GUI = new GUI();
        Convert convert = new Convert(format, aspectRatio, DVD_GUI);
        boolean result = convert.init();
        assertFalse(result);
    }
    
    @Test
    public void testTranscode() {
        String format = "dvd-ntsc";
        String aspectRatio = "16:9";
        GUI DVD_GUI = new GUI();
        Convert convert = new Convert(format, aspectRatio, DVD_GUI);
        convert.Transcode();
        // Add assertion for the functionality of Transcode method
    }
    
    @Test
    public void testMatrix() {
        GUI DVD_GUI = new GUI();
        Convert convert = new Convert("dvd-ntsc", "16:9", DVD_GUI);
        convert.matrix();
        // Add assertion for the functionality of matrix method
    }
}