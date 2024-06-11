package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Test;

public class CharacterReaderTest {
    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader("test");
        int pos = reader.pos();
        assertEquals(0, pos);
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("test");
        boolean empty = reader.isEmpty();
        assertFalse(empty);
    }

    @Test
    public void testCurrentEmpty() {
        CharacterReader reader = new CharacterReader("");
        char current = reader.current();
        assertEquals(CharacterReader.EOF, current);
    }

    @Test
    public void testCurrentNonEmpty() {
        CharacterReader reader = new CharacterReader("test");
        char current = reader.current();
        assertEquals('t', current);
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("test");
        char consumed = reader.consume();
        assertEquals('t', consumed);
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader("test");
        reader.consume();
        reader.unconsume();
        char consumed = reader.consume();
        assertEquals('t', consumed);
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader("test");
        reader.advance();
        char current = reader.current();
        assertEquals('e', current);
    }

    @Test
    public void testMarkRewindToMark() {
        CharacterReader reader = new CharacterReader("test");
        reader.consume();
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        char current = reader.current();
        assertEquals('t', current);
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("test");
        String consumed = reader.consumeAsString();
        assertEquals("t", consumed);
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("Hello World");
        String consumed = reader.consumeTo(' ');
        assertEquals("Hello", consumed);
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("Hello World");
        String consumed = reader.consumeToAny('l', 'd');
        assertEquals("He", consumed);
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("Hello World");
        String consumed = reader.consumeToEnd();
        assertEquals("Hello World", consumed);
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("Hello World");
        String consumed = reader.consumeLetterSequence();
        assertEquals("Hello", consumed);
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("0123456789ABCDEFabcdef");
        String consumed = reader.consumeHexSequence();
        assertEquals("0123456789ABCDEFabcdef", consumed);
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("0123456789");
        String consumed = reader.consumeDigitSequence();
        assertEquals("0123456789", consumed);
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("test");
        boolean matches = reader.matches('t');
        assertTrue(matches);
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("Test");
        boolean matches = reader.matchesIgnoreCase("te");
        assertTrue(matches);
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("Hello World");
        boolean matches = reader.matchesAny('H', 'W');
        assertTrue(matches);
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("Hello");
        boolean matches = reader.matchesLetter();
        assertTrue(matches);
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("12345");
        boolean matches = reader.matchesDigit();
        assertTrue(matches);
    }

    @Test
    public void testMatchConsume() {
        CharacterReader reader = new CharacterReader("test");
        boolean matched = reader.matchConsume("te");
        assertTrue(matched);
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("Test");
        boolean matched = reader.matchConsumeIgnoreCase("te");
        assertTrue(matched);
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("Lorem Ipsum");
        boolean contains = reader.containsIgnoreCase("ipsum");
        assertTrue(contains);
    }
}
