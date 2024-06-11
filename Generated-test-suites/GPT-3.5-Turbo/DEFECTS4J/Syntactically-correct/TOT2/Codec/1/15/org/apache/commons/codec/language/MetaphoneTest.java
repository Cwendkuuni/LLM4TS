import static org.junit.Assert.*;
import org.junit.Test;

public class MetaphoneTest {
  
    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use cases
        assertEquals("HT", metaphone.metaphone("hat"));
        assertEquals("FL", metaphone.metaphone("flame"));
        
        // Edge cases
        assertEquals("", metaphone.metaphone(""));
        assertEquals("A", metaphone.metaphone("a"));
        
        // Error scenarios
        try {
            metaphone.metaphone(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
  
    @Test
    public void testEncode() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use case
        assertEquals("HT", metaphone.encode("hat"));
        
        // Edge case
        assertEquals("F", metaphone.encode("flame"));
        
        // Error scenario
        try {
            metaphone.encode(123);
            fail("Expected EncoderException");
        } catch (EncoderException e) {
            // Expected
        }
    }
  
    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use case
        assertTrue(metaphone.isMetaphoneEqual("hat", "hat"));
        
        // Edge case
        assertFalse(metaphone.isMetaphoneEqual("hat", "hit"));
        
        // Error scenario
        assertFalse(metaphone.isMetaphoneEqual(null, "hit"));
    }
  
    @Test
    public void testGetMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        assertEquals(4, metaphone.getMaxCodeLen());
    }
  
    @Test
    public void testSetMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        metaphone.setMaxCodeLen(3);
        assertEquals(3, metaphone.getMaxCodeLen());
    }
  
}