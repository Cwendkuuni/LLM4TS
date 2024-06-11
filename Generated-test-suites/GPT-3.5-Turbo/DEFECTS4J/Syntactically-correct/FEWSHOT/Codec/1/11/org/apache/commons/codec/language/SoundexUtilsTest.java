
package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;

public class SoundexUtilsTest {
    
    // test for clean method
    @Test
    public void testClean() {
        assertEquals("ABC", SoundexUtils.clean("a/b.c"));
        assertEquals("DEF", SoundexUtils.clean("DE;/F!"));
        assertNull(SoundexUtils.clean(null));
        assertEquals("", SoundexUtils.clean(""));
    }
    
    // test for difference method
    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new Soundex();
        assertEquals(0, SoundexUtils.difference(encoder, "abc", "def"));
        assertEquals(0, SoundexUtils.difference(encoder, "hello", "world"));
        assertEquals(4, SoundexUtils.difference(encoder, "jumps", "jumps"));
        assertEquals(2, SoundexUtils.difference(encoder, "music", "mute"));
        
        encoder = new RefinedSoundex();
        assertEquals(0, SoundexUtils.difference(encoder, "abc", "def"));
        assertEquals(0, SoundexUtils.difference(encoder, "hello", "world"));
        assertEquals(5, SoundexUtils.difference(encoder, "jumps", "jumps"));
        assertEquals(4, SoundexUtils.difference(encoder, "music", "mute"));
    }
    
    // test for differenceEncoded method
    @Test
    public void testDifferenceEncoded() {
        assertEquals(0, SoundexUtils.differenceEncoded("abc", "def"));
        assertEquals(0, SoundexUtils.differenceEncoded("hello", "world"));
        assertEquals(4, SoundexUtils.differenceEncoded("jumps", "jumps"));
        assertEquals(2, SoundexUtils.differenceEncoded("music", "mute"));
    }
}
