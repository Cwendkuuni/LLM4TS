package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(01234567L, result);

        assertEquals(0L, TarUtils.parseOctal(new byte[]{}, 0, 0));
        assertEquals(0L, TarUtils.parseOctal(new byte[]{'0'}, 0, 1));
        assertEquals(07777777777L, TarUtils.parseOctal(new byte[]{'7', '7', '7', '7', '7', '7', '7', '7', '7'}, 0, 9));

        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(new byte[]{'1'}, 0, 1));
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(new byte[]{'8'}, 0, 1));
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(new byte[]{'9'}, 0, 1));

        byte[] invalidBytes = new byte[]{'a', 'b', 'c'};
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(invalidBytes, 0, invalidBytes.length));
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'a', 'b', 'c', 0};
        String name = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("abc", name);

        byte[] invalidBuffer = new byte[]{'a', 'b', 0, 0};
        assertNull(TarUtils.parseName(invalidBuffer, 0, invalidBuffer.length));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "abc";
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'a', 'b', 'c', 0, 0, 0, 0, 0, 0, 0}, buffer);
        assertEquals(10, offset);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '0', '4', '4', '5', '7', '5', '6', '4', '4'}, buffer);
        assertEquals(10, offset);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 12345;
        byte[] buffer = new byte[12];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '0', '4', '4', '5', '7', '5', '6', '4', '4', ' ', '0'}, buffer);
        assertEquals(12, offset);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 123456789012L;
        byte[] buffer = new byte[11];
        int offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '3', '6', '6', '7', '5', '4', '5', '6', '7'}, buffer);
        assertEquals(11, offset);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123456789012L;
        byte[] buffer = new byte[13];
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '3', '6', '6', '7', '5', '4', '5', '6', '7', '0', ' '}, buffer);
        assertEquals(13, offset);
    }
}