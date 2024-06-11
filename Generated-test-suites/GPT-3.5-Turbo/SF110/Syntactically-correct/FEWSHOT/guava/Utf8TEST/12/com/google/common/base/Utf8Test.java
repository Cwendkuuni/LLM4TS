import static org.junit.Assert.*;

import org.junit.Test;

public class Utf8Test {
    @Test
    public void testEncodedLength() {
        // Test input with single character less than '\u0080'
        assertEquals(1, Utf8.encodedLength("a")); // 1-byte encoding

        // Test input with single character greater than or equal to '\u0080'
        assertEquals(2, Utf8.encodedLength("è")); // 2-byte encoding

        // Test input with multiple characters and a mixture of characters less than and greater than or equal to '\u0080'
        assertEquals(5, Utf8.encodedLength("abcè")); // 1-byte encoding + 2-byte encoding + 1-byte encoding + 2-byte encoding

        // Test input with invalid UTF-8 length
        try {
            Utf8.encodedLength("abc"); // Number of bytes exceeds the maximum value of an int
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            String expectedMessage = "UTF-8 length does not fit in int: " + (4L + 4294967296L);
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void testIsWellFormed() {
        // Test well-formed input
        byte[] bytes = new byte[] { 0b01100001 }; // 1-byte character encoding
        assertTrue(Utf8.isWellFormed(bytes));

        bytes = new byte[] { (byte) 0b11000111, (byte) 0b10111110 }; // 2-byte character encoding
        assertTrue(Utf8.isWellFormed(bytes));

        bytes = new byte[] { 0b01100001, (byte) 0b11000111, (byte) 0b10111110 }; // 1-byte encoding + 2-byte encoding
        assertTrue(Utf8.isWellFormed(bytes));

        // Test malformed input
        bytes = new byte[] { (byte) 0b10111110 }; // Start byte of a 2-byte character without a continuation byte
        assertFalse(Utf8.isWellFormed(bytes));

        bytes = new byte[] { (byte) 0b11000011 }; // Start byte of a 2-byte character without a continuation byte
        assertFalse(Utf8.isWellFormed(bytes));

        bytes = new byte[] { (byte) 0b11110000 }; // Start byte of a 4-byte character without continuation bytes
        assertFalse(Utf8.isWellFormed(bytes));

        bytes = new byte[] { (byte) 0b11000111, (byte) 0b101111101 }; // 2-byte encoding with missing continuation byte
        assertFalse(Utf8.isWellFormed(bytes));

        bytes = new byte[] { (byte) 0b111100001, (byte) 0b10111110 }; // 4-byte encoding with missing continuation byte
        assertFalse(Utf8.isWellFormed(bytes));
    }
}