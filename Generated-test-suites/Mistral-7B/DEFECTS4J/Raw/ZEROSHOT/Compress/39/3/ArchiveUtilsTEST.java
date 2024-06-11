package org.apache.commons.compress.utils;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry("main.c");
        entry.setSize(2000);
        entry.setDirectory(false);

        String output = ArchiveUtils.toString(entry);
        String expected = "- 2000 main.c";
        assertEquals(expected, output);

        ArchiveEntry directoryEntry = new ArchiveEntry("testfiles");
        directoryEntry.setSize(100);
        directoryEntry.setDirectory(true);

        String directoryOutput = ArchiveUtils.toString(directoryEntry);
        String directoryExpected = "d 100 testfiles";
        assertEquals(directoryExpected, directoryOutput);
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "hello";
        byte[] buffer = "hello".getBytes();

        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        assertTrue(result);
    }

    @Test
    public void testMatchAsciiBufferWithOffset() {
        String expected = "hello";
        byte[] buffer = new byte[]{'h', 'e', 'l', 'l', 'o'};

        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, 5);
        assertTrue(result);
    }

    @Test
    public void testMatchAsciiBufferWithDifferentLength() {
        String expected = "hello";
        byte[] buffer = new byte[]{'h', 'e', 'l', 'l', 'o', 'k'};

        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, 5);
        assertFalse(result);
    }

    @Test
    public void testMatchAsciiBufferWithDifferentEncoding() {
        String expected = "hello";
        byte[] buffer = "hello world".getBytes();

        boolean result = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        assertFalse(result);
    }

    @Test
    public void testToAsciiBytes() {
        String inputString = "hello";
        byte[] expected = new byte[]{'h', 'e', 'l', 'l', 'o'};
        byte[] result = ArchiveUtils.toAsciiBytes(inputString);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes = new byte[]{'h', 'e', 'l', 'l', 'o'};
        String expected = "hello";
        String result = ArchiveUtils.toAsciiString(inputBytes);
        assertEquals(expected, result);
    }

    @Test
    public void testToAsciiStringWithOffsetAndLength() {
        byte[] inputBytes = new byte[]{'h', 'e', 'l', 'l', 'o'};
        String expected = "hello";
        String result = ArchiveUtils.toAsciiString(inputBytes, 0, 5);
        assertEquals(expected, result);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[]{'h', 'e', 'l', 'l', 'o', 'k'};
        byte[] buffer2 = new byte[]{'h', 'e', 'l', 'l', 'o'};

        boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false);
        assertFalse(result);

        buffer1 = new byte[]{'h', 'e', 'l', 'l', 'o'};
        buffer2 = new byte[]{'h', 'e', 'l', 'l', 'o'};

        result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false);
        assertTrue(result);
    }

    @Test
    public void testIsEqualWithNullTrailing() {
        byte[] buffer1 = new byte[]{'h', 'e', 'l', 'l', 'o', 'k', 0, 0};
        byte[] buffer2 = new byte[]{'h', 'e', 'l', 'l', 'o'};

        boolean result = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        assertTrue(result);
    }

    @Test
    public void testIsEqualWithNullTrailingInSecondBuffer() {
        byte[] buffer1 = new byte[]{'h', 'e', 'l', 'l', 'o'};
        byte[] buffer2 = new byte[]{'h', 'e', 'l', 'l', 'o', 0, 0};

        boolean result = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        assertTrue(result);
    }

    @Test
    public void testIsEqualWithNullTrailingInBothBuffers() {
        byte[] buffer1 = new byte[]{'h', 'e', 'l', 'l', 'o', 0, 0};
        byte[] buffer2 = new byte[]{'h', 'e', 'l', 'l', 'o', 0, 0};

        boolean result = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        assertTrue(result);
    }

    @Test
    public void testIsEqualWithOffsetAndLength() {
        byte[] buffer1 = new byte[]{'h', 'e', 'l', 'l', 'o'};
        byte[] buffer2 = new byte[]{'h', 'e', 'l', 'l', 'o'};

        boolean result = ArchiveUtils.isEqual(buffer1, 1, 3, buffer2, 1, 3, false);
        assertTrue(result);
    }

    @Test
    public void testIsEqualWithDifferentLength() {
        byte[] buffer1 = new byte[]{'h', 'e', 'l', 'l', 'o'};
        byte[] buffer2 = new byte[]{'h', 'e', 'l', 'l', 'o', 'k'};

        boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false);
        assertFalse(result);
    }

    @Test
    public void testIsEqualWithIgnoredTrailingNulls() {
        byte[] buffer1 = new byte[]{'h', 'e', 'l', 'l', 'o', 0, 0};
        byte[] buffer2 = new byte[]{'h', 'e', 'l', 'l', 'o'};

        boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, true);
        assertTrue(result);
    }

    @Test
    public void testIsEqualWithIgnoredTrailingNullsInSecondBuffer() {
        byte[] buffer1 = new byte[]{'h', 'e', 'l', 'l', 'o'};
        byte[] buffer2 = new byte[]{'h', 'e', 'l', 'l', 'o', 0, 0};

        boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, true);
        assertTrue(result);
    }

    @Test
    public void testIsEqualWithIgnoredTrailingNullsInBothBuffers() {
        byte[] buffer1 = new byte[]{'h', 'e', 'l', 'l', 'o', 0, 0};
        byte[] buffer2 = new byte[]{'h', 'e', 'l', 'l', 'o', 0, 0};

        boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, true);
        assertTrue(result);
    }

    @Test
    public void testIsEqualWithOffsetAndLengthAndIgnoredTrailingNulls() {
        byte[] buffer1 = new byte[]{'h', 'e', 'l', 'l', 'o'};
        byte[] buffer2 = new byte[]{'h', 'e', 'l', 'l', 'o'};

        boolean result = ArchiveUtils.isEqual(buffer1, 1, 3, buffer2, 1, 3, true);
        assertTrue(result);
    }

    @Test
    public void testIsArrayZero() {
        byte[] a = new byte[0];
        assertTrue(ArchiveUtils.isArrayZero(a, 0));

        byte[] a1 = new byte[]{'h'};
        assertFalse(ArchiveUtils.isArrayZero(a1, 0));

        byte[] a2 = new byte[]{0};
        assertFalse(ArchiveUtils.isArrayZero(a2, 0));

        byte[] a3 = new byte[]{0, 0};
        assertTrue(ArchiveUtils.isArrayZero(a3, 0));

        byte[] a4 = new byte[]{'h', 0};
        assertFalse(ArchiveUtils.isArrayZero(a4, 0));

        byte[] a5 = new byte[]{'h', 0, 0};
        assertTrue(ArchiveUtils.isArrayZero(a5, 1));
    }

    @Test
    public void testSanitize() {
        String input = "안녕하세요, 아파치 압축 유틸즈입니다!";
        String output = ArchiveUtils.sanitize(input);
        String expected = "???????? ??????? ?????? ?????? ?????? ?????? ?????????? ?????????";
        assertEquals(expected, output);
    }
}