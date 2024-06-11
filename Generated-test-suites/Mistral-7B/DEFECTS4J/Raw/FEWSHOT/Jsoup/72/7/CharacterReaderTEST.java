package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.UncheckedIOException;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testBufferUp() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefgh"));
        reader.bufferUp();
        assertEquals(6, reader.pos());
    }

    @Test(expected = UncheckedIOException.class)
    public void testBufferUpIOException() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefgh"));
        reader.bufferUp();
        reader.bufferUp();
        reader.reader.close();
        reader.bufferUp();
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        assertTrue(reader.isEmpty());

        reader = new CharacterReader(new StringReader("abc"));
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals('b', reader.current());
        reader.consume();
        assertEquals('c', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        reader.consume();
        assertEquals('b', reader.current());

        reader = new CharacterReader(new StringReader(""));
        reader.consume();
        assertNull(reader.current());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        reader.consume();
        reader.unconsume();
        assertEquals('a', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        reader.advance();
        assertEquals('b', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("abcabc"));
        assertEquals(1, reader.nextIndexOf('b'));
        assertEquals(4, reader.nextIndexOf('c'));
        assertEquals(-1, reader.nextIndexOf('d'));
    }

    @Test
    public void testNextIndexOfSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcabc"));
        assertEquals(1, reader.nextIndexOf("bc"));
        assertEquals(5, reader.nextIndexOf("abc"));
        assertEquals(-1, reader.nextIndexOf("def"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("abcabc"));
        assertEquals("bc", reader.consumeTo('b'));
        assertEquals("abc", reader.consumeTo('c'));
        assertEquals("", reader.consumeTo('d'));
    }

    @Test
    public void testConsumeToSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcabc"));
        assertEquals("bc", reader.consumeTo("bc"));
        assertEquals("abc", reader.consumeTo("abc"));
        assertEquals("", reader.consumeTo("def"));
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abc123def"));
        assertEquals("123", reader.consumeToAny(new char[]{'1', '2', '3'}));
        assertEquals("def", reader.consumeToAny(new char[]{'d', 'e', 'f'}));
        assertEquals("", reader.consumeToAny(new char[]{'g', 'h'}));
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abc123def"));
        assertEquals("123", reader.consumeToAnySorted(new char[]{'1', '2', '3'}));
        assertEquals("def", reader.consumeToAnySorted(new char[]{'d', 'e', 'f'}));
        assertEquals("", reader.consumeToAnySorted(new char[]{'g', 'h'}));
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&<null"));
        assertEquals("&<", reader.consumeData());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("\t<title>My Title</title>\n"));
        assertEquals("\t<title>", reader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefg"));
        assertEquals("defg", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        assertEquals("opqrstuvwxyz", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abc123def456"));
        assertEquals("56", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('b'));
        assertFalse(reader.matches('c'));
    }

    @Test
    public void testMatchesSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcabc"));
        assertTrue(reader.matches("bc"));
        assertTrue(reader.matches("abc"));
        assertFalse(reader.matches("def"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abcABC"));
        assertTrue(reader.matchesIgnoreCase("ab"));
        assertTrue(reader.matchesIgnoreCase("Ab"));
        assertFalse(reader.matchesIgnoreCase("def"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertTrue(reader.matchesAny('a'));
        assertTrue(reader.matchesAny('b'));
        assertTrue(reader.matchesAny('c'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertTrue(reader.matchesAnySorted(new char[]{'a', 'b', 'c'}));
        assertFalse(reader.matchesAnySorted(new char[]{'d', 'e'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter()); // consume letter
        assertFalse(reader.matchesLetter()); // consume non-letter
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("123"));
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit()); // consume digit
        assertFalse(reader.matchesDigit()); // consume non-digit
    }

    @Test
    public void testMatchConsumeSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcabc"));
        assertTrue(reader.matchConsume("bc"));
        assertTrue(reader.matchConsume("abc"));
        assertFalse(reader.matchConsume("def"));
    }

    @Test
    public void testMatchConsumeIgnoreCaseSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("AbCaBc"));
        assertTrue(reader.matchConsumeIgnoreCase("ab"));
        assertTrue(reader.matchConsumeIgnoreCase("Ab"));
        assertFalse(reader.matchConsumeIgnoreCase("def"));
    }
}