package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;

public class SoundexUtilsTest {
    
    @Test
    public void testClean() {
        assertEquals("TEST", SoundexUtils.clean("test"));
        assertEquals("TEST", SoundexUtils.clean("TEST"));
        assertEquals("TEST", SoundexUtils.clean("tEsT"));
        assertNull(SoundexUtils.clean(null));
        assertEquals("", SoundexUtils.clean(""));
        assertEquals("", SoundexUtils.clean("123"));
    }

    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new Soundex();
        assertEquals(4, SoundexUtils.difference(encoder, "test1", "test2"));
        assertEquals(0, SoundexUtils.difference(encoder, "different", "values"));
        assertEquals(2, SoundexUtils.difference(encoder, "sound", "sond"));
        assertEquals(0, SoundexUtils.difference(encoder, null, "test"));
        assertEquals(0, SoundexUtils.difference(encoder, "test", null));
    }

    @Test
    public void testDifferenceEncoded() {
        assertEquals(0, SoundexUtils.differenceEncoded(null, null));
        assertEquals(0, SoundexUtils.differenceEncoded(null, ""));
        assertEquals(0, SoundexUtils.differenceEncoded("", null));
        assertEquals(0, SoundexUtils.differenceEncoded("", ""));
        assertEquals(2, SoundexUtils.differenceEncoded("ABCD", "ABEF"));
        assertEquals(4, SoundexUtils.differenceEncoded("ABCD", "ABCD"));
        assertEquals(3, SoundexUtils.differenceEncoded("ABCD", "ABCE"));
        assertEquals(0, SoundexUtils.differenceEncoded("ABCD", ""));
        assertEquals(0, SoundexUtils.differenceEncoded(null, "ABCD"));
    }
}