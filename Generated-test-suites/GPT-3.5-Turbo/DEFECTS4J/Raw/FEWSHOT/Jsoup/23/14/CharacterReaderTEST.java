package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {
    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals(0, reader.pos());
        
        reader.consume();
        assertEquals(1, reader.pos());
        
        reader.consume();
        assertEquals(2, reader.pos());
    }
    
    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        
        reader = new CharacterReader("test");
        assertFalse(reader.isEmpty());
        
        reader.consume();
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
        CharacterReader reader = new CharacterReader("test");
        assertEquals('t', reader.current());
        
        reader.consume();
        assertEquals('e', reader.current());
        
        reader.consume();
        assertEquals('s', reader.current());
        
        reader.consume();
        assertEquals('t', reader.current());
        
        reader.consume();
        assertEquals(CharacterReader.EOF, reader.current());
    }
    
    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals('t', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals('s', reader.consume());
        assertEquals('t', reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());
    }
    
    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("test");
        reader.consume();
        reader.consume();
        reader.unconsume();
        assertEquals('s', reader.consume());
        assertEquals('t', reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());
    }
    
    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("test");
        reader.consume();
        reader.advance();
        assertEquals('s', reader.consume());
        assertEquals('t', reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());
    }
    
    @Test
    public void testMarkAndRewindToMark() {
        CharacterReader reader = new CharacterReader("test");
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('t', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals('s', reader.consume());
        assertEquals('t', reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());
    }
    
    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("t", reader.consumeAsString());
        assertEquals("e", reader.consumeAsString());
        assertEquals("s", reader.consumeAsString());
        assertEquals("t", reader.consumeAsString());
        assertEquals("", reader.consumeAsString());
    }
    
    @Test
    public void testConsumeToChar() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("test", reader.consumeTo(' '));
        
        reader = new CharacterReader("test test");
        assertEquals("test", reader.consumeTo(' '));
        
        reader = new CharacterReader("test test");
        reader.consumeTo(' ');
        assertEquals("test", reader.consumeTo(' '));
        
        reader = new CharacterReader("test");
        assertNull(reader.consumeTo(' '));
    }
    
    @Test
    public void testConsumeToString() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("test", reader.consumeTo(" "));
        
        reader = new CharacterReader("test test");
        assertEquals("test", reader.consumeTo(" "));
        
        reader = new CharacterReader("test test");
        reader.consumeTo(" ");
        assertEquals("test", reader.consumeTo(" "));
        
        reader = new CharacterReader("test");
        assertNull(reader.consumeTo(" "));
    }
    
    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("", reader.consumeToAny(' '));
        
        reader = new CharacterReader("test test");
        assertEquals("test", reader.consumeToAny(' '));
        
        reader = new CharacterReader("test test");
        reader.consumeTo(' ');
        assertEquals("test", reader.consumeToAny(' '));
        
        reader = new CharacterReader("test");
        assertEquals("test", reader.consumeToAny('t'));
        
        reader = new CharacterReader("test");
        assertNull(reader.consumeToAny('z'));
    }
    
    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("test", reader.consumeToEnd());
        
        reader = new CharacterReader("test");
        reader.consumeTo(' ');
        assertEquals("est", reader.consumeToEnd());
    }
    
    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("test", reader.consumeLetterSequence());
        
        reader = new CharacterReader("test 123");
        assertEquals("test", reader.consumeLetterSequence());
        
        reader = new CharacterReader("123 test");
        assertEquals("", reader.consumeLetterSequence());
    }
    
    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("0123456789ABCDEFabcdef");
        assertEquals("0123456789ABCDEFabcdef", reader.consumeHexSequence());
        
        reader = new CharacterReader("0123456789ABCDEFabcdef ");
        assertEquals("0123456789ABCDEFabcdef", reader.consumeHexSequence());
        
        reader = new CharacterReader("01234G");
        assertEquals("01234", reader.consumeHexSequence());
    }
    
    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("0123456789");
        assertEquals("0123456789", reader.consumeDigitSequence());
        
        reader = new CharacterReader("0123456789 ");
        assertEquals("0123456789", reader.consumeDigitSequence());
        
        reader = new CharacterReader("01234G");
        assertEquals("01234", reader.consumeDigitSequence());
    }
    
    @Test
    public void testMatchesChar() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matches('t'));
        assertFalse(reader.matches('e'));
        
        reader.consume();
        assertFalse(reader.matches('t'));
        assertTrue(reader.matches('e'));
    }
    
    @Test
    public void testMatchesString() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matches("test"));
        assertFalse(reader.matches("tes"));
        
        reader.consume();
        assertFalse(reader.matches("test"));
        assertTrue(reader.matches("est"));
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("tEsT");
        assertTrue(reader.matchesIgnoreCase("test"));
        assertFalse(reader.matchesIgnoreCase("TEs"));
        
        reader.consume();
        assertFalse(reader.matchesIgnoreCase("test"));
        assertTrue(reader.matchesIgnoreCase("Est"));
    }
    
    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchesAny('t', 'e'));
        assertFalse(reader.matchesAny('a', 'b'));
        
        reader.consume();
        assertFalse(reader.matchesAny('t', 'e'));
        assertTrue(reader.matchesAny('e', 's'));
    }
    
    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchesLetter());
        
        reader.consume();
        assertFalse(reader.matchesLetter());
        
        reader.consume();
        assertFalse(reader.matchesLetter());
    }
    
    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("123");
        assertTrue(reader.matchesDigit());
        
        reader.consume();
        assertFalse(reader.matchesDigit());
        
        reader.consume();
        assertFalse(reader.matchesDigit());
    }
    
    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchConsume("test"));
        assertFalse(reader.matchConsume("tes"));
        
        reader = new CharacterReader("test");
        assertFalse(reader.matchConsume("foo"));
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("tEsT");
        assertTrue(reader.matchConsumeIgnoreCase("test"));
        assertFalse(reader.matchConsumeIgnoreCase("tes"));
        
        reader = new CharacterReader("tEsT");
        assertFalse(reader.matchConsumeIgnoreCase("foo"));
    }
    
    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.containsIgnoreCase("test"));
        assertTrue(reader.containsIgnoreCase("TeSt"));
        assertTrue(reader.containsIgnoreCase("EST"));
        assertFalse(reader.containsIgnoreCase("tes"));
    }
}