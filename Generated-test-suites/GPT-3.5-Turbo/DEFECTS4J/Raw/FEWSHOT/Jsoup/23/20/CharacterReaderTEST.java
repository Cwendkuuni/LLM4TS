
package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class CharacterReaderTest {
    private CharacterReader reader;

    @Before
    public void setup() {
        reader = new CharacterReader("Hello World");
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
        assertEquals('H', reader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('H', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals('l', reader.consume());
        assertEquals('l', reader.consume());
        assertEquals('o', reader.consume());
    }

    @Test
    public void testUnconsume() {
        assertEquals('H', reader.consume());
        reader.unconsume();
        assertEquals('H', reader.consume());
    }

    @Test
    public void testAdvance() {
        assertEquals('H', reader.consume());
        reader.advance();
        assertEquals('l', reader.current());
        reader.advance();
        assertEquals('l', reader.current());
    }

    @Test
    public void testMarkAndRewindToMark() {
        reader.mark();
        assertEquals('H', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals('l', reader.consume());
        reader.rewindToMark();
        assertEquals('H', reader.consume());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("H", reader.consumeAsString());
        assertEquals("e", reader.consumeAsString());
        assertEquals("l", reader.consumeAsString());
        assertEquals("l", reader.consumeAsString());
        assertEquals("o", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo_char() {
        assertEquals("", reader.consumeTo('H'));
        assertEquals("Hello W", reader.consumeTo('o'));
    }

    @Test
    public void testConsumeTo_seq() {
        assertEquals("", reader.consumeTo("He"));
        assertEquals("l", reader.consumeTo("el"));
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("Hello ", reader.consumeToAny('W', 'o'));
        assertEquals("World", reader.consumeToAny('!'));
    }

    @Test
    public void testConsumeToEnd() {
        assertEquals("Hello World", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("Hello", reader.consumeLetterSequence());
        assertEquals(" ", reader.consumeLetterSequence());
        assertEquals("World", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        assertEquals("", reader.consumeHexSequence());
        assertEquals("Hello", reader.consumeHexSequence());
        assertEquals(" ", reader.consumeHexSequence());
        assertEquals("World", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("", reader.consumeDigitSequence());
        assertEquals("Hello", reader.consumeDigitSequence());
        assertEquals(" ", reader.consumeDigitSequence());
        assertEquals("World", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches_char() {
        assertTrue(reader.matches('H'));
        reader.consume();
        assertTrue(reader.matches('e'));
    }

    @Test
    public void testMatches_seq() {
        assertTrue(reader.matches("Hello"));
        reader.consume();
        assertTrue(reader.matches("ello"));
    }

    @Test
    public void testMatchesIgnoreCase_seq() {
        assertTrue(reader.matchesIgnoreCase("HELLO"));
        reader.consume();
        assertTrue(reader.matchesIgnoreCase("ELLO"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(reader.matchesAny('H', 'e'));
        reader.consume();
        assertTrue(reader.matchesAny('e', 'l'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(reader.matchesLetter());
        reader.consume();
        assertTrue(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertFalse(reader.matchesDigit());
        reader.consume();
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume_seq() {
        assertTrue(reader.matchConsume("Hello"));
        assertEquals(" World", reader.toString());
    }

    @Test
    public void testMatchConsumeIgnoreCase_seq() {
        assertTrue(reader.matchConsumeIgnoreCase("HELLO"));
        assertEquals(" World", reader.toString());
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(reader.containsIgnoreCase("HELLO"));
        assertTrue(reader.containsIgnoreCase("ELL"));
        assertFalse(reader.containsIgnoreCase("WORLD"));
    }
}
