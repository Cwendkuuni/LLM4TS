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
        assertEquals(3, soundex.difference("apple", "appel"));
        assertEquals(0, soundex.difference("apple", "orange"));
        assertEquals(4, soundex.difference("knight", "night"));
    }

    @Test
    public void testEncode() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals("A123", soundex.encode("Apple"));
        assertEquals("A230", soundex.encode("Application"));
        assertEquals("A540", soundex.encode("Indiana"));
        assertEquals("A560", soundex.encode("Jamaica"));
    }

    @Test
    public void testEncodeCustomMapping() throws EncoderException {
        char[] customMapping = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '0', '0', '0', '0', '0', '0', '0'};
        Soundex customSoundex = new Soundex(customMapping);
        assertEquals("A123", customSoundex.encode("Apple"));
        assertEquals("B123", customSoundex.encode("Application"));
        assertEquals("C540", customSoundex.encode("Indiana"));
        assertEquals("D560", customSoundex.encode("Jamaica"));
    }

    @Test(expected = EncoderException.class)
    public void testInvalidEncode() throws EncoderException {
        Soundex soundex = new Soundex();
        soundex.encode(null);
    }

    @Test
    public void testGetSoundexMapping() {
        Soundex soundex = new Soundex();
        assertArrayEquals(US_ENGLISH_MAPPING, soundex.getSoundexMapping());
    }

    @Test
    public void testMap() {
        Soundex soundex = new Soundex();
        assertEquals('1', soundex.map('P'));
        assertEquals('0', soundex.map(' '));
        assertEquals('0', soundex.map('Q'));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMap() {
        Soundex soundex = new Soundex();
        soundex.map('Z');
    }
}