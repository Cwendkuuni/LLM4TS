package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader_Constructor() {
        String input = "abcdefghijklmnopqrstuvwxyz";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("abcdefghijklmnopqrstuvwxyz", reader.input);
        assertEquals(26, reader.length);
    }

    @Test
    public void testIsEmpty() {
        String input = "";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.isEmpty());

        input = "abc";
        reader = new CharacterReader(input);
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrentChar() {
        String input = "abcdefghijklmnopqrstuvwxyz";
        CharacterReader reader = new CharacterReader(input);
        assertEquals('a', reader.current());
        reader.advance();
        assertEquals('b', reader.current());
        reader.consume();
        assertEquals('c', reader.current());
    }

    @Test
    public void testConsume() {
        String input = "abcdefghijklmnopqrstuvwxyz";
        CharacterReader reader = new CharacterReader(input);
        reader.consume();
        assertEquals('b', reader.current());

        reader.consume();
        assertEquals('c', reader.current());

        reader.consumeToEnd();
        assertEquals("", reader.toString());
    }

    @Test
    public void testUnconsume() {
        String input = "abcdefghijklmnopqrstuvwxyz";
        CharacterReader reader = new CharacterReader(input);
        reader.consume();
        reader.unconsume();
        assertEquals('a', reader.current());

        reader.consumeToEnd();
        assertEquals("", reader.toString());
    }

    @Test
    public void testAdvance() {
        String input = "abcdefghijklmnopqrstuvwxyz";
        CharacterReader reader = new CharacterReader(input);
        reader.advance();
        reader.advance();
        reader.advance();
        assertEquals('d', reader.current());

        reader.advanceToEnd();
        assertEquals("", reader.toString());
    }

    @Test
    public void testMark() {
        String input = "abcdefghijklmnopqrstuvwxyz";
        CharacterReader reader = new CharacterReader(input);
        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testConsumeAsString() {
        String input = "abcdefghijklmnopqrstuvwxyz";
        CharacterReader reader = new CharacterReader(input);
        String consumed = reader.consumeAsString();
        assertEquals("b", consumed);

        reader.advance();
        consumed = reader.consumeAsString();
        assertEquals("c", consumed);

        reader.advanceToEnd();
        consumed = reader.consumeAsString();
        assertEquals("", consumed);
    }

    @Test
    public void testNextIndexOf_char() {
        String input = "abcabcabc";
        CharacterReader reader = new CharacterReader(input);
        int index = reader.nextIndexOf('a');
        assertEquals(2, index);

        index = reader.nextIndexOf('z');
        assertEquals(-1, index);

        reader.advanceToEnd();
        index = reader.nextIndexOf('z');
        assertEquals(-1, index);
    }

    @Test
    public void testNextIndexOf_CharSequence() {
        String input = "abcabcabc";
        CharacterReader reader = new CharacterReader(input);
        String seq = "bc";
        int index = reader.nextIndexOf(seq);
        assertEquals(2, index);

        seq = "zz";
        index = reader.nextIndexOf(seq);
        assertEquals(-1, index);

        reader.advanceToEnd();
        index = reader.nextIndexOf(seq);
        assertEquals(-1, index);
    }

    @Test
    public void testConsumeTo_char() {
        String input = "abcabcabc";
        CharacterReader reader = new CharacterReader(input);
        String consumed = reader.consumeTo('c');
        assertEquals("bc", consumed);

        reader.advance();
        consumed = reader.consumeTo('c');
        assertEquals("d", consumed);

        reader.advanceToEnd();
        consumed = reader.consumeTo('c');
        assertEquals("", consumed);
    }

    @Test
    public void testConsumeTo_String() {
        String input = "abcabcabc";
        CharacterReader reader = new CharacterReader(input);
        String consumed = reader.consumeTo("bc");
        assertEquals("bc", consumed);

        reader.advance();
        consumed = reader.consumeTo("bc");
        assertEquals("d", consumed);

        reader.advanceToEnd();
        consumed = reader.consumeTo("bc");
        assertEquals("", consumed);
    }

    @Test
    public void testConsumeToAny() {
        String input = "abcd1234ef";
        CharacterReader reader = new CharacterReader(input);
        String consumed = reader.consumeToAny('d', 'e');
        assertEquals("cd", consumed);

        reader.advance();
        consumed = reader.consumeToAny('1', '3');
        assertEquals("2", consumed);

        reader.advanceToEnd();
        consumed = reader.consumeToAny('d', 'e');
        assertEquals("", consumed);

        consumed = reader.consumeToAny('a', 'z');
        assertEquals("abcd1234ef", consumed);
    }

    @Test
    public void testConsumeToEnd() {
        String input = "abcdefghijklmnopqrstuvwxyz";
        CharacterReader reader = new CharacterReader(input);
        String consumed = reader.consumeToEnd();
        assertEquals("", consumed);

        reader = new CharacterReader("abc");
        consumed = reader.consumeToEnd();
        assertEquals("abc", consumed);

        reader = new CharacterReader("");
        consumed = reader.consumeToEnd();
        assertEquals("", consumed);
    }

    @Test
    public void testConsumeLetterSequence() {
        String input = "abcdefghijklmnopqrstuvwxyz";
        CharacterReader reader = new CharacterReader(input);
        String consumed = reader.consumeLetterSequence();
        assertEquals("abc", consumed);

        reader.advance();
        consumed = reader.consumeLetterSequence();
        assertEquals("defghijklmnopqrstuvwxyz", consumed);

        reader.advanceToEnd();
        consumed = reader.consumeLetterSequence();
        assertEquals("", consumed);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        String input = "abc123def456ghi";
        CharacterReader reader = new CharacterReader(input);
        String consumed = reader.consumeLetterThenDigitSequence();
        assertEquals("abc123", consumed);

        reader.advance();
        consumed = reader.consumeLetterThenDigitSequence();
        assertEquals("def456", consumed);

        reader.advanceToEnd();
        consumed = reader.consumeLetterThenDigitSequence();
        assertEquals("", consumed);
    }

    @Test
    public void testConsumeHexSequence() {
        String input = "abcdefghijklmnopqrstuvwxyzABCDEF";
        CharacterReader reader = new CharacterReader(input);
        String consumed = reader.consumeHexSequence();
        assertEquals("abcdef", consumed);

        reader.advance();
        consumed = reader.consumeHexSequence();
        assertEquals("ABCDEF", consumed);

        reader.advanceToEnd();
        consumed = reader.consumeHexSequence();
        assertEquals("", consumed);
    }

    @Test
    public void testConsumeDigitSequence() {
        String input = "12345678901234567890";
        CharacterReader reader = new CharacterReader(input);
        String consumed = reader.consumeDigitSequence();
        assertEquals("12345678901234567890", consumed);

        reader.advance();
        consumed = reader.consumeDigitSequence();
        assertEquals("", consumed);

        reader.advanceToEnd();
        consumed = reader.consumeDigitSequence();
        assertEquals("", consumed);
    }

    @Test
    public void testMatches() {
        String input = "abcdefghijklmnopqrstuvwxyz";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches("a"));
        assertFalse(reader.matches("b"));
        assertFalse(reader.matches("1"));
        assertFalse(reader.matches('\u007F'));

        reader.advance();
        assertTrue(reader.matches('b'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        String input = "abcdefghijklmnopqrstuvwxyz";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchesIgnoreCase("a"));
        assertTrue(reader.matchesIgnoreCase("ABC"));
        assertFalse(reader.matchesIgnoreCase("b"));
        assertFalse(reader.matchesIgnoreCase("1"));
        assertFalse(reader.matchesIgnoreCase("\u007F"));

        reader.advance();
        assertTrue(reader.matchesIgnoreCase("b"));
    }

    @Test
    public void testMatchesAny() {
        String input = "abcdefghijklmnopqrstuvwxyz";
        CharacterReader reader = new CharacterReader(input);
        assertFalse(reader.matchesAny('\u007F'));
        assertTrue(reader.matchesAny('a', 'z'));
        assertTrue(reader.matchesAny('a', 'b', 'z'));
        assertFalse(reader.matchesAny('x', 'y'));

        reader.advance();
        assertTrue(reader.matchesAny('b', 'z'));
        assertFalse(reader.matchesAny('x', 'y'));
    }

    @Test
    public void testMatchesLetter() {
        String input = "abcdefghijklmnopqrstuvwxyz";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());

        reader.advance();
        assertFalse(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        String input = "12345678901234567890";
        CharacterReader reader = new CharacterReader(input);
        assertFalse(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());

        reader.advance();
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume() {
        String input = "abcdefghijklmnopqrstuvwxyz";
        CharacterReader reader = new CharacterReader(input);
        String matched = reader.matchConsume("a");
        assertEquals("a", matched);
        assertEquals('a', reader.current());

        reader.advance();
        matched = reader.matchConsume("b");
        assertEquals("b", matched);
        assertEquals('b', reader.current());

        reader.advanceToEnd();
        assertNull(reader.matchConsume("a"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        String input = "abcdefghijklmnopqrstuvwxyz";
        CharacterReader reader = new CharacterReader(input);
        String matched = reader.matchConsumeIgnoreCase("A");
        assertEquals("a", matched);
        assertEquals('a', reader.current());

        reader.advance();
        matched = reader.matchConsumeIgnoreCase("B");
        assertEquals("b", matched);
        assertEquals('b', reader.current());

        reader.advanceToEnd();
        assertNull(reader.matchConsumeIgnoreCase("A"));
    }

    @Test
    public void testContainsIgnoreCase() {
        String input = "<html><head></head><body><title>Test</title></body></html>";
        CharacterReader reader = new CharacterReader(input);
        assertTrue(reader.containsIgnoreCase("/title>"));
        assertTrue(reader.containsIgnoreCase("/TITLE>"));
        assertFalse(reader.containsIgnoreCase("/body>"));
        assertFalse(reader.containsIgnoreCase("/HEAD>"));
    }

    @Test
    public void testToString() {
        String input = "abcdefghijklmnopqrstuvwxyz";
        CharacterReader reader = new CharacterReader(input);
        assertEquals("abcdefghijklmnopqrstuvwxyz", reader.toString());

        reader.advance();
        assertEquals("bcdefghijklmnopqrstuvwxyz", reader.toString());

        reader.advanceToEnd();
        assertEquals("", reader.toString());
    }
}