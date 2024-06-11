package org.jsoup.parser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {

    private CharacterReader reader;

    @Before
    public void setUp() {
        reader = new CharacterReader("Test String");
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
        assertEquals(1, reader.pos());
    }

    @Test
    public void testUnconsume() {
        reader.consume();
        reader.unconsume();
        assertEquals(0, reader.pos());
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
        assertEquals("T", reader.consumeAsString());
        assertEquals(1, reader.pos());
    }

    @Test
    public void testNextIndexOf() {
        assertEquals(2, reader.nextIndexOf('s'));
        assertEquals(-1, reader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOfWithSequence() {
        assertEquals(2, reader.nextIndexOf("st"));
        assertEquals(-1, reader.nextIndexOf("xt"));
    }

    @Test
    public void testConsumeTo() {
        assertEquals("Test ", reader.consumeTo('S'));
    }

    @Test
    public void testConsumeToWithSequence() {
        assertEquals("Test ", reader.consumeTo("Str"));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("Te", reader.consumeToAny('s', 't'));
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("Test String", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
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
        assertEquals("", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(reader.matches('T'));
    }

    @Test
    public void testMatchesWithSequence() {
        assertTrue(reader.matches("Test"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(reader.matchesIgnoreCase("test"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(reader.matchesAny('T', 'S'));
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
    public void testMatchConsume() {
        assertTrue(reader.matchConsume("Test"));
        assertEquals(4, reader.pos());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(reader.matchConsumeIgnoreCase("test"));
        assertEquals(4, reader.pos());
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(reader.containsIgnoreCase("s"));
        assertFalse(reader.containsIgnoreCase("z"));
    }

    @Test
    public void testToString() {
        assertEquals("Test String", reader.toString());
    }
}