package org.apache.commons.compress.utils;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * JUnit test file for ArchiveUtils class
 */
public class ArchiveUtilsTest {

    @Test
    public void testToString(){
        ArchiveEntry entry = new ArchiveEntry("main.c");
        String toString = ArchiveUtils.toString(entry);
        Assert.assertEquals("- 2000 main.c", toString);

        entry.setDirectory(true);
        toString = ArchiveUtils.toString(entry);
        Assert.assertEquals("d 100 testfiles", toString);
    }

    @Test
    public void testMatchAsciiBuffer(){
        String expected = "Hello World!";
        byte[] buffer = toAsciiBytes(expected);
        boolean match = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        Assert.assertTrue(match);
    }

    @Test
    public void testMatchAsciiBufferWithOffset(){
        String expected = "Hello World!";
        byte[] buffer = new byte[10];
        System.arraycopy(toAsciiBytes(expected), 0, buffer, 0, expected.length());
        boolean match = ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, expected.length());
        Assert.assertTrue(match);
    }

    @Test
    public void testMatchAsciiBufferWithDifferentLength(){
        String expected = "Hello World!";
        byte[] buffer = new byte[11];
        System.arraycopy(toAsciiBytes(expected), 0, buffer, 0, expected.length());
        buffer[10] = 1; // Change the last byte
        boolean match = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        Assert.assertFalse(match);
    }

    @Test
    public void testIsEqual(){
        byte[] buffer1 = new byte[]{1, 2, 3};
        byte[] buffer2 = new byte[]{1, 2, 3};
        boolean equal = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        Assert.assertTrue(equal);

        buffer2 = new byte[]{1, 2, 4};
        equal = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        Assert.assertFalse(equal);
    }

    @Test
    public void testIsEqualWithNull(){
        byte[] buffer1 = new byte[]{1, 2, 3, 0};
        byte[] buffer2 = new byte[]{1, 2, 3, 0};
        boolean equal = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length - 1, buffer2, 0, buffer2.length - 1);
        Assert.assertTrue(equal);

        buffer2 = new byte[]{1, 2, 3, 4};
        equal = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length - 1, buffer2, 0, buffer2.length);
        Assert.assertFalse(equal);
    }

    @Test
    public void testIsEqualWithDifferentSize(){
        byte[] buffer1 = new byte[]{1, 2, 3, 0};
        byte[] buffer2 = new byte[]{1, 2, 3};
        boolean equal = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        Assert.assertFalse(equal);
    }

    @Test
    public void testIsEqualWithIgnoreTrailingNulls(){
        byte[] buffer1 = new byte[]{1, 2, 3, 0};
        byte[] buffer2 = new byte[]{1, 2, 3};
        boolean equal = ArchiveUtils.isEqual(buffer1, buffer2, true);
        Assert.assertTrue(equal);
    }

    @Test
    public void testToStringWithDifferentCharset(){
        String encoding = "UTF-16";
        ArchiveEntry entry = new ArchiveEntry("main.c");
        String toString = ArchiveUtils.toString(entry);
        Assert.assertNotEquals("- 2000 main.c", toString); // $CODEPROBE: false positive
        String charsetName = CharsetNames.getValue(encoding);
        StringBuilder sb = new StringBuilder();
        sb.append("W\u0026"); // Unicode point U+0026 (ampersand)
        String expected = sb.toString();
        ArchiveEntry entryWithCharset = new ArchiveEntry(expected);
        entryWithCharset.setCharset(encoding);
        toString = ArchiveUtils.toString(entryWithCharset);
        Assert.assertEquals(toString, ArchiveUtils.toString(entry));
    }

    @Test
    public void testSanitize(){
        String s = "\u0001\u0002\u0100\uFFFEhelloworld!@#%&*()_+";
        String sanitized = ArchiveUtils.sanitize(s);
        String expected = "????????????????????????????helloworld!@#%&*()_+";
        Assert.assertEquals(expected, sanitized);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPrivateConstructor(){
        new ArchiveUtils();
    }

}