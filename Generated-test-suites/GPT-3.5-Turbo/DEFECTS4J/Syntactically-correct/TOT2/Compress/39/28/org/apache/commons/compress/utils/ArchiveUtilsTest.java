import static org.junit.Assert.*;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        // Typical use case
        ArchiveEntry entry = new ArchiveEntry("testFile.txt", false, 1000);
        String result = ArchiveUtils.toString(entry);
        assertEquals("-  1000 testFile.txt", result);
        
        // Edge case
        entry = new ArchiveEntry("longFileNameWithSpaces.txt", true, 99999);
        result = ArchiveUtils.toString(entry);
        assertEquals("d  99999 longFileNameWithSpaces.txt", result);
        
        // Error scenario
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
        
        // Edge case
        byte[] buffer2 = "test2".getBytes();
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer2));
        
        // Error scenario
        try {
            ArchiveUtils.matchAsciiBuffer(null, buffer);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    // more test cases for other methods...

}