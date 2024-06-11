import static org.junit.Assert.*;
import org.junit.Test;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use case
        assertEquals("NTN", metaphone.metaphone("Nation"));
        
        // Edge cases
        assertEquals("", metaphone.metaphone(""));
        assertEquals("X", metaphone.metaphone("X"));
    }

    @Test
    public void testEncodeObject() {
        Metaphone metaphone = new Metaphone();
        
        try {
            // Typical use case
            assertEquals("NTN", metaphone.encode("Nation"));
        } catch(EncoderException e) {
            fail("Unexpected EncoderException");
        }
        
        // Error scenario
        try {
            // Passing an object that is not a String
            metaphone.encode(new Object());
            fail("Expected EncoderException was not thrown");
        } catch(EncoderException e) {
            // Expected behavior
        }
    }

    @Test
    public void testEncodeString() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use case
        assertEquals("NTN", metaphone.encode("Nation"));
    }

    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use cases
        assertTrue(metaphone.isMetaphoneEqual("Nation", "Nations"));
        assertFalse(metaphone.isMetaphoneEqual("Hello", "World"));
    }

    @Test
    public void testGetMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use case
        assertEquals(4, metaphone.getMaxCodeLen());
    }

    @Test
    public void testSetMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use case
        metaphone.setMaxCodeLen(5);
        assertEquals(5, metaphone.getMaxCodeLen());
    }

}