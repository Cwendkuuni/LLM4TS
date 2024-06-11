package org.apache.commons.codec.language;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;

public class SoundexTest {

    @Test
    public void testSoundex() {
        Soundex soundex = new Soundex();
        assertEquals("A123", soundex.soundex("Apple"));
        assertEquals("B423", soundex.soundex("Banana"));
        assertEquals("A100", soundex.soundex("Almond"));
        assertEquals("E111", soundex.soundex("Elephant"));
        assertEquals("C230", soundex.soundex("Coconut"));
        assertEquals("S222", soundex.soundex("Sunflower"));
        assertEquals("O000", soundex.soundex(""));
        assertEquals("K000", soundex.soundex(null));

        Soundex soundexWithCustomMapping = new Soundex(new char[]{'1', '1', '2', '2', '3', '3', '4', '4', '5', '5'});
        assertEquals("A111", soundexWithCustomMapping.soundex("Apple"));
        assertEquals("B111", soundexWithCustomMapping.soundex("Banana"));
        assertEquals("A111", soundexWithCustomMapping.soundex("Almond"));
        assertEquals("E111", soundexWithCustomMapping.soundex("Elephant"));
        assertEquals("C111", soundexWithCustomMapping.soundex("Coconut"));
        assertEquals("S111", soundexWithCustomMapping.soundex("Sunflower"));
        assertEquals("O111", soundexWithCustomMapping.soundex(""));
        assertNull(soundexWithCustomMapping.soundex(null));
    }

    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals(3, soundex.difference("Apple", "Apple"));
        assertEquals(2, soundex.difference("Apple", "Banana"));
        assertEquals(3, soundex.difference("Apple", "Almond"));
        assertEquals(3, soundex.difference("Apple", "Elephant"));
        assertEquals(3, soundex.difference("Apple", "Coconut"));
        assertEquals(3, soundex.difference("Apple", "Sunflower"));
        assertEquals(0, soundex.difference("Apple", ""));
        assertEquals(0, soundex.difference(null, "Apple"));

        assertEquals(4, soundex.difference("Apple", "Apple"));
        assertEquals(0, soundex.difference("Apple", "Apple"));

        Soundex soundexWithCustomMapping = new Soundex(new char[]{'1', '1', '2', '2', '3', '3', '4', '4', '5', '5'});
        assertEquals(3, soundexWithCustomMapping.difference("Apple", "Apple"));
        assertEquals(2, soundexWithCustomMapping.difference("Apple", "Banana"));
        assertEquals(3, soundexWithCustomMapping.difference("Apple", "Almond"));
        assertEquals(3, soundexWithCustomMapping.difference("Apple", "Elephant"));
        assertEquals(3, soundexWithCustomMapping.difference("Apple", "Coconut"));
        assertEquals(3, soundexWithCustomMapping.difference("Apple", "Sunflower"));
        assertEquals(0, soundexWithCustomMapping.difference("Apple", ""));
        assertNull(soundexWithCustomMapping.difference(null, "Apple"));

        assertEquals(4, soundexWithCustomMapping.difference("Apple", "Apple"));
        assertEquals(0, soundexWithCustomMapping.difference("Apple", "Apple"));
    }
}