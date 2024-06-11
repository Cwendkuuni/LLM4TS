package org.apache.commons.compress.archivers.tar;

import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.compress.archivers.tar.TarUtils.BYTE_MASK;
import static org.apache.commons.compress.archivers.tar.TarUtils.parseOctal;
import static org.apache.commons.compress.archivers.tar.TarUtils.parseOctalOrBinary;
import static org.apache.commons.compress.archivers.tar.TarUtils.parseBoolean;
import static org.apache.commons.compress.archivers.tar.TarUtils.parseName;
import static org.apache.commons.compress.archivers.tar.TarUtils.formatNameBytes;
import static org.apache.commons.compress.archivers.tar.TarUtils.formatUnsignedOctalString;
import static org.apache.commons.compress.archivers.tar.TarUtils.formatLongOctalBytes;
import static org.apache.commons.compress.archivers.tar.TarUtils.formatLongOctalOrBinaryBytes;
import static org.apache.commons.compress.archivers.tar.TarUtils.formatCheckSumOctalBytes;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', ' '};
        long expected = 01755;
        long actual = parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidByte() {
        byte[] buffer = new byte[]{'0', '7', '5', 'A', ' '};
        parseOctal(buffer, 0, buffer.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidLength() {
        byte[] buffer = new byte[]{'0'};
        parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseOctalAllNUL() {
        byte[] buffer = new byte[0];
        long actual = parseOctal(buffer, 0, buffer.length);
        assertEquals(0L, actual);
    }

    @Test
    public void testParseOctalTrailingNUL() {
        byte[] buffer = new byte[]{'0', '0'};
        long actual = parseOctal(buffer, 0, buffer.length);
        assertEquals(0L, actual);
    }

    @Test
    public void testParseOctalTrailingSpace() {
        byte[] buffer = new byte[]{'0', '0', ' '};
        long actual = parseOctal(buffer, 0, buffer.length);
        assertEquals(0L, actual);
    }

    @Test
    public void testParseOctalLeadingNUL() {
        byte[] buffer = new byte[]{0, '7', '5', '5', ' '};
        long actual = parseOctal(buffer, 0, buffer.length);
        assertEquals(0L, actual);
    }

    @Test
    public void testParseOctalOrBinary() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4'};
        long expected = 011234;
        long actual = parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseOctalOrBinaryLeadingBit() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5'};
        long expected = 112345;
        long actual = parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalOrBinaryInvalidByte() {
        byte[] buffer = new byte[]{'0', '1', '2', 'A', '4'};
        parseOctalOrBinary(buffer, 0, buffer.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalOrBinaryInvalidLength() {
        byte[] buffer = new byte[]{'0'};
        parseOctalOrBinary(buffer, 0, buffer.length);
    }

    @Test
    public void testParseBooleanTrue() {
        byte[] buffer = new byte[]{1};
        boolean expected = true;
        boolean actual = parseBoolean(buffer, 0);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseBooleanFalse() {
        byte[] buffer = new byte[]{0};
        boolean expected = false;
        boolean actual = parseBoolean(buffer, 0);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBooleanInvalidByte() {
        byte[] buffer = new byte[]{2};
        parseBoolean(buffer, 0);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{0, 'T', 'a', 'r', ' ', 0};
        String expected = "Tar";
        String actual = parseName(buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "TarUtilsTest";
        byte[] buffer = new byte[50];
        int expected = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        int actual = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345;
        byte[] buffer = new byte[13];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        String expected = "012345";
        String actual = new String(buffer);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 12345;
        byte[] buffer = new byte[13];
        TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        String expected = "012345 ";
        String actual = new String(buffer, 0, 7) + new String(new char[]{32, 0});
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatLongOctalOrBinaryBytes() {
        long value = 12345;
        byte[] buffer = new byte[10];
        TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        long expected = 012345;
        long actual = ByteBuffer.wrap(buffer).getLong();
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatLongOctalOrBinaryBytesTooLong() {
        long value = 12345678901L;
        byte[] buffer = new byte[5];
        TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = TarUtils.computeCheckSum(new byte[]{0, 1, 2, 3, 4});
        byte[] buffer = new byte[5];
        TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        String expected = "012345 ";
        String actual = new String(buffer, 0, 7) + new String(new char[]{32, 0});
        assertEquals(expected, actual);
    }
}