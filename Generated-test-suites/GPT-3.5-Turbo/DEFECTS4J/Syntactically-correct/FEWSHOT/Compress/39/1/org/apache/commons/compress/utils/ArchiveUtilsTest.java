import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        // Test when entry is a file
        String expected = "-     2000 main.c";
        ArchiveEntry entry = new ArchiveEntry("main.c", 2000, false);
        assertEquals(expected, ArchiveUtils.toString(entry));

        // Test when entry is a directory
        expected = "d      100 testfiles";
        entry = new ArchiveEntry("testfiles", 100, true);
        assertEquals(expected, ArchiveUtils.toString(entry));
    }

    @Test
    public void testMatchAsciiBuffer() {
        // Test when buffer matches expected string
        String expected = "hello";
        byte[] buffer = expected.getBytes();
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));

        // Test when buffer does not match expected string
        expected = "world";
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    @Test
    public void testToAsciiBytes() {
        // Test when inputString is a valid ASCII string
        String inputString = "hello";
        byte[] expected = inputString.getBytes();
        assertArrayEquals(expected, ArchiveUtils.toAsciiBytes(inputString));

        // Test when inputString contains non-ASCII characters
        inputString = "こんにちは";
        expected = new byte[]{63};
        assertArrayEquals(expected, ArchiveUtils.toAsciiBytes(inputString));
    }

    @Test
    public void testToAsciiString() {
        // Test when inputBytes is a valid ASCII byte array
        byte[] inputBytes = "hello".getBytes();
        String expected = new String(inputBytes);
        assertEquals(expected, ArchiveUtils.toAsciiString(inputBytes));

        // Test when inputBytes contains non-ASCII bytes
        inputBytes = new byte[]{63};
        expected = "?";
        assertEquals(expected, ArchiveUtils.toAsciiString(inputBytes));
    }

    @Test
    public void testToAsciiStringWithOffsetAndLength() {
        // Test when inputBytes is a valid ASCII byte array
        byte[] inputBytes = "hello".getBytes();
        int offset = 0;
        int length = inputBytes.length;
        String expected = new String(inputBytes, offset, length);
        assertEquals(expected, ArchiveUtils.toAsciiString(inputBytes, offset, length));

        // Test when inputBytes contains non-ASCII bytes
        inputBytes = new byte[]{63};
        offset = 0;
        length = inputBytes.length;
        expected = "?";
        assertEquals(expected, ArchiveUtils.toAsciiString(inputBytes, offset, length));
    }

    @Test
    public void testIsEqual() {
        // Test when buffer1 and buffer2 have same contents
        byte[] buffer1 = "hello".getBytes();
        byte[] buffer2 = "hello".getBytes();
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));

        // Test when buffer1 and buffer2 have different contents
        buffer2 = "world".getBytes();
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer2));
    }

    @Test
    public void testIsEqualWithOffsetAndLength() {
        // Test when buffer1 and buffer2 have same contents
        byte[] buffer1 = "hello".getBytes();
        byte[] buffer2 = "hello".getBytes();
        int offset = 0;
        int length = buffer1.length;
        assertTrue(ArchiveUtils.isEqual(buffer1, offset, length, buffer2, offset, length));

        // Test when buffer1 and buffer2 have different contents
        buffer2 = "world".getBytes();
        assertFalse(ArchiveUtils.isEqual(buffer1, offset, length, buffer2, offset, length));
    }

    @Test
    public void testIsEqualIgnoreTrailingNulls() {
        // Test when buffer1 and buffer2 have same contents
        byte[] buffer1 = "hello".getBytes();
        byte[] buffer2 = "hello".getBytes();
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, true));

        // Test when buffer1 and buffer2 have different contents
        buffer2 = "world".getBytes();
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer2, true));
    }

    @Test
    public void testIsEqualWithNull() {
        // Test when buffer1 and buffer2 have same contents
        byte[] buffer1 = "hello".getBytes();
        byte[] buffer2 = "hello".getBytes();
        int offset = 0;
        int length = buffer1.length;
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, offset, length, buffer2, offset, length));

        // Test when buffer1 and buffer2 have different contents
        buffer2 = "world".getBytes();
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, offset, length, buffer2, offset, length));
    }

    @Test
    public void testIsArrayZero() {
        // Test when first N bytes of array are zero
        byte[] a = new byte[]{0, 0, 0, 0};
        int size = 4;
        assertTrue(ArchiveUtils.isArrayZero(a, size));

        // Test when first N bytes of array are not zero
        a = new byte[]{0, 0, 0, 1};
        assertFalse(ArchiveUtils.isArrayZero(a, size));
    }

    @Test
    public void testSanitize() {
        // Test when string does not contain non-printable characters
        String s = "hello";
        assertEquals(s, ArchiveUtils.sanitize(s));

        // Test when string contains non-printable characters
        s = "hello\tworld";
        String expected = "hello?world";
        assertEquals(expected, ArchiveUtils.sanitize(s));
    }
}