package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.charset.ASCII;
import org.apache.commons.lang3.StringUtils;

import org.junit.Test;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        assertEquals("", SoundexUtils.clean(null));
        assertEquals("", SoundexUtils.clean(""));
        assertEquals("HELLO", SoundexUtils.clean("HELLO"));
        assertEquals("HELLO", SoundexUtils.clean(" HeLLo "));
        assertEquals("HELLO", SoundexUtils.clean("HeLLo"));
        assertEquals("HELLO", SoundexUtils.clean(" H E L L O "));
        assertEquals("HELLO", SoundexUtils.clean(" HeLlO "));
        assertEquals("HELLO", SoundexUtils.clean("H e l l o"));
        assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ", SoundexUtils.clean("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ", SoundexUtils.clean("AbCdEfGhIjKlMnOpQrStUvWxYz"));
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceThrowsExceptionWithNullEncoder() {
        SoundexUtils.difference(null, "foo", "bar");
    }

    @Test
    public void testDifference() throws EncoderException {
        int result = SoundexUtils.difference(new Soundex(), "foo", "bar");
        assertEquals(0, result);

        String fooEncoded = new Soundex().encode("foo");
        String barEncoded = new Soundex().encode("bar");
        result = SoundexUtils.differenceEncoded(fooEncoded, barEncoded);
        assertEquals(0, result);

        String fooBarEncoded = new Soundex().encode("foo bar");
        result = SoundexUtils.differenceEncoded(fooEncoded, fooBarEncoded);
        assertEquals(1, result);

        String fooFooEncoded = new Soundex().encode("foo foo");
        result = SoundexUtils.differenceEncoded(fooEncoded, fooFooEncoded);
        assertEquals(3, result);

        String appleEncoded = new Metaphone().encode("apple");
        String appleyEncoded = new Metaphone().encode("appley");
        result = SoundexUtils.differenceEncoded(appleEncoded, appleyEncoded);
        assertEquals(1, result);

        String alfaEncoded = new Soundex().encode("alfa");
        String alphaEncoded = new Soundex().encode("alpha");
        result = SoundexUtils.differenceEncoded(alfaEncoded, alphaEncoded);
        assertEquals(1, result);

        String alphaAlphaEncoded = new Soundex().encode("alpha alpha");
        result = SoundexUtils.differenceEncoded(alfaEncoded, alphaAlphaEncoded);
        assertEquals(4, result);

        String kangarooEncoded = new RefinedSoundex().encode("kangaroo");
        String kangerooEncoded = new RefinedSoundex().encode("kangeroo");
        result = SoundexUtils.differenceEncoded(kangarooEncoded, kangerooEncoded);
        assertEquals(3, result);

        String aardvarkEncoded = new RefinedSoundex().encode("aardvark");
        String anardvarkEncoded = new RefinedSoundex().encode("anardvark");
        result = SoundexUtils.differenceEncoded(aardvarkEncoded, anardvarkEncoded);
        assertEquals(3, result);
    }

    @Test
    public void testDifferenceWithNullStrings() {
        assertEquals(0, SoundexUtils.differenceEncoded(null, null));
    }
}