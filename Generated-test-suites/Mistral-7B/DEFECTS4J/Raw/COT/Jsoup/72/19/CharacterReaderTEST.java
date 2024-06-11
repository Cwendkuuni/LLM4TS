package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Locale;

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
    public void testEmptyCharacterReader() {
        CharacterReader characterReader = new CharacterReader(new StringReader(""));
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCharacterReaderCurrent() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.advance();
        characterReader.advance();
        assertEquals('H', characterReader.current());
    }

    @Test(expected = IllegalStateException.class)
    public void testCharacterReaderConsumerWithNoChar() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.consume();
        characterReader.consume();
        characterReader.consume();
        characterReader.consume(); // should throw IllegalStateException as no char left
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.advance();
        characterReader.advance();
        String consumed = characterReader.consumeTo('l');
        assertEquals("llo World", characterReader.toString());
        assertEquals("Hi", consumed);
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String consumed = characterReader.consumeToEnd();
        assertEquals("Hello World", characterReader.toString());
        assertEquals("Hello World", consumed);
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcd1234efgh"));
        String consumed = characterReader.consumeToAny(new char[]{'a', 'b'});
        assertEquals("ab", consumed);
    }

    @Test
    public void testCharacterReaderConsumeToAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcd1234efgh"));
        String consumed = characterReader.consumeToAnySorted(new char[]{'d', 'e', 'f'});
        assertEquals("defg", consumed);
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader characterReader = new CharacterReader(new StringReader("&lt;abc&gt;def"));
        String consumed = characterReader.consumeData();
        assertEquals("&lt;abc&gt;def", characterReader.toString());
        assertEquals("&lt;abc", consumed);
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<title>Hello World</title>"));
        String consumed = characterReader.consumeTagName();
        assertEquals("<title>Hello World</title>", characterReader.toString());
        assertEquals("title", consumed);
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcd1234efgh"));
        assertTrue(characterReader.matches('a'));
        assertTrue(characterReader.matches('d'));
        assertFalse(characterReader.matches('x'));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("ABCD1234EFGH"));
        assertTrue(characterReader.matchesIgnoreCase("ab"));
        assertTrue(characterReader.matchesIgnoreCase("ef"));
        assertFalse(characterReader.matchesIgnoreCase("ij"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcd1234efgh"));
        assertTrue(characterReader.matchesAny('a'));
        assertTrue(characterReader.matchesAny('e'));
        assertFalse(characterReader.matchesAny('i'));
    }

    @Test
    public void testCharacterReaderMatchesAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcd1234efgh"));
        assertTrue(characterReader.matchesAnySorted(new char[]{'d', 'e'}));
        assertTrue(characterReader.matchesAnySorted(new char[]{'h', 'g'}));
        assertFalse(characterReader.matchesAnySorted(new char[]{'i', 'j'}));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abc123def"));
        assertTrue(characterReader.matchesLetter());
        assertTrue(characterReader.matchesLetter());
        assertTrue(characterReader.matchesLetter());
        assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcd1234ef"));
        assertTrue(characterReader.matchesDigit());
        assertTrue(characterReader.matchesDigit());
        assertTrue(characterReader.matchesDigit());
        assertTrue(characterReader.matchesDigit());
        assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testCharacterReaderToString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertEquals("Hello World", characterReader.toString());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() throws IOException {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World") {
            @Override
            public void close() throws IOException {
                throw new IOException();
            }
        });
        characterReader.consume();
    }

    @Test
    public void testCharacterReaderBufferUp() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.advance();
        characterReader.advance();
        characterReader.bufferUp();
        assertEquals(3, characterReader.pos());
    }
}