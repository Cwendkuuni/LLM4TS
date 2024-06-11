package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {
    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals(0, reader.pos());
        reader.consume();
        assertEquals(1, reader.pos());
        reader.consume();
        reader.consume();
        assertEquals(3, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        reader = new CharacterReader("abc");
        assertFalse(reader.isEmpty());
        reader.consume();
        assertFalse(reader.isEmpty());
        reader.consume();
        reader.consume();
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals('b', reader.current());
        reader.consume();
        reader.consume();
        assertEquals('d', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
        assertEquals('d', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals('f', reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        reader.unconsume();
        assertEquals('b', reader.current());
        assertEquals('b', reader.consume());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.advance();
        assertEquals('b', reader.current());
        reader.consume();
        reader.advance();
        reader.advance();
        assertEquals('e', reader.current());
    }

    @Test
    public void testMarkAndRewindToMark() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals('a', reader.consume());
        reader.mark();
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
        reader.rewindToMark();
        assertEquals('b', reader.current());
        assertEquals('b', reader.consume());
    }
    
    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("a", reader.consumeAsString());
        assertEquals("b", reader.consumeAsString());
        assertEquals("c", reader.consumeAsString());
        assertEquals("d", reader.consumeAsString());
        assertEquals("e", reader.consumeAsString());
        assertEquals("f", reader.consumeAsString());
    }
    
    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("", reader.consumeTo('a'));
        assertEquals("a", reader.consumeTo('a'));
        assertEquals("bc", reader.consumeTo('d'));
        assertEquals("d", reader.consumeTo('e'));
        assertEquals("", reader.consumeTo('e'));
    }
    
    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("abcdefgh", reader.consumeToAny('g'));
        assertEquals("", reader.consumeToAny('g'));
    }
    
    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("abcdef", reader.consumeToEnd());
    }
    
    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abc123");
        assertEquals("abc", reader.consumeLetterSequence());
        assertEquals("123", reader.consumeLetterSequence());
        assertEquals("", reader.consumeLetterSequence());
    }
    
    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("abcdefABC123$%^");
        assertEquals("abcdefABC123", reader.consumeHexSequence());
        assertEquals("$%^", reader.consumeHexSequence());
        assertEquals("", reader.consumeHexSequence());
    }
    
    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123");
        assertEquals("", reader.consumeDigitSequence());
        assertEquals("123", reader.consumeDigitSequence());
        assertEquals("", reader.consumeDigitSequence());
    }
    
    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matches('a'));
        assertFalse(reader.matches('b'));
        assertTrue(reader.matches("abc"));
        assertFalse(reader.matches("def"));
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcDEF");
        assertTrue(reader.matchesIgnoreCase("abc"));
        assertFalse(reader.matchesIgnoreCase("def"));
        assertTrue(reader.matchesIgnoreCase("ABC"));
        assertFalse(reader.matchesIgnoreCase("DEF"));
    }
    
    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matchesAny('a','b','c','d','e','f'));
        assertFalse(reader.matchesAny('g','h','i'));
    }
    
    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abc123");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }
    
    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("abc123");
        assertFalse(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
    }
    
    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matchConsume("abc"));
        assertFalse(reader.matchConsume("def"));
        assertTrue(reader.matchConsume("def"));
        assertFalse(reader.matchConsume("def"));
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcDEF");
        assertTrue(reader.matchConsumeIgnoreCase("abc"));
        assertFalse(reader.matchConsumeIgnoreCase("def"));
        assertTrue(reader.matchConsumeIgnoreCase("DEF"));
        assertFalse(reader.matchConsumeIgnoreCase("DEF"));
    }
    
    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.containsIgnoreCase("cD"));
        assertFalse(reader.containsIgnoreCase("gh"));
    }
    
    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("abcdef", reader.toString());
        reader.consume();
        assertEquals("bcdef", reader.toString());
    }
}