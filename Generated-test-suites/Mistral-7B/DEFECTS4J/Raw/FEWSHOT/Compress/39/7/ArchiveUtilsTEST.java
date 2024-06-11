package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.test.TestFile;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new TestFile("main.c").getEntry();
        assertEquals("- 2000 main.c", ArchiveUtils.toString(entry));
        entry.setDirectory(true);
        assertEquals("d 100 testfiles", ArchiveUtils.toString(entry));
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "Magic";
        byte[] buffer = toAsciiBytes(expected);
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
        assertFalse(ArchiveUtils.matchAsciiBuffer("Magic2", buffer));
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = Arrays.copyOfRange(toAsciiBytes("Magic1".getBytes()), 0, 5);
        byte[] buffer2 = Arrays.copyOfRange(toAsciiBytes("Magic2".getBytes()), 0, 5);
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer2, 0, buffer2.length, 0, buffer1.length, false));
        byte[] buffer3 = new byte[5];
        System.arraycopy(buffer1, 0, buffer3, 0, 5);
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer3, 0, buffer3.length, 0, buffer1.length, false));

        byte[] buffer4 = Arrays.copyOfRange(toAsciiBytes("Magic1".getBytes()), 0, 5);
        byte[] buffer5 = Arrays.copyOfRange(toAsciiBytes("Magic1".getBytes()), 0, 5);
        assertTrue(ArchiveUtils.isEqual(buffer4, buffer5, 0, buffer5.length, 0, buffer4.length, false));

        byte[] buffer6 = Arrays.copyOfRange(toAsciiBytes("Magic1".getBytes()), 0, 6);
        byte[] buffer7 = Arrays.copyOfRange(toAsciiBytes("Magic2".getBytes()), 0, 6);
        assertFalse(ArchiveUtils.isEqual(buffer6, buffer7, 0, buffer7.length, 0, buffer6.length, false));

        byte[] buffer8 = Arrays.copyOfRange(toAsciiBytes("Magic1".getBytes()), 0, 5);
        byte[] buffer9 = new byte[6];
        Arrays.fill(buffer9, (byte) 0);
        assertTrue(ArchiveUtils.isEqual(buffer8, buffer9, 0, buffer9.length, 0, buffer8.length, true));
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = Arrays.copyOfRange(toAsciiBytes("Magic1".getBytes()), 0, 5);
        byte[] buffer2 = Arrays.copyOfRange(toAsciiBytes("Magic2".getBytes()), 0, 5);
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, buffer2, 0, buffer2.length, 0, buffer1.length));

        byte[] buffer3 = new byte[5];
        System.arraycopy(buffer1, 0, buffer3, 0, 5);
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, buffer3, 0, buffer3.length, 0, buffer1.length));

        byte[] buffer4 = Arrays.copyOfRange(toAsciiBytes("Magic1".getBytes()), 0, 5);
        byte[] buffer5 = Arrays.copyOfRange(toAsciiBytes("Magic1".getBytes()), 0, 5);
        assertTrue(ArchiveUtils.isEqualWithNull(buffer4, buffer5, 0, buffer5.length, 0, buffer4.length));

        byte[] buffer6 = Arrays.copyOfRange(toAsciiBytes("Magic1".getBytes()), 0, 6);
        byte[] buffer7 = new byte[6];
        Arrays.fill(buffer7, (byte) 0);
        assertTrue(ArchiveUtils.isEqualWithNull(buffer6, buffer7, 0, buffer7.length, 0, buffer6.length));
    }

    @Test
    public void testisArrayZero() {
        byte[] a = new byte[10];
        assertFalse(ArchiveUtils.isArrayZero(a, 11));
        assertTrue(ArchiveUtils.isArrayZero(a, 10));
    }

    @Test
    public void testSanitize() {
        String input = "test\u0001string\u007F";
        String expected = "test?string?";
        assertEquals(expected, ArchiveUtils.sanitize(input));
    }

    @Test(expected = RuntimeException.class)
    public void testToAsciiBytesWithUnsupportedEncodingException() {
        String input = "test\u0001string\u007F";
        ArchiveUtils.toAsciiBytes(input);
    }

    @Test(expected = RuntimeException.class)
    public void testToAsciiStringWithUnsupportedEncodingException() {
        byte[] inputBytes = {1, 2, 3};
        ArchiveUtils.toAsciiString(inputBytes);
    }
}