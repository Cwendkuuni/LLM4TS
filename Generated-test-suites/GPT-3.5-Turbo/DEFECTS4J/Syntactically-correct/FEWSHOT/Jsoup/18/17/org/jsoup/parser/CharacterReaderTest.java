package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {
    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("testing");
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
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("testing");
        assertEquals('t', reader.current());
        reader.consume();
        assertEquals('e', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("testing");
        assertEquals('t', reader.consume());
        assertEquals('e', reader.consume());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("testing");
        reader.consume();
        reader.unconsume();
        assertEquals('t', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("testing");
        reader.advance();
        assertEquals('e', reader.current());
    }

    @Test
    public void testMarkAndRewindToMark() {
        CharacterReader reader = new CharacterReader("testing");
        reader.mark();
        assertEquals('t', reader.consume());
        reader.rewindToMark();
        assertEquals('t', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("test");
        assertEquals("t", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("testing");
        assertEquals("test", reader.consumeTo('i'));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("testing");
        assertEquals("tes", reader.consumeToAny('i', 'n'));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("testing");
        assertEquals("testing", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abc123");
        assertEquals("abc", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("0x123");
        assertEquals("0x123", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("123abc");
        assertEquals("123", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("testing");
        assertTrue(reader.matches('t'));
        reader.consume();
        assertTrue(reader.matches('e'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("testing");
        assertTrue(reader.matchesIgnoreCase("TE"));
        reader.consume();
        assertTrue(reader.matchesIgnoreCase("E"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("testing");
        assertTrue(reader.matchesAny('t', 'e'));
        reader.consume();
        assertTrue(reader.matchesAny('e', 's'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("testing");
        assertTrue(reader.matchesLetter());
        reader.consume();
        assertTrue(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("123abc");
        assertTrue(reader.matchesDigit());
        reader.consume();
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("testing");
        assertTrue(reader.matchConsume("test"));
        assertEquals('i', reader.current());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("TestinG");
        assertTrue(reader.matchConsumeIgnoreCase("teST"));
        assertEquals('i', reader.current());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("</style>");
        assertTrue(reader.containsIgnoreCase("</STYLE>"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("testing");
        assertEquals("testing", reader.toString());
    }
}