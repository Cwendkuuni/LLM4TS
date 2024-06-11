package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{(byte) '0', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '7', (byte) '0'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(01777770L, result);

        byte[] buffer2 = new byte[]{(byte) '0', (byte) '7', (byte) '7', (byte) '7', (byte) '7'};
        long result2 = TarUtils.parseOctal(buffer2, 0, buffer2.length);
        assertEquals(0177777L, result2);

        byte[] buffer3 = new byte[]{};
        try {
            TarUtils.parseOctal(buffer3, 0, buffer3.length);
            assert false; // should throw exception
        } catch (IllegalArgumentException e) {
            // expected
        }

        byte[] buffer4 = new byte[]{(byte) '1', (byte) '8'};
        try {
            TarUtils.parseOctal(buffer4, 0, buffer4.length);
            assert false; // should throw exception
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{(byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'o', (byte) 'p', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '.', (byte) 'm', (byte) 'y', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'x', (byte) 'z', (byte) '.'};
        String name = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("tar.gz.my.tar.xz.", name);

        byte[] buffer2 = new byte[]{};
        String name2 = TarUtils.parseName(buffer2, 0, buffer2.length);
        assertNull(name2);

        byte[] buffer3 = new byte[]{(byte) ' '};
        String name3 = TarUtils.parseName(buffer3, 0, buffer3.length);
        assertNull(name3);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "test.tar.gz";
        byte[] buffer = new byte[name.length() + 1];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertArrayEquals(name.getBytes(), new byte[]{'t', 'e', 's', 't', '.', 't', 'a', 'r', '.', 'g', 'z'}, buffer, 0, offset);

        String name2 = "";
        byte[] buffer2 = new byte[name2.length() + 1];
        int offset2 = TarUtils.formatNameBytes(name2, buffer2, 0, buffer2.length);
        assertArrayEquals(new byte[]{0}, buffer2, 0, offset2);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 65535;
        byte[] buffer = new byte[7];
        int offset = TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{"0", "07771", "77655", "535"}, buffer, 0, offset);

        long value2 = -1;
        try {
            TarUtils.formatUnsignedOctalString(value2, new byte[1], 0, 1);
            assert false; // should throw exception
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 65535;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{"0", "07771", "77655", "535", " ", "0"}, buffer, 0, offset);

        long value2 = -1;
        try {
            TarUtils.formatOctalBytes(value2, new byte[1], 0, 1);
            assert false; // should throw exception
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 65535;
        byte[] buffer = new byte[9];
        int offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{"0", "07771", "77655", "535"}, buffer, 0, offset);

        long value2 = -1;
        try {
            TarUtils.formatLongOctalBytes(value2, new byte[1], 0, 1);
            assert false; // should throw exception
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 12345678901234567890L;
        byte[] buffer = new byte[11];
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{"0", "206553512134567", "677553343567890"}, buffer, 0, offset);

        long value2 = -1;
        try {
            TarUtils.formatCheckSumOctalBytes(value2, new byte[1], 0, 1);
            assert false; // should throw exception
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{(byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'o', (byte) 'p', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p'};
        long checksum = TarUtils.computeChecksum(buffer);
        long computedChecksum = 0;
        for (byte b : buffer) {
            computedChecksum += TarUtils.BYTE_MASK & b;
        }
        assertEquals(computedChecksum, checksum);
    }
}