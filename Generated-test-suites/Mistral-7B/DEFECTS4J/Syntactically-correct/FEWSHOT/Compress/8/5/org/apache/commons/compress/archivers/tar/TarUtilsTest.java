package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '7', '4', '0', '0', '0'};
        assertEquals(0177400L, TarUtils.parseOctal(buffer, 0, buffer.length));

        byte[] bufferInvalid = new byte[]{'0', '7', '8', '4', '0', '0', '0'};
        try {
            TarUtils.parseOctal(bufferInvalid, 0, bufferInvalid.length);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        byte[] bufferNull = null;
        try {
            TarUtils.parseOctal(bufferNull, 0, 7);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        byte[] bufferEmpty = new byte[0];
        assertNull(TarUtils.parseOctal(bufferEmpty, 0, 0));
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'T', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '.', 'd', 'a', 't', 'a', 0};
        assertEquals("tar.data", TarUtils.parseName(buffer, 0, buffer.length));

        byte[] bufferInvalidLength = new byte[]{'T', 'a', 'r', 0};
        try {
            TarUtils.parseName(bufferInvalidLength, 0, bufferInvalidLength.length);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        byte[] bufferInvalid = new byte[]{'T', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '.', 'd', 'a', 't', 'a', 'x', 0};
        try {
            TarUtils.parseName(bufferInvalid, 0, bufferInvalid.length);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        byte[] bufferNull = null;
        assertNull(TarUtils.parseName(bufferNull, 0, 7));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "tar.file";
        byte[] bufferExpected = new byte[]{'T', 'a', 'r', '.', 'f', 'i', 'l', 'e'};

        byte[] buffer = new byte[7];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertArrayEquals(bufferExpected, buffer, 0, buffer.length);
        assertEquals(7, offset);

        name = "tar.file.without.trailing.null";
        buffer = new byte[13];
        offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals(13, offset);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        byte[] bufferExpected = new byte[]{'0', '7', '7', '7', '4', '0', '0'};
        TarUtils.formatUnsignedOctalString(0177400L, new byte[7], 0, 7);
        assertArrayEquals(bufferExpected, new byte[7], 0, bufferExpected.length);

        byte[] bufferExpectedLong = new byte[]{'0', '1', '7', '7', '7', '7', '7', '4', '0'};
        TarUtils.formatUnsignedOctalString(177777400L, new byte[10], 0, 10);
        assertArrayEquals(bufferExpectedLong, new byte[10], 0, bufferExpectedLong.length);

        byte[] bufferExpectedShort = new byte[]{'0', '1', '7', '7'};
        TarUtils.formatUnsignedOctalString(177L, new byte[4], 0, 4);
        assertArrayEquals(bufferExpectedShort, new byte[4], 0, bufferExpectedShort.length);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 177777400L;
        byte[] bufferExpected = new byte[]{'0', '1', '7', '7', '7', '7', '7', '4', '0', ' '};

        byte[] buffer = new byte[11];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(bufferExpected, buffer, 0, buffer.length);
        assertEquals(11, offset);

        value = 177L;
        byte[] bufferExpectedShort = new byte[]{'0', '1', '7', '7'};

        byte[] bufferShort = new byte[5];
        offset = TarUtils.formatOctalBytes(value, bufferShort, 0, bufferShort.length);
        assertArrayEquals(bufferExpectedShort, bufferShort, 0, bufferExpectedShort.length);
        assertEquals(5, offset);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 177777400L;
        byte[] bufferExpected = new byte[]{'0', '1', '7', '7', '7', '7', '7', '4'};

        byte[] buffer = new byte[9];
        int offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(bufferExpected, buffer, 0, buffer.length);
        assertEquals(9, offset);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 177777400L;
        byte[] bufferExpected = new byte[]{'0', '1', '7', '7', '7', '7', '7', '4', '0', ' '};

        byte[] buffer = new byte[11];
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(bufferExpected, buffer, 0, buffer.length);
        assertEquals(11, offset);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{'T', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '.', 'd', 'a', 't', 'a'};
        long checksum = TarUtils.computeCheckSum(buffer);
        // This checksum is for "tar.data"
        assertEquals(2117161117L, checksum);
    }
}