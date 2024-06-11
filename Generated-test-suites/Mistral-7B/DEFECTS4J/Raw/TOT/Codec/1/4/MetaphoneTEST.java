package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for Metaphone class
 */
public class MetaphoneTest {

    private Metaphone metaphone = new Metaphone();

    /**
     * Test for empty string
     */
    @Test
    public void testEmptyString() {
        String actual = metaphone.metaphone("");
        Assert.assertEquals("", actual);
    }

    /**
     * Test for single character string
     */
    @Test
    public void testSingleCharacterString() {
        for (char c = 'A'; c <= 'Z'; c++) {
            String actual = metaphone.metaphone(Character.toString(c));
            String expected = Character.toString(c).toUpperCase();
            Assert.assertEquals(expected, actual);
        }
    }

    /**
     * Test for simple words
     */
    @Test
    public void testSimpleWords() {
        String[] testWords = {"APPLE", "BANANA", "CAT", "DOG", "ELEPHANT", "FISH", "GIRAFFE", "HIPPO", "IGLOO", "JACKAL", "KANGAROO", "LION", "MISTER", "NIGHT", "ORANGUTAN", "POTATO", "QUIZ", "RABBIT", "SHEEP", "TIGER", "UNICORN", "VAMPIRE", "WOLF", "XYZ", "YAK", "ZOOM"};

        for (String word : testWords) {
            String actual = metaphone.metaphone(word);
            String expected = MetaphoneTest.getExpectedCode(word);
            Assert.assertEquals(expected, actual);
        }
    }

    /**
     * Test for special cases
     */
    @Test
    public void testSpecialCases() {
        String testString = "A BANDAGEable man, with a KANGAROO, playing a HARP, in the BUSH, at NIGHT.";
        String actual = metaphone.metaphone(testString);
        String expected = "B1NJ AEDG AB, W K1NGG R00, PIHG AH1 R, AT N1GH.";
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test for error scenarios
     */
    @Test(expected = EncoderException.class)
    public void testErrorScenarios() {
        metaphone.metaphone(null);
    }

    /**
     * Test for maxCodeLen
     */
    @Test
    public void testMaxCodeLen() {
        Metaphone metaphoneWithMaxCodeLen5 = new Metaphone();
        Metaphone metaphoneWithMaxCodeLen6 = new Metaphone();
        metaphoneWithMaxCodeLen5.setMaxCodeLen(5);
        metaphoneWithMaxCodeLen6.setMaxCodeLen(6);

        String testString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String actualWithMaxCodeLen5 = metaphoneWithMaxCodeLen5.metaphone(testString);
        String actualWithMaxCodeLen6 = metaphoneWithMaxCodeLen6.metaphone(testString);

        Assert.assertEquals("A2B2C2D2E2F2G2H2I2J2K2L2M2N2O2P2Q2R2S2T2U2V2W2X2Y2Z2", actualWithMaxCodeLen5);
        Assert.assertEquals("A2B2C2D2E2F2G2H2I2J2K2L2M2N2O2P2Q2R2S2T2U2V2W2X2Y2Z3", actualWithMaxCodeLen6);
    }

    /**
     * Helper method to get expected metaphone code for a given word
     */
    private static String getExpectedCode(String word) {
        switch (word.charAt(0)) {
            case 'K':
                if (word.length() > 1 && word.charAt(1) == 'N') {
                    return "KN" + getExpectedCode(word.substring(2));
                } else {
                    return word.charAt(0) + "" + getExpectedCode(word.substring(1));
                }
            case 'A':
                if (word.length() > 1 && word.charAt(1) == 'E') {
                    return "AE" + getExpectedCode(word.substring(2));
                } else {
                    return word.charAt(0) + "" + getExpectedCode(word.substring(1));
                }
            default:
                return word.charAt(0) + "" + getExpectedCode(word.substring(1));
        }
    }

}