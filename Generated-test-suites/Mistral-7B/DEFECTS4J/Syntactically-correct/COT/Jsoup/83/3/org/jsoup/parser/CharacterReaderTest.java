package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderInstance() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        assertNotNull(characterReader);
    }

    @Test
    public void testCharacterReaderPos() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test String"));
        assertEquals(0, characterReader.pos());

        characterReader.advance();
        characterReader.advance();

        assertEquals(2, characterReader.pos());
    }

    @Test
    public void testCharacterReaderIsEmpty() {
        CharacterReader characterReader = new CharacterReader(new StringReader(""));
        assertTrue(characterReader.isEmpty());

        characterReader.advance();

        assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test"));
        assertEquals('T', characterReader.consume());
        assertEquals('e', characterReader.consume());
        assertEquals('s', characterReader.consume());
        assertEquals('t', characterReader.consume());
        assertEquals(EOF, characterReader.consume());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() throws IOException {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test") {
            @Override
            public int read(char[] cbuf) throws IOException {
                throw new IOException();
            }
        });
        characterReader.consume();
    }

    @Test
    public void testCharacterReaderBufferUp() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test"));
        characterReader.bufferUp();

        assertEquals(4, characterReader.bufLength);
        assertEquals(0, characterReader.bufPos);
        assertEquals(0, characterReader.readerPos);
        assertNull(characterReader.bufMark);
        assertArrayEquals(new String[]{""}, characterReader.stringCache, 0);

        characterReader.advance();
        characterReader.advance();

        characterReader.bufferUp();

        assertEquals(6, characterReader.bufLength);
        assertEquals(2, characterReader.bufPos);
        assertEquals(2, characterReader.readerPos);
        assertNull(characterReader.bufMark);
        assertEquals(1, characterReader.stringCache.length);
        assertNotNull(characterReader.stringCache[0]);
        assertEquals("Test", characterReader.stringCache[0]);
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestString"));
        assertEquals(6, characterReader.nextIndexOf('S'));
        assertEquals(-1, characterReader.nextIndexOf('Z'));

        characterReader.advance();
        characterReader.advance();

        assertEquals(7, characterReader.nextIndexOf('S'));
        assertEquals(-1, characterReader.nextIndexOf('Z'));
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestString"));
        assertEquals("Test", characterReader.consumeTo('S').trim());
        assertEquals("String", characterReader.consumeToEnd().trim());
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestString"));
        assertEquals("est", characterReader.consumeToAny(new char[]{'e', 's', 't'}));
        assertEquals("String", characterReader.consumeToEnd().trim());

        characterReader = new CharacterReader(new StringReader("TestString123"));
        assertEquals("String", characterReader.consumeToAny(new char[]{'1', '2', '3'}));
        assertEquals("String", characterReader.consumeToEnd().trim());

        characterReader = new CharacterReader(new StringReader("TestString123!"));
        assertEquals("String", characterReader.consumeToAny(new char[]{'1', '2', '3', '!'}));
        assertEquals("!", characterReader.consumeToEnd().charAt(0));
    }

    @Test
    public void testCharacterReaderConsumeToAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestString"));
        assertEquals("est", characterReader.consumeToAnySorted(new char[]{'e', 's', 't'}));
        assertEquals("String", characterReader.consumeToEnd().trim());

        characterReader = new CharacterReader(new StringReader("TestString123"));
        assertEquals("String", characterReader.consumeToAnySorted(new char[]{'1', '2', '3'}));
        assertEquals("String", characterReader.consumeToEnd().trim());

        characterReader = new CharacterReader(new StringReader("TestString123!"));
        assertEquals("String", characterReader.consumeToAnySorted(new char[]{'1', '2', '3', '!'}));
        assertEquals("!", characterReader.consumeToEnd().charAt(0));
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test&<null"));
        assertEquals("Test", characterReader.consumeData().trim());
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test\t<tagName>"));
        assertEquals("tagName", characterReader.consumeTagName().trim());
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test"));
        assertEquals("Test", characterReader.consumeToEnd().trim());

        characterReader = new CharacterReader(new StringReader("TestString"));
        assertEquals("String", characterReader.consumeToEnd().trim());
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestLetterSequence"));
        assertEquals("LetterSequence", characterReader.consumeLetterSequence().trim());
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestLetterThenDigitSequence123"));
        assertEquals("LetterThenDigitSequence123", characterReader.consumeLetterThenDigitSequence().trim());
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test%#ABcd%EF"));
        assertEquals("%#ABcd%EF", characterReader.consumeHexSequence().trim());
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test123456"));
        assertEquals("123456", characterReader.consumeDigitSequence().trim());
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestString"));
        assertTrue(characterReader.matches('T'));
        assertTrue(characterReader.matches("String"));
        assertFalse(characterReader.matches('Z'));

        characterReader.advance();
        characterReader.advance();

        assertTrue(characterReader.matches('s'));
        assertTrue(characterReader.matches("ring"));
        assertFalse(characterReader.matches('Z'));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestString"));
        assertTrue(characterReader.matchesIgnoreCase("test"));
        assertTrue(characterReader.matchesIgnoreCase("string"));
        assertFalse(characterReader.matchesIgnoreCase("Z"));

        characterReader.advance();
        characterReader.advance();

        assertTrue(characterReader.matchesIgnoreCase("s"));
        assertTrue(characterReader.matchesIgnoreCase("ring"));
        assertFalse(characterReader.matchesIgnoreCase("Z"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestString"));
        assertTrue(characterReader.matchesAny('T', 's'));
        assertTrue(characterReader.matchesAny("test", "string"));
        assertFalse(characterReader.matchesAny('Z'));

        characterReader.advance();
        characterReader.advance();

        assertTrue(characterReader.matchesAny('s', "ring"));
        assertTrue(characterReader.matchesAny("ring", "ring"));
        assertFalse(characterReader.matchesAny('Z'));
    }

    @Test
    public void testCharacterReaderMatchesAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestString"));
        assertTrue(characterReader.matchesAnySorted('T', 's'));
        assertTrue(characterReader.matchesAnySorted("test", "string"));
        assertFalse(characterReader.matchesAnySorted('Z'));

        characterReader.advance();
        characterReader.advance();

        assertTrue(characterReader.matchesAnySorted('s', "ring"));
        assertTrue(characterReader.matchesAnySorted("ring", "ring"));
        assertFalse(characterReader.matchesAnySorted('Z'));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestString"));
        assertTrue(characterReader.matchesLetter());
        assertTrue(characterReader.matchesLetter());
        assertFalse(characterReader.matchesLetter());

        characterReader.advance();
        characterReader.advance();

        assertTrue(characterReader.matchesLetter());
        assertTrue(characterReader.matchesLetter());
        assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader characterReader = new CharacterReader(new StringReader("Test123String"));
        assertTrue(characterReader.matchesDigit());
        assertTrue(characterReader.matchesDigit());
        assertTrue(characterReader.matchesDigit());
        assertFalse(characterReader.matchesDigit());

        characterReader.advance();
        characterReader.advance();
        characterReader.advance();
        characterReader.advance();

        assertTrue(characterReader.matchesDigit());
        assertTrue(characterReader.matchesDigit());
        assertTrue(characterReader.matchesDigit());
        assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testCharacterReaderMatchConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestString"));
        assertTrue(characterReader.matchConsume("Test"));
        assertTrue(characterReader.matchConsume("String"));
        assertFalse(characterReader.matchConsume("Z"));

        characterReader.advance();
        characterReader.advance();

        assertTrue(characterReader.matchConsume("s"));
        assertTrue(characterReader.matchConsume("ring"));
        assertFalse(characterReader.matchConsume("Z"));
    }

    @Test
    public void testCharacterReaderMatchConsumeIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("TestString"));
        assertTrue(characterReader.matchConsumeIgnoreCase("test"));
        assertTrue(characterReader.matchConsumeIgnoreCase("string"));
        assertFalse(characterReader.matchConsumeIgnoreCase("Z"));

        characterReader.advance();
        characterReader.advance();

        assertTrue(characterReader.matchConsumeIgnoreCase("s"));
        assertTrue(characterReader.matchConsumeIgnoreCase("ring"));
        assertFalse(characterReader.matchConsumeIgnoreCase("Z"));
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html><title>TestString</title>"));
        assertTrue(characterReader.containsIgnoreCase("/title>"));
        assertTrue(characterReader.containsIgnoreCase("/TITLE>"));
        assertTrue(characterReader.containsIgnoreCase("teststring"));
        assertTrue(characterReader.containsIgnoreCase("TestString"));
        assertFalse(characterReader.containsIgnoreCase("Z"));
    }

}