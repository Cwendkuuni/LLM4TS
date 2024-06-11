package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        assertEquals("JAVA", SoundexUtils.clean("Java123"));
        assertEquals("HELLO", SoundexUtils.clean("Hello99"));
        assertEquals("WORLD", SoundexUtils.clean("World-1"));
        assertNull(SoundexUtils.clean(""));
        assertNull(SoundexUtils.clean(null));
    }

    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new Soundex();
        assertEquals(4, SoundexUtils.difference(encoder, "java", "jafa"));
        assertEquals(0, SoundexUtils.difference(encoder, "hello", "world"));
        assertEquals(2, SoundexUtils.difference(encoder, "java", "jova"));
        assertEquals(3, SoundexUtils.difference(encoder, "world", "wordl"));
        assertEquals(4, SoundexUtils.difference(encoder, "soundex", "soundex"));
    }

    @Test
    public void testDifferenceEncoded() {
        assertEquals(2, SoundexUtils.differenceEncoded("J200", "J300"));
        assertEquals(0, SoundexUtils.differenceEncoded("H400", "W450"));
        assertEquals(1, SoundexUtils.differenceEncoded("W430", "W420"));
        assertEquals(2, SoundexUtils.differenceEncoded("W560", "W540"));
        assertEquals(3, SoundexUtils.differenceEncoded("S530", "WDC1"));
    }
}