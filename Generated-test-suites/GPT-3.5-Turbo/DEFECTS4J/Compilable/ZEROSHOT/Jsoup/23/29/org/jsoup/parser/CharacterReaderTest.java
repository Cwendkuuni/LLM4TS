package org.jsoup.parser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {
    private CharacterReader reader;

    @Before
    public void setUp() {
        reader = new CharacterReader("This is a test");
    }

    @Test
    public void testPos() {
        int pos = reader.pos();
        assertEquals(0, pos);
    }

    @Test
    public void testIsEmpty() {
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        char current = reader.current();
        assertEquals('T', current);
    }

    @Test
    public void testConsume() {
        char val = reader.consume();
        assertEquals('T', val);
    }

    @Test
    public void testUnconsume() {
        reader.consume();
        reader.unconsume();
        char current = reader.current();
        assertEquals('T', current);
    }

    @Test
    public void testAdvance() {
        reader.advance();
        char current = reader.current();
        assertEquals('h', current);
    }

    @Test
    public void testMark() {
        reader.advance();
        reader.mark();
        int pos = reader.pos();
        assertEquals(1, pos);
    }

    @Test
    public void testRewindToMark() {
        reader.advance();
        reader.mark();
        reader.rewindToMark();
        int pos = reader.pos();
        assertEquals(0, pos);
    }

    @Test
    public void testConsumeAsString() {
        String consumed = reader.consumeAsString();
        assertEquals("T", consumed);
    }

    @Test
    public void testConsumeTo() {
        String consumed = reader.consumeTo(" ");
        assertEquals("This", consumed);
    }

    @Test
    public void testConsumeToAny() {
        String consumed = reader.consumeToAny(' ', 'a');
        assertEquals("This", consumed);
    }

    @Test
    public void testConsumeToEnd() {
        String consumed = reader.consumeToEnd();
        assertEquals("This is a test", consumed);
    }

    @Test
    public void testConsumeDigitSequence() {
        String consumed = reader.consumeDigitSequence();
        assertEquals("", consumed);
    }

    @Test
    public void testMatches() {
        boolean matches = reader.matches('T');
        assertTrue(matches);
    }

    @Test
    public void testMatchesIgnoreCase() {
        boolean matches = reader.matchesIgnoreCase("t");
        assertTrue(matches);
    }

    @Test
    public void testMatchesAny() {
        boolean matches = reader.matchesAny('T', 't');
        assertTrue(matches);
    }

    @Test
    public void testMatchesLetter() {
        boolean matches = reader.matchesLetter();
        assertTrue(matches);
    }

    @Test
    public void testMatchesDigit() {
        boolean matches = reader.matchesDigit();
        assertFalse(matches);
    }

    @Test
    public void testMatchConsume() {
        boolean matchConsume = reader.matchConsume("T");
        assertTrue(matchConsume);
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        boolean matchConsumeIgnoreCase = reader.matchConsumeIgnoreCase("t");
        assertTrue(matchConsumeIgnoreCase);
    }

    @Test
    public void testContainsIgnoreCase() {
        boolean containsIgnoreCase = reader.containsIgnoreCase("is");
        assertTrue(containsIgnoreCase);
    }
}