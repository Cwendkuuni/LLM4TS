package org.jsoup.parser;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CharacterReaderTest {
    private CharacterReader reader;

    @Before
    public void setUp() {
        reader = new CharacterReader("test");
    }

    @Test
    public void testPos() {
        assertEquals(0, reader.pos());
        reader.advance();
        assertEquals(1, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(reader.isEmpty());
        reader.consume();
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        assertEquals('t', reader.current());
        reader.consume();
        assertEquals('e', reader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('t', reader.consume());
        assertEquals('e', reader.consume());
    }

    @Test
    public void testUnconsume() {
        reader.consume();
        reader.unconsume();
        assertEquals('t', reader.current());
    }

    @Test
    public void testAdvance() {
        reader.advance();
        assertEquals('e', reader.current());
    }

    @Test
    public void testMark() {
        reader.mark();
        reader.advance();
        assertEquals('e', reader.current());
        reader.rewindToMark();
        assertEquals('t', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("t", reader.consumeAsString());
        assertEquals("e", reader.consumeAsString());
    }

    @Test
    public void testConsumeToChar() {
        assertEquals("t", reader.consumeTo('e'));
        assertEquals("st", reader.consumeTo('t'));
    }

    @Test
    public void testConsumeToString() {
        assertEquals("t", reader.consumeTo("es"));
        assertEquals("te", reader.consumeTo("st"));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("t", reader.consumeToAny('e', 's'));
        assertEquals("st", reader.consumeToAny('t', 'e'));
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("test", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("", reader.consumeLetterSequence());
        reader.advance();
        assertEquals("e", reader.consumeLetterSequence());
        reader.advance();
        assertEquals("e", reader.consumeLetterSequence());
        reader.advance();
        assertEquals("t", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        assertEquals("", reader.consumeHexSequence());
        reader.advance();
        assertEquals("", reader.consumeHexSequence());
        reader.advance();
        assertEquals("", reader.consumeHexSequence());
        reader.advance();
        assertEquals("", reader.consumeHexSequence());
        reader.advance();
        assertEquals("t", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("", reader.consumeDigitSequence());
        reader.advance();
        assertEquals("", reader.consumeDigitSequence());
        reader.advance();
        assertEquals("", reader.consumeDigitSequence());
        reader.advance();
        assertEquals("", reader.consumeDigitSequence());
        reader.advance();
        assertEquals("", reader.consumeDigitSequence());
        reader.advance();
        assertEquals("t", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(reader.matches('t'));
        assertFalse(reader.matches('e'));
    }

    @Test
    public void testMatchesString() {
        assertTrue(reader.matches("tes"));
        assertFalse(reader.matches("est"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(reader.matchesIgnoreCase("Tes"));
        assertFalse(reader.matchesIgnoreCase("Set"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(reader.matchesAny('t', 'e'));
        assertFalse(reader.matchesAny('e', 's'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(reader.matchesLetter());
        reader.advance();
        assertTrue(reader.matchesLetter());
        reader.advance();
        assertTrue(reader.matchesLetter());
        reader.advance();
        assertTrue(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(reader.matchesDigit());
        reader.advance();
        assertFalse(reader.matchesDigit());
        reader.advance();
        assertFalse(reader.matchesDigit());
        reader.advance();
        assertFalse(reader.matchesDigit());
        reader.advance();
        assertFalse(reader.matchesDigit());
        reader.advance();
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(reader.matchConsume("tes"));
        assertFalse(reader.matchConsume("te"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(reader.matchConsumeIgnoreCase("TES"));
        assertFalse(reader.matchConsumeIgnoreCase("TE"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(reader.containsIgnoreCase("tes"));
        assertFalse(reader.containsIgnoreCase("set"));
    }

    @Test
    public void testToString() {
        reader.advance();
        assertEquals("est", reader.toString());
    }
}