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
        assertNull(reader.current());
    }

    @Test
    public void testCharacterReader_SingleCharacter() {
        CharacterReader reader = new CharacterReader("a");
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_MultipleCharacters() {
        CharacterReader reader = new CharacterReader("ab");
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals('b', reader.current());
        assertEquals('b', reader.consume());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_MarkAndRewind() {
        CharacterReader reader = new CharacterReader("abc");
        reader.mark();
        assertEquals('a', reader.current());
        reader.advance();
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testCharacterReader_ConsumeAsString() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals("ab", reader.consumeAsString());
        assertEquals('c', reader.current());
    }

    @Test
    public void testCharacterReader_ConsumeTo() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals("ab", reader.consumeTo('b'));
        assertEquals('d', reader.current());
    }

    @Test
    public void testCharacterReader_ConsumeToNull() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals("abcd", reader.consumeTo(null));
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_ConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals("cd", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_ConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcd123efg");
        assertEquals("123", reader.consumeToAny('1', '3'));
        assertEquals('e', reader.current());
    }

    @Test
    public void testCharacterReader_ConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcd123efg");
        assertEquals("abc", reader.consumeLetterSequence());
        assertEquals('d', reader.current());
    }

    @Test
    public void testCharacterReader_ConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("abcd123efg");
        assertEquals("12", reader.consumeHexSequence());
        assertEquals('e', reader.current());
    }

    @Test
    public void testCharacterReader_ConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("abcd123efg");
        assertEquals("123", reader.consumeDigitSequence());
        assertEquals('e', reader.current());
    }

    @Test
    public void testCharacterReader_Matches() {
        CharacterReader reader = new CharacterReader("abcd123efg");
        assertTrue(reader.matches('a'));
        assertFalse(reader.matches('z'));
    }

    @Test
    public void testCharacterReader_MatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcd123efg");
        assertTrue(reader.matchesIgnoreCase("a"));
        assertTrue(reader.matchesIgnoreCase("A"));
        assertFalse(reader.matchesIgnoreCase("z"));
    }

    @Test
    public void testCharacterReader_MatchesAny() {
        CharacterReader reader = new CharacterReader("abcd123efg");
        assertTrue(reader.matchesAny('a', 'b'));
        assertFalse(reader.matchesAny('z'));
    }

    @Test
    public void testCharacterReader_MatchesLetter() {
        CharacterReader reader = new CharacterReader("abcd123efg");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testCharacterReader_MatchesDigit() {
        CharacterReader reader = new CharacterReader("abcd123efg");
        assertFalse(reader.matchesLetter());
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testCharacterReader_MatchConsume() {
        CharacterReader reader = new CharacterReader("abcd123efg");
        assertTrue(reader.matchConsume("ab"));
    }

    @Test
    public void testCharacterReader_MatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcd123efg");
        assertTrue(reader.matchConsumeIgnoreCase("AB"));
    }

    @Test
    public void testCharacterReader_ContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcd<title>123efg</title>");
        assertTrue(reader.containsIgnoreCase("title"));
        assertFalse(reader.containsIgnoreCase("Title"));
    }
}