package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

public class SoundexUtilsTest {
    
    @Test
    public void testClean() {
        assertEquals("ABC", SoundexUtils.clean("ABC"));
        assertEquals("DEF", SoundexUtils.clean("dE6f"));
        assertEquals("GHIJKL", SoundexUtils.clean("GH$^%IJKL"));
        assertEquals("MNOP", SoundexUtils.clean("mnop"));
        assertEquals("QRSTUV", SoundexUtils.clean("123qrstuv"));
        assertEquals("WXYZ", SoundexUtils.clean("wxyz"));
    }
    
    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new Soundex();
        assertEquals(1, SoundexUtils.difference(encoder, "ABCD", "ABZD"));
        assertEquals(2, SoundexUtils.difference(encoder, "ABCD", "ABCD"));
        assertEquals(3, SoundexUtils.difference(encoder, "ABCD", "ABCE"));
        assertEquals(0, SoundexUtils.difference(encoder, "ABCD", "WXYZ"));
    }
    
    @Test
    public void testDifferenceEncoded() {
        assertEquals(1, SoundexUtils.differenceEncoded("ABCD", "ABZD"));
        assertEquals(2, SoundexUtils.differenceEncoded("ABCD", "ABCD"));
        assertEquals(3, SoundexUtils.differenceEncoded("ABCD", "ABCE"));
        assertEquals(0, SoundexUtils.differenceEncoded("ABCD", "WXYZ"));
    }

}