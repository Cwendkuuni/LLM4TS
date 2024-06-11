import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Before;
import org.junit.Test;

public class ArchiveUtilsTest {

    private ArchiveEntry entry;

    @Before
    public void setUp() {
        entry = mock(ArchiveEntry.class);
    }

    @Test
    public void testToString() {
        when(entry.isDirectory()).thenReturn(false);
        when(entry.getSize()).thenReturn(2000L);
        when(entry.getName()).thenReturn("main.c");

        String expected = "-    2000 main.c";
        assertEquals(expected, ArchiveUtils.toString(entry));
    }

    @Test
    public void testToString_Directory() {
        when(entry.isDirectory()).thenReturn(true);
        when(entry.getSize()).thenReturn(100L);
        when(entry.getName()).thenReturn("testfiles");

        String expected = "d     100 testfiles";
        assertEquals(expected, ArchiveUtils.toString(entry));
    }

    @Test
    public void testToString_EmptyName() {
        when(entry.isDirectory()).thenReturn(false);
        when(entry.getSize()).thenReturn(0L);
        when(entry.getName()).thenReturn("");

        String expected = "-       0 ";
        assertEquals(expected, ArchiveUtils.toString(entry));
    }

    @Test
    public void testToString_NullEntry() {
        assertNull(ArchiveUtils.toString(null));
    }

    @Test
    public void testMatchAsciiBuffer() {
        byte[] buffer = new byte[]{97, 98, 99};
        String expected = "abc";
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    @Test
    public void testMatchAsciiBuffer_NotMatch() {
        byte[] buffer = new byte[]{97, 98, 99};
        String expected = "def";
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    @Test
    public void testMatchAsciiBuffer_WithOffsetAndLength() {
        byte[] buffer = new byte[]{97, 98, 99, 100, 101};
        String expected = "abc";
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, 3));
    }

    @Test
    public void testMatchAsciiBuffer_WithOffsetAndLength_NotMatch() {
        byte[] buffer = new byte[]{97, 98, 99, 100, 101};
        String expected = "def";
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, 3));
    }

    @Test
    public void testToAsciiBytes() {
        String inputString = "abc";
        byte[] expected = new byte[]{97, 98, 99};
        assertArrayEquals(expected, ArchiveUtils.toAsciiBytes(inputString));
    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes = new byte[]{97, 98, 99};
        String expected = "abc";
        assertEquals(expected, ArchiveUtils.toAsciiString(inputBytes));
    }

    @Test
    public void testToAsciiString_WithOffsetAndLength() {
        byte[] inputBytes = new byte[]{97, 98, 99, 100, 101};
        String expected = "bc";
        assertEquals(expected, ArchiveUtils.toAsciiString(inputBytes, 1, 2));
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = new byte[]{97, 98, 99};
        byte[] buffer2 = new byte[]{97, 98, 99};
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
    }

    @Test
    public void testIsEqual_NotEqual() {
        byte[] buffer1 = new byte[]{97, 98, 99};
        byte[] buffer2 = new byte[]{100, 101, 102};
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer2));
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = new byte[]{97, 98, 99};
        byte[] buffer2 = new byte[]{97, 98, 99};
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, 3, buffer2, 0, 3));
    }
    
    @Test
    public void testIsEqualWithNull_NotEqual() {
        byte[] buffer1 = new byte[]{97, 98, 99};
        byte[] buffer2 = new byte[]{100, 101, 102};
        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, 0, 3, buffer2, 0, 3));
    }

    @Test
    public void testIsEqualWithIgnoreTrailingNulls() {
        byte[] buffer1 = new byte[]{97, 98, 0, 0, 0};
        byte[] buffer2 = new byte[]{97, 98};
        assertTrue(ArchiveUtils.isEqual(buffer1, 0, 5, buffer2, 0, 2, true));
    }
    
    @Test
    public void testIsEqualWithIgnoreTrailingNulls_NotEqual() {
        byte[] buffer1 = new byte[]{97, 98, 0, 0};
        byte[] buffer2 = new byte[]{97, 98, 0};
        assertFalse(ArchiveUtils.isEqual(buffer1, 0, 4, buffer2, 0, 3, true));
    }

    @Test
    public void testIsEqual_WithOffsetAndLength() {
        byte[] buffer1 = new byte[]{97, 98, 99, 100, 101};
        byte[] buffer2 = new byte[]{100, 101};
        assertTrue(ArchiveUtils.isEqual(buffer1, 2, 3, buffer2, 0, 2));
    }

    @Test
    public void testIsEqual_WithOffsetAndLength_NotEqual() {
        byte[] buffer1 = new byte[]{97, 98, 99, 100, 101};
        byte[] buffer2 = new byte[]{100, 101, 102};
        assertFalse(ArchiveUtils.isEqual(buffer1, 2, 3, buffer2, 0, 3));
    }

    @Test
    public void testIsArrayZero_AllZero() {
        byte[] a = new byte[]{0, 0, 0, 0};
        int size = 4;
        assertTrue(ArchiveUtils.isArrayZero(a, size));
    }

    @Test
    public void testIsArrayZero_NotAllZero() {
        byte[] a = new byte[]{0, 1, 0, 0};
        int size = 4;
        assertFalse(ArchiveUtils.isArrayZero(a, size));
    }

    @Test
    public void testSanitize() {
        String input = "\u0001abc\u0013def\u007F";
        String expected = "?abc?def?";
        assertEquals(expected, ArchiveUtils.sanitize(input));
    }

    @Test
    public void testSanitize_NoSpecialCharacters() {
        String input = "abcdef";
        assertEquals(input, ArchiveUtils.sanitize(input));
    }

    @Test
    public void testSanitize_EmptyString() {
        String input = "";
        assertEquals(input, ArchiveUtils.sanitize(input));
    }

    @Test
    public void testSanitize_NullString() {
        assertNull(ArchiveUtils.sanitize(null));
    }
}