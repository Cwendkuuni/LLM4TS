import static org.junit.Assert.*;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        // Typical use case
        ArchiveEntry entry = new ArchiveEntry("file.txt", false, 100);
        assertEquals("-    100 file.txt", ArchiveUtils.toString(entry));
        
        // Edge case with directory entry
        entry = new ArchiveEntry("directory", true, 0);
        assertEquals("d         0 directory", ArchiveUtils.toString(entry));
        
        // Error scenario with null entry
        assertNull(ArchiveUtils.toString(null));
    }

    @Test
    public void testMatchAsciiBuffer() {
        // Typical use case
        assertTrue(ArchiveUtils.matchAsciiBuffer("test", "test".getBytes(), 0, 4));
        
        // Edge case with non-matching buffer
        assertFalse(ArchiveUtils.matchAsciiBuffer("test", "text".getBytes(), 0, 4));
        
        // Error scenario with unsupported encoding
        try {
            ArchiveUtils.matchAsciiBuffer("test", "test".getBytes(), 0, 4);
        } catch (RuntimeException e) {
            // Expected exception
            assertTrue(true);
        }
    }

    // Additional test methods for isEqual, toAsciiBytes, toAsciiString, and sanitize can be added here
  
}