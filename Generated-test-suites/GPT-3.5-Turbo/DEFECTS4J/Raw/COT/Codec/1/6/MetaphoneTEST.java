
import static org.junit.Assert.*;
import org.junit.Test;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();

        // Basic functionality test
        assertEquals("QMN", metaphone.metaphone("common"));
        
        // Test cases for potential edge cases and exception handling scenarios
        assertNull(metaphone.metaphone(null)); // Null input
        assertEquals("", metaphone.metaphone("")); // Empty string
        
        // Test cases for single character input
        assertEquals("A", metaphone.metaphone("a")); // Lowercase vowel
        assertEquals("A", metaphone.metaphone("A")); // Uppercase vowel
        assertEquals("N", metaphone.metaphone("n")); // Lowercase consonant
        assertEquals("N", metaphone.metaphone("N")); // Uppercase consonant
        
        // Test cases for words with single character
        assertEquals("F", metaphone.metaphone("f")); // Single letter word starting with F
        assertEquals("T", metaphone.metaphone("t")); // Single letter word starting with T
        
        // Test cases for words with special characters
        assertEquals("FS", metaphone.metaphone("f$s")); // Word with special characters
        assertEquals("MLS", metaphone.metaphone("m$l$s")); // Word with multiple special characters
        
        // Test cases for large words
        assertEquals("TRST", metaphone.metaphone("transportation")); // Large word with multiple syllables
        
    }
}

