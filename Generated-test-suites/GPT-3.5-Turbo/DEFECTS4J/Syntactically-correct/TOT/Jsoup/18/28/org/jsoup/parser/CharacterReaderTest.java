package org.jsoup.parser;

import org.junit.*;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        String input = "ABCDE";
        CharacterReader characterReader = new CharacterReader(input);

        assertEquals(0, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        String input = "";
        CharacterReader characterReader = new CharacterReader(input);

        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        String input = "ABCDE";
        CharacterReader characterReader = new CharacterReader(input);

        assertEquals('A', characterReader.current());
    }

    @Test
    public void testCurrentWithEmptyInput() {
        String input = "";
        CharacterReader characterReader = new CharacterReader(input);

        assertEquals((char) -1, characterReader.current());
    }

    @Test
    public void testConsume() {
        String input = "ABCDE";
        CharacterReader characterReader = new CharacterReader(input);

        assertEquals('A', characterReader.consume());
        assertEquals('B', characterReader.consume());
    }

    @Test
    public void testConsumeWithEmptyInput() {
        String input = "";
        CharacterReader characterReader = new CharacterReader(input);

        assertEquals((char) -1, characterReader.consume());
    }

    @Test
    public void testUnconsume() {
        String input = "ABCDE";
        CharacterReader characterReader = new CharacterReader(input);

        assertEquals('A', characterReader.consume());
        assertEquals('B', characterReader.consume());

        characterReader.unconsume();
        assertEquals('B', characterReader.consume());
    }

    @Test
    public void testAdvance() {
        String input = "ABCDE";
        CharacterReader characterReader = new CharacterReader(input);

        assertEquals('A', characterReader.consume());
        characterReader.advance();
        assertEquals('C', characterReader.consume());
    }

    @Test
    public void testMark() {
        String input = "ABCDE";
        CharacterReader characterReader = new CharacterReader(input);

        characterReader.mark();
        assertEquals(0, characterReader.pos());
    }

    @Test
    public void testRewindToMark() {
        String input = "ABCDE";
        CharacterReader characterReader = new CharacterReader(input);

        characterReader.mark();
        assertEquals(0, characterReader.pos());

        characterReader.advance();
        assertEquals('B', characterReader.consume());

        characterReader.rewindToMark();
        assertEquals(0, characterReader.pos());
    }

    @Test
    public void testConsumeAsString() {
        String input = "ABCDE";
        CharacterReader characterReader = new CharacterReader(input);

        assertEquals("A", characterReader.consumeAsString());
        assertEquals("B", characterReader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        String input = "ABCDEF";
        CharacterReader characterReader = new CharacterReader(input);

        assertEquals("ABC", characterReader.consumeTo('D'));
        assertEquals("D", characterReader.consumeTo('E'));
    }

    @Test
    public void testConsumeToWithNoMatch() {
        String input = "ABC";
        CharacterReader characterReader = new CharacterReader(input);

        assertEquals("ABC", characterReader.consumeTo('D'));
    }

    @Test
    public void testConsumeToAny() {
        String input = "ABCDEF";
        CharacterReader characterReader = new CharacterReader(input);

        assertEquals("ABC", characterReader.consumeToAny('E'));
        assertEquals("", characterReader.consumeToAny('X', 'Y', 'Z'));
    }

    @Test
    public void testConsumeToEnd() {
        String input = "ABCDE";
        CharacterReader characterReader = new CharacterReader(input);

        assertEquals("ABCDE", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        String input = "ABC123";
        CharacterReader characterReader = new CharacterReader(input);

        assertEquals("ABC", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterSequenceWithNoLetters() {
        String input = "123";
        CharacterReader characterReader = new CharacterReader(input);

        assertEquals("", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        String input = "123ABCDEFG";
        CharacterReader characterReader = new CharacterReader(input);

        assertEquals("123ABCDEF", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeHexSequenceWithNoHex() {
        String input = "XYZ";
        CharacterReader characterReader = new CharacterReader(input);

        assertEquals("", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        String input = "123ABC";
        CharacterReader characterReader = new CharacterReader(input);

        assertEquals("123", characterReader.consumeDigitSequence());
    }

    @Test
    public void testConsumeDigitSequenceWithNoDigits() {
        String input = "ABC";
        CharacterReader characterReader = new CharacterReader(input);

        assertEquals("", characterReader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        String input = "ABC";
        CharacterReader characterReader = new CharacterReader(input);

        assertTrue(characterReader.matches('A'));
        assertFalse(characterReader.matches('B'));
    }

    @Test
    public void testMatchesWithEmptyInput() {
        String input = "";
        CharacterReader characterReader = new CharacterReader(input);

        assertFalse(characterReader.matches('A'));
    }

    @Test
    public void testMatchesSeq() {
        String input = "ABC";
        CharacterReader characterReader = new CharacterReader(input);

        assertTrue(characterReader.matches("AB"));
        assertFalse(characterReader.matches("BC"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        String input = "ABC";
        CharacterReader characterReader = new CharacterReader(input);

        assertTrue(characterReader.matchesIgnoreCase("AB"));
        assertFalse(characterReader.matchesIgnoreCase("BC"));
    }

    @Test
    public void testMatchesAny() {
        String input = "ABC";
        CharacterReader characterReader = new CharacterReader(input);

        assertTrue(characterReader.matchesAny('X', 'Y', 'Z'));
        assertFalse(characterReader.matchesAny('B', 'C'));
    }

    @Test
    public void testMatchesLetter() {
        String input = "ABC";
        CharacterReader characterReader = new CharacterReader(input);

        assertTrue(characterReader.matchesLetter());
        assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchesDigit() {
        String input = "123";
        CharacterReader characterReader = new CharacterReader(input);

        assertTrue(characterReader.matchesDigit());
        assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchConsume() {
        String input = "ABC";
        CharacterReader characterReader = new CharacterReader(input);

        assertTrue(characterReader.matchConsume("AB"));
        assertFalse(characterReader.matchConsume("BC"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        String input = "ABC";
        CharacterReader characterReader = new CharacterReader(input);

        assertTrue(characterReader.matchConsumeIgnoreCase("ab"));
        assertFalse(characterReader.matchConsumeIgnoreCase("bc"));
    }

    @Test
    public void testContainsIgnoreCase() {
        String input = "ABC";
        CharacterReader characterReader = new CharacterReader(input);

        assertTrue(characterReader.containsIgnoreCase("bc"));
        assertFalse(characterReader.containsIgnoreCase("xy"));
    }

    @Override
    protected void tearDown() {
        // Code to cleanup resources after test execution
    }
}