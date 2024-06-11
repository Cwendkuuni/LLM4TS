package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;

public class SoundexUtilsTest {
    @Test
    public void testClean() {
        assertEquals("ABC", SoundexUtils.clean("a b c"));
        assertEquals("ABC", SoundexUtils.clean("A1B2C3"));
        assertEquals("DEF", SoundexUtils.clean("D.E.F"));
        assertNull(SoundexUtils.clean(null));
    }

    @Test(expected = EncoderException.class)
    public void testDifference() throws EncoderException {
        StringEncoder encoder = null;
        SoundexUtils.difference(encoder, "ABC", "DEF");
    }

    @Test
    public void testDifferenceEncoded() {
        assertEquals(3, SoundexUtils.differenceEncoded("ABC", "DEF"));
        assertEquals(0, SoundexUtils.differenceEncoded("", ""));
        assertEquals(0, SoundexUtils.differenceEncoded(null, null));
        assertEquals(0, SoundexUtils.differenceEncoded("AB", ""));
        assertEquals(2, SoundexUtils.differenceEncoded("AB", "AC"));
    }
}