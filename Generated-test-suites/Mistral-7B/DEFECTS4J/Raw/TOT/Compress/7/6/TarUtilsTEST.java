package org.apache.commons.compress.archivers.tar.test;

import org.apache.commons.compress.archivers.tar.TarUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.compress.archivers.tar.TarConstants.MAGIC_NUMBER;

/**
 * Test class for TarUtils.
 */
public class TarUtilsTest {

    /**
     * Test parseOctal method with valid octal string.
     */
    @Test
    public void testParseOctalValid() {
        byte[] buffer = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7'};
        long expected = 0123456L;
        long actual = TarUtils.parseOctal(buffer, 0, buffer.length);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test parseOctal method with invalid octal string.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalInvalid() {
        byte[] buffer = new byte[]{'0', '1', '2', 'a'};
        TarUtils.parseOctal(buffer, 0, buffer.length);
    }

    /**
     * Test parseName method with valid name.
     */
    @Test
    public void testParseNameValid() {
        byte[] buffer = new byte[]{'.','.', 'a', '.', 'b', '.', 'c', '.', '.', 0};
        String expected = "a.b.c.";
        String actual = TarUtils.parseName(buffer, 0, buffer.length);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test parseName method with invalid name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseNameInvalid() {
        byte[] buffer = new byte[]{'.'};
        TarUtils.parseName(buffer, 0, buffer.length);
    }

    /**
     * Test formatNameBytes method with valid name and buffer.
     */
    @Test
    public void testFormatNameBytesValid() {
        String name = "DirectoryName";
        byte[] expected = new byte[]{'D', 'i', 'r', 'e', 'c', 't', 'o', 'r', 'y', 'N', 'a', 'm', 'e'};
        byte[] buffer = new byte[expected.length];
        int offset = 0;
        int length = expected.length;
        TarUtils.formatNameBytes(name, buffer, offset, length);
        Assert.assertArrayEquals(expected, buffer);
    }

    /**
     * Test formatNameBytes method with invalid name and buffer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatNameBytesInvalid() {
        String name = "";
        byte[] buffer = new byte[1];
        int offset = 0;
        int length = 1;
        TarUtils.formatNameBytes(name, buffer, offset, length);
    }

    /**
     * Test formatCheckSumOctalBytes method with valid checksum.
     */
    @Test
    public void testFormatCheckSumOctalBytesValid() {
        long checksum = 123456789012L;
        byte[] expected = new byte[9];
        TarUtils.formatCheckSumOctalBytes(checksum, expected, 0, expected.length);
        Assert.assertArrayEquals(new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', ' '}, expected);
    }

    /**
     * Test formatCheckSumOctalBytes method with invalid checksum.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatCheckSumOctalBytesInvalid() {
        long checksum = Long.MAX_VALUE + 1;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;
        TarUtils.formatCheckSumOctalBytes(checksum, buffer, offset, length);
    }

    /**
     * Test parseOctal method with empty buffer.
     */
    @Test
     public void testParseOctalEmptyBuffer() {
        byte[] buffer = new byte[0];
        try {
            TarUtils.parseOctal(buffer, 0, buffer.length);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test parseName method with empty buffer.
     */
    @Test
     public void testParseNameEmptyBuffer() {
        byte[] buffer = new byte[0];
        try {
            TarUtils.parseName(buffer, 0, buffer.length);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test parseOctal method with null buffer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseOctalNullBuffer() {
        byte[] buffer = null;
        try {
            TarUtils.parseOctal(buffer, 0, buffer.length);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test parseName method with null buffer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseNameNullBuffer() {
        byte[] buffer = null;
        try {
            TarUtils.parseName(buffer, 0, buffer.length);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test formatNameBytes method with null name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatNameBytesNullName() {
        String name = null;
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 10;
        try {
            TarUtils.formatNameBytes(name, buffer, offset, length);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test formatNameBytes method with name longer than buffer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatNameBytesNameLongerThanBuffer() {
        String name = "LongName";
        byte[] buffer = new byte[5];
        int offset = 0;
        int length = buffer.length;
        try {
            TarUtils.formatNameBytes(name, buffer, offset, length);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test formatNameBytes method with null buffer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatNameBytesNullBuffer() {
        String name = "Name";
        byte[] buffer = null;
        int offset = 0;
        int length = name.length();
        try {
            TarUtils.formatNameBytes(name, buffer, offset, length);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test formatNameBytes method with buffer length less than name length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatNameBytesBufferLengthLessThanNameLength() {
        String name = "Name";
        byte[] buffer = new byte[name.length()-1];
        int offset = 0;
        int length = buffer.length;
        try {
            TarUtils.formatNameBytes(name, buffer, offset, length);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test computeCheckSum method with empty buffer.
     */
    @Test
    public void testComputeCheckSumEmptyBuffer() {
        byte[] buffer = new byte[0];
        long checksum = TarUtils.computeCheckSum(buffer);
        Assert.assertEquals(0, checksum);
    }

    /**
     * Test computeCheckSum method with valid buffer.
     */
    @Test
    public void testComputeCheckSumValidBuffer() {
        byte[] buffer = new byte[]{MAGIC_NUMBER};
        long checksum = TarUtils.computeCheckSum(buffer);
        Assert.assertEquals(MAGIC_NUMBER ^ (MAGIC_NUMBER & TarUtils.BYTE_MASK), checksum);
    }
}