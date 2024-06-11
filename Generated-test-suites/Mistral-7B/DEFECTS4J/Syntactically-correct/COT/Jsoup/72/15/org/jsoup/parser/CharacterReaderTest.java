package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Reader;

import static org.jsoup.parser.CharacterReader.EOF;

/**
 * Test class for CharacterReader.
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderWithInput() {
        String input = "Hello World!";
        CharacterReader reader = new CharacterReader(new ByteArrayInputStream(input.getBytes()));
        assertEquals(0, reader.pos());
        assertEquals(false, reader.isEmpty());
        assertEquals('H', reader.current());
        reader.consume();
        assertEquals('H', reader.current());
        assertEquals('e', reader.consume());
        assertEquals(2, reader.pos());
        assertEquals(3, reader.nextIndexOf('l'));
        reader.rewindToMark();
        assertEquals(2, reader.pos());
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals(3, reader.pos());
        reader.consumeTo('l');
        assertEquals("llo", reader.toString());
        reader.consumeToEnd();
        assertEquals("World!", reader.toString());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOExceptionWhenReading() {
        Reader reader = new ByteArrayInputStream(new byte[0]) {
            @Override
            public int read() throws IOException {
                throw new IOException();
            }
        };
        CharacterReader characterReader = new CharacterReader(reader);
        characterReader.bufferUp();
        characterReader.consume();
    }

    @Test
    public void testCharacterReaderWithReader() {
        String input = "Hello World!";
        Reader reader = new ByteArrayInputStream(input.getBytes());
        CharacterReader characterReader = new CharacterReader(reader);
        assertEquals(0, characterReader.pos());
        assertEquals(false, characterReader.isEmpty());
        assertEquals('H', characterReader.current());
        characterReader.consume();
        assertEquals('H', characterReader.current());
        characterReader.consume();
        assertEquals('e', characterReader.current());
        characterReader.advance();
        assertEquals('e', characterReader.current());
        characterReader.mark();
        characterReader.rewindToMark();
        assertEquals('e', characterReader.current());
        characterReader.consumeTo('l');
        assertEquals("llo", characterReader.toString());
        characterReader.consumeToEnd();
        assertEquals("World!", characterReader.toString());
    }

    @Test
    public void testCharacterReaderWithString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        assertEquals(0, characterReader.pos());
        assertEquals(false, characterReader.isEmpty());
        assertEquals('H', characterReader.current());
        characterReader.consume();
        assertEquals('H', characterReader.current());
        characterReader.consume();
        assertEquals('e', characterReader.current());
        characterReader.advance();
        assertEquals('e', characterReader.current());
        characterReader.mark();
        characterReader.rewindToMark();
        assertEquals('e', characterReader.current());
        characterReader.consumeTo('l');
        assertEquals("llo", characterReader.toString());
        characterReader.consumeToEnd();
        assertEquals("World!", characterReader.toString());
    }

    @Test
    public void testStringCache() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World! Hello World!"));
        assertEquals("Hello World!", characterReader.consumeToEnd());
        assertEquals("Hello World!", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeToEndWithEmptyString() {
        CharacterReader characterReader = new CharacterReader(new StringReader(""));
        assertEquals("", characterReader.consumeToEnd());
    }

    @Test
    public void testEmptyReader() {
        CharacterReader characterReader = new CharacterReader(new StringReader(""));
        assertEquals(0, characterReader.pos());
        assertEquals(true, characterReader.isEmpty());
    }

    @Test
    public void testConsumeToEndAfterEndOfReader() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        characterReader.consumeToEnd();
        characterReader.consumeToEnd();
        assertEquals(EOF, characterReader.current());
    }

    @Test
    public void testConsumeToEndWithNullCharacter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        characterReader.consumeTo(Character.MIN_VALUE);
        assertEquals(EOF, characterReader.current());
    }

    @Test
    public void testConsumeToSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdabcdefg"));
        assertEquals("abcd", characterReader.consumeTo("cd"));
        assertEquals("abcd", characterReader.toString());
        assertEquals("efg", characterReader.consumeToEnd());
    }

    @Test
    public void testConsumeToSequenceWithEmptySequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcd"));
        assertEquals("", characterReader.consumeTo(""));
    }

    @Test
    public void testConsumeToSequenceWithLastCharacter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        assertEquals("World!", characterReader.consumeToEnd());
        assertEquals(EOF, characterReader.current());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdabcdabcdabcd"));
        assertEquals("abcd", characterReader.consumeToAny(new char[]{'a', 'b'}));
        assertEquals("cdabcdabcdabcd", characterReader.toString());
    }

    @Test
    public void testConsumeToAnyWithEmptySequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcd"));
        assertEquals("", characterReader.consumeToAny(new char[0]));
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdabcdabcdabcd"));
        assertEquals("abcd", characterReader.consumeToAnySorted(new char[]{'b', 'd', 'a'}));
        assertEquals("cdabcdabcdabcd", characterReader.toString());
    }

    @Test
    public void testConsumeToAnySortedWithEmptySequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcd"));
        assertEquals("", characterReader.consumeToAnySorted(new char[0]));
    }

    @Test
    public void testConsumeToAnyWithLastCharacter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World!"));
        assertEquals("World!", characterReader.consumeToAny(new char[]{'!'}));
        assertEquals(EOF, characterReader.current());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        assertEquals("abcdef", characterReader.consumeLetterSequence());
        assertEquals("ghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", characterReader.toString());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abc123def456ghi789"));
        assertEquals("abc123", characterReader.consumeLetterThenDigitSequence());
        assertEquals("def456ghi789", characterReader.toString());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("0123456789ABCDEFabcdef"));
        assertEquals("0123456", characterReader.consumeHexSequence());
        assertEquals("789ABCDEFabcdef", characterReader.toString());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("01234567890123456789012345678901234567890123456789"));
        assertEquals("01234567890123456789012345678901234567890123456789", characterReader.consumeDigitSequence());
        assertEquals("", characterReader.toString());
    }

    @Test
    public void testMatches() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        assertTrue(characterReader.matches('a'));
        assertTrue(characterReader.matches('z'));
        assertFalse(characterReader.matches('0'));
        assertFalse(characterReader.matches('!'));
    }

    @Test
    public void testMatchesSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        assertTrue(characterReader.matches("abc"));
        assertFalse(characterReader.matches("xyz"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        assertTrue(characterReader.matchesIgnoreCase("abC"));
        assertFalse(characterReader.matchesIgnoreCase("xyz"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        assertTrue(characterReader.matchesAny(new char[]{'a', 'z'}));
        assertFalse(characterReader.matchesAny(new char[]{'}'}));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        assertTrue(characterReader.matchesAnySorted(new char[]{'z', 'a'}));
        assertFalse(characterReader.matchesAnySorted(new char[]{'!'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        assertTrue(characterReader.matchesLetter());
        assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader characterReader = new CharacterReader(new StringReader("01234567890123456789012345678901234567890123456789"));
        assertTrue(characterReader.matchesDigit());
        assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsumeSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        assertTrue(characterReader.matchConsume("abc"));
        assertEquals(3, characterReader.pos());
        assertFalse(characterReader.matchConsume("xyz"));
        assertEquals(10, characterReader.pos());
    }

    @Test
    public void testMatchConsumeSequenceIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        assertTrue(characterReader.matchConsumeIgnoreCase("abC"));
        assertEquals(3, characterReader.pos());
        assertFalse(characterReader.matchConsumeIgnoreCase("xyz"));
        assertEquals(10, characterReader.pos());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>"));
        assertTrue(characterReader.containsIgnoreCase("/title"));
        assertTrue(characterReader.containsIgnoreCase("HEAD"));
        assertFalse(characterReader.containsIgnoreCase("/footer"));
    }
}