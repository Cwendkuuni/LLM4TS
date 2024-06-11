package org.apache.commons.codec.language.test;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexUtilsTest {

    @Test
    public void testCleanMethod() {
        String input = null;
        String expectedOutput = "";
        String actualOutput = SoundexUtils.clean(input);
        assertEquals(expectedOutput, actualOutput);

        input = "";
        expectedOutput = "";
        actualOutput = SoundexUtils.clean(input);
        assertEquals(expectedOutput, actualOutput);

        input = "abc123";
        expectedOutput = "";
        actualOutput = SoundexUtils.clean(input);
        assertEquals(expectedOutput, actualOutput);

        input = "A b c";
        expectedOutput = "ABC";
        actualOutput = SoundexUtils.clean(input);
        assertEquals(expectedOutput, actualOutput);

        input = "The quick Brown Fox jumps over the Lazy Dog";
        expectedOutput = "THE QUICK BROWN FOX JUMPS OVER THE LAZY DOG";
        actualOutput = SoundexUtils.clean(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDifferenceMethodWithSoundexEncoder() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.language.Soundex();
        String s1 = "apple";
        String s2 = "apples";
        int expectedOutput = 3;
        int actualOutput = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expectedOutput, actualOutput);

        s1 = "cat";
        s2 = "cat";
        expectedOutput = 4;
        actualOutput = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expectedOutput, actualOutput);

        s1 = "dog";
        s2 = "cat";
        expectedOutput = 0;
        actualOutput = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDifferenceMethodWithRefinedSoundexEncoder() throws EncoderException {
        StringEncoder encoder = new org.apache.commons.codec.language.RefinedSoundex();
        String s1 = "apple";
        String s2 = "apples";
        int expectedOutput = 3;
        int actualOutput = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expectedOutput, actualOutput);

        s1 = "cat";
        s2 = "cat";
        expectedOutput = 4;
        actualOutput = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expectedOutput, actualOutput);

        s1 = "dog";
        s2 = "cat";
        expectedOutput = 0;
        actualOutput = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expectedOutput, actualOutput);

        s1 = "father";
        s2 = "fifther";
        expectedOutput = 5;
        actualOutput = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = EncoderException.class)
    public void testDifferenceMethodWithInvalidEncoder() throws EncoderException {
        StringEncoder encoder = new InvalidEncoder();
        String s1 = "apple";
        String s2 = "apples";
        SoundexUtils.difference(encoder, s1, s2);
    }
}