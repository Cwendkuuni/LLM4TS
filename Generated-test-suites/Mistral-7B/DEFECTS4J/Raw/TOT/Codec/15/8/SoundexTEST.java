package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import org.junit.Assert.*;

/**
 * Test class for Soundex encoder.
 */
public class SoundexTest {

    /**
     * Test encoding of single letters.
     */
    @Test
    public void testSingleLetters() {
        Soundex encoder = Soundex.US_ENGLISH;
        // Test encoding of English letters
        for (char c = 'A'; c <= 'Z'; c++) {
            String encoded = encoder.encode(Character.toString(c));
            char expected = Soundex.US_ENGLISH_MAPPING[c - 'A'];
            assertEquals(String.valueOf(expected), encoded);
        }
        // Test encoding of non-English letters
        for (char c = 'À'; c <= 'Ö'; c++) {
            String encoded = encoder.encode(Character.toString(c));
            if (Soundex.US_ENGLISH_MAPPING[c - 'A'] == '0') {
                assertEquals("", encoded);
            } else {
                char expected = Soundex.US_ENGLISH_MAPPING[c - 'A'];
                assertEquals(String.valueOf(expected), encoded);
            }
        }
    }

    /**
     * Test encoding of simple words.
     */
    @Test
    public void testSimpleWords() {
        Soundex encoder = Soundex.US_ENGLISH;
        String[] words = {"Apple", "Banana", "Cat", "Dog", "Elephant", "Fish", "Grapes", "Hippopotamus", "Iguana",
                "Jaguar", "Kangaroo", "Lion", "Monkey", "Nightingale", "Octopus", "Penguin", "Quail", "Rhinoceros",
                "Tiger", "Umbrella", "Vulture", "Watermelon", "Xylophone", "Yak", "Zebra"};
        for (String word : words) {
            String encoded = encoder.encode(word);
            String expected = SoundexUtils.soundex(word.toCharArray(), encoder.getSoundexMapping());
            assertEquals(expected, encoded);
        }
    }

    /**
     * Test encoding of words with same soundex code.
     */
    @Test
    public void testWordsWithSameSoundexCode() {
        Soundex encoder = Soundex.US_ENGLISH;
        String[] words1 = {"Apple", "Apricot", "Orange"};
        for (String word1 : words1) {
            String soundex1 = encoder.encode(word1);
            String[] words2 = {"Apricot", "Orange", "Mango"};
            for (String word2 : words2) {
                String soundex2 = encoder.encode(word2);
                int difference = encoder.difference(word1, word2);
                assertEquals(soundex1, soundex2);
                assertEquals(difference, 4);
            }
        }
    }

    /**
     * Test encoding of words with different soundex codes.
     */
    @Test
    public void testWordsWithDifferentSoundexCodes() {
        Soundex encoder = Soundex.US_ENGLISH;
        String[] words1 = {"Apple", "Apricot", "Orange"};
        for (String word1 : words1) {
            String soundex1 = encoder.encode(word1);
            String[] words2 = {"Banana", "Watermelon", "Grapes"};
            for (String word2 : words2) {
                String soundex2 = encoder.encode(word2);
                int difference = encoder.difference(word1, word2);
                assertNotEquals(soundex1, soundex2);
                assertEquals(difference, 0);
            }
        }
    }

    /**
     * Test encoding with null argument.
     */
    @Test(expected = EncoderException.class)
    public void testNullArgument() {
        Soundex encoder = Soundex.US_ENGLISH;
        encoder.encode(null);
    }

    /**
     * Test encoding with empty string.
     */
    @Test
    public void testEmptyString() {
        Soundex encoder = Soundex.US_ENGLISH;
        String encoded = encoder.encode("");
        assertEquals("", encoded);
    }
}