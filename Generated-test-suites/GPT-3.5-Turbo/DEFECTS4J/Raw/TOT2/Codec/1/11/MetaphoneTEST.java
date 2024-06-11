import static org.junit.Assert.*;
import org.junit.Test;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use case
        assertEquals("FKS", metaphone.metaphone("fox"));
        
        // Edge case: Empty string
        assertEquals("", metaphone.metaphone(""));
        
        // Edge case: Single character
        assertEquals("A", metaphone.metaphone("a"));
        
        // Error scenario: Null input
        assertEquals("", metaphone.metaphone(null));
    }

    @Test
    public void testEncode() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use case
        assertEquals("WMT", metaphone.encode("winter"));
        
        // Edge case: Empty string
        assertEquals("", metaphone.encode(""));
        
        // Error scenario: Non-string input
        try {
            metaphone.encode(123);
            fail("Exception not thrown for non-string input");
        } catch (EncoderException e) {
            // Exception expected
        }
    }

    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use case
        assertTrue(metaphone.isMetaphoneEqual("car", "kar"));
        
        // Edge case: Same word
        assertTrue(metaphone.isMetaphoneEqual("hello", "hello"));
        
        // Error scenario: Different words
        assertFalse(metaphone.isMetaphoneEqual("cat", "dog"));
    }
}