package org.apache.commons.compress.archivers.tar;
import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_InvalidDigit() {
        byte[] buffer = {'1', '2', '3'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    @Test
    public void testParseOctal_ValidInput() {
        byte[] buffer = {'1', '0', '0'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(64, result);
    }

    @Test
    public void testParseOctal_TrailingNull() {
        byte[] buffer = {'1', '0', '0', 0};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(64, result);
    }

    @Test
    public void testParseOctal_TrailingSpace() {
        byte[] buffer = {'1', '0', '0', ' '};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(64, result);
    }

    @Test
    public void testParseName() {
        byte[] buffer = {'n', 'a', 'm', 'e', 0};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("name", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "name";
        byte[] buffer = new byte[4];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals(4, offset);
        assertEquals('n', buffer[0]);
        assertEquals('a', buffer[1]);
        assertEquals('m', buffer[2]);
        assertEquals('e', buffer[3]);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buffer = new byte[4];
        TarUtils.formatUnsignedOctalString(64, buffer, 0, buffer.length);
        assertEquals('1', buffer[0]);
        assertEquals('0', buffer[1]);
        assertEquals('0', buffer[2]);
        assertEquals(0, buffer[3]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalString_ValueTooLarge() {
        byte[] buffer = new byte[1];
        TarUtils.formatUnsignedOctalString(512, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatOctalBytes() {
        byte[] buffer = new byte[4];
        int offset = TarUtils.formatOctalBytes(64, buffer, 0, buffer.length);
        assertEquals(4, offset);
        assertEquals('1', buffer[0]);
        assertEquals('0', buffer[1]);
        assertEquals('0', buffer[2]);
        assertEquals(' ', buffer[3]);
    }

    @Test
    public void testFormatLongOctalBytes() {
        byte[] buffer = new byte[3];
        int offset = TarUtils.formatLongOctalBytes(64, buffer, 0, buffer.length);
        assertEquals(3, offset);
        assertEquals('1', buffer[0]);
        assertEquals('0', buffer[1]);
        assertEquals(' ', buffer[2]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatCheckSumOctalBytes_ValueTooLarge() {
        byte[] buffer = new byte[1];
        TarUtils.formatCheckSumOctalBytes(512, buffer, 0, buffer.length);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = {'1', '2', '3', '4'};
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(466, result);
    }
}