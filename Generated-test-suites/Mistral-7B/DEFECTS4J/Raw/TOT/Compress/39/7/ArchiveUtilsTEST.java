package org.apache.commons.compress.utils.test;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Test cases for ArchiveUtils class
 */
public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry("test.txt");
        entry.setSize(1024);

        String result = ArchiveUtils.toString(entry);
        String expected = "- 1024 test.txt";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "Hello, World!";
        byte[] buffer = toAsciiBytes(expected);

        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        Assert.assertTrue(result);

        expected += "!";
        result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        Assert.assertFalse(result);
    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes = toAsciiBytes("Hello, World!");
        String result = ArchiveUtils.toAsciiString(inputBytes);
        String expected = "Hello, World!";
        Assert.assertEquals(expected, result);

        inputBytes[0] = 1; // Change first byte to be invalid ASCII
        result = ArchiveUtils.toAsciiString(inputBytes);
        Assert.assertNull(result);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};
        byte[] buffer2 = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};
        byte[] buffer3 = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x09, 0x08};

        boolean result = ArchiveUtils.isEqual(buffer1, buffer2);
        Assert.assertTrue(result);

        result = ArchiveUtils.isEqual(buffer1, buffer3);
        Assert.assertFalse(result);

        result = ArchiveUtils.isEqual(buffer1, buffer1, 3); // Test with partial match
        Assert.assertTrue(result);
    }

    @Test
    public void testIsArrayZero() {
        byte[] array = new byte[10];
        Arrays.fill(array, (byte) 0);

        boolean result = ArchiveUtils.isArrayZero(array, 10);
        Assert.assertTrue(result);

        result = ArchiveUtils.isArrayZero(array, 11);
        Assert.assertFalse(result);
    }

    @Test
    public void testSanitize() {
        String input = "Hello, World!\u0001\u0002";
        String result = ArchiveUtils.sanitize(input);
        String expected = "Hello, World?";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testToString_DirectoryEntry() {
        ArchiveEntry entry = new ArchiveEntry("test.txt");
        entry.setSize(1024);
        entry.setName("test/");
        entry.setDirectory(true);

        String result = ArchiveUtils.toString(entry);
        String expected = "d 0 test/";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testMatchAsciiBuffer_NullInput() {
        String expected = "";
        byte[] buffer = null;

        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        Assert.assertFalse(result);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testToAsciiBytes_InvalidEncoding() {
        String input = "Hello, World!";
        ArchiveUtils.toAsciiBytes(input);
    }

    @Test
    public void testToAsciiString_NullInput() {
        byte[] inputBytes = null;

        String result = ArchiveUtils.toAsciiString(inputBytes);
        Assert.assertNull(result);
    }

    @Test
    public void testIsEqual_NullInput() {
        byte[] buffer1 = null;
        byte[] buffer2 = new byte[]{"test"};

        boolean result = ArchiveUtils.isEqual(buffer1, buffer2, false);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = new byte[]{"test1", 0};
        byte[] buffer2 = new byte[]{"test2", 0};

        boolean result = ArchiveUtils.isEqualWithNull(buffer1, 0, 7, buffer2, 0, 7);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsEqualWithNull_UnequalLength() {
        byte[] buffer1 = new byte[]{"test1", 0};
        byte[] buffer2 = new byte[]{"test2", 0, 0};

        boolean result = ArchiveUtils.isEqualWithNull(buffer1, 0, 6, buffer2, 0, 7);
        Assert.assertFalse(result);
    }
}