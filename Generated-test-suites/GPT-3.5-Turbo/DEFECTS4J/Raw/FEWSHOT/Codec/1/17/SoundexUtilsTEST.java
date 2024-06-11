package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SoundexUtilsTest {
    @Test
    public void testClean() {
        // normal case
        assertEquals("ABC", SoundexUtils.clean("a1b2c3"));

        // empty string
        assertEquals("", SoundexUtils.clean(""));

        // null string
        assertNull(SoundexUtils.clean(null));

        // non-letter characters
        assertEquals("A BC", SoundexUtils.clean("a1 b2 c3"));

        // case sensitivity
        assertEquals("A B C", SoundexUtils.clean("a b c"));

        // non-English characters
        assertEquals("ÜÖÄ", SoundexUtils.clean("üöä"));
    }

    @Test
    public void testDifference() throws EncoderException {
        // similar strings
        Soundex encoder = new Soundex();
        assertEquals(4, SoundexUtils.difference(encoder, "hello", "hollo"));

        // dissimilar strings
        assertEquals(0, SoundexUtils.difference(encoder, "hello", "world"));

        // null strings
        assertEquals(0, SoundexUtils.difference(encoder, null, "world"));
        assertEquals(0, SoundexUtils.difference(encoder, "hello", null));
        assertEquals(0, SoundexUtils.difference(encoder, null, null));
    }

    @Test
    public void testDifferenceEncoded() {
        // similar strings
        assertEquals(4, SoundexUtils.differenceEncoded("1234", "1243"));

        // dissimilar strings
        assertEquals(0, SoundexUtils.differenceEncoded("1234", "5678"));

        // null strings
        assertEquals(0, SoundexUtils.differenceEncoded(null, "1243"));
        assertEquals(0, SoundexUtils.differenceEncoded("1234", null));
        assertEquals(0, SoundexUtils.differenceEncoded(null, null));

        // different lengths
        assertEquals(2, SoundexUtils.differenceEncoded("123", "1234"));
        assertEquals(2, SoundexUtils.differenceEncoded("1234", "123"));
    }
}