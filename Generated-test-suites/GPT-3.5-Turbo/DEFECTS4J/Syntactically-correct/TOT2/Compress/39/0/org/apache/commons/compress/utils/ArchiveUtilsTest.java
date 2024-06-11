import static org.junit.Assert.*;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        // Typical use case
        ArchiveEntry entry = new ArchiveEntry();
        entry.setName("example.txt");
        entry.setSize(100);
        entry.setDirectory(false);

        String expected = "-    100 example.txt";
        assertEquals(expected, ArchiveUtils.toString(entry));

        // Edge case: Empty entry
        entry = new ArchiveEntry();
        expected = "-         ";
        assertEquals(expected, ArchiveUtils.toString(entry));
        
        // Error scenario: Null entry
        try {
            ArchiveUtils.toString(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testMatchAsciiBuffer() {
        // Typical use case
        String expected = "test";
        byte[] buffer = expected.getBytes();
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));

        // Edge case: Different length
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, 3));

        // Error scenario: Unsupported encoding
        try {
            ArchiveUtils.matchAsciiBuffer(expected, buffer, 0, 4);
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            // Expected exception
        }
    }

    // Add test cases for other methods in ArchiveUtils

}