package org.apache.commons.compress.utils;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToStringMethod() {
        ArchiveEntry archiveEntry = new ArchiveEntry("main.c");
        ArchiveEntry directoryEntry = new ArchiveEntry("testfiles");
        ArchiveEntry emptyEntry = new ArchiveEntry("");

        String actualToStringMain = ArchiveUtils.toString(archiveEntry);
        String expectedToStringMain = "- 2000 main.c";
        assertEquals(expectedToStringMain, actualToStringMain);

        String actualToStringDirectory = ArchiveUtils.toString(directoryEntry);
        String expectedToStringDirectory = "d 100 testfiles";
        assertEquals(expectedToStringDirectory, actualToStringDirectory);

        String actualToStringEmpty = ArchiveUtils.toString(emptyEntry);
        String expectedToStringEmpty = "- 0 ";
        assertEquals(expectedToStringEmpty, actualToStringEmpty);
    }

    @Test
    public void testMatchAsciiBufferMethod() {
        String expected = "Hello World";
        byte[] buffer = toAsciiBytes(expected);

        boolean actual = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        assertTrue(actual);
    }

    @Test
    public void testMatchAsciiBufferNegativeCase() {
        String expected = "Hello World";
        byte[] buffer = new byte[10];

        boolean actual = ArchiveUtils.matchAsciiBuffer(expected, buffer);
        assertFalse(actual);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testToAsciiBytesThrowExceptionIfUnsupportedEncoding() {
        String inputString = "Hello World with ¥en ├╝mlauts";
        ArchiveUtils.toAsciiBytes(inputString);
    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes = toAsciiBytes("Hello World");
        String actualToString = ArchiveUtils.toAsciiString(inputBytes);
        String expectedToString = "Hello World";
        assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testToAsciiStringNegativeCase() {
        byte[] inputBytes = new byte[10];
        String actualToString = ArchiveUtils.toAsciiString(inputBytes);
        assertEquals("", actualToString);
    }

    @Test
    public void testIsEqualMethod() {
        byte[] buffer1 = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
        byte[] buffer2 = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
        byte[] buffer3 = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', '!'};
        byte[] buffer4 = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', '!', '!'};

        boolean actual = ArchiveUtils.isEqual(buffer1, buffer2, 0, buffer2, 0, buffer2.length, false);
        assertTrue(actual);

        boolean actual2 = ArchiveUtils.isEqual(buffer1, buffer3, 0, buffer3, 0, buffer3.length, false);
        assertFalse(actual2);

        boolean actual3 = ArchiveUtils.isEqual(buffer1, buffer4, 0, buffer4, 0, buffer4.length, false);
        assertFalse(actual3);
    }

    @Test
    public void testIsEqualMethodWithNull() {
        byte[] buffer1 = new byte[]{'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd'};
        byte[] buffer2 = new byte[10];
        byte[] buffer3 = new byte[10];

        Arrays.fill(buffer2, (byte) 0);
        Arrays.fill(buffer3, (byte) 0);

        boolean actual1 = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
        assertFalse(actual1);

        boolean actual2 = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length);
        assertTrue(actual2);
    }

    @Test
    public void testIsArrayZeroMethod() {
        byte[] a = new byte[10];
        Arrays.fill(a, (byte) 0);

        boolean actual = ArchiveUtils.isArrayZero(a, 10);
        assertTrue(actual);
    }

    @Test
    public void testIsArrayZeroMethodNegativeCase() {
        byte[] a = new byte[10];
        Arrays.fill(a, (byte) 1);

        boolean actual = ArchiveUtils.isArrayZero(a, 10);
        assertFalse(actual);
    }

    @Test
    public void testSanitizeMethod() {
        String input = "Hello World with ¥en ├╝mlauts";
        String expected = "Hello World with ????? ??????";
        String actual = ArchiveUtils.sanitize(input);
        assertEquals(expected, actual);
    }

}