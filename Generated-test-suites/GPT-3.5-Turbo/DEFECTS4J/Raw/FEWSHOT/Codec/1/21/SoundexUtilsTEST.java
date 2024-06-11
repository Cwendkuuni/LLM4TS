package org.apache.commons.codec.language;

import static org.junit.Assert.*;

import org.apache.commons.codec.EncoderException;
import org.junit.BeforeClass;
import org.junit.Test;

public class SoundexUtilsTest {

    private static Soundex soundex;
    private static RefinedSoundex refinedSoundex;

    @BeforeClass
    public static void setUp() {
        soundex = new Soundex();
        refinedSoundex = new RefinedSoundex();
    }

    @Test
    public void testClean_NullInput() {
        assertNull(SoundexUtils.clean(null));
    }

    @Test
    public void testClean_EmptyInput() {
        assertEquals("", SoundexUtils.clean(""));
    }

    @Test
    public void testClean_AllNonLetterCharacters() {
        // Only non-letter characters, should return empty string
        assertEquals("", SoundexUtils.clean("123 #$%"));
    }

    @Test
    public void testClean_SingleLetterUppercase() {
        assertEquals("A", SoundexUtils.clean("A"));
    }

    @Test
    public void testClean_SingleLetterLowercase() {
        assertEquals("A", SoundexUtils.clean("a"));
    }

    @Test
    public void testClean_MultipleLetterUppercase() {
        assertEquals("ABCD", SoundexUtils.clean("ABCD"));
    }

    @Test
    public void testClean_MultipleLetterLowercase() {
        assertEquals("ABCD", SoundexUtils.clean("abcd"));
    }

    @Test
    public void testClean_MixedLettersUppercaseAndLowercase() {
        assertEquals("ABCD", SoundexUtils.clean("aBcD"));
    }

    @Test
    public void testClean_StringWithNonLetterCharacters() {
        // String with non-letter characters, should remove non-letter characters and upper case the remaining letters
        assertEquals("ABCD", SoundexUtils.clean("a12 b#$ c48d %^&"));
    }

    @Test
    public void testDifference_NullEncoder() throws EncoderException {
        assertEquals(0, SoundexUtils.difference(null, "ABC", "DEF"));
    }

    @Test
    public void testDifference_NullInput() throws EncoderException {
        assertEquals(0, SoundexUtils.difference(soundex, null, null));
    }

    @Test
    public void testDifference_OneNullInput() throws EncoderException {
        assertEquals(0, SoundexUtils.difference(soundex, "ABC", null));
        assertEquals(0, SoundexUtils.difference(soundex, null, "DEF"));
    }

    @Test
    public void testDifference_EmptyInput() throws EncoderException {
        assertEquals(0, SoundexUtils.difference(soundex, "", ""));
    }

    @Test
    public void testDifference_SameInput() throws EncoderException {
        // Same input string, should return the length of the string
        assertEquals(3, SoundexUtils.difference(soundex, "AAA", "AAA"));
    }

    @Test
    public void testDifference_DifferentInput() throws EncoderException {
        // Different input string, should return the number of characters that are the same
        assertEquals(1, SoundexUtils.difference(soundex, "ABC", "DEF"));
    }

    @Test
    public void testDifference_DifferentInputWithSameStart() throws EncoderException {
        // Different input string with same starting characters, should return the number of characters that are the same
        assertEquals(3, SoundexUtils.difference(soundex, "ABC123", "DEF567"));
    }

    @Test
    public void testDifference_DifferentInputWithDifferentLengths() throws EncoderException {
        // Different input strings with different lengths, should return the number of characters that are the same up to the length of the shorter string
        assertEquals(3, SoundexUtils.difference(soundex, "ABC", "DEF123"));
    }

    @Test
    public void testDifferenceEncoded_NullInput() {
        assertEquals(0, SoundexUtils.differenceEncoded(null, null));
    }

    @Test
    public void testDifferenceEncoded_EmptyInput() {
        assertEquals(0, SoundexUtils.differenceEncoded("", ""));
    }

    @Test
    public void testDifferenceEncoded_SameInput() {
        // Same input string, should return the length of the string
        assertEquals(4, SoundexUtils.differenceEncoded("A123", "A123"));
    }

    @Test
    public void testDifferenceEncoded_DifferentInput() {
        // Different input string, should return the number of characters that are the same
        assertEquals(1, SoundexUtils.differenceEncoded("A123", "B456"));
    }

    @Test
    public void testDifferenceEncoded_DifferentInputWithSameStart() {
        // Different input string with same starting characters, should return the number of characters that are the same
        assertEquals(3, SoundexUtils.differenceEncoded("ABC", "DEF"));
    }

    @Test
    public void testDifferenceEncoded_DifferentInputWithDifferentLengths() {
        // Different input strings with different lengths, should return the number of characters that are the same up to the length of the shorter string
        assertEquals(3, SoundexUtils.differenceEncoded("ABC", "DEF123"));
    }
}