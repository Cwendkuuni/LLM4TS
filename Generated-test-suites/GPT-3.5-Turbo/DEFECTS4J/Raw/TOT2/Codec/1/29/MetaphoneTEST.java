import org.junit.Test;
import static org.junit.Assert.*;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use case
        assertEquals("ATR", metaphone.metaphone("attract"));
        
        // Edge cases
        // Testing empty input
        assertEquals("", metaphone.metaphone(""));
        
        // Testing single character input
        assertEquals("A", metaphone.metaphone("a"));
        
        // Testing input with special characters
        assertEquals("JRS", metaphone.metaphone("jones&"));
        
        // Testing input with numbers
        assertEquals("NXS1", metaphone.metaphone("next2"));
    }

    @Test
    public void testEncode() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use case
        assertEquals("FLKS", metaphone.encode("folks"));
        
        // Edge cases
        // Testing encoding a non-string object
        try {
            metaphone.encode(123);
            fail("Expected EncoderException was not thrown");
        } catch (EncoderException e) {
            // Expected exception
        }
    }

    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use case
        assertTrue(metaphone.isMetaphoneEqual("hello", "hollow"));
        
        // Edge cases
        // Testing with non-equal metaphones
        assertFalse(metaphone.isMetaphoneEqual("test", "best"));
    }
    
}