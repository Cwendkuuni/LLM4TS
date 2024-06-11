package org.jsoup.parser.test;

import org.junit.Test;
import org.junit.Assert.*;

import static org.jsoup.parser.CharacterReader.EOF;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderInstance() {
        String input = "This is a test string";
        CharacterReader reader = new CharacterReader(input);
        assertEquals(input, reader.input);
        assertEquals(input.length(), reader.length);
        assertEquals(0, reader.pos);
        assertEquals(0, reader.mark);
        assertEquals(EOF, reader.current());
        assertEquals(true, reader.isEmpty());
    }

    @Test
    public void testCharacterReaderPos() {
        String input = "This is a test string";
        CharacterReader reader = new CharacterReader(input);
        reader.advance();
        reader.advance();
        assertEquals('T', reader.current());
        assertEquals(2, reader.pos());
        reader.unconsume();
        assertEquals('T', reader.current());
    }

    @Test
    public void testCharacterReaderConsume() {
        String input = "This is a test string";
        CharacterReader reader = new CharacterReader(input);
        char c = reader.consume();
        assertEquals('T', c);
        assertEquals(1, reader.pos());
        assertEquals('h', reader.current());
    }

    @Test(expected = IllegalStateException.class)
    public void testCharacterReaderConsumePastEnd() {
        String input = "This is a test string";
        CharacterReader reader = new CharacterReader(input);
        for (int i = 0; i < input.length(); i++) {
            reader.consume();
        }
        reader.consume(); // should throw exception
    }

    @Test
    public void testCharacterReaderConsumeAsString() {
        String input = "abcd";
        CharacterReader reader = new CharacterReader(input);
        String consumed = reader.consumeAsString();
        assertEquals("ab", consumed);
        assertEquals(2, reader.pos());
        assertEquals('c', reader.current());
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        String input = "abcdabcd";
        CharacterReader reader = new CharacterReader(input);
        String consumed = reader.consumeTo('a');
        assertEquals("ab", consumed);
        assertEquals(2, reader.pos());
        assertEquals('c', reader.current());
        consumed = reader.consumeTo('d');
        assertEquals("abcd", consumed);
        assertEquals(5, reader.pos());
        assertEquals(EOF, reader.current());
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        String input = "abcdabcd";
        CharacterReader reader = new CharacterReader(input);
        String consumed = reader.consumeToEnd();
        assertEquals("abcdabcd", consumed);
        assertEquals(13, reader.pos());
        assertEquals(EOF, reader.current());
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        String input = "abcd1234efgABC";
        CharacterReader reader = new CharacterReader(input);
        String consumed = reader.consumeToAny('1', '2', '3');
        assertEquals("123", consumed);
        assertEquals(3, reader.pos());
        assertEquals('A', reader.current());

        consumed = reader.consumeToAny('e', 'f');
        assertEquals("efg", consumed);
        assertEquals(6, reader.pos());
        assertEquals('A', reader.current());
    }

    @Test
    public void testCharacterReaderMatches() {
        String input = "abcdabcd";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matches('a'));
        assertFalse(reader.matches('z'));
        assertFalse(reader.matches("ab"));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        String input = "abcdABCD";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchesIgnoreCase("ab"));
        assertTrue(reader.matchesIgnoreCase("Ab"));
        assertFalse(reader.matchesIgnoreCase("xy"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        String input = "abcdabcd";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchesAny('a', 'b'));
        assertFalse(reader.matchesAny('z'));
        assertFalse(reader.matchesAny('x'));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        String input = "abcdabcd";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        String input = "12345678";
        CharacterReader reader = new CharacterReader(input);
        assertFalse(reader.matchesLetter());
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testCharacterReaderMatchConsume() {
        String input = "abcdabcd";
        CharacterReader reader = new CharacterReader(input);
        String consumed = reader.matchConsume("ab");
        assertEquals("ab", consumed);
        assertEquals(2, reader.pos());
        assertEquals('c', reader.current());

        consumed = reader.matchConsume("cd");
        assertEquals("cd", consumed);
        assertEquals(4, reader.pos());
        assertEquals(EOF, reader.current());
    }

    @Test
    public void testCharacterReaderMatchConsumeIgnoreCase() {
        String input = "abCDabcd";
        CharacterReader reader = new CharacterReader(input);
        String consumed = reader.matchConsumeIgnoreCase("ab");
        assertEquals("ab", consumed);
        assertEquals(2, reader.pos());
        assertEquals('C', reader.current());
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        String input = "abcd<ABC>efgABCD";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.containsIgnoreCase("<ABC>"));
        assertFalse(reader.containsIgnoreCase("<xyz>"));
    }

    @Test
    public void testCharacterReaderMarkAndRewind() {
        String input = "abcdabcd";
        CharacterReader reader = new CharacterReader(input);
        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        assertEquals('T', reader.current());
    }
}