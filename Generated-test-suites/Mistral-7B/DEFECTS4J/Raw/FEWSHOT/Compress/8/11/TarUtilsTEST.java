package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        long value = TarUtils.parseOctal(new byte[]{'0', '7', '7', '7'}, 0, 3);
        assertEquals(0177L, value);

        value = TarUtils.parseOctal(new byte[]{'7', '7', '7'}, 0, 3);
        assertEquals(0777L, value);

        value = TarUtils.parseOctal(new byte[]{'6', '6', '6'}, 0, 3);
        assertEquals(0666L, value);

        value = TarUtils.parseOctal(new byte[]{'1', '1', '1'}, 0, 3);
        assertEquals(0111L, value);

        value = TarUtils.parseOctal(new byte[]{'0'}, 0, 1);
        assertEquals(0L, value);

        value = TarUtils.parseOctal(new byte[0], 0, 0);
        assertEquals(0L, value);

        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(new byte[]{'8'}, 0, 1));
    }

    @Test
    public void testExceptionMessage() {
        byte[] buffer = new byte[10];
        int offset = 0, length = 10;

        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer, offset, length));

        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = 0;
            assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer, offset, length, i, (byte) 8));
        }
    }

    @Test
    public void testParseName() {
        String name = TarUtils.parseName(new byte[]{'a', 'b', 'c', 'd', 'e', '0'}, 0, 6);
        assertEquals("abcde", name);

        name = TarUtils.parseName(new byte[0], 0, 0);
        assertNull(name);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "abcdefg";

        byte[] buffer = new byte[10];
        int offset = 0, length = 10;

        int result = TarUtils.formatNameBytes(name, buffer, offset, length);
        assertEquals("abcdefg", new String(buffer, 0, result));

        result = TarUtils.formatNameBytes(name, buffer, offset, 5);
        assertEquals("abcde", new String(buffer, 0, result));
    }

    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buffer = new byte[10];

        long value = 0;
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[10], buffer); // All NUL

        value = 1;
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0'}, buffer);

        value = 7;
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1'}, buffer);

        value = 65;
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '101'}, buffer);

        value = 0xFFFF;
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '377'}, buffer);
    }

    @Test
    public void testFormatOctalBytes() {
        byte[] buffer = new byte[10];

        long value = 0;
        int offset = 0, length = 10;

        int result = TarUtils.formatOctalBytes(value, buffer, offset, length);
        assertArrayEquals(new byte[10], buffer); // All NUL

        value = 1;
        result = TarUtils.formatOctalBytes(value, buffer, offset, length);
        assertEquals(offset + length, result);
        assertArrayEquals(new byte[]{'0', ' ', '0'}, buffer, 0, 3);

        value = 7;
        result = TarUtils.formatOctalBytes(value, buffer, offset, length);
        assertEquals(offset + length, result);
        assertArrayEquals(new byte[]{'0', '1', ' ', '0'}, buffer, 0, 4);

        value = 65;
        result = TarUtils.formatOctalBytes(value, buffer, offset, length);
        assertEquals(offset + length, result);
        assertArrayEquals(new byte[]{'0', '101', ' ', '0'}, buffer, 0, 4);

        value = 0xFFFF;
        result = TarUtils.formatOctalBytes(value, buffer, offset, length);
        assertEquals(offset + length, result);
        assertArrayEquals(new byte[]{'0', '377', ' ', '0'}, buffer, 0, 4);
    }

    @Test
    public void testFormatLongOctalBytes() {
        byte[] buffer = new byte[10];

        long value = 0;
        int offset = 0, length = 10;

        int result = TarUtils.formatLongOctalBytes(value, buffer, offset, length);
        assertEquals(offset + length, result);
        assertArrayEquals(new byte[10], buffer); // All NUL

        value = 1;
        result = TarUtils.formatLongOctalBytes(value, buffer, offset, length);
        assertEquals(offset + length, result);
        assertArrayEquals(new byte[]{'0', ' ', '0'}, buffer, 0, 3);

        value = 7;
        result = TarUtils.formatLongOctalBytes(value, buffer, offset, length);
        assertEquals(offset + length, result);
        assertArrayEquals(new byte[]{'0', '1', ' ', '0'}, buffer, 0, 4);

        value = 65;
        result = TarUtils.formatLongOctalBytes(value, buffer, offset, length);
        assertEquals(offset + length, result);
        assertArrayEquals(new byte[]{'0', '101', ' ', '0'}, buffer, 0, 4);

        value = 0xFFFF;
        result = TarUtils.formatLongOctalBytes(value, buffer, offset, length);
        assertEquals(offset + length, result);
        assertArrayEquals(new byte[]{'0', '377'}, buffer, 0, 2);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        byte[] buffer = new byte[10];

        long value = 0;
        int offset = 0, length = 10;

        int result = TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);
        assertEquals(offset + length, result);
        assertArrayEquals(new byte[10], buffer); // All NUL

        value = 1;
        result = TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);
        assertEquals(offset + length, result);
        assertArrayEquals(new byte[]{'0', ' ', '0'}, buffer, 0, 3);

        value = 7;
        result = TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);
        assertEquals(offset + length, result);
        assertArrayEquals(new byte[]{'0', '1', ' ', '0'}, buffer, 0, 4);

        value = 65;
        result = TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);
        assertEquals(offset + length, result);
        assertArrayEquals(new byte[]{'0', '101', ' ', '0'}, buffer, 0, 4);

        value = 0xFFFF;
        result = TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);
        assertEquals(offset + length, result);
        assertArrayEquals(new byte[]{'0', '377', ' ', '0'}, buffer, 0, 4);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        long checksum = TarUtils.computeCheckSum(buf);
        long expectedChecksum = 0x676F926F676F926FL;
        assertEquals(expectedChecksum, checksum);
    }

    @Test
    @Ignore
    public void testOctalStringTooLong() {
        byte[] buffer = new byte[5];
        long value = Long.parseUnsignedLong("12345678901234567890", 8);

        assertThrows(IllegalArgumentException.class, () -> TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length));
    }
}