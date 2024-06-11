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
        byte[] buffer = new byte[]{(byte) 0777, (byte) 0666, (byte) 0111, (byte) 0};
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(0177706660111L, result);

        buffer = new byte[]{(byte) 0, (byte) 0, (byte) 0};
        result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(0L, result);

        buffer = new byte[]{(byte) 0};
        try {
            result = TarUtils.parseOctal(buffer, 0, buffer.length);
            assertFalse(true);
        } catch (IllegalArgumentException e) {
            // Expected
        }

        buffer = new byte[]{(byte) 0x81, (byte) 0x01};
        try {
            result = TarUtils.parseOctal(buffer, 0, buffer.length);
            assertFalse(true);
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseOctalOrBinary() {
        byte[] buffer1 = new byte[]{(byte) 077, (byte) 0};
        long result1 = TarUtils.parseOctalOrBinary(buffer1, 0, buffer1.length);
        assertEquals(0177L, result1);

        byte[] buffer2 = new byte[]{(byte) 0x80, (byte) 0x41};
        long result2 = TarUtils.parseOctalOrBinary(buffer2, 0, buffer2.length);
        assertEquals(325121L, result2);

        byte[] buffer3 = new byte[]{(byte) 0x80, (byte) 0x12, (byte) 0x34};
        long result3 = TarUtils.parseOctalOrBinary(buffer3, 0, buffer3.length);
        assertEquals(268435456 + 512 + 8388608 + 33554432L, result3); // 0x80801234

        byte[] buffer4 = new byte[]{(byte) 0};
        try {
            result4 = TarUtils.parseOctalOrBinary(buffer4, 0, buffer4.length);
            assertFalse(true);
        } catch (IllegalArgumentException e) {
            // Expected
        }

        byte[] buffer5 = new byte[]{(byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0x80};
        try {
            result5 = TarUtils.parseOctalOrBinary(buffer5, 0, buffer5.length);
            assertFalse(true);
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseBoolean() {
        byte[] buffer1 = new byte[]{(byte) 1};
        boolean result1 = TarUtils.parseBoolean(buffer1, 0);
        assertTrue(result1);

        byte[] buffer2 = new byte[]{(byte) 0};
        boolean result2 = TarUtils.parseBoolean(buffer2, 0);
        assertFalse(result2);

        byte[] buffer3 = new byte[]{(byte) 2};
        try {
            result3 = TarUtils.parseBoolean(buffer3, 0);
            assertFalse(true);
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseName() {
        byte[] buffer1 = new byte[]{(byte) 0x41, (byte) 0x54, (byte) 0x41, (byte) 0x54, (byte) 0, (byte) 0};
        String result1 = TarUtils.parseName(buffer1, 0, buffer1.length);
        assertEquals("DATA", result1);

        byte[] buffer2 = new byte[]{(byte) 0x41};
        String result2 = TarUtils.parseName(buffer2, 0, buffer2.length);
        assertEquals("A", result2);

        byte[] buffer3 = new byte[]{(byte) 0};
        String result3 = TarUtils.parseName(buffer3, 0, buffer3.length);
        assertNull(result3);
    }

    @Test
    public void testFormatNameBytes() {
        String name1 = "NAME1";
        byte[] buffer1 = new byte[10];
        int result1 = TarUtils.formatNameBytes(name1, buffer1, 0, buffer1.length);
        assertEquals(name1.length(), result1);
        assertArrayEquals(name1.getBytes(), buffer1, 0, name1.length());

        String name2 = "NAME2";
        byte[] buffer2 = new byte[5];
        int result2 = TarUtils.formatNameBytes(name2, buffer2, 0, buffer2.length);
        assertEquals(name2.length(), result2);
        assertArrayEquals(name2.getBytes(), buffer2, 0, name2.length());

        String name3 = "NAME3";
        byte[] buffer3 = new byte[20];
        int result3 = TarUtils.formatNameBytes(name3, buffer3, 0, buffer3.length);
        assertEquals(name3.length(), result3);
        assertArrayEquals(name3.getBytes(), buffer3, 0, name3.length());

        String name4 = "";
        byte[] buffer4 = new byte[1];
        int result4 = TarUtils.formatNameBytes(name4, buffer4, 0, buffer4.length);
        assertEquals(0, result4);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value1 = 0177706660111L;
        byte[] buffer1 = new byte[10];
        TarUtils.formatUnsignedOctalString(value1, buffer1, 0, buffer1.length);
        assertEquals("0177706660111", new String(buffer1));

        long value2 = 0;
        byte[] buffer2 = new byte[1];
        TarUtils.formatUnsignedOctalString(value2, buffer2, 0, buffer2.length);
        assertEquals("0", new String(buffer2));
    }

    @Test
    public void testFormatOctalBytes() {
        long value1 = 0177706660111L;
        byte[] buffer1 = new byte[13];
        int result1 = TarUtils.formatOctalBytes(value1, buffer1, 0, buffer1.length);
        assertEquals(13, result1);
        assertArrayEquals(new String("0177706660111\0 ").getBytes(), buffer1, 0, 13);

        long value2 = 0;
        byte[] buffer2 = new byte[3];
        int result2 = TarUtils.formatOctalBytes(value2, buffer2, 0, buffer2.length);
        assertEquals(3, result2);
        assertArrayEquals(new String("000").getBytes(), buffer2, 0, 3);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value1 = 0177706660111L;
        byte[] buffer1 = new byte[12];
        int result1 = TarUtils.formatLongOctalBytes(value1, buffer1, 0, buffer1.length);
        assertEquals(12, result1);
        assertArrayEquals(new String("0177706660111").getBytes(), buffer1, 0, 12);

        long value2 = 0;
        byte[] buffer2 = new byte[3];
        int result2 = TarUtils.formatLongOctalBytes(value2, buffer2, 0, buffer2.length);
        assertEquals(3, result2);
        assertArrayEquals(new String("000").getBytes(), buffer2, 0, 3);
    }

    @Test
    public void testFormatLongOctalOrBinaryBytes() {
        long value1 = 0177706660111L;
        byte[] buffer1 = new byte[13];
        int result1 = TarUtils.formatLongOctalOrBinaryBytes(value1, buffer1, 0, buffer1.length);
        assertEquals(13, result1);
        assertArrayEquals(new String("0177706660111\0 ").getBytes(), buffer1, 0, 13);

        long value2 = 0x1234567890ABCDEF0L;
        byte[] buffer2 = new byte[11];
        int result2 = TarUtils.formatLongOctalOrBinaryBytes(value2, buffer2, 0, buffer2.length);
        assertEquals(11, result2);
        assertArrayEquals(new String("01234567543210ABCDEF0").getBytes(), buffer2, 0, 11);
    }

    @Test
    public void testFormatCheckSumOctalBytes() {
        long value1 = 335544321L;
        byte[] buffer1 = new byte[5];
        int result1 = TarUtils.formatCheckSumOctalBytes(value1, buffer1, 0, buffer1.length);
        assertEquals(5, result1);
        assertArrayEquals(new String("33551\0 ").getBytes(), buffer1, 0, 5);

        long value2 = 0;
        byte[] buffer2 = new byte[3];
        int result2 = TarUtils.formatCheckSumOctalBytes(value2, buffer2, 0, buffer2.length);
        assertEquals(3, result2);
        assertArrayEquals(new String("000").getBytes(), buffer2, 0, 3);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{0x1A, 0x2B, 0x3C, 0x4D, 0x5E, 0x6F, 0x77};
        long checksum1 = TarUtils.computeCheckSum(buffer);
        long checksum2 = 394165451703449L;
        assertEquals(checksum2, checksum1);
    }
}