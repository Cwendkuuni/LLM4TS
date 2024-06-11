package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.Soundex;
import org.apache.commons.codec.language.SoundexUtils;

public class SoundexTest {

    @Test
    public void testUS_ENGLISH() {
        Soundex soundex = Soundex.US_ENGLISH;
        String[] testStrings = new String[]{"apple", "appel", "apfel", "ape", "about", "above", "amigo", "angel", "einstein", "empire", "eternity", "everest"};
        String[] expectedSoundexCodes = new String[]{"A123", "A123", "A201", "A100", "A010", "A011", "A161", "A131", "A16261", "A022", "A02101", "A02032"};

        for (int i = 0; i < testStrings.length; i++) {
            String actualSoundexCode = null;
            try {
                actualSoundexCode = soundex.encode(testStrings[i]);
            } catch (EncoderException e) {
                e.printStackTrace();
            }
            assertEquals(expectedSoundexCodes[i], actualSoundexCode);
        }
    }

    @Test
    public void testCustomMapping() {
        char[] customMapping = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        Soundex customSoundex = new Soundex(customMapping);
        String[] testStrings = new String[]{"apple", "appel", "apfel", "ape", "about", "above", "amigo", "angel", "einstein", "empire", "eternity", "everest"};
        String[] expectedSoundexCodes = new String[]{"A123", "A123", "A201", "A100", "A010", "A011", "A161", "A131", "A16261", "A022", "A02101", "A02032"};

        for (int i = 0; i < testStrings.length; i++) {
            String actualSoundexCode = null;
            try {
                actualSoundexCode = customSoundex.encode(testStrings[i]);
            } catch (EncoderException e) {
                e.printStackTrace();
            }
            assertEquals(expectedSoundexCodes[i], actualSoundexCode);
        }

        // Test with a non-English character
        String nonEnglishString = "über";
        String actualNonEnglishSoundexCode = null;
        try {
            actualNonEnglishSoundexCode = customSoundex.encode(nonEnglishString);
        } catch (EncoderException e) {
            e.printStackTrace();
        }
        assertEquals("U211", actualNonEnglishSoundexCode);
    }

    @Test
    public void testDifference() throws EncoderException {
        Soundex soundex = new Soundex();
        String[] testStrings = new String[]{"apple", "appel", "apfel", "ape", "about", "above", "amigo", "angel", "einstein", "empire", "eternity", "everest"};
        String[] comparisonStrings = new String[]{"apple", "appel", "apfel", "ape", "about", "above", "amigo", "angel", "einstein", "empire", "eternity", "everest"};
        int[] expectedDifferences = new int[]{3, 3, 3, 0, 1, 1, 3, 2, 4, 2, 4, 4};

        for (int i = 0; i < testStrings.length; i++) {
            int actualDifference = soundex.difference(testStrings[i], comparisonStrings[i]);
            assertEquals(expectedDifferences[i], actualDifference);
        }

        // Test with different string lengths
        String shorterString = "apple";
        String longerString = "the quick brown fox jumps over the lazy dog";
        int actualDifference = soundex.difference(shorterString, longerString);
        assertEquals(4, actualDifference);
    }
}