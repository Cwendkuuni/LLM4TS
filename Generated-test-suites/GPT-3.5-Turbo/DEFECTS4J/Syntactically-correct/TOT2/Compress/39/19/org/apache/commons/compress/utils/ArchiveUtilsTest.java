import static org.junit.Assert.*;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        // Typical use case
        ArchiveEntry entry = new ArchiveEntry("example.txt");
        entry.setDirectory(false);
        entry.setSize(2000);
        assertEquals("-    2000 example.txt", ArchiveUtils.toString(entry));

        // Edge case
        entry.setSize(100);
        entry.setDirectory(true);
        assertEquals("d     100 example.txt", ArchiveUtils.toString(entry));

        // Error scenario
        assertNull(ArchiveUtils.toString(null));
    }

    @Test
    public void testMatchAsciiBuffer() {
        // Typical use case
        byte[] buffer = "test".getBytes();
        assertTrue(ArchiveUtils.matchAsciiBuffer("test", buffer));

        // Edge case
        byte[] buffer2 = "abc".getBytes();
        assertFalse(ArchiveUtils.matchAsciiBuffer("def", buffer2));

        // Error scenario
        assertFalse(ArchiveUtils.matchAsciiBuffer("error", null));
    }

    // Other test methods for the remaining public methods in ArchiveUtils class...

}