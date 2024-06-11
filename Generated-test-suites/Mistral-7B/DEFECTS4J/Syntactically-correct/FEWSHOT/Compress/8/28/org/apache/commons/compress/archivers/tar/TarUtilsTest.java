package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{(byte) '7', (byte) '5', (byte) '4', (byte) '0'};
        assertEquals(017540L, TarUtils.parseOctal(buffer, 0, buffer.length));

        byte[] buffer2 = new byte[]{(byte) '0', (byte) '1', (byte) '1', (byte) '1'};
        assertEquals(00000111L, TarUtils.parseOctal(buffer2, 0, buffer2.length));

        byte[] buffer3 = new byte[]{(byte) '1', (byte) '2', (byte) '3'};
        try {
            TarUtils.parseOctal(buffer3, 0, buffer3.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        byte[] buffer4 = new byte[]{};
        assertEquals(0L, TarUtils.parseOctal(buffer4, 0, buffer4.length));
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{(byte) 'f', (byte) 'o', (byte) 'o', (byte) '.', (byte) 'b', (byte) 'a', (byte) 'r'};
        assertEquals("foo.bar", TarUtils.parseName(buffer, 0, buffer.length));

        byte[] buffer2 = new byte[]{(byte) '.'};
        assertEquals("", TarUtils.parseName(buffer2, 0, buffer2.length));

        byte[] buffer3 = new byte[]{(byte) 'a', (byte) '.', (byte) 'b', (byte) '.', (byte) 'c', (byte) '.'};
        assertEquals("a.b.c.", TarUtils.parseName(buffer3, 0, buffer3.length));

        byte[] buffer4 = new byte[]{(byte) ' ', (byte) 'a', (byte) '.', (byte) 'b', (byte) '.', (byte) 'c', (byte) ' ', (byte) 'd'};
        assertEquals(" a.b.c d", TarUtils.parseName(buffer4, 0, buffer4.length));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "foo.bar";
        byte[] buffer = new byte[17];

        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals("foo.bar".getBytes().length, offset);

        assertArrayEquals(new byte[]{'f', 'o', 'o', (byte) '.', 'b', 'a', 'r', (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0}, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        byte[] buffer = new byte[10];

        TarUtils.formatUnsignedOctalString(0123L, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '2', '3'}, buffer, 0, buffer.length);

        TarUtils.formatUnsignedOctalString(065432L, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '0', '1', '5', '4', '3', '2'}, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatOctalBytes() {
        byte[] buffer = new byte[8];

        TarUtils.formatOctalBytes(012345L, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '2', '3', '4', '5', ' ', '0'}, buffer, 0, buffer.length);

        TarUtils.formatOctalBytes(0654321L, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '0', '1', '5', '4', '3', '2'}, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatLongOctalBytes() {
        byte[] buffer = new byte[8];

        TarUtils.formatLongOctalBytes(0x123456789012345L, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '2', '3', '4', '5', '6', '7'}, buffer, 0, buffer.length);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        byte[] buffer = new byte[11];

        TarUtils.formatCheckSumOctalBytes(0x123456789012345L, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '0', ' ', '0'}, buffer, 0, buffer.length);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{0, 1, 2, 3};
        assertEquals(0x0fL, TarUtils.computeCheckSum(buffer));

        byte[] buffer2 = new byte[]{0};
        assertEquals(0x00L, TarUtils.computeCheckSum(buffer2));

        byte[] buffer3 = new byte[]{(byte) 0xFF};
        assertEquals(0xFFL, TarUtils.computeCheckSum(buffer3));
    }
}