package org.jsoup.parser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * CharacterReaderTest class to test all methods in CharacterReader class
 */
public class CharacterReaderTest {
    private CharacterReader reader;

    @Before
    public void setUp() {
        String input = "abcABC123DEF";
        reader = new CharacterReader(input);
    }

    @After
    public void tearDown() {
        reader = null;
    }

    @Test
    public void testEmpty() {
        assertTrue(reader.isEmpty());
        assertEquals(-1, reader.pos());
    }

    @Test
    public void testPos() {
        reader.advance();
        reader.advance();
        reader.advance();
        assertEquals(3, reader.pos());
    }

    @Test
    public void testCurrent() {
        reader.advance();
        reader.advance();
        assertEquals('c', reader.current());
    }

    @Test
    public void testConsume() {
        char consumed = reader.consume();
        assertEquals('a', consumed);
        assertEquals(2, reader.pos());
    }

    @Test
    public void testUnconsume() {
        reader.advance();
        reader.consume();
        reader.unconsume();
        assertEquals('a', reader.current());
        assertEquals(2, reader.pos());
    }

    @Test
    public void testAdvance() {
        reader.advance();
        reader.advance();
        assertEquals('c', reader.current());
        assertEquals(4, reader.pos());
    }

    @Test
    public void testMark() {
        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        assertEquals('a', reader.current());
        assertEquals(3, reader.pos());
    }

    @Test
    public void testConsumeAsString() {
        String consumed = reader.consumeAsString();
        assertEquals("abc", consumed);
        assertEquals(7, reader.pos());
    }

    @Test
    public void testConsumeTo() {
        String consumed = reader.consumeTo('B');
        assertEquals("aBC", consumed);
        assertEquals(6, reader.pos());
    }

    @Test
    public void testConsumeToEnd() {
        String consumed = reader.consumeToEnd();
        assertEquals("DEF", consumed);
        assertEquals(12, reader.pos());
    }

    @Test
    public void testConsumeToAny() {
        String consumed = reader.consumeToAny('A', 'B');
        assertEquals("aABC", consumed);
        assertEquals(7, reader.pos());
    }

    @Test
    public void testConsumeLetterSequence() {
        String consumed = reader.consumeLetterSequence();
        assertEquals("abc", consumed);
        assertEquals(7, reader.pos());
    }

    @Test
    public void testConsumeHexSequence() {
        String consumed = reader.consumeHexSequence();
        assertEquals("123", consumed);
        assertEquals(10, reader.pos());
    }

    @Test
    public void testConsumeDigitSequence() {
        String consumed = reader.consumeDigitSequence();
        assertEquals("123", consumed);
        assertEquals(10, reader.pos());
    }

    @Test
    public void testMatches() {
        assertTrue(reader.matches('a'));
        assertFalse(reader.matches('z'));
    }

    @Test
    public void testMatchesString() {
        assertTrue(reader.matches("bc"));
        assertFalse(reader.matches("xyz"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(reader.matchesIgnoreCase("bc"));
        assertTrue(reader.matchesIgnoreCase("BC"));
        assertFalse(reader.matchesIgnoreCase("xyz"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(reader.matchesAny('a', 'b'));
        assertFalse(reader.matchesAny('z', 'a'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsumeString() {
        String consumed = reader.matchConsume("bc");
        assertEquals("bc", consumed);
        assertEquals(6, reader.pos());
    }

    @Test
    public void testMatchConsumeIgnoreCaseString() {
        String consumed = reader.matchConsumeIgnoreCase("BC");
        assertEquals("BC", consumed);
        assertEquals(6, reader.pos());
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(reader.containsIgnoreCase("def"));
        assertTrue(reader.containsIgnoreCase("DEF"));
        assertFalse(reader.containsIgnoreCase("xyz"));
    }

    @Test
    public void testToString() {
        assertEquals("DEF", reader.toString());
    }
}