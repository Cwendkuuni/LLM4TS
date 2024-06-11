package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderWithReader() {
        char[] charBuf = new char[100];
        CharacterReader reader = new CharacterReader(new StringReader("test"), charBuf.length);
        int pos = reader.pos();
        assertEquals(0, pos);

        reader.consume();
        char c = reader.current();
        assertEquals('t', c);
        assertEquals(1, pos);

        reader.advance();
        c = reader.current();
        assertEquals('e', c);
        assertEquals(2, pos);

        reader.unconsume();
        assertEquals('e', reader.current());

        reader.mark();
        reader.rewindToMark();
        assertEquals('e', reader.current());

        reader.advance();
        reader.consumeTo('s');
        String consumed = reader.consumeToEnd();
        assertEquals("est", consumed);
        assertEquals(6, pos);
        assertEquals("test", reader.toString());
    }

    @Test
    public void testCharacterReaderWithString() {
        CharacterReader reader = new CharacterReader(new StringReader("test"), 100);
        CharacterReader stringReader = new CharacterReader("test", 5);
        assertEquals(reader.toString(), stringReader.toString());

        reader.consume();
        char c = reader.current();
        assertEquals('t', c);

        reader.advance();
        c = reader.current();
        assertEquals('e', c);

        reader.consumeTo('s');
        String consumed = reader.consumeToEnd();
        assertEquals("est", consumed);

        assertEquals(5, reader.pos());
        assertEquals("test", reader.toString());

        assertFalse(reader.isEmpty());
        reader.rewindToMark();
        assertTrue(reader.isEmptyNoBufferUp());
        reader.bufferUp();
        assertFalse(reader.isEmptyNoBufferUp());
    }

    @Test
    public void testCharacterReaderMethods() {
        CharacterReader reader = new CharacterReader(new StringReader("test"), 100);

        reader.consume();
        reader.consume();
        reader.advance();
        reader.advance();

        assertEquals(4, reader.pos());
        assertEquals(4, reader.nextIndexOf('t'));
        assertEquals(-1, reader.nextIndexOf('z'));

        reader.consumeTo('s');
        String consumed = reader.consumeToEnd();
        assertEquals("st", consumed);

        reader.consumeToAny(new char[] {'t', 's'});
        consumed = reader.consumeToEnd();
        assertEquals("test", consumed);

        reader.consumeData();
        consumed = reader.consumeToEnd();
        assertEquals("test", consumed);

        reader.consumeTagName();
        consumed = reader.consumeToEnd();
        assertEquals("test", consumed);

        reader.consumeToEnd();
        consumed = reader.consumeToEnd();
        assertEquals("", consumed);

        reader.bufferUp();
        reader.bufferUp();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();

        assertEquals(8, reader.pos());
        assertEquals("testest", reader.toString());

        reader.mark();
        reader.consume();
        reader.rewindToMark();
        reader.consume();
        reader.consume();

        assertEquals(6, reader.pos());
        assertEquals("testes", reader.toString());
    }

    @Test
    public void testCharacterReaderStringCache() {
        CharacterReader reader = new CharacterReader(new StringReader("testtesttesttest"), 100);
        reader.consumeTo('t');
        String a = reader.consumeToEnd();
        reader.consumeTo('t');
        String b = reader.consumeToEnd();

        assertEquals(a, b);
        assertEquals("testtesttesttest", reader.toString());

        reader.consumeTo('t');
        String c = reader.consumeToEnd();
        reader.consumeTo('t');
        String d = reader.consumeToEnd();

        assertFalse(a.equals(c));
        assertFalse(b.equals(d));
        assertFalse(a.equals(d));
        assertFalse(b.equals(c));
    }

    @Test
    public void testCharacterReaderLimit() {
        CharacterReader reader = new CharacterReader(new StringReader("123456789012345678901234567890"), 10);
        reader.consumeTo('3');
        String a = reader.consumeToEnd();
        assertEquals("34567890", a);
        assertEquals(10, reader.pos());
        assertEquals("123", reader.toString());

        reader.bufferUp();
        reader.consumeTo('6');
        String b = reader.consumeToEnd();
        assertEquals("67890", b);
        assertEquals(10, reader.pos());
        assertEquals("123", reader.toString());

        reader.consumeTo('9');
        String c = reader.consumeToEnd();
        assertEquals("901234567890", c);
        assertEquals(10, reader.pos());
        assertEquals("123", reader.toString());
    }

    @Test
    public void testCharacterReaderException() {
        CharacterReader reader = new CharacterReader(new StringReader("test"), 100);
        reader.consumeTo('x');
        assertNull(reader.consumeToEnd());
        assertEquals(0, reader.pos());
        assertEquals("test", reader.toString());

        reader.consumeTo('x');
        String consumed = reader.consumeToEnd();
        assertNull(consumed);
        assertEquals(0, reader.pos());
        assertEquals("test", reader.toString());
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("testtesttesttest"), 100);

        reader.consume();
        reader.consume();
        reader.advance();
        reader.advance();

        assertTrue(reader.matches('t'));
        assertTrue(reader.matches('e'));
        assertFalse(reader.matches('z'));
        assertTrue(reader.matches("es"));
        assertFalse(reader.matches("est"));
        assertFalse(reader.matchesIgnoreCase("est"));
        assertTrue(reader.matchesAny(new char[] {'t', 's'}));
        assertTrue(reader.matchesAnySorted(new char[] {'s', 't'}));
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesDigit());
        assertFalse(reader.matchesLetterThenDigitSequence());
        assertFalse(reader.matchesHexSequence());
        assertFalse(reader.matchesDigitSequence());
        assertFalse(reader.matchesIgnoreCase("test"));
        assertFalse(reader.matchesLetterThenDigitSequence());
        assertFalse(reader.matchesHexSequence());
        assertFalse(reader.matchesDigitSequence());
        assertFalse(reader.matches("test"));
        assertFalse(reader.matches("test"));
        assertFalse(reader.matchesIgnoreCase("test"));
        assertFalse(reader.matchesIgnoreCase("test"));
        assertFalse(reader.matches("test"));
        assertFalse(reader.matchesIgnoreCase("test"));
        assertFalse(reader.matches("test"));
        assertFalse(reader.matchesIgnoreCase("test"));
        assertFalse(reader.matches("test"));
        assertFalse(reader.matchesIgnoreCase("test"));
        assertFalse(reader.matches("test"));
        assertFalse(reader.matchesIgnoreCase("test"));
        assertFalse(reader.matches("test"));
        assertFalse(reader.matchesIgnoreCase("test"));
        assertFalse(reader.matches("test"));
        assertFalse(reader.matchesIgnoreCase("test"));
    }
}