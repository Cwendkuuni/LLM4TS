package org.apache.commons.codec.language.test;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for SoundexUtils class.
 */
public class SoundexUtilsTest {

    /**
     * Test for clean method.
     */
    @Test
    public void testClean() {
        String input = "A short test string!";
        String expected = "ABSHORTTESTSTR";
        String result = SoundexUtils.clean(input);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test for difference method with Soundex encoder.
     */
    @Test
    public void testDifferenceWithSoundexEncoder() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.language.soundex.SoundexEncoder();
        String s1 = "Apple";
        String s2 = "Orange";
        int expected = 0;
        int result = SoundexUtils.difference(encoder, s1, s2);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test for difference method with RefinedSoundex encoder.
     */
    @Test
    public void testDifferenceWithRefinedSoundexEncoder() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.language.refined.RefinedSoundexEncoder();
        String s1 = "Apple";
        String s2 = "Orange";
        int expected = 0;
        int result = SoundexUtils.difference(encoder, s1, s2);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test for differenceEncoded method with Soundex encoded strings.
     */
    @Test
    public void testDifferenceEncodedWithSoundexEncodedStrings() {
        String es1 = "A123";
        String es2 = "B456";
        int expected = 0;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test for differenceEncoded method with RefinedSoundex encoded strings.
     */
    @Test
    public void testDifferenceEncodedWithRefinedSoundexEncodedStrings() {
        String es1 = "A123";
        String es2 = "B456";
        int expected = 0;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test for differenceEncoded method with same RefinedSoundex encoded strings.
     */
    @Test
    public void testDifferenceEncodedWithSameRefinedSoundexEncodedStrings() {
        String es1 = "A123";
        String es2 = "A123";
        int expected = 3; // all the same characters are the same
        int result = SoundexUtils.differenceEncoded(es1, es2);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test for differenceEncoded method with null input.
     */
    @Test(expected = NullPointerException.class)
    public void testDifferenceEncodedWithNullInput() {
        String es1 = null;
        String es2 = "A123";
        SoundexUtils.differenceEncoded(es1, es2);
    }

    /**
     * Test for differenceEncoded method with null input 2.
     */
    @Test(expected = NullPointerException.class)
    public void testDifferenceEncodedWithNullInput2() {
        String es1 = "A123";
        String es2 = null;
        SoundexUtils.differenceEncoded(es1, es2);
    }
}