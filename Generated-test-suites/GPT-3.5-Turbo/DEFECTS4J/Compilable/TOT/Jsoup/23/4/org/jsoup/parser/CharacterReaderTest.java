package org.jsoup.parser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {
    private CharacterReader reader;

    @Before
    public void setUp() {
        reader = new CharacterReader("Example string");
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
        assertEquals('E', reader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('E', reader.consume());
        assertEquals('x', reader.consume());
    }

    @Test
    public void testUnconsume() {
        char c = reader.consume();
        reader.unconsume();
        assertEquals(c, reader.current());
    }

    @Test
    public void testAdvance() {
        int currentPosition = reader.pos();
        reader.advance();
        assertEquals(currentPosition + 1, reader.pos());
    }

    @Test
    public void testMark() {
        reader.mark();
        assertEquals(0, reader.pos());
    }

    @Test
    public void testRewindToMark() {
        reader.advance();
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals(1, reader.pos());
    }

    @Test
    public void testConsumeAsString() {
        String expected = "Example";
        String actual = reader.consumeAsString();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeTo() {
        String expected = "Example";
        String actual = reader.consumeTo(' ');
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToNotFound() {
        String expected = "Example string";
        String actual = reader.consumeTo('z');
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToAny() {
        String expected = "Example";
        String actual = reader.consumeToAny(' ', 's');
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeToEnd() {
        String expected = "Example string";
        String actual = reader.consumeToEnd();
        assertEquals(expected, actual);
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeLetterSequence() {
        String expected = "Example";
        String actual = reader.consumeLetterSequence();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeHexSequence() {
        String expected = "Example";
        String actual = reader.consumeHexSequence();
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeDigitSequence() {
        String expected = "";
        String actual = reader.consumeDigitSequence();
        assertEquals(expected, actual);
    }

    @Test
    public void testMatches() {
        assertTrue(reader.matches('E'));
        assertFalse(reader.matches('e'));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(reader.matchesAny(' ', 'E'));
        assertFalse(reader.matchesAny('s', 't'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(reader.matchConsume("Exam"));
        assertEquals('p', reader.current());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(reader.matchConsumeIgnoreCase("eXAM"));
        assertEquals('p', reader.current());
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(reader.containsIgnoreCase("exAMP"));
        assertFalse(reader.containsIgnoreCase("examP"));
    }

    @Test
    public void testToString() {
        String expected = "Example string";
        String actual = reader.toString();
        assertEquals(expected, actual);
    }
}