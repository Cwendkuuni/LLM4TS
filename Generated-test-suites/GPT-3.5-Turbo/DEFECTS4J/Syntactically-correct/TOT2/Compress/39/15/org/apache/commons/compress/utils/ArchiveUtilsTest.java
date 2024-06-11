import static org.junit.Assert.*;

import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        // Typical use case
        ArchiveEntry entry = new ArchiveEntry("example.txt", false, 100);
        String expected = "-    100 example.txt";
        String result = ArchiveUtils.toString(entry);
        assertEquals(expected, result);

        // Edge case
        entry = new ArchiveEntry("test", true, 1000);
        expected = "d    1000 test";
        result = ArchiveUtils.toString(entry);
        assertEquals(expected, result);

        // Error scenario
        assertNull(ArchiveUtils.toString(null));
    }

    @Test
    public void testMatchAsciiBuffer() {
        // Typical use case
        String expected = "test";
        byte[] buffer = "test".getBytes();
        assertTrue(ArchiveUtils.matchAsciiBuffer(expected, buffer));

        // Edge case
        expected = "example";
        buffer = "notmatch".getBytes();
        assertFalse(ArchiveUtils.matchAsciiBuffer(expected, buffer));

        // Error scenario
        assertFalse(ArchiveUtils.matchAsciiBuffer(" ", null));
    }

    // Add test cases for other methods...

}