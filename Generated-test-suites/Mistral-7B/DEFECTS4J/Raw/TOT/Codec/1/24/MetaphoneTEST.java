package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.*;

public class MetaphoneTest {

    @Test
    public void testMetaphoneEncodesSingleCharacter() {
        Metaphone encoder = new Metaphone();
        String input = "A";
        String expectedOutput = "A";
        String actualOutput = encoder.metaphone(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testMetaphoneEncodesTwoCharactersWithException() {
        Metaphone encoder = new Metaphone();
        String input = null;
        try {
            String actualOutput = encoder.metaphone(input);
            fail("Expected EncoderException for null input");
        } catch (EncoderException e) {
            // Expected
        }
        String input2 = "";
        try {
            String actualOutput = encoder.metaphone(input2);
            fail("Expected EncoderException for empty input");
        } catch (EncoderException e) {
            // Expected
        }
    }

    @Test
    public void testMetaphoneEncodesTwoVowels() {
        Metaphone encoder = new Metaphone();
        String input = "AI";
        String expectedOutput = "A1";
        String actualOutput = encoder.metaphone(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testMetaphoneEncodesTwoConsonants() {
        Metaphone encoder = new Metaphone();
        String input = "AB";
        String expectedOutput = "B";
        String actualOutput = encoder.metaphone(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testMetaphoneEncodesInitialKN() {
        Metaphone encoder = new Metaphone();
        String input = "KN";
        String expectedOutput = "K";
        String actualOutput = encoder.metaphone(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testMetaphoneEncodesInitialAE() {
        Metaphone encoder = new Metaphone();
        String input = "AE";
        String expectedOutput = "A";
        String actualOutput = encoder.metaphone(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testMetaphoneEncodesInitialWH() {
        Metaphone encoder = new Metaphone();
        String input = "WH";
        String expectedOutput = "W";
        String actualOutput = encoder.metaphone(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testMetaphoneEncodesInitialX() {
        Metaphone encoder = new Metaphone();
        String input = "X";
        String expectedOutput = "S";
        String actualOutput = encoder.metaphone(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testMetaphoneEncodesInitialK() {
        Metaphone encoder = new Metaphone();
        String input = "K";
        String expectedOutput = "K";
        String actualOutput = encoder.metaphone(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testMetaphoneEncodesInitialWR() {
        Metaphone encoder = new Metaphone();
        String input = "WR";
        String expectedOutput = "R";
        String actualOutput = encoder.metaphone(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testMetaphoneEncodesInitialCH() {
        Metaphone encoder = new Metaphone();
        String input = "CH";
        String expectedOutput = "K";
        String actualOutput = encoder.metaphone(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testMetaphoneEncodesInitialDGE() {
        Metaphone encoder = new Metaphone();
        String input = "DGE";
        String expectedOutput = "J";
        String actualOutput = encoder.metaphone(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testMetaphoneEncodesInitialG() {
        Metaphone encoder = new Metaphone();
        String input = "G";
        try {
            String actualOutput = encoder.metaphone(input);
            fail("Expected exception: G is a silent letter at the end or before a consonant");
        } catch (EncoderException e) {
            // Expected
        }
    }

    @Test
    public void testMetaphoneEncodesInitialSH() {
        Metaphone encoder = new Metaphone();
        String input = "SH";
        String expectedOutput = ""; // Silent
        String actualOutput = encoder.metaphone(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testMetaphoneEncodesInitialTIA() {
        Metaphone encoder = new Metaphone();
        String input = "TIA";
        String expectedOutput = "X";
        String actualOutput = encoder.metaphone(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testMetaphoneEncodesInitialTH() {
        Metaphone encoder = new Metaphone();
        String input = "TH";
        String expectedOutput = "0"; // Represents theta
        String actualOutput = encoder.metaphone(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testMetaphoneEncodesMultipleCharacters() {
        Metaphone encoder = new Metaphone();
        String input = "APPLE";
        String expectedOutput = "A1P1L1E";
        String actualOutput = encoder.metaphone(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testMetaphoneEncodesMultipleVowels() {
        Metaphone encoder = new Metaphone();
        String input = "APPLEEE";
        String expectedOutput = "A1P1L1E1E1";
        String actualOutput = encoder.metaphone(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testMetaphoneEncodesMultipleConsonants() {
        Metaphone encoder = new Metaphone();
        String input = "BANANA";
        String expectedOutput = "B1N1N1A";
        String actualOutput = encoder.metaphone(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testMetaphoneEncodesMultipleVowelsAndConsonants() {
        Metaphone encoder = new Metaphone();
        String input = "APPLEEAPPLE";
        String expectedOutput = "A1P1L1E1A1P1L1E1";
        String actualOutput = encoder.metaphone(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testMetaphoneEncodesStringWithSpecialCharacters() {
        Metaphone encoder = new Metaphone();
        String input = "APPLE!@#$%^&*()_";
        String expectedOutput = "A1P1L1E";
        String actualOutput = encoder.metaphone(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testMetaphoneEncodesStringWithNumbers() {
        Metaphone encoder = new Metaphone();
        String input = "APPLE12345";
        String expectedOutput = "A1P1L1E";
        String actualOutput = encoder.metaphone(input);
        assertEquals(expectedOutput, actualOutput);
    }
}