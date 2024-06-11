import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry();
        entry.setName("test");
        entry.setDirectory(false);
        entry.setSize(2000);
        
        String expected = "-    2000 test";
        String result = ArchiveUtils.toString(entry);
        
        assertEquals(expected, result);
    }

    @Test
    public void testMatchAsciiBuffer_withValidInput() {
        String expected = "test";
        byte[] buffer = null;
        try {
            buffer = expected.getBytes("US-ASCII");
        } catch (UnsupportedEncodingException e) {
            fail("Failed to encode string to ASCII byte array");
        }

        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        assertTrue(result);
    }

    @Test
    public void testMatchAsciiBuffer_withInvalidInput() {
        String expected = "test";
        byte[] buffer = null;
        try {
            buffer = expected.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            fail("Failed to encode string to UTF-8 byte array");
        }

        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        assertFalse(result);
    }

    @Test
    public void testToAsciiBytes() {
        String inputString = "test";
        byte[] expected = null;
        try {
            expected = inputString.getBytes("US-ASCII");
        } catch (UnsupportedEncodingException e) {
            fail("Failed to encode string to ASCII byte array");
        }

        byte[] result = ArchiveUtils.toAsciiBytes(inputString);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testToAsciiString_withValidInput() {
        byte[] inputBytes = {116, 101, 115, 116};
        String expected = "test";

        String result = ArchiveUtils.toAsciiString(inputBytes);
        assertEquals(expected, result);
    }

    @Test
    public void testToAsciiString_withInvalidInput() {
        byte[] inputBytes = {116, 101, 115, -1, 116};
        String expected = "tes?t";

        String result = ArchiveUtils.toAsciiString(inputBytes);
        assertEquals(expected, result);
    }

    @Test
    public void testIsEqual_withEqualBuffers() {
        byte[] buffer1 = {1, 2, 3, 4};
        byte[] buffer2 = {1, 2, 3, 4};

        boolean result = ArchiveUtils.isEqual(buffer1, buffer2);
        assertTrue(result);
    }

    @Test
    public void testIsEqual_withUnequalBuffers() {
        byte[] buffer1 = {1, 2, 3, 4};
        byte[] buffer2 = {4, 3, 2, 1};

        boolean result = ArchiveUtils.isEqual(buffer1, buffer2);
        assertFalse(result);
    }

    @Test
    public void testIsEqualWithNull_withEqualBuffers() {
        byte[] buffer1 = {1, 2, 3, 4};
        byte[] buffer2 = {1, 2, 3, 4};

        boolean result = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        assertTrue(result);
    }

    @Test
    public void testIsEqualWithNull_withUnequalBuffers() {
        byte[] buffer1 = {1, 2, 3, 4};
        byte[] buffer2 = {4, 3, 2, 1};

        boolean result = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        assertFalse(result);
    }

    @Test
    public void testIsEqualWithNull_withInvalidLength() {
        byte[] buffer1 = {1, 2, 3, 4};
        byte[] buffer2 = {1, 2};

        boolean result = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        assertFalse(result);
    }

    @Test
    public void testIsArrayZero_withZeroArray() {
        byte[] array = {0, 0, 0, 0};

        boolean result = ArchiveUtils.isArrayZero(array, array.length);
        assertTrue(result);
    }

    @Test
    public void testIsArrayZero_withNonZeroArray() {
        byte[] array = {0, 0, 1, 0};

        boolean result = ArchiveUtils.isArrayZero(array, array.length);
        assertFalse(result);
    }

    @Test
    public void testSanitize_withValidInput() {
        String input = "test";
        String expected = "test";

        String result = ArchiveUtils.sanitize(input);
        assertEquals(expected, result);
    }

    @Test
    public void testSanitize_withNonPrintableCharacters() {
        String input = "\u0001test\u0007";
        String expected = "?test?";

        String result = ArchiveUtils.sanitize(input);
        assertEquals(expected, result);
    }

    @Test
    public void testSanitize_withLongString() {
        String input = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz";
        String expected = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwx";

        String result = ArchiveUtils.sanitize(input);
        assertEquals(expected, result);
    }

}