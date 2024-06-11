package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;

public class SoundexUtilsTest {
    @Test
    public void testClean() {
        assertEquals("HELLO", SoundexUtils.clean("hEllo"));
        assertEquals("WORLD", SoundexUtils.clean("WORLD"));
        assertNull(SoundexUtils.clean(null));
        assertNull(SoundexUtils.clean(""));
    }

    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new Soundex();
        assertEquals(0, SoundexUtils.difference(encoder, "hello", "world"));
        assertEquals(4, SoundexUtils.difference(encoder, "hello", "hEllo"));
        
        encoder = new RefinedSoundex();
        assertEquals(0, SoundexUtils.difference(encoder, "hello", "world"));
        assertEquals(5, SoundexUtils.difference(encoder, "hello", "hEllo"));
    }

    @Test
    public void testDifferenceEncoded() {
        assertEquals(0, SoundexUtils.differenceEncoded("HELLO", "WORLD"));
        assertEquals(4, SoundexUtils.differenceEncoded("HELLO", "HELLo"));
        assertEquals(4, SoundexUtils.differenceEncoded("HELLO", "hello"));
        assertEquals(5, SoundexUtils.differenceEncoded("HELLO", "hEllo"));
    }
}