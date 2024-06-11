import static org.junit.Assert.*;
import org.junit.Test;

public class MetaphoneTest {
    
    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use case
        assertEquals("MTFN", metaphone.metaphone("Metaphone"));
        
        // Edge case: Empty input
        assertEquals("", metaphone.metaphone(""));
        
        // Edge case: Single character input
        assertEquals("A", metaphone.metaphone("a"));
    }
    
    @Test
    public void testEncodeObject() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use case
        assertEquals("MTFN", metaphone.encode("Metaphone"));
        
        // Error scenario: Non-string input
        try {
            metaphone.encode(123); // Should throw EncoderException
            fail("Expected EncoderException was not thrown");
        } catch (EncoderException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use case where metaphones are equal
        assertTrue(metaphone.isMetaphoneEqual("Hello", "Hello"));
        
        // Typical use case where metaphones are not equal
        assertFalse(metaphone.isMetaphoneEqual("World", "Warld"));
    }
    
    @Test
    public void testMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        
        // Setter and getter test
        metaphone.setMaxCodeLen(3);
        assertEquals(3, metaphone.getMaxCodeLen());
    }
}