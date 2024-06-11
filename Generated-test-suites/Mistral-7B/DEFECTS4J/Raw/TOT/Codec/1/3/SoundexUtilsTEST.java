package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

/**
 * Test cases for {@link SoundexUtils} class.
 */
public class SoundexUtilsTest {

    /**
     * Test for {@link SoundexUtils#clean(String)} method.
     */
    @Test
    public void testClean() {
        String input = "The quick brown fox jumps over the lazy dog.";
        String expected = "THE QUICK BROWN FOX JUMPS OVER THE LAZY DOG.";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);

        input = "12345";
        result = SoundexUtils.clean(input);
        assertNull(result);

        input = null;
        result = SoundexUtils.clean(input);
        assertNull(result);
    }

    /**
     * Test for {@link SoundexUtils#difference(StringEncoder, String, String)}
     * method.
     */
    @Test
    public void testDifferenceWithSoundexEncoder() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.language.Soundex();
        String s1 = "Apple";
        String s2 = "Orange";
        int expected = 0;
        int result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);

        s1 = "Apple";
        s2 = "Apple";
        expected = 4;
        result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);
    }

    /**
     * Test for {@link SoundexUtils#differenceEncoded(String, String)} method.
     */
    @Test
    public void testDifferenceEncoded() {
        String es1 = "1222201212";
        String es2 = "1222301212";
        int expected = 4;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);

        es1 = null;
        es2 = "1222201212";
        result = SoundexUtils.differenceEncoded(es1, es2);
        assertNull(result);

        es1 = "1222201212";
        es2 = null;
        result = SoundexUtils.differenceEncoded(es1, es2);
        assertNull(result);
    }

    /**
     * Test for {@link SoundexUtils#difference(StringEncoder, String, String)}
     * method with {@link RefinedSoundex} encoder.
     */
    @Test
    public void testDifferenceWithRefinedSoundexEncoder() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.language.RefinedSoundex();
        String s1 = "Apple";
        String s2 = "Orange";
        int expected = 0;
        int result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);

        s1 = "Apple";
        s2 = "Apple";
        expected = 4;
        result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);

        s1 = "Apple";
        s2 = "AppleApple";
        expected = 6;
        result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);
    }
}