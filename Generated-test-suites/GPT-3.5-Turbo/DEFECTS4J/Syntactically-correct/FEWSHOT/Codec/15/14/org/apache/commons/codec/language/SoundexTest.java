import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SoundexTest {
    @Test
    public void testEncode() {
        Soundex soundex = new Soundex();
        
        // Test cases with a single word
        assertEquals("S530", soundex.encode("Soundex"));
        assertEquals("C450", soundex.encode("Codec"));
        assertEquals("L525", soundex.encode("Language"));
        assertEquals("U536", soundex.encode("US_ENGLISH"));
        
        // Test cases with multiple words
        assertEquals("S530", soundex.encode("Soundex Codec"));
        assertEquals("L525", soundex.encode("Language US_ENGLISH"));
        
        // Test empty string
        assertEquals("", soundex.encode(""));
        
        // Test null string
        assertNull(soundex.encode(null));
    }
    
    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        
        // Test cases with a single word
        assertEquals(4, soundex.difference("Soundex", "soundex"));
        assertEquals(1, soundex.difference("Codec", "CodeC"));
        assertEquals(0, soundex.difference("Language", "Language"));
        assertEquals(3, soundex.difference("US_ENGLISH", "us_ENGLISH"));
        
        // Test cases with multiple words
        assertEquals(4, soundex.difference("Soundex Codec", "soundex codec"));
        assertEquals(0, soundex.difference("Language US_ENGLISH", "Language US_ENGLISH"));
        
        // Test empty string
        assertEquals(0, soundex.difference("", ""));
        
        // Test null string
        assertEquals(0, soundex.difference(null, null));
    }
}