package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

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
        reader.consume();
        reader.unconsume();
        assertEquals('e', reader.current());
    }
    
    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("test");
        reader.advance();
        assertEquals('e', reader.current());
    }
    
    @Test
    public void testMarkAndRewindToMark() {
        CharacterReader reader = new CharacterReader("test");
        reader.consume();
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        
        assertEquals('t', reader.current());
    }
    
    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("t", reader.consumeAsString());
        
        reader.consume();
        assertEquals("e", reader.consumeAsString());
        
        assertEquals("s", reader.consumeAsString());
    }
    
    @Test
    public void testNextIndexOf_char() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals(1, reader.nextIndexOf('e'));
        
        reader.consume();
        assertEquals(1, reader.nextIndexOf('e'));
        assertEquals(-1, reader.nextIndexOf('x'));
    }
    
    @Test
    public void testNextIndexOf_CharSequence() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals(1, reader.nextIndexOf("es"));
        
        reader.consume();
        assertEquals(1, reader.nextIndexOf("es"));
        assertEquals(-1, reader.nextIndexOf("ex"));
    }
    
    @Test
    public void testConsumeTo_char() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("t", reader.consumeTo('e'));
        
        reader.consume();
        assertEquals("t", reader.consumeTo('e'));
        assertEquals("", reader.consumeTo('x'));
    }
    
    @Test
    public void testConsumeTo_String() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("t", reader.consumeTo("es"));
        
        reader.consume();
        assertEquals("t", reader.consumeTo("es"));
        assertEquals("", reader.consumeTo("ex"));
    }
    
    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("", reader.consumeToAny('x', 'y', 'z'));
        
        reader.consume();
        assertEquals("t", reader.consumeToAny('e', 'y', 'z'));
        
        reader.consume();
        assertEquals("s", reader.consumeToAny('e', 'y', 'z'));
    }
    
    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("test");
        reader.consume();
        reader.consume();
        
        assertEquals("st", reader.consumeToEnd());
    }
    
    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("A123");
        assertEquals("A", reader.consumeLetterSequence());
        
        reader.consume();
        assertEquals("", reader.consumeLetterSequence());
    }
    
    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("A123");
        assertEquals("A", reader.consumeLetterThenDigitSequence());
        
        reader.consume();
        reader.consume();
        assertEquals("", reader.consumeLetterThenDigitSequence());
    }
    
    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("A123abc");
        assertEquals("A123abc", reader.consumeHexSequence());
        
        reader.consume();
        reader.consume();
        assertEquals("", reader.consumeHexSequence());
    }
    
    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("123abc");
        assertEquals("123", reader.consumeDigitSequence());
        
        reader.consume();
        reader.consume();
        assertEquals("", reader.consumeDigitSequence());
    }
    
    @Test
    public void testMatches_char() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matches('t'));
        
        reader.consume();
        assertFalse(reader.matches('t'));
        assertTrue(reader.matches('e'));
    }
    
    @Test
    public void testMatches_String() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matches("test"));
        
        reader.consume();
        assertFalse(reader.matches("test"));
        assertTrue(reader.matches("est"));
        
        reader.consume();
        assertFalse(reader.matches("est"));
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchesIgnoreCase("TeSt"));
        
        reader.consume();
        assertFalse(reader.matchesIgnoreCase("TeSt"));
        assertTrue(reader.matchesIgnoreCase("eSt"));
        
        reader.consume();
        assertFalse(reader.matchesIgnoreCase("eSt"));
    }
    
    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchesAny('t', 'e', 'x'));
        
        reader.consume();
        assertFalse(reader.matchesAny('t', 'e', 'x'));
        assertTrue(reader.matchesAny('e', 's', 'x'));
    }
    
    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchesLetter());
        
        reader.consume();
        assertFalse(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
    }
    
    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("123");
        assertTrue(reader.matchesDigit());
        
        reader.consume();
        assertFalse(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
    }
    
    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchConsume("test"));
        
        reader.consume();
        assertFalse(reader.matchConsume("test"));
        assertTrue(reader.matchConsume("est"));
        
        reader.consume();
        assertFalse(reader.matchConsume("est"));
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchConsumeIgnoreCase("TEst"));
        
        reader.consume();
        assertFalse(reader.matchConsumeIgnoreCase("TEst"));
        assertTrue(reader.matchConsumeIgnoreCase("eSt"));
        
        reader.consume();
        assertFalse(reader.matchConsumeIgnoreCase("eSt"));
    }
    
    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.containsIgnoreCase("es"));
        
        reader.consume();
        assertFalse(reader.containsIgnoreCase("es"));
        assertTrue(reader.containsIgnoreCase("s"));
    }
    
    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("test");
        reader.consume();
        assertEquals("est", reader.toString());
    }
}