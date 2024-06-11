package org.apache.commons.compress.archivers.tar.test;

import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.compress.archivers.tar.TarUtils.BYTE_MASK;
import static org.apache.commons.compress.archivers.tar.TarUtils.parseOctal;
import static org.apache.commons.compress.archivers.tar.TarUtils.parseName;

/**
 * Test class for TarUtils
 */
public class TarUtilsTest {

    /**
     * Test parseOctal method with valid input
     */
    @Test
    public void testParseOctalValidInput() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '0', '1'};
        long expected = 0175501;
        long result = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test parseOctal method with invalid input - missing trailing space/NUL
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidInput1() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '0', '1'};
        TarUtils.parseOctal(buffer, 0, buffer.length - 1);
    }

    /**
     * Test parseOctal method with invalid input - invalid byte
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidInput2() {
        byte[] buffer = new byte[]{'0', '7', '5', '5', '.', '1'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    /**
     * Test parseName method with valid input
     */
    @Test
    public void testParseNameValidInput() {
        byte[] buffer = new byte[]{(byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'i', (byte) 'n',
            (byte) '.', (byte) 't', (byte) 'e', (byte) 's', (byte) 't', (byte) '.', (byte) 'c', (byte) 'h',
            (byte) 'e', (byte) 'c', (byte) 'k', (byte) 's', (byte) 'u', (byte) 'm', (byte) '.', (byte) 'o',
            (byte) 'f', (byte) 'f', (byte) '.', (byte) 't', (byte) 'e', (byte) 's', (byte) 't'};
        String expected = "tar.in.test.checksums.off";
        String result = TarUtils.parseName(buffer, 0, buffer.length);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test parseName method with invalid input - missing trailing NUL
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseNameInvalidInput1() {
        byte[] buffer = new byte[]{(byte) 't', (byte) 'a', (byte) 'r', (byte) '.', (byte) 'i', (byte) 'n',
            (byte) '.', (byte) 't', (byte) 'e', (byte) 's', (byte) 't', (byte) '.'};
        TarUtils.parseName(buffer, 0, buffer.length);
    }

    /**
     * Test formatNameBytes method with valid input
     */
    @Test
    public void testFormatNameBytesValidInput() {
        String name = "tar.in.test.checksums.off";
        byte[] buffer = new byte[name.length() + 1];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        String result = new String(buffer, 0, offset);
        Assert.assertEquals(name, result);
    }

    /**
     * Test formatNameBytes method with invalid input - name is too long
     */
    @Test
    public void testFormatNameBytesInvalidInput1() {
        String name = "a" + new String(new char[1024 * 1024]).substring(0, 1024 * 1024);
        byte[] buffer = new byte[name.length() + 1];
        int offset = TarUtils.formatNameBytes(name, buffer, 0, buffer.length);
        Assert.fail("Expected IllegalArgumentException but got: " + new String(buffer, 0, offset));
    }

    /**
     * Test formatUnsignedOctalString method with valid input
     */
    @Test
    public void testFormatUnsignedOctalStringValidInput() {
        long value = 12345678;
        byte[] buffer = new byte[9];
        int offset = TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        long expectedValue = 01234567;
        Assert.assertEquals(expectedValue, TarUtils.parseOctal(buffer, 0, offset));
    }

    /**
     * Test formatUnsignedOctalString method with invalid input - value will not fit in the buffer
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatUnsignedOctalStringInvalidInput() {
        long value = Long.MAX_VALUE + 1;
        byte[] buffer = new byte[8];
        int offset = TarUtils.formatUnsignedOctalString(value, buffer, 0, buffer.length);
        Assert.fail("Expected IllegalArgumentException but got: " + new String(buffer, 0, offset));
    }

    /**
     * Test formatOctalBytes method with valid input
     */
    @Test
    public void testFormatOctalBytesValidInput() {
        long value = 12345678;
        byte[] buffer = new byte[11];
        int offset = TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        Assert.assertEquals(offset, 11);
    }

    /**
     * Test formatOctalBytes method with invalid input - value (and trailer) will not fit in the buffer
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatOctalBytesInvalidInput() {
        long value = Long.MAX_VALUE + 1;
        byte[] buffer = new byte[10];
        TarUtils.formatOctalBytes(value, buffer, 0, buffer.length);
        Assert.fail("Expected IllegalArgumentException but got: " + new String(buffer, 0, buffer.length));
    }

    /**
     * Test computeCheckSum method with valid input
     */
    @Test
    public void testComputeCheckSumValidInput() {
        byte[] buffer = new byte[]{(byte) 0x75, (byte) 0x20, (byte) 0x2b, (byte) 0x62,
            (byte) 0x79, (byte) 0x20, (byte) 0x14, (byte) 0x31, (byte) 0x37, (byte) 0x35,
            (byte) 0x2e, (byte) 0x31, (byte) 0x2d, (byte) 0x32, (byte) 0x00};
        long expected = 0x143137522B792014;
        long result = TarUtils.computeCheckSum(buffer);
        Assert.assertEquals(expected, result);
    }
}