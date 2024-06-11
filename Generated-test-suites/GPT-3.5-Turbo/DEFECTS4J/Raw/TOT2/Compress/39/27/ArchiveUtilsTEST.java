import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        // Typical use case
        ArchiveEntry entry = new ArchiveEntry();
        entry.setName("example.txt");
        entry.setSize(100);
        entry.setDirectory(false);
        String result = ArchiveUtils.toString(entry);
        assertEquals("-    100 example.txt", result);

        // Edge case: empty entry
        ArchiveEntry emptyEntry = new ArchiveEntry();
        String emptyResult = ArchiveUtils.toString(emptyEntry);
        assertEquals("-         0 ", emptyResult);
    }

    @Test
    public void testMatchAsciiBuffer() {
        // Typical use case
        String expected = "Test";
        byte[] buffer = expected.getBytes();
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));

        // Edge case: mismatch
        String incorrect = "Mismatch";
        byte[] incorrectBuffer = incorrect.getBytes();
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, incorrectBuffer));
    }

    // Additional test cases for other methods can be added here

}