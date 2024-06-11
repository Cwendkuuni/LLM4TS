package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

public class SoundexUtilsTest {
    @Test
    public void testClean() {
        assertEquals("FOO", SoundexUtils.clean("foo"));
        assertEquals("BAR", SoundexUtils.clean("bar"));
        assertEquals("", SoundexUtils.clean(""));
        assertNull(SoundexUtils.clean(null));
    }

    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new Soundex();
        assertEquals(3, SoundexUtils.difference(encoder, "foo", "bar"));
        assertEquals(4, SoundexUtils.difference(encoder, "abcd", "abcd"));
        assertEquals(2, SoundexUtils.difference(encoder, "abc", "abd"));
        assertEquals(0, SoundexUtils.difference(encoder, "", ""));
    }

    @Test
    public void testDifferenceEncoded() {
        assertEquals(3, SoundexUtils.differenceEncoded("F001", "B600"));
        assertEquals(4, SoundexUtils.differenceEncoded("A000", "A000"));
        assertEquals(2, SoundexUtils.differenceEncoded("A000", "A100"));
        assertEquals(0, SoundexUtils.differenceEncoded("", ""));
    }
}