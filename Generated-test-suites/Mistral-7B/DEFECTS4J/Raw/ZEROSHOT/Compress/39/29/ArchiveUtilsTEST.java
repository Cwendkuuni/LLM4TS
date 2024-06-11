package org.apache.commons.compress.utils;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * JUnit test file for ArchiveUtils class
 */
public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry("main.c");
        entry.setSize(2000);
        entry.setDirectory(false);

        String result = ArchiveUtils.toString(entry);
        String expected = "- 2000 main.c";
        Assert.assertEquals(expected, result);

        entry.setDirectory(true);
        result = ArchiveUtils.toString(entry);
        expected = "d 100 testfiles";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "magic";
        byte[] buffer = toAsciiBytes(expected);

        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        Assert.assertTrue(result);

        result = ArchiveUtils.matchAsciiBuffer(expected, buffer, 1, buffer.length - 1);
        Assert.assertTrue(result);

        byte[] buffer2 = Arrays.copyOfRange(buffer, 1, buffer.length);
        result = ArchiveUtils.matchAsciiBuffer(expected, buffer2);
        Assert.assertFalse(result);
    }

    @Test(expected = RuntimeException.class)
    public void testMatchAsciiBufferWithInvalidEncoding() {
        String expected = "magic";
        ArchiveUtils.matchAsciiBuffer(expected, new byte[1]);
    }

    @Test
    public void testToAsciiString() {
        byte[] buffer = toAsciiBytes("magic");
        String result = ArchiveUtils.toAsciiString(buffer);
        String expected = "magic";
        Assert.assertEquals(expected, result);

        result = ArchiveUtils.toAsciiString(buffer, 1, buffer.length - 1);
        expected = "agic";
        Assert.assertEquals(expected, result);
    }

    @Test(expected = RuntimeException.class)
    public void testToAsciiStringWithInvalidEncoding() {
        byte[] buffer = new byte[1];
        ArchiveUtils.toAsciiString(buffer);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[]{'m', 'a', 'g', 'i', 'c'};
        byte[] buffer2 = new byte[]{'m', 'a', 'g', 'i', 'c'};
        boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false);
        Assert.assertTrue(result);

        result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 1, buffer2.length - 1, false);
        Assert.assertFalse(result);

        result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 1, buffer2.length - 1, true);
        Assert.assertTrue(result);

        byte[] buffer3 = new byte[]{'m', 'a', 'g', 'i', 'c', 'd'};
        result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length, false);
        Assert.assertFalse(result);

        result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length, true);
        Assert.assertFalse(result);

        result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer3, 1, buffer3.length - 1, false);
        Assert.assertFalse(result);

        result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer3, 1, buffer3.length - 1, true);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = new byte[]{'m', 'a', 'g', 'i', 'c', 'd'};
        byte[] buffer2 = new byte[]{'m', 'a', 'g', 'i', 'c'};
        boolean result = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        Assert.assertFalse(result);

        result = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length - 1, buffer2, 0, buffer2.length);
        Assert.assertTrue(result);

        result = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 1, buffer2.length - 1);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsEqualWithNull2() {
        byte[] buffer1 = new byte[]{'m', 'a', 'g', 'i', 'c', 'd'};
        byte[] buffer2 = new byte[6];
        Arrays.fill(buffer2, (byte) 0);
        boolean result = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        Assert.assertFalse(result);

        result = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length - 1, buffer2, 0, buffer2.length);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsArrayZero() {
        byte[] buffer = new byte[10];
        Arrays.fill(buffer, (byte) 0);
        boolean result = ArchiveUtils.isArrayZero(buffer, 10);
        Assert.assertTrue(result);

        result = ArchiveUtils.isArrayZero(buffer, 5);
        Assert.assertTrue(result);

        result = ArchiveUtils.isArrayZero(buffer, 15);
        Assert.assertFalse(result);
    }

    @Test
    public void testSanitize() {
        String s = "abçdéfghıijklmñöőüű";
        String expected = "ab?c?d?e?f?g?h?i?j?k?l?m?n?o?o?u?u";
        String result = ArchiveUtils.sanitize(s);
        Assert.assertEquals(expected, result);

        s = "abc";
        result = ArchiveUtils.sanitize(s);
        expected = "abc";
        Assert.assertEquals(expected, result);

        s = "";
        result = ArchiveUtils.sanitize(s);
        expected = "";
        Assert.assertEquals(expected, result);
    }
}