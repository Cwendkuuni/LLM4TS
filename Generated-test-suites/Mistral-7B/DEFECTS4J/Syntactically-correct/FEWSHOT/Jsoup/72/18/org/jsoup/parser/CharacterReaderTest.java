package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.UncheckedIOException;

import java.io.IOException;
import java.util.Arrays;

public class CharacterReaderTest {

    private CharacterReader reader;

    @Before
    public void setup() {
        String input = "abcd1234efghABC5678";
        reader = new CharacterReader(new StringReader(input));
    }

    @Test
    public void testIsNull() {
        assertNull(new CharacterReader(null));
    }

    @Test
    public void testIsEmpty() {
        assertEquals(0, reader.pos());
        assertTrue(reader.isEmpty());

        reader.bufferUp();
        reader.consume();
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testPos() {
        assertEquals(0, reader.pos());

        reader.consume();
        assertEquals(1, reader.pos());

        reader.consumeTo('d');
        assertEquals(4, reader.pos());

        reader.rewindToMark();
        assertEquals(0, reader.pos());
    }

    @Test
    public void testConsume() {
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
        assertEquals('d', reader.consume());
        assertEquals('1', reader.consume());
        assertEquals('2', reader.consume());
        assertEquals('3', reader.consume());
        assertEquals('4', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals('f', reader.consume());
        assertEquals('g', reader.consume());
        assertEquals('h', reader.consume());
        assertEquals('A', reader.consume());
        assertEquals('B', reader.consume());
        assertEquals('C', reader.consume());
        assertEquals('5', reader.consume());
        assertEquals('6', reader.consume());
        assertEquals('7', reader.consume());
        assertEquals('8', reader.consume());
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testConsumeTo() {
        String expected = "d1234";
        String result = reader.consumeTo('d');
        assertEquals(expected, result);

        expected = "1234efghABC5678";
        result = reader.consumeToEnd();
        assertEquals(expected, result);

        expected = "efghABC";
        result = reader.consumeTo("efghABC");
        assertEquals(expected, result);
    }

    @Test
    public void testNextIndexOf() {
        assertEquals(4, reader.nextIndexOf('d'));
        assertEquals(-1, reader.nextIndexOf('z'));
        assertEquals(7, reader.nextIndexOf("efgh"));
        assertEquals(-1, reader.nextIndexOf("xyz"));
    }

    @Test
    public void testNextIndexOfWithTrailingNull() {
        String input = "abcd1234efghABC5678\u0000";
        CharacterReader reader = new CharacterReader(new StringReader(input));
        assertEquals(11, reader.nextIndexOf("\u0000"));
    }

    @Test
    public void testConsumeToAny() {
        String expected = "d1234";
        String result = reader.consumeToAny(new char[]{'d'});
        assertEquals(expected, result);

        expected = "1234efghABC5678";
        result = reader.consumeToAny(new char[]{});
        assertEquals(expected, result);
    }

    @Test
    public void testConsumeToAnySorted() {
        String expected = "5678";
        String result = reader.consumeToAnySorted(new char[]{'5', '6', '7', '8'});
        assertEquals(expected, result);
    }

    @Test
    public void testConsumeData() {
        String expected = "abcd1234efghABC5678";
        String result = reader.consumeData();
        assertEquals(expected, result);
    }

    @Test
    public void testConsumeTagName() {
        String expected = "abcd";
        String result = reader.consumeTagName();
        assertEquals(expected, result);
    }

    @Test
    public void testConsumeLetterSequence() {
        String expected = "abcd";
        String result = reader.consumeLetterSequence();
        assertEquals(expected, result);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        String expected = "d1234";
        String result = reader.consumeLetterThenDigitSequence();
        assertEquals(expected, result);
    }

    @Test
    public void testConsumeHexSequence() {
        String expected = "1234";
        String result = reader.consumeHexSequence();
        assertEquals(expected, result);
    }

    @Test
    public void testConsumeDigitSequence() {
        String expected = "1234";
        String result = reader.consumeDigitSequence();
        assertEquals(expected, result);
    }

    @Test
    public void testMatches() {
        assertTrue(reader.matches('d'));
        assertFalse(reader.matches('z'));
    }

    @Test
    public void testMatchesSequence() {
        assertTrue(reader.matches("efgh"));
        assertFalse(reader.matches("xyz"));
    }

    @Test
    public void testMatchesIgnoreCase() {
        assertTrue(reader.matchesIgnoreCase("EFGH"));
        assertFalse(reader.matchesIgnoreCase("xyz"));
    }

    @Test
    public void testMatchesAny() {
        assertTrue(reader.matchesAny('d'));
        assertFalse(reader.matchesAny('z'));
    }

    @Test
    public void testMatchesAnySorted() {
        assertTrue(reader.matchesAnySorted(new char[]{'5', '6', '7', '8'}));
        assertFalse(reader.matchesAnySorted(new char[]{'1', '2', '3'}));
    }

    @Test
    public void testMatchesLetter() {
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter('2'));
    }

    @Test
    public void testMatchesDigit() {
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesDigit('a'));
    }

    @Test
    public void testMatchConsume() {
        String expected = "efghABC";
        reader.mark();
        String result = reader.consumeTo("efgh");
        assertEquals(expected, result);
        assertEquals(4, reader.pos());
        reader.rewindToMark();
        assertEquals(0, reader.pos());
    }

    @Test
    public void testMatchConsumeIgnoreCase() {
        String expected = "efghABC";
        reader.mark();
        String result = reader.consumeToIgnoreCase("EFGH");
        assertEquals(expected, result);
        assertEquals(4, reader.pos());
        reader.rewindToMark();
        assertEquals(0, reader.pos());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader(""), 1);
        try {
            reader.bufferUp();
        } finally {
            reader.close();
        }
    }
}