package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;

import java.util.Locale;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderEmptyInput() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals( CharacterReader.EOF, reader.current() );
    }

    @Test
    public void testCharacterReaderConsume() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
        assertTrue(reader.isEmpty());
        assertEquals( CharacterReader.EOF, reader.current() );
    }

    @Test
    public void testCharacterReaderConsumeAsString() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals("a", reader.consumeAsString());
        assertEquals("b", reader.consumeAsString());
        assertEquals("c", reader.consumeAsString());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals(1, reader.nextIndexOf('b'));
        assertEquals(-1, reader.nextIndexOf('x'));
        assertEquals(-1, reader.nextIndexOf(""));
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("bc", reader.consumeTo('c'));
        assertEquals("def", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcd123ef");
        assertEquals("bcd123", reader.consumeToAny('b', 'd', '1', '2', '3'));
        assertEquals("ef", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("abc", reader.consumeLetterSequence());
        assertEquals("def", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123def");
        assertEquals("abc123", reader.consumeLetterThenDigitSequence());
        assertEquals("def", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("abc123def0x123456");
        assertEquals("123456", reader.consumeHexSequence());
        assertEquals("def0x", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123def456");
        assertEquals("123456", reader.consumeDigitSequence());
        assertEquals("def", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('b'));
        assertFalse(reader.matches('c'));
        assertTrue(reader.matches("ab"));
        assertFalse(reader.matches("cd"));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("AbC");
        assertTrue(reader.matchesIgnoreCase("aBc"));
        assertTrue(reader.matchesIgnoreCase("AbC"));
        assertFalse(reader.matchesIgnoreCase("dEf"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader("abc");
        assertTrue(reader.matchesAny('a', 'b', 'c'));
        assertTrue(reader.matchesAny('a', 'b', 'c', 'd'));
        assertFalse(reader.matchesAny('x', 'y', 'z'));
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcABC");
        assertTrue(reader.containsIgnoreCase("ab"));
        assertTrue(reader.containsIgnoreCase("ABC"));
        assertFalse(reader.containsIgnoreCase("xyz"));
    }

    @Test
    public void testCharacterReaderMark() {
        CharacterReader reader = new CharacterReader("abc");
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test(expected = NullPointerException.class)
    public void testCharacterReaderNullInput() {
        new CharacterReader(null);
    }
}