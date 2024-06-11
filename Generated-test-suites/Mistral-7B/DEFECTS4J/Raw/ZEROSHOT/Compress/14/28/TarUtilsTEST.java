package org.apache.commons.compress.archivers.tar;

import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', ' '};
        long expected = 01755L;
        long actual = TarUtils.parseOctal(buffer, 0, 5);
        assertEquals(expected, actual);

        buffer = new byte[]{'0', '1', '7', '7', '5', '5', ' '};
        expected = 0117755L;
        actual = TarUtils.parseOctal(buffer, 0, 7);
        assertEquals(expected, actual);

        buffer = new byte[]{'7', '7', '7', '7'};
        long actual2 = TarUtils.parseOctal(buffer, 0, 4);
        assertEquals(0L, actual2);

        try {
            byte[] buffer3 = new byte[]{0x01};
            TarUtils.parseOctal(buffer3, 0, 1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            byte[] buffer4 = new byte[]{'0', '1', '0'};
            TarUtils.parseOctal(buffer4, 0, 3);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseOctalOrBinary() {
        byte[] buffer = new byte[]{0x81, 0x12, 0x34, 0x56, 0x78};
        long expected = 0x81123456L;
        long actual = TarUtils.parseOctalOrBinary(buffer, 0, 5);
        assertEquals(expected, actual);

        buffer = new byte[]{0x80, 0x11, 0x22, 0x33, 0x44, 0x55, 0x66, 0x77, 0x88};
        long expected2 = 0x801122334455667788L;
        long actual2 = TarUtils.parseOctalOrBinary(buffer, 0, 9);
        assertEquals(expected2, actual2);

        byte[] buffer3 = new byte[]{0x80, 0x11, 0x22, 0x33, 0x44, 0x55, 0x66, 0x77};
        long expected3 = 0x8011223344556677L;
        long actual3 = TarUtils.parseOctalOrBinary(buffer3, 0, 8);
        assertEquals(expected3, actual3);

        try {
            byte[] buffer4 = new byte[]{0x81, 0x12, 0x34, 0x56, 0x78, 0x89};
            TarUtils.parseOctalOrBinary(buffer4, 0, 6);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
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

        byte[] buffer3 = new byte[]{0x81};
        boolean expected3 = true;
        boolean actual3 = TarUtils.parseBoolean(buffer3, 0);
        assertEquals(expected3, actual3);

        try {
            byte[] buffer4 = new byte[]{0x00};
            TarUtils.parseBoolean(buffer4, 0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{0x68, 0x65, 0x6c, 0x6c, 0x6f, 0x00};
        String expected = "hello";
        String actual = TarUtils.parseName(buffer, 0, 6);
        assertEquals(expected, actual);

        byte[] buffer2 = new byte[]{0x68, 0x65, 0x6c, 0x6c, 0x6f, 0x6e, 0x64, 0x6F, 0x77, 0x73, 0x00};
        String expected2 = "helloworld";
        String actual2 = TarUtils.parseName(buffer2, 0, 12);
        assertEquals(expected2, actual2);
    }

    @Test
    public void testFormatNameBytes() {
        String name = "hello";
        byte[] buffer = new byte[10];
        int expected = TarUtils.formatNameBytes(name, buffer, 0, 10);
        TarUtils.parseName(buffer, 0, 10);
        assertEquals(name, new String(buffer, 0, expected));

        name = "hello world";
        buffer = new byte[20];
        expected = TarUtils.formatNameBytes(name, buffer, 0, 20);
        TarUtils.parseName(buffer, 0, 20);
        assertEquals(name, new String(buffer, 0, expected));
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 01755L;
        byte[] buffer = new byte[12];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, 11);
        String expected = "000001755";
        String actual = new String(buffer, 0, 11);
        assertEquals(expected, actual);

        value = 0117755L;
        buffer = new byte[15];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, 14);
        expected = "000117755";
        actual = new String(buffer, 0, 14);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 01755L;
        byte[] buffer = new byte[13];
        TarUtils.formatOctalBytes(value, buffer, 0, 12);
        String expected = "00000000001755 ";
        String actual = new String(buffer, 0, 13);
        assertEquals(expected, actual);

        value = 0117755L;
        buffer = new byte[17];
        TarUtils.formatOctalBytes(value, buffer, 0, 16);
        expected = "000000000117755 ";
        actual = new String(buffer, 0, 17);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 01755L;
        byte[] buffer = new byte[12];
        TarUtils.formatLongOctalBytes(value, buffer, 0, 11);
        String expected = "000001755";
        String actual = new String(buffer, 0, 11);
        assertEquals(expected, actual);

        value = 0117755L;
        buffer = new byte[15];
        TarUtils.formatLongOctalBytes(value, buffer, 0, 14);
        expected = "000117755";
        actual = new String(buffer, 0, 14);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatLongOctalOrBinaryBytes() {
        long value = 0x81123456L;
        byte[] buffer = new byte[9];
        TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, 8);
        String expected = "01123456";
        String actual = new String(buffer, 0, 8);
        assertEquals(expected, actual);

        value = 0x801122334455667788L;
        buffer = new byte[17];
        TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, 16);
        expected = "0111223344556677";
        actual = new String(buffer, 0, 16);
        assertEquals(expected, actual);

        value = 0x807FFFFFFF807FFFFFL;
        buffer = new byte[19];
        TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, 18);
        expected = "0777777777777777";
        actual = new String(buffer, 0, 18);
        assertEquals(expected, actual);

        byte[] buffer2 = new byte[8];
        try {
            TarUtils.formatLongOctalOrBinaryBytes(0x807FFFFFFF807FFFFFL, buffer2, 0, 7);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{0x01, 0x23, 0x45, 0x67, 0x89, 0xab, 0xcd, 0xef};
        long expected = (0x01 << 8) | (0x23 & 0xFF) | ((0x45 & 0xFF) << 8)
                | ((0x67 & 0xFF) << 16) | ((0x89 & 0xFF) << 24) | ((0xab & 0xFF) << 32)
                | ((0xcd & 0xFF) << 40) | ((0xef & 0xFF) << 48);
        long actual = TarUtils.computeCheckSum(buffer);
        assertEquals(expected, actual);
    }
}