package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderCreation() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        assertEquals("Hello World", reader.toString());
    }

    @Test
    public void testCharacterReaderBufferUp() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        reader.bufferUp();
        assertEquals(0, reader.bufPos);
        assertEquals(11, reader.readerPos);

        reader.consume();
        reader.bufferUp();
        assertEquals(1, reader.bufPos);
        assertEquals(12, reader.readerPos);

        reader.consume(2);
        reader.bufferUp();
        assertEquals(3, reader.bufPos);
        assertEquals(14, reader.readerPos);
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World")) {
            @Override
            public void bufferUp() throws IOException {
                throw new IOException("Test IOException");
            }
        };

        reader.bufferUp();
    }

    @Test
    public void testCharacterReaderPos() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        assertEquals(0, reader.pos());
        reader.consume();
        reader.consume();
        reader.consume();
        reader.advance();
        reader.advance();
        assertEquals(7, reader.pos());
    }

    @Test
    public void testCharacterReaderIsEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        assertFalse(reader.isEmpty());

        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderCurrent() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        assertEquals('H', reader.current());
        reader.consume();
        assertEquals('e', reader.current());
        reader.consume();
        assertEquals('l', reader.current());
        reader.consume();
        assertEquals('l', reader.current());
        reader.consume();
        assertEquals('o', reader.current());
        reader.consume();
        assertEquals(' ', reader.current());
        reader.consume();
        assertEquals('W', reader.current());
        reader.consume();
        assertEquals('r', reader.current());
        reader.consume();
        assertEquals('d', reader.current());
        reader.consume();
        assertEquals('\n', reader.current());
        reader.consume();
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testCharacterReaderConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        reader.consume();
        assertEquals('e', reader.current());

        reader.consume(2);
        assertEquals('l', reader.current());
        assertEquals('l', reader.current());

        reader.unconsume();
        assertEquals('o', reader.current());
    }

    @Test
    public void testCharacterReaderRewindToMark() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        reader.mark();
        reader.consume();
        reader.consume();
        reader.rewindToMark();
        assertEquals('H', reader.current());
    }

    @Test
    public void testCharacterReaderNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        assertEquals(-1, reader.nextIndexOf('x'));
        reader.consume();
        reader.consume();
        assertEquals(0, reader.nextIndexOf('l'));
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        assertEquals(12, reader.nextIndexOf('\n'));
    }

    @Test
    public void testCharacterReaderNextIndexOfWithSubsequence() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        assertEquals(-1, reader.nextIndexOf("abcd"));
        reader.consume();
        reader.consume();
        assertEquals(-1, reader.nextIndexOf("llll"));
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        assertEquals(11, reader.nextIndexOf("World"));
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        String result = reader.consumeTo('l');
        assertEquals("He", result);
        reader.consume();
        reader.consume();
        reader.consume();
        result = reader.consumeTo('l');
        assertEquals("llo", result);
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consumeToEnd();
        assertEquals("", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReaderConsumeToWithString() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        String result = reader.consumeTo("llo");
        assertEquals("Hello", result);
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        String result = reader.consumeToAny(new char[]{'l', 'l', 'o'});
        assertEquals("llo", result);
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        result = reader.consumeToAny(new char[]{'H', 'e'});
        assertEquals("llo", result);
    }

    @Test
    public void testCharacterReaderConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        String result = reader.consumeToAnySorted(new char[]{'l', 'l', 'o'});
        assertEquals("llo", result);
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        result = reader.consumeToAnySorted(new char[]{'H', 'e'});
        assertEquals("llo", result);
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        String result = reader.consumeData();
        assertEquals("&<null", result);
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello <html>World"));
        String result = reader.consumeTagName();
        assertEquals("html", result);
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        String result = reader.consumeToEnd();
        assertEquals("World", result);
    }

    @Test
    public void testCharacterReaderConsumeLetterSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        String result = reader.consumeLetterSequence();
        assertEquals("Hello", result);
    }

    @Test
    public void testCharacterReaderConsumeLetterThenDigitSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello123 World"));
        String result = reader.consumeLetterThenDigitSequence();
        assertEquals("Hello123", result);
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(reader.matches('l'));
        assertTrue(reader.matches('l'));
        assertTrue(reader.matches('o'));
        assertFalse(reader.matches('H'));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(reader.matchesIgnoreCase("World"));
        assertTrue(reader.matchesIgnoreCase("world"));
        assertFalse(reader.matchesIgnoreCase("Hello"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(reader.matchesAny('l'));
        assertTrue(reader.matchesAny('o'));
        assertTrue(reader.matchesAny('W'));
        assertFalse(reader.matchesAny('x'));
    }

    @Test
    public void testCharacterReaderMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        assertTrue(reader.matchesAnySorted('l'));
        assertTrue(reader.matchesAnySorted('o'));
        assertTrue(reader.matchesAnySorted('W'));
        assertFalse(reader.matchesAnySorted('x'));
    }
}