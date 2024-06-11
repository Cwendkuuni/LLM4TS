package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.UncheckedIOException;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        CharSequence testString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*()_+{}|:<>?";
        CharacterReader reader = new CharacterReader(new StringReader(testString.toString()));

        assertEquals(0, reader.pos());
        assertTrue(reader.isEmpty());

        reader.consume();
        assertEquals('a', reader.current());
        assertEquals(1, reader.pos());
        assertFalse(reader.isEmpty());

        reader.consume();
        assertEquals('b', reader.current());
        assertEquals(2, reader.pos());

        reader.unconsume();
        assertEquals('b', reader.current());
        assertEquals(2, reader.pos());

        reader.advance();
        assertEquals('c', reader.current());
        assertEquals(3, reader.pos());

        reader.mark();

        reader.consume();
        assertEquals('d', reader.current());
        assertEquals(4, reader.pos());

        reader.rewindToMark();
        assertEquals('c', reader.current());
        assertEquals(3, reader.pos());

        reader.consumeTo('e');
        String consumed = reader.consumeToEnd();
        assertEquals("fghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*()_+{}|:<>", consumed);
        assertEquals(13, reader.pos());
        assertTrue(reader.isEmpty());

        reader.consumeLetterSequence();
        consumed = reader.consumeToEnd();
        assertEquals("ijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*()_+{}|:<>", consumed);
        assertEquals(42, reader.pos());
        assertTrue(reader.isEmpty());

        reader.consumeLetterThenDigitSequence();
        consumed = reader.consumeToEnd();
        assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ", consumed);
        assertEquals(26, reader.pos());
        assertTrue(reader.isEmpty());

        reader.consumeHexSequence();
        consumed = reader.consumeToEnd();
        assertEquals("", consumed);
        assertEquals(0, reader.pos());
        assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderWithEmptyInput() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        assertTrue(reader.isEmpty());
        assertEquals(0, reader.pos());
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        reader.reader.close();
        reader.consume();
    }

    @Test
    public void testStringCache() {
        CharSequence testString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*()_+{}|:<>?";
        CharacterReader reader = new CharacterReader(new StringReader(testString.toString()));

        String cached = reader.consumeTo("ab").toString();
        assertEquals("cdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*()_+{}|:<>", reader.consumeToEnd().toString());
        assertEquals("cd", cached);

        cached = reader.consumeTo("xy").toString();
        assertEquals("zwabcdefghijklmnopqrstuvwABCDEFGHIJKLMNOPQRSTUVWXY1234567890!@#$%^&*()_+{}|:<>", reader.consumeToEnd().toString());
        assertEquals("xy", cached);

        cached = reader.consumeTo("xyz").toString();
        assertEquals("", reader.consumeToEnd().toString());
        assertEquals("xyz", cached);
    }
}