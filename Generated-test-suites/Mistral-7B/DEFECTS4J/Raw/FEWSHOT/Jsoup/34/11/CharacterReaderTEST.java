package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.jsoup.helper.Validate;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader_EmptyString() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertNull(reader.consume());
    }

    @Test
    public void testCharacterReader_SimpleConsumption() {
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
    public void testCharacterReader_MarkAndRewind() {
        CharacterReader reader = new CharacterReader("abc");
        reader.mark();
        assertEquals('a', reader.current());
        reader.advance();
        assertEquals('b', reader.current());
        reader.rewindToMark();
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals('b', reader.current());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_ConsumeAsString() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("a", reader.consumeAsString());
        assertEquals("b", reader.consumeAsString());
        assertEquals("c", reader.consumeAsString());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_ConsumeToChar() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("bcde", reader.consumeTo('f'));
        assertEquals("", reader.consumeTo('z'));
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_ConsumeToString() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("bcde", reader.consumeTo("f"));
        assertEquals("", reader.consumeTo("z"));
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_ConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcdabcdabcd");
        assertEquals("bc", reader.consumeToAny('b', 'c'));
        assertEquals("d", reader.consumeToAny('d'));
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_ConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("abc", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_ConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcd123efghikl");
        assertEquals("abcd", reader.consumeLetterSequence());
        assertEquals("efghikl", reader.consumeLetterSequence());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_ConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("abcd123efghikl");
        assertEquals("abcd123", reader.consumeLetterThenDigitSequence());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_ConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("a1b2c3d4e5f6");
        assertEquals("a1b2", reader.consumeHexSequence());
        assertEquals("c3d4e5f6", reader.consumeHexSequence());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_ConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("1234567890");
        assertEquals("1234567890", reader.consumeDigitSequence());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_Matches() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('b'));
        assertTrue(reader.matches('c'));
        assertFalse(reader.matches('d'));
        assertTrue(reader.matches("ab"));
        assertFalse(reader.matches("cd"));
    }

    @Test
    public void testCharacterReader_MatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesIgnoreCase("ab"));
        assertTrue(reader.matchesIgnoreCase("C"));
        assertFalse(reader.matchesIgnoreCase("d"));
    }

    @Test
    public void testCharacterReader_MatchesAny() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matchesAny('a', 'b'));
        assertTrue(reader.matchesAny('c', 'd'));
        assertFalse(reader.matchesAny('e'));
    }

    @Test
    public void testCharacterReader_MatchesLetter() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testCharacterReader_MatchesDigit() {
        CharacterReader reader = new CharacterReader("123");
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testCharacterReader_MatchConsume() {
        CharacterReader reader = new CharacterReader("abcd");
        reader.matchConsume("ab");
        assertEquals("cd", reader.consumeLetterSequence());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_MatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABCD");
        reader.matchConsumeIgnoreCase("ab");
        assertEquals("CD", reader.consumeLetterSequence());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_ContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcdabcdabcd");
        assertTrue(reader.containsIgnoreCase("bc"));
        assertTrue(reader.containsIgnoreCase("cd"));
        assertTrue(reader.containsIgnoreCase("AB"));
        assertFalse(reader.containsIgnoreCase("ef"));
    }
}