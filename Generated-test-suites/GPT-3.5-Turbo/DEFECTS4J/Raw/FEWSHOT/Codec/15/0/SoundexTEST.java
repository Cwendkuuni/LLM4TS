package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SoundexTest {
    @Test
    public void testEncode() {
        Soundex soundex = new Soundex();
        
        assertEquals("S530", soundex.encode("Smith"));
        assertEquals("T522", soundex.encode("Turing"));
        assertEquals("A261", soundex.encode("Adams"));
        assertEquals("A261", soundex.encode("adams"));
        assertEquals("D250", soundex.encode("Doe"));
        assertEquals("D251", soundex.encode("doe"));
        assertEquals("O425", soundex.encode("O'Brien"));
        assertNull(soundex.encode(null));
    }

    @Test
    public void testDifference() {
        Soundex soundex = new Soundex();
        
        assertEquals(0, soundex.difference("Smith", "Smith"));
        assertEquals(3, soundex.difference("Smith", "Smyth"));
        assertEquals(2, soundex.difference("Turing", "Turinge"));
        assertEquals(1, soundex.difference("Adams", "Addams"));
        assertEquals(2, soundex.difference("Doe", "Dode"));
        assertEquals(3, soundex.difference("O'Brien", "O'Bryant"));
        assertEquals(0, soundex.difference(null, null));
    }
}