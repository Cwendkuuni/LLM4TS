package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {

    private String input = "abcdef1234ABC DEF5 012345";
    private CharacterReader reader = new CharacterReader(input);

    @Test
    public void testIsEmpty() {
        assertTrue(reader.isEmpty());
        reader.advance();
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testPos() {
        assertEquals(0, reader.pos());
        reader.advance();
        assertEquals(1, reader.pos());

        reader.rewindToMark();
        assertEquals(0, reader.pos());
    }

    @Test
    public void testConsume() {
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
        assertEquals('d', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals('f', reader.consume());
        assertEquals('1', reader.consume());
        assertEquals('2', reader.consume());
        assertEquals('3', reader.consume());
        assertEquals('4', reader.consume());
        assertEquals('A', reader.consume());
        assertEquals('B', reader.consume());
        assertEquals(' ', reader.consume());
        assertEquals('D', reader.consume());
        assertEquals('E', reader.consume());
        assertEquals('F', reader.consume());
        assertEquals('5', reader.consume());
        assertEquals(' ', reader.consume());
        assertEquals('0', reader.consume());
        assertEquals('1', reader.consume());
        assertEquals('2', reader.consume());
        assertEquals('3', reader.consume());
        assertEquals('4', reader.consume());
        assertEquals('5', reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testConsumeAsString() {
        assertEquals("abcdef", reader.consumeAsString());
        assertEquals("1234", reader.consumeAsString());
        assertEquals("ABC DEF", reader.consumeAsString());
        assertEquals("012345", reader.consumeToEnd());
    }

    @Test
    public void testConsumeTo() {
        assertEquals("def", reader.consumeTo('f'));
        assertEquals("DEF", reader.consumeTo("DEF"));
        assertEquals("012345", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToAny() {
        assertEquals("123", reader.consumeToAny('1', '2', '3'));
        assertEquals("DEF", reader.consumeToAny("DEF"));
        assertEquals("5", reader.consumeToAny('5'));
        assertEquals("", reader.consumeToAny('a'));
    }

    @Test
    public void testConsumeLetterSequence() {
        assertEquals("abcdef", reader.consumeLetterSequence());
        assertEquals("ABC DEF", reader.consumeLetterSequence());
        assertEquals("", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        assertEquals("1234", reader.consumeHexSequence());
        assertEquals("", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        assertEquals("1234", reader.consumeDigitSequence());
        assertEquals("", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() {
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('f'));
        assertFalse(reader.matches('g'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(reader.matchesIgnoreCase("ABC"));
        assertTrue(reader.matchesIgnoreCase("def"));
        assertFalse(reader.matchesIgnoreCase("GHI"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(reader.matchesAny('1', '2', '3'));
        assertTrue(reader.matchesAny('f'));
        assertFalse(reader.matchesAny('g'));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        assertEquals("def", reader.consumeAsString());
        reader.mark();
        assertEquals("DEF", reader.matchConsume("DEF"));
        assertEquals("GHI", reader.consumeAsString());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        assertEquals("def", reader.consumeAsString());
        reader.mark();
        assertEquals("DEF", reader.matchConsumeIgnoreCase("DEF"));
        assertEquals("GHI", reader.consumeAsString());
    }

    @Test
    public void testContainsIgnoreCase() {
        assertTrue(reader.containsIgnoreCase("def"));
        assertTrue(reader.containsIgnoreCase("DEF"));
        assertFalse(reader.containsIgnoreCase("GHI"));
    }

    @Test
    public void testToString() {
        assertEquals("", reader.toString());
        reader.consumeLetterSequence();
        assertEquals("abcdef", reader.toString());
    }
}