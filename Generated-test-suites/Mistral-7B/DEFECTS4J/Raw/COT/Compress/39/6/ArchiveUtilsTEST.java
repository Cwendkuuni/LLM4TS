package org.apache.commons.compress.utils;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry("main.c");
        ArchiveEntry directoryEntry = new ArchiveEntry("testfiles/");

        assertEquals("- 2000 main.c\n", ArchiveUtils.toString(entry));
        assertEquals("d 100 testfiles/\n", ArchiveUtils.toString(directoryEntry));
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "expected string";
        byte[] buffer = toAsciiBytes(expected);

        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    @Test(expected = RuntimeException.class)
    public void testMatchAsciiBufferWithInvalidEncoding() {
        String expected = "expected string";
        byte[] buffer = new byte[10]; // arbitrary buffer

        ArchiveUtils.matchAsciiBuffer(expected, buffer);
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = toAsciiBytes("expected string 1");
        byte[] buffer2 = toAsciiBytes("expected string 2");

        assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, false));
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 1, buffer2.length, false));
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = new byte[10];
        Arrays.fill(buffer1, (byte) 0);
        byte[] buffer2 = new byte[10];
        Arrays.fill(buffer2, (byte) 0);

        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 1, buffer2.length));
    }

    @Test
    public void testIsArrayZero() {
        byte[] array = new byte[10];
        Arrays.fill(array, (byte) 0);

        assertTrue(ArchiveUtils.isArrayZero(array, array.length));
    }

    @Test
    public void testSanitize() {
        String s = "Sanitized string with non-printable characters: ♫♫♫";
        String sanitized = ArchiveUtils.sanitize(s);

        assertEquals(s, "Sanitized string with non-printable characters: ♫♫♫");
        assertEquals(sanitized, sanitized.replaceAll("[^\\x20-\\x7E]", "?"));
        assertEquals(sanitized, sanitized.substring(0, 255));
    }
}