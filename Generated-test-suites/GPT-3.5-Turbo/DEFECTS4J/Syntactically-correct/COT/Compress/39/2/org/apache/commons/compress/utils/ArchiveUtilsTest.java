import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveEntry;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry(); // Replace with actual ArchiveEntry object
        String expected = "-    2000 main.c\nd     100 testfiles";
        String result = ArchiveUtils.toString(entry);
        assertEquals(expected, result);
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "Hello";
        byte[] buffer = "Hello".getBytes();
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    @Test
    public void testToAsciiBytes() {
        String inputString = "Hello";
        byte[] expected = "Hello".getBytes();
        byte[] result = ArchiveUtils.toAsciiBytes(inputString);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes = "Hello".getBytes();
        String expected = "Hello";
        String result = ArchiveUtils.toAsciiString(inputBytes);
        assertEquals(expected, result);
    }

    @Test
    public void testToAsciiStringWithOffsetAndLength() {
        byte[] inputBytes = "Hello World".getBytes();
        int offset = 6;
        int length = 5;
        String expected = "World";
        String result = ArchiveUtils.toAsciiString(inputBytes, offset, length);
        assertEquals(expected, result);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = "Hello".getBytes();
        byte[] buffer2 = "Hello".getBytes();
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
    }

    @Test
    public void testIsEqualWithIgnoreTrailingNulls() {
        byte[] buffer1 = "Hello".getBytes();
        byte[] buffer2 = "Hello\0\0".getBytes();
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, true));
    }

    @Test
    public void testIsEqualWithOffsetAndLength() {
        byte[] buffer1 = "Hello World".getBytes();
        byte[] buffer2 = "World".getBytes();
        int offset1 = 6;
        int length1 = 5;
        int offset2 = 0;
        int length2 = 5;
        assertTrue(ArchiveUtils.isEqual(buffer1, offset1, length1, buffer2, offset2, length2));
    }

    @Test
    public void testIsEqualWithOffsetAndLengthAndIgnoreTrailingNulls() {
        byte[] buffer1 = "Hello World".getBytes();
        byte[] buffer2 = "World\0\0".getBytes();
        int offset1 = 6;
        int length1 = 5;
        int offset2 = 0;
        int length2 = 5;
        assertTrue(ArchiveUtils.isEqual(buffer1, offset1, length1, buffer2, offset2, length2, true));
    }

    @Test
    public void testIsArrayZero() {
        byte[] a = {0, 0, 0};
        int size = 3;
        assertTrue(ArchiveUtils.isArrayZero(a, size));
    }

    @Test
    public void testSanitize() {
        String s = "Hello\nWorld";
        String expected = "Hello?World";
        String result = ArchiveUtils.sanitize(s);
        assertEquals(expected, result);
    }

}