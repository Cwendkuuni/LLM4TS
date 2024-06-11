import org.junit.Test;

import static org.junit.Assert.*;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        // Test with empty string
        assertEquals(0, Utf8.encodedLength(""));

        // Test with ASCII characters
        assertEquals(5, Utf8.encodedLength("Hello"));

        // Test with Unicode characters
        assertEquals(9, Utf8.encodedLength("こんにちは"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLengthOverflow() {
        Utf8.encodedLength("This is a test string that exceeds UTF-8 length limit");
    }

    @Test
    public void testIsWellFormed() {
        // Test with well-formed bytes
        assertTrue(Utf8.isWellFormed(new byte[]{104, 101, 108, 108, 111}));

        // Test with ill-formed bytes
        assertFalse(Utf8.isWellFormed(new byte[]{104, -128, 101, 108, 108, 111}));
    }

    @Test
    public void testIsWellFormedWithOffsetAndLength() {
        // Test with well-formed bytes
        assertTrue(Utf8.isWellFormed(new byte[]{0, 104, 101, 108, 108, 111, 0}, 1, 5));

        // Test with ill-formed bytes
        assertFalse(Utf8.isWellFormed(new byte[]{0, 104, -128, 101, 108, 108, 111, 0}, 1, 6));
    }

    @Test
    public void testIsWellFormedSlowPath() {
        // Test with well-formed bytes
        assertTrue(Utf8.isWellFormedSlowPath(new byte[]{0, 104, 101, 108, 108, 111}, 1, 6));

        // Test with ill-formed bytes
        assertFalse(Utf8.isWellFormedSlowPath(new byte[]{0, 104, -128, 101, 108, 108, 111}, 1, 7));
    }
}