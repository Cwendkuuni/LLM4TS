package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.compress.archivers.tar.TarUtils;
import org.apache.commons.compress.archivers.tar.TarUtils.IllegalArgumentException;

import org.junit.Test;

/**
 * JUnit test cases for TarUtils class.
 */
public class TarUtilsTest {

    /**
     * Test parseOctal method with valid input.
     */
    @Test
    public void testParseOctal_ValidInput() {
        byte[] buffer = new byte[] { (byte) '0', (byte) '7', (byte) '7', (byte) '7', (byte) '7' };
        int offset = 0;
        int length = 5;
        long expected = 017777L;
        long result = TarUtils.parseOctal(buffer, offset, length);
        assertEquals(expected, result);
    }

    /**
     * Test parseOctal method with invalid input - missing trailing space/NUL.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_MissingTrailingSpaceOrNul() {
        byte[] buffer = new byte[] { (byte) '0', (byte) '7', (byte) '7', (byte) '7' };
        int offset = 0;
        int length = 4;
        TarUtils.parseOctal(buffer, offset, length);
    }

    /**
     * Test parseOctal method with invalid input - invalid byte.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctal_InvalidByte() {
        byte[] buffer = new byte[] { (byte) '0', (byte) '7', (byte) '8', (byte) '7', (byte) '7' };
        int offset = 0;
        int length = 5;
        TarUtils.parseOctal(buffer, offset, length);
    }

    /**
     * Test parseName method with valid input.
     */
    @Test
    public void testParseName_ValidInput() {
        byte[] buffer = new byte[] { (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '.', (byte) 'p', (byte) 'l', (byte) 'a', (byte) 't', (byte) 'f', (byte) 'o', (byte) 'r', (byte) 'm', (byte) 'a', (byte) 't', (byte) 'e', (byte) '.', (byte) 't', (byte) 'x', (byte) 't', (byte) '.', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'c', (byte) 'h', (byte) 'a', (byte) 'r', (byte) '.', (byte) 's', (byte) 'o', (byte) 'u', (byte) 'r', (byte) 'c', (byte) 'e', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '\u0000' };
        int offset = 0;
        int length = 47;
        String expected = "tar.gz.platform.tar.char.soource.tar.gz";
        String result = TarUtils.parseName(buffer, offset, length);
        assertEquals(expected, result);
    }

    /**
     * Test parseName method with invalid input - missing trailing NUL.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseName_MissingTrailingNul() {
        byte[] buffer = new byte[] { (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'g', (byte) 'z', (byte) 'i', (byte) 'p', (byte) '.', (byte) 'p', (byte) 'l', (byte) 'a', (byte) 't', (byte) 'f', (byte) 'o', (byte) 'r', (byte) 'm', (byte) 'a', (byte) 't', (byte) 'e', (byte) '.', (byte) 't', (byte) 'x', (byte) 't', (byte) '.', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'c', (byte) 'h', (byte) 'a', (byte) 'r', (byte) '.', (byte) 's', (byte) 'o', (byte) 'u', (byte) 'r', (byte) 'c', (byte) 'e' };
        int offset = 0;
        int length = 43;
        TarUtils.parseName(buffer, offset, length);
    }

    /**
     * Test formatNameBytes method with valid input.
     */
    @Test
    public void testFormatNameBytes_ValidInput() {
        String name = "tar.gz.platform.tar.char.soource.tar.gz";
        byte[] buffer = new byte[47];
        int offset = 0;
        int length = 47;
        int result = TarUtils.formatNameBytes(name, buffer, offset, length);
        assertEquals(47, result);
    }

    /**
     * Test formatNameBytes method with invalid input - name longer than buffer.
     */
    @Test
    public void testFormatNameBytes_NameLongerThanBuffer() {
        String name = "tar.gz.platform.tar.char.soource.tar.gz.longer.than.buffer";
        byte[] buffer = new byte[47];
        int offset = 0;
        int length = 47;
        TarUtils.formatNameBytes(name, buffer, offset, length);
    }

    /**
     * Test formatUnsignedOctalString method with valid input.
     */
    @Test
    public void testFormatUnsignedOctalString_ValidInput() {
        long value = 1234567L;
        byte[] buffer = new byte[11];
        int offset = 0;
        int length = 11;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        String expected = "01234567";
        String result = new String(buffer, 0, length);
        assertEquals(expected, result);
    }

    /**
     * Test formatUnsignedOctalString method with invalid input - value larger than buffer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalString_ValueLargerThanBuffer() {
        long value = 10000000000L;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
    }

    /**
     * Test formatOctalBytes method with valid input.
     */
    @Test
    public void testFormatOctalBytes_ValidInput() {
        long value = 1234567L;
        byte[] buffer = new byte[13];
        int offset = 0;
        int length = 13;
        int result = TarUtils.formatOctalBytes(value, buffer, offset, length);
        assertEquals(13, result);
    }

    /**
     * Test formatOctalBytes method with invalid input - value larger than buffer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatOctalBytes_ValueLargerThanBuffer() {
        long value = 10000000000L;
        byte[] buffer = new byte[13];
        int offset = 0;
        int length = 13;
        TarUtils.formatOctalBytes(value, buffer, offset, length);
    }

    /**
     * Test formatCheckSumOctalBytes method with valid input.
     */
    @Test
    public void testFormatCheckSumOctalBytes_ValidInput() {
        long value = 1234567890L;
        byte[] buffer = new byte[13];
        int offset = 0;
        int length = 13;
        int result = TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);
        assertEquals(13, result);
    }

    /**
     * Test formatCheckSumOctalBytes method with invalid input - value larger than buffer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatCheckSumOctalBytes_ValueLargerThanBuffer() {
        long value = 1000000000000000L;
        byte[] buffer = new byte[13];
        int offset = 0;
        int length = 13;
        TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);
    }

    /**
     * Test computeCheckSum method with valid input.
     */
    @Test
    public void testComputeCheckSum_ValidInput() {
        byte[] buffer = new byte[] { (byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'h', (byte) 'e', (byte) 'a', (byte) 'd', (byte) 'e', (byte) 'r', (byte) '.', (byte) 't', (byte) 'a', (byte) 'r' };
        long expected = 3433921739L;
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(expected, result);
    }
}