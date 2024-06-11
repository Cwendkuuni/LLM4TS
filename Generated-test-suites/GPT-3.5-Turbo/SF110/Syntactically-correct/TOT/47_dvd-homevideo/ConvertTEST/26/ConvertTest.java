import org.junit.*;
import static org.junit.Assert.*;

public class ConvertTest {

    @Test
    public void testConvertFormatAspectRatio() {
        Convert convert = new Convert("dvd-ntsc", "16:9", new GUI());
        assertEquals("dvd-ntsc", convert.m_Format);
        assertEquals("16:9", convert.m_AspectRatio);
    }

    @Test
    public void testConvertQualityFormatAspectRatio() {
        Convert convert = new Convert(0, "dvd-ntsc", "4:3", new GUI());
        assertEquals(0, convert.m_Quality);
        assertEquals("dvd-ntsc", convert.m_Format);
        assertEquals("4:3", convert.m_AspectRatio);
    }

    @Test
    public void testInitWithThreadSelected() {
        GUI DVD_GUI = new GUI();
        DVD_GUI.menuChkThread.setSelected(true);
        Convert convert = new Convert("dvd-ntsc", "16:9", DVD_GUI);
        assertFalse(convert.init());
    }

    @Test
    public void testInitWithThreadNotSelected() {
        GUI DVD_GUI = new GUI();
        DVD_GUI.menuChkThread.setSelected(false);
        Convert convert = new Convert("dvd-ntsc", "16:9", DVD_GUI);
        assertFalse(convert.init());
    }

    @Test
    public void testRun() {
        Convert convert = new Convert("dvd-ntsc", "16:9", new GUI());
        convert.run();
        // Add assertions for the expected behavior of the run method
    }

    @Test
    public void testTranscode() {
        Convert convert = new Convert("dvd-ntsc", "16:9", new GUI());
        convert.Transcode();
        // Add assertions for the expected behavior of the Transcode method
    }

    @Test
    public void testMatrix() {
        Convert convert = new Convert("dvd-ntsc", "16:9", new GUI());
        convert.matrix();
        // Add assertions for the expected behavior of the matrix method
    }
}