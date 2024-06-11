import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.ArchiveEntry;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry1 = new ArchiveEntry("main.c", 2000, false);
        assertEquals("-    2000 main.c", ArchiveUtils.toString(entry1));
        
        ArchiveEntry entry2 = new ArchiveEntry("testfiles", 100, true);
        assertEquals("d     100 testfiles", ArchiveUtils.toString(entry2));
        
        ArchiveEntry entry3 = new ArchiveEntry("file.txt", 0, false);
        assertEquals("-       0 file.txt", ArchiveUtils.toString(entry3));
    }

    @Test
    public void testMatchAsciiBuffer() {
        byte[] buffer1 = "Hello".getBytes();
        assertTrue(ArchiveUtils.matchAsciiBuffer("Hello", buffer1));

        byte[] buffer2 = "World".getBytes();
        assertFalse(ArchiveUtils.matchAsciiBuffer("Hello", buffer2));

        byte[] buffer3 = "null".getBytes();
        assertFalse(ArchiveUtils.matchAsciiBuffer("null", buffer3));

        byte[] buffer4 = "1234567890".getBytes();
        assertFalse(ArchiveUtils.matchAsciiBuffer("1234", buffer4, 0, 4));
        assertTrue(ArchiveUtils.matchAsciiBuffer("4567890", buffer4, 4, 7));
    }

    @Test
    public void testToAsciiBytes() {
        byte[] expected1 = "Hello".getBytes();
        assertArrayEquals(expected1, ArchiveUtils.toAsciiBytes("Hello"));

        byte[] expected2 = "World".getBytes();
        assertArrayEquals(expected2, ArchiveUtils.toAsciiBytes("World"));

        byte[] expected3 = "null".getBytes();
        assertArrayEquals(expected3, ArchiveUtils.toAsciiBytes("null"));

        byte[] expected4 = "1234567890".getBytes();
        assertArrayEquals(expected4, ArchiveUtils.toAsciiBytes("1234567890"));
    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes1 = "Hello".getBytes();
        assertEquals("Hello", ArchiveUtils.toAsciiString(inputBytes1));

        byte[] inputBytes2 = "World".getBytes();
        assertEquals("World", ArchiveUtils.toAsciiString(inputBytes2));

        byte[] inputBytes3 = "null".getBytes();
        assertEquals("null", ArchiveUtils.toAsciiString(inputBytes3));

        byte[] inputBytes4 = "1234567890".getBytes();
        assertEquals("1234567890", ArchiveUtils.toAsciiString(inputBytes4));
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = "Hello".getBytes();
        byte[] buffer2 = "Hello".getBytes();
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));

        byte[] buffer3 = "World".getBytes();
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer3));

        byte[] buffer4 = "null".getBytes();
        assertFalse(ArchiveUtils.isEqual(buffer2, buffer4));

        byte[] buffer5 = "1234567890".getBytes();
        assertFalse(ArchiveUtils.isEqual(buffer3, buffer5));
    }

    @Test
    public void testIsEqualWithIgnoreTrailingNulls() {
        byte[] buffer1 = "Hello".getBytes();
        byte[] buffer2 = "Hello\0\0\0".getBytes();
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, 5, buffer2, 0, 8, true));

        byte[] buffer3 = "World".getBytes();
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, 5, buffer3, 0, 5, true));

        byte[] buffer4 = "null\0\0".getBytes();
        assertFalse(ArchiveUtils.isEqual(buffer3, 0, 5, buffer4, 0, 6, true));

        byte[] buffer5 = "1234567890".getBytes();
        assertFalse(ArchiveUtils.isEqual(buffer4, 0, 4, buffer5, 0, 10, true));
    }

    @Test
    public void testIsArrayZero() {
        byte[] array1 = new byte[5];
        assertTrue(ArchiveUtils.isArrayZero(array1, 5));

        byte[] array2 = {0, 0, 0, 0};
        assertTrue(ArchiveUtils.isArrayZero(array2, 4));

        byte[] array3 = {0, 0, 0, 1};
        assertFalse(ArchiveUtils.isArrayZero(array3, 4));

        byte[] array4 = {1, 0, 0, 0};
        assertFalse(ArchiveUtils.isArrayZero(array4, 4));
    }

    @Test
    public void testSanitize() {
        String input1 = "Hello";
        assertEquals("Hello", ArchiveUtils.sanitize(input1));

        String input2 = "Wor\u0000ld";
        assertEquals("Wor?", ArchiveUtils.sanitize(input2));

        String input3 = "\u001F";
        assertEquals("?", ArchiveUtils.sanitize(input3));

        String input4 = "non-printable \u007F";
        assertEquals("non-printable ?", ArchiveUtils.sanitize(input4));
    }
}