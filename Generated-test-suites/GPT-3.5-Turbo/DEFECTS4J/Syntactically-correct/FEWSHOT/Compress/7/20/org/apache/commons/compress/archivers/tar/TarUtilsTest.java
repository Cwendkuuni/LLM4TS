package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 5;

        buffer[0] = 48;
        buffer[1] = 49;
        buffer[2] = 50;
        buffer[3] = 51;
        buffer[4] = 52;

        long result = TarUtils.parseOctal(buffer, offset, length);
        assertEquals(1234, result);

        // test buffer with leading and trailing spaces
        buffer[0] = 32;
        buffer[1] = 32;
        buffer[2] = 48;
        buffer[3] = 49;
        buffer[4] = 50;
        buffer[5] = 32;
        buffer[6] = 0;
        buffer[7] = 32;

        result = TarUtils.parseOctal(buffer, offset, length);
        assertEquals(12, result);

        // test buffer with invalid octal digit
        buffer[0] = 48;
        buffer[1] = 49;
        buffer[2] = 50;
        buffer[3] = 56;
        buffer[4] = 52;

        try {
            result = TarUtils.parseOctal(buffer, offset, length);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // exception thrown as expected
        }
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 5;

        buffer[0] = 70;
        buffer[1] = 79;
        buffer[2] = 79;
        buffer[3] = 0;
        buffer[4] = 66;
        buffer[5] = 65;
        buffer[6] = 82;
        buffer[7] = 0;

        String result = TarUtils.parseName(buffer, offset, length);
        assertEquals("FOO", result);

        // test buffer with no null character
        buffer[0] = 70;
        buffer[1] = 79;
        buffer[2] = 79;
        buffer[3] = 66;
        buffer[4] = 65;

        result = TarUtils.parseName(buffer, offset, length);
        assertEquals("FOOB", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "FOO";
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 5;

        int result = TarUtils.formatNameBytes(name, buffer, offset, length);
        assertEquals(offset + length, result);

        // test name longer than buffer
        name = "FOOBAR";
        result = TarUtils.formatNameBytes(name, buffer, offset, length);
        assertEquals(offset + length, result);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 1234;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 5;

        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);

        assertEquals((byte) '2', buffer[offset + 4]);
        assertEquals((byte) '3', buffer[offset + 3]);
        assertEquals((byte) '4', buffer[offset + 2]);

        // test value == 0
        value = 0;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        assertEquals((byte) '0', buffer[offset + 4]);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 1234;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 5;

        int result = TarUtils.formatOctalBytes(value, buffer, offset, length);
        assertEquals(offset + length, result);

        // test value == 0
        value = 0;
        result = TarUtils.formatOctalBytes(value, buffer, offset, length);
        assertEquals(offset + length, result);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 1234;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 5;

        int result = TarUtils.formatLongOctalBytes(value, buffer, offset, length);
        assertEquals(offset + length, result);

        // test value == 0
        value = 0;
        result = TarUtils.formatLongOctalBytes(value, buffer, offset, length);
        assertEquals(offset + length, result);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 1234;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 5;

        int result = TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);
        assertEquals(offset + length, result);

        // test value == 0
        value = 0;
        result = TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);
        assertEquals(offset + length, result);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[10];
        buffer[0] = 48;
        buffer[1] = 49;
        buffer[2] = 50;
        buffer[3] = 51;
        buffer[4] = 52;
        buffer[5] = 53;
        buffer[6] = 54;
        buffer[7] = 55;
        buffer[8] = 56;
        buffer[9] = 57;

        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(429, result);
    }
}