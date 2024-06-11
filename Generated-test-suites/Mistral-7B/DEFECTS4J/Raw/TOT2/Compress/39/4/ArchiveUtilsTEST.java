
import static org.junit.Assert.*;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.fail;

public class ArchiveUtilsTest {
    // test cases for methods and their basic functionality

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry("test.txt");
        String result = ArchiveUtils.toString(entry);
        assertEquals("-- 0 test.txt", result);
    }

    @Test
    public void testMatchAsciiBufferWithStringAndBuffer() {
        String expected = "test";
        byte[] buffer = {0x74, 0x65, 0x73, 0x74};
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, 4));
    }

    @Test
    public void testMatchAsciiBufferWithStringAndByteArray() {
        String expected = "test";
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, new byte[] {0x74, 0x65, 0x73, 0x74}));
    }

    @Test
    public void testToAsciiBytes() {
        String inputString = "test";
        byte[] result = ArchiveUtils.toAsciiBytes(inputString);
        assertArrayEquals(new byte[] {0x74, 0x65, 0x73, 0x74}, result);
    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes = {0x74, 0x65, 0x73, 0x74};
        String result = ArchiveUtils.toAsciiString(inputBytes);
        assertEquals("test", result);
    }

    @Test
    public void testToAsciiStringWithOffsetAndLength() {
        byte[] inputBytes = {0x74, 0x65, 0x73, 0x74};
        String result = ArchiveUtils.toAsciiString(inputBytes, 0, 4);
        assertEquals("test", result);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = {0x74, 0x65, 0x73, 0x74};
        byte[] buffer2 = {0x74, 0x65, 0x73, 0x74};
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
    }

    @Test
    public void testIsEqualWithIgnoreTrailingNulls() {
        byte[] buffer1 = {0x74, 0x65, 0x73, 0x74, 0};
        byte[] buffer2 = {0x74, 0x65, 0x73, 0x74};
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, true));
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = {0x74, 0x65, 0x73, 0x74};
        byte[] buffer2 = null;
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, 0));
    }

    @Test
    public void testIsArrayZero() {
        byte[] array = new byte[10];
        assertTrue(ArchiveUtils.isArrayZero(array, 5));
    }

    @Test
    public void testSanitize() {
        String input = "test♫";
        String result = ArchiveUtils.sanitize(input);
        assertEquals("test??", result);
    }

    // test cases for edge cases and exceptions

    @Test
    public void testToStringWithNullEntry() {
        assertNull(ArchiveUtils.toString(null));
    }

    @Test
    public void testToStringWithEmptyEntryName() {
        ArchiveEntry entry = new ArchiveEntry("");
        String result = ArchiveUtils.toString(entry);
        assertEquals("-- 0                 \n", result);
    }

    @Test
    public void testToStringWithLargeEntryName() {
        final int MAX_LENGTH = 1024 * 1024; // 1MB
        String longEntryName = StringUtils.repeat("A", MAX_LENGTH + 1);
        ArchiveEntry entry = new ArchiveEntry(longEntryName);
        String result = ArchiveUtils.toString(entry);
        // test the length of the output string
        // and check if the output string starts with '--' and ends with the entry name
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testMatchAsciiBufferWithInvalidEncoding() {
        String expected = "test";
        byte[] buffer = {0x74, 0x65, 0x73, 0x74};
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, 4));
        String invalidEncoding = "UTF-16BE";
        ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, 4, invalidEncoding);
    }

    @Test(expected = NullPointerException.class)
    public void testMatchAsciiBufferWithNullExpected() {
        byte[] buffer = {0x74, 0x65, 0x73, 0x74};
        assertFalse(ArchiveUtils.matchAsciiBuffer(null, buffer, 0, 4));
    }

    @Test(expected = NullPointerException.class)
    public void testMatchAsciiBufferWithNullBuffer() {
        String expected = "test";
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, null, 0, 4));
    }

    // test cases for other exception handling scenarios

    // ...
}