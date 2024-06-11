import org.junit.Test;
import static org.junit.Assert.*;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use case
        assertEquals("X", metaphone.metaphone("example"));
        
        // Edge case: empty string
        assertEquals("", metaphone.metaphone(""));
        
        // Edge case: single character
        assertEquals("A", metaphone.metaphone("a"));
        
        // Error scenario: null input
        assertNull(metaphone.metaphone(null));
    }

    @Test
    public void testEncode() {
        Metaphone metaphone = new Metaphone();
        
        try {
            // Typical use case
            assertEquals("X", metaphone.encode("example"));
        } catch (EncoderException e) {
            fail("Should not throw exception for valid input.");
        }
        
        // Error scenario: encoding non-string object
        try {
            metaphone.encode(123); // should throw EncoderException
            fail("Should throw EncoderException for non-string input.");
        } catch (EncoderException e) {
            // Exception expected
        }
    }

    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use case
        assertTrue(metaphone.isMetaphoneEqual("example", "eggsample"));
        
        // Edge case: equal strings
        assertTrue(metaphone.isMetaphoneEqual("hello", "hello"));
        
        // Error scenario: different metaphones
        assertFalse(metaphone.isMetaphoneEqual("example", "notexample"));
    }

}