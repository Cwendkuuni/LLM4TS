package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;

public class SoundexUtilsTest {
    @Test
    public void testClean() {
        assertEquals("ABCD", SoundexUtils.clean("abcd"));
        assertEquals("EFGH", SoundexUtils.clean("efgh"));
        assertEquals("IJKL", SoundexUtils.clean("ijkl"));
        assertEquals("MNOP", SoundexUtils.clean("mnop"));
    }

    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new Soundex();
        assertEquals(0, SoundexUtils.difference(encoder, "abcd", "efgh"));
        assertEquals(1, SoundexUtils.difference(encoder, "abcd", "abce"));
        assertEquals(2, SoundexUtils.difference(encoder, "abcd", "abcf"));
        assertEquals(3, SoundexUtils.difference(encoder, "abcd", "abcf"));
        assertEquals(4, SoundexUtils.difference(encoder, "abcd", "abcd"));
    }

    @Test
    public void testDifferenceEncoded() {
        assertEquals(0, SoundexUtils.differenceEncoded("ABCD", "EFGH"));
        assertEquals(1, SoundexUtils.differenceEncoded("ABCD", "ABCE"));
        assertEquals(2, SoundexUtils.differenceEncoded("ABCD", "ABCF"));
        assertEquals(2, SoundexUtils.differenceEncoded("ABCD", "ABCF"));
        assertEquals(4, SoundexUtils.differenceEncoded("ABCD", "ABCD"));
    }
}