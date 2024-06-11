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
    public void testIsEmpty_emptyString() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testIsEmpty_nonEmptyString() {
        CharacterReader reader = new CharacterReader("test");
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent_emptyString() {
        CharacterReader reader = new CharacterReader("");
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testCurrent_nonEmptyString() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals('t', reader.current());
    }

    @Test
    public void testConsume_emptyString() {
        CharacterReader reader = new CharacterReader("");
        assertEquals(CharacterReader.EOF, reader.consume());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsume_nonEmptyString() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals('t', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals('s', reader.consume());
        assertEquals('t', reader.consume());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals('t', reader.consume());
        reader.unconsume();
        assertEquals('t', reader.consume());
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
        assertEquals(0, reader.pos());
        reader.mark();
        assertEquals(0, reader.pos());
    }

    @Test
    public void testRewindToMark() {
        CharacterReader reader = new CharacterReader("test");
        reader.advance();
        reader.mark();
        reader.advance();
        assertEquals(2, reader.pos());
        reader.rewindToMark();
        assertEquals(1, reader.pos());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("t", reader.consumeAsString());
        assertEquals("e", reader.consumeAsString());
        assertEquals("s", reader.consumeAsString());
        assertEquals("t", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("test string");
        assertEquals("test", reader.consumeTo(' '));
        reader.consume(); // consume whitespace
        assertEquals("string", reader.consumeTo(' '));
    }

    @Test
    public void testConsumeTo_withInvalidDelimiter() {
        CharacterReader reader = new CharacterReader("test string");
        assertEquals("test string", reader.consumeTo('.'));
    }

    @Test
    public void testConsumeTo_withSequence() {
        CharacterReader reader = new CharacterReader("test string");
        assertEquals("test", reader.consumeTo(" "));
        reader.consume(); // consume whitespace
        assertEquals("string", reader.consumeTo(" "));
    }

    @Test
    public void testConsumeTo_withInvalidSequence() {
        CharacterReader reader = new CharacterReader("test string");
        assertEquals("test string", reader.consumeTo("."));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("test string");
        assertEquals("test", reader.consumeToAny(' ', '.'));
        reader.consume(); // consume whitespace
        assertEquals("string", reader.consumeToAny(' ', '.'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("test string");
        assertEquals("test string", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("test123");
        assertEquals("test", reader.consumeLetterSequence());
        assertEquals("123", reader.consumeToEnd());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("abcdef123");
        assertEquals("abcdef", reader.consumeHexSequence());
        assertEquals("123", reader.consumeToEnd());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("123abcdef");
        assertEquals("123", reader.consumeDigitSequence());
        assertEquals("abcdef", reader.consumeToEnd());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matches('t'));
        assertFalse(reader.matches('e'));
    }

    @Test
    public void testMatches_withEmptyString() {
        CharacterReader reader = new CharacterReader("");
        assertFalse(reader.matches('t'));
    }

    @Test
    public void testMatches_withString() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matches("tes"));
        assertFalse(reader.matches("est"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchesIgnoreCase("TeS"));
        assertFalse(reader.matchesIgnoreCase("EST"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchesAny('t', 'e'));
        assertFalse(reader.matchesAny('e', 's'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.matchesLetter());
        reader.consume(); // consume 't'
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("123");
        assertTrue(reader.matchesDigit());
        reader.consume(); // consume '1'
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("test");
        assertFalse(reader.matchConsume("tes"));
        assertTrue(reader.matchConsume("tes"));
        assertTrue(reader.matchConsume("t"));
        assertFalse(reader.matchConsume("t"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");
        assertFalse(reader.matchConsumeIgnoreCase("TeS"));
        assertTrue(reader.matchConsumeIgnoreCase("TeS"));
        assertTrue(reader.matchConsumeIgnoreCase("T"));
        assertFalse(reader.matchConsumeIgnoreCase("T"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("test");
        assertTrue(reader.containsIgnoreCase("te"));
        assertFalse(reader.containsIgnoreCase("es"));
    }
}

