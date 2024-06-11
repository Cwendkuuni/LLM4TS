package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {
    private CharacterReader reader;

    @Before
    public void setUp() {
        reader = new CharacterReader("Test Input");
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
        assertEquals('T', reader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('T', reader.consume());
        assertEquals('e', reader.consume());
    }

    @Test
    public void testUnconsume() {
        reader.consume();
        reader.unconsume();
        assertEquals('T', reader.current());
    }

    @Test
    public void testAdvance() {
        reader.advance();
        assertEquals('e', reader.current());
    }

    @Test
    public void testMarkRewindToMark() {
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('T', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("T", reader.consumeAsString());
    }

    @Test
    public void testNextIndexOfChar() {
        assertEquals(1, reader.nextIndexOf('e'));
        assertEquals(-1, reader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOfSeq() {
        assertEquals(1, reader.nextIndexOf("es"));
        assertEquals(-1, reader.nextIndexOf("zx"));
    }

    @Test
    public void testConsumeToChar() {
        assertEquals("Test ", reader.consumeTo('I'));
    }

    @Test
    public void testConsumeToSeq() {
        assertEquals("Test ", reader.consumeTo("In"));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("Tes", reader.consumeToAny('t', ' ', 'I', 'n'));
    }

    @Test
    public void testConsumeToAnySorted() {
        assertEquals("Tes", reader.consumeToAnySorted(' ', 'I', 'n', 't'));
    }

    @Test
    public void testConsumeData() {
        assertEquals("", reader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        assertEquals("Test", reader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("Test Input", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("Test", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        assertEquals("Test", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        assertEquals("Test", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(reader.matches('T'));
        assertFalse(reader.matches('t'));
    }

    @Test
    public void testMatchesSeq() {
        assertTrue(reader.matches("Test"));
        assertFalse(reader.matches("test"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(reader.matchesIgnoreCase("TEst"));
        assertFalse(reader.matchesIgnoreCase("test"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(reader.matchesAny('T', 't'));
        assertFalse(reader.matchesAny('A', 'a'));
    }

    @Test
    public void testMatchesAnySorted() {
        assertTrue(reader.matchesAnySorted('t', 'T'));
        assertFalse(reader.matchesAnySorted('A', 'a'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(reader.matchConsume("Test"));
        assertFalse(reader.matchConsume("test"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(reader.matchConsumeIgnoreCase("TEst"));
        assertFalse(reader.matchConsumeIgnoreCase("test"));
    }

    @Test
    public void testContainsIgnoreCase() {
        assertFalse(reader.containsIgnoreCase("Test"));
    }

    @Test
    public void testToString() {
        assertEquals("Test Input", reader.toString());
    }

    @Test
    public void testCacheString() {
        String input = "test";
        int start = 0;
        int count = 4;
        String cached = reader.cacheString(start, count);
        assertEquals("test", cached);
    }

    @Test
    public void testRangeEquals() {
        int start = 0;
        int count = 4;
        String cached = "test";
        assertTrue(reader.rangeEquals(start, count, cached));
        assertFalse(reader.rangeEquals(1, count, cached));
    }
}