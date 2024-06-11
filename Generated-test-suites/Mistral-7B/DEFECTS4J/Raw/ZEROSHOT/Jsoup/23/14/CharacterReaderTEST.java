package org.jsoup.parser.test;

import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import static org.junit.Assert.*;

/**
 * CharacterReaderTest
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader characterReader = new CharacterReader("");
        assertTrue(characterReader.isEmpty());
        assertEquals(-1, characterReader.current());
    }

    @Test
    public void testCharacterReaderSingleCharacter() {
        CharacterReader characterReader = new CharacterReader("A");
        assertFalse(characterReader.isEmpty());
        assertEquals('A', characterReader.current());
        characterReader.consume();
        assertTrue(characterReader.isEmpty());
        assertEquals(-1, characterReader.current());
    }

    @Test
    public void testCharacterReaderMultipleCharacters() {
        CharacterReader characterReader = new CharacterReader("ABC");
        assertFalse(characterReader.isEmpty());
        assertEquals('A', characterReader.current());
        characterReader.consume();
        assertEquals('B', characterReader.current());
        characterReader.consume();
        assertEquals('C', characterReader.current());
        characterReader.consume();
        assertTrue(characterReader.isEmpty());
        assertEquals(-1, characterReader.current());
    }

    @Test
    public void testCharacterReaderMarkPos() {
        CharacterReader characterReader = new CharacterReader("ABCDEF");
        characterReader.mark();
        characterReader.consume();
        characterReader.advance();
        characterReader.consume();
        assertEquals('C', characterReader.current());
        characterReader.rewindToMark();
        assertEquals('A', characterReader.current());
    }

    @Test
    public void testCharacterReaderConsumeAsString() {
        CharacterReader characterReader = new CharacterReader("ABCDEF");
        String consumed = characterReader.consumeAsString();
        assertEquals("AB", consumed);
        consumed = characterReader.consumeAsString();
        assertEquals("CD", consumed);
        consumed = characterReader.consumeAsString();
        assertEquals("EF", consumed);
        assertTrue(characterReader.isEmpty());
        assertEquals(-1, characterReader.current());
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader characterReader = new CharacterReader("ABCDEF");
        String consumed = characterReader.consumeTo('C');
        assertEquals("AB", consumed);
        consumed = characterReader.consumeTo('F');
        assertEquals("CDEF", consumed);
        consumed = characterReader.consumeToEnd();
        assertEquals("EF", consumed);
        assertTrue(characterReader.isEmpty());
        assertEquals(-1, characterReader.current());
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader characterReader = new CharacterReader("ABC123DEF");
        String consumed = characterReader.consumeToAny('1', '2');
        assertEquals("123", consumed);
        consumed = characterReader.consumeToAny('B', 'C');
        assertEquals("BC", consumed);
        consumed = characterReader.consumeToEnd();
        assertEquals("DEF", consumed);
        assertTrue(characterReader.isEmpty());
        assertEquals(-1, characterReader.current());
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader characterReader = new CharacterReader("ABCDEF");
        String consumed = characterReader.consumeLetterSequence();
        assertEquals("ABC", consumed);
        consumed = characterReader.consumeLetterSequence();
        assertEquals("DEF", consumed);
        assertTrue(characterReader.isEmpty());
        assertEquals(-1, characterReader.current());
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader characterReader = new CharacterReader("ABCDEF0123456789ABCDEF");
        String consumed = characterReader.consumeHexSequence();
        assertEquals("0123", consumed);
        consumed = characterReader.consumeHexSequence();
        assertEquals("4567", consumed);
        consumed = characterReader.consumeHexSequence();
        assertEquals("89AB", consumed);
        assertTrue(characterReader.isEmpty());
        assertEquals(-1, characterReader.current());
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader characterReader = new CharacterReader("ABCDEF0123456789");
        String consumed = characterReader.consumeDigitSequence();
        assertEquals("0123456789", consumed);
        consumed = characterReader.consumeDigitSequence();
        assertTrue(characterReader.isEmpty());
        assertEquals(-1, characterReader.current());
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader characterReader = new CharacterReader("ABCDEF");
        assertTrue(characterReader.matches('A'));
        assertTrue(characterReader.matches("AB"));
        assertFalse(characterReader.matches('1'));
        assertFalse(characterReader.matches("GH"));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader characterReader = new CharacterReader("ABCDEF");
        assertTrue(characterReader.matchesIgnoreCase("ab"));
        assertTrue(characterReader.matchesIgnoreCase("ABC"));
        assertFalse(characterReader.matchesIgnoreCase("123"));
        assertFalse(characterReader.matchesIgnoreCase("GH"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader characterReader = new CharacterReader("ABCDEF");
        assertTrue(characterReader.matchesAny('A', 'B'));
        assertTrue(characterReader.matchesAny('A', 'E'));
        assertFalse(characterReader.matchesAny('1', 'B'));
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader characterReader = new CharacterReader("ABCDEF");
        assertTrue(characterReader.containsIgnoreCase("AB"));
        assertTrue(characterReader.containsIgnoreCase("bc"));
        assertFalse(characterReader.containsIgnoreCase("12"));
    }
}