import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveUtilsTest {
    
    @Test
    public void testToString() {
        // Typical case
        ArchiveEntry entry = new ArchiveEntry("example.txt", 100, false);
        String expected = "-    100 example.txt";
        assertEquals(expected, ArchiveUtils.toString(entry));
        
        // Edge case
        ArchiveEntry entry2 = new ArchiveEntry("longfilename.txt", Long.MAX_VALUE, true);
        String expected2 = "d     9223372036854775807 longfilename.txt";
        assertEquals(expected2, ArchiveUtils.toString(entry2));
    }
    
    @Test
    public void testMatchAsciiBuffer() {
        // Typical case
        String expected = "test";
        byte[] buffer = expected.getBytes();
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
        
        // Edge case
        String expected2 = "test";
        byte[] buffer2 = "testcase".getBytes();
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected2, buffer2));
    }
    
    @Test
    public void testToAsciiBytes() {
        // Typical case
        String inputString = "hello";
        byte[] expectedBytes = {104, 101, 108, 108, 111};
        assertArrayEquals(expectedBytes, ArchiveUtils.toAsciiBytes(inputString));
        
        // Edge case
        String inputString2 = "";
        byte[] expectedBytes2 = {};
        assertArrayEquals(expectedBytes2, ArchiveUtils.toAsciiBytes(inputString2));
    }
    
    // Add more test methods here for the other public methods in ArchiveUtils
    
}