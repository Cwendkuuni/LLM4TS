package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader_Empty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertNull(reader.consume());
    }

    @Test
    public void testCharacterReader_SingleCharacter() {
        CharacterReader reader = new CharacterReader("a");
        assertEquals('a', reader.consume());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_MultipleCharacters() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_MarkAndRewind() {
        CharacterReader reader = new CharacterReader("abc");
        reader.mark();
        assertEquals('a', reader.consume());
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testCharacterReader_ConsumeSubstring() {
        CharacterReader reader = new CharacterReader("abcdef");
        String consumed = reader.consumeAsString();
        assertEquals("abc", consumed);
        reader.advance();
        consumed = reader.consumeAsString();
        assertEquals("def", consumed);
    }

    @Test
    public void testCharacterReader_ConsumeToCharacter() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.advance();
        reader.advance();
        String consumed = reader.consumeTo('f');
        assertEquals("cdef", consumed);
    }

    @Test
    public void testCharacterReader_ConsumeToString() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.advance();
        reader.advance();
        String consumed = reader.consumeTo("def");
        assertEquals("cdef", consumed);
    }

    @Test
    public void testCharacterReader_ConsumeToAnyCharacter() {
        CharacterReader reader = new CharacterReader("ab1cd2ef");
        String consumed = reader.consumeToAny('1', '2');
        assertEquals("b1", consumed);
    }

    @Test
    public void testCharacterReader_ConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcdef");
        String consumed = reader.consumeToEnd();
        assertEquals("abcdef", consumed);
    }

    @Test
    public void testCharacterReader_ConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abc123def");
        String consumed = reader.consumeLetterSequence();
        assertEquals("abc", consumed);
    }

    @Test
    public void testCharacterReader_ConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("abc123defghijk0lmnop");
        String consumed = reader.consumeHexSequence();
        assertEquals("123", consumed);
    }

    @Test
    public void testCharacterReader_ConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123defghijk0lmnop");
        String consumed = reader.consumeDigitSequence();
        assertEquals("0123", consumed);
    }

    @Test
    public void testCharacterReader_MatchesCharacter() {
        CharacterReader reader = new CharacterReader("ababc");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('b'));
        assertFalse(reader.matches('c'));
    }

    @Test
    public void testCharacterReader_MatchesString() {
        CharacterReader reader = new CharacterReader("ababc");
        assertTrue(reader.matches("ab"));
        assertFalse(reader.matches("cd"));
    }

    @Test
    public void testCharacterReader_MatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abABC");
        assertTrue(reader.matchesIgnoreCase("ab"));
        assertTrue(reader.matchesIgnoreCase("Ab"));
        assertFalse(reader.matchesIgnoreCase("cd"));
    }

    @Test
    public void testCharacterReader_MatchesAnyCharacter() {
        CharacterReader reader = new CharacterReader("ab123");
        assertTrue(reader.matchesAny('1', '2'));
        assertTrue(reader.matchesAny('a', 'b'));
        assertFalse(reader.matchesAny('c', 'd'));
    }

    @Test
    public void testCharacterReader_MatchesLetter() {
        CharacterReader reader = new CharacterReader("ab123");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testCharacterReader_MatchesDigit() {
        CharacterReader reader = new CharacterReader("123ab");
        assertFalse(reader.matchesLetter());
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testCharacterReader_MatchConsumeString() {
        CharacterReader reader = new CharacterReader("ababc");
        assertTrue(reader.matchConsume("ab"));
    }

    @Test
    public void testCharacterReader_MatchConsumeIgnoreCaseString() {
        CharacterReader reader = new CharacterReader("abABC");
        assertTrue(reader.matchConsumeIgnoreCase("ab"));
        assertTrue(reader.matchConsumeIgnoreCase("Ab"));
    }

    @Test
    public void testCharacterReader_ContainsIgnoreCaseString() {
        CharacterReader reader = new CharacterReader("abABC");
        assertTrue(reader.containsIgnoreCase("ab"));
        assertTrue(reader.containsIgnoreCase("Ab"));
        assertFalse(reader.containsIgnoreCase("cd"));
    }
}