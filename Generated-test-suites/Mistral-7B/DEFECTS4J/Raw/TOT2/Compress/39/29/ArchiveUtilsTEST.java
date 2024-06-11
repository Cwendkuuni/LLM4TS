
package org.apache.commons.compress.utils;

import org.apache.commons.compress.archivers.test.TestFiles;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveUtilsTest {

    @Test
    public void testToStringMethod() {
        ArchiveEntry entry = TestFiles.getTestEntry("testfile.txt");
        String result = ArchiveUtils.toString(entry);
        String expected = "- 0 testfile.txt";
        assertEquals(expected, result);
    }

    @Test
    public void testToStringNullEntry() {
        ArchiveEntry entry = null;
        String result = ArchiveUtils.toString(entry);
        assertNull(result);
    }

    @Test
    public void testToStringDirectory() {
        ArchiveEntry entry = TestFiles.getTestDirectory();
        String result = ArchiveUtils.toString(entry);
        String expected = "d 0 ...";
        assertEquals(expected, result);
    }

    @Test
    public void testMatchAsciiBuffer() {
        byte[] buffer = {65, 66, 67};
        String expected = "ABC";
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    @Test
    public void testMatchAsciiBufferDifferentStrings() {
        byte[] buffer = {65, 66, 67};
        String expected = "DEF";
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    @Test
    public void testMatchAsciiBufferLongStrings() {
        byte[] buffer = {65, 66, 67, 68, 69, 70};
        String expected = "ABCDEF";
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    @Test
    public void testMatchAsciiBufferNullStrings() {
        byte[] buffer = {65, 66, 67};
        String expected = null;
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    @Test
    public void testMatchAsciiBufferNonAsciiBytes() {
        byte[] buffer = {65, 66, 67, 0x80};
        String expected = "ABC";
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    @Test
    public void testToAsciiBytes() {
        String inputString = "ABC";
        byte[] result = ArchiveUtils.toAsciiBytes(inputString);
        byte[] expected = {65, 66, 67};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testToAsciiBytesNullString() {
        String inputString = null;
        byte[] result = ArchiveUtils.toAsciiBytes(inputString);
        assertNull(result);
    }

    @Test
    public void testIsEqualMethod() {
        byte[] buffer1 = {65, 66, 67};
        byte[] buffer2 = {65, 66, 67};
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
    }

    @Test
    public void testIsEqualDifferentBuffers() {
        byte[] buffer1 = {65, 66, 67};
        byte[] buffer2 = {65, 66, 68};
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer2));
    }

    @Test
    public void testIsEqualBuffersWithTrailingNulls() {
        byte[] buffer1 = {65, 66, 67};
        byte[] buffer2 = {65, 66, 67, 0};
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, buffer2, 3, 4));
    }

    @Test
    public void testIsEqualBuffersDifferentTrailingNulls() {
        byte[] buffer1 = {65, 66, 67, 0};
        byte[] buffer2 = {65, 66, 67};
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, buffer2, 4, 3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsEqualNullBuffers() {
        byte[] buffer1 = null;
        byte[] buffer2 = {65, 66, 67};
        ArchiveUtils.isEqual(buffer1, buffer2);
    }

    @Test
    public void testIsEqualBuffersWithNonAsciiBytes() {
        byte[] buffer1 = {65, 66, 67};
        byte[] buffer2 = {65, 66, 67, 0x80};
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer2));
    }
}
