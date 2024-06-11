package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TarUtilsTest {
    @Test
    public void testParseOctal() {
        // Test with valid octal input
        byte[] buffer = { '1', '3', '5', '0', '4' };
        assertEquals(013504L, TarUtils.parseOctal(buffer, 0, 5));

        // Test with invalid octal input
        byte[] invalidBuffer = { '1', '3', '5', 'G', '4' };
        try {
            TarUtils.parseOctal(invalidBuffer, 0, 5);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid octal digit at position 3 in '135G4'", e.getMessage());
        }
    }

    @Test
    public void testParseName() {
        // Test with null terminated name
        byte[] buffer = { 'h', 'e', 'l', 'l', 'o', 0, 'w', 'o', 'r', 'l', 'd' };
        assertEquals("hello", TarUtils.parseName(buffer, 0, 5));

        // Test with non-null terminated name
        byte[] buffer2 = { 'h', 'e', 'l', 'l', 'o', 'w', 'o', 'r', 'l', 'd' };
        assertEquals("hello", TarUtils.parseName(buffer2, 0, 5));
    }

    @Test
    public void testFormatNameBytes() {
        byte[] buf = new byte[5];

        // Test with name shorter than buffer
        String name = "hello";
        assertEquals(5, TarUtils.formatNameBytes(name, buf, 0, 5));
        assertEquals("hello", new String(buf));

        // Test with name longer than buffer
        String longName = "helloworld";
        assertEquals(5, TarUtils.formatNameBytes(longName, buf, 0, 5));
        assertEquals("hello", new String(buf));
    }

    @Test
    public void testFormatUnsignedOctalString() {
        // Test with positive value
        long value = 42L;
        byte[] buf = new byte[5];
        TarUtils.formatUnsignedOctalString(value, buf, 0, 5);
        assertEquals("00042", new String(buf));

        // Test with zero value
        long zeroValue = 0L;
        byte[] zeroBuf = new byte[5];
        TarUtils.formatUnsignedOctalString(zeroValue, zeroBuf, 0, 5);
        assertEquals("00000", new String(zeroBuf));

        // Test with negative value
        long negativeValue = -42L;
        byte[] negBuf = new byte[5];
        TarUtils.formatUnsignedOctalString(negativeValue, negBuf, 0, 5); // Negative values are treated as positive
        assertEquals("17736", new String(negBuf));
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 42L;
        byte[] buf = new byte[5];
        TarUtils.formatOctalBytes(value, buf, 0, 5);
        assertEquals("00042 ", new String(buf));
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 42L;
        byte[] buf = new byte[5];
        TarUtils.formatLongOctalBytes(value, buf, 0, 5);
        assertEquals("00042", new String(buf));
    }

    @Test
    public void testFormaCheckSumOctalBytes() {
        long value = 42L;
        byte[] buf = new byte[5];
        TarUtils.formatCheckSumOctalBytes(value, buf, 0, 5);
        assertEquals("00042 ", new String(buf));
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        assertEquals(45L, TarUtils.computeCheckSum(buf));
    }
}