package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SoundexTest {
    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals(0, soundex.difference("hello", "world"));
        assertEquals(2, soundex.difference("coding", "code"));
        assertEquals(4, soundex.difference("test", "test"));
    }

    @Test
    public void testEncode_Object() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals("H416", soundex.encode("hello"));
        assertEquals("C315", soundex.encode("coding"));
        assertEquals("T230", soundex.encode("test"));
    }

    @Test
    public void testEncode_String() {
        Soundex soundex = new Soundex();
        assertEquals("H416", soundex.encode("hello"));
        assertEquals("C315", soundex.encode("coding"));
        assertEquals("T230", soundex.encode("test"));
    }

    @Test
    public void testGetMaxLength() {
        Soundex soundex = new Soundex();
        assertEquals(4, soundex.getMaxLength());
    }

    @Test
    public void testSetMaxLength() {
        Soundex soundex = new Soundex();
        soundex.setMaxLength(6);
        assertEquals(6, soundex.getMaxLength());
    }

    @Test
    public void testSoundex() {
        Soundex soundex = new Soundex();
        assertEquals("H416", soundex.soundex("hello"));
        assertEquals("C315", soundex.soundex("coding"));
        assertEquals("T230", soundex.soundex("test"));
    }
}