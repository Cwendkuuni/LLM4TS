import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.archivers.ArchiveEntry;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry();
        entry.setName("testFile.txt");
        entry.setSize(100);
        entry.setDirectory(false);
        
        String expected = "-     100 testFile.txt";
        assertEquals(expected, ArchiveUtils.toString(entry));
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "Hello";
        byte[] buffer = expected.getBytes();
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    @Test
    public void testToAsciiBytes() {
        String inputString = "AsciiString";
        byte[] expectedBytes = {65, 115, 99, 105, 105, 83, 116, 114, 105, 110, 103};
        assertArrayEquals(expectedBytes, ArchiveUtils.toAsciiBytes(inputString));
    }

    @Test
    public void testIsEqual() {
        byte[] buffer1 = {1, 2, 3, 4};
        byte[] buffer2 = {1, 2, 3, 4};
        assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
    }

    @Test
    public void testIsArrayZero() {
        byte[] a = {0, 0, 0, 0};
        assertTrue(ArchiveUtils.isArrayZero(a, 4));
    }

    @Test
    public void testSanitize() {
        String input = "This is a test string \nwith newline character";
        String expected = "This is a test string ?with newline character";
        assertEquals(expected, ArchiveUtils.sanitize(input));
    }
}