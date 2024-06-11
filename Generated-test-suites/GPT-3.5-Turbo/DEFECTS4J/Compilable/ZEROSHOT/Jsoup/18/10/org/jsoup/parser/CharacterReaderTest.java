package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testIsEmpty_notEmpty() {
        CharacterReader reader = new CharacterReader("input");
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent_empty() {
        CharacterReader reader = new CharacterReader("");
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testCurrent_notEmpty() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals('i', reader.current());
    }

    @Test
    public void testConsume_empty() {
        CharacterReader reader = new CharacterReader("");
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testConsume_notEmpty() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals('i', reader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("input");
        reader.consume();
        reader.unconsume();
        assertEquals('i', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("input");
        reader.advance();
        assertEquals('n', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader("input");
        reader.mark();
        assertEquals(0, reader.pos());
    }

    @Test
    public void testRewindToMark() {
        CharacterReader reader = new CharacterReader("input");
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('i', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals("i", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo_found() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals("in", reader.consumeTo('p'));
    }

    @Test
    public void testConsumeTo_notFound() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals("input", reader.consumeTo('z'));
    }

    @Test
    public void testConsumeToAny_noneFound() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals("", reader.consumeToAny('z', 'x', 'y'));
    }

    @Test
    public void testConsumeToAny_found() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals("in", reader.consumeToAny('p', 'u', 't'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals("input", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals("input", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals("", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals("", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches_true() {
        CharacterReader reader = new CharacterReader("input");
        assertTrue(reader.matches('i'));
    }

    @Test
    public void testMatches_false() {
        CharacterReader reader = new CharacterReader("input");
        assertFalse(reader.matches('z'));
    }

    @Test
    public void testMatches_trueString() {
        CharacterReader reader = new CharacterReader("input");
        assertTrue(reader.matches("input"));
    }

    @Test
    public void testMatches_falseString() {
        CharacterReader reader = new CharacterReader("input");
        assertFalse(reader.matches("notinput"));
    }

    @Test
    public void testMatchesIgnoreCase_true() {
        CharacterReader reader = new CharacterReader("input");
        assertTrue(reader.matchesIgnoreCase("INPUT"));
    }

    @Test
    public void testMatchesIgnoreCase_false() {
        CharacterReader reader = new CharacterReader("input");
        assertFalse(reader.matchesIgnoreCase("notinput"));
    }

    @Test
    public void testMatchesAny_true() {
        CharacterReader reader = new CharacterReader("input");
        assertTrue(reader.matchesAny('z', 'x', 'y', 'i'));
    }

    @Test
    public void testMatchesAny_false() {
        CharacterReader reader = new CharacterReader("input");
        assertFalse(reader.matchesAny('z', 'x', 'y'));
    }

    @Test
    public void testMatchesLetter_true() {
        CharacterReader reader = new CharacterReader("input");
        assertTrue(reader.matchesLetter());
    }

    @Test
    public void testMatchesLetter_false() {
        CharacterReader reader = new CharacterReader("123");
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit_true() {
        CharacterReader reader = new CharacterReader("123");
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchesDigit_false() {
        CharacterReader reader = new CharacterReader("input");
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume_true() {
        CharacterReader reader = new CharacterReader("input");
        assertTrue(reader.matchConsume("input"));
    }

    @Test
    public void testMatchConsume_false() {
        CharacterReader reader = new CharacterReader("input");
        assertFalse(reader.matchConsume("notinput"));
    }

    @Test
    public void testMatchConsumeIgnoreCase_true() {
        CharacterReader reader = new CharacterReader("input");
        assertTrue(reader.matchConsumeIgnoreCase("INPUT"));
    }

    @Test
    public void testMatchConsumeIgnoreCase_false() {
        CharacterReader reader = new CharacterReader("input");
        assertFalse(reader.matchConsumeIgnoreCase("notinput"));
    }

    @Test
    public void testContainsIgnoreCase_true() {
        CharacterReader reader = new CharacterReader("Input");
        assertTrue(reader.containsIgnoreCase("input"));
    }

    @Test
    public void testContainsIgnoreCase_false() {
        CharacterReader reader = new CharacterReader("Input");
        assertFalse(reader.containsIgnoreCase("notinput"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals("input", reader.toString());
    }
}