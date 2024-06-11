package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        // Test constructor with valid input
        Reader input = new StringReader("test");
        CharacterReader reader = new CharacterReader(input);
        assertEquals(input, reader.getReader());

        // Test constructor with maxBufferLen
        input = new StringReader("test");
        reader = new CharacterReader(input, 10);
        assertEquals(input, reader.getReader());
        assertEquals(10, reader.charBuf.length);

        // Test constructor with String input
        String inputString = "test";
        reader = new CharacterReader(inputString);
        assertEquals(inputString, reader.getReader().toString());

        // Test bufferUp() method
        reader.bufferUp();
        assertEquals(0, reader.bufPos);
        assertEquals(0, reader.bufMark);
        assertEquals(0, reader.bufSplitPoint);
        assertEquals(0, reader.readerPos);
    }

    @Test
    public void testPos() {
        Reader input = new StringReader("test1234");
        CharacterReader reader = new CharacterReader(input);

        assertEquals(0, reader.pos());

        reader.consume();
        reader.consume();
        reader.advance();
        reader.advance();

        assertEquals(4, reader.pos());
    }

    @Test
    public void testIsEmpty() {
        Reader input = new StringReader("");
        CharacterReader reader = new CharacterReader(input);

        assertTrue(reader.isEmpty());

        reader.consume();

        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        Reader input = new StringReader("abcdef");
        CharacterReader reader = new CharacterReader(input);

        reader.advance();
        assertEquals('a', reader.current());

        reader.consume();
        assertEquals('b', reader.current());
    }

    @Test
    public void testConsume() {
        Reader input = new StringReader("abcd");
        CharacterReader reader = new CharacterReader(input);

        assertEquals('a', reader.current());
        reader.consume();
        assertEquals('b', reader.current());
        reader.unconsume();
        assertEquals('a', reader.current());
    }

    @Test
    public void testAdvance() {
        Reader input = new StringReader("abcd");
        CharacterReader reader = new CharacterReader(input);

        reader.advance();
        assertEquals('a', reader.current());
        reader.advance();
        assertEquals('b', reader.current());
    }

    @Test
    public void testMark() {
        Reader input = new StringReader("abcd");
        CharacterReader reader = new CharacterReader(input);

        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();

        assertEquals('a', reader.current());
    }

    @Test
    public void testNextIndexOf() {
        Reader input = new StringReader("abcdef");
        CharacterReader reader = new CharacterReader(input);

        assertEquals(-1, reader.nextIndexOf('x'));
        assertEquals(5, reader.nextIndexOf('e'));
    }

    @Test
    public void testNextIndexOfSequence() {
        Reader input = new StringReader("abcdef");
        CharacterReader reader = new CharacterReader(input);

        assertEquals(-1, reader.nextIndexOf("xyz"));
        assertEquals(5, reader.nextIndexOf("ef"));
    }

    @Test
    public void testConsumeTo() {
        Reader input = new StringReader("abcdef");
        CharacterReader reader = new CharacterReader(input);

        assertEquals("a", reader.consumeTo('b').trim());
        assertEquals("def", reader.consumeToEnd().trim());
        assertEquals("abc", reader.consumeTo("c").trim());
    }

    @Test
    public void testConsumeToSequence() {
        Reader input = new StringReader("abcdef");
        CharacterReader reader = new CharacterReader(input);

        assertEquals("abc", reader.consumeTo("def").trim());
        assertEquals("ef", reader.consumeTo("e").trim());
    }

    @Test
    public void testConsumeToAny() {
        Reader input = new StringReader("abcd1234");
        CharacterReader reader = new CharacterReader(input);

        assertEquals("1", reader.consumeToAny('1').trim());
        assertEquals("234", reader.consumeToAny("34").trim());
        assertEquals("abcd", reader.consumeToAny('a').trim());
    }

    @Test
    public void testConsumeToAnySorted() {
        Reader input = new StringReader("abcd1234");
        CharacterReader reader = new CharacterReader(input);

        assertEquals("1234", reader.consumeToAnySorted(new char[]{'3', '4'}).trim());
        assertEquals("1", reader.consumeToAnySorted(new char[]{'1', '2'}).trim());
        assertEquals("abcd", reader.consumeToAnySorted(new char[]{'a', 'b', 'c'}).trim());
    }

    @Test
    public void testConsumeData() {
        Reader input = new StringReader("&<NULL");
        CharacterReader reader = new CharacterReader(input);

        assertEquals("&<", reader.consumeData().trim());
    }

    @Test
    public void testConsumeTagName() {
        Reader input = new StringReader("<tab> <title> Test </style>");
        CharacterReader reader = new CharacterReader(input);

        assertEquals("tab", reader.consumeTagName().trim());
        assertEquals("title", reader.consumeTagName().trim());
    }

    @Test
    public void testConsumeToEnd() {
        Reader input = new StringReader("<title> Test </style>");
        CharacterReader reader = new CharacterReader(input);

        assertEquals("Test </style>", reader.consumeToEnd().trim());
    }

    @Test
    public void testConsumeLetterSequence() {
        Reader input = new StringReader("AbCdEfGhIjKlMnOpQrStUvWxYz");
        CharacterReader reader = new CharacterReader(input);

        assertEquals("AbCdEfGhIjKlMnOpQrStUvWxYz", reader.consumeLetterSequence().trim());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        Reader input = new StringReader("AbCd123Ef456GhIj789KlMnOp0QrStUVwXyZ");
        CharacterReader reader = new CharacterReader(input);

        assertEquals("AbCd123", reader.consumeLetterThenDigitSequence().trim());
    }

    @Test
    public void testConsumeHexSequence() {
        Reader input = new StringReader("AbCd%25EF%2545GhIj%25789KlMnOp%250QrStUV%25uwXyZ");
        CharacterReader reader = new CharacterReader(input);

        assertEquals("AbCdEF45", reader.consumeHexSequence().trim());
    }

    @Test
    public void testConsumeDigitSequence() {
        Reader input = new StringReader("AbCd1234567890");
        CharacterReader reader = new CharacterReader(input);

        assertEquals("1234567890", reader.consumeDigitSequence().trim());
    }

    @Test
    public void testMatches() {
        Reader input = new StringReader("AbCdEfGhIj");
        CharacterReader reader = new CharacterReader(input);

        assertTrue(reader.matches('e'));
        assertFalse(reader.matches('x'));
    }

    @Test
    public void testMatchesSequence() {
        Reader input = new StringReader("AbCdEfGhIj");
        CharacterReader reader = new CharacterReader(input);

        assertTrue(reader.matches("ef"));
        assertFalse(reader.matches("xy"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        Reader input = new StringReader("AbCdEfGhIj");
        CharacterReader reader = new CharacterReader(input);

        assertTrue(reader.matchesIgnoreCase("ef"));
        assertTrue(reader.matchesIgnoreCase("Ef"));
        assertFalse(reader.matchesIgnoreCase("xy"));
    }

    @Test
    public void testMatchesAny() {
        Reader input = new StringReader("AbCdEfGhIj");
        CharacterReader reader = new CharacterReader(input);

        assertTrue(reader.matchesAny('a', 'e'));
        assertFalse(reader.matchesAny('x', 'e'));
    }

    @Test
    public void testMatchesAnySorted() {
        Reader input = new StringReader("AbCdEfGhIj");
        CharacterReader reader = new CharacterReader(input);

        assertTrue(reader.matchesAnySorted(new char[]{'a', 'e'}));
        assertFalse(reader.matchesAnySorted(new char[]{'x', 'a'}));
    }

    @Test
    public void testMatchesLetter() {
        Reader input = new StringReader("AbCdEfGhIj");
        CharacterReader reader = new CharacterReader(input);

        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter('0'));
    }

    @Test
    public void testMatchesDigit() {
        Reader input = new StringReader("AbCd123456");
        CharacterReader reader = new CharacterReader(input);

        assertFalse(reader.matchesDigit());
        assertTrue(reader.matchesDigit('1'));
    }

    @Test
    public void testMatchConsume() {
        Reader input = new StringReader("AbCdEfGhIj");
        CharacterReader reader = new CharacterReader(input);

        assertTrue(reader.matchConsume("ef"));
        assertFalse(reader.matchConsume("xy"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        Reader input = new StringReader("AbCdEfGhIj");
        CharacterReader reader = new CharacterReader(input);

        assertTrue(reader.matchConsumeIgnoreCase("ef"));
        assertTrue(reader.matchConsumeIgnoreCase("Ef"));
        assertFalse(reader.matchConsumeIgnoreCase("xy"));
    }

    @Test
    public void testContainsIgnoreCase() {
        Reader input = new StringReader("<html><title>Test</title></html>");
        CharacterReader reader = new CharacterReader(input);

        assertTrue(reader.containsIgnoreCase("/title"));
        assertTrue(reader.containsIgnoreCase("/TITLE"));
        assertFalse(reader.containsIgnoreCase("/body"));
    }

    @Test
    public void testToString() {
        Reader input = new StringReader("AbCdEfGhIj");
        CharacterReader reader = new CharacterReader(input);

        assertEquals("AbCdEfGhIj", reader.toString());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() throws IOException {
        Reader input = new StringReader("") {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new IOException();
            }
        };
        CharacterReader reader = new CharacterReader(input);
        reader.bufferUp();
    }
}