package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

import static org.jsoup.parser.CharacterReader.EOF;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructorWithReader() {
        Reader input = new StringReader("abcdef");
        CharacterReader reader = new CharacterReader(input);

        assertEquals("abcdef", reader.toString());
        assertEquals(0, reader.pos());
        assertEquals(false, reader.isEmpty());
        assertEquals(EOF, reader.current());
    }

    @Test
    public void testCharacterReaderConstructorWithString() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdef"), "abcdef".length());

        assertEquals("abcdef", reader.toString());
        assertEquals(0, reader.pos());
        assertEquals(false, reader.isEmpty());
        assertEquals(EOF, reader.current());
    }

    @Test
    public void testBufferUp() {
        Reader input = new StringReader("abcdef");
        CharacterReader reader = new CharacterReader(input);

        reader.bufferUp();
        assertEquals(5, reader.pos());
        assertEquals(false, reader.isEmpty());
        assertEquals('a', reader.current());

        reader.bufferUp();
        assertEquals(10, reader.pos());
        assertEquals(false, reader.isEmpty());
        assertEquals('b', reader.current());
    }

    @Test
    public void testPos() {
        Reader input = new StringReader("abcdef");
        CharacterReader reader = new CharacterReader(input);

        reader.bufferUp();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.mark();
        reader.rewindToMark();

        assertEquals(3, reader.pos());
        assertEquals('c', reader.current());
    }

    @Test
    public void testIsEmpty() {
        Reader input = new StringReader("abcdef");
        CharacterReader reader = new CharacterReader(input);

        assertEquals(false, reader.isEmpty());
        reader.consumeToEnd();
        assertEquals(true, reader.isEmpty());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() throws IOException {
        Reader input = new StringReader("abcdef") {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new IOException("Test IOException");
            }
        };
        CharacterReader reader = new CharacterReader(input);

        reader.bufferUp();
        reader.consume();

        // This line should throw an UncheckedIOException
        reader.consume();
    }

    @Test
    public void testConsume() {
        Reader input = new StringReader("abcdef");
        CharacterReader reader = new CharacterReader(input);

        assertEquals(EOF, reader.consume());
        assertEquals(0, reader.pos());
        assertEquals(true, reader.isEmpty());

        reader.bufferUp();
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals('b', reader.current());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.current());
        assertEquals(4, reader.pos());
        assertEquals(false, reader.isEmpty());
    }

    @Test
    public void testConsumeTo() {
        Reader input = new StringReader("abcdabcdabcd");
        CharacterReader reader = new CharacterReader(input);

        String s = reader.consumeTo('c');
        assertEquals("ab", s);
        assertEquals(3, reader.pos());
        assertEquals('d', reader.current());

        s = reader.consumeTo('d');
        assertEquals("abc", s);
        assertEquals(4, reader.pos());
        assertEquals('a', reader.current());

        s = reader.consumeToEnd();
        assertEquals("abcdabcdabcd", s);
        assertEquals(21, reader.pos());
        assertEquals(false, reader.isEmpty());
    }

    @Test
    public void testConsumeToAny() {
        Reader input = new StringReader("1234567890");
        CharacterReader reader = new CharacterReader(input);

        String s = reader.consumeToAny(new char[]{'1', '3', '5'});
        assertEquals("135", s);
        assertEquals(6, reader.pos());
        assertEquals('6', reader.current());

        s = reader.consumeToAny(new char[]{'3'});
        assertEquals("13", s);
        assertEquals(3, reader.pos());
        assertEquals('3', reader.current());
    }

    @Test
    public void testConsumeToAnySorted() {
        Reader input = new StringReader("1234567890");
        CharacterReader reader = new CharacterReader(input);

        String s = reader.consumeToAnySorted(new char[]{'5', '3'});
        assertEquals("53", s);
        assertEquals(6, reader.pos());
        assertEquals('6', reader.current());

        s = reader.consumeToAnySorted(new char[]{'0'});
        assertEquals("", s);
        assertEquals(0, reader.pos());
        assertEquals(EOF, reader.current());
    }

    @Test
    public void testConsumeData() {
        Reader input = new StringReader("&<%nullChar");
        CharacterReader reader = new CharacterReader(input);

        String s = reader.consumeData();
        assertEquals("&<%", s);
        assertEquals(3, reader.pos());
        assertEquals('%', reader.current());
    }

    @Test
    public void testConsumeTagName() {
        Reader input = new StringReader("<tab> <tab>\r\n<tab> <tab>   <tab>");
        CharacterReader reader = new CharacterReader(input);

        String s = reader.consumeTagName();
        assertEquals("<tab>", s);
        assertEquals(5, reader.pos());
        assertEquals('>', reader.current());

        s = reader.consumeTagName();
        assertEquals("<tab>", s);
        assertEquals(5, reader.pos());
        assertEquals('>', reader.current());
    }

    @Test
    public void testConsumeToEnd() {
        Reader input = new StringReader("abcdabcd");
        CharacterReader reader = new CharacterReader(input);

        String s = reader.consumeToEnd();
        assertEquals("abcdabcd", s);
        assertEquals(12, reader.pos());
        assertEquals(false, reader.isEmpty());
    }

    @Test
    public void testConsumeLetterSequence() {
        Reader input = new StringReader("abcABC123DEF");
        CharacterReader reader = new CharacterReader(input);

        String s = reader.consumeLetterSequence();
        assertEquals("abc", s);
        assertEquals(3, reader.pos());
        assertEquals('A', reader.current());

        s = reader.consumeLetterSequence();
        assertEquals("ABC", s);
        assertEquals(4, reader.pos());
        assertEquals('1', reader.current());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        Reader input = new StringReader("abc123ABC123");
        CharacterReader reader = new CharacterReader(input);

        String s = reader.consumeLetterThenDigitSequence();
        assertEquals("abc", s);
        assertEquals(3, reader.pos());
        assertEquals('1', reader.current());

        s = reader.consumeLetterThenDigitSequence();
        assertEquals("123", s);
        assertEquals(4, reader.pos());
        assertEquals('A', reader.current());
    }

    @Test
    public void testMatches() {
        Reader input = new StringReader("abcdefgh");
        CharacterReader reader = new CharacterReader(input);

        assertTrue(reader.matches('a'));
        assertTrue(reader.matches('e'));
        assertFalse(reader.matches('g'));
        assertFalse(reader.matches('z'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        Reader input = new StringReader("abcdefgh");
        CharacterReader reader = new CharacterReader(input);

        assertTrue(reader.matchesIgnoreCase("a"));
        assertTrue(reader.matchesIgnoreCase("e"));
        assertFalse(reader.matchesIgnoreCase("G"));
        assertFalse(reader.matchesIgnoreCase("z"));
    }

    @Test
    public void testMatchesAny() {
        Reader input = new StringReader("abcdefgh");
        CharacterReader reader = new CharacterReader(input);

        assertTrue(reader.matchesAny('a', 'e'));
        assertTrue(reader.matchesAny('a', 'g'));
        assertFalse(reader.matchesAny('i', 'z'));
        assertFalse(reader.matchesAny('z', 'a'));
    }

    @Test
    public void testMatchesAnySorted() {
        Reader input = new StringReader("abcdefgh");
        CharacterReader reader = new CharacterReader(input);

        assertTrue(reader.matchesAnySorted(new char[]{'a', 'e'}));
        assertTrue(reader.matchesAnySorted(new char[]{'a', 'g'}));
        assertFalse(reader.matchesAnySorted(new char[]{'i', 'z'}));
        assertFalse(reader.matchesAnySorted(new char[]{'z', 'a'}));
    }

    @Test
    public void testMatchesLetter() {
        Reader input = new StringReader("abcdefgh");
        CharacterReader reader = new CharacterReader(input);

        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit() {
        Reader input = new StringReader("abcdefgh");
        CharacterReader reader = new CharacterReader(input);

        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
        assertFalse(reader.matchesDigit());
        assertTrue(reader.matchesDigit());
    }

}