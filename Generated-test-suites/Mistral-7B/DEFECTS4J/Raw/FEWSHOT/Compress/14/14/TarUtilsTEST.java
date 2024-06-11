package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[] { 0, '7', '5', '5', '4' };
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(017554L, result);

        byte[] buffer1 = new byte[] { 0, '0' };
        long result1 = TarUtils.parseOctal(buffer1, 0, buffer1.length);
        assertEquals(0L, result1);

        byte[] buffer2 = new byte[] { 0, '0', '1' };
        long result2 = TarUtils.parseOctal(buffer2, 0, buffer2.length);
        assertEquals(01L, result2);

        byte[] buffer3 = new byte[] { 0, '7', '6', '7' };
        try {
            TarUtils.parseOctal(buffer3, 0, buffer3.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        byte[] buffer4 = new byte[] { 0, '8' };
        try {
            TarUtils.parseOctal(buffer4, 0, buffer4.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        byte[] buffer5 = new byte[] { 0, '5', '8' };
        try {
            TarUtils.parseOctal(buffer5, 0, buffer5.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseOctalOrBinary() {
        byte[] buffer = new byte[] { 0, '6', '2', '3', '5' };
        long result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(0146235L, result);

        byte[] buffer1 = new byte[] { 0, '1' };
        try {
            TarUtils.parseOctalOrBinary(buffer1, 0, buffer1.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        byte[] buffer2 = new byte[] { 0, '1', '0' };
        long result2 = TarUtils.parseOctalOrBinary(buffer2, 0, buffer2.length);
        assertEquals(1L, result2);

        byte[] buffer3 = new byte[] { 0, '1', '0', '1' };
        long result3 = TarUtils.parseOctalOrBinary(buffer3, 0, buffer3.length);
        assertEquals(101L, result3);

        byte[] buffer4 = new byte[] { 0, '1', '0', '1', '1' };
        long result4 = TarUtils.parseOctalOrBinary(buffer4, 0, buffer4.length);
        assertEquals(10101L, result4);

        byte[] buffer5 = new byte[] { 0, '1', '1', '1', '1' };
        try {
            TarUtils.parseOctalOrBinary(buffer5, 0, buffer5.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseBoolean() {
        byte[] buffer1 = new byte[] { 0 };
        assertTrue(TarUtils.parseBoolean(buffer1, 0));

        byte[] buffer2 = new byte[] { 1 };
        assertTrue(TarUtils.parseBoolean(buffer2, 0));

        byte[] buffer3 = new byte[] { (byte) 0x7f };
        assertFalse(TarUtils.parseBoolean(buffer3, 0));

        byte[] buffer4 = new byte[] { (byte) 0x80 };
        assertFalse(TarUtils.parseBoolean(buffer4, 0));
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[] { 'f', 'o', 'o', 0 };
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("foo", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "foo";
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals(offset, 10);
        byte[] result = new byte[10];
        System.arraycopy(buffer, 0, result, 0, 10);
        String resultString = new String(result, 0, 10);
        assertEquals("foo\0\0\0\0\0\0\0\0\0", resultString);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        String result = new String(buffer, 0, 10);
        assertEquals("0123\0\0\0\0\0\0\0\0\0", result);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 123;
        byte[] buffer = new byte[11];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(offset, 11);
        String result = new String(buffer, 0, 11);
        assertEquals("0123 \0", result);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 12345;
        byte[] buffer = new byte[11];
        int offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(offset, 11);
        String result = new String(buffer, 0, 11);
        assertEquals("012345 \0", result);
    }

    @Test
    public void testFormatLongOctalOrBinaryBytes() {
        long value = 12345;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        assertEquals(offset, 10);
        String result = new String(buffer, 0, 10);
        assertEquals("12345\0\0\0\0\0\0\0", result);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 12345;
        byte[] buffer = new byte[11];
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(offset, 11);
        String result = new String(buffer, 0, 11);
        assertEquals("012345\0\0", result);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[] { 0, 1, 2, 3, 4 };
        long checksum = TarUtils.computeCheckSum(buffer);
        long expected = 0b11110110111111101111011011111101L;
        assertEquals(expected, checksum);
    }
}