package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '4', '2'};
        assertEquals(017542L, TarUtils.parseOctal(buffer, 0, buffer.length));

        byte[] invalidBuffer = new byte[]{'0', '8', '5', '4', '2'}; // Leading octal digit is invalid
        assertThrows(IllegalArgumentException.class, () -> TarUtils.parseOctal(invalidBuffer, 0, invalidBuffer.length));

        byte[] emptyBuffer = new byte[0];
        assertNull(TarUtils.parseOctal(emptyBuffer, 0, emptyBuffer.length));

        byte[] excessBuffer = new byte[]{'0', '7', '5', '4', '2', '3'};
        assertEquals(017542L, TarUtils.parseOctal(excessBuffer, 0, excessBuffer.length));
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'a', 'b', 'c', 'd', 'e', '0'};
        assertEquals("abcde", TarUtils.parseName(buffer, 0, buffer.length));

        byte[] emptyBuffer = new byte[0];
        assertNull(TarUtils.parseName(emptyBuffer, 0, emptyBuffer.length));

        byte[] excessBuffer = new byte[]{'a', 'b', 'c', 'd', 'e', 'f'};
        assertEquals("abcde", TarUtils.parseName(excessBuffer, 0, excessBuffer.length));
    }

    @Test
    public void testFormatNameBytes() {
        String name = "abcdef";
        byte[] buffer = new byte[12];

        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        byte[] expected = new byte[]{'a', 'b', 'c', 'd', 'e', 'f', '0', '0', '0', '0', '0', '0'};

        assertArrayEquals(expected, buffer, 0, expected.length);
        assertEquals(offset, expected.length);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123456789L;
        byte[] buffer = new byte[10];

        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);

        byte[] expected = new byte[]{'0', '1', '0', '1', '1', '4', '5', '6', '7', '8'};
        assertArrayEquals(expected, buffer, 0, expected.length);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 123456789L;
        byte[] buffer = new byte[12];

        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);

        byte[] expected = new byte[]{'0', '1', '0', '1', '1', '4', '5', '6', '7', '8', ' ', '0'};
        assertArrayEquals(expected, buffer, 0, expected.length);
        assertEquals(offset, expected.length);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 123456789L;
        byte[] buffer = new byte[11];

        int offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);

        byte[] expected = new byte[]{'0', '1', '0', '1', '1', '4', '5', '6', '7', '8'};
        assertArrayEquals(expected, buffer, 0, expected.length);
        assertEquals(offset, expected.length);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123456789L;
        byte[] buffer = new byte[13];

        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);

        byte[] expected = new byte[]{'0', '1', '0', '1', '1', '4', '5', '6', '7', '8', '0', ' ', '0'};
        assertArrayEquals(expected, buffer, 0, expected.length);
        assertEquals(offset, expected.length);
    }

}