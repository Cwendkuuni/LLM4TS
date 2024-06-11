import org.junit.Test;
import static org.junit.Assert.*;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use case
        assertEquals("AN", metaphone.metaphone("Anne"));
        
        // Edge cases
        assertEquals("", metaphone.metaphone(null));
        assertEquals("A", metaphone.metaphone("A"));
        
        // Error scenario
        assertEquals("T", metaphone.metaphone("Tomato123")); // Non-alphabetic characters should be ignored
    }
    
    @Test
    public void testEncode() throws EncoderException {
        Metaphone metaphone = new Metaphone();
        
        // Typical use case
        assertEquals("AN", metaphone.encode("Anne"));
        
        // Edge cases
        assertEquals("", metaphone.encode(""));
        
        // Error scenario
        assertThrows(EncoderException.class, () -> {
            metaphone.encode(123); // Non-string input should throw EncoderException
        });
    }
    
    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use case
        assertTrue(metaphone.isMetaphoneEqual("John", "Jon"));

        // Edge cases
        assertTrue(metaphone.isMetaphoneEqual("", ""));
        assertFalse(metaphone.isMetaphoneEqual("Hello", "World"));
    }
}