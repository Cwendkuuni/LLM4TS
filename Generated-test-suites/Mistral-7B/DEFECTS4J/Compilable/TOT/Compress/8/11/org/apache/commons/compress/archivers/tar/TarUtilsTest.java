package org.apache.commons.compress.archivers.tar.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.compress.archivers.tar.TarUtils;

import org.junit.Test;

/**
 * Test cases for {@link TarUtils}.
 */
public class TarUtilsTest {

    /**
     * Test parseOctal method with valid octal string.
     */
    @Test
    public void testParseOctalValid() {
        byte[] buffer = new byte[] { '0', '7', '7', '5', '0' };
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(017750L, result);
    }

    /**
     * Test parseOctal method with empty buffer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalEmpty() {
        byte[] buffer = new byte[0];
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    /**
     * Test parseOctal method with buffer containing trailing NUL.
     */
    @Test
    public void testParseOctalTrailingNul() {
        byte[] buffer = new byte[] { '0', '7', '7', '5' };
        long result = TarUtils.parseOctal(buffer, 0, buffer.length + 1);
        assertEquals(01775L, result); // Leading zero is ignored
    }

    /**
     * Test parseOctal method with buffer containing invalid byte.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidByte() {
        byte[] buffer = new byte[] { '0', '7', '7', '5', '8' }; // '8' is not valid octal digit
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    /**
     * Test parseName method with valid name.
     */
    @Test
    public void testParseNameValid() {
        byte[] buffer = new byte[] { 'a', 'b', 'c', 'd', 'e', 0 };
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("abcde", result);
    }

    /**
     * Test parseName method with empty buffer.
     */
    @Test
    public void testParseNameEmpty() {
        byte[] buffer = new byte[0];
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("", result);
    }

    /**
     * Test formatNameBytes method with valid name.
     */
    @Test
    public void testFormatNameBytesValid() {
        String name = "abcde";
        byte[] buffer = new byte[name.length() + 1];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);

        for (int i = 0; i < name.length(); ++i) {
            assertEquals(name.charAt(i), (char) (buffer[i] & 0xFF));
        }
        assertEquals(0, buffer[name.length()]); // Trailing NUL
        assertEquals(offset, buffer.length);
    }

    /**
     * Test formatNameBytes method with buffer shorter than name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatNameBytesNameLongerThanBuffer() {
        String name = "abcdef";
        byte[] buffer = new byte[name.length()];
        TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
    }

    /**
     * Test formatUnsignedOctalString method with valid value.
     */
    @Test
    public void testFormatUnsignedOctalStringValidValue() {
        byte[] buffer = new byte[8];
        long value = 12345678L;
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);

        for (int i = 0; i < 7; ++i) {
            assertEquals('0', buffer[i]);
        }
        assertEquals('7', buffer[6]);
        assertEquals('5', buffer[7]);
        assertEquals(value, Long.parseLong(new String(buffer, 0, 7) + ""));
    }

    /**
     * Test formatUnsignedOctalString method with value longer than buffer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringValueLongerThanBuffer() {
        byte[] buffer = new byte[5];
        long value = 12345678912345L; // longer than 5 bytes
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
    }

    /**
     * Test formatOctalBytes method with valid value.
     */
    @Test
    public void testFormatOctalBytesValidValue() {
        byte[] buffer = new byte[10];
        long value = 12345678912345L; // longer than 5 bytes
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);

        assertEquals(value, Long.parseLong(new String(buffer, 0, offset - 2) + ""));
        assertEquals(0, buffer[offset - 1]); // Trailing NUL
        assertEquals(' ', buffer[offset]); // Trailing space
        assertEquals(offset, buffer.length);
    }

    /**
     * Test formatOctalBytes method with value longer than buffer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatOctalBytesValueLongerThanBuffer() {
        byte[] buffer = new byte[5];
        long value = 12345678912345L; // longer than 5 bytes
        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
    }

    /**
     * Test computeCheckSum method with valid header.
     */
    @Test
    public void testComputeCheckSumValidHeader() {
        byte[] buffer = new byte[] {
            // Header magic number
            0x75, 0x74, 0x64, 0x66,
            // Name length
            0x06, 0x00,
            // Name
            't', 'a', 'r', '.', 't', 0,
            // Mode
            '0', '7', '5', '4', '0', '0',
            // User id and group id
            '4', '8', '6', '6',
            // Size
            0x04, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01,
            // Modification time
            0x64, 0x75, 0x6E, 0x61, 0x6D, 0x65, 0x00, 0x00,
            // Checksum
            0x42, 0x42, 0x42, 0x42
        };
        long checksum = TarUtils.computeCheckSum(buffer);

        long expectedChecksum = 0x49535342L; // computed using algorithm from TarUtils source code
        assertEquals(expectedChecksum, checksum);
    }

    /**
     * Test parseOctal method with buffer containing only NULs.
     */
    @Test
    public void testParseOctalAllNuls() {
        byte[] buffer = new byte[0];
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(0L, result);
    }
}