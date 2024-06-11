import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.apache.commons.compress.archivers.ArchiveEntry;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry();
        entry.setDirectory(true);
        entry.setSize(100);
        entry.setName("testfiles");

        String expected = "d     100 testfiles";
        assertEquals(expected, ArchiveUtils.toString(entry));
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "test";
        byte[] buffer = expected.getBytes();

        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    @Test
    public void testToAsciiBytes() {
        String inputString = "test";
        byte[] expected = inputString.getBytes();

        assertArrayEquals(expected, ArchiveUtils.toAsciiBytes(inputString));
    }

    @Test
    public void testToAsciiString() {
        byte[] inputBytes = "test".getBytes();
        String expected = "test";

        assertEquals(expected, ArchiveUtils.toAsciiString(inputBytes));
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = "test".getBytes();
        byte[] buffer2 = "test".getBytes();

        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
    }

    @Test
    public void testIsArrayZero() {
        byte[] array = new byte[10];
        assertTrue(ArchiveUtils.isArrayZero(array, 10));
    }

    @Test
    public void testSanitize() {
        String input = "special\tchars\0to\0replace";
        String expected = "special?chars?to?replace";

        assertEquals(expected, ArchiveUtils.sanitize(input));
    }

}