package org.apache.commons.codec.language.test;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test class for SoundexUtils.
 */
public class SoundexUtilsTest {

    /**
     * Test for clean method.
     */
    @Test
    public void testClean() {
        String input = "aBcDeF 123!@#$%";
        String expected = "ABC";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);
    }

    /**
     * Test for difference method with Soundex encoder.
     */
    @Test
    public void testDifferenceSoundexEncoder() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.language.SoundexEncoder();
        String str1 = "Apple";
        String str2 = "Orange";
        int expected = 2;
        int result = SoundexUtils.difference(encoder, str1, str2);
        assertEquals(expected, result);
    }

    /**
     * Test for differenceEncoded method.
     */
    @Test
    public void testDifferenceEncoded() {
        String es1 = "A123";
        String es2 = "B456";
        int expected = 0;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);
    }

    /**
     * Test for difference method with RefinedSoundex encoder.
     */
    @Test
    public void testDifferenceRefinedSoundexEncoder() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.language.RefinedSoundexEncoder();
        String str1 = "Apple";
        String str2 = "Orange";
        int expected = 3; // Refined Soundex can have more than 4 identical characters
        int result = SoundexUtils.difference(encoder, str1, str2);
        assertEquals(expected, result);
    }

    /**
     * Test for difference method with null input.
     */
    @Test(expected = NullPointerException.class)
    public void testDifferenceNullInput() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.language.RefinedSoundexEncoder();
        String str1 = null;
        String str2 = "Orange";
        SoundexUtils.difference(encoder, str1, str2);
    }
}