package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

/**
 * Test class for CharacterReader
 */
public final class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithReader() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertEquals("Hello World", characterReader.toString());
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertEquals("Hello World", characterReader.toString());
    }

    @Test
    public void testCharacterReaderPos() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.advance();
        characterReader.advance();
        assertEquals(2, characterReader.pos());
    }

    @Test
    public void testCharacterReaderIsEmpty() {
        CharacterReader characterReader = new CharacterReader(new StringReader(""));
        assertTrue(characterReader.isEmpty());
    }

    @Test
    public void testCharacterReaderIsNotEmpty() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertFalse(characterReader.isEmpty());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderThrowsIOException() throws IOException {
        CharacterReader characterReader = new CharacterReader(new Reader() {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new IOException();
            }
        });
    }

    @Test
    public void testCharacterReaderConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.advance();
        assertEquals('H', characterReader.consume());
    }

    @Test
    public void testCharacterReaderBufferUp() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        characterReader.advance();
        characterReader.advance();
        characterReader.bufferUp();
        assertEquals(3, characterReader.pos());
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertEquals(4, characterReader.nextIndexOf('l'));
        assertEquals(-1, characterReader.nextIndexOf('z'));
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String result = characterReader.consumeTo(' ');
        assertEquals("Hello", result);
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String result = characterReader.consumeToAny(new char[]{'l', 'o'});
        assertEquals("Hello", result);
    }

    @Test
    public void testCharacterReaderConsumeToAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String result = characterReader.consumeToAnySorted(new char[]{'o', 'l'});
        assertEquals("Hello", result);
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader characterReader = new CharacterReader(new StringReader("&amp;<tag>null"));
        String result = characterReader.consumeData();
        assertEquals("&amp;<tag>null", result);
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<tag>"));
        String result = characterReader.consumeTagName();
        assertEquals("tag", result);
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        String result = characterReader.consumeToEnd();
        assertEquals("Hello World", result);
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("HelloWorld"));
        String result = characterReader.consumeLetterSequence();
        assertEquals("Hello", result);
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello123World456"));
        String result = characterReader.consumeLetterThenDigitSequence();
        assertEquals("Hello123", result);
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("0x123ABCDEF"));
        String result = characterReader.consumeHexSequence();
        assertEquals("123ABCDEF", result);
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("1234567890"));
        String result = characterReader.consumeDigitSequence();
        assertEquals("1234567890", result);
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(characterReader.matches('H'));
        assertTrue(characterReader.matches("Hello"));
        assertFalse(characterReader.matches('Z'));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(characterReader.matchesIgnoreCase("Hello"));
        assertTrue(characterReader.matchesIgnoreCase("world"));
        assertFalse(characterReader.matchesIgnoreCase("Z"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(characterReader.matchesAny('H', 'W'));
        assertFalse(characterReader.matchesAny('Z'));
    }

    @Test
    public void testCharacterReaderMatchesAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(characterReader.matchesAnySorted('H', 'W'));
        assertFalse(characterReader.matchesAnySorted('Z'));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(characterReader.matchesLetter());
        assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader characterReader = new CharacterReader(new StringReader("12345"));
        assertFalse(characterReader.matchesLetter());
        assertTrue(characterReader.matchesDigit());
    }

    @Test
    public void testCharacterReaderMatchConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(characterReader.matchConsume("Hello"));
        assertFalse(characterReader.matchConsume("World"));
    }

    @Test
    public void testCharacterReaderMatchConsumeIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(characterReader.matchConsumeIgnoreCase("Hello"));
        assertTrue(characterReader.matchConsumeIgnoreCase("world"));
        assertFalse(characterReader.matchConsumeIgnoreCase("World"));
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html><head><title>My Title</title></head><body>Hello World</body></html>"));
        assertTrue(characterReader.containsIgnoreCase("/title"));
    }
}