package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.Metaphone;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for Metaphone class.
 */
public class MetaphoneTest {

    private static final Metaphone METAPHONE = new Metaphone();

    /**
     * Test method for {@link Metaphone#metaphone(String)} with empty string.
     */
    @Test
    public void testMetaphoneEmptyString() {
        Assert.assertEquals("", METAPHONE.metaphone(""));
    }

    /**
     * Test method for {@link Metaphone#metaphone(String)} with single character string.
     */
    @Test
    public void testMetaphoneSingleCharacter() {
        List<String> expected = Arrays.asList("B", "F", "H", "J", "K", "P", "Q", "S", "T", "V", "W", "X", "Z");
        for (char c = 'A'; c <= 'Z'; c++) {
            String actual = METAPHONE.metaphone(Character.toString(c));
            Assert.assertEquals("Unexpected metaphone code for character: " + c, expected.get(c - 'A'), actual);
        }
    }

    /**
     * Test method for {@link Metaphone#metaphone(String)} with some common test cases.
     */
    @Test
    public void testMetaphoneCommonCases() {
        List<String> testCases = Arrays.asList("cat", "bat", "tomato", "tomorrow", "apple", "banana", "orange", "elephant", "python");

        for (String txt : testCases) {
            String actual = METAPHONE.metaphone(txt);
            String expected = null;
            switch (txt) {
                case "cat":
                    expected = "K1T";
                    break;
                case "bat":
                    expected = "B1T";
                    break;
                case "tomato":
                    expected = "T2M1T";
                    break;
                case "tomorrow":
                    expected = "T2M1R1";
                    break;
                case "apple":
                    expected = "A1P1L1";
                    break;
                case "banana":
                    expected = "M1N1N1";
                    break;
                case "orange":
                    expected = "O2R1J1";
                    break;
                case "elephant":
                    expected = "E1L1F1P1T";
                    break;
                case "python":
                    expected = "P1F1T1";
                    break;
            }
            Assert.assertEquals("Unexpected metaphone code for text: " + txt, expected, actual);
        }
    }

    /**
     * Test method for {@link Metaphone#metaphone(String)} with some edge cases and exceptions.
     */
    @Test(expected = EncoderException.class)
    public void testMetaphoneEdgeCasesAndExceptions() {
        METAPHONE.metaphone(null);
        METAPHONE.metaphone("");
        String text = "apple banana";
        METAPHONE.metaphone(text); // test encoding multiple words as a single string
        String invalidText = "apple1banana";
        METAPHONE.metaphone(invalidText); // test string with non-ASCII character
    }
}