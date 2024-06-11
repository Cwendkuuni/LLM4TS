import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry1 = new ArchiveEntry();
        entry1.setDirectory(true);
        entry1.setName("directory1");
        entry1.setSize(1000);
        String expected1 = "d    1000 directory1";
        assertEquals(expected1, ArchiveUtils.toString(entry1));
        
        ArchiveEntry entry2 = new ArchiveEntry();
        entry2.setDirectory(false);
        entry2.setName("file1");
        entry2.setSize(2000);
        String expected2 = "-    2000 file1";
        assertEquals(expected2, ArchiveUtils.toString(entry2));
    }

    @Test
    public void testMatchAsciiBuffer() {
        byte[] buffer1 = "abc".getBytes();
        assertTrue(ArchiveUtils.matchAsciiBuffer("abc", buffer1));

        byte[] buffer2 = "def".getBytes();
        assertFalse(ArchiveUtils.matchAsciiBuffer("abc", buffer2));
    }

    @Test
    public void testToAsciiBytes() {
        byte[] expected = "abc".getBytes();
        assertArrayEquals(expected, ArchiveUtils.toAsciiBytes("abc"));
    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes = "abc".getBytes();
        String expected = "abc";
        assertEquals(expected, ArchiveUtils.toAsciiString(inputBytes));
    }

    @Test
    public void testToAsciiStringWithOffsetAndLength() {
        byte[] inputBytes = "abcd".getBytes();
        int offset = 1;
        int length = 2;
        String expected = "bc";
        assertEquals(expected, ArchiveUtils.toAsciiString(inputBytes, offset, length));
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = "abc".getBytes();
        byte[] buffer2 = "abc".getBytes();
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
        
        byte[] buffer3 = "def".getBytes();
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer3));
    }

    @Test
    public void testIsEqualWithIgnoreTrailingNulls() {
        byte[] buffer1 = "abc".getBytes();
        byte[] buffer2 = "abcd\0\0\0".getBytes();
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, true));

        byte[] buffer3 = "abc\0\0\0".getBytes();
        byte[] buffer4 = "abc".getBytes();
        assertTrue(ArchiveUtils.isEqual(buffer3, buffer4, true));

        byte[] buffer5 = "abc".getBytes();
        byte[] buffer6 = "abcd".getBytes();
        assertFalse(ArchiveUtils.isEqual(buffer5, buffer6, true));
    }

    @Test
    public void testIsEqualWithOffsetAndLength() {
        byte[] buffer1 = "abc".getBytes();
        byte[] buffer2 = "efgabcxyz".getBytes();
        int offset1 = 3;
        int length1 = 3;
        int offset2 = 3;
        int length2 = 3;
        assertTrue(ArchiveUtils.isEqual(buffer1, offset1, length1, buffer2, offset2, length2));
        
        byte[] buffer3 = "abc".getBytes();
        byte[] buffer4 = "efgdefxyz".getBytes();
        int offset3 = 0;
        int length3 = 3;
        int offset4 = 3;
        int length4 = 3;
        assertFalse(ArchiveUtils.isEqual(buffer3, offset3, length3, buffer4, offset4, length4));
    }

    @Test
    public void testIsEqualWithArrayZero() {
        byte[] buffer1 = "abcd\0\0\0\0".getBytes();
        int size1 = 4;
        assertTrue(ArchiveUtils.isArrayZero(buffer1, size1));

        byte[] buffer2 = "abcde\0\0\0\0".getBytes();
        int size2 = 5;
        assertFalse(ArchiveUtils.isArrayZero(buffer2, size2));
    }

    @Test
    public void testSanitize() {
        String input1 = "abc";
        String expected1 = "abc";
        assertEquals(expected1, ArchiveUtils.sanitize(input1));

        String input2 = "abc\n";
        String expected2 = "abc?";
        assertEquals(expected2, ArchiveUtils.sanitize(input2));

        String input3 = "abc\u0003";
        String expected3 = "abc?";
        assertEquals(expected3, ArchiveUtils.sanitize(input3));
    }
}