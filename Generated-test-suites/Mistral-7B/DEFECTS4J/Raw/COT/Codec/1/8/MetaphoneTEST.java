package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.*;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();
        String input = "test";
        String expected = "T2";
        String actual = metaphone.metaphone(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testMetaphoneEmptyString() {
        Metaphone metaphone = new Metaphone();
        String input = "";
        String expected = "";
        String actual = metaphone.metaphone(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testMetaphoneNullString() {
        Metaphone metaphone = new Metaphone();
        String input = null;
        String expected = "";
        String actual = metaphone.metaphone(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testMetaphoneSingleCharacter() {
        Metaphone metaphone = new Metaphone();
        String input = "A";
        String expected = "A";
        String actual = metaphone.metaphone(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testMetaphoneInitialK() {
        Metaphone metaphone = new Metaphone();
        String input = "Kitten";
        String expected = "K2";
        String actual = metaphone.metaphone(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testMetaphoneInitialC() {
        Metaphone metaphone = new Metaphone();
        String input = "Cat";
        String expected = "C3";
        String actual = metaphone.metaphone(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testMetaphoneInitialX() {
        Metaphone metaphone = new Metaphone();
        String input = "Example";
        String expected = "S5";
        String actual = metaphone.metaphone(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testMetaphoneDoubleVowels() {
        Metaphone metaphone = new Metaphone();
        String input = "Apple";
        String expected = "A1P1L1E1";
        String actual = metaphone.metaphone(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testMetaphoneDoubleConssonant() {
        Metaphone metaphone = new Metaphone();
        String input = "Bottle";
        String expected = "B3T3L3";
        String actual = metaphone.metaphone(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testMetaphoneDoubleLetterD() {
        Metaphone metaphone = new Metaphone();
        String input = "Badd";
        String expected = "B3D2";
        String actual = metaphone.metaphone(input);
        assertEquals(expected, actual);
    }

    @Test(expected = EncoderException.class)
    public void testMetaphoneNonAsciiCharacter() {
        Metaphone metaphone = new Metaphone();
        String input = "Testé";
        metaphone.metaphone(input);
    }

    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        String str1 = "test";
        String str2 = "test";
        boolean expected = true;
        boolean actual = metaphone.isMetaphoneEqual(str1, str2);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsMetaphoneEqualDifferentStrings() {
        Metaphone metaphone = new Metaphone();
        String str1 = "test";
        String str2 = "test2";
        boolean expected = false;
        boolean actual = metaphone.isMetaphoneEqual(str1, str2);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsMetaphoneEqualSameCodeLength() {
        Metaphone metaphone = new Metaphone();
        String str1 = "test";
        String str2 = "test";
        boolean expected = true;
        boolean actual = metaphone.isMetaphoneEqual(str1, str2);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsMetaphoneEqualDifferentCodeLengths() {
        Metaphone metaphone = new Metaphone();
        String str1 = "test";
        String str2 = "test1";
        boolean expected = false;
        boolean actual = metaphone.isMetaphoneEqual(str1, str2);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsVowel() {
        Metaphone metaphone = new Metaphone();
        String input = "test";
        char letter = 'e';
        boolean expected = true;
        boolean actual = metaphone.isVowel(input, input.indexOf(letter));
        assertEquals(expected, actual);
    }

    @Test
    public void testIsVowelFalse() {
        Metaphone metaphone = new Metaphone();
        String input = "test";
        char letter = 't';
        boolean expected = false;
        boolean actual = metaphone.isVowel(input, input.indexOf(letter));
        assertEquals(expected, actual);
    }

    @Test
    public void testIsPreviousChar() {
        Metaphone metaphone = new Metaphone();
        String input = "test";
        char previousChar = 't';
        char currentChar = 'e';
        boolean expected = true;
        boolean actual = metaphone.isPreviousChar(input, input.length() - 1, previousChar);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsPreviousCharFalse() {
        Metaphone metaphone = new Metaphone();
        String input = "test";
        char previousChar = 'z';
        char currentChar = 'e';
        boolean expected = false;
        boolean actual = metaphone.isPreviousChar(input, input.length() - 1, previousChar);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsNextChar() {
        Metaphone metaphone = new Metaphone();
        String input = "test";
        char currentChar = 'e';
        char nextChar = 't';
        boolean expected = true;
        boolean actual = metaphone.isNextChar(input, input.length() - 1, nextChar);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsNextCharFalse() {
        Metaphone metaphone = new Metaphone();
        String input = "test";
        char currentChar = 'e';
        char nextChar = 'z';
        boolean expected = false;
        boolean actual = metaphone.isNextChar(input, input.length() - 1, nextChar);
        assertEquals(expected, actual);
    }

    @Test
    public void testRegionMatch() {
        Metaphone metaphone = new Metaphone();
        String input = "test";
        String test = "sh";
        boolean expected = false;
        boolean actual = metaphone.regionMatch(input, input.length() - 2, test);
        assertEquals(expected, actual);
    }

    @Test
    public void testRegionMatchTrue() {
        Metaphone metaphone = new Metaphone();
        String input = "test";
        String test = "sh";
        boolean expected = true;
        boolean actual = metaphone.regionMatch(input, input.length() - 2, test);
        assertEquals(expected, actual);
    }

    @Test
    public void testRegionMatchFalseLongerString() {
        Metaphone metaphone = new Metaphone();
        String input = "test";
        String test = "shi";
        boolean expected = false;
        boolean actual = metaphone.regionMatch(input, input.length() - 2, test);
        assertEquals(expected, actual);
    }

    @Test
    public void testRegionMatchTrueLongerString() {
        Metaphone metaphone = new Metaphone();
        String input = "test";
        String test = "shi";
        boolean expected = true;
        boolean actual = metaphone.regionMatch(input, 1, test);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsLastChar() {
        Metaphone metaphone = new Metaphone();
        String input = "test";
        boolean expected = false;
        boolean actual = metaphone.isLastChar(input.length(), 0);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsLastCharTrue() {
        Metaphone metaphone = new Metaphone();
        String input = "test";
        boolean expected = true;
        boolean actual = metaphone.isLastChar(input.length(), input.length() - 1);
        assertEquals(expected, actual);
    }
}