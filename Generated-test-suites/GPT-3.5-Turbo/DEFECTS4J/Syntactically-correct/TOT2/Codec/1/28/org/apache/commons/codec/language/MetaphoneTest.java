import static org.junit.Assert.*;
import org.junit.Test;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use case
        assertEquals("MTFN", metaphone.metaphone("Metaphone"));
        
        // Edge case with empty string
        assertEquals("", metaphone.metaphone(""));
        
        // Edge case with single character
        assertEquals("A", metaphone.metaphone("a"));
    }

    @Test
    public void testEncode() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use case
        assertEquals("MTFN", metaphone.encode("Metaphone"));
        
        // Error scenario with non-string object
        try {
            metaphone.encode(123); // Expect EncoderException
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            // Expected exception
        }
    }

    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use case
        assertTrue(metaphone.isMetaphoneEqual("Metaphone", "Mtfn"));
        
        // Edge case with different strings
        assertFalse(metaphone.isMetaphoneEqual("Apache", "Metaphone"));
        
        // Edge case with empty strings
        assertTrue(metaphone.isMetaphoneEqual("", ""));
    }
}