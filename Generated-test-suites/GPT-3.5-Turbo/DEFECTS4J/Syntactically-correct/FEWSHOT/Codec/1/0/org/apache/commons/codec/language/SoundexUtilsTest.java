package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.BeforeClass;
import org.junit.Test;

public class SoundexUtilsTest {
    private static StringEncoder encoder;

    @BeforeClass
    public static void setup() {
        encoder = new Soundex();
    }

    @Test
    public void testClean() {
        assertEquals("", SoundexUtils.clean(""));
        assertEquals("", SoundexUtils.clean(null));
        assertEquals("ABC", SoundexUtils.clean("ABC"));
        assertEquals("abc", SoundexUtils.clean("abc"));
        assertEquals("ABC", SoundexUtils.clean("a1b2c3"));
    }

    @Test
    public void testDifference() throws EncoderException {
        assertEquals(4, SoundexUtils.difference(encoder, "FOO", "FOO"));
        assertEquals(0, SoundexUtils.difference(encoder, "BAR", "FOO"));
        assertEquals(1, SoundexUtils.difference(encoder, "BAR", "BAZ"));
    }

    @Test
    public void testDifferenceEncoded() {
        assertEquals(0, SoundexUtils.differenceEncoded(null, null));
        assertEquals(0, SoundexUtils.differenceEncoded(null, "FOO"));
        assertEquals(0, SoundexUtils.differenceEncoded("BAR", null));
        assertEquals(1, SoundexUtils.differenceEncoded("FOO", "BOO"));
        assertEquals(4, SoundexUtils.differenceEncoded("FOO", "FOO"));
    }
}