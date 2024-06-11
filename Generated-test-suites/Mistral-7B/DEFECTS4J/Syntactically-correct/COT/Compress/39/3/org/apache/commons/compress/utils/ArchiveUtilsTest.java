package org.apache.commons.compress.utils;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry("main.c");
        Assert.assertEquals("- 2000 main.c\n", ArchiveUtils.toString(entry));
        entry.setSize(100);
        Assert.assertEquals("d 100 testfiles\n", ArchiveUtils.toString(entry));
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "hello";
        byte[] buffer = toAsciiBytes(expected);
        Assert.assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
        Assert.assertFalse(ArchiveUtils.matchAsciiBuffer(expected, new byte[buffer.length]));
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testToAsciiBytesWithInvalidEncoding() {
        String inputString = "test";
        ArchiveUtils.toAsciiBytes(inputString);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[]{'h', 'e', 'l', 'l', 'o'};
        byte[] buffer2 = new byte[]{'h', 'e', 'l', 'l', 'o'};
        Assert.assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));
        byte[] buffer3 = new byte[]{'h', 'e', 'l', 'l', 'o'};
        Assert.assertTrue(ArchiveUtils.isEqual(buffer1, buffer3));

        byte[] buffer4 = new byte[]{'h', 'e', 'l', 'l', 'o'};
        byte[] buffer5 = new byte[]{'h', 'e', 'l', 'l', 'o'};
        Assert.assertTrue(ArchiveUtils.isEqual(buffer4, buffer5, false));

        byte[] buffer6 = new byte[]{'h', 'e', 'l', 'l', 'o'};
        byte[] buffer7 = new byte[]{'h', 'e', 'l', 'l', 'o'};
        Assert.assertTrue(ArchiveUtils.isEqual(buffer6, buffer7, true));

        byte[] buffer8 = new byte[]{'h', 'e', 'l', 'l', 'o'};
        byte[] buffer9 = new byte[]{'h', 'e', 'l', 'l', 'o'};
        Assert.assertFalse(ArchiveUtils.isEqual(buffer8, buffer9, false));

        byte[] buffer10 = new byte[]{'h', 'e', 'l', 'l', 'o'};
        byte[] buffer11 = new byte[]{'h', 'e', 'l', 'l', 'o', (byte) 0};
        Assert.assertTrue(ArchiveUtils.isEqual(buffer10, buffer11, true));

        byte[] buffer12 = new byte[]{'h', 'e', 'l', 'l', (byte) 0, 'o'};
        Assert.assertFalse(ArchiveUtils.isEqual(buffer10, buffer12, true));

        byte[] buffer13 = new byte[]{'h', 'e', 'l', 'l', 'o'};
        byte[] buffer14 = new byte[]{'h', 'e', 'l', 'l', 'o', (byte) 0};
        Assert.assertTrue(ArchiveUtils.isEqual(buffer13, buffer14, true));

        byte[] buffer15 = new byte[]{'h', 'e', 'l', 'l', (byte) 0, 'o'};
        Assert.assertFalse(ArchiveUtils.isEqual(buffer13, buffer15, true));
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = new byte[]{'h', 'e', 'l', 'l', 'o'};
        byte[] buffer2 = new byte[]{'h', 'e', 'l', 'l', 'o'};
        Assert.assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));

        byte[] buffer3 = new byte[]{'h', 'e', 'l', 'l', 'o'};
        byte[] buffer4 = new byte[]{'h', 'e', 'l', 'l', 'o'};
        Assert.assertFalse(ArchiveUtils.isEqualWithNull(buffer3, 0, buffer3.length, buffer4, 0, buffer4.length));

        byte[] buffer5 = new byte[]{'h', 'e', 'l', 'l', 'o'};
        byte[] buffer6 = new byte[]{'h', 'e', 'l', 'l', 'o'};
        byte[] buffer7 = new byte[]{'h', 'e', 'l', 'l', 'o'};
        Assert.assertTrue(ArchiveUtils.isEqualWithNull(buffer5, 0, buffer5.length, buffer6, 0, buffer6.length));
        Assert.assertTrue(ArchiveUtils.isEqualWithNull(buffer5, 0, buffer5.length, buffer7, 0, buffer7.length));
        Assert.assertTrue(ArchiveUtils.isEqualWithNull(buffer6, 0, buffer6.length, buffer7, 0, buffer7.length));

        byte[] buffer8 = new byte[]{'h', 'e', 'l', 'l', 'o'};
        byte[] buffer9 = new byte[]{'h', 'e', 'l', 'l', 'o', (byte) 0};
        Assert.assertTrue(ArchiveUtils.isEqualWithNull(buffer8, 0, buffer8.length, buffer9, 0, buffer9.length));
    }

    @Test
    public void testIsArrayZero() {
        byte[] array = new byte[0];
        Assert.assertTrue(ArchiveUtils.isArrayZero(array, 0));

        byte[] array2 = new byte[5];
        Assert.assertFalse(ArchiveUtils.isArrayZero(array2, 5));

        Assert.assertTrue(ArchiveUtils.isArrayZero(array2, 0));
        Assert.assertTrue(ArchiveUtils.isArrayZero(array2, 1));
        Assert.assertTrue(ArchiveUtils.isArrayZero(array2, 2));
        Assert.assertTrue(ArchiveUtils.isArrayZero(array2, 3));
        Assert.assertTrue(ArchiveUtils.isArrayZero(array2, 4));
    }

    @Test
    public void testSanitize() {
        String test = "test\u0000\u0001 \u007F!@#$%^&*()_+{}|:<>?[]~`";
        String result = ArchiveUtils.sanitize(test);
        String expected = "test?? ????~`";
        Assert.assertEquals(expected, result);
    }

}