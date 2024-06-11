package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.tar.TarUtils;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '7', '7', '7', '7', '7', '7', '7', '7', ' '};
        long expected = 0777777L;
        long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidInput() {
        byte[] buffer = new byte[]{'0', '7', '7', '7', '7', '7', '7', '7', '7', '7'};
        TarUtils.parseOctal(buffer, 0, buffer.length - 1);
    }

    @Test
    public void testParseOctalTrailingNul() {
        byte[] buffer = new byte[]{'0', '7', '7', '7', '7', '7', '7', '7', '7', '7', ' ', '0'};
        long expected = 0777777L;
        long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalMissingTrailingNul() {
        byte[] buffer = new byte[]{'0', '7', '7', '7', '7', '7', '7', '7', '7', '7'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseOctalAllNuls() {
        byte[] buffer = new byte[]{'0'};
        long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(0L, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidByte() {
        byte[] buffer = new byte[]{'0', '7', '7', '7', '7', '7', '7', '7', '7', '7', '8'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseOctalOrBinary() {
        byte[] buffer = new byte[]{'0', '7', '7', '7', '7', '7', '7', '7', '7', '7'};
        long actual = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(0777777L, actual);
    }

    @Test
    public void testParseOctalOrBinaryBinary() {
        byte[] buffer = new byte[]{'0', '1', '0', '1', '1', '1', '1', '1', '1', '1'};
        long actual = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(101111111111L, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalOrBinaryInvalidByte() {
        byte[] buffer = new byte[]{'0', '7', '7', '7', '7', '7', '7', '7', '7', '7', '9'};
        TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
    }

    @Test
    public void testParseBoolean() {
        byte[] buffer = new byte[]{1};
        boolean expected = true;
        boolean actual = TarUtils.parseBoolean(buffer, 0);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBooleanInvalidByte() {
        byte[] buffer = new byte[]{0};
        TarUtils.parseBoolean(buffer, 0);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '.', 't', 'a', 'r', '.', 'g', 'z'};
        String expected = "tar.file.gz";
        String actual = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "tar.file.gz";
        byte[] buffer = new byte[32];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        byte[] result = new byte[offset];
        System.arraycopy(buffer, 0, result, 0, offset);
        String actual = new String(result);
        String expected = "tar.file.gz";
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123456789L;
        byte[] buffer = new byte[12];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        String actual = new String(buffer);
        String expected = "01234567755";
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 123456789L;
        byte[] buffer = new byte[12];
        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        String actual = new String(buffer);
        String expected = "01234567755 ";
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 123456789012345L;
        byte[] buffer = new byte[13];
        TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        String actual = new String(buffer);
        String expected = "012345677552667166545 ";
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatLongOctalOrBinaryBytes() {
        long value = 123456789012345L;
        byte[] buffer = new byte[10];
        TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        String actual = new String(buffer);
        String expected = "0123456775\u0000\u0000\u0000\u0000\u0000\u0000\u0000\u0000";
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123456789012345L;
        byte[] buffer = new byte[12];
        TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        String actual = new String(buffer);
        String expected = "01234567755  ";
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '.', 't', 'a', 'r', '.', 'g', 'z'};
        long expected = TarUtils.computeCheckSum(buffer);
        long actual = TarUtils.computeCheckSum(buffer);
        assertEquals(expected, actual);
    }
}