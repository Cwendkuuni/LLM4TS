import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ArchiveUtilsTest {

    private ArchiveEntry entry;

    @BeforeEach
    public void setUp() {
        entry = new ArchiveEntry();
    }

    @Test
    public void testToStringWithDirectoryEntry() {
        entry.setDirectory(true);
        entry.setSize(100);
        entry.setName("testfiles");

        String expected = "d     100 testfiles";
        String result = ArchiveUtils.toString(entry);

        assertEquals(expected, result);
    }

    @Test
    public void testToStringWithFileEntry() {
        entry.setDirectory(false);
        entry.setSize(2000);
        entry.setName("main.c");

        String expected = "-    2000 main.c";
        String result = ArchiveUtils.toString(entry);

        assertEquals(expected, result);
    }

    @Test
    public void testMatchAsciiBufferWithMatchingBuffers() {
        String expected = "Hello";
        byte[] buffer = expected.getBytes();
        int offset = 0;
        int length = buffer.length;

        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer, offset, length);

        assertTrue(result);
    }

    @Test
    public void testMatchAsciiBufferWithNonMatchingBuffers() {
        String expected = "Hello";
        byte[] buffer = "World".getBytes();
        int offset = 0;
        int length = buffer.length;

        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer, offset, length);

        assertFalse(result);
    }

    @Test
    public void testMatchAsciiBufferWithOffsetAndLength() {
        String expected = "Hello";
        byte[] buffer = "Hello, World!".getBytes();
        int offset = 0;
        int length = expected.length();

        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer, offset, length);

        assertTrue(result);
    }

    @Test
    public void testMatchAsciiBufferWithFullBuffer() {
        String expected = "Hello";
        byte[] buffer = expected.getBytes();

        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);

        assertTrue(result);
    }

    @Test
    public void testToAsciiBytes() {
        String inputString = "Hello";
        byte[] expectedBytes = inputString.getBytes();

        byte[] result = ArchiveUtils.toAsciiBytes(inputString);

        assertArrayEquals(expectedBytes, result);
    }

    @Test
    public void testToAsciiStringWithFullBuffer() {
        byte[] inputBytes = "Hello".getBytes();
        String expectedString = "Hello";

        String result = ArchiveUtils.toAsciiString(inputBytes);

        assertEquals(expectedString, result);
    }

    @Test
    public void testToAsciiStringWithOffsetAndLength() {
        byte[] inputBytes = "Hello, World!".getBytes();
        int offset = 0;
        int length = 5;
        String expectedString = "Hello";

        String result = ArchiveUtils.toAsciiString(inputBytes, offset, length);

        assertEquals(expectedString, result);
    }

    @Test
    public void testIsEqualWithEqualBuffers() {
        byte[] buffer1 = "Hello".getBytes();
        byte[] buffer2 = "Hello".getBytes();

        boolean result = ArchiveUtils.isEqual(buffer1, buffer2);

        assertTrue(result);
    }

    @Test
    public void testIsEqualWithNonEqualBuffers() {
        byte[] buffer1 = "Hello".getBytes();
        byte[] buffer2 = "World".getBytes();

        boolean result = ArchiveUtils.isEqual(buffer1, buffer2);

        assertFalse(result);
    }

    @Test
    public void testIsEqualWithOffsetAndLength() {
        byte[] buffer1 = "Hello, World!".getBytes();
        byte[] buffer2 = "Hello".getBytes();
        int offset1 = 0;
        int length1 = buffer2.length;
        int offset2 = 0;
        int length2 = buffer2.length;

        boolean result = ArchiveUtils.isEqual(buffer1, offset1, length1, buffer2, offset2, length2);

        assertTrue(result);
    }

    @Test
    public void testIsEqualWithIgnoreTrailingNulls() {
        byte[] buffer1 = "Hello\0\0\0".getBytes();
        byte[] buffer2 = "Hello".getBytes();

        boolean result = ArchiveUtils.isEqual(buffer1, buffer2, true);

        assertTrue(result);
    }

    @Test
    public void testIsEqualWithIgnoreTrailingNullsAndDifferentLengths() {
        byte[] buffer1 = "Hello\0\0\0".getBytes();
        byte[] buffer2 = "Hello".getBytes();

        boolean result = ArchiveUtils.isEqual(buffer1, buffer2, false);

        assertFalse(result);
    }

    @Test
    public void testIsArrayZeroWithZeroArray() {
        byte[] array = new byte[10];
        int size = array.length;

        boolean result = ArchiveUtils.isArrayZero(array, size);

        assertTrue(result);
    }

    @Test
    public void testIsArrayZeroWithNonZeroArray() {
        byte[] array = new byte[10];
        array[0] = 1;
        int size = array.length;

        boolean result = ArchiveUtils.isArrayZero(array, size);

        assertFalse(result);
    }

    @Test
    public void testSanitizeWithPrintableString() {
        String inputString = "Hello, World!";
        String expectedString = inputString;

        String result = ArchiveUtils.sanitize(inputString);

        assertEquals(expectedString, result);
    }

    @Test
    public void testSanitizeWithNonPrintableString() {
        String inputString = "Hello, \u0007World!";
        String expectedString = "Hello, ?World!";

        String result = ArchiveUtils.sanitize(inputString);

        assertEquals(expectedString, result);
    }
}