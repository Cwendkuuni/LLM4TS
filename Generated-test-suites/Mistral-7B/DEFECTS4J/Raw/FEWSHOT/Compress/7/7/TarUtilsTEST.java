package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TarUtilsTest {
    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{-64, 75, 65, 76};
        assertEquals(0640102L, TarUtils.parseOctal(buffer, 0, buffer.length));

        byte[] bufferNull = new byte[0];
        assertNull(TarUtils.parseOctal(bufferNull, 0, bufferNull.length));

        byte[] bufferTrailingNull = new byte[]{1, 2, 3, 0};
        assertEquals(12L, TarUtils.parseOctal(bufferTrailingNull, 0, bufferTrailingNull.length));

        byte[] bufferTrailingSpace = new byte[]{1, 2, 3, 32};
        assertEquals(12L, TarUtils.parseOctal(bufferTrailingSpace, 0, bufferTrailingSpace.length));

        byte[] bufferInvalidLength = new byte[]{1, 2, 3, 4, 5, 6, 7, 8};
        assertEquals(56294995344L, TarUtils.parseOctal(bufferInvalidLength, 0, bufferInvalidLength.length));

        byte[] bufferInvalidDigit = new byte[]{1, 9, 3, 4, 5, 6, 7, 8};
        try {
            TarUtils.parseOctal(bufferInvalidDigit, 0, bufferInvalidDigit.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'a', 'b', 'c', 0};
        assertEquals("abc", TarUtils.parseName(buffer, 0, buffer.length));

        byte[] bufferNull = new byte[0];
        assertNull(TarUtils.parseName(bufferNull, 0, bufferNull.length));

        byte[] bufferTrailingNull = new byte[]{1, 2, 3, 0};
        assertEquals("123", TarUtils.parseName(bufferTrailingNull, 0, bufferTrailingNull.length - 1));

        byte[] bufferTrailingSpace = new byte[]{1, 2, 3, 32};
        assertEquals("123", TarUtils.parseName(bufferTrailingSpace, 0, bufferTrailingSpace.length - 1));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "abc";
        byte[] buf = new byte[10];
        int offset = TarUtils.formatNameBytes(name, buf, 0, buf.length);
        assertArrayEquals(new byte[]{'a', 'b', 'c', 0, 0, 0, 0, 0, 0, 0}, buf, 0, offset);

        name = "123";
        offset = TarUtils.formatNameBytes(name, buf, 0, buf.length);
        assertArrayEquals(new byte[]{'1', '2', '3', 0, 0, 0, 0, 0, 0, 0}, buf, 0, offset);

        name = "";
        offset = TarUtils.formatNameBytes(name, buf, 0, buf.length);
        assertArrayEquals(new byte[]{}, buf, 0, offset);

        name = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_+/";
        offset = TarUtils.formatNameBytes(name, buf, 0, buf.length);
        assertTrue(offset >= buf.length);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345;
        byte[] buf = new byte[10];
        int offset = TarUtils.formatUnsignedOctalString(value, buf, 0, buf.length);
        assertArrayEquals(new byte[]{'0', '0', '1', '2', '3', '4', '5', '0', '0', '0'}, buf, 0, offset);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 12345;
        byte[] buf = new byte[10];
        int offset = TarUtils.formatOctalBytes(value, buf, 0, buf.length);
        assertArrayEquals(new byte[]{'0', '0', '1', '2', '3', '4', '5', ' ', 0}, buf, 0, offset);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 12345;
        byte[] buf = new byte[10];
        int offset = TarUtils.formatLongOctalBytes(value, buf, 0, buf.length);
        assertArrayEquals(new byte[]{'0', '0', '1', '2', '3', '4', '5', ' ', 0}, buf, 0, offset);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 12345;
        byte[] buf = new byte[10];
        int offset = TarUtils.formatCheckSumOctalBytes(value, buf, 0, buf.length);
        assertArrayEquals(new byte[]{'\u0000', '0', '0', '1', '2', '3', '4', '5', ' ', '\u0000'}, buf, 0, offset);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buf = new byte[]{1, 2, 3, 4, 5};
        long checksum = TarUtils.computeCheckSum(buf);
        long computedChecksum = 0;
        for (byte b : buf) {
            computedChecksum += 0xff & b;
        }
        assertEquals(computedChecksum, checksum);
    }
}