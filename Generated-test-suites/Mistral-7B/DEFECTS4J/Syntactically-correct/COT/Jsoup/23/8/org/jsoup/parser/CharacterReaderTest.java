package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.jsoup.parser.CharacterReader;

import java.util.Arrays;

public class CharacterReaderTest {

    @org.junit.Before
    public void setUp() {
        // Set up any initial state here if necessary
    }

    // List all the public methods and generate a basic JUnit test case for each

    @org.junit.Test
    public void testCharacterReaderConstructor() {
        String input = "Hello World";
        CharacterReader characterReader = new CharacterReader(input);
        assertEquals("Hello World", characterReader.input);
        assertEquals(input.length(), characterReader.length);
    }

    @org.junit.Test
    public void testIsEmpty() {
        CharacterReader characterReader = new CharacterReader("");
        assertTrue(characterReader.isEmpty());

        characterReader = new CharacterReader("abc");
        assertFalse(characterReader.isEmpty());
    }

    @org.junit.Test
    public void testPos() {
        CharacterReader characterReader = new CharacterReader("abc");
        assertEquals(0, characterReader.pos());

        characterReader.consume();
        assertEquals(1, characterReader.pos());
    }

    @org.junit.Test
    public void testCurrent() {
        CharacterReader characterReader = new CharacterReader("abc");
        assertEquals('a', characterReader.current());

        characterReader.consume();
        assertEquals('b', characterReader.current());
    }

    @org.junit.Test
    public void testConsume() {
        CharacterReader characterReader = new CharacterReader("abc");
        characterReader.consume();
        assertEquals('b', characterReader.current());

        characterReader.consume();
        assertEquals('c', characterReader.current());

        characterReader.consume(); // consume EOF
        assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @org.junit.Test
    public void testUnconsume() {
        CharacterReader characterReader = new CharacterReader("abc");
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('a', characterReader.current());
    }

    @org.junit.Test
    public void testAdvance() {
        CharacterReader characterReader = new CharacterReader("abc");
        characterReader.advance();
        assertEquals('b', characterReader.current());
    }

    @org.junit.Test
    public void testMark() {
        CharacterReader characterReader = new CharacterReader("abc");
        characterReader.mark();
        characterReader.consume();

        characterReader.rewindToMark();
        assertEquals('a', characterReader.current());
    }

    @org.junit.Test
    public void testConsumeAsString() {
        CharacterReader characterReader = new CharacterReader("abcd");
        String consumed = characterReader.consumeAsString();
        assertEquals("bc", consumed);

        characterReader.rewindToMark();
        consumed = characterReader.consumeAsString();
        assertEquals("ab", consumed);
    }

    @org.junit.Test
    public void testConsumeTo() {
        CharacterReader characterReader = new CharacterReader("abcdabcd");
        String consumed = characterReader.consumeTo('c');
        assertEquals("ab", consumed);

        consumed = characterReader.consumeTo('d');
        assertEquals("bc", consumed);

        consumed = characterReader.consumeToEnd();
        assertEquals("dabcd", consumed);
    }

    @org.junit.Test
    public void testConsumeToSequence() {
        CharacterReader characterReader = new CharacterReader("123456");
        String consumed = characterReader.consumeTo("23");
        assertEquals("1", consumed);

        consumed = characterReader.consumeTo("56");
        assertEquals("234", consumed);

        consumed = characterReader.consumeToEnd();
        assertEquals("56", consumed);
    }

    @org.junit.Test
    public void testConsumeToAny() {
        CharacterReader characterReader = new CharacterReader("12ab34");
        String consumed = characterReader.consumeToAny(new char[]{'a', 'b'});
        assertEquals("2", consumed);

        consumed = characterReader.consumeToAny(new char[]{ '3' });
        assertEquals("3", consumed);

        consumed = characterReader.consumeToEnd();
        assertEquals("4", consumed);
    }

    @org.junit.Test
    public void testConsumeToEnd() {
        CharacterReader characterReader = new CharacterReader("abcd");
        String consumed = characterReader.consumeToEnd();
        assertEquals("d", characterReader.current());
        assertEquals("cd", consumed);
    }

    @org.junit.Test
    public void testConsumeLetterSequence() {
        CharacterReader characterReader = new CharacterReader("abcdef");
        String consumed = characterReader.consumeLetterSequence();
        assertEquals("abc", consumed);

        consumed = characterReader.consumeLetterSequence();
        assertEquals("def", consumed);
    }

    @org.junit.Test
    public void testConsumeHexSequence() {
        CharacterReader characterReader = new CharacterReader("1234abc5ef");
        String consumed = characterReader.consumeHexSequence();
        assertEquals("1234", consumed);

        consumed = characterReader.consumeHexSequence();
        assertEquals("5ef", consumed);
    }

    @org.junit.Test
    public void testConsumeDigitSequence() {
        CharacterReader characterReader = new CharacterReader("123456");
        String consumed = characterReader.consumeDigitSequence();
        assertEquals("1234", consumed);

        consumed = characterReader.consumeDigitSequence();
        assertEquals("56", consumed);
    }

    @org.junit.Test
    public void testMatches() {
        CharacterReader characterReader = new CharacterReader("abc");
        assertTrue(characterReader.matches('a'));
        assertFalse(characterReader.matches('b'));
        assertFalse(characterReader.matches('c'));
    }

    @org.junit.Test
    public void testMatchesSequence() {
        CharacterReader characterReader = new CharacterReader("abc");
        assertTrue(characterReader.matches("ab"));
        assertFalse(characterReader.matches("ac"));
        assertFalse(characterReader.matches("bc"));
    }

    @org.junit.Test
    public void testMatchesIgnoreCase() {
        CharacterReader characterReader = new CharacterReader("ABC");
        assertTrue(characterReader.matchesIgnoreCase("AbC"));
        assertFalse(characterReader.matchesIgnoreCase("aBc"));
        assertFalse(characterReader.matchesIgnoreCase("bCd"));
    }

    @org.junit.Test
    public void testMatchesAny() {
        CharacterReader characterReader = new CharacterReader("abc");
        assertTrue(characterReader.matchesAny(new char[]{'a', 'b'}));
        assertFalse(characterReader.matchesAny(new char[]{'c'}));
        assertFalse(characterReader.matchesAny(new char[]{'d'}));
    }

    @org.junit.Test
    public void testMatchesLetter() {
        CharacterReader characterReader = new CharacterReader("abc");
        assertTrue(characterReader.matchesLetter());
        assertFalse(characterReader.matchesLetter());
        assertFalse(characterReader.matchesLetter());

        characterReader = new CharacterReader("ABC");
        assertTrue(characterReader.matchesLetter()); // uppercase 'A'
        assertTrue(characterReader.matchesLetter()); // lowercase 'b'
        assertTrue(characterReader.matchesLetter()); // lowercase 'c'
    }

    @org.junit.Test
    public void testMatchesDigit() {
        CharacterReader characterReader = new CharacterReader("123");
        assertTrue(characterReader.matchesDigit());
        assertTrue(characterReader.matchesDigit());
        assertTrue(characterReader.matchesDigit());

        characterReader = new CharacterReader("456");
        assertTrue(characterReader.matchesDigit());
        assertTrue(characterReader.matchesDigit());
        assertTrue(characterReader.matchesDigit());

        characterReader = new CharacterReader("789");
        assertTrue(characterReader.matchesDigit());
        assertTrue(characterReader.matchesDigit());
        assertTrue(characterReader.matchesDigit());
    }

    @org.junit.Test
    public void testMatchConsumeSequence() {
        CharacterReader characterReader = new CharacterReader("abcd");
        assertTrue(characterReader.matchConsume("ab"));
        assertEquals("cd", characterReader.input.substring(characterReader.pos));

        characterReader = new CharacterReader("ABCD");
        assertTrue(characterReader.matchConsumeIgnoreCase("ab"));
        assertEquals("CD", characterReader.input.substring(characterReader.pos));
    }

    @org.junit.Test
    public void testContainsIgnoreCase() {
        CharacterReader characterReader = new CharacterReader("</title><style>");
        assertTrue(characterReader.containsIgnoreCase("<title>"));
        assertTrue(characterReader.containsIgnoreCase("<style>"));
        assertFalse(characterReader.containsIgnoreCase("<script>"));
    }

    @org.junit.Test
    public void testToString() {
        CharacterReader characterReader = new CharacterReader("abc");
        assertEquals("bc", characterReader.toString());

        characterReader = new CharacterReader("def");
        assertEquals("ef", characterReader.toString());
    }

    // Identify potential edge cases and exception handling scenarios

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCharacterReaderNullInput() {
        new CharacterReader(null);
    }

    @org.junit.Test
    public void testConsumeAfterEnd() {
        CharacterReader characterReader = new CharacterReader("abc");
        characterReader.consumeToEnd();
        characterReader.consume();
        assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testConsumeToEndBeyondInputLimit() {
        CharacterReader characterReader = new CharacterReader("abc");
        characterReader.consumeToEnd();
        characterReader.consume(); // consume EOF
        characterReader.consume(2);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testConsumeToNegativeIndex() {
        CharacterReader characterReader = new CharacterReader("abc");
        characterReader.consumeTo(-1);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testConsumeToNullSequence() {
        CharacterReader characterReader = new CharacterReader("abc");
        characterReader.consumeTo(null);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testConsumeToEmptySequence() {
        CharacterReader characterReader = new CharacterReader("abc");
        characterReader.consumeTo("");
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testConsumeToSequenceBeyondInputLimit() {
        CharacterReader characterReader = new CharacterReader("abc");
        characterReader.consumeTo("def");
    }

    @org.junit.Test
    public void testConsumeToAnyEmptySequence() {
        CharacterReader characterReader = new CharacterReader("abc");
        String consumed = characterReader.consumeToAny(new char[]{});
        assertEquals("", consumed);
    }

    @org.junit.Test
    public void testConsumeToAnyNullSequence() {
        CharacterReader characterReader = new CharacterReader("abc");
        String consumed = characterReader.consumeToAny(null);
        assertEquals("", consumed);
    }

    @org.junit.Test
    public void testConsumeToAnyBeyondInputLimit() {
        CharacterReader characterReader = new CharacterReader("abc");
        String consumed = characterReader.consumeToAny(new char[]{ 'd' });
        assertEquals("", consumed);
    }

    @org.junit.Test
    public void testConsumeLetterSequenceEmptyInput() {
        CharacterReader characterReader = new CharacterReader("");
        assertEquals("", characterReader.consumeLetterSequence());
    }

    @org.junit.Test
    public void testConsumeHexSequenceEmptyInput() {
        CharacterReader characterReader = new CharacterReader("");
        assertEquals("", characterReader.consumeHexSequence());
    }

    @org.junit.Test
    public void testConsumeDigitSequenceEmptyInput() {
        CharacterReader characterReader = new CharacterReader("");
        assertEquals("", characterReader.consumeDigitSequence());
    }

    @org.junit.Test
    public void testMatchesInvalidCharacter() {
        CharacterReader characterReader = new CharacterReader("abc");
        assertFalse(characterReader.matches('\u0001'));
    }

    @org.junit.Test
    public void testMatchesSequenceInvalidCharacter() {
        CharacterReader characterReader = new CharacterReader("abc");
        assertFalse(characterReader.matches("ab\u0001"));
    }

    @org.junit.Test
    public void testMatchesIgnoreCaseInvalidCharacter() {
        CharacterReader characterReader = new CharacterReader("ABC");
        assertFalse(characterReader.matchesIgnoreCase("ab\u0001"));
    }

    @org.junit.Test
    public void testMatchesAnyInvalidCharacter() {
        CharacterReader characterReader = new CharacterReader("abc");
        assertFalse(characterReader.matchesAny(new char[]{'\\u0001'}));
    }

    @org.junit.Test
    public void testMatchesLetterInvalidCharacter() {
        CharacterReader characterReader = new CharacterReader("abc");
        assertFalse(characterReader.matchesLetter());
        assertFalse(characterReader.matchesLetter());
        assertFalse(characterReader.matchesLetter());

        characterReader = new CharacterReader("ABC");
        assertTrue(characterReader.matchesLetter()); // uppercase 'A'
        assertTrue(characterReader.matchesLetter()); // lowercase 'b'
        assertFalse(characterReader.matchesLetter()); // invalid character '\u0001'
    }

    @org.junit.Test
    public void testMatchesDigitInvalidCharacter() {
        CharacterReader characterReader = new CharacterReader("123");
        assertTrue(characterReader.matchesDigit());
        assertTrue(characterReader.matchesDigit());
        assertTrue(characterReader.matchesDigit());

        characterReader = new CharacterReader("456");
        assertTrue(characterReader.matchesDigit());
        assertTrue(characterReader.matchesDigit());
        assertTrue(characterReader.matchesDigit());

        characterReader = new CharacterReader("789");
        assertTrue(characterReader.matchesDigit());
        assertTrue(characterReader.matchesDigit());
        assertTrue(characterReader.matchesDigit());

        characterReader = new CharacterReader("012");
        assertTrue(characterReader.matchesDigit());
        assertTrue(characterReader.matchesDigit());
        assertFalse(characterReader.matchesDigit()); // invalid character '1'
    }

    @org.junit.Test
    public void testMatchConsumeSequenceEmptyInput() {
        CharacterReader characterReader = new CharacterReader("");
        assertFalse(characterReader.matchConsume(""));
    }

    @org.junit.Test
    public void testMatchConsumeSequenceInvalidInput() {
        CharacterReader characterReader = new CharacterReader("abc");
        assertFalse(characterReader.matchConsume("def"));
    }

    @org.junit.Test
    public void testMatchConsumeIgnoreCaseEmptyInput() {
        CharacterReader characterReader = new CharacterReader("ABC");
        assertFalse(characterReader.matchConsumeIgnoreCase(""));
    }

    @org.junit.Test
    public void testMatchConsumeIgnoreCaseInvalidInput() {
        CharacterReader characterReader = new CharacterReader("ABC");
        assertFalse(characterReader.matchConsumeIgnoreCase("def"));
    }
}