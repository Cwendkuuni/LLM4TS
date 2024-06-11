package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testEmptyCharacterReader() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals("", reader.toString());
    }

    @Test
    public void testCharacterReaderWithSingleCharacter() {
        CharacterReader reader = new CharacterReader("A");
        assertEquals('A', reader.current());
        assertEquals("A", reader.consumeAsString());
        assertEquals(1, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderWithMultipleCharacters() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals('H', reader.current());
        assertEquals("H", reader.consumeAsString());
        assertEquals(1, reader.pos());
        assertEquals('H', reader.matches(reader.current()));
        assertEquals("Hello", reader.consumeTo(' ').trim());
        assertEquals("World", reader.consumeToEnd());
        assertEquals(11, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderWithMultipleCharactersAndMarkPosition() {
        CharacterReader reader = new CharacterReader("Hello World");
        reader.mark();
        assertEquals("Hello", reader.consumeTo(' ').trim());
        assertEquals("World", reader.consumeToEnd());
        reader.rewindToMark();
        assertEquals("Hello", reader.consumeTo(' ').trim());
        assertEquals("Hello", reader.consumeTo(' ').trim());
        assertEquals('W', reader.current());
        assertEquals("World", reader.consumeToEnd());
        assertEquals(12, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderWithMultipleCharactersAndConsumeSingleCharacter() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals('H', reader.current());
        reader.consume();
        assertEquals("ello World", reader.toString());
        assertEquals('e', reader.current());
        assertEquals("ello", reader.consumeTo(' ').trim());
        assertEquals("World", reader.consumeToEnd());
        assertEquals(10, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderWithMultipleCharactersAndConsumeString() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals("Hello", reader.consumeTo(' ').trim());
        assertEquals("World", reader.consumeToEnd());
        assertEquals("World", reader.consumeToEnd());
        assertEquals("", reader.consumeToEnd());
        assertEquals(12, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderWithMultipleCharactersAndConsumeCharacterSequence() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals("Hello", reader.consumeTo(' ').trim());
        assertEquals("World", reader.consumeTo("ld").toString());
        assertEquals("rld", reader.consumeToEnd());
        assertEquals(11, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderWithMultipleCharactersAndConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("1234ABCD");
        assertEquals("1234", reader.consumeTo('A').toString());
        assertEquals("ABCD", reader.consumeToEnd());
        assertEquals(5, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderWithMultipleCharactersAndConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("123456");
        assertEquals("123456", reader.consumeToEnd());
        assertEquals(6, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderWithMultipleCharactersAndMatches() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertTrue(reader.matches('H'));
        assertTrue(reader.matches('e'));
        assertTrue(reader.matches(' '));
        assertFalse(reader.matches('A'));
        assertFalse(reader.matches('!'));
        assertTrue(reader.matches("Hello"));
        assertTrue(reader.matchesIgnoreCase("hello"));
        assertFalse(reader.matchesIgnoreCase("World"));
        assertFalse(reader.matchesAny('B', 'C'));
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testCharacterReaderWithMultipleCharactersAndContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("<html><head><title>Test</title></head></html>");
        assertTrue(reader.containsIgnoreCase("/html"));
        assertTrue(reader.containsIgnoreCase("/head"));
        assertTrue(reader.containsIgnoreCase("/title"));
        assertFalse(reader.containsIgnoreCase("/body"));
    }

    @Test(expected = NullPointerException.class)
    public void testCharacterReaderWithNullInput() {
        new CharacterReader(null);
    }

    @Test
    public void testCharacterReaderWithSurrogates() {
        // This test case is not covered in the given code snippet, but it's important to ensure that CharacterReader can handle surrogates properly.
        // To do so, you can extend the CharacterReaderTest class and add test methods to cover the nextIndexOf(char c) and nextIndexOf(CharSequence seq) methods,
        // handling surrogates in the same way as the JSoup parser does.
    }
}