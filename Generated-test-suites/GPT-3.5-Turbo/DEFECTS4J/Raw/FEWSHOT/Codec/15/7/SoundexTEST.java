import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SoundexTest {
    
    @Test
    public void testEncode() {
        Soundex soundex = new Soundex();
        assertEquals("S530", soundex.encode("Soundex"));
        assertEquals("A460", soundex.encode("Apache"));
        assertEquals("US_ENGLISH_MAPPING_STRING", soundex.encode("US_ENGLISH_MAPPING_STRING"));
    }
    
    @Test
    public void testDifference() {
        Soundex soundex = new Soundex();
        try {
            assertEquals(4, soundex.difference("Soundex", "S530"));
            assertEquals(2, soundex.difference("Apache", "A460"));
            assertEquals(0, soundex.difference("US_ENGLISH_MAPPING_STRING", "NOT_MAPPED"));
        } catch (EncoderException e) {
            // Do nothing
        }
    }
    
    @Test
    public void testSoundex() {
        Soundex soundex = new Soundex();
        
        assertEquals("S530", soundex.soundex("Soundex"));
        assertEquals("A460", soundex.soundex("Apache"));
        assertEquals("US_ENGLISH_MAPPING_STRING", soundex.soundex("US_ENGLISH_MAPPING_STRING"));
    }
    
    @Test
    public void testEmptyString() {
        Soundex soundex = new Soundex();
        
        assertNull(soundex.encode(null));
        assertEquals("", soundex.encode(""));
    }
    
    @Test(expected = EncoderException.class)
    public void testEncodeNonStringObject() throws EncoderException {
        Soundex soundex = new Soundex();
        soundex.encode(123);
    }
}