package org.apache.commons.compress.archivers.tar;
import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{49, 50, 51}; // ASCII values for '123'
        int offset = 0;
        int length = 3;

        long result = TarUtils.parseOctal(buffer, offset, length);

        assertEquals(123, result);
    }

    @Test
    public void testParseOctal_Exception() {
        byte[] buffer = new byte[1]; // Invalid length
        int offset = 0;
        int length = 1;

        try {
            TarUtils.parseOctal(buffer, offset, length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseOctalOrBinary() {
        byte[] buffer = new byte[]{(byte) 128, 1}; // Represents binary number 10000000 00000001
        int offset = 0;
        int length = 2;

        long result = TarUtils.parseOctalOrBinary(buffer, offset, length);

        assertEquals(128, result);
    }

    @Test
    public void testParseBoolean() {
        byte[] buffer = new byte[]{1}; // Represents boolean value true
        int offset = 0;

        boolean result = TarUtils.parseBoolean(buffer, offset);

        assertTrue(result);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{65, 66, 67, 0}; // ASCII values for 'ABC' followed by NUL
        int offset = 0;
        int length = 4;

        String result = TarUtils.parseName(buffer, offset, length);

        assertEquals("ABC", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "Hello";
        byte[] buffer = new byte[8]; // Buffer
        int offset = 0;
        int length = 5;

        int newOffset = TarUtils.formatNameBytes(name, buffer, offset, length);

        assertEquals(5, newOffset);
        assertArrayEquals(new byte[]{72, 101, 108, 108, 111, 0, 0, 0}, buffer);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123;
        byte[] buffer = new byte[8]; // Buffer
        int offset = 0;
        int length = 5;

        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);

        assertArrayEquals(new byte[]{48, 48, 49, 50, 51, 0, 0, 0}, buffer);
    }

    // Add more tests for remaining methods

    // Edge cases and error scenarios testing can be added

}