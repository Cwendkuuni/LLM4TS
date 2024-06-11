package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;

public class SoundexUtilsTest {
    @Test
    public void testClean() {
        assertEquals("ABC", SoundexUtils.clean("abc"));
        assertEquals("ABC", SoundexUtils.clean("Abc"));
        assertEquals("ABC", SoundexUtils.clean("aBc"));
        assertEquals("ABC", SoundexUtils.clean("ABC"));
        assertEquals("ABC", SoundexUtils.clean("aBc123"));
        assertEquals("ABC", SoundexUtils.clean("123Abc"));
        assertEquals("", SoundexUtils.clean(""));
        assertNull(SoundexUtils.clean(null));
    }
    
    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new Soundex();
        assertEquals(4, SoundexUtils.difference(encoder, "abcd", "abcd"));
        assertEquals(3, SoundexUtils.difference(encoder, "abcd", "abce"));
        assertEquals(2, SoundexUtils.difference(encoder, "abcd", "abcf"));
        assertEquals(1, SoundexUtils.difference(encoder, "abcd", "abcg"));
        assertEquals(0, SoundexUtils.difference(encoder, "abcd", "abch"));
        
        encoder = new RefinedSoundex();
        assertEquals(4, SoundexUtils.difference(encoder, "abcd", "abcd"));
        assertEquals(4, SoundexUtils.difference(encoder, "abcd", "abce"));
        assertEquals(4, SoundexUtils.difference(encoder, "abcd", "abcf"));
        assertEquals(4, SoundexUtils.difference(encoder, "abcd", "abcg"));
        assertEquals(4, SoundexUtils.difference(encoder, "abcd", "abch"));
    }
    
    @Test
    public void testDifferenceEncoded() {
        assertEquals(4, SoundexUtils.differenceEncoded("abcd", "abcd"));
        assertEquals(3, SoundexUtils.differenceEncoded("abcd", "abce"));
        assertEquals(2, SoundexUtils.differenceEncoded("abcd", "abcf"));
        assertEquals(1, SoundexUtils.differenceEncoded("abcd", "abcg"));
        assertEquals(0, SoundexUtils.differenceEncoded("abcd", "abch"));
        
        assertEquals(0, SoundexUtils.differenceEncoded("", ""));
        assertEquals(0, SoundexUtils.differenceEncoded(null, null));
    }
}