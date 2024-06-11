package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal_validCases() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '4', '4', '3'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(0175544L, result);

        byte[] buffer2 = new byte[]{'0', '1', '7', '7', '7', '7', '7'};
        result = TarUtils.parseOctal(buffer2, 0, buffer2.length);
        assertEquals(0177777L, result);

        byte[] buffer3 = new byte[]{'0', '6', '6', '6', '6', '6', '6'};
        result = TarUtils.parseOctal(buffer3, 0, buffer3.length);
        assertEquals(0177777L, result);

        byte[] buffer4 = new byte[]{'0'};
        result = TarUtils.parseOctal(buffer4, 0, buffer4.length);
        assertEquals(0L, result);
    }

    @Test
    public void testParseOctal_invalidCases() {
        byte[] buffer = new byte[]{'0', '1', 'a', '5', '4', '4', '3'};
        assertThrows(IllegalArgumentException.class, () -> {
            TarUtils.parseOctal(buffer, 0, buffer.length);
        });

        byte[] buffer2 = new byte[]{'0', '7', '5', '5', '4', '4', 'X'};
        assertThrows(IllegalArgumentException.class, () -> {
            TarUtils.parseOctal(buffer2, 0, buffer2.length);
        });

        byte[] buffer3 = new byte[]{'0'};
        assertThrows(IllegalArgumentException.class, () -> {
            TarUtils.parseOctal(buffer3, 0, 0);
        });

        byte[] buffer4 = new byte[]{'0', '1', '1', '1', '1', '1'};
        assertThrows(IllegalArgumentException.class, () -> {
            TarUtils.parseOctal(buffer4, 0, buffer4.length);
        });

        byte[] buffer5 = new byte[]{'0', '-', '1', '1', '1', '1'};
        assertThrows(IllegalArgumentException.class, () -> {
            TarUtils.parseOctal(buffer5, 0, buffer5.length);
        });
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'a', 'p', 'a', 'c', 'h', 'e', '.', 't', 'a', 'r', '.', 'g', 'z', 'i', 'p', '.', 'c', 'o', 'm', 'p', 'r', 'e', 's', 's', 'z', '.', 't', 'a', 'r', '.', 'g', 'z', 'i', 'p'};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("apache.compress.tar.gz.compress", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "apache.compress.tar.gz.compress";
        byte[] buffer = new byte[32];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);

        assertEquals(32, offset);
        byte[] expected = new byte[]{
            (byte) 0x61, (byte) 0x68, (byte) 0x65, (byte) 0x63, (byte) 0x68, (byte) 0x61, (byte) 0x70, (byte) 0x71, (byte) 0x73, (byte) 0x74,
            (byte) 0x61, (byte) 0x73, (byte) 0x70, (byte) 0x71, (byte) 0x72, (byte) 0x63, (byte) 0x6F, (byte) 0x6D, (byte) 0x65,
            (byte) 0x72, (byte) 0x76, (byte) 0x73, (byte) 0x73, (byte) 0x63, (byte) 0x61, (byte) 0x74, (byte) 0x65, (byte) 0x72,
            (byte) 0x65, (byte) 0x6E, (byte) 0x63, (byte) 0x6F, (byte) 0x64, (byte) 0x65, (byte) 0x64, (byte) 0x65, (byte) 0x72,
            (byte) 0x65, (byte) 0x73, (byte) 0x74, (byte) 0x61, (byte) 0x72, (byte) 0x73, (byte) 0x63, (byte) 0x61, (byte) 0x70,
            (byte) 0x75, (byte) 0x74, (byte) 0x69, (byte) 0x6F, (byte) 0x6E
        };
        assertArrayEquals(expected, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatUnsignedOctalString_validCases() {
        long value = 0175544L;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{"0", "01", "175", "544"}, buffer);
    }

    @Test
    public void testFormatUnsignedOctalString_invalidLength() {
        long value = 0175544L;
        byte[] buffer = new byte[5];
        assertThrows(IllegalArgumentException.class, () -> {
            TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        });
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 0175544L;
        byte[] buffer = new byte[12];
        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{"0", "01", "175", "544", " ", "0"}, buffer);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 017554412345L;
        byte[] buffer = new byte[12];
        TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{"0", "01", "175", "544", "1", "2", "3", "4", "5"}, buffer);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 1234567890L;
        byte[] buffer = new byte[12];
        TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{"0", "2", "1", "0", "7", "5", "5", "4", "6", "7", "8", "9", "0", " "}, buffer);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{0x75, 0x73, 0x74, 0x65, 0x64, 0x00};
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(0x7573746564L, result);
    }

    @Test
    public void testFormatCheckSumOctalBytes_zeroLength() {
        byte[] buffer = new byte[]{};
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(0L, result);
    }

    @Test
    public void testFormatCheckSumOctalBytes_nullInput() {
        byte[] buffer = null;
        assertThrows(IllegalArgumentException.class, () -> {
            TarUtils.computeCheckSum(buffer);
        });
    }
}