import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.ArchiveEntry;

public class ArchiveUtilsTest {

    @Test
    public void testToStringDirectory() {
        ArchiveEntry entry = new ArchiveEntry();
        entry.setName("testfiles");
        entry.setDirectory(true);
        entry.setSize(100);
        assertEquals("d     100 testfiles", ArchiveUtils.toString(entry));
    }

    @Test
    public void testToStringFile() {
        ArchiveEntry entry = new ArchiveEntry();
        entry.setName("main.c");
        entry.setDirectory(false);
        entry.setSize(2000);
        assertEquals("-    2000 main.c", ArchiveUtils.toString(entry));
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "test";
        byte[] buffer = "test".getBytes();
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    @Test
    public void testMatchAsciiBufferWithOffsetAndLength() {
        String expected = "test";
        byte[] buffer = "testing123".getBytes();
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, 4));
    }

    @Test
    public void testToAsciiBytes() {
        String inputString = "test";
        byte[] expected = "test".getBytes();
        assertArrayEquals(expected, ArchiveUtils.toAsciiBytes(inputString));
    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes = "test".getBytes();
        String expected = "test";
        assertEquals(expected, ArchiveUtils.toAsciiString(inputBytes));
    }

    @Test
    public void testToAsciiStringWithOffsetAndLength() {
        byte[] inputBytes = "testing123".getBytes();
        String expected = "test";
        assertEquals(expected, ArchiveUtils.toAsciiString(inputBytes, 0, 4));
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = "test1".getBytes();
        byte[] buffer2 = "test2".getBytes();
        assertFalse(ArchiveUtils.isEqual(buffer1, buffer2));
    }

    @Test
    public void testIsEqualWithIgnoreTrailingNulls() {
        byte[] buffer1 = "test\0\0\0".getBytes();
        byte[] buffer2 = "test".getBytes();
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, true));
    }

    @Test
    public void testIsEqualWithNull() {
        byte[] buffer1 = "test".getBytes();
        byte[] buffer2 = "test".getBytes();
        assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
    }

    @Test
    public void testIsArrayZero() {
        byte[] a = new byte[] {0, 0, 0, 0, 0};
        int size = 5;
        assertTrue(ArchiveUtils.isArrayZero(a, size));
    }

    @Test
    public void testSanitize() {
        String s = "test$%^&*()";
        String expected = "test$%^&*()";
        assertEquals(expected, ArchiveUtils.sanitize(s));
    }
}