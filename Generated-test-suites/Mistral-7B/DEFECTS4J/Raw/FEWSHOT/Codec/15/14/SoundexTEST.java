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
        assertEquals(0, soundex.difference("app", "apple"));
        assertEquals(2, soundex.difference("apple", "application"));
        assertEquals(4, soundex.difference("application", "application"));
        assertEquals(1, soundex.difference("alpha", "alpha"));
        assertEquals(0, soundex.difference("alpha", "alpha"));
        assertEquals(3, soundex.difference("alpha", "alphonse"));
        assertEquals(3, soundex.difference("alphonse", "alpha"));
        assertNull(soundex.difference(null, "alpha", "alpha"));
        assertNull(soundex.difference("alpha", null, "alpha"));
        assertNull(soundex.difference("alpha", "alpha", null));
    }

    @Test
    public void testEncode() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals("A123", soundex.encode("Alvin"));
        assertEquals("A200", soundex.encode("Albert"));
        assertEquals("A163", soundex.encode("Alphonse"));
        assertEquals("A630", soundex.encode("Algernon"));
        assertEquals("A510", soundex.encode("Alonzo"));
        assertEquals("A500", soundex.encode("Alfred"));
        assertEquals("A260", soundex.encode("Alvin"));
        assertEquals("A260", soundex.encode("Alva"));
        assertEquals("A000", soundex.encode(""));
        assertEquals("A000", soundex.encode(null));
    }

    @Test
    public void testGetMappingCode() {
        Soundex soundex = new Soundex();
        assertEquals('1', soundex.getMappingCode("app", 0));
        assertEquals('1', soundex.getMappingCode("app", 1));
        assertEquals('1', soundex.getMappingCode("app", 2));
        assertEquals('1', soundex.getMappingCode("app", 3));
        assertEquals('0', soundex.getMappingCode("app", 4));
        assertEquals('1', soundex.getMappingCode("apple", 0));
        assertEquals('1', soundex.getMappingCode("apple", 1));
        assertEquals('1', soundex.getMappingCode("apple", 2));
        assertEquals('1', soundex.getMappingCode("apple", 3));
        assertEquals('0', soundex.getMappingCode("apple", 4));
        assertEquals('1', soundex.getMappingCode("application", 0));
        assertEquals('1', soundex.getMappingCode("application", 1));
        assertEquals('1', soundex.getMappingCode("application", 2));
        assertEquals('1', soundex.getMappingCode("application", 3));
        assertEquals('1', soundex.getMappingCode("application", 4));
        assertEquals('1', soundex.getMappingCode("alpha", 0));
        assertEquals('1', soundex.getMappingCode("alpha", 1));
        assertEquals('1', soundex.getMappingCode("alpha", 2));
        assertEquals('1', soundex.getMappingCode("alpha", 3));
        assertEquals('0', soundex.getMappingCode("alpha", 4));
        assertEquals('1', soundex.getMappingCode("alphonse", 0));
        assertEquals('1', soundex.getMappingCode("alphonse", 1));
        assertEquals('1', soundex.getMappingCode("alphonse", 2));
        assertEquals('3', soundex.getMappingCode("alphonse", 3));
        assertEquals('1', soundex.getMappingCode("alphonse", 4));
        assertEquals('1', soundex.getMappingCode("Algernon", 0));
        assertEquals('1', soundex.getMappingCode("Algernon", 1));
        assertEquals('1', soundex.getMappingCode("Algernon", 2));
        assertEquals('6', soundex.getMappingCode("Algernon", 3));
        assertEquals('0', soundex.getMappingCode("Algernon", 4));
        assertEquals('1', soundex.getMappingCode("Alonzo", 0));
        assertEquals('1', soundex.getMappingCode("Alonzo", 1));
        assertEquals('1', soundex.getMappingCode("Alonzo", 2));
        assertEquals('1', soundex.getMappingCode("Alonzo", 3));
        assertEquals('0', soundex.getMappingCode("Alonzo", 4));
        assertEquals('1', soundex.getMappingCode("Alfred", 0));
        assertEquals('1', soundex.getMappingCode("Alfred", 1));
        assertEquals('1', soundex.getMappingCode("Alfred", 2));
        assertEquals('0', soundex.getMappingCode("Alfred", 3));
        assertEquals('0', soundex.getMappingCode("Alfred", 4));
        assertEquals('1', soundex.getMappingCode("Alvin", 0));
        assertEquals('1', soundex.getMappingCode("Alvin", 1));
        assertEquals('1', soundex.getMappingCode("Alvin", 2));
        assertEquals('1', soundex.getMappingCode("Alvin", 3));
        assertEquals('0', soundex.getMappingCode("Alvin", 4));
        assertEquals('1', soundex.getMappingCode("Alva", 0));
        assertEquals('1', soundex.getMappingCode("Alva", 1));
        assertEquals('1', soundex.getMappingCode("Alva", 2));
        assertEquals('0', soundex.getMappingCode("Alva", 3));
        assertEquals('0', soundex.getMappingCode("Alva", 4));
    }

    @Test
    public void testSoundex() {
        Soundex soundex = new Soundex();
        assertEquals("A123", soundex.soundex("Alvin"));
        assertEquals("A200", soundex.soundex("Albert"));
        assertEquals("A163", soundex.soundex("Alphonse"));
        assertEquals("A630", soundex.soundex("Algernon"));
        assertEquals("A510", soundex.soundex("Alonzo"));
        assertEquals("A500", soundex.soundex("Alfred"));
        assertEquals("A260", soundex.soundex("Alvin"));
        assertEquals("A260", soundex.soundex("Alva"));
        assertEquals("A000", soundex.soundex(""));
        assertEquals("A000", soundex.soundex(null));
    }
}