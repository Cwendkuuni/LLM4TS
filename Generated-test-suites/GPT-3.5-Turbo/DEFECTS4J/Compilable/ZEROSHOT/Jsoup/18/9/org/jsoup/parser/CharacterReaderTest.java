package org.jsoup.parser;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    private CharacterReader reader;

    @Before
    public void setUp() {
        reader = new CharacterReader("Lorem ipsum dolor sit amet, consectetur adipiscing elit.");
    }

    @Test
    public void testPos() {
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        assertEquals('L', reader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('L', reader.consume());
        assertEquals('o', reader.consume());
    }

    @Test
    public void testUnconsume() {
        reader.consume();
        reader.consume();
        reader.unconsume();
        assertEquals('o', reader.consume());
    }

    @Test
    public void testAdvance() {
        reader.advance();
        assertEquals('o', reader.current());
    }

    @Test
    public void testMark() {
        reader.advance();
        reader.advance();
        reader.mark();
        assertEquals(2, reader.pos());
    }

    @Test
    public void testRewindToMark() {
        reader.advance();
        reader.advance();
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals(2, reader.pos());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("L", reader.consumeAsString());
        assertEquals("o", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        assertEquals("Lorem ipsum ", reader.consumeTo('d'));
        assertEquals("dolor si", reader.consumeTo('t'));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("Lo", reader.consumeToAny('r', 'r'));
        assertEquals("em ipsum dol", reader.consumeToAny('o', 'm'));
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("Lorem ipsum dolor sit amet, consectetur adipiscing elit.", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("Lorem", reader.consumeLetterSequence());
        assertEquals("ipsum", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        assertEquals("", reader.consumeHexSequence());
        assertEquals("", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("", reader.consumeDigitSequence());
        assertEquals("", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(reader.matches('L'));
        assertFalse(reader.matches('A'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(reader.matchesIgnoreCase("LO"));
        assertFalse(reader.matchesIgnoreCase("EL"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(reader.matchesAny('L', 'i'));
        assertFalse(reader.matchesAny('a', 'b'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(reader.matchesLetter());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(reader.matchConsume("Lo"));
        assertFalse(reader.matchConsume("em"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(reader.matchConsumeIgnoreCase("LO"));
        assertFalse(reader.matchConsumeIgnoreCase("EL"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(reader.containsIgnoreCase("lorem"));
        assertFalse(reader.containsIgnoreCase("dolor"));
    }

    @Test
    public void testToString() {
        assertEquals("Lorem ipsum dolor sit amet, consectetur adipiscing elit.", reader.toString());
    }
}