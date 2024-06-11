package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        assertEquals("ABC", SoundexUtils.clean("aBc"));
        assertEquals("DEF", SoundexUtils.clean("dEfG"));
        assertEquals("GHIJKLMNOPQRST", SoundexUtils.clean("gHijklmNoPqRsT"));
        assertEquals("", SoundexUtils.clean(null));
        assertEquals("", SoundexUtils.clean(""));
        assertEquals("X", SoundexUtils.clean("x"));
        assertEquals("XYZ", SoundexUtils.clean("xYz"));
        assertEquals("XYZWV", SoundexUtils.clean("xYzWv"));
    }

    @Test
    public void testDifference() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.language.Soundex();
        assertEquals(3, SoundexUtils.difference(encoder, "apple", "appetite"));
        assertEquals(3, SoundexUtils.difference(encoder, "apple", "apples"));
        assertEquals(2, SoundexUtils.difference(encoder, "apple", "apricot"));
        assertEquals(1, SoundexUtils.difference(encoder, "apple", "orange"));
        assertEquals(0, SoundexUtils.difference(encoder, "apple", "banana"));
        assertEquals(4, SoundexUtils.difference(encoder, "appel", "apple"));
        assertEquals(4, SoundexUtils.difference(encoder, "Apple", "Apple"));
        assertEquals(3, SoundexUtils.difference(encoder, "apfel", "apple"));

        StringEncoder refinedEncoder = new org.apache.commons.codec.language.Metaphone();
        assertEquals(4, SoundexUtils.difference(refinedEncoder, "apple", "appetite"));
        assertEquals(4, SoundxUtils.difference(refinedEncoder, "apple", "apples"));
        assertEquals(3, SoundexUtils.difference(refinedEncoder, "apple", "apricot"));
        assertEquals(2, SoundexUtils.difference(refinedEncoder, "apple", "orange"));
        assertEquals(0, SoundexUtils.difference(refinedEncoder, "apple", "banana"));
        assertEquals(5, SoundexUtils.difference(refinedEncoder, "apple", "apfel"));
        assertEquals(4, SoundexUtils.difference(refinedEncoder, "Apple", "Apple"));
        assertEquals(4, SoundexUtils.difference(refinedEncoder, "apfel", "apple"));
    }

    @Test
    public void testDifferenceEncoded() {
        String encodedApple = new org.apache.commons.codec.language.Soundex().encode("apple");
        String encodedAppetite = new org.apache.commons.codec.language.Soundex().encode("appetite");
        String encodedApples = new org.apache.commons.codec.language.Soundex().encode("apples");
        String encodedApricot = new org.apache.commons.codec.language.Soundex().encode("apricot");
        String encodedOrange = new org.apache.commons.codec.language.Soundex().encode("orange");
        String encodedBanana = new org.apache.commons.codec.language.Soundex().encode("banana");

        assertEquals(3, SoundexUtils.differenceEncoded(encodedApple, encodedAppetite));
        assertEquals(3, SoundexUtils.differenceEncoded(encodedApple, encodedApples));
        assertEquals(2, SoundexUtils.differenceEncoded(encodedApple, encodedApricot));
        assertEquals(1, SoundexUtils.differenceEncoded(encodedApple, encodedOrange));
        assertEquals(0, SoundexUtils.differenceEncoded(encodedApple, encodedBanana));
        assertEquals(4, SoundexUtils.differenceEncoded("AA123", "AA123"));
        assertEquals(4, SoundexUtils.differenceEncoded("AA123", "AA123"));
        assertEquals(4, SoundexUtils.differenceEncoded("AA123", "AA123"));
        assertEquals(4, SoundexUtils.differenceEncoded("AA123", "AA123"));
        assertEquals(0, SoundexUtils.differenceEncoded(null, null));
        assertEquals(0, SoundexUtils.differenceEncoded("", ""));
    }
}