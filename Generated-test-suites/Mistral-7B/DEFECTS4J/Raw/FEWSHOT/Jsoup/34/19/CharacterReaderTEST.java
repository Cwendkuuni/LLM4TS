package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmptyString() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderSingleCharacter() {
        CharacterReader reader = new CharacterReader("a");
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals(1, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderMultipleCharacters() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals('b', reader.current());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.current());
        assertEquals('c', reader.consume());
        assertEquals('d', reader.current());
        assertEquals('d', reader.consume());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        CharacterReader reader = new CharacterReader("abcd");
        reader.mark();
        assertEquals('a', reader.current());
        reader.advance();
        assertEquals('b', reader.current());
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeAsString() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("abc", reader.consumeAsString());
        assertEquals('d', reader.current());
        assertEquals("def", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader reader = new CharacterReader("abcdabcd");
        assertEquals(1, reader.nextIndexOf('b'));
        assertEquals(6, reader.nextIndexOf('c'));
        assertEquals(-1, reader.nextIndexOf('z'));
        assertEquals(-1, reader.nextIndexOf(""));
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals("bc", reader.consumeTo('c'));
        assertEquals("d", reader.current());
        assertEquals("", reader.consumeTo('z'));
    }

    @Test
    public void testCharacterReaderConsumeToString() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals("bc", reader.consumeTo("bc"));
        assertEquals("d", reader.current());
        assertEquals("", reader.consumeTo("z"));
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals("bc", reader.consumeToAny('b', 'c'));
        assertEquals("d", reader.current());
        assertEquals("", reader.consumeToAny('z'));
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcd");
        assertEquals("cd", reader.consumeToEnd());
        assertEquals(EOF, reader.current());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcd1efgh");
        assertEquals("abcd", reader.consumeLetterSequence());
        assertEquals('1', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("abcd1efgh");
        assertEquals("abcd1", reader.consumeLetterThenDigitSequence());
        assertEquals('e', reader.current());
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('b'));
        assertFalse(reader.matches('z'));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("ABCD");
        assertTrue(reader.matchesIgnoreCase("ab"));
        assertTrue(reader.matchesIgnoreCase("CD"));
        assertFalse(reader.matchesIgnoreCase("z"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader("abcd");
        assertTrue(reader.matchesAny('a', 'b', 'c', 'd'));
        assertFalse(reader.matchesAny('z'));
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcdefghijk<title>ABCDEFGHIJK</title>");
        assertTrue(reader.containsIgnoreCase("title"));
        assertTrue(reader.containsIgnoreCase("Title"));
        assertFalse(reader.containsIgnoreCase("Z"));
    }
}