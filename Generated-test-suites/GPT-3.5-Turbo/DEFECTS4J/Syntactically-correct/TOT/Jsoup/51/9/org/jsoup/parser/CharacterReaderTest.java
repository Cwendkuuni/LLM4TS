package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {

    private CharacterReader reader;

    @Before
    public void setup() {
        reader = new CharacterReader("This is a test string");
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
        assertEquals('h', reader.consume());
        assertEquals('i', reader.consume());
        assertEquals('s', reader.consume());
    }

    @Test
    public void testUnconsume() {
        reader.consume();
        reader.consume();
        assertEquals('h', reader.current());
        reader.unconsume();
        assertEquals('h', reader.current());
    }

    @Test
    public void testAdvance() {
        assertEquals('T', reader.current());
        reader.advance();
        assertEquals('h', reader.current());
    }

    @Test
    public void testMarkAndRewindToMark() {
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('T', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("T", reader.consumeAsString());
        assertEquals("h", reader.consumeAsString());
        assertEquals("i", reader.consumeAsString());
        assertEquals("s", reader.consumeAsString());
    }

    @Test
    public void testNextIndexOf_characterFound() {
        assertEquals(2, reader.nextIndexOf('i'));
    }

    @Test
    public void testNextIndexOf_characterNotFound() {
        assertEquals(-1, reader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOf_sequenceFound() {
        assertEquals(2, reader.nextIndexOf("is"));
    }

    @Test
    public void testNextIndexOf_sequenceNotFound() {
        assertEquals(-1, reader.nextIndexOf("xyz"));
    }

    @Test
    public void testConsumeTo_characterFound() {
        assertEquals("This ", reader.consumeTo('i'));
    }

    @Test
    public void testConsumeTo_characterNotFound() {
        assertEquals("This is a test string", reader.consumeTo('z'));
    }

    @Test
    public void testConsumeTo_sequenceFound() {
        assertEquals("This ", reader.consumeTo(" is"));
    }

    @Test
    public void testConsumeTo_sequenceNotFound() {
        assertEquals("This is a test string", reader.consumeTo("xyz"));
    }

    @Test
    public void testConsumeToAny_charactersFound() {
        assertEquals("This is a ", reader.consumeToAny('t', 's'));
    }

    @Test
    public void testConsumeToAny_charactersNotFound() {
        assertEquals("This is a test string", reader.consumeToAny('x', 'y', 'z'));
    }

    @Test
    public void testConsumeToAnySorted_charactersFound() {
        assertEquals("This is a ", reader.consumeToAnySorted('a', 'e', 'i', 'o', 'u'));
    }

    @Test
    public void testConsumeToAnySorted_charactersNotFound() {
        assertEquals("This is a test string", reader.consumeToAnySorted('a', 'e', 'i', 'o', 'u', 'x', 'y', 'z'));
    }

    @Test
    public void testConsumeData() {
        assertEquals("This ", reader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        assertEquals("This", reader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("This is a test string", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("This", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        assertEquals("This", reader.consumeLetterThenDigitSequence());
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
    public void testMatches_characterFound() {
        assertTrue(reader.matches('T'));
    }

    @Test
    public void testMatches_characterNotFound() {
        assertFalse(reader.matches('z'));
    }

    @Test
    public void testMatches_sequenceFound() {
        assertTrue(reader.matches("This"));
    }

    @Test
    public void testMatches_sequenceNotFound() {
        assertFalse(reader.matches("xyz"));
    }

    @Test
    public void testMatchesIgnoreCase_sequenceFound() {
        assertTrue(reader.matchesIgnoreCase("tHIS"));
    }

    @Test
    public void testMatchesIgnoreCase_sequenceNotFound() {
        assertFalse(reader.matchesIgnoreCase("xyz"));
    }

    @Test
    public void testMatchesAny_charactersFound() {
        assertTrue(reader.matchesAny('h', 'T'));
    }

    @Test
    public void testMatchesAny_charactersNotFound() {
        assertFalse(reader.matchesAny('x', 'y', 'z'));
    }

    @Test
    public void testMatchesAnySorted_charactersFound() {
        assertTrue(reader.matchesAnySorted('T', 'h'));
    }

    @Test
    public void testMatchesAnySorted_charactersNotFound() {
        assertFalse(reader.matchesAnySorted('a', 'e', 'i', 'o', 'u', 'x', 'y', 'z'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume_sequenceFound() {
        assertTrue(reader.matchConsume("This "));
        assertEquals("is a test string", reader.toString());
    }

    @Test
    public void testMatchConsume_sequenceNotFound() {
        assertFalse(reader.matchConsume("xyz"));
        assertEquals("This is a test string", reader.toString());
    }

    @Test
    public void testMatchConsumeIgnoreCase_sequenceFound() {
        assertTrue(reader.matchConsumeIgnoreCase("tHIS "));
        assertEquals("is a test string", reader.toString());
    }

    @Test
    public void testMatchConsumeIgnoreCase_sequenceNotFound() {
        assertFalse(reader.matchConsumeIgnoreCase("xyz"));
        assertEquals("This is a test string", reader.toString());
    }

    @Test
    public void testContainsIgnoreCase_sequenceFound() {
        assertTrue(reader.containsIgnoreCase("hiS is a tEST"));
    }

    @Test
    public void testContainsIgnoreCase_sequenceNotFound() {
        assertFalse(reader.containsIgnoreCase("xyz"));
    }
}