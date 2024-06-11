import org.junit.*;
import static org.junit.Assert.*;

public class ConvertTest {

    @Test
    public void testConstructorWithFormatAspectRatioAndGUI() {
        GUI mockGUI = new GUI();
        Convert convert = new Convert("dvd-ntsc", "16:9", mockGUI);
        assertNotNull(convert);
        assertEquals("dvd-ntsc", convert.m_Format);
        assertEquals("16:9", convert.m_AspectRatio);
    }

    @Test
    public void testConstructorWithQualityFormatAspectRatioAndGUI() {
        GUI mockGUI = new GUI();
        Convert convert = new Convert(1, "dvd-pal", "4:3", mockGUI);
        assertNotNull(convert);
        assertEquals(1, convert.m_Quality);
        assertEquals("dvd-pal", convert.m_Format);
        assertEquals("4:3", convert.m_AspectRatio);
    }

    @Test
    public void testInitWithoutThread() {
        GUI mockGUI = new GUI();
        Convert convert = new Convert("dvd-pal", "4:3", mockGUI);
        boolean result = convert.init();
        assertFalse(result);
    }

    @Test
    public void testRun() {
        GUI mockGUI = new GUI();
        Convert convert = new Convert("dvd-pal", "4:3", mockGUI);
        convert.run();
        // No assertions since run method does not return anything
    }

    @Test
    public void testTranscode() {
        GUI mockGUI = new GUI();
        Convert convert = new Convert("dvd-pal", "4:3", mockGUI);
        convert.Transcode();
        // No assertions since Transcode method does not return anything
    }

    @Test
    public void testMatrix() {
        GUI mockGUI = new GUI();
        Convert convert = new Convert("dvd-pal", "4:3", mockGUI);
        convert.matrix();
        // No assertions since matrix method does not return anything
    }
}