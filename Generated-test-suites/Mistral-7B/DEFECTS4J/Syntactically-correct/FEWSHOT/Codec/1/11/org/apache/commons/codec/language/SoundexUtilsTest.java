package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.SoundexUtils;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        String input = "The quick Brown fox jumps over the lazy dog";
        assertEquals("THE QUICK BROWN FOX JUMPS OVER THE LAZY DOG", SoundexUtils.clean(input));
        assertEquals("", SoundexUtils.clean(null));
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceThrowsExceptionWhenNullEncoder() throws EncoderException {
        SoundexUtils.difference(null, "foo", "bar");
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceThrowsExceptionWhenNullString1() throws EncoderException {
        StringEncoder encoder = org.apache.commons.codec.net.URLCodec.INSTANCE;
        SoundexUtils.difference(encoder, null, "bar");
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceThrowsExceptionWhenNullString2() throws EncoderException {
        StringEncoder encoder = org.apache.commons.codec.net.URLCodec.INSTANCE;
        SoundexUtils.difference(encoder, "foo", null);
    }

    @Test
    public void testDifferenceSoundex() {
        StringEncoder encoder = org.apache.commons.codec.net.URLCodec.INSTANCE;
        assertEquals(2, SoundexUtils.difference(encoder, "apple", "appal"));
        assertEquals(4, SoundexUtils.difference(encoder, "apple", "apples"));
        assertEquals(0, SoundexUtils.difference(encoder, "apple", "orange"));
        assertNull(SoundexUtils.differenceEncoded(null, null));
    }

    @Test
    public void testDifferenceRefinedSoundex() {
        StringEncoder encoder = new SoundexEncoder();
        String string1 = "appalachian";
        String string2 = "application";
        int difference = SoundexUtils.difference(encoder, string1, string2);
        assertEquals(5, difference);
    }
}

class SoundexEncoder implements StringEncoder {
    @Override
    public String encode(String string) {
        return SoundexUtils.soundex(string);
    }

    @Override
    public String decode(String encoded) {
        throw new UnsupportedOperationException();
    }
}