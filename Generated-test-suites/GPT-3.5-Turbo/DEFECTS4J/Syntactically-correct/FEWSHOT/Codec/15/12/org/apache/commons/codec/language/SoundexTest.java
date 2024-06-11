package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SoundexTest {

    @Test
    public void testEncode() {
        Soundex soundex = new Soundex();
        assertEquals("A061", soundex.encode("Apache"));
        assertEquals("A061", soundex.encode("apache"));
        assertEquals("A061", soundex.encode("APACHE"));

        Soundex customSoundex = new Soundex(new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'});
        assertEquals("3139", customSoundex.encode("Hello"));
        assertEquals("0811", customSoundex.encode("World"));
    }

    @Test
    public void testEncodeInvalidCharacters() {
        Soundex soundex = new Soundex();
        assertEquals("A061", soundex.encode("Apache#$@#!"));
    }

    @Test(expected = EncoderException.class)
    public void testEncodeNonStringObject() throws EncoderException {
        Soundex soundex = new Soundex();
        soundex.encode(123);
    }

    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals(0, soundex.difference("Apache", "ebokwW"));
        assertEquals(1, soundex.difference("Apache", "ebokhw"));
    }

    @Test
    public void testDifferenceNullInput() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals(0, soundex.difference(null, "ebokhw"));
        assertEquals(0, soundex.difference("Apache", null));
    }

    @Test
    public void testDifferenceBothNullInput() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals(0, soundex.difference(null, null));
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceNonStringInput() throws EncoderException {
        Soundex soundex = new Soundex();
        soundex.difference(123, "ebokhw");
    }

    @Test
    public void testSoundex() {
        Soundex soundex = new Soundex();
        assertEquals("A120", soundex.soundex("Apache"));
        assertEquals("A120", soundex.soundex("apache"));
        assertEquals("A120", soundex.soundex("APACHE"));

        Soundex customSoundex = new Soundex(new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'});
        assertEquals("H141", customSoundex.soundex("Hello"));
        assertEquals("W410", customSoundex.soundex("World"));
    }

    @Test
    public void testSoundexEmptyString() {
        Soundex soundex = new Soundex();
        assertEquals("", soundex.soundex(""));
    }

    @Test
    public void testSoundexNullInput() {
        Soundex soundex = new Soundex();
        assertNull(soundex.soundex(null));
    }

    @Test
    public void testSoundexInvalidCharacters() {
        Soundex soundex = new Soundex();
        assertEquals("A120", soundex.soundex("Apache#$@#!"));
    }

}