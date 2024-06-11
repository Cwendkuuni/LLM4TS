import org.junit.Test;
import static org.junit.Assert.*;

public class ConvertTest {
    
    @Test
    public void testConvertFormatAspectRatioGUIConstructor() {
        GUI gui = new GUI();
        Convert converter = new Convert("dvd-ntsc", "4:3", gui);
        assertNotNull(converter);
        assertEquals("dvd-ntsc", converter.m_Format);
        assertEquals("4:3", converter.m_AspectRatio);
        assertEquals(gui, converter.m_GUI);
    }
    
    @Test
    public void testConvertQualityFormatAspectRatioGUIConstructor() {
        GUI gui = new GUI();
        Convert converter = new Convert(1, "dvd-pal", "16:9", gui);
        assertNotNull(converter);
        assertEquals(1, converter.m_Quality);
        assertEquals("dvd-pal", converter.m_Format);
        assertEquals("16:9", converter.m_AspectRatio);
        assertEquals(gui, converter.m_GUI);
    }
    
    @Test
    public void testInitWithThreadSelected() {
        GUI gui = new GUI();
        Convert converter = new Convert("dvd-ntsc", "4:3", gui);
        gui.menuChkThread.setSelected(true);
        assertTrue(converter.init());
    }
    
    @Test
    public void testInitWithoutThreadSelected() {
        GUI gui = new GUI();
        Convert converter = new Convert("dvd-ntsc", "4:3", gui);
        gui.menuChkThread.setSelected(false);
        assertTrue(converter.init());
    }

    // Add more test cases based on the edge cases and exceptions identified in the class Convert

}