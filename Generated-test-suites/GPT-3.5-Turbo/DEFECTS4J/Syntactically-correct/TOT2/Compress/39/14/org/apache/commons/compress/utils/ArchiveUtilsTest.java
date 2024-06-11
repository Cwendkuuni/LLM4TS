import static org.junit.Assert.*;
import org.junit.Test;

public class ArchiveUtilsTest {

    @Test
    public void testToString() {
        // Typical use case
        assertEquals("-    2000 main.c", ArchiveUtils.toString(mockArchiveEntry));

        // Edge case: empty entry
        assertEquals("-       0 ", ArchiveUtils.toString(emptyEntry));

        // Error scenario: null entry
        assertThrows(NullPointerException.class, () -> ArchiveUtils.toString(null));
    }

    @Test
    public void testMatchAsciiBuffer() {
        // Typical use case
        assertTrue(ArchiveUtils.matchAsciiBuffer("test", new byte[] {'t', 'e', 's', 't'}));

        // Edge case: mismatched buffer
        assertFalse(ArchiveUtils.matchAsciiBuffer("test", new byte[] {'a', 'b', 'c', 'd'}));

        // Error scenario: null buffer
        assertThrows(NullPointerException.class, () -> ArchiveUtils.matchAsciiBuffer("test", null));
    }

    @Test
    public void testToAsciiBytes() {
        // Typical use case
        assertArrayEquals(new byte[] {'t', 'e', 's', 't'}, ArchiveUtils.toAsciiBytes("test"));

        // Edge case: empty string
        assertArrayEquals(new byte[0], ArchiveUtils.toAsciiBytes(""));

        // Error scenario: null string
        assertThrows(NullPointerException.class, () -> ArchiveUtils.toAsciiBytes(null));
    }

    // Add more test methods for each public method in ArchiveUtils

}