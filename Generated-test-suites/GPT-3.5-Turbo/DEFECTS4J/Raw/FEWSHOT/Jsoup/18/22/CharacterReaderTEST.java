
package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {
    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        assertEquals(0, reader.pos());
        reader.consume();
        assertEquals(1, reader.pos());
        reader.rewindToMark();
        assertEquals(0, reader.pos());
    }
    
    @Test
    public void testIsEmpty() {
        CharacterReader reader1 = new CharacterReader("");
        assertTrue(reader1.isEmpty());

        CharacterReader reader2 = new CharacterReader("Test");
        assertFalse(reader2.isEmpty());
    }
    
    @Test
    public void testCurrent() {
        CharacterReader reader1 = new CharacterReader("Hello");
        assertEquals('H', reader1.current());
        reader1.consume();
        assertEquals('e', reader1.current());
    }
    
    @Test
    public void testConsume() {
        CharacterReader reader1 = new CharacterReader("foo");
        assertEquals('f', reader1.consume());
        assertEquals('o', reader1.consume());
        assertEquals('o', reader1.consume());
        assertEquals(CharacterReader.EOF, reader1.consume());
    }
    
    @Test
    public void testUnconsume() {
        CharacterReader reader1 = new CharacterReader("foobar");
        reader1.consume();
        reader1.consume();
        reader1.unconsume();
        assertEquals('o', reader1.consume());
    }
    
    @Test
    public void testAdvance() {
        CharacterReader reader1 = new CharacterReader("Test");
        assertEquals('T', reader1.current());
        reader1.advance();
        assertEquals('e', reader1.current());
        reader1.advance();
        assertEquals('s', reader1.current());
    }
    
    @Test
    public void testMarkAndRewindToMark() {
        CharacterReader reader1 = new CharacterReader("Testing");
        assertEquals(0, reader1.pos());
        reader1.advance();
        reader1.mark();
        reader1.advance();
        assertEquals(2, reader1.pos());
        reader1.rewindToMark();
        assertEquals(1, reader1.pos());
    }
    
    @Test
    public void testConsumeAsString() {
        CharacterReader reader1 = new CharacterReader("Hello");
        assertEquals("H", reader1.consumeAsString());
        assertEquals("e", reader1.consumeAsString());
        reader1.consume();
        reader1.consume();
        reader1.consume();
        assertEquals(CharacterReader.EOF, reader1.consumeAsString());
    }
    
    @Test
    public void testConsumeToChar() {
        CharacterReader reader1 = new CharacterReader("Hello");
        assertEquals("Hello", reader1.consumeTo('x'));
        reader1.consume();
        assertEquals(CharacterReader.EOF, reader1.consumeTo('x'));
    }
    
    @Test
    public void testConsumeToString() {
        CharacterReader reader1 = new CharacterReader("Hello");
        assertEquals("Hello", reader1.consumeTo("x"));
        reader1.consume();
        assertEquals("", reader1.consumeTo("x"));
    }
    
    @Test
    public void testConsumeToAny() {
        CharacterReader reader1 = new CharacterReader("Hello123");
        assertEquals("Hello", reader1.consumeToAny('1', '2', '3'));
        reader1.consume();
        assertEquals("23", reader1.consumeToAny('1', '2', '3'));
    }
    
    @Test
    public void testConsumeToEnd() {
        CharacterReader reader1 = new CharacterReader("The end");
        assertEquals("The end", reader1.consumeToEnd());
        assertEquals("", reader1.consumeToEnd());
    }
    
    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader1 = new CharacterReader("Testing123");
        assertEquals("Testing", reader1.consumeLetterSequence());
        assertEquals("123", reader1.consumeLetterSequence());
    }
    
    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader1 = new CharacterReader("0x123");
        assertEquals("0x123", reader1.consumeHexSequence());
        assertEquals("", reader1.consumeHexSequence());
    }
    
    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader1 = new CharacterReader("007");
        assertEquals("007", reader1.consumeDigitSequence());
        assertEquals("", reader1.consumeDigitSequence());
    }
    
    @Test
    public void testMatches() {
        CharacterReader reader1 = new CharacterReader("Hello");
        assertTrue(reader1.matches('H'));
        assertFalse(reader1.matches('e'));
    }
    
    @Test
    public void testMatchesToString() {
        CharacterReader reader1 = new CharacterReader("Hello");
        assertTrue(reader1.matches("H"));
        assertFalse(reader1.matches("e"));
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader1 = new CharacterReader("Hello");
        assertTrue(reader1.matchesIgnoreCase("h"));
        assertFalse(reader1.matchesIgnoreCase("X"));
    }
    
    @Test
    public void testMatchesAny() {
        CharacterReader reader1 = new CharacterReader("123");
        assertTrue(reader1.matchesAny('1', '2', '3'));
        assertFalse(reader1.matchesAny('4', '5'));
    }
    
    @Test
    public void testMatchesLetter() {
        CharacterReader reader1 = new CharacterReader("Hello");
        assertTrue(reader1.matchesLetter());
        reader1.consume();
        assertFalse(reader1.matchesLetter());
    }
    
    @Test
    public void testMatchesDigit() {
        CharacterReader reader1 = new CharacterReader("123");
        assertTrue(reader1.matchesDigit());
        reader1.consume();
        assertFalse(reader1.matchesDigit());
    }
    
    @Test
    public void testMatchConsume() {
        CharacterReader reader1 = new CharacterReader("Hello");
        assertTrue(reader1.matchConsume("Hell"));
        assertFalse(reader1.matchConsume("X"));
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader1 = new CharacterReader("Hello");
        assertTrue(reader1.matchConsumeIgnoreCase("hell"));
        assertFalse(reader1.matchConsumeIgnoreCase("X"));
    }
    
    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader1 = new CharacterReader("Hello");
        assertTrue(reader1.containsIgnoreCase("hello"));
        assertFalse(reader1.containsIgnoreCase("X"));
    }
    
    @Test
    public void testToString() {
        CharacterReader reader1 = new CharacterReader("Hello");
        assertEquals("Hello", reader1.toString());
        reader1.consume();
        assertEquals("ello", reader1.toString());
    }
}
