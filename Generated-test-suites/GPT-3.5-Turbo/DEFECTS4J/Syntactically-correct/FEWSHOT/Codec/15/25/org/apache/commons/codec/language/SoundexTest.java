import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SoundexTest {
    
    @Test
    public void testEncode() {
        Soundex soundex = new Soundex();
        
        assertNull(soundex.encode(null));
        assertEquals("S530", soundex.encode("Smith"));
        assertEquals("W252", soundex.encode("Williams"));
        assertEquals("T522", soundex.encode("Thomas"));
        assertEquals("J500", soundex.encode("John"));
        assertEquals("J500", soundex.encode("john"));
        
        Soundex customSoundex = new Soundex("01230120022455012623010203");
        
        assertNull(customSoundex.encode(null));
        assertEquals("S530", customSoundex.encode("Smith"));
        assertEquals("W230", customSoundex.encode("Williams"));
        assertEquals("T522", customSoundex.encode("Thomas"));
        assertEquals("J500", customSoundex.encode("John"));
        assertEquals("J500", customSoundex.encode("john"));
    }
    
    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        
        assertEquals(0, soundex.difference(null, null));
        assertEquals(0, soundex.difference("", ""));
        assertEquals(4, soundex.difference("Smith", "Smythe"));
        assertEquals(4, soundex.difference("John", "john"));
        assertEquals(2, soundex.difference("Thomas", "Thomason"));
        
        Soundex customSoundex = new Soundex("01230120022455012623010203");
        
        assertEquals(0, customSoundex.difference(null, null));
        assertEquals(0, customSoundex.difference("", ""));
        assertEquals(4, customSoundex.difference("Smith", "Smythe"));
        assertEquals(0, customSoundex.difference("John", "john"));
        assertEquals(2, customSoundex.difference("Thomas", "Thomason"));
    }
    
    @Test(expected = EncoderException.class)
    public void testEncodeNonString() throws EncoderException {
        Soundex soundex = new Soundex();
        
        soundex.encode(123);
    }
}