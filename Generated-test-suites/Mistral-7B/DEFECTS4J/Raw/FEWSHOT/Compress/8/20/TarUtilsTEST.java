package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{-65, '1', '7', '1', '5', '0', '2', '3', '7', '5'};
        assertEquals(01111111111111011115L, TarUtils.parseOctal(buffer, 0, buffer.length));

        byte[] emptyBuffer = new byte[0];
        assertEquals(0L, TarUtils.parseOctal(emptyBuffer, 0, emptyBuffer.length));

        byte[] invalidByteBuffer = new byte[]{-65, '1', '7', '1', '5', '0', '2', '3', '7', 'X'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(invalidByteBuffer, 0, invalidByteBuffer.length));
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '.', 't', 'a', 'r', '\0'};
        assertEquals("tar.tar", TarUtils.parseName(buffer, 0, buffer.length));

        byte[] emptyBuffer = new byte[0];
        assertNull(TarUtils.parseName(emptyBuffer, 0, emptyBuffer.length));

        byte[] invalidByteBuffer = new byte[]{'t', 'a', 'r', '.', 't', 'a', 'r', '\0'};
        assertNull(TarUtils.parseName(invalidByteBuffer, 0, invalidByteBuffer.length));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "my_file.tar";
        byte[] buffer = new byte[256];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);

        assertEquals(name.length(), offset);
        for (int i = 0; i < offset; ++i) {
            assertEquals((byte) name.charAt(i), buffer[i]);
        }
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 0xFFFFFFFFFFFFFFFFL;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);

        assertArrayEquals(new byte[]{'0', '7', '7', '7', '7', '7', '7', '7', '7', '7'}, buffer);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 0xFFFFFFFFFFFFFFFFL;
        byte[] buffer = new byte[12];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);

        assertArrayEquals(new byte[]{'0', '7', '7', '7', '7', '7', '7', '7', '7', '7', ' ', '0'}, buffer);
        assertEquals(11, offset);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 0xFFFFFFFFFFFFFFFFL;
        byte[] buffer = new byte[11];
        int offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);

        assertArrayEquals(new byte[]{'0', '7', '7', '7', '7', '7', '7', '7', '7', '7'}, buffer);
        assertEquals(10, offset);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 0xFFFFFFFFFFFFFFFFL;
        byte[] buffer = new byte[12];
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);

        assertArrayEquals(new byte[]{'0', '7', '7', '7', '7', '7', '7', '7', '7', '7', '0', ' '}, buffer);
        assertEquals(11, offset);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{'t', 'a', 'r', ' ', 'f', 'i', 'l', 'e', '.', 't', 'a', 'r', '\0'};
        long checksum = TarUtils.computeChecksum(buffer);

        long nameChecksum = 0;
        for (byte b : buffer) {
            nameChecksum += TarUtils.BYTE_MASK & b;
        }
        assertEquals(nameChecksum, checksum);
    }
}