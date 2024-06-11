package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.Test;

/**
 * Test class for ArchiveUtils
 */
public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry("main.c");
        String result = ArchiveUtils.toString(entry);
        String expected = "- 2000 main.c";
        assertTrue(result.equals(expected));

        entry = new ArchiveEntry("testfiles");
        entry.setDirectory(true);
        result = ArchiveUtils.toString(entry);
        expected = "d 100 testfiles";
        assertTrue(result.equals(expected));
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "expected string";
        byte[] buffer = "matching string".getBytes();

        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    @Test
    public void testMatchAsciiBuffer_False() {
        String expected = "expected string";
        byte[] buffer = "different string".getBytes();

        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    @Test
    public void testToAsciiBytes() {
        String inputString = "input string";
        byte[] result = ArchiveUtils.toAsciiBytes(inputString);

        byte[] expected = inputString.getBytes(CharsetNames.US_ASCII);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes = "input bytes".getBytes();
        String result = ArchiveUtils.toAsciiString(inputBytes);

        String expected = new String(inputBytes, CharsetNames.US_ASCII);
        assertTrue(result.equals(expected));
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[]{'A', 'B', 'C'};
        byte[] buffer2 = new byte[]{'A', 'B', 'C'};
        byte[] buffer3 = new byte[]{'A', 'B', 'D'};

        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length));
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = new byte[]{'A', 'B', 'C', 0};
        byte[] buffer2 = new byte[]{'A', 'B', 'C'};
        byte[] buffer3 = new byte[]{'A', 'B', 'C', 'D'};

        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length - 1, buffer2, 0, buffer2.length));
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length - 1, buffer3, 0, buffer3.length));
    }

    @Test
    public void testIsArrayZero() {
        byte[] a = new byte[0];
        byte[] b = new byte[]{0};
        byte[] c = new byte[]{1};

        assertTrue(ArchiveUtils.isArrayZero(a, 0));
        assertTrue(ArchiveUtils.isArrayZero(b, 0));
        assertFalse(ArchiveUtils.isArrayZero(c, 0));
    }

    @Test
    public void testSanitize() {
        String input = "test\nfile Name with spaces and \nnewline character";
        String expected = "test????? file Name with spaces and ???? newline character";
        String result = ArchiveUtils.sanitize(input);

        assertTrue(result.equals(expected));
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testToAsciiBytes_UnsupportedEncodingException() {
        String inputString = "input string";
        ArchiveUtils.toAsciiBytes(inputString); // This should throw an UnsupportedEncodingException
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testToAsciiString_UnsupportedEncodingException() {
        byte[] inputBytes = new byte[]{1, 2, 3};
        ArchiveUtils.toAsciiString(inputBytes); // This should throw an UnsupportedEncodingException
    }
}