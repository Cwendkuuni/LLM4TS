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
        String result = ArchiveUtils.toString(entry);
        String expected = "- 2000 main.c";
        assertEquals(expected, result);

        entry = new ArchiveEntry("testfiles");
        result = ArchiveUtils.toString(entry);
        expected = "d 100 testfiles";
        assertEquals(expected, result);
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "test";
        byte[] buffer = new byte[5];
        Arrays.fill(buffer, 0, 3, 't'.toString().getBytes()[0]);
        Arrays.fill(buffer, 3, 5, 'e'.toString().getBytes()[0]);

        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, 5));

        expected = "invalid";
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, 3));

        byte[] bufferWithInvalid = new byte[5];
        Arrays.fill(bufferWithInvalid, 0, 3, 't'.toString().getBytes()[0]);
        Arrays.fill(bufferWithInvalid, 3, 5, (byte) 0xFF);

        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, bufferWithInvalid, 0, 5));
    }

    @Test
    public void testToStringAsciiBytes() {
        String expected = "test";
        byte[] asciiBytes = ArchiveUtils.toAsciiBytes(expected);

        assertArrayEquals(expected.getBytes(), asciiBytes);
    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes = new byte[]{0x74, 0x65, 0x73, 0x74};
        String expected = "test";
        String result = ArchiveUtils.toAsciiString(inputBytes);

        assertEquals(expected, result);

        byte[] inputBytesWithNull = new byte[]{0x74, 0x65, 0x73, 0x74, 0};
        String resultWithNull = ArchiveUtils.toAsciiString(inputBytesWithNull, 0, 5);
        String expectedWithNull = "test\0";
        assertEquals(expectedWithNull, resultWithNull);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[]{0x74, 0x65, 0x73, 0x74};
        byte[] buffer2 = new byte[]{0x74, 0x65, 0x73, 0x74};
        byte[] buffer3 = new byte[]{0x74, 0x65, 0x73, 0x73};
        byte[] buffer4 = new byte[]{0x74, 0x65, 0x73, 0x75};

        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer3));
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer4));

        byte[] buffer5 = new byte[]{0x74, 0x65, 0x73, 0x73, 0};
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer5, 0, 5, false));
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer5, 0, 4, false));
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer5, 0, 5, true));
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = new byte[]{0x74, 0x65, 0x73, 0x73, 0};
        byte[] buffer2 = new byte[]{0x74, 0x65, 0x73, 0x73};

        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, buffer2, 0, 5));
    }

    @Test
    public void testIsArrayZero() {
        byte[] arrayZero = new byte[5];

        assertTrue(ArchiveUtils.isArrayZero(arrayZero, 5));
    }

    @Test
    public void testSanitize() {
        String s = "test\u0001\u007Fexample";
        String result = ArchiveUtils.sanitize(s);

        String expected = "test?example";
        assertEquals(expected, result);
    }
}