package org.apache.commons.codec.language;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;

public class SoundexTest {

    @Test
    public void testSoundexWithUS_ENGLISH_MAPPING() {
        Soundex soundex = Soundex.US_ENGLISH;

        assertEquals("B263", soundex.encode("Bob"));
        assertEquals("B161", soundex.encode("Alice"));
        assertEquals("B123", soundex.encode("Barbara"));
        assertEquals("B161", soundex.encode("Bobby"));
        assertEquals("B161", soundex.encode("Robert"));
        assertEquals("B161", soundex.encode("RobERT"));
        assertEquals("B325", soundex.encode("Abraham"));
        assertEquals("B234", soundex.encode("Theodore"));
        assertEquals("B161", soundex.encode("Doris"));
        assertEquals("B161", soundex.encode("Edward"));

        assertEquals(4, soundex.difference("Alice", "Bobby"));
        assertEquals(3, soundex.difference("Alice", "Bob"));
        assertEquals(2, soundex.difference("Alice", "Barbara"));
        assertEquals(1, soundex.difference("Alice", "Doris"));

        try {
            soundex.encode(new Object() {});
            assert false;
        } catch (EncoderException e) {
            // Expected
        }

        try {
            soundex.encode("abCdeF");
            assert false;
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCustomSoundexMapping() {
        char[] customMapping = new char[]{'0', '1', '1', '0', '2', '1', '2', '2', '3', '1', '1', '0', '1', '1', '1', '1', '1', '1', '1', '1'};
        Soundex customSoundex = new Soundex(customMapping);

        assertEquals("A111", customSoundex.encode("Alice"));
        assertEquals("B111", customSoundex.encode("Bob"));
        assertEquals("C222", customSoundex.encode("Barbara"));
    }
}