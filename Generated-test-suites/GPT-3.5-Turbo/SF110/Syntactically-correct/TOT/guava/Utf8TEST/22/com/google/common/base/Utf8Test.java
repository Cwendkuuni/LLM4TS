import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

    @Test
    public void testEncodedLength_typicalCase() {
        // Typical case with input containing characters below \u0800
        String input = "Hello, world!";
        int expected = 13;
        assertEquals(expected, Utf8.encodedLength(input));
    }

    @Test
    public void testEncodedLength_edgeCase_singleByteCharacter() {
        // Edge case with input containing single byte character
        String input = "A";
        int expected = 1;
        assertEquals(expected, Utf8.encodedLength(input));
    }

    @Test
    public void testEncodedLength_edgeCase_multiByteCharacter() {
        // Edge case with input containing multi-byte character
        String input = "\u20AC"; // Euro symbol
        int expected = 3;
        assertEquals(expected, Utf8.encodedLength(input));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLength_errorCase_utf8LengthExceedsIntMaxValue() {
        // Error case where UTF-8 length exceeds int max value
        String input = String.join("", Collections.nCopies(1_000_000, "a"));
        Utf8.encodedLength(input);
    }

    @Test
    public void testIsWellFormed_typicalCase() {
        // Typical case with well-formed UTF-8 bytes
        byte[] input = {72, 101, 108, 108, 111, 44, 32, 119, 111, 114, 108, 100, 33};
        assertTrue(Utf8.isWellFormed(input));
    }

    @Test
    public void testIsWellFormed_edgeCase_invalidUtf8Byte() {
        // Edge case with input containing an invalid UTF-8 byte
        byte[] input = {72, -127, 108, 108, 111};
        assertFalse(Utf8.isWellFormed(input));
    }

    @Test
    public void testIsWellFormed_errorCase_invalidUtf8Sequence() {
        // Error case with input containing an invalid UTF-8 sequence
        byte[] input = {-20, -96, -122, 108, 108, 111}; // Invalid UTF-8 sequence for euro symbol
        assertFalse(Utf8.isWellFormed(input));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIsWellFormed_errorCase_invalidIndexes() {
        // Error case with invalid indexes
        byte[] input = {72, 101, 108, 108, 111};
        Utf8.isWellFormed(input, -1, 6);
    }

    @Test
    public void testIsWellFormedSlowPath_typicalCase() {
        // Typical case with well-formed UTF-8 bytes for slow path
        byte[] input = {-62, -128, 72, -62, -119, 101, 108};
        assertTrue(Utf8.isWellFormedSlowPath(input, 2, 7));
    }

    @Test
    public void testIsWellFormedSlowPath_edgeCase_invalidUtf8Byte() {
        // Edge case with input containing an invalid UTF-8 byte for slow path
        byte[] input = {-62, -128, -127, 101, 108};
        assertFalse(Utf8.isWellFormedSlowPath(input, 2, 5));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIsWellFormedSlowPath_errorCase_invalidIndexes() {
        // Error case with invalid indexes for slow path
        byte[] input = {-62, -128, 72};
        Utf8.isWellFormedSlowPath(input, 2, 4);
    }

}