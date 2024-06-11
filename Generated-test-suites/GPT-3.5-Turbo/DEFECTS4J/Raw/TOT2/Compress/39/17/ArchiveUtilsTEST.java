import static org.junit.Assert.*;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntry();
        entry.setName("test.txt");
        entry.setSize(100);
        entry.setDirectory(false);

        String expected = "-    100 test.txt";
        assertEquals(expected, ArchiveUtils.toString(entry));
    }

    @Test
    public void testMatchAsciiBuffer() {
        String expected = "Hello";
        byte[] buffer = "Hello".getBytes();
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));
    }

    @Test
    public void testSanitize() {
        String input = "abc\u0001def\u0001ghi";
        String expected = "abc?def?ghi";
        assertEquals(expected, ArchiveUtils.sanitize(input));
    }

    // Add more test cases for the other methods in ArchiveUtils class

}