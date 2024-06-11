package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SoundexTest {
    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        
        assertEquals(4, soundex.difference("hello", "hello"));
        assertEquals(3, soundex.difference("hello", "hella"));
        assertEquals(2, soundex.difference("hello", "halle"));
        assertEquals(1, soundex.difference("hello", "hallo"));
        assertEquals(0, soundex.difference("hello", "halal"));
    }

    @Test
    public void testEncode() throws EncoderException {
        Soundex soundex = new Soundex();
        
        assertEquals("H400", soundex.encode("hello"));
        assertEquals("W400", soundex.encode("well"));
        assertEquals("Q530", soundex.encode("quick"));
        assertEquals("B260", soundex.encode("brown"));
        assertEquals("F200", soundex.encode("fox"));
        assertEquals("J450", soundex.encode("jumps"));
        assertEquals("O163", soundex.encode("over"));
        assertEquals("T400", soundex.encode("the"));
        assertEquals("L310", soundex.encode("lazy"));
        assertEquals("D600", soundex.encode("dogs"));
    }

    @Test
    public void testSoundex() {
        Soundex soundex = new Soundex();
        
        assertEquals("H400", soundex.soundex("hello"));
        assertEquals("W400", soundex.soundex("well"));
        assertEquals("Q530", soundex.soundex("quick"));
        assertEquals("B260", soundex.soundex("brown"));
        assertEquals("F200", soundex.soundex("fox"));
        assertEquals("J450", soundex.soundex("jumps"));
        assertEquals("O163", soundex.soundex("over"));
        assertEquals("T400", soundex.soundex("the"));
        assertEquals("L310", soundex.soundex("lazy"));
        assertEquals("D600", soundex.soundex("dogs"));
    }
}