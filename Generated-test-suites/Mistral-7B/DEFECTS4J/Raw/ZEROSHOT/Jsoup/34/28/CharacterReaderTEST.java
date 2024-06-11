package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Locale;

/**
 * CharacterReaderTest
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstruction() {
        CharacterReader characterReader = new CharacterReader("testString");
        int expectedPos = 0;
        int expectedLength = 6;
        int actualPos = characterReader.pos();
        int actualLength = characterReader.length();

        assertEquals("Unexpected position", expectedPos, actualPos);
        assertEquals("Unexpected length", expectedLength, actualLength);
    }

    @Test
    public void testCharacterReaderIsEmpty() {
        CharacterReader characterReader = new CharacterReader("");
        boolean expectedResult = true;
        boolean actualResult = characterReader.isEmpty();

        assertEquals("Unexpected result", expectedResult, actualResult);
    }

    @Test
    public void testCharacterReaderIsNotEmpty() {
        CharacterReader characterReader = new CharacterReader("testString");
        boolean expectedResult = false;
        boolean actualResult = characterReader.isEmpty();

        assertEquals("Unexpected result", expectedResult, actualResult);
    }

    @Test
    public void testCharacterReaderCurrentChar() {
        CharacterReader characterReader = new CharacterReader("testString");
        char expectedChar = 't';
        char actualChar = characterReader.current();

        assertEquals("Unexpected current character", expectedChar, actualChar);
    }

    @Test
    public void testCharacterReaderConsumeChar() {
        CharacterReader characterReader = new CharacterReader("testString");
        char expectedChar = 't';
        char actualChar = characterReader.current();
        characterReader.consume();

        char consumedChar = characterReader.current();
        char expectedConsumedChar = 'e';

        assertEquals("Unexpected current character after consuming", expectedConsumedChar, consumedChar);
    }

    @Test(expected = IllegalStateException.class)
    public void testCharacterReaderConsumeFromEmptyReader() {
        CharacterReader characterReader = new CharacterReader("");
        characterReader.consume();
    }

    @Test
    public void testCharacterReaderUnconsumeChar() {
        CharacterReader characterReader = new CharacterReader("testString");
        characterReader.consume();
        characterReader.unconsume();

        char currentChar = characterReader.current();
        char expectedCurrentChar = 't';

        assertEquals("Unexpected current character after unconsuming", expectedCurrentChar, currentChar);
    }

    @Test
    public void testCharacterReaderAdvanceChar() {
        CharacterReader characterReader = new CharacterReader("testString");
        characterReader.advance();

        char currentChar = characterReader.current();
        char expectedCurrentChar = 'e';

        assertEquals("Unexpected current character after advancing", expectedCurrentChar, currentChar);
    }

    @Test
    public void testCharacterReaderMark() {
        CharacterReader characterReader = new CharacterReader("testString");
        characterReader.mark();

        int currentPos = characterReader.pos();
        int expectedCurrentPos = 0;
        int markPos = characterReader.mark();

        assertEquals("Unexpected current position", expectedCurrentPos, currentPos);
        assertEquals("Unexpected mark position", 1, markPos);
    }

    @Test
    public void testCharacterReaderRewindToMark() {
        CharacterReader characterReader = new CharacterReader("testString");
        characterReader.mark();
        characterReader.advance();
        characterReader.rewindToMark();

        char currentChar = characterReader.current();
        char expectedCurrentChar = 't';

        assertEquals("Unexpected current character after rewinding to mark", expectedCurrentChar, currentChar);
    }

    @Test
    public void testCharacterReaderConsumeAsString() {
        CharacterReader characterReader = new CharacterReader("testString");
        String expectedString = "test";
        String actualString = characterReader.consumeAsString();

        assertEquals("Unexpected consumed string", expectedString, actualString);
    }

    @Test
    public void testCharacterReaderNextIndexOfChar() {
        CharacterReader characterReader = new CharacterReader("testString");
        int expectedIndex = 3;
        int actualIndex = characterReader.nextIndexOf('s');

        assertEquals("Unexpected index of character 's'", expectedIndex, actualIndex);
    }

    @Test
    public void testCharacterReaderNextIndexOfString() {
        CharacterReader characterReader = new CharacterReader("testStringtestString");
        int expectedIndex = 5;
        int actualIndex = characterReader.nextIndexOf("String");

        assertEquals("Unexpected index of string 'String'", expectedIndex, actualIndex);
    }

    @Test
    public void testCharacterReaderConsumeToChar() {
        CharacterReader characterReader = new CharacterReader("testString");
        String expectedString = "test";
        String actualString = characterReader.consumeTo('s');

        assertEquals("Unexpected consumed string", expectedString, actualString);
    }

    @Test
    public void testCharacterReaderConsumeToString() {
        CharacterReader characterReader = new CharacterReader("testStringtestString");
        String expectedString = "String";
        String actualString = characterReader.consumeTo("String");

        assertEquals("Unexpected consumed string", expectedString, actualString);
    }

    @Test
    public void testCharacterReaderConsumeToAnyChars() {
        CharacterReader characterReader = new CharacterReader("test123String");
        String expectedString = "123";
        String actualString = characterReader.consumeToAny('1', '2', '3');

        assertEquals("Unexpected consumed string", expectedString, actualString);
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader characterReader = new CharacterReader("testString");
        String expectedString = "String";
        String actualString = characterReader.consumeToEnd();

        assertEquals("Unexpected consumed string", expectedString, actualString);
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader characterReader = new CharacterReader("testString");
        String expectedString = "test";
        String actualString = characterReader.consumeLetterSequence();

        assertEquals("Unexpected consumed letter sequence", expectedString, actualString);
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader characterReader = new CharacterReader("test123String");
        String expectedString = "test123";
        String actualString = characterReader.consumeLetterThenDigitSequence();

        assertEquals("Unexpected consumed letter then digit sequence", expectedString, actualString);
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader characterReader = new CharacterReader("test#ff0000String");
        String expectedString = "#ff0000";
        String actualString = characterReader.consumeHexSequence();

        assertEquals("Unexpected consumed hex sequence", expectedString, actualString);
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader characterReader = new CharacterReader("test123String");
        String expectedString = "123";
        String actualString = characterReader.consumeDigitSequence();

        assertEquals("Unexpected consumed digit sequence", expectedString, actualString);
    }

    @Test
    public void testCharacterReaderMatchesChar() {
        CharacterReader characterReader = new CharacterReader("testString");
        char expectedChar = 't';
        boolean actualResult = characterReader.matches(expectedChar);

        assertTrue("Unexpected result of matches('" + expectedChar + "')", actualResult);
    }

    @Test
    public void testCharacterReaderMatchesString() {
        CharacterReader characterReader = new CharacterReader("testStringtestString");
        String expectedString = "String";
        boolean actualResult = characterReader.matches(expectedString);

        assertTrue("Unexpected result of matches(\"" + expectedString + "\")", actualResult);
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCaseString() {
        CharacterReader characterReader = new CharacterReader("TestSTRINGTestString");
        String expectedString = "string";
        boolean actualResult = characterReader.matchesIgnoreCase(expectedString);

        assertTrue("Unexpected result of matchesIgnoreCase(\"" + expectedString + "\")", actualResult);
    }

    @Test
    public void testCharacterReaderMatchesAnyChars() {
        CharacterReader characterReader = new CharacterReader("test123String");
        char[] expectedChars = {'1', '2', '3'};
        boolean actualResult = characterReader.matchesAny(expectedChars);

        assertTrue("Unexpected result of matchesAny(" + Arrays.toString(expectedChars) + ")", actualResult);
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader characterReader = new CharacterReader("testString");
        boolean expectedResult = true;
        boolean actualResult = characterReader.matchesLetter();

        assertTrue("Unexpected result of matchesLetter()", actualResult);
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader characterReader = new CharacterReader("test123String");
        boolean expectedResult = true;
        boolean actualResult = characterReader.matchesDigit();

        assertTrue("Unexpected result of matchesDigit()", actualResult);
    }

    @Test
    public void testCharacterReaderMatchConsumeString() {
        CharacterReader characterReader = new CharacterReader("testStringtestString");
        String expectedString = "String";
        characterReader.matchConsume(expectedString);

        String actualString = characterReader.consumeToEnd();
        assertEquals("Unexpected consumed string after matching and consuming", expectedString, actualString);
    }

    @Test
    public void testCharacterReaderMatchConsumeIgnoreCaseString() {
        CharacterReader characterReader = new CharacterReader("TestSTRINGTestString");
        String expectedString = "string";
        characterReader.matchConsumeIgnoreCase(expectedString);

        String actualString = characterReader.consumeToEnd();
        assertEquals("Unexpected consumed string after matching and consuming ignore case", expectedString, actualString);
    }

    @Test
    public void testCharacterReaderContainsIgnoreCaseString() {
        CharacterReader characterReader = new CharacterReader("test<html><title>Test</title></html>");
        String expectedString = "/title>";
        boolean actualResult = characterReader.containsIgnoreCase(expectedString);

        assertTrue("Unexpected result of containsIgnoreCase(\"" + expectedString + "\")", actualResult);
    }
}