package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.UncheckedIOException;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Locale;

import org.junit.Test;

/**
 * CharacterReaderTest.java
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithReader() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello, World!"));
        assertEquals("Hello, World!", characterReader.toString());
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        CharacterReader characterReader = new CharacterReader("Hello, World!");
        assertEquals("Hello, World!", characterReader.toString());
    }

    @Test
    public void testCharacterReaderConstructorWithReaderAndSize() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello, World!"), 10);
        assertEquals("Hello, World!", characterReader.toString());
    }

    @Test
    public void testBufferUp() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello, World!"));
        characterReader.bufferUp();
        assertEquals(0, characterReader.pos());
        assertEquals(false, characterReader.isEmpty());
        assertEquals('H', characterReader.current());
        characterReader.consume();
        assertEquals('H', characterReader.current());
        characterReader.consume();
        assertEquals('e', characterReader.current());
        characterReader.unconsume();
        assertEquals('e', characterReader.current());
        characterReader.mark();
        characterReader.rewindToMark();
        assertEquals('e', characterReader.current());
        characterReader.advance();
        assertEquals('l', characterReader.current());
        characterReader.consumeTo('o');
        assertEquals("ello", characterReader.toString());
        characterReader.consumeToEnd();
        assertEquals("Hello, World!", characterReader.toString());
    }

    @Test(expected = UncheckedIOException.class)
    public void testBufferUpWithIOException() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello, World!")) {
            @Override
            public void bufferUp() {
                throw new IOException("Test Exception");
            }
        };
        characterReader.consume();
    }

    @Test
    public void testPos() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello, World!"));
        characterReader.bufferUp();
        assertEquals(0, characterReader.pos());
        characterReader.consume();
        characterReader.consume();
        assertEquals(2, characterReader.pos());
        characterReader.advance();
        assertEquals(3, characterReader.pos());
        characterReader.mark();
        characterReader.rewindToMark();
        assertEquals(3, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello, World!"));
        characterReader.bufferUp();
        assertEquals(false, characterReader.isEmpty());
        characterReader.consumeToEnd();
        assertEquals(true, characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello, World!"));
        characterReader.bufferUp();
        assertEquals('H', characterReader.current());
        characterReader.consume();
        assertEquals('e', characterReader.current());
        characterReader.consume();
        assertEquals('l', characterReader.current());
        characterReader.advance();
        assertEquals('o', characterReader.current());
        characterReader.consume();
        assertEquals('W', characterReader.current());
        characterReader.consume();
        assertEquals('r', characterReader.current());
        characterReader.consume();
        assertEquals('d', characterReader.current());
        characterReader.consume();
        assertEquals('!', characterReader.current());
        characterReader.consume();
        assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello, World!"));
        characterReader.bufferUp();
        characterReader.consume();
        assertEquals('H', characterReader.current());
        characterReader.consume();
        assertEquals('e', characterReader.current());
        characterReader.consume();
        assertEquals('l', characterReader.current());
        characterReader.consume();
        assertEquals('l', characterReader.current());
        characterReader.consume();
        assertEquals('o', characterReader.current());
        characterReader.consume();
        assertEquals('W', characterReader.current());
        characterReader.consume();
        assertEquals('r', characterReader.current());
        characterReader.consume();
        assertEquals('d', characterReader.current());
        characterReader.consume();
        assertEquals('!', characterReader.current());
        characterReader.consume();
        assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testUnconsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello, World!"));
        characterReader.bufferUp();
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('H', characterReader.current());
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('H', characterReader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello, World!"));
        characterReader.bufferUp();
        characterReader.advance();
        assertEquals('e', characterReader.current());
        characterReader.consume();
        assertEquals('l', characterReader.current());
        characterReader.advance();
        assertEquals('o', characterReader.current());
        characterReader.consume();
        assertEquals('W', characterReader.current());
        characterReader.advance();
        assertEquals('r', characterReader.current());
        characterReader.consume();
        assertEquals('d', characterReader.current());
        characterReader.advance();
        assertEquals('!', characterReader.current());
        characterReader.consume();
        assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testMark() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello, World!"));
        characterReader.bufferUp();
        characterReader.mark();
        characterReader.advance();
        characterReader.consume();
        characterReader.rewindToMark();
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello, World!"));
        characterReader.bufferUp();
        assertEquals(-1, characterReader.nextIndexOf('x'));
        characterReader.consume();
        assertEquals(-1, characterReader.nextIndexOf('x'));
        characterReader.consume();
        characterReader.consume();
        assertEquals(3, characterReader.nextIndexOf('l'));
        characterReader.consumeTo('l');
        assertEquals(4, characterReader.nextIndexOf('l'));
        characterReader.consumeToEnd();
        characterReader.consumeTo('l');
        assertEquals(-1, characterReader.nextIndexOf('l'));
    }

    @Test
    public void testNextIndexOfWithCharSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello, World!"));
        characterReader.bufferUp();
        assertEquals(-1, characterReader.nextIndexOf("abcd"));
        characterReader.consume();
        characterReader.consume();
        assertEquals(-1, characterReader.nextIndexOf("abcd"));
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        assertEquals(4, characterReader.nextIndexOf("ll"));
        characterReader.consumeTo('l');
        characterReader.consumeTo('l');
        assertEquals(7, characterReader.nextIndexOf("ll"));
        characterReader.consumeToEnd();
        characterReader.consumeTo('l');
        characterReader.consumeTo('l');
        assertEquals(-1, characterReader.nextIndexOf("ll"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello, World!"));
        characterReader.bufferUp();
        characterReader.consumeTo('l');
        assertEquals("ello", characterReader.toString());
        characterReader.consumeTo('l');
        characterReader.consumeToEnd();
        characterReader.consumeTo('l');
        assertEquals("", characterReader.toString());
    }

    @Test
    public void testConsumeToWithString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello, World!"));
        characterReader.bufferUp();
        characterReader.consumeTo("ll");
        assertEquals("llo", characterReader.toString());
        characterReader.consumeToEnd();
        characterReader.consumeTo("ll");
        assertEquals("", characterReader.toString());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello, World!"));
        characterReader.bufferUp();
        assertEquals("", characterReader.consumeToAny(new char[]{'x'}));
        characterReader.consume();
        assertEquals("", characterReader.consumeToAny(new char[]{'x'}));
        characterReader.consume();
        characterReader.consume();
        assertEquals("l", characterReader.consumeToAny(new char[]{'l'}));
        characterReader.consumeTo('l');
        assertEquals("ll", characterReader.consumeToAny(new char[]{'l'}));
        characterReader.consumeToEnd();
        characterReader.consumeTo('l');
        characterReader.consumeTo('l');
        assertEquals("ll", characterReader.consumeToAny(new char[]{'l'}));
        characterReader.consumeToEnd();
        characterReader.consumeTo('l');
        characterReader.consumeTo('l');
        assertEquals("", characterReader.consumeToAny(new char[]{'l'}));
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello, World!"));
        characterReader.bufferUp();
        assertEquals("", characterReader.consumeToAnySorted(new char[]{'z'}));
        characterReader.consume();
        assertEquals("", characterReader.consumeToAnySorted(new char[]{'z'}));
        characterReader.consume();
        characterReader.consume();
        assertEquals("l", characterReader.consumeToAnySorted(new char[]{'l'}));
        characterReader.consumeTo('l');
        assertEquals("ll", characterReader.consumeToAnySorted(new char[]{'l'}));
        characterReader.consumeToEnd();
        characterReader.consumeTo('l');
        characterReader.consumeTo('l');
        assertEquals("ll", characterReader.consumeToAnySorted(new char[]{'l'}));
        characterReader.consumeToEnd();
        characterReader.consumeTo('l');
        characterReader.consumeTo('l');
        assertEquals("", characterReader.consumeToAnySorted(new char[]{'l'}));
    }

    @Test
    public void testConsumeData() {
        CharacterReader characterReader = new CharacterReader(new StringReader("&<null"));
        characterReader.bufferUp();
        assertEquals("&<null", characterReader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader characterReader = new CharacterReader(new StringReader("\t<title>Hello\nWorld</title>"));
        characterReader.bufferUp();
        assertEquals("\t<title>", characterReader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello, World!"));
        characterReader.bufferUp();
        assertEquals("Hello, World!", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello, World!"));
        characterReader.bufferUp();
        assertEquals("Hello", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello1234World!"));
        characterReader.bufferUp();
        assertEquals("Hello123", characterReader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello, World!"));
        characterReader.bufferUp();
        assertTrue(characterReader.matches('l'));
        assertTrue(characterReader.matches("ll"));
        assertTrue(characterReader.matches('/'));
        assertFalse(characterReader.matches('x'));
        assertFalse(characterReader.matches("xxx"));
        assertFalse(characterReader.matches('\t'));
        assertFalse(characterReader.matches('\n'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello, World!"));
        characterReader.bufferUp();
        assertTrue(characterReader.matchesIgnoreCase("ll"));
        assertTrue(characterReader.matchesIgnoreCase("Ll"));
        assertFalse(characterReader.matchesIgnoreCase("XX"));
        assertFalse(characterReader.matchesIgnoreCase("x"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello, World!"));
        characterReader.bufferUp();
        assertTrue(characterReader.matchesAny('l'));
        assertTrue(characterReader.matchesAny('l'));
        assertTrue(characterReader.matchesAny('/'));
        assertFalse(characterReader.matchesAny('x'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello, World!"));
        characterReader.bufferUp();
        assertTrue(characterReader.matchesAnySorted(new char[]{'l'}));
        assertTrue(characterReader.matchesAnySorted(new char[]{'l'}));
        assertTrue(characterReader.matchesAnySorted('/'));
        assertFalse(characterReader.matchesAnySorted('x'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello, World!"));
        characterReader.bufferUp();
        assertTrue(characterReader.matchesLetter());
        assertTrue(characterReader.matchesLetter());
        assertTrue(characterReader.matchesLetter());
        assertTrue(characterReader.matchesLetter());
        assertTrue(characterReader.matchesLetter());
        assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello1234World!"));
        characterReader.bufferUp();
        assertTrue(characterReader.matchesDigit());
        assertTrue(characterReader.matchesDigit());
        assertTrue(characterReader.matchesDigit());
        assertTrue(characterReader.matchesDigit());
        assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello<title>World!"));
        characterReader.bufferUp();
        assertTrue(characterReader.matchConsume("/title"));
        assertEquals("title", characterReader.toString());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<TITLE>Hello, World!</TITLE>"));
        characterReader.bufferUp();
        assertTrue(characterReader.matchConsumeIgnoreCase("/title"));
        assertEquals("title", characterReader.toString());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html><head><title>Hello, World!</title></head><body><h1>Test</h1></body></html>"));
        characterReader.bufferUp();
        assertTrue(characterReader.containsIgnoreCase("/title"));
        assertTrue(characterReader.containsIgnoreCase("title"));
    }

    @Test
    public void testToString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello, World!"));
        characterReader.bufferUp();
        assertEquals("Hello, World!", characterReader.toString());
    }

}