package org.apache.commons.compress.archivers.tar;

import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal_ValidOctalString() {
        byte[] buffer = "0777".getBytes();
        int offset = 0;
        int length = 3;
        long expected = 0777L;

        long result = TarUtils.parseOctal(buffer, offset, length);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_InvalidOctalString_MissesTrailingSpaceOrNUL() {
        byte[] buffer = "0777".getBytes();
        int offset = 0;
        int length = 3;

        TarUtils.parseOctal(buffer, offset, length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_InvalidOctalString_LeadingNUL() {
        byte[] buffer = new byte[]{0, '7', '7', '7'};
        int offset = 0;
        int length = 3;

        TarUtils.parseOctal(buffer, offset, length);
    }

    @Test
    public void testParseOctal_InvalidOctalString_InvalidByte() {
        byte[] buffer = "077X".getBytes();
        int offset = 0;
        int length = 4;

        try {
            TarUtils.parseOctal(buffer, offset, length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseOctalOrBinary_ValidBinaryNumber() {
        byte[] buffer = new byte[]{0x55, 0x55, 0x55, 0x55, 0x55, 0x55};
        int offset = 0;
        int length = 6;

        long expected = 220775L;

        long result = TarUtils.parseOctalOrBinary(buffer, offset, length);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalOrBinary_InvalidOctalOrBinaryNumber_LengthLessThanTwo() {
        byte[] buffer = new byte[]{0x55};
        int offset = 0;
        int length = 1;

        TarUtils.parseOctalOrBinary(buffer, offset, length);
    }

    @Test
    public void testParseBoolean_ValidBooleanByte() {
        byte[] buffer = new byte[]{1};
        int offset = 0;

        boolean expected = true;

        boolean result = TarUtils.parseBoolean(buffer, offset);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseBoolean_InvalidBooleanByte() {
        byte[] buffer = new byte[]{0};
        int offset = 0;

        TarUtils.parseBoolean(buffer, offset);
    }

    @Test
    public void testParseName_ValidName() {
        byte[] buffer = "filename.txt".getBytes();
        int offset = 0;
        int length = "filename.txt".length();

        String expected = "filename.txt";

        String result = TarUtils.parseName(buffer, offset, length);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatNameBytes_ValidName() {
        String name = "filename.txt";
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = name.length();

        offset = TarUtils.formatNameBytes(name, buffer, offset, length);

        String result = new String(buffer, 0, offset);
        assertEquals(name, result);
    }

    @Test
    public void testFormatUnsignedOctalString_ValidValue() {
        long value = 1234567;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;

        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);

        String expected = "01111111111\t";
        String result = new String(buffer, 0, offset + length);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatOctalBytes_ValidValue() {
        long value = 1234567;
        byte[] buffer = new byte[11];
        int offset = 0;
        int length = 11;

        TarUtils.formatOctalBytes(value, buffer, offset, length);

        String expected = "01111111111\ts\0";
        String result = new String(buffer, 0, offset + length);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatLongOctalOrBinaryBytes_ValidValue() {
        long value = 123456789012345L;
        byte[] buffer = new byte[15];
        int offset = 0;
        int length = 15;

        TarUtils.formatLongOctalOrBinaryBytes(value, buffer, offset, length);

        String expected = "01234567777555566615\ts\0";
        String result = new String(buffer, 0, offset + length);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatLongOctalOrBinaryBytes_ValueTooLargeForOctal() {
        long value = 1234567890123456789L;
        byte[] buffer = new byte[15];
        int offset = 0;
        int length = 15;

        try {
            TarUtils.formatLongOctalOrBinaryBytes(value, buffer, offset, length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = "header\000".getBytes();
        long expected = TarUtils.computeCheckSum(buffer);

        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(expected, result);
    }
}