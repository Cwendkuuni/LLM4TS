package org.jsoup.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderSingleCharacter() {
        CharacterReader reader = new CharacterReader("A");
        assertFalse(reader.isEmpty());
        assertEquals('A', reader.current());
        reader.consume();
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderMultipleCharacters() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertFalse(reader.isEmpty());
        assertEquals('H', reader.current());
        reader.consume();
        assertEquals('e', reader.current());
        reader.consume();
        assertEquals('l', reader.current());
        reader.consume();
        assertEquals('l', reader.current());
        reader.consume();
        assertEquals('o', reader.current());
        reader.consume();
        assertEquals(' ', reader.current());
        reader.consume();
        assertEquals('W', reader.current());
        reader.consume();
        assertEquals('o', reader.current());
        reader.consume();
        assertEquals('r', reader.current());
        reader.consume();
        assertEquals('l', reader.current());
        reader.consume();
        assertEquals('d', reader.current());
        reader.consume();
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeSingleCharacter() {
        CharacterReader reader = new CharacterReader("A");
        assertEquals('A', reader.consume());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeMultipleCharacters() {
        CharacterReader reader = new CharacterReader("Hello World");
        String consumed = "";
        consumed += reader.consume();
        consumed += reader.consume();
        consumed += reader.consume();
        consumed += reader.consume();
        consumed += reader.consume();
        consumed += reader.consume();
        consumed += reader.consume();
        consumed += reader.consume();
        consumed += reader.consume();
        consumed += reader.consume();
        consumed += reader.consume();
        consumed += reader.consume();
        consumed += reader.consume();
        consumed += reader.consume();
        consumed += reader.consume();
        consumed += reader.consume();
        consumed += reader.consume();
        consumed += reader.consume();
        consumed += reader.consume();
        consumed += reader.consume();
        consumed += reader.consume();
        consumed += reader.consume();
        consumed += reader.consume();
        consumed += reader.consume();
        consumed += reader.consume();
        consumed += reader.consume();
        consumed += reader.consume();
        consumed += reader.consume();
        consumed += reader.consume();
        consumed += reader.consume();
        consumed += reader.consume();
        consumed += reader.consume();
        consumed += reader.consume();
        assertTrue(reader.isEmpty());
        assertEquals("Hello World", consumed);
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        CharacterReader reader = new CharacterReader("Hello World");
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('H', reader.current());
    }

    @Test
    public void testCharacterReaderNextIndexOfSingleCharacter() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals(11, reader.nextIndexOf('o'));
    }

    @Test
    public void testCharacterReaderNextIndexOfString() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals(11, reader.nextIndexOf("o"));
        assertEquals(6, reader.nextIndexOf("World"));
    }

    @Test
    public void testCharacterReaderConsumeToSingleCharacter() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals("o", reader.consumeTo('o'));
    }

    @Test
    public void testCharacterReaderConsumeToString() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals("World", reader.consumeTo("World"));
    }

    @Test
    public void testCharacterReaderConsumeToAnySingleCharacter() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals("l", reader.consumeToAny(new char[]{'l'}));
    }

    @Test
    public void testCharacterReaderConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals("l Wo", reader.consumeToAnySorted(new char[]{'l', 'W'}));
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader reader = new CharacterReader("& < null");
        assertEquals("& < null", reader.consumeData());
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader reader = new CharacterReader("\t Hello <title>World</title> \r\n");
        assertEquals("\t Hello <title>", reader.consumeTagName());
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertEquals("Hello World", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("HelloWorld");
        assertEquals("lloWorld", reader.consumeLetterSequence());
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("123HelloWorld456");
        assertEquals("HelloWorld", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testCharacterReaderMatchesSingleCharacter() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertTrue(reader.matches('o'));
        assertFalse(reader.matches('a'));
    }

    @Test
    public void testCharacterReaderMatchesString() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertTrue(reader.matches("o"));
        assertFalse(reader.matches("a"));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCaseString() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertTrue(reader.matchesIgnoreCase("o"));
        assertTrue(reader.matchesIgnoreCase("o"));
        assertTrue(reader.matchesIgnoreCase("O"));
        assertFalse(reader.matchesIgnoreCase("a"));
    }

    @Test
    public void testCharacterReaderMatchesAnySingleCharacter() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertTrue(reader.matchesAny('o'));
        assertFalse(reader.matchesAny('a'));
    }

    @Test
    public void testCharacterReaderMatchesAnySorted() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertTrue(reader.matchesAnySorted(new char[]{'l', 'o'}));
        assertFalse(reader.matchesAnySorted(new char[]{'a'}));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testCharacterReaderMatchConsumeSingleCharacter() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertTrue(reader.matchConsume("o"));
    }

    @Test
    public void testCharacterReaderMatchConsumeIgnoreCaseString() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertTrue(reader.matchConsumeIgnoreCase("o"));
        assertTrue(reader.matchConsumeIgnoreCase("o"));
        assertTrue(reader.matchConsumeIgnoreCase("O"));
    }

    @Test
    public void testCharacterReaderContainsIgnoreCaseString() {
        CharacterReader reader = new CharacterReader("Hello World");
        assertTrue(reader.containsIgnoreCase("/title"));
        assertTrue(reader.containsIgnoreCase("/TITLE"));
        assertFalse(reader.containsIgnoreCase("/style"));
    }
}