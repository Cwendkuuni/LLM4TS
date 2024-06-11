package org.jsoup.parser;

import static org.junit.Assert.*;

import java.io.IOException;
import org.jsoup.UncheckedIOException;
import org.junit.Test;

/**
 * Test class for CharacterReader
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructor() {
        // Test CharacterReader constructor with InputStream
        Reader reader = new StringReader("Test String");
        CharacterReader characterReader = new CharacterReader(reader);
        assertEquals("Test String", characterReader.toString());

        // Test CharacterReader constructor with String
        characterReader = new CharacterReader("Test String");
        assertEquals("Test String", characterReader.toString());

        // Test CharacterReader constructor with null input
        try {
            characterReader = new CharacterReader(null);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testBufferUp() {
        Reader reader = new StringReader("Test String");
        CharacterReader characterReader = new CharacterReader(reader);

        // Test bufferUp with empty buffer
        characterReader.bufferUp();
        assertEquals(0, characterReader.bufPos);
        assertEquals(0, characterReader.readerPos);

        // Test bufferUp with filled buffer
        characterReader.advance();
        characterReader.advance();
        characterReader.bufferUp();
        assertEquals(2, characterReader.bufPos);
        assertEquals(2, characterReader.readerPos);
    }

    @Test
    public void testIsEmpty() {
        Reader reader = new StringReader("Test String");
        CharacterReader characterReader = new CharacterReader(reader);

        // Test isEmpty with empty buffer
        assertTrue(characterReader.isEmpty());

        // Test isEmpty with filled buffer
        characterReader.advance();
        assertFalse(characterReader.isEmpty());

        // Test isEmptyNoBufferUp with empty buffer
        assertTrue(characterReader.isEmptyNoBufferUp());

        // Test isEmptyNoBufferUp with filled buffer
        characterReader.advance();
        assertFalse(characterReader.isEmptyNoBufferUp());
    }

    @Test
    public void testCurrent() {
        Reader reader = new StringReader("Test String");
        CharacterReader characterReader = new CharacterReader(reader);

        // Test current with empty buffer
        assertEquals(CharacterReader.EOF, characterReader.current());

        // Test current with filled buffer
        characterReader.advance();
        assertEquals('T', characterReader.current());
    }

    @Test
    public void testConsume() {
        Reader reader = new StringReader("Test String");
        CharacterReader characterReader = new CharacterReader(reader);

        // Test consume with empty buffer
        assertEquals(CharacterReader.EOF, characterReader.consume());

        // Test consume with filled buffer
        characterReader.advance();
        assertEquals('T', characterReader.consume());

        // Test consume multiple times with filled buffer
        assertEquals('s', characterReader.consume());
        assertEquals(' ', characterReader.consume());
        assertEquals('T', characterReader.consume());
        assertEquals(' ', characterReader.consume());
        assertEquals('String', characterReader.consumeToEnd().trim());
    }

    @Test
    public void testUnconsume() {
        Reader reader = new StringReader("Test String");
        CharacterReader characterReader = new CharacterReader(reader);

        // Test unconsume with empty buffer
        characterReader.unconsume();
        assertEquals(CharacterReader.EOF, characterReader.current());

        // Test unconsume with filled buffer
        characterReader.advance();
        characterReader.consume();
        assertEquals('T', characterReader.current());
        characterReader.unconsume();
        assertEquals('T', characterReader.current());
    }

    @Test
    public void testAdvance() {
        Reader reader = new StringReader("Test String");
        CharacterReader characterReader = new CharacterReader(reader);

        // Test advance with empty buffer
        characterReader.advance();
        assertEquals(CharacterReader.EOF, characterReader.current());

        // Test advance with filled buffer
        characterReader.advance();
        assertEquals('T', characterReader.current());
    }

    @Test
    public void testMark() {
        Reader reader = new StringReader("Test String");
        CharacterReader characterReader = new CharacterReader(reader);

        // Test mark with empty buffer
        characterReader.mark();
        assertEquals(CharacterReader.EOF, characterReader.current());

        // Test mark with filled buffer
        characterReader.advance();
        characterReader.mark();
        assertEquals('T', characterReader.current());
    }

    @Test
    public void testRewindToMark() {
        Reader reader = new StringReader("Test String");
        CharacterReader characterReader = new CharacterReader(reader);

        // Test rewindToMark with empty buffer
        characterReader.rewindToMark();
        assertEquals(CharacterReader.EOF, characterReader.current());

        // Test rewindToMark with filled buffer
        characterReader.advance();
        characterReader.mark();
        characterReader.rewindToMark();
        assertEquals('T', characterReader.current());
    }

    @Test
    public void testNextIndexOf() {
        Reader reader = new StringReader("Test String");
        CharacterReader characterReader = new CharacterReader(reader);

        // Test nextIndexOf with char
        assertEquals(1, characterReader.nextIndexOf('T'));

        // Test nextIndexOf with empty buffer
        assertEquals(-1, characterReader.nextIndexOf(' '));

        // Test nextIndexOf with filled buffer
        characterReader.advance();
        assertEquals(1, characterReader.nextIndexOf('T'));

        // Test nextIndexOf with multiple 'T' in buffer
        characterReader.consume();
        assertEquals(2, characterReader.nextIndexOf('T'));

        // Test nextIndexOf with char sequence
        assertEquals(0, characterReader.nextIndexOf("Te"));

        // Test nextIndexOf with empty buffer and char sequence
        assertEquals(-1, characterReader.nextIndexOf("Te"));

        // Test nextIndexOf with filled buffer and char sequence
        characterReader.advance();
        characterReader.consume();
        assertEquals(0, characterReader.nextIndexOf("Te"));
    }

    @Test
    public void testConsumeTo() {
        Reader reader = new StringReader("Test String");
        CharacterReader characterReader = new CharacterReader(reader);

        // Test consumeTo with char
        String result = characterReader.consumeTo('T');
        assertEquals("", result);
        assertEquals('T', characterReader.current());

        // Test consumeTo with empty buffer
        result = characterReader.consumeToAny(Character.toCharArray(' '));
        assertEquals("", result);
        assertEquals(CharacterReader.EOF, characterReader.current());

        // Test consumeTo with filled buffer
        characterReader.advance();
        result = characterReader.consumeTo('T');
        assertEquals("Test", result);
        assertEquals('s', characterReader.current());

        // Test consumeTo with char sequence
        result = characterReader.consumeTo("Te");
        assertEquals("Test", result);
        assertEquals(' ', characterReader.current());
    }

    @Test
    public void testConsumeToEnd() {
        Reader reader = new StringReader("Test String");
        CharacterReader characterReader = new CharacterReader(reader);

        // Test consumeToEnd with empty buffer
        String result = characterReader.consumeToEnd();
        assertEquals("", result);
        assertEquals(CharacterReader.EOF, characterReader.current());

        // Test consumeToEnd with filled buffer
        characterReader.advance();
        result = characterReader.consumeToEnd();
        assertEquals("String", result);
        assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testConsumeLetterSequence() {
        Reader reader = new StringReader("Test String");
        CharacterReader characterReader = new CharacterReader(reader);

        // Test consumeLetterSequence with empty buffer
        String result = characterReader.consumeLetterSequence();
        assertEquals("", result);
        assertEquals(CharacterReader.EOF, characterReader.current());

        // Test consumeLetterSequence with filled buffer
        characterReader.advance();
        characterReader.consume(); // consume 'T'
        result = characterReader.consumeLetterSequence();
        assertEquals("est", result);
        assertEquals('s', characterReader.current());

        // Test consumeLetterSequence with multiple letters in buffer
        characterReader.consume(); // consume 'e'
        characterReader.consume(); // consume 's'
        characterReader.consume(); // consume 't'
        result = characterReader.consumeLetterSequence();
        assertEquals("", result);
        assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        Reader reader = new StringReader("Test123 String");
        CharacterReader characterReader = new CharacterReader(reader);

        // Test consumeLetterThenDigitSequence with empty buffer
        String result = characterReader.consumeLetterThenDigitSequence();
        assertEquals("", result);
        assertEquals(CharacterReader.EOF, characterReader.current());

        // Test consumeLetterThenDigitSequence with filled buffer
        characterReader.advance();
        characterReader.consume(); // consume 'T'
        result = characterReader.consumeLetterThenDigitSequence();
        assertEquals("123", result);
        assertEquals(CharacterReader.EOF, characterReader.current());

        // Test consumeLetterThenDigitSequence with multiple letters and digits in buffer
        characterReader.consume(); // consume 'e'
        characterReader.consume(); // consume 's'
        characterReader.consume(); // consume 't'
        result = characterReader.consumeLetterThenDigitSequence();
        assertEquals("123", result);
        assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testConsumeHexSequence() {
        Reader reader = new StringReader("Test#%$&/0x1a String");
        CharacterReader characterReader = new CharacterReader(reader);

        // Test consumeHexSequence with empty buffer
        String result = characterReader.consumeHexSequence();
        assertEquals("", result);
        assertEquals(CharacterReader.EOF, characterReader.current());

        // Test consumeHexSequence with filled buffer
        characterReader.advance();
        result = characterReader.consumeHexSequence();
        assertEquals("%$&", result);
        assertEquals('/', characterReader.current());

        // Test consumeHexSequence with multiple hex sequences in buffer
        characterReader.consume(); // consume '#'
        result = characterReader.consumeHexSequence();
        assertEquals("1a", result);
        assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testConsumeDigitSequence() {
        Reader reader = new StringReader("Test123 String");
        CharacterReader characterReader = new CharacterReader(reader);

        // Test consumeDigitSequence with empty buffer
        String result = characterReader.consumeDigitSequence();
        assertEquals("", result);
        assertEquals(CharacterReader.EOF, characterReader.current());

        // Test consumeDigitSequence with filled buffer
        characterReader.advance();
        result = characterReader.consumeDigitSequence();
        assertEquals("123", result);
        assertEquals(CharacterReader.EOF, characterReader.current());

        // Test consumeDigitSequence with multiple digits in buffer
        characterReader.consume(); // consume '1'
        characterReader.consume(); // consume '2'
        characterReader.consume(); // consume '3'
        result = characterReader.consumeDigitSequence();
        assertEquals("", result);
        assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testMatches() {
        Reader reader = new StringReader("Test String");
        CharacterReader characterReader = new CharacterReader(reader);

        // Test matches with char
        assertTrue(characterReader.matches('T'));
        assertFalse(characterReader.matches('S'));

        // Test matches with empty buffer
        assertFalse(characterReader.matches('S'));

        // Test matches with filled buffer
        characterReader.advance();
        assertTrue(characterReader.matches('T'));
        assertFalse(characterReader.matches('S'));

        // Test matches with multiple chars in buffer
        characterReader.consume();
        assertTrue(characterReader.matches('e'));
        assertFalse(characterReader.matches('S'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        Reader reader = new StringReader("Test String");
        CharacterReader characterReader = new CharacterReader(reader);

        // Test matchesIgnoreCase with char
        assertTrue(characterReader.matchesIgnoreCase('T'));
        assertTrue(characterReader.matchesIgnoreCase('t'));
        assertFalse(characterReader.matchesIgnoreCase('S'));

        // Test matchesIgnoreCase with empty buffer
        assertFalse(characterReader.matchesIgnoreCase('S'));

        // Test matchesIgnoreCase with filled buffer
        characterReader.advance();
        assertTrue(characterReader.matchesIgnoreCase('T'));
        assertTrue(characterReader.matchesIgnoreCase('t'));
        assertFalse(characterReader.matchesIgnoreCase('S'));
    }

    @Test
    public void testMatchesAny() {
        Reader reader = new StringReader("Test String");
        CharacterReader characterReader = new CharacterReader(reader);

        // Test matchesAny with char
        characterReader.advance();
        assertTrue(characterReader.matchesAny(Character.toCharArray('T')));
        assertFalse(characterReader.matchesAny(Character.toCharArray('S')));

        // Test matchesAny with empty buffer
        assertFalse(characterReader.matchesAny(Character.toCharArray('S')));

        // Test matchesAny with filled buffer
        characterReader.consume();
        assertTrue(characterReader.matchesAny(Character.toCharArray('e')));
        assertFalse(characterReader.matchesAny(Character.toCharArray('S')));
    }

    @Test
    public void testMatchesAnySorted() {
        Reader reader = new StringReader("Test String");
        CharacterReader characterReader = new CharacterReader(reader);

        // Test matchesAnySorted with char array
        characterReader.advance();
        assertTrue(characterReader.matchesAnySorted(Character.toCharArray('T', 't')));
        assertFalse(characterReader.matchesAnySorted(Character.toCharArray('S')));

        // Test matchesAnySorted with empty buffer
        assertFalse(characterReader.matchesAnySorted(Character.toCharArray('S')));

        // Test matchesAnySorted with filled buffer
        characterReader.consume();
        assertTrue(characterReader.matchesAnySorted(Character.toCharArray('e')));
        assertFalse(characterReader.matchesAnySorted(Character.toCharArray('S')));
    }

    @Test
    public void testContainsIgnoreCase() {
        Reader reader = new StringReader("Test String");
        CharacterReader characterReader = new CharacterReader(reader);

        // Test containsIgnoreCase with empty buffer
        assertFalse(characterReader.containsIgnoreCase("TeST"));

        // Test containsIgnoreCase with filled buffer
        characterReader.advance();
        assertTrue(characterReader.containsIgnoreCase("TeST"));
    }

    @Test
    public void testToString() {
        Reader reader = new StringReader("Test String");
        CharacterReader characterReader = new CharacterReader(reader);

        // Test toString with empty buffer
        assertEquals("", characterReader.toString());

        // Test toString with filled buffer
        characterReader.advance();
        assertEquals("Test", characterReader.toString());
    }

    @Test
    public void testCacheString() {
        Reader reader = new StringReader("Test String");
        CharacterReader characterReader = new CharacterReader(reader);

        // Test cacheString with empty buffer
        assertEquals("", characterReader.cacheString(characterReader.charBuf, characterReader.stringCache, 0, 0));

        // Test cacheString with filled buffer
        characterReader.advance();
        characterReader.consume();
        assertEquals("est", characterReader.cacheString(characterReader.charBuf, characterReader.stringCache, 0, 3));

        // Test cacheString with same string in buffer
        characterReader.consume();
        assertEquals("est", characterReader.cacheString(characterReader.charBuf, characterReader.stringCache, 0, 3));

        // Test cacheString with different string in buffer
        characterReader.consume();
        assertEquals("e", characterReader.cacheString(characterReader.charBuf, characterReader.stringCache, 0, 1));
    }
}