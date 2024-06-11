
package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class CharacterReaderTest {

    private CharacterReader reader;

    @Before
    public void setUp() {
        reader = new CharacterReader("testing 1 2 3");
    }

    @Test
    public void testPos() {
        assertEquals(0, reader.pos());
        reader.advance();
        assertEquals(1, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(reader.isEmpty());
        while (!reader.isEmpty()) {
            reader.consume();
        }
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        assertEquals('t', reader.current());
        reader.consume();
        assertEquals('e', reader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('t', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals('s', reader.consume());
    }

    @Test
    public void testUnconsume() {
        reader.consume();
        reader.consume();
        reader.unconsume();
        assertEquals('e', reader.current());
    }

    @Test
    public void testAdvance() {
        reader.advance();
        assertEquals('e', reader.current());
        reader.advance();
        assertEquals('s', reader.current());
    }

    @Test
    public void testMarkRewindToMark() {
        reader.consume();
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('t', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("t", reader.consumeAsString());
        assertEquals("e", reader.consumeAsString());
        assertEquals("s", reader.consumeAsString());
    }

    @Test
    public void testNextIndexOf() {
        assertEquals(8, reader.nextIndexOf('1'));
        assertEquals(10, reader.nextIndexOf('2'));
        assertEquals(12, reader.nextIndexOf('3'));
        assertEquals(-1, reader.nextIndexOf('4'));
    }

    @Test
    public void testNextIndexOfSequence() {
        assertEquals(8, reader.nextIndexOf("1"));
        assertEquals(10, reader.nextIndexOf("2"));
        assertEquals(12, reader.nextIndexOf("3"));
        assertEquals(7, reader.nextIndexOf("ting"));
        assertEquals(-1, reader.nextIndexOf("4"));
    }

    @Test
    public void testConsumeTo() {
        assertEquals("test", reader.consumeTo('i'));
        assertEquals("ng ", reader.consumeTo('1'));
        assertEquals("", reader.consumeTo('4'));
    }

    @Test
    public void testConsumeToSequence() {
        assertEquals("test", reader.consumeTo("i"));
        assertEquals("ng ", reader.consumeTo("1"));
        assertEquals(" 2 3", reader.consumeTo("4"));
        assertEquals("", reader.consumeTo("5"));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("testing ", reader.consumeToAny('1', '4'));
        assertEquals("1", reader.consumeToAny('2'));
        assertEquals("", reader.consumeToAny('9'));
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("testing 1 2 3", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("testing", reader.consumeLetterSequence());
        assertEquals(" ", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        assertEquals("testing", reader.consumeLetterThenDigitSequence());
        assertEquals(" ", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        assertEquals("t", reader.consumeHexSequence());
        assertEquals("e", reader.consumeHexSequence());
        assertEquals("s", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("1", reader.consumeDigitSequence());
        assertEquals(" ", reader.consumeDigitSequence());
        assertEquals("2", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(reader.matches('t'));
        assertFalse(reader.matches('e'));
    }

    @Test
    public void testMatchesSequence() {
        assertTrue(reader.matches("test"));
        assertFalse(reader.matches("fail"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(reader.matchesIgnoreCase("TEST"));
        assertFalse(reader.matchesIgnoreCase("FAIL"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(reader.matchesAny('t', 'p'));
        assertFalse(reader.matchesAny('s', 'p'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(reader.matchesLetter());
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertTrue(reader.matchConsume("test"));
        assertEquals("ing 1 2 3", reader.toString());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertTrue(reader.matchConsumeIgnoreCase("TEST"));
        assertEquals("ing 1 2 3", reader.toString());
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(reader.containsIgnoreCase("TESTING"));
        assertFalse(reader.containsIgnoreCase("FAIL"));
    }
}

