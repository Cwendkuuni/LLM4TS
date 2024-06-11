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
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals('i', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals('i', reader.consume());
        assertEquals('n', reader.current());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("input");
        reader.consume();
        reader.consume();
        reader.unconsume();
        assertEquals('n', reader.current());
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
        reader.advance();
        assertEquals('n', reader.current());
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
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals("in", reader.consumeTo('p'));
        assertEquals("put", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals("in", reader.consumeToAny('p', 'u'));
        assertEquals("t", reader.consumeToEnd());
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
        assertEquals("inp", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals("", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("input");
        assertTrue(reader.matches('i'));
        assertFalse(reader.matches('n'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("input");
        assertTrue(reader.matchesIgnoreCase("In"));
        assertFalse(reader.matchesIgnoreCase("ip"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("input");
        assertTrue(reader.matchesAny('i', 'n'));
        assertFalse(reader.matchesAny('p', 'u'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("input");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("input");
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("input");
        assertTrue(reader.matchConsume("in"));
        assertFalse(reader.matchConsume("pu"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("input");
        assertTrue(reader.matchConsumeIgnoreCase("In"));
        assertFalse(reader.matchConsumeIgnoreCase("ip"));
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("input");
        assertTrue(reader.containsIgnoreCase("In"));
        assertFalse(reader.containsIgnoreCase("pu"));
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("input");
        assertEquals("input", reader.toString());
    }
}