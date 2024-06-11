package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.language.Soundex;
import org.junit.Test;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        assertEquals("AbCdEfGhIjKlMnOpQrStUvWxYz", SoundexUtils.clean("AbCdEfGhIjKlMnOpQrStUvWxYz"));
        assertEquals("", SoundexUtils.clean(null));
        assertEquals("", SoundexUtils.clean(""));
        assertEquals("ABC", SoundexUtils.clean("AbC"));
        assertEquals("ABC", SoundexUtils.clean("AbCdEf"));
        assertEquals("ABC", SoundexUtils.clean("ABCdEf"));
        assertEquals("ABC", SoundexUtils.clean("ABCdEfG"));
    }

    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new Soundex();
        assertEquals(0, SoundexUtils.difference(encoder, null, null));
        assertEquals(0, SoundexUtils.difference(encoder, "", ""));
        assertEquals(4, SoundexUtils.difference(encoder, "cat", "kat"));
        assertEquals(4, SoundexUtils.difference(encoder, "cat", "cot"));
        assertEquals(3, SoundexUtils.difference(encoder, "cat", "mat"));
        assertEquals(4, SoundexUtils.difference(encoder, "cat", "cotat"));
        assertEquals(2, SoundexUtils.difference(encoder, "cat", "bat"));
        assertEquals(1, SoundexUtils.difference(encoder, "cat", "batman"));
        assertEquals(1, SoundexUtils.difference(encoder, "cat", "batman123"));

        StringEncoder refinedEncoder = new RefinedSoundex();
        assertEquals(0, SoundexUtils.difference(refinedEncoder, null, null));
        assertEquals(0, SoundexUtils.difference(refinedEncoder, "", ""));
        assertEquals(5, SoundexUtils.difference(refinedEncoder, "cat", "kat"));
        assertEquals(5, SoundexUtils.difference(refinedEncoder, "cat", "cot"));
        assertEquals(4, SoundexUtils.difference(refinedEncoder, "cat", "mat"));
        assertEquals(5, SoundexUtils.difference(refinedEncoder, "cat", "cotat"));
        assertEquals(3, SoundexUtils.difference(refinedEncoder, "cat", "bat"));
        assertEquals(2, SoundexUtils.difference(refinedEncoder, "cat", "batman"));
        assertEquals(1, SoundexUtils.difference(refinedEncoder, "cat", "batman123"));
        assertEquals(3, SoundexUtils.difference(refinedEncoder, "cat", "batman1234"));
        assertEquals(4, SoundexUtils.difference(refinedEncoder, "cat", "batman12345"));
    }

    @Test
    public void testDifferenceEncoded() {
        String encodedCat = new Soundex().encode("cat");
        String encodedKat = new Soundex().encode("kat");
        String encodedCot = new Soundex().encode("cot");

        assertEquals(0, SoundexUtils.differenceEncoded(null, null));
        assertEquals(0, SoundexUtils.differenceEncoded("", ""));
        assertEquals(4, SoundexUtils.differenceEncoded(encodedCat, encodedKat));
        assertEquals(4, SoundexUtils.differenceEncoded(encodedCat, encodedCot));
        assertEquals(3, SoundexUtils.differenceEncoded(encodedCat, new String(Hex.decodeHex("414143616e6174"))));
        assertEquals(2, SoundexUtils.differenceEncoded(encodedCat, new String(Hex.decodeHex("42415472616e616d"))));
        assertEquals(1, SoundexUtils.differenceEncoded(encodedCat, new String(Hex.decodeHex("42415472616e616d2061313233"))));
    }
}