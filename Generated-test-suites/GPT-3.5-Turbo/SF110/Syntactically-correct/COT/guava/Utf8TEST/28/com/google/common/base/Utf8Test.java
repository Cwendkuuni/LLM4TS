
import org.junit.Test;

import static org.junit.Assert.*;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        // Basic test case
        assertEquals(6, Utf8.encodedLength("Hello!"));

        // Edge case - empty string
        assertEquals(0, Utf8.encodedLength(""));

        // Edge case - one-character string
        assertEquals(1, Utf8.encodedLength("a"));

        // Exception handling - input exceeds int limit
        try {
            Utf8.encodedLength("a" + new String(new char[Integer.MAX_VALUE]));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("UTF-8 length does not fit in int: " + (Integer.MAX_VALUE + 4294967296L), e.getMessage());
        }
    }

    @Test
    public void testEncodedLengthGeneral() {
        // Basic test case
        assertEquals(4, Utf8.encodedLengthGeneral("ab\u0800cd", 2));

        // Edge case - empty string
        assertEquals(0, Utf8.encodedLengthGeneral("", 0));

        // Exception handling - unpaired surrogate
        try {
            Utf8.encodedLengthGeneral("a\ud800bc", 1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Unpaired surrogate at index 1", e.getMessage());
        }
    }

    @Test
    public void testIsWellFormed() {
        // Basic test case
        assertTrue(Utf8.isWellFormed(new byte[]{72, 101, 108, 108, 111, 33}));

        // Exception handling - invalid byte sequence
        assertFalse(Utf8.isWellFormed(new byte[]{72, -128, 101, 108, 108, 111, 33}));
    }

    @Test
    public void testIsWellFormedWithOffsetAndLength() {
        // Basic test case
        assertTrue(Utf8.isWellFormed(new byte[]{72, 101, 108, 108, 111, 33}, 0, 6));

        // Exception handling - invalid byte sequence
        assertFalse(Utf8.isWellFormed(new byte[]{72, -128, 101, 108, 108, 111, 33}, 0, 7));

        // Exception handling - invalid offset and length
        try {
            Utf8.isWellFormed(new byte[]{72, 101, 108, 108, 111, 33}, 2, 5);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            assertEquals("Index: 2, Size: 5", e.getMessage());
        }
    }

    @Test
    public void testIsWellFormedSlowPath() {
        // Basic test case
        assertTrue(Utf8.isWellFormedSlowPath(new byte[]{72, 101, 108, 108, 111, 33}, 0, 6));

        // Exception handling - invalid byte sequence
        assertFalse(Utf8.isWellFormedSlowPath(new byte[]{72, -128, 101, 108, 108, 111, 33}, 0, 7));
    }
}
