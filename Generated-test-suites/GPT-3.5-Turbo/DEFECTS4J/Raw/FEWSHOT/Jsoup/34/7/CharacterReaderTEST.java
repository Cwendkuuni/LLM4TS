package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {
    
    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals(0, reader.pos());
        reader.consume();
        assertEquals(1, reader.pos());
    }
    
    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        reader = new CharacterReader("test");
        assertFalse(reader.isEmpty());
    }
    
    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals('t', reader.current());
        reader.consume();
        assertEquals('e', reader.current());
    }
    
    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals('t', reader.consume());
        assertEquals('e', reader.consume());
    }
    
    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("test");
        reader.consume();
        reader.unconsume();
        assertEquals('t', reader.current());
    }
    
    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("test");
        reader.advance();
        assertEquals('e', reader.current());
    }
    
    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("test");
        reader.advance();
        reader.mark();
        assertEquals(1, reader.pos());
    }
    
    @Test
    public void testRewindToMark() {
        CharacterReader reader = new CharacterReader("test");
        reader.advance();
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals(1, reader.pos());
    }
    
    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("t", reader.consumeAsString());
        assertEquals("e", reader.consumeAsString());
    }
    
    @Test
    public void testNextIndexOf_char() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals(1, reader.nextIndexOf('e'));
        assertEquals(2, reader.nextIndexOf('s'));
        assertEquals(-1, reader.nextIndexOf('x'));
    }
    
    @Test
    public void testNextIndexOf_CharSequence() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals(1, reader.nextIndexOf("es"));
        assertEquals(3, reader.nextIndexOf("x"));
        assertEquals(-1, reader.nextIndexOf("abcd"));
    }
    
    @Test
    public void testConsumeTo_char() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("t", reader.consumeTo('e'));
        assertEquals("es", reader.consumeTo('t'));
    }
    
    @Test
    public void testConsumeTo_String() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("t", reader.consumeTo("es"));
        assertEquals("es", reader.consumeTo("x"));
    }
    
    @Test
    public void testConsumeToAny_chars() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals("ab", reader.consumeToAny('c', 'd'));
        assertEquals("cd", reader.consumeToAny('e', 'f'));
    }
    
    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("test", reader.consumeToEnd());
    }
    
    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("test", reader.consumeLetterSequence());
        reader = new CharacterReader("123test");
        assertEquals("", reader.consumeLetterSequence());
    }
    
    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("test123");
        assertEquals("test", reader.consumeLetterThenDigitSequence());
        reader = new CharacterReader("123test");
        assertEquals("", reader.consumeLetterThenDigitSequence());
    }
    
    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("abc123");
        assertEquals("abc", reader.consumeHexSequence());
        reader = new CharacterReader("123abc");
        assertEquals("", reader.consumeHexSequence());
    }
    
    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123");
        assertEquals("123", reader.consumeDigitSequence());
        reader = new CharacterReader("123abc");
        assertEquals("123", reader.consumeDigitSequence());
    }
    
    @Test
    public void testMatches_char() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matches('t'));
        assertFalse(reader.matches('e'));
    }
    
    @Test
    public void testMatches_String() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matches("test"));
        assertFalse(reader.matches("es"));
    }
    
    @Test
    public void testMatchesIgnoreCase_String() {
        CharacterReader reader = new CharacterReader("tEsT");
        assertTrue(reader.matchesIgnoreCase("test"));
        assertFalse(reader.matchesIgnoreCase("es"));
    }
    
    @Test
    public void testMatchesAny_chars() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchesAny('t','e'));
        assertFalse(reader.matchesAny('x','y'));
    }
    
    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchesLetter());
        reader.consume();
        assertFalse(reader.matchesLetter());
    }
    
    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("test");
        assertFalse(reader.matchesDigit());
        reader.consume();
        assertTrue(reader.matchesDigit());
    }
    
    @Test
    public void testMatchConsume_String() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchConsume("test"));
        assertFalse(reader.matchConsume("es"));
    }
    
    @Test
    public void testMatchConsumeIgnoreCase_String() {
        CharacterReader reader = new CharacterReader("tEsT");
        assertTrue(reader.matchConsumeIgnoreCase("test"));
        assertFalse(reader.matchConsumeIgnoreCase("es"));
    }
    
    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.containsIgnoreCase("es"));
        assertFalse(reader.containsIgnoreCase("x"));
    }
    
    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("test");
        reader.consume();
        assertEquals("est", reader.toString());
    }
}