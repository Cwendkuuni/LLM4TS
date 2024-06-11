package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;

/**
 * CharacterReaderTest class for testing the CharacterReader class.
 */
public class CharacterReaderTest {

    private CharacterReader reader;

    @Before
    public void setUp() {
        reader = new CharacterReader(new StringReader("Hello World"));
    }

    /**
     * Test empty character reader.
     */
    @Test
    public void testIsEmpty() {
        CharacterReader emptyReader = new CharacterReader(new StringReader(""));
        assertTrue(emptyReader.isEmpty());
    }

    /**
     * Test character reader with content.
     */
    @Test
    public void testCharacterReader() {
        assertEquals('H', reader.current());
        reader.consume();
        assertEquals('e', reader.current());
        reader.advance();
        assertEquals('l', reader.current());
        reader.mark();
        reader.unconsume();
        reader.rewindToMark();
        assertEquals('e', reader.current());
        reader.consume();
        reader.advance();
        assertEquals('l', reader.current());
        reader.consumeTo('l');
        assertEquals("ll", reader.consumeToEnd());
        reader.consumeToAny('o');
        assertEquals("rld", reader.consumeToAnyEnd());
        reader.consumeData();
        assertEquals("llorld", reader.consumeData());
        reader.consumeTagName();
        assertEquals("Hello", reader.consumeTagName());
    }

    /**
     * Test exception handling.
     */
    @Test(expected = UncheckedIOException.class)
    public void testIOException() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        reader.bufferUp();
        throw new RuntimeException("Test exception");
    }

    /**
     * Test consumeTo with null char sequence.
     */
    @Test
    public void testConsumeToNull() {
        reader.consumeTo(null);
        assertEquals("", reader.consumeToEnd());
    }

    /**
     * Test consumeTo with empty CharSequence.
     */
    @Test
    public void testConsumeToEmpty() {
        reader.consumeTo("");
        assertEquals("", reader.consumeToEnd());
    }

    /**
     * Test consumeToAny with null char array.
     */
    @Test
    public void testConsumeToAnyNull() {
        reader.consumeToAny((char[]) null);
        assertEquals("", reader.consumeToAnyEnd());
    }

    /**
     * Test consumeToAny with empty char array.
     */
    @Test
    public void testConsumeToAnyEmpty() {
        reader.consumeToAny(new char[0]);
        assertEquals("", reader.consumeToAnyEnd());
    }

    /**
     * Test consumeToAny with char array containing duplicate characters.
     */
    @Test
    public void testConsumeToAnyDuplicate() {
        reader.consumeToAny(new char[]{'l', 'l'});
        assertEquals("ll", reader.consumeToAnyEnd());
    }

    /**
     * Test consumeToAny with char array containing non-matching characters.
     */
    @Test
    public void testConsumeToAnyMismatch() {
        reader.consumeToAny(new char[]{'o', 'l'});
        assertEquals("rld", reader.consumeToAnyEnd());
    }

    /**
     * Test matches method.
     */
    @Test
    public void testMatches() {
        reader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(reader.matches('H'));
        assertTrue(reader.matches("He"));
        assertFalse(reader.matches('W'));
    }

    /**
     * Test matchesIgnoreCase method.
     */
    @Test
    public void testMatchesIgnoreCase() {
        reader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(reader.matchesIgnoreCase("He"));
        assertTrue(reader.matchesIgnoreCase("hello"));
        assertFalse(reader.matchesIgnoreCase("World"));
    }

    /**
     * Test matchesAny method.
     */
    @Test
    public void testMatchesAny() {
        reader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(reader.matchesAny('H'));
        assertTrue(reader.matchesAny('e'));
        assertTrue(reader.matchesAny('l'));
        assertTrue(reader.matchesAny('W'));
        assertFalse(reader.matchesAny('x'));
    }

    /**
     * Test matchesAnySorted method.
     */
    @Test
    public void testMatchesAnySorted() {
        reader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(reader.matchesAnySorted(new char[]{'H', 'e', 'l', 'W'}));
        assertFalse(reader.matchesAnySorted(new char[]{'x', 'H', 'l'}));
    }

    /**
     * Test matchesLetter method.
     */
    @Test
    public void testMatchesLetter() {
        reader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter(' '));
    }

    /**
     * Test matchesDigit method.
     */
    @Test
    public void testMatchesDigit() {
        reader = new CharacterReader(new StringReader("Hello World"));
        assertFalse(reader.matchesDigit());
        reader = new CharacterReader(new StringReader("12345"));
        assertTrue(reader.matchesDigit());
    }

    /**
     * Test matchConsume method.
     */
    @Test
    public void testMatchConsume() {
        reader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(reader.matchConsume("He"));
        assertEquals("llo World", reader.consumeToEnd());
    }

    /**
     * Test matchConsumeIgnoreCase method.
     */
    @Test
    public void testMatchConsumeIgnoreCase() {
        reader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(reader.matchConsumeIgnoreCase("He"));
        assertEquals("llo World", reader.consumeToEnd());
    }

    /**
     * Test containsIgnoreCase method.
     */
    @Test
    public void testContainsIgnoreCase() {
        reader = new CharacterReader(new StringReader("<html><head><title>Test</title></head>"));
        assertTrue(reader.containsIgnoreCase("title"));
        assertTrue(reader.containsIgnoreCase("HEAD"));
        assertFalse(reader.containsIgnoreCase("body"));
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString() {
        reader = new CharacterReader(new StringReader("Hello World"));
        assertEquals("Hello World", reader.toString());
    }

    /**
     * Test consumeToEnd method.
     */
    @Test
    public void testConsumeToEnd() {
        reader = new CharacterReader(new StringReader("Hello World"));
        assertEquals("World", reader.consumeToEnd());
    }

    /**
     * Test consumeLetterSequence method.
     */
    @Test
    public void testConsumeLetterSequence() {
        reader = new CharacterReader(new StringReader("Hello World"));
        assertEquals("Hello", reader.consumeLetterSequence());
    }

    /**
     * Test consumeLetterThenDigitSequence method.
     */
    @Test
    public void testConsumeLetterThenDigitSequence() {
        reader = new CharacterReader(new StringReader("Hello12345"));
        assertEquals("Hello", reader.consumeLetterThenDigitSequence());
        assertEquals("5", reader.consumeDigitSequence());
    }

    /**
     * Test consumeHexSequence method.
     */
    @Test
    public void testConsumeHexSequence() {
        reader = new CharacterReader(new StringReader("0x1a 0x2b 0x3c 0x4d"));
        assertEquals("1a2b3c4d", reader.consumeHexSequence());
    }

    /**
     * Test consumeDigitSequence method.
     */
    @Test
    public void testConsumeDigitSequence() {
        reader = new CharacterReader(new StringReader("12345"));
        assertEquals("12345", reader.consumeDigitSequence());
    }
}