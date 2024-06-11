package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {
    private String input = "abcd---ef--1234--56--ABCD1234--56-";
    private CharacterReader reader = new CharacterReader(input);

    @Test
    public void testIsEmpty() {
        assertTrue(reader.isEmpty());
        reader.consume();
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testPos() {
        assertEquals(0, reader.pos());

        reader.consume();
        reader.consume();
        reader.consume();

        assertEquals(3, reader.pos());
    }

    @Test
    public void testCurrent() {
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals('b', reader.current());
    }

    @Test
    public void testConsume() {
        reader.consume();
        assertEquals('b', reader.current());

        reader.consume();
        reader.consume();
        assertEquals('d', reader.current());
    }

    @Test
    public void testUnconsume() {
        reader.consume();
        reader.consume();

        reader.unconsume();
        reader.unconsume();

        assertEquals('b', reader.current());
    }

    @Test
    public void testAdvance() {
        reader.advance();
        assertEquals('b', reader.current());

        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();

        assertEquals('e', reader.current());
    }

    @Test
    public void testMark() {
        reader.mark();
        reader.consume();
        reader.consume();

        reader.rewindToMark();
        assertEquals('b', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("ab", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        assertEquals("ef--", reader.consumeTo('-'));
        assertEquals("ABCD1234--56", reader.consumeTo("--56"));
        assertNull(reader.consumeTo('z'));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("ef--", reader.consumeToAny('-'));
        assertEquals("ABCD1234--56", reader.consumeToAny("--", '5'));
        assertEquals("ABCD1234--56", reader.consumeToAny("--", new char[]{'5'}));
        assertEquals("", reader.consumeToAny(new char[]{'z'}));
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("ABCD1234--56", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("abcd", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        assertEquals("1234", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("1234", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(reader.matches('b'));
        assertFalse(reader.matches('z'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(reader.matchesIgnoreCase("ef"));
        assertFalse(reader.matchesIgnoreCase("ZX"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(reader.matchesAny(new char[]{'d'}));
        assertFalse(reader.matchesAny(new char[]{'z'}));
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
    public void testMatchConsume() {
        assertTrue(reader.matchConsume("ef"));
        assertFalse(reader.matchConsume("zx"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(reader.matchConsumeIgnoreCase("ef"));
        assertFalse(reader.matchConsumeIgnoreCase("zx"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(reader.containsIgnoreCase("ef"));
        assertFalse(reader.containsIgnoreCase("AB"));
    }

    @Test
    public void testToString() {
        assertEquals("--56", reader.toString());
    }
}