package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testNewCharacterReader() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("abcdef", reader.input);
        assertEquals(5, reader.length);
        assertEquals(0, reader.pos);
        assertEquals(0, reader.mark);
        assertFalse(reader.isEmpty());
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals('b', reader.current());
        assertEquals(1, reader.pos());
    }

    @Test
    public void testConsumeAndAdvance() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        reader.consume();
        reader.advance();
        assertEquals('b', reader.current());
        assertEquals(2, reader.pos());
    }

    @Test
    public void testConsumeString() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("def", reader.consumeToEnd());
        assertEquals(6, reader.pos());
        assertEquals(6, reader.length());
    }

    @Test
    public void testConsumeToCharacter() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("def", reader.consumeTo('f'));
        assertEquals(5, reader.pos());
        assertEquals(6, reader.length());
    }

    @Test
    public void testConsumeToString() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("def", reader.consumeTo("ef"));
        assertEquals(5, reader.pos());
        assertEquals(6, reader.length());
    }

    @Test
    public void testConsumeToAnySingleCharacter() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("def", reader.consumeToAny('f'));
        assertEquals(5, reader.pos());
        assertEquals(6, reader.length());
    }

    @Test
    public void testConsumeToAnyMultipleCharacters() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("def", reader.consumeToAny('e', 'f'));
        assertEquals(5, reader.pos());
        assertEquals(6, reader.length());
    }

    @Test
    public void testConsumeToEnd() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("", reader.consumeToEnd());
        assertEquals(0, reader.pos());
        assertEquals(6, reader.length());
    }

    @Test
    public void testConsumeLetterSequence() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("def", reader.consumeLetterSequence());
        assertEquals(5, reader.pos());
        assertEquals(6, reader.length());
    }

    @Test
    public void testConsumeHexSequence() {
        String input = "abc123def";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("123", reader.consumeHexSequence());
        assertEquals(4, reader.pos());
        assertEquals(6, reader.length());
    }

    @Test
    public void testConsumeDigitSequence() {
        String input = "abc123def";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("123", reader.consumeDigitSequence());
        assertEquals(4, reader.pos());
        assertEquals(6, reader.length());
    }

    @Test
    public void testMatches() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('e'));
        assertFalse(reader.matches('z'));
    }

    @Test
    public void testMatchesString() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matches("ef"));
        assertFalse(reader.matches("xyz"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchesIgnoreCase("ef"));
        assertTrue(reader.matchesIgnoreCase("EF"));
        assertFalse(reader.matchesIgnoreCase("xyz"));
    }

    @Test
    public void testMatchesAny() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchesAny('a', 'e'));
        assertFalse(reader.matchesAny('z'));
    }

    @Test
    public void testMatchesLetter() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter('z'));
    }

    @Test
    public void testMatchesDigit() {
        String input = "abc123def";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit('z'));
    }

    @Test
    public void testMatchConsumeString() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchConsume("ef"));
        assertEquals(5, reader.pos());
    }

    @Test
    public void testMatchConsumeIgnoreCaseString() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchConsumeIgnoreCase("ef"));
        assertTrue(reader.matchConsumeIgnoreCase("EF"));
        assertEquals(5, reader.pos());
    }

    @Test
    public void testContainsIgnoreCase() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertFalse(reader.containsIgnoreCase("xyz"));
        assertTrue(reader.containsIgnoreCase("ef"));
        assertTrue(reader.containsIgnoreCase("EF"));
    }

    @Test
    public void testRewindToMark() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('a', reader.current());
        assertEquals(0, reader.pos());
    }

    @Test
    public void testEmpty() {
        String input = "";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testNullInput() {
        CharacterReader reader = new CharacterReader(null);
    }
}