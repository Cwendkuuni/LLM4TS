package org.apache.commons.codec.language;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;

public class SoundexTest {

    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals(0, soundex.difference("cat", "cat"));
        assertEquals(1, soundex.difference("cat", "bat"));
        assertEquals(1, soundex.difference("cat", "bat"));
        assertEquals(2, soundex.difference("cat", "dog"));
        assertEquals(3, soundex.difference("cat", "apple"));
        assertEquals(4, soundex.difference("cat", "cut"));

        Soundex internationalSoundex = new Soundex("012301123012022301022455012623");
        assertEquals(0, internationalSoundex.difference("cat", "cat"));
        assertEquals(1, internationalSoundex.difference("cat", "bat"));
        assertEquals(2, internationalSoundex.difference("cat", "dog"));
        assertEquals(3, internationalSoundex.difference("cat", "apple"));
        assertEquals(4, internationalSoundex.difference("cat", "cut"));
        assertEquals(3, internationalSoundex.difference("Appel", "Apple"));
    }

    @Test
    public void testEncode() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals("K250", soundex.encode("cat"));
        assertEquals("B320", soundex.encode("bat"));
        assertEquals("G321", soundex.encode("dog"));
        assertEquals("A261", soundex.encode("apple"));
        assertEquals("K150", soundex.encode("cut"));

        Soundex internationalSoundex = new Soundex("012301123012022301022455012623");
        assertEquals("K250", internationalSoundex.encode("cat"));
        assertEquals("B320", internationalSoundex.encode("bat"));
        assertEquals("G321", internationalSoundex.encode("dog"));
        assertEquals("A221", internationalSoundex.encode("apple"));
        assertEquals("K150", internationalSoundex.encode("cut"));
    }

    @Test(expected = EncoderException.class)
    public void testEncodeWithNonString() {
        Soundex soundex = new Soundex();
        soundex.encode(123);
    }

    @Test
    public void testGetMappingCode() {
        Soundex soundex = new Soundex();
        assertEquals('2', soundex.getMappingCode("cat", 0));
        assertEquals('0', soundex.getMappingCode("cat", 1));
        assertEquals('5', soundex.getMappingCode("cat", 2));
        assertEquals('0', soundex.getMappingCode("cat", 3));
        assertEquals('0', soundex.getMappingCode("cat", 4));

        Soundex internationalSoundex = new Soundex("012301123012022301022455012623");
        assertEquals('2', internationalSoundex.getMappingCode("cat", 0));
        assertEquals('0', internationalSoundex.getMappingCode("cat", 1));
        assertEquals('5', internationalSoundex.getMappingCode("cat", 2));
        assertEquals('0', internationalSoundex.getMappingCode("cat", 3));
        assertEquals('0', internationalSoundex.getMappingCode("cat", 4));
    }

    @Test
    public void testSoundex() {
        Soundex soundex = new Soundex();
        assertEquals("K250", soundex.soundex("cat"));
        assertEquals("B320", soundex.soundex("bat"));
        assertEquals("G321", soundex.soundex("dog"));
        assertEquals("A261", soundex.soundex("apple"));
        assertEquals("K150", soundex.soundex("cut"));

        Soundex internationalSoundex = new Soundex("012301123012022301022455012623");
        assertEquals("K250", internationalSoundex.soundex("cat"));
        assertEquals("B320", internationalSoundex.soundex("bat"));
        assertEquals("G321", internationalSoundex.soundex("dog"));
        assertEquals("A221", internationalSoundex.soundex("apple"));
        assertEquals("K150", internationalSoundex.soundex("cut"));

        assertEquals("", soundex.soundex(""));
        assertEquals("", soundex.soundex(null));
    }
}