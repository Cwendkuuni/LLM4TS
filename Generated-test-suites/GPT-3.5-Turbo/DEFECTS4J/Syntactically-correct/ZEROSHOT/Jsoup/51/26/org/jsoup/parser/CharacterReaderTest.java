package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {

    private CharacterReader reader;

    @Before
    public void setUp() {
        reader = new CharacterReader("");
    }

    @Test
    public void testPos() {
        assertEquals(0, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testConsume() {
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testUnconsume() {
        reader.unconsume();
        assertEquals(-1, reader.pos());
    }

    @Test
    public void testAdvance() {
        reader.advance();
        assertEquals(1, reader.pos());
    }

    @Test
    public void testMark() {
        reader.mark();
        assertEquals(0, reader.pos());
        reader.advance();
        reader.rewindToMark();
        assertEquals(0, reader.pos());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("", reader.consumeAsString());
    }

    @Test
    public void testNextIndexOf() {
        assertEquals(-1, reader.nextIndexOf('a'));
    }

    @Test
    public void testNextIndexOfSequence() {
        assertEquals(-1, reader.nextIndexOf("abc"));
    }

    @Test
    public void testConsumeTo() {
        assertEquals("", reader.consumeTo('a'));
    }

    @Test
    public void testConsumeToSequence() {
        assertEquals("", reader.consumeTo("abc"));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("", reader.consumeToAny('a', 'b'));
    }

    @Test
    public void testConsumeToAnySorted() {
        assertEquals("", reader.consumeToAnySorted('a', 'b'));
    }

    @Test
    public void testConsumeData() {
        assertEquals("", reader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        assertEquals("", reader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        assertEquals("", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        assertEquals("", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertFalse(reader.matches('a'));
    }

    @Test
    public void testMatchesSequence() {
        assertFalse(reader.matches("abc"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertFalse(reader.matchesIgnoreCase("abc"));
    }

    @Test
    public void testMatchesAny() {
        assertFalse(reader.matchesAny('a', 'b'));
    }

    @Test
    public void testMatchesAnySorted() {
        assertFalse(reader.matchesAnySorted('a', 'b'));
    }

    @Test
    public void testMatchesLetter() {
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertFalse(reader.matchConsume("abc"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertFalse(reader.matchConsumeIgnoreCase("abc"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertFalse(reader.containsIgnoreCase("abc"));
    }

    @Test
    public void testToString() {
        assertEquals("", reader.toString());
    }

    @Test
    public void testCacheString() {
        assertEquals("", reader.cacheString(0, 0));
    }

    @Test
    public void testRangeEquals() {
        assertFalse(reader.rangeEquals(0, 0, "abc"));
    }

}