package org.apache.commons.codec.language;

import static org.junit.Assert.*;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.SoundexUtils;

/**
 * Test class for SoundexUtils class.
 */
public class SoundexUtilsTest {

    /**
     * Test method for {@link SoundexUtils#clean(String)}.
     */
    @Test
    public void testClean() {
        String input = "aBc Def gHiJkl mNoPqRs T";
        String expected = "ABC DEF GHIJKL MNOPQRS";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link SoundexUtils#clean(String)} with empty string.
     */
    @Test
    public void testCleanEmptyString() {
        String result = SoundexUtils.clean(null);
        assertEquals("", result);

        result = SoundexUtils.clean("");
        assertEquals("", result);
    }

    /**
     * Test method for {@link SoundexUtils#clean(String)} with null character.
     */
    @Test
    public void testCleanNullCharacter() {
        String input = "a\0Bc";
        String expected = "ABC";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link SoundexUtils#difference(StringEncoder, String, String)}
     * with Soundex encoder.
     */
    @Test
    public void testDifferenceSoundexEncoder() throws EncoderException {
        StringEncoder encoder = new SoundexEncoder();
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
     * Test method for {@link SoundexUtils#difference(StringEncoder, String, String)}
     * with RefinedSoundex encoder.
     */
    @Test
    public void testDifferenceRefinedSoundexEncoder() throws EncoderException {
        StringEncoder encoder = new RefinedSoundexEncoder();
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
        s2 = "Apfel";
        expected = 3;
        result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link SoundexUtils#differenceEncoded(String, String)} with
     * two empty Soundex encoded strings.
     */
    @Test
    public void testDifferenceEncodedEmptyStrings() {
        String es1 = "";
        String es2 = "";
        int expected = 0;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link SoundexUtils#differenceEncoded(String, String)} with
     * two Soundex encoded strings of different lengths.
     */
    @Test
    public void testDifferenceEncodedDifferentLengths() {
        String es1 = "A1B2C3";
        String es2 = "A1B";
        int expected = 2;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link SoundexUtils#differenceEncoded(String, String)} with
     * two identical Soundex encoded strings.
     */
    @Test
    public void testDifferenceEncodedIdenticalStrings() {
        String es1 = "A1B2C3";
        String es2 = "A1B2C3";
        int expected = 4;
        int result = SoundexUtils.differenceEncoded(es1, es2);
        assertEquals(expected, result);
    }
}