package org.apache.commons.codec.language;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Locale;

public class MetaphoneTest {

    @Test
    public void testMetaphoneEncodedString() {
        Metaphone metaphone = new Metaphone();
        String testString = "apple";
        String expected = "A0P0L1E0";
        String result = metaphone.metaphone(testString);
        assertEquals(expected, result);
    }

    @Test
    public void testMetaphoneEmptyString() {
        Metaphone metaphone = new Metaphone();
        String testString = "";
        String expected = "";
        String result = metaphone.metaphone(testString);
        assertEquals(expected, result);
    }

    @Test
    public void testMetaphoneSingleCharacterString() {
        Metaphone metaphone = new Metaphone();
        String testString = "A";
        String expected = "A";
        String result = metaphone.metaphone(testString);
        assertEquals(expected, result);
    }

    @Test
    public void testMetaphoneNullString() {
        Metaphone metaphone = new Metaphone();
        String testString = null;
        String expected = "";
        String result = metaphone.metaphone(testString);
        assertEquals(expected, result);
    }

    @Test(expected = EncoderException.class)
    public void testMetaphoneNonStringObject() {
        Metaphone metaphone = new Metaphone();
        Object testObject = new Object();
        metaphone.encode(testObject);
    }

    @Test
    public void testIsMetaphoneEqualTrue() {
        Metaphone metaphone = new Metaphone();
        String str1 = "apple";
        String str2 = "apple";
        boolean expected = true;
        boolean result = metaphone.isMetaphoneEqual(str1, str2);
        assertEquals(expected, result);
    }

    @Test
    public void testIsMetaphoneEqualFalse() {
        Metaphone metaphone = new Metaphone();
        String str1 = "apple";
        String str2 = "banana";
        boolean expected = false;
        boolean result = metaphone.isMetaphoneEqual(str1, str2);
        assertEquals(expected, result);
    }

    @Test
    public void testIsMetaphoneEqualIgnoreCase() {
        Metaphone metaphone = new Metaphone();
        String str1 = "Apple";
        String str2 = "apple";
        boolean expected = true;
        boolean result = metaphone.isMetaphoneEqual(str1, str2);
        assertEquals(expected, result);
    }

    @Test
    public void testMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        int expected = 4;
        int result = metaphone.getMaxCodeLen();
        assertEquals(expected, result);
    }

    @Test
    public void testSetMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        int newMaxCodeLen = 5;
        metaphone.setMaxCodeLen(newMaxCodeLen);
        int expected = 5;
        int result = metaphone.getMaxCodeLen();
        assertEquals(expected, result);
    }

    @Test
    public void testIsVowel() {
        Metaphone metaphone = new Metaphone();
        String testString = "hello";
        assertTrue(metaphone.isVowel(new StringBuffer(testString), 0));
        assertTrue(metaphone.isVowel(new StringBuffer(testString), 1));
        assertTrue(metaphone.isVowel(new StringBuffer(testString), 3));
        assertTrue(metaphone.isVowel(new StringBuffer(testString), 4));
        assertFalse(metaphone.isVowel(new StringBuffer(testString), 2));
    }

    @Test
    public void testIsPreviousChar() {
        Metaphone metaphone = new Metaphone();
        String testString = "hello";
        assertTrue(metaphone.isPreviousChar(new StringBuffer(testString), 0, 'h'));
        assertTrue(metaphone.isPreviousChar(new StringBuffer(testString), 1, 'h'));
        assertTrue(metaphone.isPreviousChar(new StringBuffer(testString), 2, 'e'));
        assertFalse(metaphone.isPreviousChar(new StringBuffer(testString), 2, 'l'));
        assertFalse(metaphone.isPreviousChar(new StringBuffer(testString), 5, 'o'));
    }

    @Test
    public void testIsNextChar() {
        Metaphone metaphone = new Metaphone();
        String testString = "hello";
        assertTrue(metaphone.isNextChar(new StringBuffer(testString), 1, 'e'));
        assertTrue(metaphone.isNextChar(new StringBuffer(testString), 2, 'l'));
        assertTrue(metaphone.isNextChar(new StringBuffer(testString), 4, 'o'));
        assertFalse(metaphone.isNextChar(new StringBuffer(testString), 3, 'h'));
        assertFalse(metaphone.isNextChar(new StringBuffer(testString), 5, 'e'));
    }

    @Test
    public void testRegionMatch() {
        Metaphone metaphone = new Metaphone();
        String testString = "hello";
        assertTrue(metaphone.regionMatch(new StringBuffer(testString), 0, "he"));
        assertTrue(metaphone.regionMatch(new StringBuffer(testString), 1, "ll"));
        assertTrue(metaphone.regionMatch(new StringBuffer(testString), 3, "o"));
        assertFalse(metaphone.regionMatch(new StringBuffer(testString), 3, "ll"));
        assertFalse(metaphone.regionMatch(new StringBuffer(testString), 5, "o"));
    }

    @Test
    public void testIsLastChar() {
        Metaphone metaphone = new Metaphone();
        String testString = "hello";
        assertTrue(metaphone.isLastChar(testString.length(), 0));
        assertTrue(metaphone.isLastChar(testString.length(), 4));
        assertFalse(metaphone.isLastChar(testString.length(), 2));
        assertFalse(metaphone.isLastChar(testString.length(), 5));
    }

    @Test
    public void testMetaphoneEdgeCases() {
        Metaphone metaphone = new Metaphone();
        String testString1 = "kitten";
        String expected1 = "K1T1T3N1";
        String result1 = metaphone.metaphone(testString1);
        assertEquals(expected1, result1);

        String testString2 = "einstein";
        String expected2 = "E1I1N1S2T1";
        String result2 = metaphone.metaphone(testString2);
        assertEquals(expected2, result2);

        String testString3 = "schadenfreude";
        String expected3 = "S1CH1D1E1F1R1U1D1E";
        String result3 = metaphone.metaphone(testString3);
        assertEquals(expected3, result3);

        String testString4 = "algorithm";
        String expected4 = "A0L1G0R1I1TH0M";
        String result4 = metaphone.metaphone(testString4);
        assertEquals(expected4, result4);
    }
}