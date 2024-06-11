package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.UncheckedIOException;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

/**
 * Test class for CharacterReader.
 */
public class CharacterReaderTest {

    private final static int MAX_BUFFER_LEN = 1024 * 32;
    private final static int READ_AHEAD_LIMIT = (int) (MAX_BUFFER_LEN * 0.75);

    @Test
    public void testCharacterReaderConstructorWithReader() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertEquals("Hello World", characterReader.toString());
        assertEquals(11, characterReader.pos());
        assertEquals(false, characterReader.isEmpty());
        assertEquals('H', characterReader.current());
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        CharacterReader characterReader = new CharacterReader("Hello World");
        assertEquals("Hello World", characterReader.toString());
        assertEquals(11, characterReader.pos());
        assertEquals(false, characterReader.isEmpty());
        assertEquals('H', characterReader.current());
    }

    @Test
    public void testBufferUp() throws IOException {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.bufferUp();
        assertEquals(11, characterReader.pos());
        assertEquals(false, characterReader.isEmpty());
        assertEquals('H', characterReader.current());

        characterReader.bufferUp();
        assertEquals(19, characterReader.pos());
        assertEquals(false, characterReader.isEmpty());
        assertEquals('W', characterReader.current());

        characterReader.bufferUp();
        assertEquals(27, characterReader.pos());
        assertEquals(false, characterReader.isEmpty());
        assertEquals('d', characterReader.current());

        characterReader.bufferUp();
        assertEquals(MAX_BUFFER_LEN, characterReader.pos());
        assertEquals(true, characterReader.isEmpty());
        assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testBufferUpWithIOException() throws IOException {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World")) {
            @Override
            void bufferUp() throws IOException {
                throw new IOException();
            }
        };
        characterReader.bufferUp();
    }

    @Test
    public void testPos() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.advance();
        characterReader.advance();
        assertEquals(10, characterReader.pos());

        characterReader.rewindToMark();
        assertEquals(9, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertEquals(false, characterReader.isEmpty());

        characterReader.consumeToEnd();
        assertEquals(true, characterReader.isEmpty());
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
        characterReader.consume();
        assertEquals('H', characterReader.current());
        characterReader.consume();
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testUnconsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.consume();
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.advance();
        characterReader.advance();
        assertEquals('l', characterReader.current());
    }

    @Test
    public void testMark() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.mark();
        characterReader.advance();
        characterReader.advance();
        characterReader.rewindToMark();
        assertEquals('H', characterReader.current());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdefabcdabcdabcdabcd"));
        assertEquals(2, characterReader.nextIndexOf('b'));
        assertEquals(10, characterReader.nextIndexOf('d'));
        assertEquals(-1, characterReader.nextIndexOf('x'));

        characterReader.consumeTo('a');
        assertEquals(3, characterReader.nextIndexOf('b'));
    }

    @Test
    public void testNextIndexOfWithCharSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertEquals(6, characterReader.nextIndexOf("ll"));
        assertEquals(4, characterReader.nextIndexOf("lo"));
        assertEquals(-1, characterReader.nextIndexOf("oo"));

        characterReader.consumeTo('l');
        assertEquals(5, characterReader.nextIndexOf("lo"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String result = characterReader.consumeTo('l');
        assertEquals("He", result);
        assertEquals('l', characterReader.current());

        result = characterReader.consumeTo("lo");
        assertEquals("Hello", result);
        assertEquals('o', characterReader.current());

        characterReader.consumeToEnd();
        assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testConsumeToWithEmptySequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String result = characterReader.consumeTo("");
        assertEquals("", result);
        assertEquals('H', characterReader.current());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String result = characterReader.consumeToAny(',', '.');
        assertEquals("Hello World", result);
        assertEquals('.', characterReader.current());

        result = characterReader.consumeToAny('a', 'e');
        assertEquals("Hello World", result);
        assertEquals('W', characterReader.current());
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String result = characterReader.consumeToAnySorted('z', 'a');
        assertEquals("Hello World", result);
        assertEquals('W', characterReader.current());
    }

    @Test
    public void testConsumeData() {
        CharacterReader characterReader = new CharacterReader(new StringReader("&<foo>bar"));
        String result = characterReader.consumeData();
        assertEquals("&<foo>", result);
        assertEquals('>', characterReader.current());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<title>Hello World</title>"));
        String result = characterReader.consumeTagName();
        assertEquals("title", result);
        assertEquals('/', characterReader.current());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String result = characterReader.consumeToEnd();
        assertEquals("Hello World", result);
        assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String result = characterReader.consumeLetterSequence();
        assertEquals("Hello", result);
        assertEquals('W', characterReader.current());

        characterReader.consumeTo('l');
        result = characterReader.consumeLetterSequence();
        assertEquals("lo", result);
        assertEquals('d', characterReader.current());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello1234World"));
        String result = characterReader.consumeLetterThenDigitSequence();
        assertEquals("Hello123", result);
        assertEquals('4', characterReader.current());

        characterReader.consumeTo('W');
        result = characterReader.consumeLetterThenDigitSequence();
        assertEquals("World", result);
        assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testMatches() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(characterReader.matches('l'));
        assertTrue(characterReader.matches("lo"));
        assertFalse(characterReader.matches('x'));

        characterReader.consumeTo('l');
        assertTrue(characterReader.matches('o'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(characterReader.matchesIgnoreCase("Lo"));
        assertTrue(characterReader.matchesIgnoreCase("world"));
        assertFalse(characterReader.matchesIgnoreCase("X"));

        characterReader.consumeTo('l');
        assertTrue(characterReader.matchesIgnoreCase("o"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(characterReader.matchesAny('l', 'o'));
        assertFalse(characterReader.matchesAny('x'));

        characterReader.consumeTo('l');
        assertTrue(characterReader.matchesAny('o'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(characterReader.matchesAnySorted('l', 'o'));
        assertFalse(characterReader.matchesAnySorted('x'));

        characterReader.consumeTo('l');
        assertTrue(characterReader.matchesAnySorted('o'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(characterReader.matchesLetter());
        characterReader.consume();
        assertTrue(characterReader.matchesLetter());
        assertFalse(characterReader.matchesLetter());

        characterReader.consumeTo('l');
        assertTrue(characterReader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello1234World"));
        assertTrue(characterReader.matchesDigit());
        characterReader.consume();
        assertTrue(characterReader.matchesDigit());
        assertFalse(characterReader.matchesDigit());

        characterReader.consumeTo('1');
        assertTrue(characterReader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String result = characterReader.consumeLetterSequence();
        assertEquals("Hello", result);
        assertEquals('W', characterReader.current());

        assertTrue(characterReader.matchConsume("lo"));
        assertEquals("ld", characterReader.toString());

        result = characterReader.consumeLetterSequence();
        assertEquals("ld", result);
        assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String result = characterReader.consumeLetterSequence();
        assertEquals("Hello", result);
        assertEquals('W', characterReader.current());

        assertTrue(characterReader.matchConsumeIgnoreCase("Lo"));
        assertEquals("ld", characterReader.toString());

        result = characterReader.consumeLetterSequence();
        assertEquals("ld", result);
        assertEquals(CharacterReader.EOF, characterReader.current());
    }

    @Test
    public void testContainsIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html><head><title>Hello World</title></head>"));
        assertTrue(characterReader.containsIgnoreCase("/title"));
        assertTrue(characterReader.containsIgnoreCase("/head"));
        assertFalse(characterReader.containsIgnoreCase("/body"));

        characterReader.consumeTo("/title");
        assertTrue(characterReader.containsIgnoreCase("/title"));
        assertTrue(characterReader.containsIgnoreCase("/head"));
        assertFalse(characterReader.containsIgnoreCase("/body"));
    }

    @Test
    public void testToString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String result = characterReader.toString();
        assertEquals("Hello World", result);

        characterReader.consumeTo('l');
        result = characterReader.toString();
        assertEquals("ello World", result);

        characterReader.consumeToEnd();
        result = characterReader.toString();
        assertEquals("", result);
    }
}