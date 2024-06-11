package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader_EmptyString() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
        assertEquals(-1, reader.current());
        assertEquals(-1, reader.consume());
    }

    @Test
    public void testCharacterReader_SimpleConsume() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals('b', reader.current());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.current());
        assertEquals('c', reader.consume());
        assertEquals('d', reader.current());
        assertEquals('d', reader.consume());
        assertEquals('e', reader.current());
        assertEquals('e', reader.consume());
        assertEquals('f', reader.current());
        assertEquals('f', reader.consume());
        assertTrue(reader.isEmpty());
        assertEquals(-1, reader.current());
        assertEquals(-1, reader.consume());
    }

    @Test
    public void testCharacterReader_SimpleMarkAndRewind() {
        CharacterReader reader = new CharacterReader("abcdef");
        reader.mark();
        reader.advance();
        reader.advance();
        assertEquals('c', reader.current());
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testCharacterReader_ConsumeAsString() {
        CharacterReader reader = new CharacterReader("123abc");
        assertEquals("12", reader.consumeAsString());
        assertEquals("c", reader.current().toString());
        assertEquals("abc", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReader_ConsumeTo() {
        CharacterReader reader = new CharacterReader("123abc");
        assertEquals("12", reader.consumeTo('c'));
        assertEquals('c', reader.current().toString());
        assertEquals("abc", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReader_ConsumeToAny() {
        CharacterReader reader = new CharacterReader("123abcdefg");
        assertEquals("123", reader.consumeToAny('1', '3'));
        assertEquals('d', reader.current().toString());
        assertEquals("defg", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReader_ConsumeToEnd() {
        CharacterReader reader = new CharacterReader("123abcdefg");
        assertEquals("123abcdefg", reader.consumeToEnd());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_ConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("123abcXYZ");
        assertEquals("abcXYZ", reader.consumeLetterSequence());
        assertEquals('X', reader.current().toString());
    }

    @Test
    public void testCharacterReader_ConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("123abcf12eF12G");
        assertEquals("f12eF12", reader.consumeHexSequence());
        assertEquals('G', reader.current().toString());
    }

    @Test
    public void testCharacterReader_ConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("123abcf12eF12");
        assertEquals("123", reader.consumeDigitSequence());
        assertEquals('a', reader.current().toString());
    }

    @Test
    public void testCharacterReader_Matches() {
        CharacterReader reader = new CharacterReader("123abcf12eF12");
        assertFalse(reader.matches('z'));
        assertFalse(reader.matches("xyz"));
        assertTrue(reader.matches('f'));
        assertTrue(reader.matches("F"));
        assertTrue(reader.matches("1"));
    }

    @Test
    public void testCharacterReader_MatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("123abcf12eF12");
        assertFalse(reader.matchesIgnoreCase("XYZ"));
        assertTrue(reader.matchesIgnoreCase("f"));
        assertTrue(reader.matchesIgnoreCase("F"));
        assertTrue(reader.matchesIgnoreCase("1"));
    }

    @Test
    public void testCharacterReader_MatchesAny() {
        CharacterReader reader = new CharacterReader("123abcf12eF12");
        assertFalse(reader.matchesAny('z'));
        assertTrue(reader.matchesAny('f', 'F'));
        assertTrue(reader.matchesAny('1'));
        assertTrue(reader.matchesAny('1', '2'));
    }

    @Test
    public void testCharacterReader_MatchesLetter() {
        CharacterReader reader = new CharacterReader("123abcf12eF12");
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter('1'));
        assertFalse(reader.matchesLetter(' '));
    }

    @Test
    public void testCharacterReader_MatchesDigit() {
        CharacterReader reader = new CharacterReader("123abcf12eF12");
        assertFalse(reader.matchesDigit());
        assertTrue(reader.matchesDigit('1'));
        assertFalse(reader.matchesDigit(' '));
    }

    @Test
    public void testCharacterReader_MatchConsume() {
        CharacterReader reader = new CharacterReader("123abcf12eF12");
        assertTrue(reader.matchConsume("f"));
        assertEquals("F", reader.current().toString());
        assertTrue(reader.matchConsume("F"));
        assertEquals("12", reader.consumeTo('1'));
        assertTrue(reader.matchConsume("1"));
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_MatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader("123abcf12eF12");
        assertTrue(reader.matchConsumeIgnoreCase("F"));
        assertEquals("f", reader.current().toString());
        assertTrue(reader.matchConsumeIgnoreCase("F"));
        assertTrue(reader.matchConsumeIgnoreCase("1"));
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_ContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader("123abcf12eF12");
        assertTrue(reader.containsIgnoreCase("/title"));
        assertFalse(reader.containsIgnoreCase("/style"));
        assertTrue(reader.containsIgnoreCase("/TITLE"));
        assertFalse(reader.containsIgnoreCase("/STYLE"));
        assertTrue(reader.containsIgnoreCase("/STYLE"));
    }

    @Test(expected = NullPointerException.class)
    public void testCharacterReader_NullInput() {
        new CharacterReader(null);
    }
}