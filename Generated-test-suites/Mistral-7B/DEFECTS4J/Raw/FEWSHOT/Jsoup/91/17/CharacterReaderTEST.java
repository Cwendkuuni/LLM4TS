package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.UncheckedIOException;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testEmptyCharacterReader() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        assertEquals(0, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderWithSingleChar() {
        CharacterReader reader = new CharacterReader(new StringReader("a"));
        assertEquals(1, reader.pos());
        assertFalse(reader.isEmpty());
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals(0, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderWithMultipleChars() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertEquals(3, reader.pos());
        assertFalse(reader.isEmpty());
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals('b', reader.current());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.current());
        assertEquals('c', reader.consume());
        assertEquals(0, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderWithCustomBufferSize() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"), 5);
        assertEquals(3, reader.pos());
        assertFalse(reader.isEmpty());
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals('b', reader.current());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.current());
        assertEquals('c', reader.consume());
        assertEquals(0, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderWithReader() {
        String input = "abc";
        CharacterReader reader = new CharacterReader(new StringReader(input));
        assertEquals(3, reader.pos());
        assertFalse(reader.isEmpty());
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals('b', reader.current());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.current());
        assertEquals('c', reader.consume());
        assertEquals(0, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderWithString() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"), "abc");
        assertEquals(3, reader.pos());
        assertFalse(reader.isEmpty());
        assertEquals('a', reader.current());
        assertEquals('a', reader.consume());
        assertEquals('b', reader.current());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.current());
        assertEquals('c', reader.consume());
        assertEquals(0, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testBufferUp() throws IOException {
        String input = "abc";
        CharacterReader reader = new CharacterReader(new StringReader(input));
        reader.bufferUp();
        reader.bufferUp();
        reader.bufferUp();
        reader.bufferUp();
        reader.bufferUp();
        int posBeforeRead = reader.pos();
        int lengthBeforeRead = reader.bufLength;
        reader.bufferUp();
        int posAfterRead = reader.pos();
        int lengthAfterRead = reader.bufLength;
        assertEquals(posBeforeRead + lengthBeforeRead, posAfterRead + lengthAfterRead);
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        assertTrue(reader.isEmpty());
        reader = new CharacterReader(new StringReader("abc"));
        assertFalse(reader.isEmpty());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("") {
            @Override
            public void close() throws IOException {
                throw new IOException();
            }
        });
        reader.bufferUp();
    }

    @Test
    public void testCurrent() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals('b', reader.current());
        reader.consume();
        assertEquals('c', reader.current());
        reader.consume();
        assertEquals(EOF, reader.current());
    }

    @Test
    public void testConsume() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
        assertEquals(EOF, reader.consume());
        assertEquals(0, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        reader.consume();
        reader.unconsume();
        assertEquals('a', reader.current());
    }

    @Test
    public void testAdvance() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        reader.advance();
        assertEquals('b', reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('a', reader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testInvalidMark() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("abc"));
        reader.rewindToMark();
    }

    @Test
    public void testNextIndexOf() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("abcaabca"));
        assertEquals(1, reader.nextIndexOf('a'));
        assertEquals(4, reader.nextIndexOf('a', 2));
        assertEquals(-1, reader.nextIndexOf('z'));
    }

    @Test
    public void testNextIndexOfWithSequence() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("abcaabca"));
        assertEquals(2, reader.nextIndexOf("ba"));
        assertEquals(-1, reader.nextIndexOf("xyz"));
    }

    @Test
    public void testConsumeTo() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("abcaabca"));
        assertEquals("ba", reader.consumeTo('b'));
        assertEquals("aabca", reader.consumeTo('a'));
        assertEquals("", reader.consumeTo('z'));
    }

    @Test
    public void testConsumeToWithSequence() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("abcaabca"));
        assertEquals("ba", reader.consumeTo("ba"));
        assertEquals("aabca", reader.consumeTo("a"));
        assertEquals("", reader.consumeTo("z"));
    }

    @Test
    public void testConsumeToAny() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("abcaabca"));
        assertEquals("ba", reader.consumeToAny(new char[]{'b'}));
        assertEquals("aabca", reader.consumeToAny(new char[]{ 'a' }));
        assertEquals("", reader.consumeToAny(new char[]{ 'z' }));
    }

    @Test
    public void testConsumeToAnySorted() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("abcaabca"));
        assertEquals("ba", reader.consumeToAnySorted(new char[]{ 'b' }));
        assertEquals("aabca", reader.consumeToAnySorted(new char[]{ 'a' }));
        assertEquals("", reader.consumeToAnySorted(new char[]{ 'z' }));
    }

    @Test
    public void testConsumeData() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("abcaabca&<null"));
        assertEquals("abcaabca&<null", reader.consumeData());
    }

    @Test
    public void testConsumeTagName() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("<tab>\n<title>\nTabbed Title\n</title>"));
        assertEquals("<tab>\n<title>", reader.consumeTagName());
    }

    @Test
    public void testConsumeToEnd() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("abcaabca"));
        assertEquals("abcaabca", reader.consumeToEnd());
    }

    @Test
    public void testConsumeLetterSequence() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("abcaabca"));
        assertEquals("abc", reader.consumeLetterSequence());
    }

    @Test
    public void testConsumeLetterThenDigitSequence() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("abca123abca456"));
        assertEquals("abc123", reader.consumeLetterThenDigitSequence());
    }

    @Test
    public void testConsumeHexSequence() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("abca#xf1#xf2#xf3abca"));
        assertEquals("xf1xf2xf3", reader.consumeHexSequence());
    }

    @Test
    public void testConsumeDigitSequence() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("abca123abca456"));
        assertEquals("123456", reader.consumeDigitSequence());
    }

    @Test
    public void testMatches() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("abcaabca"));
        assertTrue(reader.matches('a'));
        assertFalse(reader.matches('z'));
    }

    @Test
    public void testMatchesSequence() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("abcaabca"));
        assertTrue(reader.matches("ba"));
        assertFalse(reader.matches("xyz"));
    }

    @Test
    public void testMatchesIgnoreCase() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("ABCaABCa"));
        assertTrue(reader.matchesIgnoreCase("ba"));
        assertFalse(reader.matchesIgnoreCase("XYZ"));
    }

    @Test
    public void testMatchesAny() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("abcaabca"));
        assertTrue(reader.matchesAny('a'));
        assertTrue(reader.matchesAny('b'));
        assertFalse(reader.matchesAny('z'));
    }

    @Test
    public void testMatchesAnySorted() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("abcaabca"));
        assertTrue(reader.matchesAnySorted(new char[]{ 'a' }));
        assertTrue(reader.matchesAnySorted(new char[]{ 'b' }));
        assertFalse(reader.matchesAnySorted(new char[]{ 'z' }));
    }

    @Test
    public void testMatchesLetter() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("abcaabca"));
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesLetter('1'));
    }

    @Test
    public void testMatchesDigit() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("12345abca"));
        assertFalse(reader.matchesDigit());
        assertTrue(reader.matchesDigit('5'));
    }

    @Test
    public void testMatchConsume() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("abcaabca"));
        assertTrue(reader.matchConsume("ba"));
        assertFalse(reader.matchConsume("xyz"));
    }

    @Test
    public void testMatchConsumeIgnoreCase() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("ABCaABCa"));
        assertTrue(reader.matchConsumeIgnoreCase("ba"));
        assertFalse(reader.matchConsumeIgnoreCase("XYZ"));
    }

    @Test
    public void testContainsIgnoreCase() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("<tab>\n<title>\nTabbed Title\n</title>"));
        assertTrue(reader.containsIgnoreCase("/title"));
        assertFalse(reader.containsIgnoreCase("/head"));
    }
}