
package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SoundexTest {
    @Test
    public void testEncode() {
        Soundex soundex = new Soundex();
        
        // Test cases for Soundex.encode(String str)
        assertEquals("S530", soundex.encode("Smith"));
        assertEquals("A260", soundex.encode("Adams"));
        assertNull(soundex.encode(null));
    }
    
    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        
        // Test cases for Soundex.difference(String s1, String s2)
        assertEquals(4, soundex.difference("Smith", "Smith"));
        assertEquals(0, soundex.difference("Smith", "Adams"));
        assertEquals(1, soundex.difference("Smith", "Smyth"));
        assertEquals(2, soundex.difference("Smith", "Smithe"));
    }
    
    @Test
    public void testSoundex() {
        Soundex soundex = new Soundex();
        
        // Test cases for Soundex.soundex(String str)
        assertEquals("S530", soundex.soundex("Smith"));
        assertEquals("A260", soundex.soundex("Adams"));
        assertNull(soundex.soundex(null));
    }
}
