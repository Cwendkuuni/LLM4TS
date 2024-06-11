package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader characterReader = new CharacterReader("Hello");
        int expectedResult = 0;
        int actualResult = characterReader.pos();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsEmpty() {
        CharacterReader characterReader = new CharacterReader("");
        boolean expectedResult = true;
        boolean actualResult = characterReader.isEmpty();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testCurrent() {
        CharacterReader characterReader = new CharacterReader("Hello");
        char expectedResult = 'H';
        char actualResult = characterReader.current();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testConsume() {
        CharacterReader characterReader = new CharacterReader("Hello");
        char expectedResult = 'H';
        char actualResult = characterReader.consume();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testUnconsume() {
        CharacterReader characterReader = new CharacterReader("Hello");
        characterReader.consume();
        characterReader.unconsume();
        char expectedResult = 'H';
        char actualResult = characterReader.current();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testAdvance() {
        CharacterReader characterReader = new CharacterReader("Hello");
        characterReader.advance();
        char expectedResult = 'e';
        char actualResult = characterReader.current();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testMark() {
        CharacterReader characterReader = new CharacterReader("Hello");
        characterReader.mark();
        int expectedResult = 0;
        int actualResult = characterReader.pos();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testRewindToMark() {
        CharacterReader characterReader = new CharacterReader("Hello");
        characterReader.mark();
        characterReader.advance();
        characterReader.rewindToMark();
        char expectedResult = 'H';
        char actualResult = characterReader.current();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader characterReader = new CharacterReader("Hello");
        String expectedResult = "H";
        String actualResult = characterReader.consumeAsString();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testNextIndexOf_char() {
        CharacterReader characterReader = new CharacterReader("Hello");
        int expectedResult = 2;
        int actualResult = characterReader.nextIndexOf('l');
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testNextIndexOf_CharSequence() {
        CharacterReader characterReader = new CharacterReader("Hello");
        int expectedResult = 2;
        int actualResult = characterReader.nextIndexOf("ll");
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testConsumeTo_char() {
        CharacterReader characterReader = new CharacterReader("Hello");
        String expectedResult = "He";
        String actualResult = characterReader.consumeTo('l');
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testConsumeTo_String() {
        CharacterReader characterReader = new CharacterReader("Hello");
        String expectedResult = "He";
        String actualResult = characterReader.consumeTo("ll");
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader characterReader = new CharacterReader("Hello");
        String expectedResult = "He";
        String actualResult = characterReader.consumeToAny('l');
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader characterReader = new CharacterReader("Hello");
        String expectedResult = "Hello";
        String actualResult = characterReader.consumeToEnd();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader characterReader = new CharacterReader("Hello");
        String expectedResult = "Hello";
        String actualResult = characterReader.consumeLetterSequence();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader characterReader = new CharacterReader("Hello123");
        String expectedResult = "Hello";
        String actualResult = characterReader.consumeLetterThenDigitSequence();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader characterReader = new CharacterReader("ABC123");
        String expectedResult = "ABC";
        String actualResult = characterReader.consumeHexSequence();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader characterReader = new CharacterReader("123Hello");
        String expectedResult = "123";
        String actualResult = characterReader.consumeDigitSequence();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testMatches_char() {
        CharacterReader characterReader = new CharacterReader("Hello");
        boolean expectedResult = true;
        boolean actualResult = characterReader.matches('H');
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testMatches_String() {
        CharacterReader characterReader = new CharacterReader("Hello");
        boolean expectedResult = true;
        boolean actualResult = characterReader.matches("Hello");
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testMatchesIgnoreCase_String() {
        CharacterReader characterReader = new CharacterReader("Hello");
        boolean expectedResult = true;
        boolean actualResult = characterReader.matchesIgnoreCase("hello");
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testMatchesAny() {
        CharacterReader characterReader = new CharacterReader("Hello");
        boolean expectedResult = true;
        boolean actualResult = characterReader.matchesAny('H', 'h');
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader characterReader = new CharacterReader("Hello");
        boolean expectedResult = true;
        boolean actualResult = characterReader.matchesLetter();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader characterReader = new CharacterReader("123");
        boolean expectedResult = true;
        boolean actualResult = characterReader.matchesDigit();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testMatchConsume_String() {
        CharacterReader characterReader = new CharacterReader("Hello");
        boolean expectedResult = true;
        boolean actualResult = characterReader.matchConsume("Hello");
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testMatchConsumeIgnoreCase_String() {
        CharacterReader characterReader = new CharacterReader("Hello");
        boolean expectedResult = true;
        boolean actualResult = characterReader.matchConsumeIgnoreCase("hello");
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader characterReader = new CharacterReader("Hello");
        boolean expectedResult = true;
        boolean actualResult = characterReader.containsIgnoreCase("el");
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testToString() {
        CharacterReader characterReader = new CharacterReader("Hello");
        String expectedResult = "Hello";
        String actualResult = characterReader.toString();
        assertEquals(expectedResult, actualResult);
    }
}