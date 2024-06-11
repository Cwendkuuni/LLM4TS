import static org.junit.Assert.*;
import java.io.UnsupportedEncodingException;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry1 = new ArchiveEntry("file1.txt", false, 1000);
        ArchiveEntry entry2 = new ArchiveEntry("folder1", true, 0);
        
        assertEquals("-    1000 file1.txt", ArchiveUtils.toString(entry1));
        assertEquals("d       0 folder1", ArchiveUtils.toString(entry2));
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "test";
        byte[] buffer = "test".getBytes();

        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    @Test
    public void testMatchAsciiBuffer_withOffsetAndLength() {
        String expected = "world";
        byte[] buffer = "hello world".getBytes();

        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer, 6, 5));
    }

    @Test
    public void testToAsciiBytes() {
        String inputString = "hello";
        byte[] expected = "hello".getBytes();

        assertArrayEquals(expected, ArchiveUtils.toAsciiBytes(inputString));
    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes = "world".getBytes();
        String expected = "world";

        assertEquals(expected, ArchiveUtils.toAsciiString(inputBytes));
    }

    @Test
    public void testToAsciiString_withOffsetAndLength() {
        byte[] inputBytes = "hello world".getBytes();
        String expected = "world";

        assertEquals(expected, ArchiveUtils.toAsciiString(inputBytes, 6, 5));
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = "hello".getBytes();
        byte[] buffer2 = "hello".getBytes();

        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
    }

    @Test
    public void testIsEqual_withOffsetAndLength() {
        byte[] buffer1 = "hello world".getBytes();
        byte[] buffer2 = "world".getBytes();

        assertTrue(ArchiveUtils.isEqual(buffer1, 6, 5, buffer2, 0, 5));
    }

    @Test
    public void testIsEqual_withIgnoreTrailingNulls() {
        byte[] buffer1 = "hello\0\0\0".getBytes();
        byte[] buffer2 = "hello".getBytes();

        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, true));
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = "hello\0\0\0".getBytes();
        byte[] buffer2 = "hello".getBytes();

        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, 8, buffer2, 0, 5));
    }

    @Test
    public void testIsArrayZero() {
        byte[] array = new byte[5];

        assertTrue(ArchiveUtils.isArrayZero(array, 5));
    }

    @Test
    public void testIsArrayZero_withNonZeroBytes() {
        byte[] array = {0, 1, 2, 3, 4};

        assertFalse(ArchiveUtils.isArrayZero(array, 5));
    }

    @Test
    public void testSanitize() {
        String input = "hello\r\nworld\\0\0\0";
        String expected = "hello?world\\0\0\0";

        assertEquals(expected, ArchiveUtils.sanitize(input));
    }

}