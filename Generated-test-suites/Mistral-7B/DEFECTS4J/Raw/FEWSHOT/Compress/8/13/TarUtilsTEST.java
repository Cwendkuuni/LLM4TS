package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{0, '7', '5', '4', '1', '0', '1', '2', '0', '1'};
        assertEquals(01754101201L, TarUtils.parseOctal(buffer, 0, buffer.length));

        byte[] bufferInvalid = new byte[]{0, '8', '5', '4', '1', '0', '1', '2', '0', '1'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(bufferInvalid, 0, bufferInvalid.length));

        byte[] bufferNull = null;
        assertNull(TarUtils.parseOctal(bufferNull, 0, 0));

        byte[] bufferEmpty = new byte[]{};
        assertEquals(0L, TarUtils.parseOctal(bufferEmpty, 0, 0));
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{"name".getBytes()};
        assertEquals("name", TarUtils.parseName(buffer, 0, buffer.length));

        byte[] bufferEmpty = new byte[]{};
        assertNull(TarUtils.parseName(bufferEmpty, 0, 0));

        byte[] bufferTrailingNull = new byte[]{"name\0".getBytes()};
        assertEquals("name", TarUtils.parseName(bufferTrailingNull, 0, bufferTrailingNull.length));

        byte[] bufferTrailingNullAndSpace = new byte[]{"name\0 ".getBytes()};
        assertEquals("name", TarUtils.parseName(bufferTrailingNullAndSpace, 0, bufferTrailingNullAndSpace.length));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "name";
        byte[] buffer = new byte[25];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, 25);
        assertEquals("name".getBytes().length, offset);
        assertArrayEquals(name.getBytes(), Arrays.copyOfRange(buffer, 0, offset));
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345;
        byte[] buffer = new byte[10];

        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);

        assertArrayEquals(new byte[]{"0", "077", "155", "012", "111", "045", "034", "051", "025", "05"}, buffer);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 12345;
        byte[] buffer = new byte[12];

        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);

        assertArrayEquals(new byte[]{"0", "077", "155", "012", "111", "045", "034", "051", "025", "05", ' ', '0'}, buffer);
        assertEquals(offset, 12);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 123456789;
        byte[] buffer = new byte[11];

        int offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);

        assertArrayEquals(new byte[]{"017", "075", "156", "067", "011", "101", "054", "032", "010", "043", ' '}, buffer);
        assertEquals(offset, 11);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123456789;
        byte[] buffer = new byte[13];

        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);

        assertArrayEquals(new byte[]{"017", "143", "175", "164", "101", "111", "110", "111", "111", "043", "032", "0", ' '}, buffer);
        assertEquals(offset, 13);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{"name\0 size\0 mode\0".getBytes()};
        long checksum = TarUtils.computeCheckSum(buffer);
        // The checksum of "name\0 size\0 mode\0" is 1482536073
        assertEquals(1482536073L, checksum);
    }
}