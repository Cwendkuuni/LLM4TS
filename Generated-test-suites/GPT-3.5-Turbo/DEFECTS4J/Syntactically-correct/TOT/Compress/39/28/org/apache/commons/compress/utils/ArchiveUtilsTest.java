import static org.junit.Assert.*;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry1 = new ArchiveEntry("file1", 100, false);
        String expected1 = "-     100 file1";
        assertEquals(expected1, ArchiveUtils.toString(entry1));

        ArchiveEntry entry2 = new ArchiveEntry("dir1", 0, true);
        String expected2 = "d       0 dir1";
        assertEquals(expected2, ArchiveUtils.toString(entry2));
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "test";
        byte[] buffer = "test".getBytes();

        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, buffer.length));
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer, 1, buffer.length - 1));

        String expected2 = "abc";
        byte[] buffer2 = "abcd".getBytes();
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected2, buffer2, 0, buffer2.length));
    }

    @Test
    public void testToAsciiBytes() {
        String s = "test";
        byte[] expected = "test".getBytes();

        assertArrayEquals(expected, ArchiveUtils.toAsciiBytes(s));
    }

    @Test
    public void testToAsciiString() {
        byte[] buffer = "test".getBytes();
        String expected = "test";

        assertEquals(expected, ArchiveUtils.toAsciiString(buffer));
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = "test".getBytes();
        byte[] buffer2 = "test".getBytes();

        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
    }

    @Test
    public void testIsEqualWithIgnoreTrailingNulls() {
        byte[] buffer1 = "test\0\0\0".getBytes();
        byte[] buffer2 = "test".getBytes();

        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, true));
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = "test\0\0\0".getBytes();
        byte[] buffer2 = "test".getBytes();

        assertFalse(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
    }

    @Test
    public void testIsArrayZero() {
        byte[] buffer = new byte[10];
        int size = 10;

        assertTrue(ArchiveUtils.isArrayZero(buffer, size));

        buffer[5] = 1;
        assertFalse(ArchiveUtils.isArrayZero(buffer, size));
    }

    @Test
    public void testSanitize() {
        String s = "test\0";
        String expected = "test?";

        assertEquals(expected, ArchiveUtils.sanitize(s));
    }

}