package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.CharSequenceUtil;
import org.jsoup.helper.Validate;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

public class CharacterReaderTest {

    private static final char EOF = (char) -1;

    @Test
    public void testCharacterReaderConstructorWithInput() {
        Reader reader = new StringReader("Hello World!");
        CharacterReader characterReader = new CharacterReader(reader);
        assertEquals(reader, characterReader.reader);
    }

    @Test
    public void testCharacterReaderConstructorWithInputAndSize() {
        Reader reader = new StringReader("Hello World!");
        CharacterReader characterReader = new CharacterReader(reader, 5);
        assertEquals(reader, characterReader.reader);
        assertEquals(5, characterReader.charBuf.length);
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        String input = "Hello World!";
        CharacterReader characterReader = new CharacterReader(input);
        assertEquals(input, characterReader.toString());
        assertEquals(input.length(), characterReader.pos());
    }

    @Test
    public void testBufferUpMethod() throws IOException {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefg"));
        characterReader.bufferUp();
        assertEquals(7, characterReader.bufLength);
        assertEquals(0, characterReader.bufPos);
        assertEquals(0, characterReader.bufMark);
        assertEquals(0, characterReader.readerPos);
        assertEquals(EOF, characterReader.current());
        assertEquals(EOF, characterReader.consume());
        characterReader.advance();
        assertEquals('a', characterReader.current());
        characterReader.mark();
        assertEquals('a', characterReader.current());
        characterReader.rewindToMark();
        assertEquals('a', characterReader.current());

        characterReader.bufferUp();
        assertEquals(10, characterReader.bufLength);
        assertEquals(1, characterReader.bufPos);
        assertEquals(0, characterReader.bufMark);
        assertEquals(1, characterReader.readerPos);
        assertEquals('b', characterReader.current());
        assertEquals('b', characterReader.consume());
        assertEquals(1, characterReader.pos());
    }

    @Test(expected = UncheckedIOException.class)
    public void testBufferUpMethodWithIOException() throws IOException {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefg")) {
            @Override
            public void bufferUp() throws IOException {
                throw new IOException();
            }
        };
        characterReader.bufferUp();
    }

    @Test
    public void testIsEmptyMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader(""));
        assertTrue(characterReader.isEmpty());
        characterReader = new CharacterReader(new StringReader("abcdefg"));
        assertFalse(characterReader.isEmpty());
        characterReader.bufferUp();
        assertTrue(characterReader.isEmpty());
        characterReader.advance();
        assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testCurrentMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefg"));
        assertEquals(EOF, characterReader.current());
        characterReader.bufferUp();
        assertEquals('a', characterReader.current());
        characterReader.consume();
        assertEquals('a', characterReader.current());
        characterReader.consume();
        assertEquals('b', characterReader.current());
        characterReader.advance();
        assertEquals('c', characterReader.current());
        characterReader.rewindToMark();
        assertEquals('c', characterReader.current());
    }

    @Test
    public void testConsumeMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefg"));
        assertEquals(EOF, characterReader.consume());
        characterReader.bufferUp();
        assertEquals('a', characterReader.consume());
        assertEquals('a', characterReader.current());
        characterReader.consume();
        assertEquals('b', characterReader.consume());
        assertEquals('b', characterReader.current());
        characterReader.consume();
        assertEquals('c', characterReader.consume());
        assertEquals('c', characterReader.current());
        characterReader.advance();
        assertEquals('d', characterReader.consume());
        assertEquals(EOF, characterReader.consume());
    }

    @Test
    public void testUnconsumeMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abc"));
        characterReader.bufferUp();
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('a', characterReader.current());
        characterReader.consume();
        assertEquals('b', characterReader.current());
    }

    @Test
    public void testAdvanceMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abc"));
        assertEquals(EOF, characterReader.current());
        characterReader.advance();
        assertEquals('a', characterReader.current());
        characterReader.advance();
        assertEquals('b', characterReader.current());
        characterReader.advance();
        assertEquals(EOF, characterReader.current());
    }

    @Test
    public void testMarkMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abc"));
        characterReader.mark();
        characterReader.consume();
        characterReader.rewindToMark();
        assertEquals('a', characterReader.current());
    }

    @Test
    public void testNextIndexOfMethodWithSingleChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcd"));
        assertEquals(1, characterReader.nextIndexOf('b'));
        assertEquals(2, characterReader.nextIndexOf('c'));
        assertEquals(-1, characterReader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOfMethodWithCharSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdabcd"));
        assertEquals(1, characterReader.nextIndexOf("ab"));
        assertEquals(5, characterReader.nextIndexOf("cd"));
        assertEquals(-1, characterReader.nextIndexOf("xy"));
    }

    @Test
    public void testConsumeToMethodWithSingleChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcd"));
        assertEquals("", characterReader.consumeTo(EOF));
        characterReader.bufferUp();
        assertEquals("a", characterReader.consumeTo(EOF));
        assertEquals("ab", characterReader.consumeTo('b'));
        assertEquals("abcd", characterReader.consumeTo('d'));
    }

    @Test
    public void testConsumeToMethodWithCharSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdabcd"));
        assertEquals("", characterReader.consumeTo(EOF));
        characterReader.bufferUp();
        assertEquals("ab", characterReader.consumeTo("ab"));
        assertEquals("abcd", characterReader.consumeTo("cd"));
        assertEquals("abcdabcd", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeToAnyMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefg"));
        assertEquals("", characterReader.consumeToAny(EOF));
        assertEquals("a", characterReader.consumeToAny(new char[]{EOF, 'a'}));
        assertEquals("ab", characterReader.consumeToAny(new char[]{'a', 'b'}));
        assertEquals("abdefg", characterReader.consumeToAny(new char[]{'e', 'g'}));
    }

    @Test
    public void testConsumeToAnySortedMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefg"));
        assertEquals("", characterReader.consumeToAnySorted(new char[]{EOF}));
        assertEquals("a", characterReader.consumeToAnySorted(new char[]{EOF, 'a'}));
        assertEquals("ab", characterReader.consumeToAnySorted(new char[]{'a', 'b'}));
        assertEquals("abefg", characterReader.consumeToAnySorted(new char[]{'e', 'g'}));
    }

    @Test
    public void testConsumeDataMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("&<foo"));
        assertEquals("&<", characterReader.consumeData());
    }

    @Test
    public void testConsumeTagNameMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<tag name=\"foo\"/>"));
        assertEquals("<tag", characterReader.consumeTagName());
    }

    @Test
    public void testConsumeToEndMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefg"));
        assertEquals("", characterReader.consumeToEnd());
        characterReader.bufferUp();
        assertEquals("abcdefg", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequenceMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefg"));
        assertEquals("", characterReader.consumeLetterSequence());
        characterReader.bufferUp();
        assertEquals("abc", characterReader.consumeLetterSequence());
        characterReader.bufferUp();
        assertEquals("abcdefg", characterReader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequenceMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abc123def456"));
        assertEquals("", characterReader.consumeLetterThenDigitSequence());
        characterReader.bufferUp();
        assertEquals("a1", characterReader.consumeLetterThenDigitSequence());
        characterReader.bufferUp();
        assertEquals("abc123", characterReader.consumeLetterThenDigitSequence());
        characterReader.bufferUp();
        assertEquals("abc123def456", characterReader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequenceMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abc123def456"));
        assertEquals("", characterReader.consumeHexSequence());
        characterReader.bufferUp();
        assertEquals("12", characterReader.consumeHexSequence());
        characterReader.bufferUp();
        assertEquals("123def456", characterReader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequenceMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("1234567890"));
        assertEquals("", characterReader.consumeDigitSequence());
        characterReader.bufferUp();
        assertEquals("123", characterReader.consumeDigitSequence());
        characterReader.bufferUp();
        assertEquals("1234567890", characterReader.consumeDigitSequence());
    }

    @Test
    public void testMatchesMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefg"));
        assertTrue(characterReader.matches('a'));
        assertFalse(characterReader.matches('z'));
        characterReader.bufferUp();
        assertTrue(characterReader.matches('a'));
        assertFalse(characterReader.matches('z'));
        assertTrue(characterReader.matches("ab"));
        assertFalse(characterReader.matches("xy"));
    }

    @Test
    public void testMatchesMethodWithIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("ABCDEFG"));
        assertTrue(characterReader.matchesIgnoreCase("ab"));
        assertTrue(characterReader.matchesIgnoreCase("Ab"));
        assertFalse(characterReader.matchesIgnoreCase("XY"));
    }

    @Test
    public void testMatchesAnyMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefg"));
        assertTrue(characterReader.matchesAny(new char[]{'a', 'b'}));
        assertTrue(characterReader.matchesAny(new char[]{'b', 'a'}));
        assertFalse(characterReader.matchesAny(new char[]{'z', 'x'}));
    }

    @Test
    public void testMatchesAnySortedMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefg"));
        assertTrue(characterReader.matchesAnySorted(new char[]{'a', 'b'}));
        assertTrue(characterReader.matchesAnySorted(new char[]{'b', 'a'}));
        assertFalse(characterReader.matchesAnySorted(new char[]{'x', 'z'}));
    }

    @Test
    public void testMatchesLetterMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefg"));
        assertTrue(characterReader.matchesLetter());
        characterReader.bufferUp();
        assertTrue(characterReader.matchesLetter());
        assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigitMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("1234567890"));
        assertFalse(characterReader.matchesDigit());
        characterReader.bufferUp();
        assertFalse(characterReader.matchesDigit());
        assertTrue(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsumeMethodWithString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefg"));
        assertTrue(characterReader.matchConsume("ab"));
        characterReader.bufferUp();
        assertTrue(characterReader.matchConsume("ab"));
        assertFalse(characterReader.matchConsume("xy"));
    }

    @Test
    public void testMatchConsumeMethodWithStringIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("ABCDEFG"));
        assertTrue(characterReader.matchConsumeIgnoreCase("ab"));
        assertTrue(characterReader.matchConsumeIgnoreCase("Ab"));
        assertFalse(characterReader.matchConsumeIgnoreCase("XY"));
    }

    @Test
    public void testContainsIgnoreCaseMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>"));
        assertTrue(characterReader.containsIgnoreCase("/title"));
        assertTrue(characterReader.containsIgnoreCase("/TITLE"));
        assertFalse(characterReader.containsIgnoreCase("/body"));
    }

    @Test
    public void testToStringMethod() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefg"));
        assertEquals("abcdefg", characterReader.toString());
    }
}