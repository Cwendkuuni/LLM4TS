package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

public class SoundexUtilsTest {
    @Test
    public void testClean() {
        assertEquals("ABC", SoundexUtils.clean("a?b@C"));
        assertEquals("ABC", SoundexUtils.clean("a123b_C"));
        assertEquals("A", SoundexUtils.clean("A@"));
        assertEquals("", SoundexUtils.clean(""));
    }

    @Test
    public void testDifference() throws EncoderException {
        assertEquals(4, SoundexUtils.difference(new Soundex(), "hello", "hello"));
        assertEquals(4, SoundexUtils.difference(new Soundex(), "HELLO", "hello"));
        assertEquals(2, SoundexUtils.difference(new Soundex(), "hello", "jello"));
        assertEquals(0, SoundexUtils.difference(new Soundex(), "abc", "xyz"));
        assertEquals(4, SoundexUtils.difference(new RefinedSoundex(), "hello", "hello"));
        assertEquals(4, SoundexUtils.difference(new RefinedSoundex(), "HELLO", "hello"));
        assertEquals(4, SoundexUtils.difference(new RefinedSoundex(), "hello", "jello"));
        assertEquals(0, SoundexUtils.difference(new RefinedSoundex(), "abc", "xyz"));
    }

    @Test
    public void testDifferenceEncoded() {
        assertEquals(4, SoundexUtils.differenceEncoded("H400", "H400"));
        assertEquals(4, SoundexUtils.differenceEncoded("H400", "h400"));
        assertEquals(2, SoundexUtils.differenceEncoded("H400", "J400"));
        assertEquals(0, SoundexUtils.differenceEncoded("H400", "Z000"));
    }
}