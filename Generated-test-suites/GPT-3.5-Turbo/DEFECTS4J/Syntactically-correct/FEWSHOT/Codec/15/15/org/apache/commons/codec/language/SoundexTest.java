package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SoundexTest {
    @Test
    public void testEncode_Object() throws Exception {
        Soundex soundex = new Soundex();
        assertEquals("K530", soundex.encode("K"));
        assertNull(soundex.encode(null));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testMap_IllegalArgumentException() throws Exception {
        Soundex soundex = new Soundex();
        soundex.map('Z');
    }
    
    @Test
    public void testSoundex() throws Exception {
        Soundex soundex = new Soundex();
        assertEquals("K530", soundex.soundex("K"));
        assertNull(soundex.soundex(null));
    }
    
    @Test
    public void testDifference() throws Exception {
        Soundex soundex = new Soundex();
        String s1 = "Hello";
        String s2 = "Holla";
        assertEquals(3, soundex.difference(s1, s2));
    }
}