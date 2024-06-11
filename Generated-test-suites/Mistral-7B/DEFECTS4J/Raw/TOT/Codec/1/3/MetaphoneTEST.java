package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Test cases for Metaphone class
 */
public class MetaphoneTest {

    private static final Metaphone metaphone = new Metaphone();

    /**
     * Test for encoding a single character string
     */
    @Test
    public void testSingleCharacter() {
        String input = "A";
        String expectedOutput = "A";
        String actualOutput = metaphone.metaphone(input);
        assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Test for encoding a two character string
     */
    @Test
    public void testTwoCharacters() {
        String input = "BA";
        String expectedOutput = "B";
        String actualOutput = metaphone.metaphone(input);
        assertEquals(expectedOutput, actualOutput);

        input = "CH";
        expectedOutput = "X";
        actualOutput = metaphone.metaphone(input);
        assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Test for encoding a string with initial exceptions
     */
    @Test
    public void testInitialExceptions() {
        String input = "KN";
        String expectedOutput = "K";
        String actualOutput = metaphone.metaphone(input);
        assertEquals(expectedOutput, actualOutput);

        input = "AE";
        expectedOutput = "A";
        actualOutput = metaphone.metaphone(input);
        assertEquals(expectedOutput, actualOutput);

        input = "WR";
        expectedOutput = "W";
        actualOutput = metaphone.metaphone(input);
        assertEquals(expectedOutput, actualOutput);

        input = "WH";
        expectedOutput = "W";
        actualOutput = metaphone.metaphone(input);
        assertEquals(expectedOutput, actualOutput);

        input = "X";
        expectedOutput = "S";
        actualOutput = metaphone.metaphone(input);
        assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Test for encoding a string with duplicate letters
     */
    @Test
    public void testDuplicateLetters() {
        String input = "BAB";
        String expectedOutput = "B";
        String actualOutput = metaphone.metaphone(input);
        assertEquals(expectedOutput, actualOutput);

        input = "CC";
        expectedOutput = "K";
        actualOutput = metaphone.metaphone(input);
        assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Test for encoding a string with special cases
     */
    @Test
    public void testSpecialCases() {
        String input = "SCIENCE";
        String expectedOutput = "SXNTS";
        String actualOutput = metaphone.metaphone(input);
        assertEquals(expectedOutput, actualOutput);

        input = "SCHOOL";
        expectedOutput = "SKL";
        actualOutput = metaphone.metaphone(input);
        assertEquals(expectedOutput, actualOutput);

        input = "DIGIT";
        expectedOutput = "JT";
        actualOutput = metaphone.metaphone(input);
        assertEquals(expectedOutput, actualOutput);

        input = "BAG";
        expectedOutput = "B";
        actualOutput = metaphone.metaphone(input);
        assertEquals(expectedOutput, actualOutput);

        input = "CHAINS";
        expectedOutput = "XS";
        actualOutput = metaphone.metaphone(input);
        assertEquals(expectedOutput, actualOutput);

        input = "CHEESE";
        expectedOutput = "SZ";
        actualOutput = metaphone.metaphone(input);
        assertEquals(expectedOutput, actualOutput);

        input = "BETTER";
        expectedOutput = "BTR";
        actualOutput = metaphone.metaphone(input);
        assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Test for encoding a string with punctuation and numbers
     */
    @Test
    public void testPunctuationAndNumbers() {
        String input = "Apple, Banana! 123";
        String expectedOutput = "PPL";
        String actualOutput = metaphone.metaphone(input);
        assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Test for encoding a string with multiple words
     */
    @Test
    public void testMultipleWords() {
        String input = "Hello World";
        String expectedOutput = "HLWR";
        String actualOutput = metaphone.metaphone(input);
        assertEquals(expectedOutput, actualOutput);

        input = "The quick brown fox jumps over the lazy dog";
        expectedOutput = "TQBFRS JMPV HR DG";
        actualOutput = metaphone.metaphone(input);
        assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Test for encoding a string with initial capital letter
     */
    @Test
    public void testInitialCapitalLetter() {
        String input = "Hello";
        String expectedOutput = "HLWR";
        String actualOutput = metaphone.metaphone(input);
        assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Test for encoding a string with all capital letters
     */
    @Test
    public void testAllCapitalLetters() {
        String input = "HELLO";
        String expectedOutput = "HLWR";
        String actualOutput = metaphone.metaphone(input);
        assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Test for encoding an empty string
     */
    @Test
    public void testEmptyString() {
        String input = "";
        String expectedOutput = "";
        String actualOutput = metaphone.metaphone(input);
        assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Test for encoding a null string
     */
    @Test(expected = EncoderException.class)
    public void testNullString() {
        String input = null;
        String expectedOutput = ""; // Not applicable
        Metaphone metaphoneInstance = new Metaphone(); // Create a new instance to avoid using the static one
        String actualOutput = metaphoneInstance.metaphone(input);
        assertEquals(expectedOutput, actualOutput); // This line won't be executed due to expected exception
    }
}