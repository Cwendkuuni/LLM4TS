import static org.junit.Assert.*;
import org.junit.Test;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use case
        assertEquals("AN", metaphone.metaphone("Anne"));
        
        // Edge cases
        assertEquals("", metaphone.metaphone("")); // Empty string
        assertEquals("A", metaphone.metaphone("A")); // Single character
        assertEquals("X", metaphone.metaphone("X")); // Initial X becomes S
    }

    @Test
    public void testEncodeObject() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use case
        try {
            Object encodedObject = metaphone.encode("Test");
            assertEquals("TS", encodedObject);
        } catch (EncoderException e) {
            fail("Should not throw exception");
        }
        
        // Error scenario - Invalid input type
        try {
            metaphone.encode(123);
            fail("Should throw EncoderException");
        } catch (EncoderException e) {
            // Expected
        }
    }

    @Test
    public void testEncodeString() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use case
        assertEquals("TS", metaphone.encode("Test"));
    }

    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use cases
        assertTrue(metaphone.isMetaphoneEqual("Hello", "Halo"));
        assertFalse(metaphone.isMetaphoneEqual("World", "Waldo"));
    }

}