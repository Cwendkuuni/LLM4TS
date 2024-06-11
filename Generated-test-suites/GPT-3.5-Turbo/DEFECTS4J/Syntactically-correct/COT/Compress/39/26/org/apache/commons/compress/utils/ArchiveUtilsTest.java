
import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry1 = new ArchiveEntry("main.c", 2000, false);
        assertEquals("-    2000 main.c", ArchiveUtils.toString(entry1));

        ArchiveEntry entry2 = new ArchiveEntry("testfiles", 100, true);
        assertEquals("d     100 testfiles", ArchiveUtils.toString(entry2));
    }

    @Test
    public void testMatchAsciiBuffer() {
        byte[] buffer1 = { 65, 66, 67 };
        assertTrue(ArchiveUtils.matchAsciiBuffer("ABC", buffer1));

        byte[] buffer2 = { 65, 66, 68 };
        assertFalse(ArchiveUtils.matchAsciiBuffer("ABC", buffer2));
    }

    @Test
    public void testToAsciiBytes() {
        byte[] expected = { 65, 66, 67 };
        assertArrayEquals(expected, ArchiveUtils.toAsciiBytes("ABC"));
    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes = { 65, 66, 67 };
        assertEquals("ABC", ArchiveUtils.toAsciiString(inputBytes));
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = { 65, 66, 67 };
        byte[] buffer2 = { 65, 66, 67 };
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));

        byte[] buffer3 = { 65, 66, 68 };
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer3));
    }

    @Test
    public void testIsEqualWithIgnoreTrailingNulls() {
        byte[] buffer1 = { 65, 66, 67, 0 };
        byte[] buffer2 = { 65, 66, 67 };
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, true));

        byte[] buffer3 = { 65, 66, 68 };
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer3, true));
    }

    @Test
    public void testIsArrayZero() {
        byte[] a1 = { 0, 0, 0 };
        assertTrue(ArchiveUtils.isArrayZero(a1, 3));

        byte[] a2 = { 0, 1, 0 };
        assertFalse(ArchiveUtils.isArrayZero(a2, 3));
    }

    @Test
    public void testSanitize() {
        String s1 = "abc123";
        assertEquals("abc123", ArchiveUtils.sanitize(s1));

        String s2 = "abc\u0080\u0081\u0082";
        assertEquals("abc???", ArchiveUtils.sanitize(s2));
    }

}