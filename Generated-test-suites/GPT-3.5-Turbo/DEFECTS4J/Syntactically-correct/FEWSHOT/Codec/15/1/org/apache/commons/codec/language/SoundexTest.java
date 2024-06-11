import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SoundexTest {
    @Test
    public void testEncode() {
        Soundex soundex = new Soundex();
        assertEquals("S530", soundex.encode("Smith"));
        assertEquals("T522", soundex.encode("Taylor"));
        assertEquals("W232", soundex.encode("Wilson"));
        assertEquals("B620", soundex.encode("Brown"));
        assertNull(soundex.encode(null));
    }
    
    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals(4, soundex.difference("Smith", "Smyth"));
        assertEquals(3, soundex.difference("Brown", "Braun"));
        assertEquals(2, soundex.difference("Taylor", "Tyler"));
        assertEquals(1, soundex.difference("Wilson", "Willson"));
        assertEquals(0, soundex.difference("Jones", "Johnson"));
        
        assertNull(soundex.difference(null, "Smyth"));
        assertNull(soundex.difference("Smith", null));
    }
}