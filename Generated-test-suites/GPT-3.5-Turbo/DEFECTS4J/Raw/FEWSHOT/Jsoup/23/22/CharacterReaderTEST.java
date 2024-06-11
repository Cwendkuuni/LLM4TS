
package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {
    
    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals(0, reader.pos());
        reader.consume();
        assertEquals(1, reader.pos());
    }
    
    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        reader.consume();
        assertTrue(reader.isEmpty());
    }
    
    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals('b', reader.current());
    }
    
    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
    }
    
    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("abcdefg");
        reader.consume();
        reader.consume();
        reader.unconsume();
        assertEquals('b', reader.current());
    }
    
    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("abcdefg");
        reader.advance();
        assertEquals('b', reader.current());
    }
    
    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("abcdefg");
        reader.advance();
        reader.mark();
        assertEquals(1, reader.pos());
    }
    
    @Test
    public void testRewindToMark() {
        CharacterReader reader = new CharacterReader("abcdefg");
        reader.advance();
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals(1, reader.pos());
    }
    
    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals("a", reader.consumeAsString());
        assertEquals("b", reader.consumeAsString());
    }
    
    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals("abc", reader.consumeTo('d'));
        assertEquals("defg", reader.consumeTo('z'));
    }
    
    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals("abc", reader.consumeToAny('d', 'e', 'f'));
        assertEquals("g", reader.consumeToAny('z'));
    }
    
    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals("abcdefg", reader.consumeToEnd());
    }
    
    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abc123def456");
        assertEquals("abc", reader.consumeLetterSequence());
        assertEquals("def", reader.consumeLetterSequence());
    }
    
    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("abc123def456");
        assertEquals("123", reader.consumeHexSequence());
        assertEquals("def", reader.consumeHexSequence());
    }
    
    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123def456");
        assertEquals("123", reader.consumeDigitSequence());
        assertEquals("456", reader.consumeDigitSequence());
    }
    
    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matches('a'));
        assertFalse(reader.matches('b'));
    }
    
    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesAny('a', 'b'));
        assertFalse(reader.matchesAny('d', 'e'));
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
        CharacterReader reader = new CharacterReader("abc123");
        assertTrue(reader.matchConsume("abc"));
        assertFalse(reader.matchConsume("xyz"));
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("abc123");
        assertTrue(reader.matchConsumeIgnoreCase("ABC"));
        assertFalse(reader.matchConsumeIgnoreCase("XYZ"));
    }
    
    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abc123");
        assertTrue(reader.containsIgnoreCase("ABC"));
        assertFalse(reader.containsIgnoreCase("XYZ"));
    }
    
    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals("abcdefg", reader.toString());
    }
}
