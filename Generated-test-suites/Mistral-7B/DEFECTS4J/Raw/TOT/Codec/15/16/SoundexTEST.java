package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for Soundex class.
 */
public class SoundexTest {

    /**
     * Test encoding a single letter.
     */
    @Test
    public void testSingleLetter() {
        Soundex soundex = new Soundex();
        String expected = "A";
        String actual = soundex.soundex("A");
        assertEquals(expected, actual);
    }

    /**
     * Test encoding multiple letters with the same code.
     */
    @Test
    public void testSameCodeLetters() {
        Soundex soundex = new Soundex();
        String expected = "B3";
        String actual = soundex.soundex("BITE, BITE");
        assertEquals(expected, actual);
    }

    /**
     * Test encoding multiple letters with different codes.
     */
    @Test
    public void testDifferentCodesLetters() {
        Soundex soundex = new Soundex();
        String expected = "B123";
        String actual = soundex.soundex("BAB, ABAB");
        assertEquals(expected, actual);
    }

    /**
     * Test encoding an empty string.
     */
    @Test
    public void testEmptyString() {
        Soundex soundex = new Soundex();
        String actual = soundex.soundex("");
        assertEquals("", actual);
    }

    /**
     * Test encoding null string.
     */
    @Test(expected = EncoderException.class)
    public void testNullString() {
        Soundex soundex = new Soundex();
        soundex.soundex(null);
    }

    /**
     * Test encoding a string with non-alphabetic characters.
     */
    @Test
    public void testNonAlphabeticCharacters() {
        Soundex soundex = new Soundex();
        String expected = "N0";
        String actual = soundex.soundex("Number123");
        assertEquals(expected, actual);
    }

    /**
     * Test encoding a string with special characters at the beginning.
     */
    @Test
    public void testSpecialCharactersBeginning() {
        Soundex soundex = new Soundex();
        String expected = "D2";
        String actual = soundex.soundex("The quick brown fox jumps over the lazy dog");
        assertEquals(expected, actual);
    }

    /**
     * Test encoding a string with special characters at the end.
     */
    @Test
    public void testSpecialCharactersEnd() {
        Soundex soundex = new Soundex();
        String expected = "S2";
        String actual = soundex.soundex("The quick brown fox jumps over the lazy dog!");
        assertEquals(expected, actual);
    }

    /**
     * Test encoding a string with special characters in the middle.
     */
    @Test
    public void testSpecialCharactersMiddle() {
        Soundex soundex = new Soundex();
        String expected = "S2";
        String actual = soundex.soundex("The quick brown fox jumps over the lazy dog#");
        assertEquals(expected, actual);
    }

    /**
     * Test encoding a string with repeat letters.
     */
    @Test
    public void testRepeatLetters() {
        Soundex soundex = new Soundex();
        String expected = "B1";
        String actual = soundex.soundex("BABABA");
        assertEquals(expected, actual);
    }

    /**
     * Test encoding a string with multiple consonant clusters.
     */
    @Test
    public void testConsonantClusters() {
        Soundex soundex = new Soundex();
        String expected = "C3";
        String actual = soundex.soundex("CITIZEN, CITIZENSHIP");
        assertEquals(expected, actual);
    }

    /**
     * Test encoding a string with vowels only.
     */
    @Test
    public void testVowelsOnly() {
        Soundex soundex = new Soundex();
        String expected = "A000";
        String actual = soundex.soundex("AAA, AAA");
        assertEquals(expected, actual);
    }
}