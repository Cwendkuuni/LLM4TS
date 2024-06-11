package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import org.junit.Assert.*;

public class MetaphoneTest {

    @Test
    public void testMetaphoneEmptyString() {
        Metaphone metaphone = new Metaphone();
        assertEquals("", metaphone.metaphone(""));
    }

    @Test
    public void testMetaphoneSingleCharacter() {
        Metaphone metaphone = new Metaphone();
        for (char c = 'A'; c <= 'Z'; c++) {
            String result = metaphone.metaphone(Character.toString(c));
            assertEquals(Character.toString(c), result);
        }
    }

    @Test
    public void testMetaphoneBasicCases() {
        Metaphone metaphone = new Metaphone();
        String[] inputs = {"APPLE", "BANANA", "CAT", "DOG", "ELEPHANT", "GIRAFFE"};
        String[] expectedResults = {"A1P1L1E1", "B2N1N1A1", "K1T1", "D1OG1", "I1L1P1H1T1", "H1R1F1F1"};
        for (int i = 0; i < inputs.length; i++) {
            String result = metaphone.metaphone(inputs[i]);
            assertEquals(expectedResults[i], result);
        }
    }

    @Test
    public void testMetaphoneSpecialCases() {
        Metaphone metaphone = new Metaphone();
        String[] inputs = {"APPLEAPPLE", "APPLEE", "KITTEN", "ACTION", "BANANAS", "BANANANAS", "RABBIT", "RABBITRABBIT"};
        String[] expectedResults = {"A1P1L1E1A1P1L1E1", "A1P1L1E1E1", "K1T1T1", "A1S1J1O1N1", "B2N1N1A1S1", "B2N1N1A1S1", "R1A1B1I1T1", "R1A1B1I1T1R1A1B1I1T1"};
        for (int i = 0; i < inputs.length; i++) {
            String result = metaphone.metaphone(inputs[i]);
            assertEquals(expectedResults[i], result);
        }
    }

    @Test(expected = EncoderException.class)
    public void testMetaphoneNullString() {
        Metaphone metaphone = new Metaphone();
        metaphone.metaphone(null);
    }

    @Test(expected = EncoderException.class)
    public void testMetaphoneNonAlphaNumericString() {
        Metaphone metaphone = new Metaphone();
        metaphone.metaphone("123");
    }
}