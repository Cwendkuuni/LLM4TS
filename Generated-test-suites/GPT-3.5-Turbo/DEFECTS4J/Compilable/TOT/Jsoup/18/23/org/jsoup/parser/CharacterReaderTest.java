package org.jsoup.parser;
import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals(0, reader.pos());
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

        reader.rewindToMark();
        assertEquals('t', reader.current());
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
        assertEquals('e', reader.current());

        reader.consume();
        reader.unconsume();
        assertEquals('e', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("test");

        assertEquals('t', reader.current());
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
    public void testMarkAndRewindToMark() {
        CharacterReader reader = new CharacterReader("test");

        reader.mark();
        assertEquals('t', reader.current());

        reader.consume();
        assertEquals('e', reader.current());

        reader.rewindToMark();
        assertEquals('t', reader.current());
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
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("test");

        assertEquals("te", reader.consumeTo('s'));
        assertEquals("t", reader.consumeTo('t'));
        assertEquals("", reader.consumeTo('t'));
        assertEquals("test", reader.consumeTo('z'));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("test");

        assertEquals("", reader.consumeToAny('t', 'e', 's'));
        assertEquals("", reader.consumeToAny('t', 'e', 's'));
        assertEquals("test", reader.consumeToAny('z'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("test");

        assertEquals("test", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("test");

        assertEquals("t", reader.consumeLetterSequence());
        assertEquals("es", reader.consumeLetterSequence());
        assertEquals("t", reader.consumeLetterSequence());
        assertEquals("", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("test123");

        assertEquals("", reader.consumeHexSequence());
        assertEquals("", reader.consumeHexSequence());
        assertEquals("", reader.consumeHexSequence());
        assertEquals("", reader.consumeHexSequence());
        assertEquals("", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("test123");

        assertEquals("", reader.consumeDigitSequence());
        assertEquals("123", reader.consumeDigitSequence());
        assertEquals("", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("test");

        assertTrue(reader.matches('t'));
        assertFalse(reader.matches('e'));

        reader.consume();
        assertTrue(reader.matches('e'));

        assertFalse(reader.matches('t'));
        assertFalse(reader.matches('z'));
    }

    @Test
    public void testMatchesSequence() {
        CharacterReader reader = new CharacterReader("test");

        assertTrue(reader.matches("test"));
        assertFalse(reader.matches("tes"));
        assertFalse(reader.matches("z"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("Test");

        assertTrue(reader.matchesIgnoreCase("test"));
        assertFalse(reader.matchesIgnoreCase("tes"));
        assertFalse(reader.matchesIgnoreCase("z"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("test");

        assertFalse(reader.matchesAny('e', 's'));
        assertTrue(reader.matchesAny('t', 's'));
        assertFalse(reader.matchesAny('z'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("Hello");

        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());

        reader.consume();
        assertFalse(reader.matchesLetter());

        reader.consume();
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("123");

        assertFalse(reader.matchesDigit());
        assertTrue(reader.matchesDigit());

        reader.consume();
        assertFalse(reader.matchesDigit());

        reader.consume();
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("test");

        assertFalse(reader.matchConsume("tes"));
        assertTrue(reader.matchConsume("test"));

        assertFalse(reader.matchConsume("test"));
        assertFalse(reader.matchConsume("z"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("Test");

        assertFalse(reader.matchConsumeIgnoreCase("tes"));
        assertTrue(reader.matchConsumeIgnoreCase("test"));

        assertFalse(reader.matchConsumeIgnoreCase("test"));
        assertFalse(reader.matchConsumeIgnoreCase("z"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");

        assertFalse(reader.containsIgnoreCase("tes"));
        assertTrue(reader.containsIgnoreCase("tesT"));
        assertFalse(reader.containsIgnoreCase("z"));
    }
}