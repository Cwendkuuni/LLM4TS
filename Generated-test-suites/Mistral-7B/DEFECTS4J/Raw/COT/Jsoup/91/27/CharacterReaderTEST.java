package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.StringReader;

/**
 * Test class for CharacterReader.
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithReader() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertEquals("Hello World", characterReader.toString());
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertEquals("Hello World", characterReader.toString());
    }

    @Test
    public void testPos() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.advance();
        characterReader.advance();
        assertEquals(2, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertFalse(characterReader.isEmpty());
        characterReader.consumeToEnd();
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertEquals('H', characterReader.current());
        characterReader.advance();
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertEquals('H', characterReader.current());
        characterReader.consume();
        assertEquals('e', characterReader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testUnconsumeWhenNoBufferLeft() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.unconsume();
        characterReader.unconsume();
        characterReader.unconsume();
        characterReader.unconsume();
        characterReader.unconsume();
    }

    @Test
    public void testAdvance() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.advance();
        characterReader.advance();
        assertEquals("llo Wrld", characterReader.toString());
    }

    @Test
    public void testMarkAndRewind() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.mark();
        characterReader.consumeTo('l');
        characterReader.rewindToMark();
        assertEquals('H', characterReader.current());
    }

    @Test
    public void testNextIndexOfSingleChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertEquals(4, characterReader.nextIndexOf('l'));
    }

    @Test
    public void testNextIndexOfMultiChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertEquals(6, characterReader.nextIndexOf("ll"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertEquals("World", characterReader.consumeTo(' '));
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertEquals("World", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertEquals("llo", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello12345 World"));
        assertEquals("llo12345", characterReader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello &#x61;&#x6C;&#x65; World"));
        assertEquals("World", characterReader.consumeToEnd());
        assertEquals("allo", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello12345 World"));
        assertEquals("12345", characterReader.consumeDigitSequence());
    }

    @Test
    public void testMatchesChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(characterReader.matches('e'));
        assertTrue(characterReader.matches('l'));
        assertFalse(characterReader.matches('o'));
    }

    @Test
    public void testMatchesString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(characterReader.matches("ll"));
        assertFalse(characterReader.matches("oo"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(characterReader.matchesIgnoreCase("ll"));
        assertFalse(characterReader.matchesIgnoreCase("OO"));
    }

    @Test
    public void testMatchesAnyChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(characterReader.matchesAny('l'));
        assertTrue(characterReader.matchesAny('e'));
        assertTrue(characterReader.matchesAny('H'));
        assertFalse(characterReader.matchesAny('z'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(characterReader.matchesAnySorted(new char[]{'l', 'e'}));
        assertTrue(characterReader.matchesAnySorted(new char[]{'l', 'l'}));
        assertFalse(characterReader.matchesAnySorted(new char[]{'z', 'l'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(characterReader.matchesLetter());
        assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello12345 World"));
        assertFalse(characterReader.matchesLetter());
        assertTrue(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsumeString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.matchConsume("ll");
        assertEquals("ld", characterReader.toString());
    }

    @Test
    public void testMatchConsumeStringIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.matchConsumeIgnoreCase("LL");
        assertEquals("ld", characterReader.toString());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html><title>Hello World</title>"));
        assertTrue(characterReader.containsIgnoreCase("<title>"));
        assertFalse(characterReader.containsIgnoreCase("<body>"));
    }
}