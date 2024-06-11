package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {
    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("");
        assertEquals(0, reader.pos());
        
        reader = new CharacterReader("abc");
        assertEquals(0, reader.pos());
        
        reader.consume();
        assertEquals(1, reader.pos());
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
        assertFalse(reader.isEmpty());
        
        reader.consume();
        assertTrue(reader.isEmpty());
    }
    
    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("");
        assertEquals((char) -1, reader.current());
        
        reader = new CharacterReader("abc");
        assertEquals('a', reader.current());
        
        reader.consume();
        assertEquals('b', reader.current());
    }
    
    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abc");
        
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
        assertEquals((char) -1, reader.consume());
    }
    
    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("abc");
        
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        
        reader.unconsume();
        assertEquals('b', reader.consume());
        
        reader.unconsume();
        assertEquals('b', reader.consume());
        
        reader.unconsume();
        assertEquals('a', reader.consume());
    }
    
    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("abc");
        
        assertEquals('a', reader.current());
        
        reader.advance();
        
        assertEquals('b', reader.current());
        
        reader.advance();
        reader.advance();
        
        assertEquals((char) -1, reader.current());
    }
    
    @Test
    public void testMarkAndRewindToMark() {
        CharacterReader reader = new CharacterReader("abc");
        
        reader.mark();
        assertEquals(0, reader.pos());
        
        reader.advance();
        reader.advance();
        
        reader.rewindToMark();
        assertEquals(0, reader.pos());
        
        reader.advance();
        reader.advance();
        reader.advance();
        
        reader.mark();
        assertEquals(3, reader.pos());
    }
    
    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("a", reader.consumeAsString());
        
        reader.advance();
        assertEquals("c", reader.consumeAsString());
    }
    
    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abc");
        
        assertEquals("ab", reader.consumeTo('c'));
        assertEquals('c', reader.current());
        
        reader.advance();
        assertEquals("", reader.consumeTo('d'));
    }
    
    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcd");
        
        assertEquals("ab", reader.consumeToAny('c', 'd'));
        assertEquals('c', reader.current());
        
        reader.advance();
        assertEquals("", reader.consumeToAny('e', 'f'));
    }
    
    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("abc", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
        
        reader = new CharacterReader("abc\r\n");
        assertEquals("abc\n", reader.consumeToEnd());
    }
    
    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abc123");
        assertEquals("abc", reader.consumeLetterSequence());
        assertEquals('1', reader.current());
        
        reader.advance();
        assertEquals("", reader.consumeLetterSequence());
    }
    
    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("abc123");
        assertEquals("", reader.consumeHexSequence());
        assertEquals("abc", reader.consumeLetterSequence());
        
        reader.advance();
        assertEquals("123", reader.consumeHexSequence());
        assertEquals((char) -1, reader.current());
    }
    
    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123");
        assertEquals("", reader.consumeDigitSequence());
        assertEquals("abc", reader.consumeLetterSequence());
        
        reader.advance();
        assertEquals("123", reader.consumeDigitSequence());
        assertEquals((char) -1, reader.current());
    }
    
    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matches('a'));
        assertFalse(reader.matches('b'));
        
        reader.advance();
        
        assertFalse(reader.matches('a'));
        assertTrue(reader.matches('b'));
    }
    
    @Test
    public void testMatchesString() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matches("abc"));
        assertFalse(reader.matches("def"));
        
        reader.advance();
        
        assertFalse(reader.matches("abc"));
        assertTrue(reader.matches("bc"));
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABC");
        assertTrue(reader.matchesIgnoreCase("abc"));
        assertFalse(reader.matchesIgnoreCase("def"));
        
        reader.advance();
        
        assertFalse(reader.matchesIgnoreCase("abc"));
        assertTrue(reader.matchesIgnoreCase("bc"));
    }
    
    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesAny('a', 'b', 'c'));
        assertFalse(reader.matchesAny('d'));
        
        reader.advance();
        
        assertFalse(reader.matchesAny('a', 'b', 'c'));
        assertTrue(reader.matchesAny('b'));
    }
    
    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abc123");
        assertTrue(reader.matchesLetter());
        
        reader.advance();
        
        assertFalse(reader.matchesLetter());
    }
    
    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("abc123");
        assertFalse(reader.matchesDigit());
        
        reader.advance();
        
        assertTrue(reader.matchesDigit());
    }
    
    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchConsume("ab"));
        assertEquals('c', reader.current());
        
        assertFalse(reader.matchConsume("bc"));
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABC");
        assertTrue(reader.matchConsumeIgnoreCase("ab"));
        assertEquals('C', reader.current());
        
        assertFalse(reader.matchConsumeIgnoreCase("bc"));
    }
    
    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABC");
        assertTrue(reader.containsIgnoreCase("a"));
        assertFalse(reader.containsIgnoreCase("d"));
        
        assertTrue(reader.containsIgnoreCase("A"));
        assertFalse(reader.containsIgnoreCase("D"));
        
        assertFalse(reader.containsIgnoreCase("B"));
    }
    
    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("abc", reader.toString());
        
        reader.advance();
        assertEquals("bc", reader.toString());
    }
}