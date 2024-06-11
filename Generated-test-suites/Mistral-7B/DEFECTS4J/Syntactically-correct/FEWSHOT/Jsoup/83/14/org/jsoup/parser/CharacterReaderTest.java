package org.jsoup.parser;

import static org.junit.Assert.*;

import java.io.IOException;

import org.jsoup.UncheckedIOException;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader_constructor_Reader() {
        Reader input = new StringReader("test");
        CharacterReader reader = new CharacterReader(input);
        assertEquals(input, reader.reader);
    }

    @Test
    public void testCharacterReader_constructor_String() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        assertEquals("test", reader.consumeToEnd());
    }

    @Test
    public void testBufferUp() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        reader.bufferUp();
        assertEquals(0, reader.bufPos);
        assertEquals(4, reader.pos());
    }

    @Test
    public void testPos() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        reader.advance();
        assertEquals(1, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        assertFalse(reader.isEmpty());
        reader.rewindToMark();
        reader.unconsume();
        assertTrue(reader.isEmptyNoBufferUp());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        reader.advance();
        assertEquals('t', reader.current());
        reader.unconsume();
        assertEquals('t', reader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testConsume_IOException() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("test")) {
            @Override
            protected void bufferUp() throws IOException {
                throw new IOException();
            }
        };
        reader.consume();
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        assertEquals('t', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals('s', reader.consume());
        assertEquals('t', reader.consume());
        assertEquals('', reader.consume());
        assertTrue(reader.isEmptyNoBufferUp());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        reader.advance();
        reader.advance();
        reader.unconsume();
        assertEquals('t', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        assertEquals(5, reader.pos());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        assertEquals('t', reader.current());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        assertEquals(1, reader.nextIndexOf('e'));
        assertEquals(2, reader.nextIndexOf('s'));
        assertEquals(-1, reader.nextIndexOf('z'));
        assertEquals(4, reader.nextIndexOf('\n'));
    }

    @Test
    public void testNextIndexOf_CharSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        assertEquals(1, reader.nextIndexOf("est"));
        assertEquals(2, reader.nextIndexOf("es"));
        assertEquals(-1, reader.nextIndexOf("foo"));
        assertEquals(4, reader.nextIndexOf("\n"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        assertEquals("est", reader.consumeTo('s'));
        assertEquals("test", reader.consumeToEnd());
    }

    @Test
    public void testConsumeTo_String() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        assertEquals("est", reader.consumeTo("es"));
        assertEquals("test", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        assertEquals("es", reader.consumeToAny(new char[]{'s'}));
        assertEquals("test", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        assertEquals("es", reader.consumeToAnySorted(new char[]{'s'}));
        assertEquals("test", reader.consumeToEnd());
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&lt;foo&gt;"));
        assertEquals("&lt;foo&gt;", reader.consumeData());
        assertTrue(reader.isEmptyNoBufferUp());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n</body>\n</html>"));
        assertEquals("<html>\n<head>\n<title>", reader.consumeTagName());
        assertEquals("</title>\n<body>\n</body>\n</html>", reader.consumeToEnd());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        assertEquals("test", reader.consumeToEnd());
        assertTrue(reader.isEmptyNoBufferUp());
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abcde"));
        assertEquals("abcde", reader.consumeLetterSequence());
        assertTrue(reader.isEmptyNoBufferUp());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("abc123def456"));
        assertEquals("abc123def456", reader.consumeLetterThenDigitSequence());
        assertTrue(reader.isEmptyNoBufferUp());
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("&x3bc;&x3b9;&x3b5;"));
        assertEquals("πμπ", reader.consumeHexSequence());
        assertTrue(reader.isEmptyNoBufferUp());
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("1234567890"));
        assertEquals("1234567890", reader.consumeDigitSequence());
        assertTrue(reader.isEmptyNoBufferUp());
    }

    @Test
    public void testMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        assertTrue(reader.matches('t'));
        assertTrue(reader.matches("es"));
        assertFalse(reader.matches('z'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        assertTrue(reader.matchesIgnoreCase("es"));
        assertTrue(reader.matchesIgnoreCase("Test"));
        assertFalse(reader.matchesIgnoreCase("Z"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        assertTrue(reader.matchesAny('t'));
        assertTrue(reader.matchesAny('s'));
        assertFalse(reader.matchesAny('z'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        assertTrue(reader.matchesAnySorted(new char[]{'s', 't'}));
        assertFalse(reader.matchesAnySorted(new char[]{'a', 'z'}));
    }

    @Test
    public void testMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter('d'));
        assertFalse(reader.matchesLetter('0'));
    }

    @Test
    public void testMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("12345"));
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matchesDigit('5'));
        assertFalse(reader.matchesDigit('a'));
    }
}