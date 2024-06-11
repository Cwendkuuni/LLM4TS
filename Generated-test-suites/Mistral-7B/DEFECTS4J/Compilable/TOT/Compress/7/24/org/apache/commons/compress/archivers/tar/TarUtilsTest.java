package org.apache.commons.compress.archivers.tar.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.compress.archivers.tar.TarUtils;

import org.junit.Test;

/**
 * Test class for TarUtils methods.
 */
public class TarUtilsTest {

    /**
     * Test parseOctal method with valid input.
     */
    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '4', '4', '4', '7', '5'};
        int offset = 0;
        int length = buffer.length;

        long expected = 0144475L;
        long result = TarUtils.parseOctal(buffer, offset, length);

        assertEquals(expected, result);
    }

    /**
     * Test parseOctal method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidInput() {
        byte[] buffer = new byte[]{'0', '4', '4', '4', 'a', '5'}; // 'a' is not a valid octal digit

        int offset = 0;
        int length = buffer.length;

        TarUtils.parseOctal(buffer, offset, length); // should throw an IllegalArgumentException
    }

    /**
     * Test parseName method with valid input.
     */
    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'t', 'a', 'r', ' ', '.', 't', 'x', 't', '.', 't', 'e', 's', 't', 0};
        int offset = 0;
        int length = buffer.length;

        String expected = "tar.txt.test";
        String result = TarUtils.parseName(buffer, offset, length);

        assertEquals(expected, result);
    }

    /**
     * Test parseName method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseNameInvalidInput() {
        byte[] buffer = new byte[]{'t', 'a', 'r', 0}; // NUL character found before the end of the name

        int offset = 0;
        int length = buffer.length;

        TarUtils.parseName(buffer, offset, length); // should throw an IllegalArgumentException
    }

    /**
     * Test formatNameBytes method with valid input.
     */
    @Test
    public void testFormatNameBytes() {
        String name = "tar.txt.test";
        byte[] buffer = new byte[12];
        int offset = 0;
        int length = buffer.length;

        int result = TarUtils.formatNameBytes(name, buffer, offset, length);

        assertEquals(12, result);
        assertEquals(name.charAt(0), buffer[offset]);
        assertEquals(name.charAt(1), buffer[offset + 1]);
        // ...
        assertEquals(name.charAt(10), buffer[offset + 10]);
        assertEquals('\u0000', buffer[offset + 11]);
    }

    /**
     * Test formatNameBytes method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatNameBytesInvalidInput() {
        String name = "tar.txt.test";
        byte[] buffer = new byte[10]; // buffer is not large enough to store the name

        int offset = 0;

        TarUtils.formatNameBytes(name, buffer, offset, buffer.length); // should throw an IllegalArgumentException
    }

    /**
     * Test parseOctal method with valid input, edge cases.
     */
    @Test
    public void testParseOctalEdgeCases() {
        // Test empty string
        byte[] buffer = new byte[0];
        int offset = 0;
        int length = buffer.length;

        TarUtils.parseOctal(buffer, offset, length); // should not throw an exception

        // Test only spaces
        buffer = new byte[]{' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};
        offset = 0;
        length = buffer.length;

        TarUtils.parseOctal(buffer, offset, length); // should not throw an exception

        // Test only '0'
        buffer = new byte[]{'0'};
        offset = 0;
        length = buffer.length;

        TarUtils.parseOctal(buffer, offset, length); // should return 0
    }

    /**
     * Test parseName method with valid input, edge cases.
     */
    @Test
    public void testParseNameEdgeCases() {
        // Test empty string
        byte[] buffer = new byte[0];
        int offset = 0;
        int length = buffer.length;

        TarUtils.parseName(buffer, offset, length); // should return an empty String

        // Test only spaces
        buffer = new byte[]{' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};
        offset = 0;
        length = buffer.length;

        String result = TarUtils.parseName(buffer, offset, length);
        assertEquals("", result);
    }

    /**
     * Test formatUnsignedOctalString method with valid input.
     */
    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = buffer.length;

        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);

        assertEquals(value, Long.parseLong(new String(buffer), 8));
    }

    /**
     * Test formatUnsignedOctalString method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringInvalidInput() {
        long value = Long.MAX_VALUE + 1; // too large value to be represented as an octal string
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = buffer.length;

        TarUtils.formatUnsignedOctalString(value, buffer, offset, length); // should throw an IllegalArgumentException
    }

    /**
     * Test formatOctalBytes method with valid input.
     */
    @Test
    public void testFormatOctalBytes() {
        long value = 12345;
        byte[] buffer = new byte[13];
        int offset = 0;
        int length = buffer.length;

        int result = TarUtils.formatOctalBytes(value, buffer, offset, length);

        assertEquals(13, result);
        assertEquals('0', buffer[offset]);
        assertEquals('1', buffer[offset + 1]);
        assertEquals('2', buffer[offset + 2]);
        // ...
        assertEquals('5', buffer[offset + 8]);
        assertEquals('\u0020', buffer[offset + 9]); // space
        assertEquals('\u0000', buffer[offset + 10]); // NUL
    }

    /**
     * Test formatOctalBytes method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatOctalBytesInvalidInput() {
        long value = Long.MAX_VALUE + 1; // too large value to be represented as an octal string
        byte[] buffer = new byte[10]; // buffer is not large enough to store the octal string and the trailing space and NUL

        int offset = 0;

        TarUtils.formatOctalBytes(value, buffer, offset, buffer.length); // should throw an IllegalArgumentException
    }
}