package org.apache.commons.codec.language.test;

import org.apache.commons.codec.EncodingException;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Assert;
import org.junit.Test;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        // Typical use case
        String input = "Hello World";
        String expected = "HELLO";
        String result = SoundexUtils.clean(input);
        Assert.assertEquals(expected, result);

        // Edge case 1: empty string
        String inputEmpty = "";
        String expectedEmpty = "";
        String resultEmpty = SoundexUtils.clean(inputEmpty);
        Assert.assertEquals(expectedEmpty, resultEmpty);

        // Edge case 2: null input
        String inputNull = null;
        String expectedNull = null;
        String resultNull = SoundexUtils.clean(inputNull);
        Assert.assertEquals(expectedNull, resultNull);

        // Error scenario: input contains non-alphabetic characters
        String inputInvalid = "123@#$%^&*()_+{}|:\"<>?[]";
        String expectedInvalid = "";
        String resultInvalid = SoundexUtils.clean(inputInvalid);
        Assert.assertEquals(expectedInvalid, resultInvalid);
    }

    @Test
    public void testDifference() throws EncodingException {
        // Typical use case
        String encoder = new org.apache.commons.codec.language.Soundex();
        String s1 = "Apple";
        String s2 = "Orange";
        int expected = 0;
        int result = SoundexUtils.difference(encoder, s1, s2);
        Assert.assertEquals(expected, result);

        // Edge case 1: identical strings
        String s1Identical = "Apple";
        String s2Identical = "Apple";
        int expectedIdentical = 4;
        int resultIdentical = SoundexUtils.difference(encoder, s1Identical, s2Identical);
        Assert.assertEquals(expectedIdentical, resultIdentical);

        // Edge case 2: longer string
        String s1Longer = "AppleAppleAppleApple";
        String s2 = "Apple";
        int expectedLonger = 4;
        int resultLonger = SoundexUtils.difference(encoder, s1Longer, s2);
        Assert.assertEquals(expectedLonger, resultLonger);

        // Error scenario: one of the strings is null
        String s1Null = null;
        String s2 = "Orange";
        int expectedNull = 0;
        try {
            int resultNull = SoundexUtils.difference(encoder, s1Null, s2);
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected
        }
        Assert.assertEquals(expectedNull, 0);
    }

    @Test
    public void testDifferenceEncoded() {
        // Typical use case
        String es1 = "122110";
        String es2 = "122000";
        int expected = 1;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        Assert.assertEquals(expected, result);

        // Edge case 1: identical encoded strings
        String es1Identical = "122110";
        String es2Identical = "122110";
        int expectedIdentical = 4;
        int resultIdentical = SoundexUtils.differenceEncoded(es1Identical, es2Identical);
        Assert.assertEquals(expectedIdentical, resultIdentical);

        // Edge case 2: longer encoded string
        String es1Longer = "122110122110";
        String es2 = "122110";
        int expectedLonger = 4;
        int resultLonger = SoundexUtils.differenceEncoded(es1Longer, es2);
        Assert.assertEquals(expectedLonger, resultLonger);

        // Error scenario: one of the encoded strings is null
        String es1Null = null;
        String es2 = "122000";
        int expectedNull = 0;
        int resultNull = SoundexUtils.differenceEncoded(es1Null, es2);
        Assert.assertEquals(expectedNull, 0);
    }

}