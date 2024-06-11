package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class SoundexUtilsTest {
    
    @Test
    public void testClean() {
        assertEquals("ABC", SoundexUtils.clean("aBc"));
        assertEquals("XYZ", SoundexUtils.clean("xyz"));
        assertEquals("", SoundexUtils.clean(""));
        assertNull(SoundexUtils.clean(null));
    }

    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new Soundex();
        assertEquals(0, SoundexUtils.difference(encoder, "abc", "def"));
        assertEquals(4, SoundexUtils.difference(encoder, "abc", "abc"));
        assertEquals(1, SoundexUtils.difference(encoder, "abc", "bbc"));
        assertEquals(2, SoundexUtils.difference(encoder, "abc", "abd"));
    }

    @Test
    public void testDifferenceEncoded() {
        assertEquals(0, SoundexUtils.differenceEncoded("ABCD", "EFGH"));
        assertEquals(4, SoundexUtils.differenceEncoded("ABCD", "ABCD"));
        assertEquals(1, SoundexUtils.differenceEncoded("ABCD", "ABCE"));
        assertEquals(2, SoundexUtils.differenceEncoded("ABCD", "ABCD"));
    }

}