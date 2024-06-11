package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import org.junit.Test;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.language.Soundex;

/**
 * Test cases for {@link SoundexUtils} class.
 */
public class SoundexUtilsTest {

    /**
     * Test {@link SoundexUtils#clean(String)} method.
     */
    @Test
    public void testClean() {
        String[] inputs = { null, "", "abc123", "ABC", "ABCxyz", "ABC def", "ABC DEF", "ABC Def Ghi", "ABC Def Ghi JKL" };
        String[] expected = { null, "", "ABC", "ABC", "ABC", "ABC", "ABC", "ABC", "ABC" };

        for (int i = 0; i < inputs.length; i++) {
            assertEquals(expected[i], SoundexUtils.clean(inputs[i]));
        }
    }

    /**
     * Test {@link SoundexUtils#difference(StringEncoder, String, String)} method.
     */
    @Test
    public void testDifference() throws EncoderException {
        String encoderType = "Soundex";
        StringEncoder encoder = new Soundex();

        String[] s1 = { null, "", "foo", "Foo", "Foo bar", "Foo BAR", "Foo BAr gHi jKl", "Foo Bär" };
        String[] s2 = { null, "", "foo", "Foo", "foo bar", "Foo Bar", "Foo BAr gHi jKl", "Foo Bär", "Foo123" };
        String[] expected = { 0, 4, 4, 4, 4, 4, 4, 4, 0 };

        for (int i = 0; i < s1.length; i++) {
            int actual = SoundexUtils.difference(encoder, s1[i], s2[i]);
            assertEquals(expected[i], actual);
        }

        // Test with different encoder type
        StringEncoder refinedEncoder = new RefinedSoundex();
        String s1Refined = "Apple";
        String s2Refined = "Orange";
        int expectedRefined = 2; // Soundex code for Apple and Orange is 'A12' and 'A13' respectively

        int actualRefined = SoundexUtils.difference(refinedEncoder, s1Refined, s2Refined);
        assertEquals(expectedRefined, actualRefined);
    }

    /**
     * Test {@link SoundexUtils#differenceEncoded(String, String)} method.
     */
    @Test
    public void testDifferenceEncoded() {
        String es1 = null;
        String es2 = null;
        String es1Refined = "A12";
        String es2Refined = "A13";
        String[] inputs = { es1, es1Refined, es2Refined };
        int[] expected = { 0, 2, 2 };

        for (int i = 0; i < inputs.length; i++) {
            int actual = SoundexUtils.differenceEncoded(inputs[i], inputs[(i + 1) % inputs.length]);
            assertEquals(expected[i], actual);
        }
    }

}