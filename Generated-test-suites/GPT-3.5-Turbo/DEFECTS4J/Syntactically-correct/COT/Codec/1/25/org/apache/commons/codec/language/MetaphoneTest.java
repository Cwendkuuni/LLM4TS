import static org.junit.Assert.*;
import org.junit.Test;

public class MetaphoneTest {

    Metaphone metaphone = new Metaphone();

    @Test
    public void testMetaphone() {
        // Basic test case with input "hello"
        assertEquals("HL", metaphone.metaphone("hello"));
    }

    @Test
    public void testEncode() throws EncoderException {
        // Basic test case with input "world"
        assertEquals("WRLT", metaphone.encode("world"));
    }

    @Test
    public void testEncodeWithNonStringInput() {
        // Test case with non-string input, expect EncoderException to be thrown
        assertThrows(EncoderException.class, () -> {
            metaphone.encode(123);
        });
    }

    @Test
    public void testIsMetaphoneEqual() {
        // Test case with two different strings, expect false to be returned
        assertEquals(false, metaphone.isMetaphoneEqual("hello", "world"));
    }

    @Test
    public void testIsMetaphoneEqualWithIdenticalStrings() {
        // Test case with two identical strings, expect true to be returned
        assertEquals(true, metaphone.isMetaphoneEqual("hello", "hello"));
    }

    @Test
    public void testIsMetaphoneEqualWithDifferentCasing() {
        // Test case with two strings with different casing, expect true to be returned
        assertEquals(true, metaphone.isMetaphoneEqual("HELLO", "hello"));
    }

    // Additional test cases for edge cases and exception handling scenarios

    @Test
    public void testMetaphoneWithEmptyString() {
        // Test case with empty string as input, expect empty string to be returned
        assertEquals("", metaphone.metaphone(""));
    }

    @Test
    public void testMetaphoneWithSingleCharacter() {
        // Test case with single character as input, expect the same character to be returned in uppercase
        assertEquals("A", metaphone.metaphone("a"));
        assertEquals("B", metaphone.metaphone("b"));
        assertEquals("C", metaphone.metaphone("c"));
        // ... add more test cases for other characters
    }

    @Test
    public void testMetaphoneWithNonAsciiCharacters() {
        // Test case with non-ASCII characters as input, expect metaphone code to be generated correctly
        assertEquals("HL", metaphone.metaphone("hèllo"));
    }

    @Test
    public void testEncodeWithMaxCodeLen() {
        // Test case with input longer than maxCodeLen, expect only maxCodeLen number of characters to be encoded
        metaphone.setMaxCodeLen(2);
        assertEquals("WR", metaphone.encode("world"));
    }

    @Test
    public void testSetMaxCodeLenWithNegativeValue() {
        // Test case with negative value as maxCodeLen, expect IllegalArgumentException to be thrown
        assertThrows(IllegalArgumentException.class, () -> {
            metaphone.setMaxCodeLen(-1);
        });
    }

}