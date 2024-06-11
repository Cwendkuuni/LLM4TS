package org.jsoup.parser.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals("", reader.toString());
    }

    @Test
    public void testCharacterReaderSingleCharacter() {
        CharacterReader reader = new CharacterReader("A");
        assertEquals(0, reader.pos());
        assertEquals('A', reader.current());
        assertEquals('A', reader.consume());
        assertFalse(reader.isEmpty());
        assertEquals("", reader.toString());
    }

    @Test
    public void testCharacterReaderMultipleCharacters() {
        CharacterReader reader = new CharacterReader("ABC");
        assertEquals(0, reader.pos());
        assertEquals('A', reader.current());
        assertEquals('A', reader.consume());
        assertEquals('B', reader.current());
        assertEquals('B', reader.consume());
        assertEquals('C', reader.current());
        assertEquals(3, reader.pos());
        assertEquals("BC", reader.toString());
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        CharacterReader reader = new CharacterReader("ABCDEF");
        reader.mark();
        assertEquals('A', reader.current());
        reader.advance();
        assertEquals('B', reader.current());
        reader.unconsume();
        assertEquals('A', reader.current());
        reader.rewindToMark();
        assertEquals('A', reader.current());
    }

    @Test
    public void testCharacterReaderNextIndexOfSingleCharacter() {
        CharacterReader reader = new CharacterReader("ABCABC");
        int index = reader.nextIndexOf('C');
        assertEquals(4, index);
        index = reader.nextIndexOf('B');
        assertEquals(1, index);
        index = reader.nextIndexOf('D');
        assertEquals(-1, index);
    }

    @Test
    public void testCharacterReaderNextIndexOfSequence() {
        CharacterReader reader = new CharacterReader("ABCABCABC");
        String sequence = "AB";
        int index = reader.nextIndexOf(sequence);
        assertEquals(0, index);
        index = reader.nextIndexOf("CD");
        assertEquals(-1, index);
    }

    @Test
    public void testCharacterReaderConsumeToCharacter() {
        CharacterReader reader = new CharacterReader("ABCABCABC");
        String consumed = reader.consumeTo('C');
        assertEquals("ABC", consumed);
        consumed = reader.consumeTo('B');
        assertEquals("AB", consumed);
        consumed = reader.consumeToEnd();
        assertEquals("CABC", consumed);
    }

    @Test
    public void testCharacterReaderConsumeToSequence() {
        CharacterReader reader = new CharacterReader("ABCABCABC");
        String consumed = reader.consumeTo("AB");
        assertEquals("ABC", consumed);
        consumed = reader.consumeTo("CD");
        assertEquals("", consumed);
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader("ABCABCABC");
        String consumed = reader.consumeToAny('B', 'C');
        assertEquals("ABC", consumed);
        consumed = reader.consumeToAny('A', 'Z');
        assertEquals("ABCABCABC", consumed);
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
        String consumed = reader.consumeLetterSequence();
        assertTrue(consumed.matches("^[a-zA-Z]+$"));
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123def456");
        String consumed = reader.consumeLetterThenDigitSequence();
        assertTrue(consumed.matches("^[a-zA-Z]+[0-9]+$"));
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("0x123456789ABCDEF");
        String hex = reader.consumeHexSequence();
        assertTrue(hex.matches("^[0-9A-Fa-f]+$"));
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("1234567890");
        String digit = reader.consumeDigitSequence();
        assertEquals(10, digit.length());
        assertTrue(digit.matches("^[0-9]+$"));
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader("ABCDEF");
        assertTrue(reader.matches('E'));
        assertTrue(reader.matches("EF"));
        assertFalse(reader.matches('G'));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABCDEF");
        assertTrue(reader.matchesIgnoreCase("ef"));
        assertTrue(reader.matchesIgnoreCase("ABCD"));
        assertFalse(reader.matchesIgnoreCase("GH"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader("ABCDEF");
        assertTrue(reader.matchesAny('A', 'C'));
        assertTrue(reader.matchesAny('E', 'F'));
        assertFalse(reader.matchesAny('G'));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader reader = new CharacterReader("ABCDEF");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter('5'));
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader reader = new CharacterReader("ABCDEF");
        assertFalse(reader.matchesDigit());
        assertTrue(reader.matchesDigit('5'));
    }

    @Test
    public void testCharacterReaderMatchConsume() {
        CharacterReader reader = new CharacterReader("ABCDEF");
        String matched = reader.matchConsume("EF");
        assertEquals("EF", matched);
        assertEquals(3, reader.pos());
    }

    @Test
    public void testCharacterReaderMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABCDEF");
        String matched = reader.matchConsumeIgnoreCase("ef");
        assertEquals("EF", matched);
        assertEquals(3, reader.pos());
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("</title></head><body><h1>Test</h1>");
        assertTrue(reader.containsIgnoreCase("title"));
        assertTrue(reader.containsIgnoreCase("HEAD"));
        assertFalse(reader.containsIgnoreCase("FOOTER"));
    }
}