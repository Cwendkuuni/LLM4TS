import static org.junit.Assert.*;
import org.junit.Test;

public class ConvertTest {
   
    // Test cases for the first constructor
    @Test
    public void testConstructor1() {
        GUI gui = new GUI();
        Convert convert = new Convert("dvd-ntsc", "16:9", gui);
        assertEquals("8500", convert.m_bitrate);
        assertEquals("dvd-ntsc", convert.m_Format);
        assertEquals("16:9", convert.m_AspectRatio);
        assertEquals(29.97, convert.m_fps, 0.001);
    }
    
    @Test
    public void testInit() {
        GUI gui = new GUI();
        Convert convert = new Convert("dvd-ntsc", "16:9", gui);
        assertFalse(convert.init()); // Assuming the GUI object does not have menuChkThread selected
    }
    
    // Test cases for the second constructor
    @Test
    public void testConstructor2() {
        GUI gui = new GUI();
        Convert convert = new Convert(1, "dvd-ntsc", "4:3", gui);
        assertEquals(8500, convert.m_bitrate);
        assertEquals("dvd-ntsc", convert.m_Format);
        assertEquals("4:3", convert.m_AspectRatio);
        assertEquals(29.97, convert.m_fps, 0.001);
    }
    
    // Test case for the matrix method
    @Test
    public void testMatrix() {
        GUI gui = new GUI();
        Convert convert = new Convert(1, "dvd-ntsc", "4:3", gui);
        convert.matrix(); 
        // Add assertions to check if the file "matrix.txt" is created successfully with the expected content
    }

    // Feel free to add more test cases for other methods if needed
    
    // Edge cases and exception handling scenarios should also be considered in additional test cases
    
}