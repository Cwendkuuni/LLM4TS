import static org.junit.Assert.*;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        // Typical use case
        ArchiveEntry entry = new ArchiveEntry();
        entry.setName("test.txt");
        entry.setSize(100);
        assertFalse(ArchiveUtils.toString(entry).isEmpty());

        // Edge case when entry is a directory
        entry.setDirectory(true);
        assertTrue(ArchiveUtils.toString(entry).startsWith("d"));
    }

    @Test
    public void testMatchAsciiBuffer() {
        // Typical use case
        String expected = "test";
        byte[] buffer = "test".getBytes();
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));

        // Edge case with offset and length
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer, 1, 2));
    }

    @Test
    public void testToAsciiBytes() {
        // Typical use case
        String inputString = "test";
        assertNotNull(ArchiveUtils.toAsciiBytes(inputString));
    }

    @Test
    public void testToAsciiString() {
        // Typical use case
        byte[] inputBytes = "test".getBytes();
        assertNotNull(ArchiveUtils.toAsciiString(inputBytes));

        // Edge case with offset and length
        assertNotNull(ArchiveUtils.toAsciiString(inputBytes, 0, 2));
    }

    @Test
    public void testIsEqual() {
        // Typical use case
        byte[] buffer1 = "test".getBytes();
        byte[] buffer2 = "test".getBytes();
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));

        // Edge case with trailing nulls
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2, true));
    }

    @Test
    public void testIsArrayZero() {
        // Typical use case
        byte[] a = new byte[5];
        assertTrue(ArchiveUtils.isArrayZero(a, 5));

        // Edge case with non-zero elements
        a[2] = 1;
        assertFalse(ArchiveUtils.isArrayZero(a, 5));
    }

    @Test
    public void testSanitize() {
        // Typical use case
        String s = "test!";
        assertEquals("test!", ArchiveUtils.sanitize(s));

        // Edge case with non-printable characters
        String s2 = "te\u0001st";
        assertEquals("te?st", ArchiveUtils.sanitize(s2));
    }

}