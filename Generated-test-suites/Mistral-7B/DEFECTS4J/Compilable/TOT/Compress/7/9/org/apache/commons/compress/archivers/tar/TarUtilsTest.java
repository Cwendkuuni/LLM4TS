package org.apache.commons.compress.archivers.tar.test;

import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * TarUtilsTest class.
 */
public class TarUtilsTest {

    /**
     * Test parseOctal method.
     */
    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '7', '7', '7', '0', ' '};
        long expected = 01755770L;
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    /**
     * Test parseOctal with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidInput() {
        byte[] buffer = new byte[]{'1', '2', '3'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    /**
     * Test parseName method.
     */
    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'a', 'b', 'c', 'd', 0};
        String expected = "abcd";
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        assertEquals(expected, result);
    }

    /**
     * Test parseName with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseNameInvalidInput() {
        byte[] buffer = new byte[]{'a', 'b', 'c'};
        TarUtils.parseName(buffer, 0, buffer.length);
    }

    /**
     * Test formatNameBytes method.
     */
    @Test
    public void testFormatNameBytes() {
        String name = "testName";
        byte[] buffer = new byte[16];
        int expected = 16;
        int result = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        assertEquals(expected, result);
        byte[] resultBuffer = new byte[expected];
        System.arraycopy(buffer, 0, resultBuffer, 0, expected);
        String formattedName = new String(resultBuffer);
        assertEquals(name, formattedName);
    }

    /**
     * Test formatNameBytes with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatNameBytesInvalidInput() {
        String name = "testName";
        byte[] buffer = new byte[8];
        TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
    }

    /**
     * Test formatUnsignedOctalString method.
     */
    @Test
    public void testFormatUnsignedOctalString() {
        long value = 01234567L;
        byte[] buffer = new byte[11];
        int expected = 11;
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        String result = new String(buffer, 0, expected);
        assertEquals("01234567", result);
    }

    /**
     * Test formatUnsignedOctalString with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringInvalidInput() {
        long value = -1L;
        byte[] buffer = new byte[6];
        TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
    }

    /**
     * Test formatOctalBytes method.
     */
    @Test
    public void testFormatOctalBytes() {
        long value = 123456789L;
        byte[] buffer = new byte[10];
        int expected = 10;
        int result = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        assertEquals(expected, result);
        String resultString = new String(buffer, 0, expected);
        assertEquals("01234567\u0020", resultString);
    }

    /**
     * Test formatOctalBytes with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatOctalBytesInvalidInput() {
        long value = 1234567890L;
        byte[] buffer = new byte[6];
        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
    }

    /**
     * Test computeCheckSum method.
     */
    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{1, 2, 3, 4, 5};
        long expected = TarUtils.computeCheckSum(buffer);
        long result = TarUtils.computeCheckSum(buffer);
        assertEquals(expected, result);
    }
}