import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        // Test basic functionality
        CharSequence sequence = "Hello";
        int result = Utf8.encodedLength(sequence);
        assertEquals(5, result);

        // Test edge case where sequence is empty
        sequence = "";
        result = Utf8.encodedLength(sequence);
        assertEquals(0, result);

        // Test edge case where sequence contains a single character
        sequence = "A";
        result = Utf8.encodedLength(sequence);
        assertEquals(1, result);

        // Test exception handling scenario where utf8Length is larger than utf16Length
        // This should throw an IllegalArgumentException
        sequence = "😀"; // 1 emoji character, but requires 4 bytes in UTF-8 encoding
        try {
            result = Utf8.encodedLength(sequence);
            fail("Expected IllegalArgumentException was not thrown.");
        } catch (IllegalArgumentException e) {
            // Exception was expected
        }
    }
}