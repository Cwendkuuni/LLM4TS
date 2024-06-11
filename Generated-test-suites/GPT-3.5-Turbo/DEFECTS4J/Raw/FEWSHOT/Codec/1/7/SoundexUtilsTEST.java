package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

public class SoundexUtilsTest {
    @Test
    public void testClean() {
        assertEquals("FOO", SoundexUtils.clean("fOo"));
        assertEquals("BAR", SoundexUtils.clean("bar"));
        assertEquals("BAZ", SoundexUtils.clean("Baz"));
        assertEquals("QUX", SoundexUtils.clean("q_u_x"));
        assertEquals("", SoundexUtils.clean(""));
        assertNull(SoundexUtils.clean(null));
    }

    @Test
    public void testDifference() throws EncoderException {
        assertEquals(4, SoundexUtils.difference(new Soundex(), "foo", "foo"));
        assertEquals(2, SoundexUtils.difference(new Soundex(), "foo", "bar"));
        assertEquals(3, SoundexUtils.difference(new Soundex(), "foo", "f00"));
        assertEquals(0, SoundexUtils.difference(new Soundex(), "foo", ""));
        
        assertEquals(4, SoundexUtils.difference(new RefinedSoundex(), "foo", "foo"));
        assertEquals(2, SoundexUtils.difference(new RefinedSoundex(), "foo", "bar"));
        assertEquals(4, SoundexUtils.difference(new RefinedSoundex(), "foo", "f00"));
        assertEquals(0, SoundexUtils.difference(new RefinedSoundex(), "foo", ""));
    }

    @Test
    public void testDifferenceEncoded() {
        assertEquals(4, SoundexUtils.differenceEncoded("F000", "F000"));
        assertEquals(2, SoundexUtils.differenceEncoded("F000", "B000"));
        assertEquals(3, SoundexUtils.differenceEncoded("F000", "F010"));
        assertEquals(0, SoundexUtils.differenceEncoded("F000", ""));
        
        assertEquals(4, SoundexUtils.differenceEncoded("F0000000", "F0000000"));
        assertEquals(2, SoundexUtils.differenceEncoded("F0000000", "B0000000"));
        assertEquals(4, SoundexUtils.differenceEncoded("F0000000", "F0100000"));
        assertEquals(0, SoundexUtils.differenceEncoded("F0000000", ""));
    }
}