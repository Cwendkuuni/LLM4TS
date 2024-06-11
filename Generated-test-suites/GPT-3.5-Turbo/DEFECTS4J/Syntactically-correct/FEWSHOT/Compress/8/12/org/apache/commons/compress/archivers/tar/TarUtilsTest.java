package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TarUtilsTest {
    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[10];
        buffer[0] = 48; // '0'
        buffer[1] = 55; // '7'
        buffer[2] = 48; // '0'
        buffer[3] = 0; // trailing null
        long result = TarUtils.parseOctal(buffer, 0, 4);
        assertEquals(448, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidByte() {
        byte[] buffer = new byte[10];
        buffer[0] = 65; // 'A'
        buffer[1] = 55; // '7'
        buffer[2] = 48; // '0'
        buffer[3] = 0; // trailing null
        TarUtils.parseOctal(buffer, 0, 4);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[10];
        buffer[0] = 97; // 'a'
        buffer[1] = 98; // 'b'
        buffer[2] = 99; // 'c'
        buffer[3] = 0; // trailing null
        String result = TarUtils.parseName(buffer, 0, 4);
        assertEquals("abc", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "abc";
        byte[] buf = new byte[10];
        int offset = 0;
        int length = 5;
        int result = TarUtils.formatNameBytes(name, buf, offset, length);
        assertEquals(5, result);
        assertEquals(97, buf[0]); // 'a'
        assertEquals(98, buf[1]); // 'b'
        assertEquals(99, buf[2]); // 'c'
        assertEquals(0, buf[3]); // trailing null
        assertEquals(0, buf[4]); // padding null
    }

    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buffer = new byte[10];
        long value = 448;
        int offset = 0;
        int length = 5;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        assertEquals(48, buffer[0]); // '0'
        assertEquals(55, buffer[1]); // '7'
        assertEquals(48, buffer[2]); // '0'
        assertEquals(0, buffer[3]); // trailing null
        assertEquals(0, buffer[4]); // padding null
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringLongerThanBuffer() {
        byte[] buffer = new byte[2];
        long value = 448;
        int offset = 0;
        int length = 2;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
    }

    @Test
    public void testFormatOctalBytes() {
        byte[] buf = new byte[10];
        long value = 448;
        int offset = 0;
        int length = 5;
        int result = TarUtils.formatOctalBytes(value, buf, offset, length);
        assertEquals(5, result);
        assertEquals(48, buf[0]); // '0'
        assertEquals(55, buf[1]); // '7'
        assertEquals(48, buf[2]); // '0'
        assertEquals(32, buf[3]); // trailing space
        assertEquals(0, buf[4]); // trailing null
    }

    @Test
    public void testFormatLongOctalBytes() {
        byte[] buf = new byte[10];
        long value = 448;
        int offset = 0;
        int length = 4;
        int result = TarUtils.formatLongOctalBytes(value, buf, offset, length);
        assertEquals(4, result);
        assertEquals(48, buf[0]); // '0'
        assertEquals(55, buf[1]); // '7'
        assertEquals(48, buf[2]); // '0'
        assertEquals(32, buf[3]); // trailing space
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        byte[] buf = new byte[10];
        long value = 448;
        int offset = 0;
        int length = 5;
        int result = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
        assertEquals(5, result);
        assertEquals(48, buf[0]); // '0'
        assertEquals(55, buf[1]); // '7'
        assertEquals(48, buf[2]); // '0'
        assertEquals(0, buf[3]); // trailing null
        assertEquals(32, buf[4]); // trailing space
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = new byte[10];
        buf[0] = 97; // 'a'
        buf[1] = 98; // 'b'
        buf[2] = 99; // 'c'
        buf[3] = 0; // trailing null
        long sum = TarUtils.computeCheckSum(buf);
        assertEquals(294, sum);
    }
}