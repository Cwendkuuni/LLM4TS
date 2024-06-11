import org.junit.Test;
import static org.junit.Assert.*;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use case
        assertEquals("FNS", metaphone.metaphone("Phonetics"));
        
        // Edge cases
        assertEquals("", metaphone.metaphone(""));
        assertEquals("A", metaphone.metaphone("A"));
        assertEquals("F", metaphone.metaphone("F"));
        
        // Error scenarios
        try {
            metaphone.metaphone(null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            // Expected NullPointerException
        }
    }

    @Test
    public void testEncode() throws EncoderException {
        Metaphone metaphone = new Metaphone();
        
        // Typical use case
        assertEquals("KNSNK", metaphone.encode("Consonant"));
        
        // Edge cases
        try {
            metaphone.encode(null);
            fail("Expected EncoderException to be thrown");
        } catch (EncoderException ex) {
            // Expected EncoderException
        }
        
        try {
            metaphone.encode(123);
            fail("Expected EncoderException to be thrown");
        } catch (EncoderException ex) {
            // Expected EncoderException
        }
    }

    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        
        // Typical use case
        assertTrue(metaphone.isMetaphoneEqual("Choir", "Quire"));
        
        // Edge cases
        assertFalse(metaphone.isMetaphoneEqual("Metaphone", "Soundex"));
        assertFalse(metaphone.isMetaphoneEqual("", "A"));
        assertFalse(metaphone.isMetaphoneEqual("E", ""));
        
        // Error scenarios
        assertFalse(metaphone.isMetaphoneEqual(null, "String"));
        assertFalse(metaphone.isMetaphoneEqual("String", null));
        assertFalse(metaphone.isMetaphoneEqual(null, null));
    }
}