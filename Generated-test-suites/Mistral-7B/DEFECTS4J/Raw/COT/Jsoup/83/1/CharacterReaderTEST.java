package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructor() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        assertNotNull(characterReader);
    }

    @Test
    public void testCharacterReaderBufferUp() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        characterReader.bufferUp();
        assertTrue(characterReader.bufPos > 0);
    }

    @Test
    public void testCharacterReaderPos() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        characterReader.advance();
        characterReader.advance();
        int pos = characterReader.pos();
        assertEquals(3, pos);
    }

    @Test
    public void testCharacterReaderIsEmpty() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        assertFalse(characterReader.isEmpty());
        characterReader.consume();
        characterReader.consume();
        assertTrue(characterReader.isEmpty());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() throws IOException {
        CharacterReader characterReader = new CharacterReader(new StringReader("test") {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new IOException();
            }
        });
        characterReader.bufferUp();
    }

    @Test
    public void testCharacterReaderConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcd"));
        assertEquals('a', characterReader.consume());
        assertEquals('b', characterReader.consume());
        assertEquals('c', characterReader.consume());
        assertEquals('d', characterReader.consume());
        assertEquals(CharacterReader.EOF, characterReader.consume());
    }

    @Test
    public void testCharacterReaderUnconsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcd"));
        characterReader.consume();
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('b', characterReader.current());
    }

    @Test
    public void testCharacterReaderAdvance() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcd"));
        characterReader.advance();
        characterReader.advance();
        assertEquals('c', characterReader.current());
    }

    @Test
    public void testCharacterReaderMark() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcd"));
        characterReader.mark();
        characterReader.consume();
        characterReader.rewindToMark();
        assertEquals('a', characterReader.current());
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcd"));
        assertEquals(1, characterReader.nextIndexOf('b'));
        assertEquals(4, characterReader.nextIndexOf('e'));
        assertEquals(-1, characterReader.nextIndexOf('x'));
    }

    @Test
    public void testCharacterReaderNextIndexOfWithSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcb"));
        assertEquals(1, characterReader.nextIndexOf("bc"));
        assertEquals(-1, characterReader.nextIndexOf("xy"));
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdef"));
        assertEquals("bcde", characterReader.consumeTo('e'));
        assertEquals("f", characterReader.current());
    }

    @Test
    public void testCharacterReaderConsumeToWithSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcbcd"));
        assertEquals("bcd", characterReader.consumeTo("cd"));
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdabcd"));
        assertEquals("abcd", characterReader.consumeToAny(new char[]{'a', 'b', 'c', 'd'}));
        assertEquals("d", characterReader.consumeToAny(new char[]{'a', 'e'}));
    }

    @Test
    public void testCharacterReaderConsumeToAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("abcdabcd"));
        assertEquals("abcd", characterReader.consumeToAnySorted(new char[]{'a', 'b', 'c', 'd'}));
        assertEquals("cd", characterReader.consumeToAnySorted(new char[]{'c', 'd'}));
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader characterReader = new CharacterReader(new StringReader("&<abc"));
        assertEquals("&<", characterReader.consumeData());
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<tag name=\"test\"/>"));
        assertEquals("tag", characterReader.consumeTagName());
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        assertEquals("test", characterReader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("LetterSequence"));
        assertEquals("LetterSequence", characterReader.consumeLetterSequence());
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("LetterThenDigitSequence123"));
        assertEquals("LetterThenDigitSequence123", characterReader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("0xDEADBEEF"));
        assertEquals("DEADBEEF", characterReader.consumeHexSequence());
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader characterReader = new CharacterReader(new StringReader("1234567890"));
        assertEquals("1234567890", characterReader.consumeDigitSequence());
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        assertTrue(characterReader.matches('t'));
        assertTrue(characterReader.matches("est"));
        assertFalse(characterReader.matches('x'));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        assertTrue(characterReader.matchesIgnoreCase("esT"));
        assertFalse(characterReader.matchesIgnoreCase("x"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        assertTrue(characterReader.matchesAny(new char[]{'t', 'e'}));
        assertFalse(characterReader.matchesAny(new char[]{'x'}));
    }

    @Test
    public void testCharacterReaderMatchesAnySorted() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        assertTrue(characterReader.matchesAnySorted(new char[]{'t', 'e'}));
        assertFalse(characterReader.matchesAnySorted(new char[]{'x'}));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        assertTrue(characterReader.matchesLetter());
        assertFalse(characterReader.matchesLetter());
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test123"));
        assertTrue(characterReader.matchesDigit());
        assertFalse(characterReader.matchesDigit());
    }

    @Test
    public void testCharacterReaderMatchConsume() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        assertTrue(characterReader.matchConsume("est"));
    }

    @Test
    public void testCharacterReaderMatchConsumeIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        assertTrue(characterReader.matchConsumeIgnoreCase("esT"));
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader characterReader = new CharacterReader(new StringReader("<html><head><title>Test</title></head><body>"));
        assertTrue(characterReader.containsIgnoreCase("<title>"));
        assertTrue(characterReader.containsIgnoreCase("title"));
        assertFalse(characterReader.containsIgnoreCase("header"));
    }

    @Test
    public void testCharacterReaderToString() {
        CharacterReader characterReader = new CharacterReader(new StringReader("test"));
        assertEquals("test", characterReader.toString());
    }
}