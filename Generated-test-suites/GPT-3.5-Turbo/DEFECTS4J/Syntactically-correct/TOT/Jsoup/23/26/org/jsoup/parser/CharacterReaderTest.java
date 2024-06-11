package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testPos(){
        CharacterReader reader = new CharacterReader("test");
        assertEquals(0, reader.pos());
        
        reader.consume();
        assertEquals(1, reader.pos());

        reader.unconsume();
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty(){
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
    public void testCurrent(){
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
    public void testConsume(){
        CharacterReader reader = new CharacterReader("test");
        assertEquals('t', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals('s', reader.consume());
        assertEquals('t', reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testUnconsume(){
        CharacterReader reader = new CharacterReader("test");
        reader.consume();
        reader.unconsume();
        assertEquals('t', reader.consume());
    }

    @Test
    public void testAdvance(){
        CharacterReader reader = new CharacterReader("test");
        reader.advance();
        assertEquals('e', reader.current());
        reader.advance();
        assertEquals('s', reader.current());
        reader.advance();
        assertEquals('t', reader.current());
        reader.advance();
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testMarkAndRewindToMark(){
        CharacterReader reader = new CharacterReader("test");
        reader.advance();
        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        assertEquals('e', reader.current());
    }

    @Test
    public void testConsumeAsString(){
        CharacterReader reader = new CharacterReader("test");
        assertEquals("t", reader.consumeAsString());
        assertEquals("e", reader.consumeAsString());
        assertEquals("s", reader.consumeAsString());
        assertEquals("t", reader.consumeAsString());
        assertEquals("", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo(){
        CharacterReader reader = new CharacterReader("test");
        assertEquals("", reader.consumeTo('='));
        reader.unconsume();

        assertEquals("test", reader.consumeTo(':'));
        reader.unconsume();

        reader.advance();
        assertEquals("est", reader.consumeTo('t'));
        reader.unconsume();

        reader.advance();
        assertEquals("est", reader.consumeTo('='));
    }

    @Test
    public void testConsumeToAny(){
        CharacterReader reader = new CharacterReader("test");
        assertEquals("", reader.consumeToAny('=', ':'));
        reader.unconsume();

        assertEquals("", reader.consumeToAny('='));
        reader.unconsume();

        reader.advance();
        assertEquals("est", reader.consumeToAny(':'));
        reader.unconsume();

        reader.advance();
        assertEquals("est", reader.consumeToAny('='));
    }

    @Test
    public void testConsumeToEnd(){
        CharacterReader reader = new CharacterReader("test");
        assertEquals("test", reader.consumeToEnd());
        assertEquals("", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence(){
        CharacterReader reader = new CharacterReader("test");
        assertEquals("test", reader.consumeLetterSequence());
        assertEquals("", reader.consumeLetterSequence());

        reader = new CharacterReader("12test");
        assertEquals("", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence(){
        CharacterReader reader = new CharacterReader("test");
        assertEquals("", reader.consumeHexSequence());

        reader = new CharacterReader("12ABC");
        assertEquals("12ABC", reader.consumeHexSequence());

        reader = new CharacterReader("12XYZ");
        assertEquals("12", reader.consumeHexSequence());

        reader = new CharacterReader("12");
        assertEquals("12", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence(){
        CharacterReader reader = new CharacterReader("test");
        assertEquals("", reader.consumeDigitSequence());

        reader = new CharacterReader("12ABC");
        assertEquals("12", reader.consumeDigitSequence());

        reader = new CharacterReader("ABC");
        assertEquals("", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches(){
        CharacterReader reader = new CharacterReader("test");
        assertFalse(reader.matches('A'));
        reader.consume();
        assertFalse(reader.matches('A'));
        assertTrue(reader.matches('e'));
        reader.consume();
        assertFalse(reader.matches('A'));
        assertFalse(reader.matches('e'));
        assertFalse(reader.matches('t'));
        reader.consume();
        assertFalse(reader.matches('e'));
        assertTrue(reader.matches('s'));
        assertTrue(reader.matches('t'));
    }

    @Test
    public void testMatchesIgnoreCase(){
        CharacterReader reader = new CharacterReader("test");
        assertFalse(reader.matchesIgnoreCase("a"));
        reader.consume();
        assertFalse(reader.matchesIgnoreCase("a"));
        assertTrue(reader.matchesIgnoreCase("e"));
        reader.consume();
        assertFalse(reader.matchesIgnoreCase("A"));
        assertFalse(reader.matchesIgnoreCase("e"));
        assertFalse(reader.matchesIgnoreCase("T"));
        assertFalse(reader.matchesIgnoreCase("e"));
        assertFalse(reader.matchesIgnoreCase("s"));
        assertTrue(reader.matchesIgnoreCase("t"));
        assertFalse(reader.matchesIgnoreCase("X"));
    }

    @Test
    public void testMatchesAny(){
        CharacterReader reader = new CharacterReader("test");
        assertFalse(reader.matchesAny('A', 'B', 'C'));
        reader.consume();
        assertFalse(reader.matchesAny('A', 'B', 'C'));
        assertTrue(reader.matchesAny('e'));
        reader.consume();
        assertFalse(reader.matchesAny('A', 'B', 'C'));
        assertTrue(reader.matchesAny('s', 'S'));
        assertFalse(reader.matchesAny('X', 'Y', 'Z'));
        reader.consume();
        assertFalse(reader.matchesAny('s', 'S'));
        assertFalse(reader.matchesAny('X', 'Y', 'Z'));
        assertTrue(reader.matchesAny('t', 'x', 'T'));
    }

    @Test
    public void testMatchesLetter(){
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchesLetter());
        reader.consume();
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit(){
        CharacterReader reader = new CharacterReader("test");
        assertFalse(reader.matchesDigit());
        reader.consume();
        assertFalse(reader.matchesDigit());

        reader = new CharacterReader("1234");
        assertTrue(reader.matchesDigit());
        reader.consume();
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume(){
        CharacterReader reader = new CharacterReader("test");
        assertFalse(reader.matchConsume("a"));
        assertTrue(reader.matchConsume("t"));
        assertFalse(reader.matchConsume("a"));
        assertTrue(reader.matchConsume("e"));
        assertFalse(reader.matchConsume("a"));
        assertTrue(reader.matchConsume("s"));
        assertFalse(reader.matchConsume("t"));
        assertFalse(reader.matchConsume("e"));
    }

    @Test
    public void testMatchConsumeIgnoreCase(){
        CharacterReader reader = new CharacterReader("test");
        assertFalse(reader.matchConsumeIgnoreCase("a"));
        assertTrue(reader.matchConsumeIgnoreCase("t"));
        assertFalse(reader.matchConsumeIgnoreCase("A"));
        assertTrue(reader.matchConsumeIgnoreCase("e"));
        assertFalse(reader.matchConsumeIgnoreCase("A"));
        assertTrue(reader.matchConsumeIgnoreCase("s"));
        assertFalse(reader.matchConsumeIgnoreCase("T"));
        assertFalse(reader.matchConsumeIgnoreCase("E"));
    }

    @Test
    public void testContainsIgnoreCase(){
        CharacterReader reader = new CharacterReader("Title Test");
        assertTrue(reader.containsIgnoreCase("title"));
        assertFalse(reader.containsIgnoreCase("style"));
        assertTrue(reader.containsIgnoreCase("Test"));

        reader = new CharacterReader("title");
        assertTrue(reader.containsIgnoreCase("title"));
        assertTrue(reader.containsIgnoreCase("Title"));
        assertFalse(reader.containsIgnoreCase("style"));

        reader = new CharacterReader("STYLE");
        assertTrue(reader.containsIgnoreCase("style"));
        assertTrue(reader.containsIgnoreCase("STYLE"));
        assertFalse(reader.containsIgnoreCase("title"));
    }

    @Test
    public void testToString(){
        CharacterReader reader = new CharacterReader("test");
        reader.consume();
        assertEquals("est", reader.toString());
        reader.consume();
        assertEquals("st", reader.toString());
    }

}