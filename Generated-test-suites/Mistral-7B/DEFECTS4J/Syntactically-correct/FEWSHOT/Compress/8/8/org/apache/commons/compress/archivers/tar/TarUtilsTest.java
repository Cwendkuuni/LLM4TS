package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '4', '7', '5', '5', '\u0000'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(0175475L, result);

        buffer = new byte[]{'0', '\u0000'};
        result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(0L, result);

        buffer = new byte[]{'5', '\u0000'};
        try {
            result = TarUtils.parseOctal(buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        buffer = new byte[]{'7', '\u0001'};
        try {
            result = TarUtils.parseOctal(buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'t', 'a', 'r', '\u0000'};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("tar", result);

        buffer = new byte[]{'t', 'a', 'r', '.', 'g', 'z', '\u0000'};
        result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("tar.gz", result);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "tar.gz";
        byte[] buffer = new byte[20];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals(11, offset);
        assertArrayEquals(new byte[]{'t', 'a', 'r', '.', 'g', 'z', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000'}, buffer, 0, offset);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 123;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertEquals(9, offset);
        assertArrayEquals(new byte[]{'0', '1', '2', '3', '0', '7', '5', '5', '5', '\u0000'}, buffer, 0, offset);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 123;
        byte[] buffer = new byte[12];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(12, offset);
        assertArrayEquals(new byte[]{'0', '1', '2', '3', ' ', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000'}, buffer, 0, offset);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 123456789;
        byte[] buffer = new byte[12];
        int offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(12, offset);
        assertArrayEquals(new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ' ', '\u0000'}, buffer, 0, offset);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123456789;
        byte[] buffer = new byte[12];
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(12, offset);
        assertArrayEquals(new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '\u0000', '\u0000'}, buffer, 0, offset);
    }
}