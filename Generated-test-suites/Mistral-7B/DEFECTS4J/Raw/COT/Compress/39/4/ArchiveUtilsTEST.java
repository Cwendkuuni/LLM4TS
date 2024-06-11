package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Test class for ArchiveUtils
 */
public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry archiveEntry = new ArchiveEntry("main.c");
        archiveEntry.setSize(2000);
        String expected = "- 2000 main.c";
        String result = ArchiveUtils.toString(archiveEntry);
        assertEquals(expected, result);

        archiveEntry.setDirectory(true);
        expected = "d 100 testfiles";
        result = ArchiveUtils.toString(archiveEntry);
        assertEquals(expected, result);
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "hello world";
        byte[] buffer = expected.getBytes(StandardCharsets.US_ASCII);
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length));
    }

    @Test
    public void testToAsciiBytes() {
        String inputString = "hello world";
        byte[] result = ArchiveUtils.toAsciiBytes(inputString);
        String expected = new String(result, StandardCharsets.US_ASCII);
        assertEquals(inputString, expected);
    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes = "hello world".getBytes(StandardCharsets.US_ASCII);
        String result = ArchiveUtils.toAsciiString(inputBytes);
        assertEquals("hello world", result);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[]{'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd'};
        byte[] buffer2 = new byte[]{'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd'};
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));

        byte[] buffer3 = new byte[]{'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd'};
        byte[] buffer4 = new byte[]{'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd', '!'};
        assertFalse(ArchiveUtils.isEqual(buffer3, buffer4));

        byte[] buffer5 = new byte[]{'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd'};
        byte[] buffer6 = new byte[]{'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd', '!'};
        assertFalse(ArchiveUtils.isEqual(buffer5, buffer6));

        byte[] buffer7 = new byte[]{'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd'};
        byte[] buffer8 = new byte[]{'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd', '!', '!'};
        assertFalse(ArchiveUtils.isEqual(buffer7, buffer8));

        byte[] buffer9 = new byte[]{'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd'};
        byte[] buffer10 = new byte[]{'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd', '\u0000'};
        assertFalse(ArchiveUtils.isEqual(buffer9, buffer10, false));
        assertTrue(ArchiveUtils.isEqual(buffer9, buffer10, true));
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = new byte[]{'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd'};
        byte[] buffer2 = new byte[]{'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd'};
        byte[] buffer3 = new byte[]{'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd', '\u0000'};

        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, buffer2));
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, buffer3));
    }

    @Test
    public void testIsArrayZero() {
        byte[] buffer = new byte[10];
        assertTrue(ArchiveUtils.isArrayZero(buffer, 0));
        assertTrue(ArchiveUtils.isArrayZero(buffer, 1));
        assertTrue(ArchiveUtils.isArrayZero(buffer, 2));
        assertFalse(ArchiveUtils.isArrayZero(buffer, 3));

        byte[] buffer2 = new byte[0];
        assertTrue(ArchiveUtils.isArrayZero(buffer2, 0));
    }

    @Test
    public void testSanitize() {
        String input = "\u0001hello\u001f world\u001d!";
        String expected = "?!????hello??? world";
        String result = ArchiveUtils.sanitize(input);
        assertEquals(expected, result);
    }

    @Test(expected = RuntimeException.class)
    public void testMatchAsciiBuffer_UnsupportedEncodingException() {
        String expected = "hello world";
        byte[] buffer = new byte[11];
        ArchiveUtils.matchAsciiBuffer(expected, buffer);
    }
}