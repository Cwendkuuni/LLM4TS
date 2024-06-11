
package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {

    private CharacterReader reader;

    @Test
    public void testPos() {
        reader = new CharacterReader("test");
        assertEquals(0, reader.pos());
        reader.consume();
        assertEquals(1, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        reader = new CharacterReader("test");
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        reader = new CharacterReader("");
        assertEquals(CharacterReader.EOF, reader.current());
        reader = new CharacterReader("test");
        assertEquals('t', reader.current());
    }

    @Test
    public void testConsume() {
        reader = new CharacterReader("test");
        assertEquals('t', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals('s', reader.consume());
        assertEquals('t', reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testUnconsume() {
        reader = new CharacterReader("test");
        reader.consume();
        reader.unconsume();
        assertEquals('t', reader.current());
    }

    @Test
    public void testAdvance() {
        reader = new CharacterReader("test");
        reader.advance();
        assertEquals('e', reader.current());
    }

    @Test
    public void testMark() {
        reader = new CharacterReader("test");
        reader.mark();
        reader.advance();
        assertEquals('e', reader.current());
    }

    @Test
    public void testRewindToMark() {
        reader = new CharacterReader("test");
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('t', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        reader = new CharacterReader("test");
        assertEquals("t", reader.consumeAsString());
        assertEquals("e", reader.consumeAsString());
    }

    @Test
    public void testNextIndexOfChar() {
        reader = new CharacterReader("test");
        assertEquals(2, reader.nextIndexOf('s'));
        assertEquals(-1, reader.nextIndexOf('x'));
    }

    @Test
    public void testNextIndexOfCharSequence() {
        reader = new CharacterReader("test");
        assertEquals(2, reader.nextIndexOf("s"));
        assertEquals(-1, reader.nextIndexOf("x"));
    }

    @Test
    public void testConsumeToChar() {
        reader = new CharacterReader("test");
        assertEquals("te", reader.consumeTo('s'));
        assertEquals("st", reader.consumeTo('x'));
    }

    @Test
    public void testConsumeToSequence() {
        reader = new CharacterReader("test");
        assertEquals("te", reader.consumeTo("st"));
        assertEquals("st", reader.consumeTo("x"));
    }

    @Test
    public void testConsumeToAny() {
        reader = new CharacterReader("test");
        assertEquals("te", reader.consumeToAny('s', 'x'));
        assertEquals("st", reader.consumeToAny());
    }

    @Test
    public void testConsumeToEnd() {
        reader = new CharacterReader("test");
        assertEquals("test", reader.consumeToEnd());
        assertEquals("", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        reader = new CharacterReader("test123");
        assertEquals("test", reader.consumeLetterSequence());
        assertEquals("123", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        reader = new CharacterReader("test123");
        assertEquals("test123", reader.consumeLetterThenDigitSequence());
        assertEquals("", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        reader = new CharacterReader("123abc");
        assertEquals("123", reader.consumeHexSequence());
        assertEquals("abc", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        reader = new CharacterReader("123abc");
        assertEquals("123", reader.consumeDigitSequence());
        assertEquals("", reader.consumeDigitSequence());
    }

    @Test
    public void testMatchesChar() {
        reader = new CharacterReader("test");
        assertTrue(reader.matches('t'));
        assertFalse(reader.matches('x'));
    }

    @Test
    public void testMatchesSequence() {
        reader = new CharacterReader("test");
        assertTrue(reader.matches("tes"));
        assertFalse(reader.matches("xxx"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        reader = new CharacterReader("test");
        assertTrue(reader.matchesIgnoreCase("TeS"));
        assertFalse(reader.matchesIgnoreCase("xxx"));
    }

    @Test
    public void testMatchesAny() {
        reader = new CharacterReader("test");
        assertTrue(reader.matchesAny('t', 's'));
        assertFalse(reader.matchesAny('x', 'y'));
    }

    @Test
    public void testMatchesLetter() {
        reader = new CharacterReader("test123");
        assertTrue(reader.matchesLetter());
        reader.consumeLetterSequence();
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        reader = new CharacterReader("test123");
        reader.consumeLetterSequence();
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        reader = new CharacterReader("test");
        assertTrue(reader.matchConsume("te"));
        assertFalse(reader.matchConsume("xxx"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        reader = new CharacterReader("test");
        assertTrue(reader.matchConsumeIgnoreCase("Te"));
        assertFalse(reader.matchConsumeIgnoreCase("xxx"));
    }

    @Test
    public void testContainsIgnoreCase() {
        reader = new CharacterReader("test123");
        assertTrue(reader.containsIgnoreCase("TeS"));
        assertFalse(reader.containsIgnoreCase("xxx"));
    }

    @Test
    public void testToString() {
        reader = new CharacterReader("test");
        assertEquals("test", reader.toString());
    }
}
