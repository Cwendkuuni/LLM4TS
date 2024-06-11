package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class TarUtilsTest {
    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '7', '\u0000'};
        assertEquals(01777777L, TarUtils.parseOctal(buffer, 0, buffer.length));

        buffer = new byte[]{'0', '7', '7', '7', '7', '7', '7', '7', '7', '\u0000'};
        assertEquals(0177777L, TarUtils.parseOctal(buffer, 0, 11));

        buffer = new byte[]{'0', '7', '\u0000'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer, 0, 2));

        buffer = new byte[]{'1', '\u0001'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(buffer, 0, 2));
    }

    @Test
    public void testParseName() {
        byte[] buffer = "filename\u0000".getBytes();
        assertEquals("filename", TarUtils.parseName(buffer, 0, buffer.length));

        buffer = "longfilename\u0000".getBytes();
        assertEquals("longfilename", TarUtils.parseName(buffer, 0, buffer.length));

        buffer = new byte[0];
        assertNull(TarUtils.parseName(buffer, 0, 0));

        buffer = new byte[]{'\u0000'};
        assertNull(TarUtils.parseName(buffer, 0, 1));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "filename";
        byte[] buffer = new byte[256];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, 256);

        assertEquals("filename\u0000".length(), offset);
        assertArrayEquals("filename\u0000".getBytes(), Arrays.copyOfRange(buffer, 0, offset));
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 1777777L;
        byte[] buffer = new byte[8];
        int offset = TarUtils.formatUnsignedOctalString(value, buffer, 0, 7);

        assertEquals("1777775\u0000".getBytes().length, offset);
        assertArrayEquals("1777775\u0000".getBytes(), Arrays.copyOfRange(buffer, 0, offset));
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 1777777L;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, 9);

        assertEquals("0177775 \u0000".getBytes().length, offset);
        assertArrayEquals("0177775 \u0000".getBytes(), Arrays.copyOfRange(buffer, 0, offset));
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 177777777777L;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatLongOctalBytes(value, buffer, 0, 9);

        assertEquals("01777777775 \u0000".getBytes().length, offset);
        assertArrayEquals("01777777775 \u0000".getBytes(), Arrays.copyOfRange(buffer, 0, offset));
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123456789123L;
        byte[] buffer = new byte[5];
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, 4);

        assertEquals("33333333333\u0000".getBytes().length, offset);
        assertArrayEquals("33333333333\u0000".getBytes(), Arrays.copyOfRange(buffer, 0, offset));
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = "header\u0000".getBytes();
        long checksum = TarUtils.computeChecksum(buffer);
        byte[] expected = new byte[]{0x94, (byte) 0xC1, (byte) 0x9D, '\u0000'};

        assertArrayEquals(expected, Arrays.copyOfRange(buffer, 0, 4));
        assertEquals(0x94C19D00L, checksum);
    }
}