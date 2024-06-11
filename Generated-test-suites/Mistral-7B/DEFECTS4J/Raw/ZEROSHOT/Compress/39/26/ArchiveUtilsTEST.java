package org.apache.commons.compress.utils;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry("main.c");
        String result = ArchiveUtils.toString(entry);
        String expected = "- 2000 main.c";
        assertEquals(expected, result);

        ArchiveEntry directoryEntry = new ArchiveEntry("testfiles");
        result = ArchiveUtils.toString(directoryEntry);
        expected = "d 100 testfiles/";
        assertEquals(expected, result);
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "test";
        byte[] buffer = "test".getBytes();
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));

        expected = "test";
        buffer = new byte[]{0x74, 0x65, 0x73, 0x74}; // ASCII codes of 'test'
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    @Test
    public void testToAsciiBytes() {
        String input = "test";
        byte[] expected = new byte[]{0x74, 0x65, 0x73, 0x74};
        byte[] result = ArchiveUtils.toAsciiBytes(input);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testToAsciiString() {
        byte[] input = new byte[]{0x74, 0x65, 0x73, 0x74};
        String expected = "test";
        String result = ArchiveUtils.toAsciiString(input);
        assertEquals(expected, result);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[]{0x74, 0x65, 0x73, 0x74};
        byte[] buffer2 = new byte[]{0x74, 0x65, 0x73, 0x74};
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));

        byte[] buffer3 = new byte[]{0x74, 0x65, 0x73, 0x74, 0};
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length, false));

        byte[] buffer4 = new byte[]{0x74, 0x65, 0x73, 0x74, 0};
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer4, 0, buffer4.length, true));
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = new byte[]{0x74, 0x65, 0x73, 0x74, 0, 0};
        byte[] buffer2 = new byte[]{0x74, 0x65, 0x73, 0x74};
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));

        byte[] buffer3 = new byte[]{0x74, 0x65, 0x73, 0x74, 0, 0};
        byte[] buffer4 = new byte[]{0x74, 0x65, 0x73, 0x74, 0, 0};
        assertTrue(ArchiveUtils.isEqualWithNull(buffer3, 0, buffer3.length, buffer4, 0, buffer4.length));

        byte[] buffer5 = new byte[]{0x74, 0x65, 0x73, 0x74, 0};
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer5, 0, buffer5.length));

        byte[] buffer6 = new byte[]{0x74, 0x65, 0x73, 0x74, 0, 0, 0};
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer6, 0, buffer6.length));
    }

    @Test
    public void testIsArrayZero() {
        byte[] array = new byte[0];
        assertTrue(ArchiveUtils.isArrayZero(array, 0));

        byte[] array1 = new byte[]{0};
        assertFalse(ArchiveUtils.isArrayZero(array1, 0));

        byte[] array2 = new byte[]{0, 0};
        assertTrue(ArchiveUtils.isArrayZero(array2, 0));

        byte[] array3 = new byte[]{0, 0, 0, 0};
        assertTrue(ArchiveUtils.isArrayZero(array3, 0));

        byte[] array4 = new byte[]{1};
        assertFalse(ArchiveUtils.isArrayZero(array4, 0));
    }

    @Test
    public void testSanitize() {
        String input = "test\\invalid_filename:with|special_characters!@#$%^&*()_+<>?";
        String expected = "test\\invalid_filenam?:with?special_characters!@#$%^&*()_+<>?";
        String result = ArchiveUtils.sanitize(input);
        assertEquals(expected, result);
    }
}