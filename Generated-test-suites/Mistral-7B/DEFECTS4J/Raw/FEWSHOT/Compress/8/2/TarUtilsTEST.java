package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{(byte) '0', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '7', 0};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(0177777L, result);

        buffer = new byte[]{(byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6', 0};
        result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(0123456L, result);

        buffer = new byte[]{(byte) '0', (byte) '0', (byte) '0', 0};
        result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(0L, result);

        buffer = new byte[]{1};
        try {
            result = TarUtils.parseOctal(buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        buffer = new byte[]{1, 2};
        try {
            result = TarUtils.parseOctal(buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testExceptionMessage() {
        byte[] buffer = new byte[]{1, 1, (byte) 0};
        String msg = TarUtils.exceptionMessage(buffer, 0, buffer.length, 1, buffer[1]);
        assertEquals("Invalid byte 1 at offset 1 in '01{NUL}' len=3", msg);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{(byte) 'm', (byte) 'y', (byte) ' ', (byte) 'f', (byte) 'i', (byte) 'l', (byte) 'e', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', 0};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("my file.tar.gz", result);

        byte[] buffer2 = new byte[]{(byte) 'm', (byte) 'y', (byte) ' ', (byte) 'f', (byte) 'i', (byte) 'l', (byte) 'e', 0};
        String result2 = TarUtils.parseName(buffer2, 0, buffer2.length);
        assertEquals("my file", result2);

        byte[] buffer3 = new byte[]{0};
        String result3 = TarUtils.parseName(buffer3, 0, buffer3.length);
        assertEquals("", result3);

        byte[] buffer4 = new byte[]{1};
        String result4 = TarUtils.parseName(buffer4, 0, buffer4.length);
        assertEquals("", result4);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "my file.tar.gz";
        byte[] buffer = new byte[32];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        byte[] result = new byte[offset];
        System.arraycopy(buffer, 0, result, 0, offset);
        assertArrayEquals(new byte[]{(byte) 'm', (byte) 'y', (byte) ' ', (byte) 'f', (byte) 'i', (byte) 'l', (byte) 'e', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p'}, result);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '2', '3'}, buffer);

        value = 255;
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '3', '7'}, buffer);

        value = 0;
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0'}, buffer);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 123;
        byte[] buffer = new byte[12];
        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', ' '}, buffer);

        value = 255;
        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '3', '7', '7', '7'}, buffer);

        value = 0;
        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0'}, buffer);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 123;
        byte[] buffer = new byte[11];
        TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '1', '1', '1', '1', '1', '1', '1', ' ', ' '}, buffer);

        value = 255;
        TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '3', '7', '7'}, buffer);

        value = 0;
        TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0'}, buffer);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123;
        byte[] buffer = new byte[12];
        TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '0'}, buffer);

        value = 255;
        TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '3', '7', '7', '7', '0'}, buffer);

        value = 0;
        TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0'}, buffer);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{(byte) 0x41, (byte) 0x42, (byte) 0x43, (byte) 0x44, (byte) 0x45};
        long checksum = TarUtils.computeChecksum(buffer);
        long computedChecksum = 0;
        for (int i = 0; i < buffer.length; ++i) {
            computedChecksum += TarUtils.BYTE_MASK & buffer[i];
        }
        assertEquals(checksum, computedChecksum);
    }
}