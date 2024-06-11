
package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.*;

import org.junit.Test;

public class TarUtilsTest {
    @Test
    public void testParseOctal() {
        byte[] buffer = {1, 2, 3};
        int offset = 0;
        int length = buffer.length;
        long result = TarUtils.parseOctal(buffer, offset, length);
        assertEquals(16909060, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_Exception() {
        byte[] buffer = {1, -2, 3};
        int offset = 0;
        int length = buffer.length;
        TarUtils.parseOctal(buffer, offset, length);
    }

    @Test
    public void testParseName() {
        byte[] buffer = {65, 66, 67};
        int offset = 0;
        int length = buffer.length;
        String result = TarUtils.parseName(buffer, offset, length);
        assertEquals("ABC", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "ABC";
        byte[] buf = new byte[3];
        int offset = 0;
        int length = buf.length;
        int result = TarUtils.formatNameBytes(name, buf, offset, length);
        assertEquals(3, result);
        assertArrayEquals(new byte[]{65, 66, 67}, buf);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123;
        byte[] buffer = new byte[3];
        int offset = 0;
        int length = buffer.length;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        assertArrayEquals(new byte[]{49, 50, 51}, buffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalString_Exception() {
        long value = 1000;
        byte[] buffer = new byte[3];
        int offset = 0;
        int length = buffer.length;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 123;
        byte[] buf = new byte[3];
        int offset = 0;
        int length = buf.length;
        int result = TarUtils.formatOctalBytes(value, buf, offset, length);
        assertEquals(3, result);
        assertArrayEquals(new byte[]{49, 50, 0}, buf);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 123;
        byte[] buf = new byte[3];
        int offset = 0;
        int length = buf.length;
        int result = TarUtils.formatLongOctalBytes(value, buf, offset, length);
        assertEquals(3, result);
        assertArrayEquals(new byte[]{49, 50, 32}, buf);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123;
        byte[] buf = new byte[3];
        int offset = 0;
        int length = buf.length;
        int result = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
        assertEquals(3, result);
        assertArrayEquals(new byte[]{49, 50, 0}, buf);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = {1, 2, 3};
        long result = TarUtils.computeCheckSum(buf);
        assertEquals(6, result);
    }
}
