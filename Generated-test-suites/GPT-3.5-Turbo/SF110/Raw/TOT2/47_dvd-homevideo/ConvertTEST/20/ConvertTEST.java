import org.junit.Test;
import static org.junit.Assert.*;

public class ConvertTest {
    
    @Test
    public void testConstructor1() {
        Convert testConvert = new Convert("dvd-ntsc", "16:9", new GUI());
        assertEquals("dvd-ntsc", testConvert.m_Format);
        assertEquals(29.97, testConvert.m_fps, 0.01);
    }
    
    @Test
    public void testConstructor2() {
        Convert testConvert = new Convert(1, "dvd-pal", "4:3", new GUI());
        assertEquals(1, testConvert.m_Quality);
        assertEquals("dvd-pal", testConvert.m_Format);
        assertEquals(25.0, testConvert.m_fps, 0.01);
    }
    
    @Test
    public void testInitWithThread() {
        GUI testGUI = new GUI();
        testGUI.menuChkThread.setSelected(true);
        Convert testConvert = new Convert("dvd-ntsc", "16:9", testGUI);
        assertTrue(testConvert.init());
    }
    
    @Test
    public void testInitWithoutThread() {
        GUI testGUI = new GUI();
        testGUI.menuChkThread.setSelected(false);
        Convert testConvert = new Convert("dvd-ntsc", "16:9", testGUI);
        assertTrue(testConvert.init());
    }
    
    @Test
    public void testRun() {
        Convert testConvert = new Convert("dvd-ntsc", "16:9", new GUI());
        testConvert.run();
        
        // It's hard to test the actual functionality of the run method as it's calling Transcode internally
        // We can only test if it runs without errors
    }
    
    @Test
    public void testTranscode() {
        Convert testConvert = new Convert("dvd-ntsc", "16:9", new GUI());
        testConvert.Transcode();
        
        // Same as run(), difficult to test functionality but can test for exceptions and errors
        assertFalse(testConvert.m_Error);
    }
    
    @Test
    public void testMatrix() {
        Convert testConvert = new Convert("dvd-ntsc", "16:9", new GUI());
        testConvert.matrix();
        
        // Test if matrix method runs without exceptions
    }
    
    // More test cases can be added to cover edge cases and error scenarios
    
}