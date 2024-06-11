package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {
    private CharacterReader reader;
    private String input;

    @Before
    public void setup() {
        input = "This is a test";
        reader = new CharacterReader(input);
    }

    // Test case 1 for pos() method
    @Test
    public void testPos() {
        assertEquals(0, reader.pos());
    }

    // Test case 1 for isEmpty() method
    @Test
    public void testIsEmpty() {
        assertFalse(reader.isEmpty());
    }

    // Test case 1 for current() method
    @Test
    public void testCurrent() {
        assertEquals('T', reader.current());
    }

    // Test case 1 for consume() method
    @Test
    public void testConsume() {
        assertEquals('T', reader.consume());
        assertEquals('h', reader.current());
    }

    // Test case 1 for unconsume() method
    @Test
    public void testUnconsume() {
        reader.consume();
        reader.unconsume();
        assertEquals('T', reader.current());
    }

    // Test case 1 for advance() method
    @Test
    public void testAdvance() {
        reader.advance();
        assertEquals('h', reader.current());
    }

    // Test case 1 for mark() method
    @Test
    public void testMark() {
        reader.mark();
        assertEquals(0, reader.pos());
    }

    // Test case 1 for rewindToMark() method
    @Test
    public void testRewindToMark() {
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('T', reader.current());
    }

    // Test case 1 for consumeAsString() method
    @Test
    public void testConsumeAsString() {
        assertEquals("T", reader.consumeAsString());
    }

    // Test case 1 for consumeTo() method
    @Test
    public void testConsumeTo() {
        assertEquals("This is a ", reader.consumeTo('t'));
    }

    // Test case 1 for consumeToAny() method
    @Test
    public void testConsumeToAny() {
        assertEquals("This is ", reader.consumeToAny('a', 'e'));
    }

    // Test case 1 for consumeData() method
    @Test
    public void testConsumeData() {
        assertEquals("This is ", reader.consumeData());
    }

    // Test case 1 for consumeLetterSequence() method
    @Test
    public void testConsumeLetterSequence() {
        assertEquals("This", reader.consumeLetterSequence());
    }

    // Test case 1 for consumeLetterThenDigitSequence() method
    @Test
    public void testConsumeLetterThenDigitSequence() {
        assertEquals("This", reader.consumeLetterThenDigitSequence());
    }

    // Test case 1 for consumeHexSequence() method
    @Test
    public void testConsumeHexSequence() {
        assertEquals("This", reader.consumeHexSequence());
    }

    // Test case 1 for consumeDigitSequence() method
    @Test
    public void testConsumeDigitSequence() {
        assertEquals("", reader.consumeDigitSequence());
    }

    // Test case 1 for matches() method
    @Test
    public void testMatches() {
        assertFalse(reader.matches('t'));
        assertTrue(reader.matches('T'));
    }

    // Test case 1 for matchesIgnoreCase() method
    @Test
    public void testMatchesIgnoreCase() {
        assertFalse(reader.matchesIgnoreCase("t"));
        assertTrue(reader.matchesIgnoreCase("T"));
    }

    // Test case 1 for matchesAny() method
    @Test
    public void testMatchesAny() {
        assertFalse(reader.matchesAny('t', 'o'));
        assertTrue(reader.matchesAny('T', 'i'));
    }

    // Test case 1 for matchesAnySorted() method
    @Test
    public void testMatchesAnySorted() {
        assertFalse(reader.matchesAnySorted('t', 'o'));
        assertTrue(reader.matchesAnySorted('T', 'i'));
    }

    // Test case 1 for matchesLetter() method
    @Test
    public void testMatchesLetter() {
        assertTrue(reader.matchesLetter());
    }

    // Test case 1 for matchesDigit() method
    @Test
    public void testMatchesDigit() {
        assertFalse(reader.matchesDigit());
    }
}