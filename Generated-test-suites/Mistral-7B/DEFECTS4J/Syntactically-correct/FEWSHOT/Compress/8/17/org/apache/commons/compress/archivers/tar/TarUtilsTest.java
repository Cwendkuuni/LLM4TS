package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '4', '0', '0'}; // 0111 1101 1101 1100 1100 0000
        assertEquals(0144640L, TarUtils.parseOctal(buffer, 0, buffer.length));

        byte[] buffer1 = new byte[]{'0', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1'}; // 0111 1111 1111 1111 1111 1111 1111 1111 1111 1111 1111 1111 1111 1111 1111 1111 1111
        assertEquals(0200000012345L, TarUtils.parseOctal(buffer1, 0, buffer1.length));

        byte[] buffer2 = new byte[]{'0'}; // empty buffer
        assertNull(TarUtils.parseOctal(buffer2, 0, buffer2.length));

        byte[] buffer3 = new byte[]{'5', 'a'}; // invalid byte detected
        throw new AssertionError("Unexpected call to parseOctal with invalid byte 5a");
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'f', 'o', 'o', ' ', 'b', 'a', 'r', 0};
        assertEquals("foo bar", TarUtils.parseName(buffer, 0, buffer.length));

        byte[] buffer1 = new byte[]{'f', 'o', 'o', 0};
        assertEquals("foo", TarUtils.parseName(buffer1, 0, buffer1.length));

        byte[] buffer2 = new byte[]{'a', 'b', 'c', 'd', 'e', 0};
        assertEquals("abcd e", TarUtils.parseName(buffer2, 0, buffer2.length));

        byte[] buffer3 = new byte[]{'0'}; // empty buffer
        assertNull(TarUtils.parseName(buffer3, 0, buffer3.length));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "foo bar";
        byte[] buffer = new byte[11];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'f', 'o', 'o', ' ', 'b', 'a', 'r', 0, 0, 0, 0}, buffer, 0, offset + 10);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345;
        byte[] buffer = new byte[6];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '2', '0', '1', '1', '1'}, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 12345;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '2', '0', '1', '1', '1', ' ', '0'}, buffer, 0, offset + 10);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 1234567890L;
        byte[] buffer = new byte[9];
        int offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '2', '0', '1', '1', '1', '7', '8'}, buffer, 0, offset + 9);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 1234567890L;
        byte[] buffer = new byte[11];
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '2', '0', '1', '1', '1', '7', '8', '0', ' '}, buffer, 0, offset + 11);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{'f', 'o', 'o', ' ', 'b', 'a', 'r', ' ', '1', '2'};
        assertEquals(0x736f6f6261616212L, TarUtils.computeCheckSum(buffer));
    }
}