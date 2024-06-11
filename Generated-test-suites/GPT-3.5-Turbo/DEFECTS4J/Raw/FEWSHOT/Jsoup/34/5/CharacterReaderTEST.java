package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Test;

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
        reader.mark();
        reader.advance();
        assertEquals('e', reader.current());
        reader.rewindToMark();
        assertEquals('t', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("t", reader.consumeAsString());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals(1, reader.nextIndexOf('e'));
        assertEquals(-1, reader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOfSequence() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals(1, reader.nextIndexOf("es"));
        assertEquals(-1, reader.nextIndexOf("zt"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("t", reader.consumeTo('e'));
        assertEquals("e", reader.consumeTo('s'));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("t", reader.consumeToAny('e'));
        assertEquals("e", reader.consumeToAny('s'));
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
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("t1e2s3t");
        assertEquals("t", reader.consumeLetterThenDigitSequence());
        assertEquals("1", reader.consumeLetterThenDigitSequence());
        assertEquals("e", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("t1A2f3");
        assertEquals("t", reader.consumeHexSequence());
        assertEquals("1A2", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("t1A2f3");
        assertEquals("1", reader.consumeDigitSequence());
        assertEquals("2", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matches('t'));
        assertFalse(reader.matches('e'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchesIgnoreCase("Te"));
        assertFalse(reader.matchesIgnoreCase("es"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchesAny('t', 'e'));
        assertFalse(reader.matchesAny('a', 'b'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("123");
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchConsume("te"));
        assertFalse(reader.matchConsume("st"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchConsumeIgnoreCase("tE"));
        assertFalse(reader.matchConsumeIgnoreCase("Es"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.containsIgnoreCase("TE"));
        assertFalse(reader.containsIgnoreCase("ES"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("test", reader.toString());
    }
}