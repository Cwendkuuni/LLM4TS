package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;

/**
 * Test for SoundexUtils class.
 */
public class SoundexUtilsTest {

    /**
     * Test for clean method.
     */
    @Test
    public void testClean() {
        String input = "aBc DeF gHi Jk";
        String expected = "ABC DEF GHI";
        String actual = SoundexUtils.clean(input);
        assertEquals(expected, actual);

        String nullInput = null;
        String emptyInput = "";
        assertEquals(nullInput, SoundexUtils.clean(nullInput));
        assertEquals(emptyInput, SoundexUtils.clean(emptyInput));
    }

    /**
     * Test for difference method with Soundex encoder.
     */
    @Test
    public void testDifferenceSoundexEncoder() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.soundex.Soundex();
        String s1 = "Apple";
        String s2 = "Orange";
        int expected = 0;
        int actual = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, actual);

        String s3 = "Apple";
        String s4 = "Apple";
        int lengthToMatch = s3.length();
        int diff = 0;
        for (int i = 0; i < lengthToMatch; i++) {
            if (s3.charAt(i) == s4.charAt(i)) {
                diff++;
            }
        }
        expected = lengthToMatch;
        actual = SoundexUtils.differenceEncoded(encoder.encode(s3), encoder.encode(s4));
        assertEquals(expected, actual);

        String s5 = "Apple";
        String s6 = "AppleApple";
        expected = lengthToMatch;
        actual = SoundexUtils.differenceEncoded(encoder.encode(s5), encoder.encode(s6));
        assertEquals(expected, actual);

        String s7 = "Apple";
        String s8 = "Apple1";
        expected = 0;
        actual = SoundexUtils.differenceEncoded(encoder.encode(s7), encoder.encode(s8));
        assertEquals(expected, actual);

        assertThrows(EncoderException.class, () -> SoundexUtils.difference(encoder, null, s1));
        assertThrows(EncoderException.class, () -> SoundexUtils.difference(encoder, s1, null));
        assertThrows(EncoderException.class, () -> SoundexUtils.difference(encoder, null, null));
    }

    /**
     * Test for difference method with RefinedSoundex encoder.
     */
    @Test
    public void testDifferenceRefinedSoundexEncoder() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.soundex.RefinedSoundex();
        String s1 = "Apple";
        String s2 = "Orange";
        int expected = 0;
        int actual = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, actual);

        String s3 = "Apple";
        String s4 = "Apple";
        lengthToMatch = s3.length();
        diff = 0;
        for (int i = 0; i < lengthToMatch; i++) {
            if (s3.charAt(i) == s4.charAt(i)) {
                diff++;
            }
        }
        expected = lengthToMatch;
        actual = SoundexUtils.differenceEncoded(encoder.encode(s3), encoder.encode(s4));
        assertEquals(expected, actual);

        String s5 = "Apple";
        String s6 = "AppleApple";
        expected = lengthToMatch;
        actual = SoundexUtils.differenceEncoded(encoder.encode(s5), encoder.encode(s6));
        assertEquals(expected, actual);

        String s7 = "Apple";
        String s8 = "Apple1";
        expected = 0;
        actual = SoundexUtils.differenceEncoded(encoder.encode(s7), encoder.encode(s8));
        assertEquals(expected, actual);

        assertThrows(EncoderException.class, () -> SoundexUtils.difference(encoder, null, s1));
        assertThrows(EncoderException.class, () -> SoundexUtils.difference(encoder, s1, null));
        assertThrows(EncoderException.class, () -> SoundexUtils.difference(encoder, null, null));
    }
}