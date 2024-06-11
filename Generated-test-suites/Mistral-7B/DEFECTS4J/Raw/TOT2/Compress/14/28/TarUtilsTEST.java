package org.apache.commons.compress.archivers.tar;

import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.compress.archivers.tar.TarConstants.*;

public class TarUtilsTest {

    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '0', '7', '7', '7', '7'};
        long expected = 017777L;
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    @Test
    public void testParseOctalInvalidInput() {
        byte[] buffer = new byte[]{'0', '0', '8', '7', '7', '7'}; // invalid digit
        long expectedExceptionMessage = "Invalid byte 8 at offset 3 in '000087777' len=7";
        try {
            TarUtils.parseOctal(buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            String actualExceptionMessage = e.getMessage();
            assertEquals(expectedExceptionMessage, actualExceptionMessage);
        }
    }

    @Test
    public void testParseOctalEmptyInput() {
        byte[] buffer = new byte[]{};
        long expected = 0L;
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    @Test
    public void testParseOctalMissingTrailingSpace() {
        byte[] buffer = new byte[]{'0', '0', '7', '7', '7', '7'};
        long expectedExceptionMessage = "Invalid byte array length 6, must be at least 2";
        try {
            TarUtils.parseOctal(buffer, 0, buffer.length-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            String actualExceptionMessage = e.getMessage();
            assertEquals(expectedExceptionMessage, actualExceptionMessage);
        }
    }

    @Test
    public void testParseOctalLeadingNUL() {
        byte[] buffer = new byte[]{0, '0', '7', '7', '7', '7', 0};
        long expected = 0L;
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    @Test
    public void testParseOctalAllNUL() {
        byte[] buffer = new byte[]{0};
        long expected = 0L;
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    @Test
    public void testParseOctalOrBinary() {
        byte[] buffer1 = new byte[]{'0', '1', '0', '1', '0', '1', '0', '1'};
        long expected1 = 0b01101010L;
        long result1 = TarUtils.parseOctalOrBinary(buffer1, 0, buffer1.length);
        assertEquals(expected1, result1);

        byte[] buffer2 = new byte[]{'0', '1', '1', '1', '1', '1', '1'}; // overflow
        long expectedExceptionMessage2 = "At offset 0, 7 byte binary number exceeds maximum signed long value";
        try {
            TarUtils.parseOctalOrBinary(buffer2, 0, buffer2.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            String actualExceptionMessage2 = e.getMessage();
            assertEquals(expectedExceptionMessage2, actualExceptionMessage2);
        }
    }

    @Test
    public void testParseBooleanTrue() {
        byte[] buffer = new byte[]{1};
        boolean expected = true;
        boolean result = TarUtils.parseBoolean(buffer, 0);
        assertEquals(expected, result);
    }

    @Test
    public void testParseBooleanFalse() {
        byte[] buffer = new byte[]{0};
        boolean expected = false;
        boolean result = TarUtils.parseBoolean(buffer, 0);
        assertEquals(expected, result);
    }

    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'a', 'b', 'c', 'd', 'e', 'N', 0};
        String expected = "abcd e";
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    @Test
    public void testParseNameInvalidInput() {
        byte[] buffer = new byte[]{'a', 'b', 'c', 'd', 'e', 'N', 'N'}; // trailing NUL
        String expectedExceptionMessage = "Invalid byte array length 7, must be at most 510";
        try {
            TarUtils.parseName(buffer, 0, buffer.length);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            String actualExceptionMessage = e.getMessage();
            assertEquals(expectedExceptionMessage, actualExceptionMessage);
        }
    }

    @Test
    public void testFormatNameBytes() {
        String name = "abcd e";
        byte[] buffer = new byte[26];
        int offset = 0;
        offset = TarUtils.formatNameBytes(name, buffer, offset, buffer.length);
        String result = new String(buffer, 0, offset);
        assertEquals(name, result);
    }

    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345;
        byte[] buffer = new byte[11];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        String expected = "012345";
        String result = new String(buffer, 0, 11);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatOctalBytes() {
        long value = 12345;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '2', '3', '4', '5', ' ', ' ', ' ', 'N'}, buffer);
        assertEquals(offset, 10);
    }

    @Test
    public void testFormatLongOctalBytes() {
        long value = 12345L;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatLongOctalBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'0', '1', '2', '3', '4', '5', ' ', ' ', ' ', 'N'}, buffer);
        assertEquals(offset, 10);
    }

    @Test
    public void testFormatLongOctalOrBinaryBytes() {
        long value = 12345L;
        byte[] buffer = new byte[10];
        int offset = TarUtils.formatLongOctalOrBinaryBytes(value, buffer, 0, buffer.length);
        assertArrayEquals(new byte[]{'\u0000', '1', '2', '3', '4', '5', ' ', ' ', ' ', 'N'}, buffer);
        assertEquals(offset, 10);
    }

    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{0, 0, 0, 1, 2, 3, 4, 5};
        long expected = TarUtils.computeCheckSum(buffer);
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(expected, result);
    }

    @Test
    public void testComputeCheckSumAllNUL() {
        byte[] buffer = new byte[]{0};
        long expected = 0L;
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(expected, result);
    }
}