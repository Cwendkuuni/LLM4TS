import static org.junit.Assert.*;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        // Test case 1: Entry is a file
        ArchiveEntry entry1 = new ArchiveEntry();
        entry1.setName("file.txt");
        entry1.setDirectory(false);
        entry1.setSize(2000);
        String expected1 = "-    2000 file.txt";
        assertEquals(expected1, ArchiveUtils.toString(entry1));
        
        // Test case 2: Entry is a directory
        ArchiveEntry entry2 = new ArchiveEntry();
        entry2.setName("directory");
        entry2.setDirectory(true);
        entry2.setSize(100);
        String expected2 = "d     100 directory";
        assertEquals(expected2, ArchiveUtils.toString(entry2));
    }

    @Test
    public void testMatchAsciiBuffer() {
        // Test case 1: Expected string matches buffer
        String expected1 = "Hello";
        byte[] buffer1 = "Hello".getBytes();
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected1, buffer1));
        
        // Test case 2: Expected string does not match buffer
        String expected2 = "Goodbye";
        byte[] buffer2 = "Hello".getBytes();
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected2, buffer2));
    }

    @Test
    public void testToAsciiBytes() {
        // Test case 1: Convert string to Ascii bytes
        String inputString1 = "Hello";
        byte[] expected1 = {72, 101, 108, 108, 111};
        assertArrayEquals(expected1, ArchiveUtils.toAsciiBytes(inputString1));
        
        // Test case 2: Convert empty string to Ascii bytes
        String inputString2 = "";
        byte[] expected2 = {};
        assertArrayEquals(expected2, ArchiveUtils.toAsciiBytes(inputString2));
    }

    @Test
    public void testToAsciiString() {
        // Test case 1: Convert bytes to Ascii string
        byte[] inputBytes1 = {72, 101, 108, 108, 111};
        String expected1 = "Hello";
        assertEquals(expected1, ArchiveUtils.toAsciiString(inputBytes1));
        
        // Test case 2: Convert empty bytes to Ascii string
        byte[] inputBytes2 = {};
        String expected2 = "";
        assertEquals(expected2, ArchiveUtils.toAsciiString(inputBytes2));
    }

    @Test
    public void testToAsciiStringWithOffsetAndLength() {
        // Test case 1: Convert bytes to Ascii string with offset and length
        byte[] inputBytes1 = {72, 101, 108, 108, 111, 33, 33, 33};
        int offset1 = 1;
        int length1 = 5;
        String expected1 = "ello!";
        assertEquals(expected1, ArchiveUtils.toAsciiString(inputBytes1, offset1, length1));
        
        // Test case 2: Convert empty bytes to Ascii string with offset and length
        byte[] inputBytes2 = {};
        int offset2 = 0;
        int length2 = 0;
        String expected2 = "";
        assertEquals(expected2, ArchiveUtils.toAsciiString(inputBytes2, offset2, length2));
    }

    @Test
    public void testIsEqual() {
        // Test case 1: Compare equal byte buffers
        byte[] buffer1 = {72, 101, 108, 108, 111};
        byte[] buffer2 = {72, 101, 108, 108, 111};
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
        
        // Test case 2: Compare different byte buffers
        byte[] buffer3 = {72, 101, 108, 108, 111};
        byte[] buffer4 = {104, 101, 108, 108, 111};
        assertFalse(ArchiveUtils.isEqual(buffer3, buffer4));
    }

    @Test
    public void testIsEqualWithIgnoreTrailingNulls() {
        // Test case 1: Compare byte buffers ignoring trailing nulls
        byte[] buffer1 = {72, 101, 108, 108, 111, 0, 0, 0};
        byte[] buffer2 = {72, 101, 108, 108, 111};
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, true));
        
        // Test case 2: Compare byte buffers without ignoring trailing nulls
        byte[] buffer3 = {72, 101, 108, 108, 111, 0, 0, 0};
        byte[] buffer4 = {72, 101, 108, 108, 111};
        assertFalse(ArchiveUtils.isEqual(buffer3, buffer4, false));
    }
    
    @Test
    public void testIsArrayZero() {
        // Test case 1: Array is zero
        byte[] a1 = {0, 0, 0, 0, 0};
        int size1 = 5;
        assertTrue(ArchiveUtils.isArrayZero(a1, size1));
        
        // Test case 2: Array is not zero
        byte[] a2 = {0, 0, 0, 0, 1};
        int size2 = 5;
        assertFalse(ArchiveUtils.isArrayZero(a2, size2));
    }
    
    @Test
    public void testSanitize() {
        // Test case 1: Sanitize a string with non-printable characters
        String s1 = "Hello\tWorld\n!";
        String expected1 = "Hello?World?";
        assertEquals(expected1, ArchiveUtils.sanitize(s1));
        
        // Test case 2: Sanitize an empty string
        String s2 = "";
        String expected2 = "";
        assertEquals(expected2, ArchiveUtils.sanitize(s2));
    }
}