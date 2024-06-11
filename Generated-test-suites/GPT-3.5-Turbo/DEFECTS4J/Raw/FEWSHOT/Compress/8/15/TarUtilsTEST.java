
package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer1 = { '1', '2', '3', '4', '5', '6', '7', '8', '9', '\0' };
        byte[] buffer2 = { ' ', ' ', '1', '2', '3', '4', '5', '6', '7', '8', '9', '\0' };
        assertEquals(123456789L, TarUtils.parseOctal(buffer1, 0, 10));
        assertEquals(123456789L, TarUtils.parseOctal(buffer2, 2, 10));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalWithInvalidByte() {
        byte[] buffer = { '1', '2', '3', '4', '5', '6', '7', '8', 'A', '\0' };
        TarUtils.parseOctal(buffer, 0, 9);
    }

    @Test
    public void testParseName() {
        byte[] buffer1 = { 'f', 'o', 'o', ' ', 'b', 'a', 'r', '\0' };
        byte[] buffer2 = { 'f', 'o', 'o', ' ', 'b', 'a', 'r', ' ', 'b', 'a', 'z', '\0' };
        assertEquals("foo bar", TarUtils.parseName(buffer1, 0, 8));
        assertEquals("foo bar ", TarUtils.parseName(buffer2, 0, 12));
    }

    @Test
    public void testFormatNameBytes() {
        byte[] buffer1 = new byte[8];
        byte[] buffer2 = new byte[12];
        assertEquals(8, TarUtils.formatNameBytes("foo", buffer1, 0, 8));
        assertEquals(12, TarUtils.formatNameBytes("foo bar ", buffer2, 0, 12));
    }

    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buffer1 = new byte[8];
        byte[] buffer2 = new byte[12];
        TarUtils.formatUnsignedOctalString(123456789L, buffer1, 0, 8);
        TarUtils.formatUnsignedOctalString(123456789L, buffer2, 0, 12);
        assertEquals("123456789", new String(buffer1));
        assertEquals("123456789", new String(buffer2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringWithInvalidLength() {
        byte[] buffer = new byte[9];
        TarUtils.formatUnsignedOctalString(123456789L, buffer, 0, 9);
    }

    @Test
    public void testFormatOctalBytes() {
        byte[] buffer1 = new byte[8];
        byte[] buffer2 = new byte[12];
        assertEquals(8, TarUtils.formatOctalBytes(123456789L, buffer1, 0, 8));
        assertEquals(12, TarUtils.formatOctalBytes(123456789L, buffer2, 0, 12));
        assertEquals("123456789 ", new String(buffer1));
        assertEquals("123456789 ", new String(buffer2));
    }

    @Test
    public void testFormatLongOctalBytes() {
        byte[] buffer1 = new byte[8];
        byte[] buffer2 = new byte[12];
        assertEquals(8, TarUtils.formatLongOctalBytes(123456789L, buffer1, 0, 8));
        assertEquals(12, TarUtils.formatLongOctalBytes(123456789L, buffer2, 0, 12));
        assertEquals("123456789", new String(buffer1));
        assertEquals("123456789", new String(buffer2));
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        byte[] buffer1 = new byte[8];
        byte[] buffer2 = new byte[12];
        assertEquals(8, TarUtils.formatCheckSumOctalBytes(123456789L, buffer1, 0, 8));
        assertEquals(12, TarUtils.formatCheckSumOctalBytes(123456789L, buffer2, 0, 12));
        assertEquals("123456789 ", new String(buffer1));
        assertEquals("123456789 ", new String(buffer2));
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = { '1', '2', '3', '4', '5', '6', '7', '8', '9', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '\0' };
        assertEquals(123456789, TarUtils.computeCheckSum(buffer));
    }
}
