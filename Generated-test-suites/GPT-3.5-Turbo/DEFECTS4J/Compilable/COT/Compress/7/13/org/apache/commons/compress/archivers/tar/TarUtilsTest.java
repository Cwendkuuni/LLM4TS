package org.apache.commons.compress.archivers.tar;
import org.junit.Test;

import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        // Test with valid octal string
        byte[] buffer = "012345\0".getBytes();
        long result = TarUtils.parseOctal(buffer, 0, 6);
        assertEquals(012345L, result);

        // Test with octal string containing leading and trailing spaces
        buffer = "  012345  ".getBytes();
        result = TarUtils.parseOctal(buffer, 0, 9);
        assertEquals(012345L, result);

        // Test with octal string containing non-octal characters
        buffer = "01234a".getBytes();
        try {
            TarUtils.parseOctal(buffer, 0, 6);
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid octal digit at position 5 in '01234a'", e.getMessage());
        }
    }

    @Test
    public void testParseName() {
        // Test with valid name
        byte[] buffer = "abc\0".getBytes();
        String result = TarUtils.parseName(buffer, 0, 4);
        assertEquals("abc", result);

        // Test with name containing null character
        buffer = "abc\0def".getBytes();
        result = TarUtils.parseName(buffer, 0, 8);
        assertEquals("abc", result);
    }

    @Test
    public void testFormatNameBytes() {
        // Test with name shorter than buffer
        String name = "abc";
        byte[] buffer = new byte[4];
        int result = TarUtils.formatNameBytes(name, buffer, 0, 4);
        assertEquals(4, result);
        assertArrayEquals(new byte[] {'a', 'b', 'c', 0}, buffer);

        // Test with name longer than buffer
        name = "abcdefghi";
        buffer = new byte[6];
        result = TarUtils.formatNameBytes(name, buffer, 0, 6);
        assertEquals(6, result);
        assertArrayEquals(new byte[] {'a', 'b', 'c', 'd', 'e', 'f'}, buffer);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        // Test with value 0
        long value = 0;
        byte[] buffer = new byte[4];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, 4);
        assertArrayEquals(new byte[] {'0', 0, 0, 0}, buffer);

        // Test with value greater than 0
        value = 12345;
        buffer = new byte[6];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, 6);
        assertArrayEquals(new byte[] {'3', '0', '1', '1', 0, 0}, buffer);

        // Test with value that will not fit in buffer
        value = 12345678;
        buffer = new byte[5];
        try {
            TarUtils.formatUnsignedOctalString(value, buffer, 0, 5);
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("12345678=570605156 will not fit in octal number buffer of length 5", e.getMessage());
        }
    }

    @Test
    public void testFormatOctalBytes() {
        // Test with value 0
        long value = 0;
        byte[] buffer = new byte[8];
        int result = TarUtils.formatOctalBytes(value, buffer, 0, 8);
        assertEquals(8, result);
        assertArrayEquals(new byte[] {'0', 0, ' ', 0, 0, 0, 0, 0}, buffer);

        // Test with value greater than 0
        value = 12345;
        buffer = new byte[8];
        result = TarUtils.formatOctalBytes(value, buffer, 0, 8);
        assertEquals(8, result);
        assertArrayEquals(new byte[] {'3', '0', '1', '1', ' ', 0, 0, 0}, buffer);
    }

    @Test
    public void testFormatLongOctalBytes() {
        // Test with value 0
        long value = 0;
        byte[] buffer = new byte[8];
        int result = TarUtils.formatLongOctalBytes(value, buffer, 0, 8);
        assertEquals(8, result);
        assertArrayEquals(new byte[] {'0', 0, 0, 0, 0, 0, ' ', 0}, buffer);

        // Test with value greater than 0
        value = 12345;
        buffer = new byte[8];
        result = TarUtils.formatLongOctalBytes(value, buffer, 0, 8);
        assertEquals(8, result);
        assertArrayEquals(new byte[] {'3', '0', '1', '1', ' ', 0, 0, 0}, buffer);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        // Test with value 0
        long value = 0;
        byte[] buffer = new byte[8];
        int result = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, 8);
        assertEquals(8, result);
        assertArrayEquals(new byte[] {'0', 0, 0, 0, 0, 0, ' ', ' '}, buffer);

        // Test with value greater than 0
        value = 12345;
        buffer = new byte[8];
        result = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, 8);
        assertEquals(8, result);
        assertArrayEquals(new byte[] {'3', '0', '1', '1', ' ', 0, ' ', ' '}, buffer);
    }

    @Test
    public void testComputeCheckSum() {
        // Test with buffer containing all zeros
        byte[] buffer = new byte[8];
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(0, result);

        // Test with buffer containing non-zero bytes
        buffer = new byte[] {1, 2, 3, 4, 5, 6, 7, 8};
        result = TarUtils.computeCheckSum(buffer);
        assertEquals(36, result);
    }

}