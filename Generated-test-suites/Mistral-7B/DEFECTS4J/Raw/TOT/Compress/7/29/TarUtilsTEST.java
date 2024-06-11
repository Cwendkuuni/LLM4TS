package org.apache.commons.compress.archivers.tar.test;

import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests for the TarUtils class.
 */
public class TarUtilsTest {

    /**
     * Tests parsing of octal strings with valid input.
     */
    @Test
    public void testParseOctalValidInput() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(0123457L, result);

        byte[] buffer1 = new byte[]{'0', '1', '2', '3', '4', '7'};
        long result1 = TarUtils.parseOctal(buffer1, 0, buffer1.length);
        assertEquals(012347L, result1);

        byte[] buffer2 = new byte[]{'0', '1', '2', '0', '1', '2'};
        long result2 = TarUtils.parseOctal(buffer2, 0, buffer2.length);
        assertEquals(012012L, result2);
    }

    /**
     * Tests parsing of octal strings with invalid input.
     */
    @Test
    public void testParseOctalInvalidInput() {
        byte[] buffer = new byte[]{'0', '1', '2', 'a', '4', '5', '6', '7'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer, 0, buffer.length));

        byte[] buffer1 = new byte[]{'0', '1', '2', '3', '4', 'x', '5', '7'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer1, 0, buffer1.length));

        byte[] buffer2 = new byte[]{'0', '1', '2', '3', '4', '7', '8'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer2, 0, buffer2.length));
    }

    /**
     * Tests parsing of entry names with valid input.
     */
    @Test
    public void testParseNameValidInput() {
        byte[] buffer = new byte[]{'.'.byteValue(), 'a', '.', 'b', '.', 'c', '.', 'd', 0};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("abc.d", result);

        byte[] buffer1 = new byte[]{'/', 'a', '/', 'b', '/', 'c', '/', 'd', 0};
        String result1 = TarUtils.parseName(buffer1, 0, buffer1.length);
        assertEquals("ab/c/d", result1);

        byte[] buffer2 = new byte[]{'/', '.', '/', 'b', '/', 'c', '/', 'd', 0};
        String result2 = TarUtils.parseName(buffer2, 0, buffer2.length);
        assertEquals("b/c/d", result2);
    }

    /**
     * Tests parsing of entry names with invalid input.
     */
    @Test
    public void testParseNameInvalidInput() {
        byte[] buffer = new byte[]{'/', 'a', '.', 'b', '.', '/', 0};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseName(buffer, 0, buffer.length));

        byte[] buffer1 = new byte[]{'/', 'a', '.', 'b', '.', '.', '.'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseName(buffer1, 0, buffer1.length));

        byte[] buffer2 = new byte[]{'/', 'a', '.', 'b', '.', 'd'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseName(buffer2, 0, buffer2.length));
    }

    /**
     * Tests formatting of a name into a buffer with valid input.
     */
    @Test
    public void testFormatNameBytesValidInput() {
        String name = "abc.def";
        byte[] buffer = new byte[name.length() + 1];
        int result = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals(name.length() + 1, result);
        assertArrayEquals(name.getBytes(), Arrays.copyOfRange(buffer, 0, result));
    }

    /**
     * Tests formatting of a name into a buffer with invalid input.
     */
    @Test
    public void testFormatNameBytesInvalidInput() {
        String name = "/abc/def";
        byte[] buffer = new byte[name.length() + 1];
        assertThrows(IllegalArgumentException.class, () -> TarUtils.formatNameBytes(name, buffer, 0, buffer.length));
    }

    /**
     * Tests formatting of an unsigned octal number into a buffer with valid input.
     */
    @Test
    public void testFormatUnsignedOctalStringValidInput() {
        long value = 12345;
        byte[] buffer = new byte[8];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '2', '3', '4', '5', '7', '7'}, buffer);
    }

    /**
     * Tests formatting of an unsigned octal number into a buffer with invalid input.
     */
    @Test
    public void testFormatUnsignedOctalStringInvalidInput() {
        long value = Long.MAX_VALUE + 1;
        byte[] buffer = new byte[8];
        assertThrows(IllegalArgumentException.class, () -> TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length));
    }

    /**
     * Tests writing an octal integer into a buffer with valid input.
     */
    @Test
    public void testFormatOctalBytesValidInput() {
        long value = 12345;
        byte[] buffer = new byte[10];
        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '2', '3', '4', '5', '7', '7', ' ', '0'}, buffer);
    }

    /**
     * Tests writing an octal integer into a buffer with invalid input.
     */
    @Test
    public void testFormatOctalBytesInvalidInput() {
        long value = Long.MAX_VALUE + 1;
        byte[] buffer = new byte[10];
        assertThrows(IllegalArgumentException.class, () -> TarUtils.formatOctalBytes(value, buffer, 0, buffer.length));
    }

    /**
     * Tests writing an octal long integer into a buffer with valid input.
     */
    @Test
    public void testFormatLongOctalBytesValidInput() {
        long value = 1234567890123L;
        byte[] buffer = new byte[12];
        TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1'}, buffer);
    }

    /**
     * Tests writing an octal long integer into a buffer with invalid input.
     */
    @Test
    public void testFormatLongOctalBytesInvalidInput() {
        long value = Long.MAX_VALUE + 1;
        byte[] buffer = new byte[12];
        assertThrows(IllegalArgumentException.class, () -> TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length));
    }

    /**
     * Tests writing an octal value into a buffer with valid input.
     */
    @Test
    public void testFormatCheckSumOctalBytesValidInput() {
        long value = 1234567890123L;
        byte[] buffer = new byte[12];
        TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', ' '}, buffer);
    }

    /**
     * Tests writing an octal value into a buffer with invalid input.
     */
    @Test
    public void testFormatCheckSumOctalBytesInvalidInput() {
        long value = Long.MAX_VALUE + 1;
        byte[] buffer = new byte[12];
        assertThrows(IllegalArgumentException.class, () -> TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length));
    }

    /**
     * Tests computing the checksum of a tar entry header with valid input.
     */
    @Test
    public void testComputeCheckSumValidInput() {
        byte[] buffer = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};
        long result = TarUtils.computeCheckSum(buffer);
        long expected = 0x0102030405060708L;
        assertEquals(expected, result);
    }

    /**
     * Tests computing the checksum of a tar entry header with invalid input.
     */
    @Test
    public void testComputeCheckSumInvalidInput() {
        byte[] buffer = new byte[]{0};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.computeCheckSum(buffer));
    }
}