package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;

import java.util.Arrays;
import java.util.Locale;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader_constructor() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertEquals(input.toCharArray(), reader.input);
        assertEquals(input.length(), reader.length);
    }

    @Test
    public void testCharacterReader_isEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());

        reader = new CharacterReader("abc");
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_pos() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals(0, reader.pos());

        reader.consume();
        reader.consume();
        assertEquals(2, reader.pos());
    }

    @Test
    public void testCharacterReader_pos_advance() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.advance();
        reader.advance();
        reader.advance();
        assertEquals(3, reader.pos());
    }

    @Test
    public void testCharacterReader_pos_mark() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testCharacterReader_consume() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
        assertEquals('d', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals('f', reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testCharacterReader_consumeAsString() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("abc", reader.consumeAsString());
        assertEquals("def", reader.consumeAsString());
        assertEquals("", reader.consumeAsString());
    }

    @Test
    public void testCharacterReader_nextIndexOf_char() {
        CharacterReader reader = new CharacterReader("abcdefghij");
        assertEquals(1, reader.nextIndexOf('b'));
        assertEquals(4, reader.nextIndexOf('e'));
        assertEquals(-1, reader.nextIndexOf('z'));
    }

    @Test
    public void testCharacterReader_nextIndexOf_CharSequence() {
        CharacterReader reader = new CharacterReader("abcdefghij");
        assertEquals(5, reader.nextIndexOf("ef"));
        assertEquals(-1, reader.nextIndexOf("xyz"));
    }

    @Test
    public void testCharacterReader_consumeTo_char() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("bc", reader.consumeTo('c'));
        assertEquals("def", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReader_consumeTo_String() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("bc", reader.consumeTo("c"));
        assertEquals("def", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReader_consumeToAny_charArray() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("b", reader.consumeToAny(new char[]{'b'}));
        assertEquals("", reader.consumeToAny(new char[]{'}'}));
    }

    @Test
    public void testCharacterReader_consumeToAnySorted_charArray() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("b", reader.consumeToAnySorted(new char[]{'b'}));
        assertEquals("", reader.consumeToAnySorted(new char[]{'}'}));
    }

    @Test
    public void testCharacterReader_consumeData() {
        CharacterReader reader = new CharacterReader("abc&def");
        assertEquals("abc", reader.consumeData());
    }

    @Test
    public void testCharacterReader_consumeTagName() {
        CharacterReader reader = new CharacterReader("<title>Hello World</title>");
        assertEquals("<title>", reader.consumeTagName());
    }

    @Test
    public void testCharacterReader_consumeToEnd() {
        CharacterReader reader = new CharacterReader("<title>Hello World</title>");
        assertEquals("</title>", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReader_consumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abcdefg");
        assertEquals("defg", reader.consumeLetterSequence());
    }

    @Test
    public void testCharacterReader_consumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("abc123def");
        assertEquals("123def", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testCharacterReader_consumeHexSequence() {
        CharacterReader reader = new CharacterReader("&0xA1;&0x2F;");
        assertEquals("A1", reader.consumeHexSequence());
        assertEquals("2F", reader.consumeHexSequence());
    }

    @Test
    public void testCharacterReader_consumeDigitSequence() {
        CharacterReader reader = new CharacterReader("12345");
        assertEquals("5", reader.consumeDigitSequence());
    }

    @Test
    public void testCharacterReader_matches_char() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matches('a'));
        assertFalse(reader.matches('z'));
    }

    @Test
    public void testCharacterReader_matches_String() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matches("ab"));
        assertFalse(reader.matches("xyz"));
    }

    @Test
    public void testCharacterReader_matchesIgnoreCase_String() {
        CharacterReader reader = new CharacterReader("AbCdEf");
        assertTrue(reader.matchesIgnoreCase("ab"));
        assertFalse(reader.matchesIgnoreCase("xyz"));
    }

    @Test
    public void testCharacterReader_matchesAny_charArray() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matchesAny(new char[]{'a', 'b'}));
        assertFalse(reader.matchesAny(new char[]{'z'}));
    }

    @Test
    public void testCharacterReader_matchesAnySorted_charArray() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matchesAnySorted(new char[]{'a', 'b'}));
        assertFalse(reader.matchesAnySorted(new char[]{'z'}));
    }

    @Test
    public void testCharacterReader_matchesLetter() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter('5'));
    }

    @Test
    public void testCharacterReader_matchesDigit() {
        CharacterReader reader = new CharacterReader("12345");
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit('a'));
    }

    @Test
    public void testCharacterReader_matchConsume_String() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matchConsume("ab"));
        assertFalse(reader.matchConsume("xyz"));
    }

    @Test
    public void testCharacterReader_matchConsumeIgnoreCase_String() {
        CharacterReader reader = new CharacterReader("AbCdEf");
        assertTrue(reader.matchConsumeIgnoreCase("ab"));
        assertFalse(reader.matchConsumeIgnoreCase("xyz"));
    }

    @Test
    public void testCharacterReader_containsIgnoreCase_String() {
        CharacterReader reader = new CharacterReader("<html><title>Hello World</title></html>");
        assertTrue(reader.containsIgnoreCase("/title/"));
        assertTrue(reader.containsIgnoreCase("/TITLE/"));
        assertFalse(reader.containsIgnoreCase("/body/"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCharacterReader_constructor_null() {
        new CharacterReader(null);
    }
}