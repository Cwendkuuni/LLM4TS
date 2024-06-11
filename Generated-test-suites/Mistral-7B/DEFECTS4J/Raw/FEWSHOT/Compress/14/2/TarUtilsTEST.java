package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{0, 0, '4', '4', '4', '4', '4', '4', '4', '4'};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(01777777L, result);

        buffer = new byte[]{'0'};
        try {
            TarUtils.parseOctal(buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        buffer = new byte[]{'1', '7', '7', '7'};
        try {
            TarUtils.parseOctal(buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        buffer = new byte[]{'7', '7', '7', '7'};
        try {
            TarUtils.parseOctal(buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        buffer = new byte[]{'7', '7'};
        try {
            TarUtils.parseOctal(buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        buffer = new byte[]{0};
        assertEquals(0L, TarUtils.parseOctal(buffer, 0, buffer.length));

        buffer = new byte[]{0, 0};
        assertEquals(0L, TarUtils.parseOctal(buffer, 0, buffer.length));

        buffer = new byte[]{0, 0, 0};
        assertEquals(0L, TarUtils.parseOctal(buffer, 0, buffer.length));

        buffer = new byte[]{0, 0, 0, 0};
        assertEquals(0L, TarUtils.parseOctal(buffer, 0, buffer.length));
    }

    @Test
    public void testParseOctalOrBinary() {
        byte[] buffer = new byte[]{0, 0, '4', '4', '4', '4', '4', '4', '4', '4'};
        long result = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(01777777L, result);

        buffer = new byte[]{0x80, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01};
        long result2 = TarUtils.parseOctalOrBinary(buffer, 0, buffer.length);
        assertEquals(0x01010101010101L, result2);

        byte[] buffer2 = new byte[]{0x80, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, (byte) 0x80, 0x01};
        try {
            TarUtils.parseOctalOrBinary(buffer2, 0, buffer2.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseBoolean() {
        byte[] buffer = new byte[]{1};
        assertTrue(TarUtils.parseBoolean(buffer, 0));

        buffer = new byte[]{0};
        assertFalse(TarUtils.parseBoolean(buffer, 0));

        buffer = new byte[]{0, 1};
        try {
            TarUtils.parseBoolean(buffer, 0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'a', 'b', 'c'};
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals("abc", result);

        buffer = new byte[]{0, 0};
        assertEquals("", TarUtils.parseName(buffer, 0, buffer.length));

        buffer = new byte[]{'a', 'b', 'c', 0};
        assertEquals("abc", TarUtils.parseName(buffer, 0, buffer.length));

        buffer = new byte[]{'a', 'b', (byte) 0x80, 'c'};
        try {
            TarUtils.parseName(buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFormatNameBytes() {
        String name = "abc";
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals(10, offset);
        assertArrayEquals(new byte[]{'a', 'b', 'c', 0, 0, 0, 0, 0, 0, 0}, buffer);

        name = "";
        offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals(10, offset);
        assertArrayEquals(new byte[]{'', '', '', 0, 0, 0, 0, 0, 0, 0}, buffer);

        name = "ab";
        offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals(10, offset);
        assertArrayEquals(new byte[]{'a', 'b', 0, 0, 0, 0, 0, 0, 0, 0}, buffer);

        name = "ab\u0000";
        offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals(10, offset);
        assertArrayEquals(new byte[]{'a', 'b', 0, 0, 0, 0, 0, 0, 0, 0}, buffer);

        name = "ab\u0080";
        offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        try {
            TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 017777777777L;
        byte[] buffer = new byte[10];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '7', '7', '7', '7', '7', '7', '7', '7'}, buffer);

        value = 0;
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0'}, buffer);

        value = 0177777L;
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '7', '7', '7'}, buffer);

        value = 0777L;
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '7', '7'}, buffer);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 017777777777L;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(10, offset);
        assertArrayEquals(new byte[]{'0', '1', '7', '7', '7', '7', '7', '7', '7', '7'}, buffer);

        value = 0;
        offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(10, offset);
        assertArrayEquals(new byte[]{'0', '0'}, buffer);

        value = 0177777L;
        offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(10, offset);
        assertArrayEquals(new byte[]{'0', '1', '7', '7', '7'}, buffer);

        value = 0777L;
        offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(10, offset);
        assertArrayEquals(new byte[]{'0', '7', '7'}, buffer);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 017777777777L;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(10, offset);
        assertArrayEquals(new byte[]{'0', '1', '7', '7', '7', '7', '7', '7', '7'}, buffer);

        value = 0;
        offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(10, offset);
        assertArrayEquals(new byte[]{'0', '0'}, buffer);

        value = 0177777L;
        offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(10, offset);
        assertArrayEquals(new byte[]{'0', '1', '7', '7', '7'}, buffer);

        value = 0777L;
        offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(10, offset);
        assertArrayEquals(new byte[]{'0', '7', '7'}, buffer);
    }

    @Test
    public void testFormatLongOctalOrBinaryBytes() {
        long value = 017777777777L;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        assertEquals(10, offset);
        assertArrayEquals(new byte[]{'0', '1', '7', '7', '7', '7', '7', '7', '7'}, buffer);

        value = 0xfedcba987654321L;
        offset = TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        try {
            TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 017777777777L;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(10, offset);
        assertArrayEquals(new byte[]{'0', '1', '7', '7', '7', '7', '7', '7', '7', '0'}, buffer);

        value = 0;
        offset = TarUtils.formatCheckSumOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(10, offset);
        assertArrayEquals(new byte[]{'0', '0', '0', '0', '0', '0', '0', '0', '0', '0'}, buffer);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{0, 0, 1, 2, 3, 4};
        long checksum = TarUtils.computeCheckSum(buffer);
        assertEquals(0x9c2fdaa2L, checksum);

        buffer = new byte[0];
        assertEquals(0L, TarUtils.computeCheckSum(buffer));

        buffer = new byte[]{0};
        assertEquals(0L, TarUtils.computeCheckSum(buffer));
    }
}