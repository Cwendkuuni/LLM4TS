package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {
    @Test
    public void testConstructor() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals(0, reader.pos());
        assertFalse(reader.isEmpty());
        assertEquals('t', reader.current());
    }
    
    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals('t', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals('s', reader.consume());
        assertEquals('t', reader.consume());
        assertEquals('\uFFFF', reader.consume()); //EOF
    }
    
    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals('t', reader.consume());
        assertEquals('e', reader.consume());
        reader.unconsume();
        assertEquals('e', reader.consume());
        reader.unconsume();
        assertEquals('e', reader.consume());
        assertEquals('s', reader.consume());
    }
    
    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("test");
        reader.advance();
        assertEquals('e', reader.current());
        reader.advance();
        assertEquals('s', reader.current());
        reader.advance();
        assertEquals('t', reader.current());
        reader.advance();
        assertEquals('\uFFFF', reader.current()); //EOF
    }
    
    @Test
    public void testMarkAndRewindToMark() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals('t', reader.consume());
        reader.mark();
        assertEquals('e', reader.consume());
        assertEquals('s', reader.consume());
        reader.rewindToMark();
        assertEquals('e', reader.consume());
        assertEquals('s', reader.consume());
        assertEquals('t', reader.consume());
        assertEquals('\uFFFF', reader.consume()); //EOF
    }
    
    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("t", reader.consumeAsString());
        assertEquals("e", reader.consumeAsString());
        assertEquals("s", reader.consumeAsString());
        assertEquals("t", reader.consumeAsString());
        assertEquals("", reader.consumeAsString()); //EOF
    }
    
    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals(1, reader.nextIndexOf('e'));
        assertEquals(2, reader.nextIndexOf('s'));
        assertEquals(3, reader.nextIndexOf('t'));
        assertEquals(-1, reader.nextIndexOf('x'));
    }
    
    @Test
    public void testNextIndexOfWithSeq() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals(1, reader.nextIndexOf("es"));
        assertEquals(2, reader.nextIndexOf("st"));
        assertEquals(3, reader.nextIndexOf("te"));
        assertEquals(0, reader.nextIndexOf("t"));
        assertEquals(-1, reader.nextIndexOf("x"));
    }
    
    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("t", reader.consumeTo('e'));
        assertEquals("", reader.consumeTo('e'));
        assertEquals("", reader.consumeTo('e'));
        assertEquals("", reader.consumeTo('e'));
        assertEquals("est", reader.consumeTo('A'));
        assertEquals("", reader.consumeTo('A'));
    }
    
    @Test
    public void testConsumeToWithSeq() {
        CharacterReader reader = new CharacterReader("test me");
        assertEquals("t", reader.consumeTo("te"));
        assertEquals(" ", reader.consumeTo(" "));
        assertEquals("", reader.consumeTo(" "));
        assertEquals("me", reader.consumeTo(" "));
        assertEquals("", reader.consumeTo(" "));
    }
    
    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("test me");
        assertEquals("t", reader.consumeToAny('e','s','t'));
        assertEquals(" ", reader.consumeToAny('e','s','t'));
        assertEquals("m", reader.consumeToAny('e','s','t'));
        assertEquals("e", reader.consumeToAny('e','s','t'));
        assertEquals("", reader.consumeToAny('e','s','t'));
    }
    
    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("test", reader.consumeToEnd());
        assertEquals("", reader.consumeToEnd());
    }
    
    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("test1");
        assertEquals("test", reader.consumeLetterSequence());
        assertEquals("1", reader.consumeLetterSequence());
        assertEquals("", reader.consumeLetterSequence());
    }
    
    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("test1");
        assertEquals("test1", reader.consumeLetterThenDigitSequence());
        assertEquals("", reader.consumeLetterThenDigitSequence());
    }
    
    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("test1");
        assertEquals("", reader.consumeHexSequence());
        reader = new CharacterReader("0123456789ABCDEFabcdef");
        assertEquals("0123456789ABCDEFabcdef", reader.consumeHexSequence());
    }
    
    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("test1");
        assertEquals("", reader.consumeDigitSequence());
        reader = new CharacterReader("0123456789");
        assertEquals("0123456789", reader.consumeDigitSequence());
    }
    
    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matches('t'));
        assertFalse(reader.matches('e'));
        assertTrue(reader.matches('e'));
        assertFalse(reader.matches('s'));
        assertFalse(reader.matches('x'));
        assertTrue(reader.matches('t'));
        assertFalse(reader.matches('x'));
        assertFalse(reader.matches('s'));
    }
    
    @Test
    public void testMatchesWithSeq() {
        CharacterReader reader = new CharacterReader("test");
        assertFalse(reader.matches("tt"));
        assertTrue(reader.matches("te"));
        assertFalse(reader.matches("te"));
        assertTrue(reader.matches("es"));
        assertFalse(reader.matches("ex"));
        assertFalse(reader.matches("ss"));
        assertTrue(reader.matches("t"));
        assertFalse(reader.matches("x"));
    }
    
    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchesIgnoreCase("TT"));
        assertTrue(reader.matchesIgnoreCase("Te"));
        assertFalse(reader.matchesIgnoreCase("ts"));
        assertFalse(reader.matchesIgnoreCase("Tx"));
    }
    
    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchesAny('t','e','s'));
        assertFalse(reader.matchesAny('t','e','s'));
        assertTrue(reader.matchesAny('t','e','s'));
        assertFalse(reader.matchesAny('t','e','s'));
        assertFalse(reader.matchesAny('t','e','s'));
        assertTrue(reader.matchesAny('t','e','s'));
        assertFalse(reader.matchesAny('t','e','s'));
    }
    
    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchesLetter());
        reader.consume();
        assertTrue(reader.matchesLetter());
        reader.consume();
        assertFalse(reader.matchesLetter());
        reader.consume();
        assertFalse(reader.matchesLetter());
    }
    
    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("test12");
        assertFalse(reader.matchesDigit());
        reader.consume();
        assertFalse(reader.matchesDigit());
        reader.consume();
        assertFalse(reader.matchesDigit());
        reader.consume();
        assertTrue(reader.matchesDigit());
        reader.consume();
        assertTrue(reader.matchesDigit());
        reader.consume();
        assertFalse(reader.matchesDigit());
    }
    
    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("test");
        assertFalse(reader.matchConsume("tt"));
        assertTrue(reader.matchConsume("te"));
        assertFalse(reader.matchConsume("te"));
        assertTrue(reader.matchConsume("st"));
        assertFalse(reader.matchConsume("ex"));
    }
    
    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");
        assertFalse(reader.matchConsumeIgnoreCase("TT"));
        assertTrue(reader.matchConsumeIgnoreCase("Te"));
        assertFalse(reader.matchConsumeIgnoreCase("te"));
        assertTrue(reader.matchConsumeIgnoreCase("St"));
        assertFalse(reader.matchConsumeIgnoreCase("ex"));
    }
    
    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.containsIgnoreCase("TE"));
        assertTrue(reader.containsIgnoreCase("te"));
        assertFalse(reader.containsIgnoreCase("es"));
        assertFalse(reader.containsIgnoreCase("ex"));
    }
    
    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("test", reader.toString());
        reader.consume();
        assertEquals("est", reader.toString());
        reader.consume();
        assertEquals("st", reader.toString());
        while (!reader.isEmpty()) {
            reader.consume();
        }
        assertEquals("", reader.toString());
    }
}