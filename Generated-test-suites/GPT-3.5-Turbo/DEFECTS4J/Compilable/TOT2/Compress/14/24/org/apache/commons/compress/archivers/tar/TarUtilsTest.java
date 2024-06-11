package org.apache.commons.compress.archivers.tar;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{0x31, 0x32, 0x33, 0x20};
        assertEquals(123L, TarUtils.parseOctal(buffer, 0, 3));
    }

    @Test
    public void testParseOctalOrBinary() {
        byte[] buffer = new byte[]{(byte)0x80, 0x01, 0x02};
        assertEquals(32898L, TarUtils.parseOctalOrBinary(buffer, 0, 3));
    }

    @Test
    public void testParseBoolean() {
        byte[] buffer = new byte[]{0x01};
        assertTrue(TarUtils.parseBoolean(buffer, 0));
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{0x41, 0x42, 0x00};
        assertEquals("AB", TarUtils.parseName(buffer, 0, 3));
    }

    @Test
    public void testFormatNameBytes() {
        byte[] buffer = new byte[4];
        int offset = TarUtils.formatNameBytes("CD", buffer, 0, 4);
        assertEquals("CD\0", new String(buffer, 0, 3));
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        byte[] buffer = new byte[4];
        int offset = TarUtils.formatCheckSumOctalBytes(123L, buffer, 0, 4);
        assertEquals("123 ", new String(buffer, 0, 4));
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{0x1, 0x2, 0x3, 0x4};
        assertEquals(10L, TarUtils.computeCheckSum(buffer));
    }
}