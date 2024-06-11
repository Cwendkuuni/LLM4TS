package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
        assertEquals('d', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals('f', reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());

        reader = new CharacterReader("");
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());

        reader = new CharacterReader("abcdef");
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals('b', reader.current());
        reader.consume();
        assertEquals('c', reader.current());
        // EOF is not equal to any character
        assertNotEquals(CharacterReader.EOF, 'a');
        assertNotEquals(CharacterReader.EOF, 'b');
        assertNotEquals(CharacterReader.EOF, 'c');
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals(1, reader.nextIndexOf('b'));
        assertEquals(-1, reader.nextIndexOf('x'));
        reader.consume();
        reader.consume();
        assertEquals(4, reader.nextIndexOf('e'));
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("bc", reader.consumeAsString());
        reader.consume();
        reader.consume();
        assertEquals("ef", reader.consumeAsString());
        reader.consume();
        assertEquals("", reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("b", reader.consumeTo('c').toString());
        assertEquals("d e f", reader.consumeToEnd().toString());
        reader = new CharacterReader("");
        assertEquals("", reader.consumeToEnd().toString());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("bc", reader.consumeToAny('c').toString());
        assertEquals("def", reader.consumeToAny(new char[]{'d', 'e', 'f'}).toString());
        reader = new CharacterReader("abc");
        assertEquals("", reader.consumeToAny(new char[]{'d'}).toString());
        reader = new CharacterReader("");
        assertEquals("", reader.consumeToAny(new char[0]).toString());
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("bc", reader.consumeToAnySorted(new char[]{'b', 'c'}).toString());
        assertEquals("def", reader.consumeToAnySorted(new char[]{'d', 'e', 'f'}).toString());
        reader = new CharacterReader("abc");
        assertEquals("", reader.consumeToAnySorted(new char[]{'d'}).toString());
        reader = new CharacterReader("");
        assertEquals("", reader.consumeToAnySorted(new char[0]).toString());
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader("&<null");
        assertEquals("&", reader.consumeData().toString());
        reader.consume();
        assertEquals("<", reader.consumeData().toString());
        reader.consume();
        assertEquals("null", reader.consumeData().toString());
        reader = new CharacterReader("");
        assertEquals("", reader.consumeData().toString());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader("\t<title>Test</title>\r\n");
        assertEquals("\t<title>", reader.consumeTagName().toString());
        reader.consumeLetterSequence();
        reader.consumeLetterThenDigitSequence();
        assertEquals("Test", reader.consumeTagName().toString());
        reader = new CharacterReader("");
        assertEquals("", reader.consumeTagName().toString());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("abcdef", reader.consumeToEnd().toString());
        reader = new CharacterReader("");
        assertEquals("", reader.consumeToEnd().toString());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader("abCdEf");
        assertEquals("abC", reader.consumeLetterSequence().toString());
        reader.consume();
        assertEquals("dEf", reader.consumeLetterSequence().toString());
        reader = new CharacterReader("123");
        assertEquals("", reader.consumeLetterSequence().toString());
        reader = new CharacterReader("");
        assertEquals("", reader.consumeLetterSequence().toString());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader("abC1dE2f");
        assertEquals("abC1", reader.consumeLetterThenDigitSequence().toString());
        reader.consumeLetterSequence();
        reader.consumeLetter();
        reader.consumeDigitSequence();
        assertEquals("dE2f", reader.consumeLetterThenDigitSequence().toString());
        reader = new CharacterReader("123");
        assertEquals("123", reader.consumeLetterThenDigitSequence().toString());
        reader = new CharacterReader("");
        assertEquals("", reader.consumeLetterThenDigitSequence().toString());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader("&#x61; &#x62; &#x63;");
        assertEquals("\u0061", reader.consumeHexSequence().toString());
        reader.consume();
        assertEquals("\u0062", reader.consumeHexSequence().toString());
        reader.consume();
        assertEquals("\u0063", reader.consumeHexSequence().toString());
        reader = new CharacterReader("");
        assertEquals("", reader.consumeHexSequence().toString());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader("1234567890");
        assertEquals("12345", reader.consumeDigitSequence().toString());
        reader.consumeDigitSequence();
        reader.consumeDigitSequence();
        assertEquals("67890", reader.consumeDigitSequence().toString());
        reader = new CharacterReader("");
        assertEquals("", reader.consumeDigitSequence().toString());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('b'));
        assertTrue(reader.matches('c'));
        assertTrue(reader.matches('d'));
        assertTrue(reader.matches('e'));
        assertTrue(reader.matches('f'));
        assertFalse(reader.matches('g'));
        reader = new CharacterReader("");
        assertFalse(reader.matches('a'));
    }

    @Test
    public void testMatchesSequence() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matches("ab"));
        assertTrue(reader.matches("cd"));
        assertTrue(reader.matches("ef"));
        assertFalse(reader.matches("gh"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matchesIgnoreCase("ab"));
        assertTrue(reader.matchesIgnoreCase("cd"));
        assertTrue(reader.matchesIgnoreCase("ef"));
        assertFalse(reader.matchesIgnoreCase("gh"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matchesAny('a', 'b', 'c'));
        assertTrue(reader.matchesAny('c', 'd'));
        assertTrue(reader.matchesAny('e', 'f'));
        assertFalse(reader.matchesAny('g', 'h'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matchesAnySorted('a', 'b', 'c'));
        assertTrue(reader.matchesAnySorted('c', 'd'));
        assertTrue(reader.matchesAnySorted('e', 'f'));
        assertFalse(reader.matchesAnySorted('g', 'h'));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertTrue(reader.matchesLetter());
        reader.consume();
        assertTrue(reader.matchesLetter());
        reader.consume();
        assertTrue(reader.matchesLetter());
        reader.consume();
        assertTrue(reader.matchesLetter());
        reader.consume();
        assertTrue(reader.matchesLetter());
        reader.consume();
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader("1234567890");
        assertTrue(reader.matchesDigit());
        reader.consumeDigitSequence();
        assertTrue(reader.matchesDigit());
        reader.consumeDigitSequence();
        assertTrue(reader.matchesDigit());
        reader.consumeDigitSequence();
        assertTrue(reader.matchesDigit());
        reader.consumeDigitSequence();
        assertTrue(reader.matchesDigit());
        reader.consumeDigitSequence();
        assertFalse(reader.matchesDigit());
    }
}