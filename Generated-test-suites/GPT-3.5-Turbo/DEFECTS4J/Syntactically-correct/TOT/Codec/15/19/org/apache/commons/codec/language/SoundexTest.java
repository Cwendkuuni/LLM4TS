import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {
    
    @Test
    public void testSoundex() {
        Soundex soundex = new Soundex();
        
        // Typical use case
        assertEquals("S532", soundex.soundex("Soundex"));
        
        // Edge cases
        assertEquals("A000", soundex.soundex("A"));
        assertEquals("A000", soundex.soundex("AA"));
        
        // Error scenarios
        assertNull(soundex.soundex(null));
    }
    
    @Test
    public void testEncode_Object() throws EncoderException {
        Soundex soundex = new Soundex();
        
        // Typical use case
        assertEquals("K500", soundex.encode("Codec"));
        
        // Error scenarios
        try {
            soundex.encode(123);
            fail("Should throw EncoderException");
        } catch (EncoderException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testEncode_String() {
        Soundex soundex = new Soundex();
        
        // Typical use case
        assertEquals("S532", soundex.encode("Soundex"));
        
        // Error scenarios
        assertNull(soundex.encode(null));
    }
    
    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        
        // Typical use case
        assertEquals(4, soundex.difference("Soundex", "Soundex"));
        
        // Error scenarios
        try {
            soundex.difference("Codec", "Soundex");
            fail("Should throw EncoderException");
        } catch (EncoderException e) {
            // Expected exception
        }
    }
}