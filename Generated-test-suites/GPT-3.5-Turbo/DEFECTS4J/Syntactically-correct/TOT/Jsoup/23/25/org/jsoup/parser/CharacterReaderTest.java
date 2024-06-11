
package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("Test String");
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
        reader = new CharacterReader("Test String");
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("");
        assertEquals(CharacterReader.EOF, reader.current());
        reader = new CharacterReader("Test String");
        assertEquals('T', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("Test String");
        assertEquals('T', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals('s', reader.consume());
        assertEquals('t', reader.consume());
        assertEquals(' ', reader.consume());
        assertEquals('S', reader.consume());
        assertEquals('t', reader.consume());
        assertEquals('r', reader.consume());
        assertEquals('i', reader.consume());
        assertEquals('n', reader.consume());
        assertEquals('g', reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("Test String");
        assertEquals('T', reader.consume());
        reader.unconsume();
        assertEquals('T', reader.current());
        assertEquals('T', reader.consume());
        assertEquals('e', reader.consume());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("Test String");
        assertEquals('T', reader.consume());
        reader.advance();
        assertEquals('s', reader.current());
        assertEquals('s', reader.consume());
        assertEquals(' ', reader.advance());
    }

    @Test
    public void testMarkAndRewindToMark() {
        CharacterReader reader = new CharacterReader("Test String");
        reader.mark();
        assertEquals('T', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals('s', reader.consume());
        reader.rewindToMark();
        assertEquals('e', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("Test String");
        assertEquals("T", reader.consumeAsString());
        assertEquals("e", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("Test String");
        assertEquals("Test ", reader.consumeTo(' '));
        assertEquals("S", reader.consumeTo('S'));
        assertEquals("tring", reader.consumeTo('z'));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("Test5String");
        assertEquals("Test", reader.consumeToAny('5'));
        assertEquals("5", reader.consumeToAny('5'));
        assertEquals("String", reader.consumeToAny('a', 'b', 'c'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("Test String");
        assertEquals("Test String", reader.consumeToEnd());
        assertEquals("", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("Test String123");
        assertEquals("Test", reader.consumeLetterSequence());
        assertEquals(" ", reader.consumeLetterSequence());
        assertEquals("String", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("Test String1AF");
        assertEquals("1A", reader.consumeHexSequence());
        assertEquals("F", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("Test String123");
        assertEquals("123", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("Test String");
        assertFalse(reader.matches('S'));
        assertTrue(reader.matches('T'));
        reader.consume();
        assertTrue(reader.matches("String"));
        assertFalse(reader.matches("Test"));
    }

    @Test
    public void testMatchesIgnoringCase() {
        CharacterReader reader = new CharacterReader("Test String");
        assertFalse(reader.matchesIgnoreCase("string"));
        assertTrue(reader.matchesIgnoreCase("strinG"));
        reader.consume();
        assertTrue(reader.matchesIgnoreCase("String"));
        assertFalse(reader.matchesIgnoreCase("T"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("TestString");
        assertFalse(reader.matchesAny('1', '2', '3'));
        assertTrue(reader.matchesAny('T', 'S', 't'));
        reader.consume();
        assertFalse(reader.matchesAny('T', 'S', 't'));
        assertTrue(reader.matchesAny('S', 's'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("Test String123");
        assertFalse(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        reader.consume();
        assertFalse(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("Test String123");
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
        reader.consume();
        assertFalse(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("Test String");
        assertFalse(reader.matchConsume("ing"));
        assertTrue(reader.matchConsume("Tes"));
        assertFalse(reader.matchesIgnoreCase("Test"));
        assertTrue(reader.matchConsumeIgnoreCase("Test"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("Test String");
        assertFalse(reader.containsIgnoreCase("input"));
        assertTrue(reader.containsIgnoreCase("test"));
        assertFalse(reader.containsIgnoreCase("Tes"));
        assertTrue(reader.containsIgnoreCase("StrinG"));
    }
}

