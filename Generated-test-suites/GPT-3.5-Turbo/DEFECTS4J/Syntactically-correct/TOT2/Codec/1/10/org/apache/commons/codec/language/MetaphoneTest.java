import static org.junit.Assert.*;
import org.junit.Test;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use case
        String result = metaphone.metaphone("hello");
        assertEquals("HLO", result);
        
        // Edge case: Empty input
        result = metaphone.metaphone("");
        assertEquals("", result);
        
        // Edge case: Single character input
        result = metaphone.metaphone("a");
        assertEquals("A", result);
        
        // Edge case: Special character input
        result = metaphone.metaphone("!@#$");
        assertEquals("", result);
        
        // Error scenario: Input contains numbers
        try {
            metaphone.metaphone("hello123");
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            // Expected exception
        }
    }

    @Test
    public void testEncode() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use case
        String result = (String)metaphone.encode("hello");
        assertEquals("HLO", result);
        
        // Edge case: Empty input
        result = (String)metaphone.encode("");
        assertEquals("", result);
        
        // Edge case: Single character input
        result = (String)metaphone.encode("a");
        assertEquals("A", result);
        
        // Error scenario: Input is not a String
        try {
            metaphone.encode(123);
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            // Expected exception
        }
    }

    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use case
        boolean result = metaphone.isMetaphoneEqual("hello", "hollow");
        assertTrue(result);
        
        // Edge case: Different metaphones
        result = metaphone.isMetaphoneEqual("hello", "world");
        assertFalse(result);
        
        // Edge case: Empty strings
        result = metaphone.isMetaphoneEqual("", "");
        assertTrue(result);
    }

    @Test
    public void testMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use case
        assertEquals(4, metaphone.getMaxCodeLen());
        
        // Set maxCodeLen and check
        metaphone.setMaxCodeLen(6);
        assertEquals(6, metaphone.getMaxCodeLen());
    }
}