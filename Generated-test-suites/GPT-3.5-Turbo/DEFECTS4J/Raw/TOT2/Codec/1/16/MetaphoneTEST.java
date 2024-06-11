import static org.junit.Assert.*;
import org.junit.Test;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();
        
        // Typical Use Case
        String result = metaphone.metaphone("hello");
        assertEquals("HL", result);
        
        // Edge Case 1: Empty String
        result = metaphone.metaphone("");
        assertEquals("", result);
        
        // Edge Case 2: Single Character word
        result = metaphone.metaphone("a");
        assertEquals("A", result);
        
        // Edge Case 3: Special Characters
        result = metaphone.metaphone("!123");
        assertEquals("", result);
        
        // Error Scenario: Null input
        result = metaphone.metaphone(null);
        assertEquals("", result);
    }

    @Test
    public void testEncode() {
        Metaphone metaphone = new Metaphone();
        
        // Typical Use Case
        Object result = metaphone.encode("world");
        assertEquals("WRLD", result);
        
        // Edge Case: Encoding with non-string object
        try {
            result = metaphone.encode(123);
            fail("Expected EncoderException was not thrown");
        } catch (EncoderException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        
        // Typical Use Case
        boolean result = metaphone.isMetaphoneEqual("hello", "helo");
        assertTrue(result);
        
        // Edge Case: Different input strings
        result = metaphone.isMetaphoneEqual("world", "hello");
        assertFalse(result);
    }
}