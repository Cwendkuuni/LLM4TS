package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link SoundexUtils} class.
 */
public class SoundexUtilsTest {

    /**
     * Test cleaning method with null string.
     */
    @Test
    public void testCleanNullString() {
        String result = SoundexUtils.clean(null);
        assertEquals("", result);
    }

    /**
     * Test cleaning method with empty string.
     */
    @Test
    public void testCleanEmptyString() {
        String result = SoundexUtils.clean("");
        assertEquals("", result);
    }

    /**
     * Test cleaning method with single letter.
     */
    @Test
    public void testCleanSingleLetter() {
        String result = SoundexUtils.clean("A");
        assertEquals("A", result);
    }

    /**
     * Test cleaning method with multiple letters.
     */
    @Test
    public void testCleanMultipleLetters() {
        String result = SoundexUtils.clean("HelloWorld");;
        assertEquals("HLRO", result);
    }

    /**
     * Test cleaning method with digits and special characters.
     */
    @Test
    public void testCleanWithDigitsAndSpecialCharacters() {
        String result = SoundexUtils.clean("Test123!@#$%^&*()_+");
        assertEquals("", result);
    }

    /**
     * Test cleaning method with mixed digits and letters.
     */
    @Test
    public void testCleanMixedDigitsAndLetters() {
        String result = SoundexUtils.clean("Test123A");
        assertEquals("T", result);
    }

    /**
     * Test difference method with null strings.
     */
    @Test(expected = NullPointerException.class)
    public void testDifferenceNullStrings() {
        SoundexUtils.difference(null, null, null);
    }

    /**
     * Test difference method with null encoder.
     */
    @Test(expected = NullPointerException.class)
    public void testDifferenceNullEncoder() {
        SoundexUtils encoder = null;
        String s1 = "Apple";
        String s2 = "Banana";
        int result = SoundexUtils.difference(encoder, s1, s2);
    }

    /**
     * Test difference method with empty strings.
     */
    @Test
    public void testDifferenceEmptyStrings() {
        SoundexUtils encoder = new SoundexUtils();
        int result = SoundexUtils.difference(encoder, "", "", "");
        assertEquals(0, result);
    }

    /**
     * Test difference method with identical strings.
     */
    @Test
    public void testDifferenceIdenticalStrings() {
        SoundexUtils encoder = new SoundexUtils();
        String s1 = "Apple";
        String s2 = "Apple";
        int result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(4, result);
    }

    /**
     * Test difference method with different encoded strings.
     */
    @Test
    public void testDifferenceDifferentEncodedStrings() {
        SoundexUtils encoder = new SoundexUtils();
        String s1 = "Apple";
        String s2 = "Banana";
        int result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(0, result);
    }

    /**
     * Test difference method with different encoded strings with some similarity.
     */
    @Test
    public void testDifferenceDifferentEncodedStringsWithSomeSimilarity() {
        SoundexUtils encoder = new SoundexUtils();
        String s1 = "Apple";
        String s2 = "Apricot";
        int result = SoundexUtils.difference(encoder, s1, s2);
        assertEquals(2, result);
    }

    /**
     * Test difference method with Soundex encoded strings.
     */
    @Test
    public void testDifferenceSoundexEncodedStrings() throws EncoderException {
        SoundexUtils encoder = new SoundexUtils();
        String s1 = encoder.encode("Apple");
        String s2 = encoder.encode("Appleton");
        int result = SoundexUtils.differenceEncoded(s1, s2);
        assertEquals(3, result);
    }

    /**
     * Test difference method with Refined Soundex encoded strings.
     */
    @Test
    public void testDifferenceRefinedSoundexEncodedStrings() throws EncoderException {
        SoundexUtils encoder = new SoundexUtils();
        String s1 = encoder.encode("Apple");
        String s2 = encoder.encode("Appleton");
        String refinedS1 = encoder.refine(s1);
        String refinedS2 = encoder.refine(s2);
        int result = SoundexUtils.differenceEncoded(refinedS1, refinedS2);
        assertEquals(4, result);
    }

}