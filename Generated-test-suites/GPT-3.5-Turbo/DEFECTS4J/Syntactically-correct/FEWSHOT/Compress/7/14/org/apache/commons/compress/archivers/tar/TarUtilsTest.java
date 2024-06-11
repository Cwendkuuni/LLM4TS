
package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class TarUtilsTest {
    @Test
    public void testParseOctal() {
        byte[] buffer = {'0'};
        assertEquals(0, TarUtils.parseOctal(buffer, 0, 1));

        byte[] buffer2 = {'1', '2', '3', '4'};
        assertEquals(1234, TarUtils.parseOctal(buffer2, 0, 4));

        byte[] buffer3 = {'0', '2', '7'};
        assertEquals(27, TarUtils.parseOctal(buffer3, 0, 3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidInput() {
        byte[] buffer = {'a', 'b', 'c'};
        TarUtils.parseOctal(buffer, 0, 3);
    }

    @Test
    public void testParseName() {
        byte[] buffer = {'h', 'e', 'l', 'l', 'o', 0};
        assertEquals("hello", TarUtils.parseName(buffer, 0, 5));

        byte[] buffer2 = {'w', 'o', 'r', 'l', 'd', 0};
        assertEquals("world", TarUtils.parseName(buffer2, 0, 5));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "hello";
        byte[] buf = new byte[8];
        int offset = 0;
        int length = 8;
        int newOffset = TarUtils.formatNameBytes(name, buf, offset, length);
        assertEquals(8, newOffset);
        byte[] expectedBuf = {'h', 'e', 'l', 'l', 'o', 0, 0, 0};
        assertArrayEquals(expectedBuf, buf);
    }

    @Test
    public void testFormatNameBytesTruncatedOutput() {
        String name = "hello";
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;
        int newOffset = TarUtils.formatNameBytes(name, buf, offset, length);
        assertEquals(5, newOffset);
        byte[] expectedBuf = {'h', 'e', 'l', 'l', 'o'};
        assertArrayEquals(expectedBuf, buf);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buffer = new byte[3];
        int offset = 0;
        int length = 3;
        TarUtils.formatUnsignedOctalString(123, buffer, offset, length);
        byte[] expectedBuffer = {'1', '7', '3'};
        assertArrayEquals(expectedBuffer, buffer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringTruncatedOutput() {
        byte[] buffer = new byte[2];
        int offset = 0;
        int length = 2;
        TarUtils.formatUnsignedOctalString(123, buffer, offset, length);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 123;
        byte[] buf = new byte[8];
        int offset = 0;
        int length = 8;
        int newOffset = TarUtils.formatOctalBytes(value, buf, offset, length);
        assertEquals(8, newOffset);
        byte[] expectedBuf = {'0', '0', '0', '0', '0', '1', '2', '3'};
        assertArrayEquals(expectedBuf, buf);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatOctalBytesTruncatedOutput() {
        long value = 123;
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;
        TarUtils.formatOctalBytes(value, buf, offset, length);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 123;
        byte[] buf = new byte[8];
        int offset = 0;
        int length = 8;
        int newOffset = TarUtils.formatLongOctalBytes(value, buf, offset, length);
        assertEquals(8, newOffset);
        byte[] expectedBuf = {'0', '0', '0', '0', '0', '0', '1', '2'};
        assertArrayEquals(expectedBuf, buf);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatLongOctalBytesTruncatedOutput() {
        long value = 123;
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;
        TarUtils.formatLongOctalBytes(value, buf, offset, length);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123;
        byte[] buf = new byte[8];
        int offset = 0;
        int length = 8;
        int newOffset = TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
        assertEquals(8, newOffset);
        byte[] expectedBuf = {'0', '0', '0', '0', '0', '1', '2', '3'};
        assertArrayEquals(expectedBuf, buf);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatCheckSumOctalBytesTruncatedOutput() {
        long value = 123;
        byte[] buf = new byte[5];
        int offset = 0;
        int length = 5;
        TarUtils.formatCheckSumOctalBytes(value, buf, offset, length);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = {'1', '2', '3', '4'};
        long expectedSum = 49 + 50 + 51 + 52;
        long checksum = TarUtils.computeCheckSum(buf);
        assertEquals(expectedSum, checksum);
    }
}
