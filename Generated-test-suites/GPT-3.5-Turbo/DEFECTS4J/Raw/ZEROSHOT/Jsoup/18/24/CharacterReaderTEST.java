package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        // Test when the position is at the beginning of the input
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals(0, reader.pos());
        
        // Test when the position is in the middle of the input
        reader.advance();
        reader.advance();
        reader.advance();
        assertEquals(3, reader.pos());
        
        // Test when the position is at the end of the input
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        assertEquals(6, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        // Test when the position is at the beginning of the input
        CharacterReader reader = new CharacterReader("abcdef");
        assertFalse(reader.isEmpty());
        
        // Test when the position is in the middle of the input
        reader.advance();
        reader.advance();
        reader.advance();
        assertFalse(reader.isEmpty());
        
        // Test when the position is at the end of the input
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        // Test when the position is at the beginning of the input
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals('a', reader.current());
        
        // Test when the position is in the middle of the input
        reader.advance();
        reader.advance();
        reader.advance();
        assertEquals('d', reader.current());

        // Test when the position is at the end of the input
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testConsume() {
        // Test when consuming characters from the beginning of the input
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals('a', reader.consume());
        assertEquals(1, reader.pos());
        
        assertEquals('b', reader.consume());
        assertEquals(2, reader.pos());
        
        assertEquals('c', reader.consume());
        assertEquals(3, reader.pos());

        // Test when consuming characters from the middle of the input
        reader.advance();
        reader.advance();
        assertEquals('e', reader.consume());
        assertEquals(5, reader.pos());

        // Test when consuming characters from the end of the input
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        assertEquals('f', reader.consume());
        assertEquals(6, reader.pos());
        
        // Test when attempting to consume a character when the input is already empty
        assertEquals(CharacterReader.EOF, reader.consume());
        assertEquals(6, reader.pos());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.advance();
        reader.advance();
        
        reader.unconsume();
        assertEquals(1, reader.pos());
        
        reader.unconsume();
        assertEquals(0, reader.pos());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("abcdef");

        reader.advance();
        assertEquals(1, reader.pos());
        
        reader.advance();
        reader.advance();
        assertEquals(3, reader.pos());
        
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        assertEquals(6, reader.pos());
        
        reader.advance();
        assertEquals(6, reader.pos());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("abcdef");

        reader.mark();
        assertEquals(0, reader.pos());
        
        reader.advance();
        reader.advance();
        reader.mark();
        assertEquals(2, reader.pos());

        reader.unconsume();
        assertEquals(2, reader.pos());
        
        reader.rewindToMark();
        assertEquals(2, reader.pos());
        
        reader.advance();
        assertEquals(3, reader.pos());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abcdef");

        assertEquals("a", reader.consumeAsString());
        assertEquals(1, reader.pos());
        
        reader.advance();
        reader.advance();
        assertEquals("d", reader.consumeAsString());
        assertEquals(3, reader.pos());

        reader.advance();
        reader.advance();
        reader.advance();
        assertEquals("f", reader.consumeAsString());
        assertEquals(6, reader.pos());
        
        assertEquals("", reader.consumeAsString());
        assertEquals(6, reader.pos());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abcdef");

        assertEquals("abc", reader.consumeTo('d'));
        assertEquals(3, reader.pos());
        
        assertEquals("", reader.consumeTo('d'));
        assertEquals(3, reader.pos());
        
        assertEquals("", reader.consumeTo('f'));
        assertEquals(6, reader.pos());
        
        assertEquals("ef", reader.consumeTo('g'));
        assertEquals(6, reader.pos());
        
        assertEquals("", reader.consumeTo('g'));
        assertEquals(6, reader.pos());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcdef");

        assertEquals("abc", reader.consumeToAny('d', 'e', 'f'));
        assertEquals(3, reader.pos());
        
        assertEquals("", reader.consumeToAny('d', 'e', 'f'));
        assertEquals(3, reader.pos());
        
        assertEquals("", reader.consumeToAny('g'));
        assertEquals(6, reader.pos());
        
        assertEquals("", reader.consumeToAny('g', 'h', 'i'));
        assertEquals(6, reader.pos());
        
        assertEquals("ef", reader.consumeToAny('g', 'h', 'i', 'j'));
        assertEquals(6, reader.pos());
        
        assertEquals("", reader.consumeToAny('g', 'h', 'i', 'j'));
        assertEquals(6, reader.pos());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcdef");

        assertEquals("abcdef", reader.consumeToEnd());
        assertEquals(6, reader.pos());
        
        assertEquals("", reader.consumeToEnd());
        assertEquals(6, reader.pos());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcdef");

        assertEquals("abcdef", reader.consumeLetterSequence());
        assertEquals(6, reader.pos());
        
        reader.advance();
        reader.advance();
        assertEquals("", reader.consumeLetterSequence());
        assertEquals(8, reader.pos());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("abcdef123456");

        assertEquals("abcdef123456", reader.consumeHexSequence());
        assertEquals(12, reader.pos());
        
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        assertEquals("", reader.consumeHexSequence());
        assertEquals(16, reader.pos());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("abcdef");

        assertEquals("", reader.consumeDigitSequence());
        assertEquals(0, reader.pos());
        
        reader.advance();
        reader.advance();
        reader.advance();
        assertEquals("", reader.consumeDigitSequence());
        assertEquals(3, reader.pos());
        
        reader.advance();
        reader.advance();
        reader.advance();
        assertEquals("ef", reader.consumeDigitSequence());
        assertEquals(6, reader.pos());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abcdef");

        assertFalse(reader.matches('b'));
        reader.advance();
        assertTrue(reader.matches('b'));

        assertFalse(reader.matches("def"));
        reader.advance();
        reader.advance();
        assertTrue(reader.matches("def"));

        assertFalse(reader.matchesIgnoreCase("DEF"));
        assertTrue(reader.matchesIgnoreCase("DEF"));

        assertFalse(reader.matchesAny('g', 'h', 'i'));
        reader.advance();
        assertTrue(reader.matchesAny('g', 'h', 'i'));

        assertFalse(reader.matchesLetter());
        reader.advance();
        reader.advance();
        assertTrue(reader.matchesLetter());

        assertFalse(reader.matchesDigit());
        reader.advance();
        reader.advance();
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("abcdef");

        assertFalse(reader.matchConsume("def"));
        assertFalse(reader.matchConsume("DEF"));
        assertFalse(reader.matchConsumeIgnoreCase("DEF"));
        
        reader.advance();
        reader.advance();
        reader.advance();
        assertTrue(reader.matchConsume("def"));
        
        reader.advance();
        reader.advance();
        assertFalse(reader.matchConsume("def"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcdef");

        assertFalse(reader.containsIgnoreCase("bcd"));
        assertFalse(reader.containsIgnoreCase("BCD"));
        assertFalse(reader.containsIgnoreCase("DEF"));

        reader.advance();
        reader.advance();
        reader.advance();
        assertTrue(reader.containsIgnoreCase("bcd"));
        
        reader.advance();
        reader.advance();
        assertFalse(reader.containsIgnoreCase("bcd"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("abcdef", reader.toString());

        reader.advance();
        reader.advance();
        assertEquals("cdef", reader.toString());
        
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        assertEquals("", reader.toString());
    }
}