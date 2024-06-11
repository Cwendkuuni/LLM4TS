package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

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
        reader.consume();
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
        assertEquals('e', reader.current());
        reader.unconsume();
        assertEquals('t', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals('t', reader.current());
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
        reader.advance();
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
    public void testNextIndexOfChar() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals(1, reader.nextIndexOf('e'));
        assertEquals(-1, reader.nextIndexOf('x'));
    }

    @Test
    public void testNextIndexOfString() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals(1, reader.nextIndexOf("es"));
        assertEquals(-1, reader.nextIndexOf("ex"));
    }

    @Test
    public void testConsumeToChar() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("t", reader.consumeTo('e'));
        assertEquals("e", reader.consumeTo('s'));
    }

    @Test
    public void testConsumeToString() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("t", reader.consumeTo("es"));
        assertEquals("e", reader.consumeTo("st"));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("t", reader.consumeToAny('e', 's'));
        assertEquals("e", reader.consumeToAny('s', 't'));
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
        CharacterReader reader = new CharacterReader("t3st");
        assertEquals("t", reader.consumeLetterThenDigitSequence());
        assertEquals("3", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("t3st");
        assertEquals("3", reader.consumeHexSequence());
        assertEquals("st", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("t3st");
        assertEquals("3", reader.consumeDigitSequence());
        assertEquals("", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matches('t'));
        reader.consume();
        assertFalse(reader.matches('t'));
    }

    @Test
    public void testMatchesString() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matches("test"));
        reader.consume();
        assertFalse(reader.matches("test"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchesIgnoreCase("TeSt"));
        reader.consume();
        assertFalse(reader.matchesIgnoreCase("TeSt"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchesAny('t', 'e'));
        reader.consume();
        assertFalse(reader.matchesAny('t', 'e'));
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
        CharacterReader reader = new CharacterReader("t3st");
        assertFalse(reader.matchesDigit());
        reader.consume();
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchConsume("t"));
        assertFalse(reader.matchConsume("t"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchConsumeIgnoreCase("TeSt"));
        assertFalse(reader.matchConsumeIgnoreCase("TeSt"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.containsIgnoreCase("Tes"));
        assertFalse(reader.containsIgnoreCase("Tex"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("test", reader.toString());
    }
}