
package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexTest {

    @Test
    public void testSoundex() {
        Soundex soundex = new Soundex();
        assertEquals("R163", soundex.soundex("Robert"));
        assertEquals("T522", soundex.soundex("Thomas"));
        assertEquals("A165", soundex.soundex("Andrew"));
        assertEquals("S531", soundex.soundex("Stephen"));
        assertEquals("W523", soundex.soundex("William"));
    }

    @Test
    public void testEncodeObject() {
        Soundex soundex = new Soundex();
        assertEquals("R163", soundex.encode("Robert"));
        assertEquals("T522", soundex.encode("Thomas"));
        assertEquals("A165", soundex.encode("Andrew"));
        assertEquals("S531", soundex.encode("Stephen"));
        assertEquals("W523", soundex.encode("William"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodeObjectInvalidType() {
        Soundex soundex = new Soundex();
        soundex.encode(12345);
    }

    @Test
    public void testEncodeString() {
        Soundex soundex = new Soundex();
        assertEquals("R163", soundex.encode("Robert"));
        assertEquals("T522", soundex.encode("Thomas"));
        assertEquals("A165", soundex.encode("Andrew"));
        assertEquals("S531", soundex.encode("Stephen"));
        assertEquals("W523", soundex.encode("William"));
    }

    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals(4, soundex.difference("Robert", "Rupert"));
        assertEquals(3, soundex.difference("Thomas", "Thompson"));
        assertEquals(1, soundex.difference("Andrew", "Andrea"));
        assertEquals(2, soundex.difference("Stephen", "Steven"));
        assertEquals(3, soundex.difference("William", "Wilson"));
    }

}
