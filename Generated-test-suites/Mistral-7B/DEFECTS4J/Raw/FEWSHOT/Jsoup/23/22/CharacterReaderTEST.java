package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader_EmptyInput() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertNull(reader.current());
    }

    @Test
    public void testCharacterReader_SingleCharacterInput() {
        CharacterReader reader = new CharacterReader("a");
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_MultipleCharacterInput() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals('b', reader.current());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.current());
        assertEquals('c', reader.consume());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_ConsumeString() {
        CharacterReader reader = new CharacterReader("abc");
        String consumed = reader.consumeAsString();
        assertEquals("bc", consumed);
        assertEquals('c', reader.current());
        assertEquals("bc", reader.consumeTo("bc"));
        assertEquals("", reader.consumeTo("xyz"));
    }

    @Test
    public void testCharacterReader_ConsumeToCharacter() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("def", reader.consumeTo('f'));
        assertEquals("ef", reader.consumeTo('e'));
        assertEquals("", reader.consumeTo('z'));
    }

    @Test
    public void testCharacterReader_ConsumeToString() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("def", reader.consumeTo("ef"));
        assertEquals("", reader.consumeTo("xyz"));
    }

    @Test
    public void testCharacterReader_ConsumeToAnyCharacter() {
        CharacterReader reader = new CharacterReader("123abcd123efgh");
        assertEquals("cd", reader.consumeToAny('c', 'd'));
        assertEquals("efgh", reader.consumeToAny('e', 'h'));
        assertEquals("", reader.consumeToAny('i', 'z'));
    }

    @Test
    public void testCharacterReader_ConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("def", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReader_ConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abc123def");
        assertEquals("def", reader.consumeLetterSequence());
        assertEquals("123", reader.consumeLetterSequence());
        assertEquals("", reader.consumeLetterSequence());
    }

    @Test
    public void testCharacterReader_ConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("ab12cdef13");
        assertEquals("def13", reader.consumeHexSequence());
        assertEquals("12", reader.consumeHexSequence());
        assertEquals("", reader.consumeHexSequence());
    }

    @Test
    public void testCharacterReader_ConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("1234567890");
        assertEquals("0", reader.consumeDigitSequence());
        assertEquals("", reader.consumeDigitSequence());
    }

    @Test
    public void testCharacterReader_Matches() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches("ab"));
        assertFalse(reader.matches('z'));
    }

    @Test
    public void testCharacterReader_MatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABCDEF");
        assertTrue(reader.matchesIgnoreCase("ab"));
        assertTrue(reader.matchesIgnoreCase("Ab"));
        assertFalse(reader.matchesIgnoreCase("z"));
    }

    @Test
    public void testCharacterReader_MatchesAny() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matchesAny('a', 'e'));
        assertFalse(reader.matchesAny('z', 'z'));
    }

    @Test
    public void testCharacterReader_MatchesLetter() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testCharacterReader_MatchesDigit() {
        CharacterReader reader = new CharacterReader("1234567890");
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testCharacterReader_ContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcABCdef");
        assertTrue(reader.containsIgnoreCase("ab"));
        assertTrue(reader.containsIgnoreCase("AB"));
        assertFalse(reader.containsIgnoreCase("xyz"));
    }

    @Test
    public void testCharacterReader_PositionMarking() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }
}