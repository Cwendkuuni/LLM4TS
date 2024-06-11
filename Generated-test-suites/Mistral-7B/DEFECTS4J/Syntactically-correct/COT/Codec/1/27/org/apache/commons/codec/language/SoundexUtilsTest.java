package org.apache.commons.codec.language.test;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for SoundexUtils.
 */
public class SoundexUtilsTest {

    @Test
    public void testCleanMethod() {
        String input = "A short string with lowercase letters and numbers.";
        String expected = "A SHORT STRING.";
        String result = SoundexUtils.clean(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testCleanEmptyString() {
        String input = "";
        String expected = "";
        String result = SoundexUtils.clean(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testCleanNullString() {
        String input = null;
        String expected = null;
        String result = SoundexUtils.clean(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDifferenceMethodWithSoundexEncoderAndSameStrings() {
        String encoderName = "Soundex";
        String s1 = "Apple";
        String s2 = "Apple";
        int expected = 4;
        int result;
        try {
            result = SoundexUtils.difference(new SoundexEncoder(), s1, s2);
        } catch (EncoderException e) {
            throw new RuntimeException(e);
        }
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDifferenceMethodWithSoundexEncoderAndDifferentStrings() {
        String encoderName = "Soundex";
        String s1 = "Apple";
        String s2 = "Orange";
        int expected = 0;
        int result;
        try {
            result = SoundexUtils.difference(new SoundexEncoder(), s1, s2);
        } catch (EncoderException e) {
            throw new RuntimeException(e);
        }
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDifferenceMethodWithRefinedSoundexEncoderAndSameStrings() {
        String encoderName = "RefinedSoundex";
        String s1 = "Apple";
        String s2 = "Apple";
        int expected = 4;
        int result;
        try {
            result = SoundexUtils.difference(new RefinedSoundexEncoder(), s1, s2);
        } catch (EncoderException e) {
            throw new RuntimeException(e);
        }
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDifferenceMethodWithRefinedSoundexEncoderAndDifferentStrings() {
        String encoderName = "RefinedSoundex";
        String s1 = "Apple";
        String s2 = "Orange";
        int expected = 0;
        int result;
        try {
            result = SoundexUtils.difference(new RefinedSoundexEncoder(), s1, s2);
        } catch (EncoderException e) {
            throw new RuntimeException(e);
        }
        Assert.assertEquals(expected, result);
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceMethodWithInvalidEncoder() {
        String encoderName = "InvalidEncoder";
        String s1 = "Apple";
        String s2 = "Apple";
        SoundexUtils.difference(new InvalidEncoder(), s1, s2);
    }

    @Test
    public void testDifferenceEncodedMethodWithSameStrings() {
        String es1 = "A123";
        String es2 = "A123";
        int expected = 4;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDifferenceEncodedMethodWithDifferentStrings() {
        String es1 = "A123";
        String es2 = "B456";
        int expected = 0;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDifferenceEncodedMethodWithNullStrings() {
        String es1 = null;
        String es2 = null;
        int expected = 0;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        Assert.assertEquals(expected, result);
    }

}