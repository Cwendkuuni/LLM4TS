
import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        // Test case for basic functionality
        assertEquals(6, Utf8.encodedLength("Hello!"));

        // Test case for edge case - empty string
        assertEquals(0, Utf8.encodedLength(""));

        // Test case for edge case - string with one character
        assertEquals(1, Utf8.encodedLength("a"));

        // Test case for edge case - string with Unicode characters
        assertEquals(6, Utf8.encodedLength("नमस्ते"));
    }

    @Test
    public void testIsWellFormed() {
        // Test case for basic functionality
        byte[] bytes = {72, 101, 108, 108, 111, 33};
        assertTrue(Utf8.isWellFormed(bytes));

        // Test case for edge case - empty byte array
        assertTrue(Utf8.isWellFormed(new byte[0]));

        // Test case for edge case - byte array with one byte
        byte[] singleByte = {65};
        assertTrue(Utf8.isWellFormed(singleByte));

        // Test case for edge case - invalid byte array
        byte[] invalidBytes = {72, -50, 101, 108, 108, 111, 33};
        assertFalse(Utf8.isWellFormed(invalidBytes));
    }

    @Test
    public void testIsWellFormedWithOffsetAndLength() {
        // Test case for basic functionality
        byte[] bytes = {72, 101, 108, 108, 111, 33};
        assertTrue(Utf8.isWellFormed(bytes, 0, bytes.length));

        // Test case for edge case - empty byte array
        assertTrue(Utf8.isWellFormed(new byte[0], 0, 0));

        // Test case for edge case - byte array with one byte
        byte[] singleByte = {65};
        assertTrue(Utf8.isWellFormed(singleByte, 0, 1));

        // Test case for edge case - invalid byte array
        byte[] invalidBytes = {72, -50, 101, 108, 108, 111, 33};
        assertFalse(Utf8.isWellFormed(invalidBytes, 0, invalidBytes.length));
    }
}
