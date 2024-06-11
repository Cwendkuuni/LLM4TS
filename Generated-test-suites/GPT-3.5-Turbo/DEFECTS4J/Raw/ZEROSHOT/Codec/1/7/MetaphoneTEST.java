import org.junit.Test;
import static org.junit.Assert.*;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();
        
        // Test for null input
        assertEquals("", metaphone.metaphone(null));
        
        // Test for empty input
        assertEquals("", metaphone.metaphone(""));
        
        // Test for single character input
        assertEquals("A", metaphone.metaphone("A"));
        assertEquals("Z", metaphone.metaphone("Z"));
        
        // Test for initial 2 characters exceptions
        assertEquals("N", metaphone.metaphone("KN"));
        assertEquals("E", metaphone.metaphone("AE"));
        assertEquals("W", metaphone.metaphone("WR"));
        assertEquals("", metaphone.metaphone("X"));
        
        // Test for other characters
        assertEquals("S", metaphone.metaphone("C"));
        assertEquals("K", metaphone.metaphone("K"));
        assertEquals("F", metaphone.metaphone("PH"));
        assertEquals("K", metaphone.metaphone("Q"));
        assertEquals("X", metaphone.metaphone("CH"));
        assertEquals("T", metaphone.metaphone("TH"));
        assertEquals("F", metaphone.metaphone("V"));
        assertEquals("F", metaphone.metaphone("W"));
        assertEquals("S", metaphone.metaphone("Z"));
        
        // Test for words
        assertEquals("MTFN", metaphone.metaphone("Metaphone"));
        assertEquals("JN", metaphone.metaphone("John"));
        assertEquals("L", metaphone.metaphone("Larry"));
        assertEquals("M", metaphone.metaphone("Mary"));
        assertEquals("RNLT", metaphone.metaphone("Reynolds"));
    }

    @Test
    public void testEncode() {
        Metaphone metaphone = new Metaphone();
        
        // Test for null input
        try {
            metaphone.encode(null);
            fail("Expected EncoderException to be thrown");
        } catch (EncoderException e) {
            // Expected exception
        }
        
        // Test for non-null input
        assertEquals("", metaphone.encode(""));
        assertEquals("MTFN", metaphone.encode("Metaphone"));
        assertEquals("JN", metaphone.encode("John"));
        assertEquals("L", metaphone.encode("Larry"));
        assertEquals("M", metaphone.encode("Mary"));
        assertEquals("RNLT", metaphone.encode("Reynolds"));
    }

    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        
        // Test for equal metaphones
        assertTrue(metaphone.isMetaphoneEqual("John", "John"));
        assertTrue(metaphone.isMetaphoneEqual("Mary", "Mary"));
        assertTrue(metaphone.isMetaphoneEqual("Alice", "Ellis"));
        
        // Test for non-equal metaphones
        assertFalse(metaphone.isMetaphoneEqual("John", "Jane"));
        assertFalse(metaphone.isMetaphoneEqual("Mary", "Mark"));
        assertFalse(metaphone.isMetaphoneEqual("Alice", "Bob"));
    }

    @Test
    public void testMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        
        // Test default maxCodeLen
        assertEquals(4, metaphone.getMaxCodeLen());
        
        // Test set maxCodeLen
        metaphone.setMaxCodeLen(3);
        assertEquals(3, metaphone.getMaxCodeLen());
    }

}