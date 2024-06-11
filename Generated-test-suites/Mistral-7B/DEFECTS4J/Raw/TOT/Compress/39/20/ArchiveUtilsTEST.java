package org.apache.commons.compress.utils;

import org.apache.commons.compress.archivers.test.util.TestArchiveEntry;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new TestArchiveEntry("test.txt", "test content".getBytes(StandardCharsets.UTF_8), true);
        String expected = "-       13 test.txt\n";
        String result = ArchiveUtils.toString(entry);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "test string";
        byte[] buffer = expected.getBytes(StandardCharsets.UTF_8);
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        Assert.assertTrue(result);
    }

    @Test
    public void testMatchAsciiBuffer_Mismatch() {
        String expected = "test string";
        byte[] buffer = new byte[10]; // different length
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        Assert.assertFalse(result);
    }

    @Test
    public void testMatchAsciiBuffer_DiffChar() {
        String expected = "test string";
        byte[] buffer = expected.getBytes(StandardCharsets.UTF_8);
        buffer[0] = (byte) (expected.charAt(0) + 1); // different char
        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        Assert.assertFalse(result);
    }

    @Test
    public void testToStringArray() {
        ArchiveEntry[] entries = new ArchiveEntry[2];
        entries[0] = new TestArchiveEntry("test1.txt", "test content1".getBytes(StandardCharsets.UTF_8), true);
        entries[1] = new TestArchiveEntry("test2.txt", "test content2".getBytes(StandardCharsets.UTF_8), false);
        byte[] buffer = new byte[2048]; // enough for the largest string in toString
        ByteArrayOutputStream out = new ByteArrayOutputStream(buffer.length);

        for (ArchiveEntry entry : entries) {
            out.write(ArchiveUtils.toString(entry).getBytes(StandardCharsets.UTF_8));
            out.write('\n');
        }

        String expected = Arrays.toString(entries);
        String result = new String(buffer, StandardCharsets.UTF_8);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[]{'T', 'e', 's', 't', ' ', 'S', 't', 'r', 'i', 'n', 'g'};
        byte[] buffer2 = new byte[]{'T', 'e', 's', 't', ' ', 'S', 't', 'r', 'i', 'n', 'g'};
        boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsEqual_DiffLength() {
        byte[] buffer1 = new byte[]{'T', 'e', 's', 't'};
        byte[] buffer2 = new byte[]{'T', 'e', 's', 't', ' ', 'S'};
        boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsEqual_DiffChar() {
        byte[] buffer1 = new byte[]{'T', 'e', 's', 't'};
        byte[] buffer2 = new byte[]{'T', 'e', 's', 'd'};
        boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsEqual_IgnoreTrailingNulls() {
        byte[] buffer1 = new byte[]{'T', 'e', 's', 't', '\u0000'};
        byte[] buffer2 = new byte[]{'T', 'e', 's', 't'};
        boolean result = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        Assert.assertTrue(result);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testToAsciiBytes_UnsupportedEncodingException() {
        String inputString = new String(new byte[0]); // empty string
        ArchiveUtils.toAsciiBytes(inputString);
    }

    @Test
    public void testToAsciiString() {
        byte[] buffer = new byte[]{'T', 'e', 's', 't'};
        String result = ArchiveUtils.toAsciiString(buffer);
        Assert.assertEquals(new String(buffer), result);
    }

    @Test
    public void testToAsciiString_Offset() {
        byte[] buffer = new byte[]{'T', 'e', 's', 't'};
        int offset = 1;
        int length = 3;
        String result = ArchiveUtils.toAsciiString(buffer, offset, length);
        Assert.assertEquals(new String(Arrays.copyOfRange(buffer, offset, offset + length)), result);
    }

    @Test
    public void testIsArrayZero() {
        byte[] a = new byte[10];
        Arrays.fill(a, (byte) 0);
        boolean result = ArchiveUtils.isArrayZero(a, 5);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsArrayZero_NotZero() {
        byte[] a = new byte[10];
        a[5] = (byte) 1;
        boolean result = ArchiveUtils.isArrayZero(a, 5);
        Assert.assertFalse(result);
    }

    @Test
    public void testSanitize() {
        String input = "test/string\\with/special/characters/and/length>255";
        String expected = "test/??????????????????????????????????????????????????\nwith/??????????????????????????????????????????????????\nand/length>255";
        String result = ArchiveUtils.sanitize(input);
        Assert.assertEquals(expected, result);
    }
}