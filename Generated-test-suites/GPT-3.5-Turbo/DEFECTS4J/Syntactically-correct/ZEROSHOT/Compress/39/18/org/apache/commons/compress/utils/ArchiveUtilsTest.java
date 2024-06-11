import org.junit.*;
import static org.junit.Assert.*;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        // Test case 1: Directory entry
        ArchiveEntry entry1 = new ArchiveEntry(true, 100, "testdir");
        String expectedResult1 = "d     100 testdir";
        String result1 = ArchiveUtils.toString(entry1);
        assertEquals(expectedResult1, result1);

        // Test case 2: File entry
        ArchiveEntry entry2 = new ArchiveEntry(false, 2000, "main.c");
        String expectedResult2 = "-    2000 main.c";
        String result2 = ArchiveUtils.toString(entry2);
        assertEquals(expectedResult2, result2);
    }

    @Test
    public void testMatchAsciiBuffer() {
        // Test case 1: Match
        String expected1 = "Hello World";
        byte[] buffer1 = expected1.getBytes();
        boolean result1 = ArchiveUtils.matchAsciiBuffer(expected1, buffer1);
        assertTrue(result1);

        // Test case 2: No match
        String expected2 = "Hello World";
        byte[] buffer2 = "Wrong".getBytes();
        boolean result2 = ArchiveUtils.matchAsciiBuffer(expected2, buffer2);
        assertFalse(result2);
    }

    @Test
    public void testToAsciiBytes() {
        String inputString = "Hello World";
        byte[] expectedResult = inputString.getBytes();
        byte[] result = ArchiveUtils.toAsciiBytes(inputString);
        assertArrayEquals(expectedResult, result);
    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes = "Hello World".getBytes();
        String expectedResult = "Hello World";
        String result = ArchiveUtils.toAsciiString(inputBytes);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testToAsciiStringWithOffsetAndLength() {
        byte[] inputBytes = "Hello World".getBytes();
        int offset = 0;
        int length = 5;
        String expectedResult = "Hello";
        String result = ArchiveUtils.toAsciiString(inputBytes, offset, length);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = "Hello World".getBytes();
        byte[] buffer2 = "Hello World".getBytes();
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2);
        assertTrue(result);
    }

    @Test
    public void testIsEqualWithOffsetAndLength() {
        byte[] buffer1 = "Hello World".getBytes();
        byte[] buffer2 = "Hello Universe Today".getBytes();
        int offset1 = 0;
        int offset2 = 0;
        int length1 = 5;
        int length2 = 5;
        boolean result = ArchiveUtils.isEqual(buffer1, offset1, length1, buffer2, offset2, length2);
        assertTrue(result);
    }

    @Test
    public void testIsEqualWithIgnoreTrailingNulls() {
        byte[] buffer1 = "Hello".getBytes();
        byte[] buffer2 = "Hello\0\0\0".getBytes();
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2, true);
        assertTrue(result);
    }

    @Test
    public void testIsEqualWithIgnoreTrailingNullsFalse() {
        byte[] buffer1 = "Hello".getBytes();
        byte[] buffer2 = "Hello\0\0\0".getBytes();
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2, false);
        assertFalse(result);
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = "Hello World".getBytes();
        byte[] buffer2 = "Hello World".getBytes();
        boolean result = ArchiveUtils.isEqual(buffer1, buffer2, true);
        assertTrue(result);
    }

    @Test
    public void testIsEqualWithNullAndOffsetAndLength() {
        byte[] buffer1 = "Hello World".getBytes();
        byte[] buffer2 = "Hello World".getBytes();
        int offset1 = 0;
        int offset2 = 0;
        int length1 = 5;
        int length2 = 5;
        boolean result = ArchiveUtils.isEqual(buffer1, offset1, length1, buffer2, offset2, length2, true);
        assertTrue(result);
    }

    @Test
    public void testIsArrayZero() {
        byte[] array = "Hello World".getBytes();
        int size = 5;
        boolean result = ArchiveUtils.isArrayZero(array, size);
        assertFalse(result);
    }

    @Test
    public void testSanitize() {
        String input = "Hello World";
        String expectedResult = "Hello World";
        String result = ArchiveUtils.sanitize(input);
        assertEquals(expectedResult, result);
    }

}