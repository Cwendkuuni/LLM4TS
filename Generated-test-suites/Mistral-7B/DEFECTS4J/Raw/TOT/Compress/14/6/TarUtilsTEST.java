package org.apache.commons.compress.archivers.tar;

import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', ' ', '0'};
        long expected = 01755;
        long actual = TarUtils.parseOctal(buffer, 0, 6);
        assertEquals(expected, actual);

        buffer = new byte[]{'0', '0'};
        expected = 0;
        actual = TarUtils.parseOctal(buffer, 0, 2);
        assertEquals(expected, actual);

        buffer = new byte[]{'0'};
        try {
            actual = TarUtils.parseOctal(buffer, 0, 1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        buffer = new byte[]{'7', '8'};
        try {
            actual = TarUtils.parseOctal(buffer, 0, 2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        buffer = new byte[]{0x12, '1', '6', '7', '5', '5', ' ', '0'};
        try {
            actual = TarUtils.parseOctal(buffer, 0, 7);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected, leading NUL not detected before Commons Compress 1.4
        }
    }

    @Test
    public void testParseOctalOrBinary() {
        byte[] buffer1 = new byte[]{0x41, '7', '5', '5', ' ', '0'};
        long expected1 = 01755;
        long actual1 = TarUtils.parseOctalOrBinary(buffer1, 0, 6);
        assertEquals(expected1, actual1);

        byte[] buffer2 = new byte[]{0x80, '1', '6', '7', '5', '5'};
        long expected2 = 163855;
        long actual2 = TarUtils.parseOctalOrBinary(buffer2, 0, 5);
        assertEquals(expected2, actual2);

        byte[] buffer3 = new byte[]{0x80, '1', '6', '7', '5', '5', '1'};
        long expected3 = 1677761;
        long actual3 = TarUtils.parseOctalOrBinary(buffer3, 0, 7);
        assertEquals(expected3, actual3);

        byte[] buffer4 = new byte[]{0x80, '1'};
        try {
            actual4 = TarUtils.parseOctalOrBinary(buffer4, 0, 2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected, length < 2
        }

        byte[] buffer5 = new byte[]{0x80, '1', '6', '7', '5', '5', '1', '1'};
        try {
            actual5 = TarUtils.parseOctalOrBinary(buffer5, 0, 8);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected, value too large
        }
    }

    @Test
    public void testParseBoolean() {
        byte[] buffer1 = new byte[]{1};
        boolean expected1 = true;
        boolean actual1 = TarUtils.parseBoolean(buffer1, 0);
        assertEquals(expected1, actual1);

        byte[] buffer2 = new byte[]{0};
        boolean expected2 = false;
        boolean actual2 = TarUtils.parseBoolean(buffer2, 0);
        assertEquals(expected2, actual2);

        byte[] buffer3 = new byte[]{0x12};
        try {
            actual3 = TarUtils.parseBoolean(buffer3, 0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseName() {
        byte[] buffer1 = new byte[]{'a', 'b', 'c', 'd', 'e', 0};
        String expected1 = "abcde";
        String actual1 = TarUtils.parseName(buffer1, 0, 6);
        assertEquals(expected1, actual1);

        byte[] buffer2 = new byte[]{'a', 'b', 'c', 'd', 0};
        String expected2 = "abcd";
        String actual2 = TarUtils.parseName(buffer2, 0, 5);
        assertEquals(expected2, actual2);

        byte[] buffer3 = new byte[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 0};
        String expected3 = "abcdefghi";
        String actual3 = TarUtils.parseName(buffer3, 0, 11);
        assertEquals(expected3, actual3);

        byte[] buffer4 = new byte[]{'a', 'b', 'c', 'd', 'e', 0x12};
        try {
            actual4 = TarUtils.parseName(buffer4, 0, 6);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFormatNameBytes() {
        String name = "abcdefghijklmnopqrstuvwxyz";
        byte[] buffer1 = new byte[11];
        int offset = TarUtils.formatNameBytes(name, buffer1, 0, 11);
        byte[] expected = new byte[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 0};
        assertArrayEquals(expected, buffer1);
        assertEquals(offset, 11);

        name = "abc";
        offset = TarUtils.formatNameBytes(name, buffer1, 0, 4);
        expected = new byte[]{'a', 'b', 'c', 0};
        assertArrayEquals(expected, buffer1);
        assertEquals(offset, 4);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value1 = 01755;
        byte[] buffer1 = new byte[7];
        TarUtils.formatUnsignedOctalString(value1, buffer1, 0, 6);
        byte[] expected1 = new byte[]{'0', '1', '7', '5', '5', ' ', '0'};
        assertArrayEquals(expected1, buffer1);

        long value2 = 0;
        byte[] buffer2 = new byte[3];
        TarUtils.formatUnsignedOctalString(value2, buffer2, 0, 3);
        byte[] expected2 = new byte[]{'0', '0', 0};
        assertArrayEquals(expected2, buffer2);

        long value3 = 163855;
        byte[] buffer3 = new byte[6];
        TarUtils.formatUnsignedOctalString(value3, buffer3, 0, 5);
        byte[] expected3 = new byte[]{'0', '1', '6', '7', '5', 0};
        assertArrayEquals(expected3, buffer3);

        long value4 = 1677761;
        byte[] buffer4 = new byte[8];
        TarUtils.formatUnsignedOctalString(value4, buffer4, 0, 7);
        byte[] expected4 = new byte[]{'0', '1', '6', '7', '7', '5', '5', 0};
        assertArrayEquals(expected4, buffer4);
    }

    @Test
    public void testFormatOctalBytes() {
        long value1 = 01755;
        byte[] buffer1 = new byte[8];
        TarUtils.formatOctalBytes(value1, buffer1, 0, 6);
        byte[] expected1 = new byte[]{'0', '1', '7', '5', '5', ' ', '0', ' '};
        assertArrayEquals(expected1, buffer1);

        long value2 = 0;
        byte[] buffer2 = new byte[6];
        TarUtils.formatOctalBytes(value2, buffer2, 0, 6);
        byte[] expected2 = new byte[]{'0', '0', '0', '0', '0', '0'};
        assertArrayEquals(expected2, buffer2);
    }

    @Test
    public void testFormatLongOctalOrBinaryBytes() {
        long value1 = 01755;
        byte[] buffer1 = new byte[7];
        TarUtils.formatLongOctalOrBinaryBytes(value1, buffer1, 0, 6);
        byte[] expected1 = new byte[]{'0', '1', '7', '5', '5', ' ', '0'};
        assertArrayEquals(expected1, buffer1);

        long value2 = 163855;
        byte[] buffer2 = new byte[6];
        TarUtils.formatLongOctalOrBinaryBytes(value2, buffer2, 0, 5);
        byte[] expected2 = new byte[]{0x80, '1', '6', '7', '5', 0};
        assertArrayEquals(expected2, buffer2);

        long value3 = 1677761;
        byte[] buffer3 = new byte[8];
        TarUtils.formatLongOctalOrBinaryBytes(value3, buffer3, 0, 7);
        byte[] expected3 = new byte[]{0x80, '1', '6', '7', '7', '5', '5', 0};
        assertArrayEquals(expected3, buffer3);

        long value4 = Long.MAX_VALUE;
        byte[] buffer4 = new byte[9];
        try {
            TarUtils.formatLongOctalOrBinaryBytes(value4, buffer4, 0, 8);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}