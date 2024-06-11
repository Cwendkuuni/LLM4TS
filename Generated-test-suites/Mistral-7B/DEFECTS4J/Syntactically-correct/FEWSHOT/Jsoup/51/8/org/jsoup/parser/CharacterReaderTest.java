package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;

import java.util.Arrays;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        String input = "abc&def<>";
        CharacterReader reader = new CharacterReader(input);
        assertEquals(0, reader.pos());
        assertEquals("", reader.consumeAsString());
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals(1, reader.pos());
        assertEquals("a", reader.consumeAsString());
        assertEquals('b', reader.current());
        assertEquals('b', reader.consume());
        assertEquals(2, reader.pos());
        assertEquals("ab", reader.consumeAsString());
        assertEquals('c', reader.current());
        assertEquals('c', reader.consume());
        assertEquals(3, reader.pos());
        assertEquals("abc", reader.consumeAsString());
        assertEquals('&', reader.current());
        assertEquals('&', reader.consume());
        assertEquals(4, reader.pos());
        assertEquals("abc&", reader.consumeAsString());
        assertEquals('d', reader.current());
        assertEquals('d', reader.consume());
        assertEquals(5, reader.pos());
        assertEquals("abcd", reader.consumeAsString());
        assertEquals('e', reader.current());
        assertEquals('e', reader.consume());
        assertEquals(6, reader.pos());
        assertEquals("abcde", reader.consumeAsString());
        assertEquals('f', reader.current());
        assertEquals('f', reader.consume());
        assertEquals(7, reader.pos());
        assertEquals("abcdef", reader.consumeAsString());
        assertEquals('>', reader.current());
        assertEquals('>', reader.consume());
        assertEquals(8, reader.pos());
        assertEquals("abcdef>", reader.consumeAsString());
        assertEquals(CharacterReader.EOF, reader.consume());
        assertEquals(8, reader.pos());
        assertEquals("abcdef>", reader.consumeAsString());
        assertEquals(false, reader.matches('z'));
        assertEquals(false, reader.matches("xyz"));
        assertEquals(true, reader.matches('d'));
        assertEquals(true, reader.matches("def"));
        assertEquals(true, reader.matchesIgnoreCase("Def"));
        assertEquals(true, reader.matchesAny('a', 'b', 'c'));
        assertEquals(true, reader.matchesAnySorted(new char[]{'c', 'b', 'a'}));
        assertEquals(true, reader.matchesLetter());
        assertEquals(true, reader.matchesDigit());
        assertEquals(true, reader.matchConsume("def"));
        assertEquals(true, reader.matchConsumeIgnoreCase("DEF"));
        assertEquals(true, reader.containsIgnoreCase("def"));
        assertEquals(true, reader.containsIgnoreCase("Def"));
        assertEquals(true, reader.containsIgnoreCase("Def123"));
        assertEquals(true, reader.containsIgnoreCase("DEF123"));
        assertEquals("", reader.consumeToAny());
        assertEquals("", reader.consumeToAnySorted());
        assertEquals("&", reader.consumeData());
        assertEquals("<title", reader.consumeTagName());
        assertEquals("abcdef", reader.consumeToEnd());
        assertEquals("abcd", reader.consumeLetterSequence());
        assertEquals("abcd123", reader.consumeLetterThenDigitSequence());
        assertEquals("012345", reader.consumeDigitSequence());
        Validate.isTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderNullInput() {
        CharacterReader reader = new CharacterReader(null);
        Validate.isTrue(reader.isEmpty());
        assertEquals(CharacterReader.EOF, reader.consume());
        Validate.isTrue(reader.isEmpty());
    }
}