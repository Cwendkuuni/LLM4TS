package org.apache.commons.compress.archivers.tar;
import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[] { 49, 50, 51, 52, 53, 54, 55, 56, 57, 0 };
        long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
        long expected = 123456789;
        assertEquals(expected, actual);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[] { 104, 101, 108, 108, 111, 0, 119, 111, 114, 108, 100, 0 };
        String actual = TarUtils.parseName(buffer, 0, buffer.length);
        String expected = "hello";
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "hello";
        byte[] buffer = new byte[10];
        int actualOffset = TarUtils.formatNameBytes(name, buffer, 0, name.length());
        int expectedOffset = name.length();
        assertArrayEquals(new byte[] { 104, 101, 108, 108, 111, 0, 0, 0, 0, 0 }, buffer);
        assertEquals(expectedOffset, actualOffset);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123456789;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[] { 49, 50, 51, 52, 53, 54, 55, 56, 57, 0 }, buffer);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 123456789;
        byte[] buffer = new byte[12];
        int actualOffset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        int expectedOffset = buffer.length;
        assertArrayEquals(new byte[] { 49, 50, 51, 52, 53, 54, 55, 56, 57, 32, 0, 0 }, buffer);
        assertEquals(expectedOffset, actualOffset);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 123456789;
        byte[] buffer = new byte[11];
        int actualOffset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        int expectedOffset = buffer.length;
        assertArrayEquals(new byte[] { 49, 50, 51, 52, 53, 54, 55, 56, 57, 32, 0 }, buffer);
        assertEquals(expectedOffset, actualOffset);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123456789;
        byte[] buffer = new byte[12];
        int actualOffset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        int expectedOffset = buffer.length;
        assertArrayEquals(new byte[] { 49, 50, 51, 52, 53, 54, 55, 56, 57, 0, 32, 0 }, buffer);
        assertEquals(expectedOffset, actualOffset);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[] { 49, 50, 51, 52, 53, 54, 55, 56, 57, 48 };
        long actual = TarUtils.computeCheckSum(buffer);
        long expected = 1234567890;
        assertEquals(expected, actual);
    }

}