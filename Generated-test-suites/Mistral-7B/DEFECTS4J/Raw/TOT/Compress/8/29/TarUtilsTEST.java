package org.apache.commons.compress.archivers.tar;

import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.compress.archivers.tar.TarUtils.BYTE_MASK;
import static org.apache.commons.compress.archivers.tar.TarUtils.formatCheckSumOctalBytes;
import static org.apache.commons.compress.archivers.tar.TarUtils.formatLongOctalBytes;
import static org.apache.commons.compress.archivers.tar.TarUtils.formatNameBytes;
import static org.apache.commons.compress.archivers.tar.TarUtils.formatUnsignedOctalString;
import static org.apache.commons.compress.archivers.tar.TarUtils.parseOctal;
import static org.apache.commons.compress.archivers.tar.TarUtils.parseName;

/**
 * Test class for TarUtils.
 */
public class TarUtilsTest {

    /**
     * Test parseOctal method.
     */
    @Test
    public void testParseOctal() {
        byte[] buffer = new byte[]{'0', '7', '5', '5'};
        long expected = 01755L;
        long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test parseOctal with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalidInput() {
        byte[] buffer = new byte[]{'0', '7', 'a'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    /**
     * Test parseName method.
     */
    @Test
    public void testParseName() {
        byte[] buffer = new byte[]{'a', 'b', 'c', '.'};
        String expected = "abc.";
        String actual = TarUtils.parseName(buffer, 0, buffer.length);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test parseName with null terminated name.
     */
    @Test
    public void testParseNameWithNullTerminatedName() {
        byte[] buffer = new byte[]{'a', 'b', 'c', ' ', 0};
        String expected = "abc";
        String actual = TarUtils.parseName(buffer, 0, buffer.length);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test formatNameBytes method.
     */
    @Test
    public void testFormatNameBytes() {
        String name = "MyFile.txt";
        byte[] buffer = new byte[32];
        int offset = 0;
        int length = name.length();
        TarUtils.formatNameBytes(name, buffer, offset, length);
        for (int i = 0; i < length; ++i) {
            Assert.assertEquals((byte) name.charAt(i), buffer[i + offset]);
        }
    }

    /**
     * Test formatUnsignedOctalString method.
     */
    @Test
    public void testFormatUnsignedOctalString() {
        long value = 12345L;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 8;
        TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
        String expected = "01235123";
        StringBuilder actual = new StringBuilder();
        for (int i = 0; i < length; ++i) {
            actual.append((char) (buffer[i] & 0xFF));
        }
        Assert.assertEquals(expected, actual.toString());
    }

    /**
     * Test formatOctalBytes method.
     */
    @Test
    public void testFormatOctalBytes() {
        long value = 12345L;
        byte[] buffer = new byte[12];
        int offset = 0;
        int length = 10;
        TarUtils.formatOctalBytes(value, buffer, offset, length);
        String expected = "01235  ";
        StringBuilder actual = new StringBuilder();
        for (int i = 0; i < length; ++i) {
            actual.append((char) (buffer[i] & 0xFF));
        }
        Assert.assertEquals(expected, actual.toString());
    }

    /**
     * Test formatLongOctalBytes method.
     */
    @Test
    public void testFormatLongOctalBytes() {
        long value = 123456789012345L;
        byte[] buffer = new byte[12];
        int offset = 0;
        int length = 10;
        TarUtils.formatLongOctalBytes(value, buffer, offset, length);
        String expected = "0123456754321012";
        StringBuilder actual = new StringBuilder();
        for (int i = 0; i < length; ++i) {
            actual.append((char) (buffer[i] & 0xFF));
        }
        Assert.assertEquals(expected, actual.toString());
    }

    /**
     * Test formatCheckSumOctalBytes method.
     */
    @Test
    public void testFormatCheckSumOctalBytes() {
        long value = 123456789012345L;
        byte[] buffer = new byte[12];
        int offset = 0;
        int length = 10;
        TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);
        String expected = "0123456754321012";
        StringBuilder actual = new StringBuilder();
        for (int i = 0; i < length; ++i) {
            actual.append((char) (buffer[i] & 0xFF));
        }
        Assert.assertEquals(expected, actual.toString());
    }

    /**
     * Test computeCheckSum method.
     */
    @Test
    public void testComputeCheckSum() {
        byte[] buffer = new byte[]{'a', 'b', 'c', 'd', 'e'};
        long expected = (BYTE_MASK & 'a') + (BYTE_MASK & 'b') + (BYTE_MASK & 'c') + (BYTE_MASK & 'd') + (BYTE_MASK & 'e');
        long actual = TarUtils.computeCheckSum(buffer);
        Assert.assertEquals(expected, actual);
    }
}