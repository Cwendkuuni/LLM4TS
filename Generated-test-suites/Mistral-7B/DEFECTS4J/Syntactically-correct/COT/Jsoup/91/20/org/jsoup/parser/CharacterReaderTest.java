package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithReader() {
        Reader reader = new StringReader("Hello World");
        CharacterReader characterReader = new CharacterReader(reader);
        assertEquals("Hello World", characterReader.toString());
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertEquals("Hello World", characterReader.toString());
    }

    @Test
    public void testBufferUpMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.bufferUp();
        assertEquals(5, characterReader.pos());
    }

    @Test(expected = UncheckedIOException.class)
    public void testBufferUpMethodWithIOException() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World")) {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new IOException();
            }
        };
        characterReader.bufferUp();
    }

    @Test
    public void testPosMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertEquals(0, characterReader.pos());
        characterReader.advance();
        characterReader.advance();
        assertEquals(2, characterReader.pos());
    }

    @Test
    public void testIsEmptyMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertFalse(characterReader.isEmpty());
        characterReader.consume();
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCurrentMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertEquals('H', characterReader.current());
        characterReader.consume();
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testConsumeMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
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
        assertEquals(' ', characterReader.current());
        characterReader.consume();
        assertEquals('W', characterReader.current());
        characterReader.consume();
        assertEquals('r', characterReader.current());
        characterReader.consume();
        assertEquals('d', characterReader.current());
        characterReader.consume();
        assertEquals('\n', characterReader.current());
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testUnconsumeMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.consume();
        assertEquals('e', characterReader.current());
        characterReader.unconsume();
        assertEquals('H', characterReader.current());
    }

    @Test
    public void testAdvanceMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.advance();
        characterReader.advance();
        assertEquals(2, characterReader.pos());
    }

    @Test
    public void testMarkMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.mark();
        characterReader.advance();
        characterReader.advance();
        characterReader.rewindToMark();
        assertEquals(0, characterReader.pos());
    }

    @Test(expected = UncheckedIOException.class)
    public void testRewindToMarkMethodWithMarkInvalidException() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.mark();
        characterReader.consume();
        characterReader.rewindToMark();
    }

    @Test
    public void testNextIndexOfMethodWithChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertEquals(6, characterReader.nextIndexOf(' '));
        assertEquals(-1, characterReader.nextIndexOf('X'));
    }

    @Test
    public void testNextIndexOfMethodWithCharSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertEquals(4, characterReader.nextIndexOf("ll"));
        assertEquals(-1, characterReader.nextIndexOf("XX"));
    }

    @Test
    public void testConsumeToMethodWithChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String consumed = characterReader.consumeTo('l');
        assertEquals("He", consumed);
    }

    @Test
    public void testConsumeToMethodWithString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String consumed = characterReader.consumeTo("ll");
        assertEquals("Hello", consumed);
    }

    @Test
    public void testConsumeToAnyMethodWithChars() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String consumed = characterReader.consumeToAny(new char[]{'l', 'o'});
        assertEquals("Hello", consumed);
    }

    @Test
    public void testConsumeToAnySortedMethodWithChars() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String consumed = characterReader.consumeToAnySorted(new char[]{'l', 'o'});
        assertEquals("Hello", consumed);
    }

    @Test
    public void testConsumeDataMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("&<null"));
        String consumed = characterReader.consumeData();
        assertEquals("&<null", consumed);
    }

    @Test
    public void testConsumeTagNameMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("\t<title>Test\r\n<style>"));
        String consumed = characterReader.consumeTagName();
        assertEquals("<title>", consumed);
    }

    @Test
    public void testConsumeToEndMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String consumed = characterReader.consumeToEnd();
        assertEquals("Hello World", consumed);
    }

    @Test
    public void testConsumeLetterSequenceMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("HeLLo"));
        String consumed = characterReader.consumeLetterSequence();
        assertEquals("HeLLo", consumed);
    }

    @Test
    public void testConsumeLetterThenDigitSequenceMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("HeLLo1234"));
        String consumed = characterReader.consumeLetterThenDigitSequence();
        assertEquals("HeLLo", consumed);
    }

    @Test
    public void testMatchesMethodWithChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(characterReader.matches('l'));
        assertFalse(characterReader.matches('x'));
    }

    @Test
    public void testMatchesMethodWithString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(characterReader.matches("ll"));
        assertFalse(characterReader.matches("XX"));
    }

    @Test
    public void testMatchesIgnoreCaseMethodWithString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(characterReader.matchesIgnoreCase("ll"));
        assertTrue(characterReader.matchesIgnoreCase("LL"));
        assertFalse(characterReader.matchesIgnoreCase("XX"));
    }

    @Test
    public void testMatchesAnyMethodWithChars() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(characterReader.matchesAny(new char[]{'l', 'o'}));
        assertFalse(characterReader.matchesAny(new char[]{'x', 'o'}));
    }

    @Test
    public void testMatchesAnySortedMethodWithChars() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(characterReader.matchesAnySorted(new char[]{'l', 'o'}));
        assertFalse(characterReader.matchesAnySorted(new char[]{'o', 'l'}));
    }

    @Test
    public void testMatchesLetterMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(characterReader.matchesLetter());
        assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchesDigitMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertFalse(characterReader.matchesLetter());
        assertFalse(characterReader.matchesDigit());
        characterReader.consume(); // consume 'H'
        assertTrue(characterReader.matchesDigit()); // consume '0'
        assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchConsumeMethodWithString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(characterReader.matchConsume("ll"));
        assertFalse(characterReader.matchConsume("XX"));
    }

    @Test
    public void testMatchConsumeIgnoreCaseMethodWithString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(characterReader.matchConsumeIgnoreCase("ll"));
        assertTrue(characterReader.matchConsumeIgnoreCase("LL"));
        assertFalse(characterReader.matchConsumeIgnoreCase("XX"));
    }

    @Test
    public void testContainsIgnoreCaseMethodWithString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html><head><title>Test</title></head><body>"));
        assertTrue(characterReader.containsIgnoreCase("title"));
        assertTrue(characterReader.containsIgnoreCase("TITLE"));
        assertFalse(characterReader.containsIgnoreCase("XXX"));
    }
}