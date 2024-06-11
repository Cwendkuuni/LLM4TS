import static org.junit.Assert.*;

import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        // typical use case
        ArchiveEntry entry = new ArchiveEntry();
        entry.setName("testFile");
        entry.setSize(100);
        entry.setDirectory(false);
        String expected = "-    100 testFile";
        assertEquals(expected, ArchiveUtils.toString(entry));
        
        // edge case
        entry.setName("testDirectory");
        entry.setSize(200);
        entry.setDirectory(true);
        expected = "d    200 testDirectory";
        assertEquals(expected, ArchiveUtils.toString(entry));
    }

    @Test
    public void testMatchAsciiBuffer() {
        // typical use case
        String expected = "test";
        byte[] buffer = "test".getBytes();
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
        
        // edge case
        expected = "test123";
        buffer = "test".getBytes();
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    @Test
    public void testToAsciiBytes() {
        // typical use case
        String inputString = "abc";
        byte[] expected = {'a', 'b', 'c'};
        assertArrayEquals(expected, ArchiveUtils.toAsciiBytes(inputString));
        
        // edge case
        inputString = "";
        expected = new byte[0];
        assertArrayEquals(expected, ArchiveUtils.toAsciiBytes(inputString));
    }

    // Add test cases for the remaining methods
    
}