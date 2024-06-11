package org.jsoup.parser;
import static org.junit.Assert.*;

import org.junit.Test;

public class CharacterReaderTest {
    
    // Test isEmpty() method
    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        
        reader = new CharacterReader("abc");
        assertFalse(reader.isEmpty());
    }
    
    // Test current() method
    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals('a', reader.current());
        
        reader.consume();
        assertEquals('b', reader.current());
        
        reader.consume();
        assertEquals('c', reader.current());
        
        reader.consume();
        assertEquals((char) -1, reader.current());
    }
    
    // Test consume() method
    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
        assertEquals((char) -1, reader.consume());
    }
    
    // Test unconsume() method
    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("abc");
        reader.consume();
        reader.unconsume();
        assertEquals('a', reader.current());
    }
    
    // Test advance() method
    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("abc");
        reader.advance();
        assertEquals('b', reader.current());
    }
    
    // Test mark() and rewindToMark() methods
    @Test
    public void testMarkAndRewindToMark() {
        CharacterReader reader = new CharacterReader("abc");
        reader.advance();
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('b', reader.current());
    }
    
    // Test consumeAsString() method
    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("a", reader.consumeAsString());
        assertEquals("b", reader.consumeAsString());
        assertEquals("c", reader.consumeAsString());
    }
    
    // Test consumeTo(char) method
    @Test
    public void testConsumeToChar() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("abc", reader.consumeTo('d'));
        assertEquals("def", reader.consumeToEnd());
    }
    
    // Test consumeTo(String) method
    @Test
    public void testConsumeToString() {
        CharacterReader reader = new CharacterReader("abc123def");
        assertEquals("abc", reader.consumeTo("123"));
        assertEquals("def", reader.consumeToEnd());
    }
    
    // Test consumeToAny(char...) method
    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abc123def");
        assertEquals("abc", reader.consumeToAny('1', '2', '3'));
        assertEquals("123", reader.consumeToAny('d', 'e', 'f'));
        assertEquals("def", reader.consumeToEnd());
    }
    
    // Test consumeToEnd() method
    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("abc", reader.consumeToEnd());
    }
    
    // Test consumeLetterSequence() method
    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abc123");
        assertEquals("abc", reader.consumeLetterSequence());
    }
    
    // Test consumeHexSequence() method
    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("ab123");
        assertEquals("ab", reader.consumeHexSequence());
    }
    
    // Test consumeDigitSequence() method
    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("123abc");
        assertEquals("123", reader.consumeDigitSequence());
    }
    
    // Test matches(char) method
    @Test
    public void testMatchesChar() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matches('a'));
        assertFalse(reader.matches('b'));
    }
    
    // Test matches(String) method
    @Test
    public void testMatchesString() {
        CharacterReader reader = new CharacterReader("abc123");
        assertTrue(reader.matches("abc"));
        assertFalse(reader.matches("def"));
    }
    
    // Test matchesIgnoreCase(String) method
    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abc123");
        assertTrue(reader.matchesIgnoreCase("AbC"));
        assertFalse(reader.matchesIgnoreCase("def"));
    }
    
    // Test matchesAny(char...) method
    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abc123");
        assertTrue(reader.matchesAny('a', 'b', 'c'));
        assertFalse(reader.matchesAny('d', 'e', 'f'));
    }
    
    // Test matchesLetter() method
    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }
    
    // Test matchesDigit() method
    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("123");
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }
    
    // Test matchConsume(String) method
    @Test
    public void testMatchConsumeString() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchConsume("abc"));
        assertFalse(reader.matchConsume("def"));
    }
    
    // Test matchConsumeIgnoreCase(String) method
    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchConsumeIgnoreCase("AbC"));
        assertFalse(reader.matchConsumeIgnoreCase("def"));
    }
    
    // Test containsIgnoreCase(String) method
    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcDEF");
        assertTrue(reader.containsIgnoreCase("abc"));
        assertFalse(reader.containsIgnoreCase("ghi"));
    }
}