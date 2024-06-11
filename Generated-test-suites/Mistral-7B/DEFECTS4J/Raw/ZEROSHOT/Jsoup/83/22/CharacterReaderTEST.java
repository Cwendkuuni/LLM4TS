package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.UncheckedIOException;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;

import org.junit.Test;

/**
 * Test class for CharacterReader.
 */
public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        // Test the constructor with a Reader
        CharacterReader reader = new CharacterReader(new StringReader("Hello World!"));
        assertEquals("Hello World!", reader.consumeToEnd());

        // Test the constructor with a String
        reader = new CharacterReader("Hello World!");
        assertEquals("Hello World!", reader.consumeToEnd());

        // Test the constructor with a Reader and a size
        reader = new CharacterReader(new StringReader("Hello World!"), 5);
        assertEquals("Hello World!", reader.consumeToEnd());

        // Test the constructor with a Reader only
        reader = new CharacterReader(new StringReader("Hello World!"));
        reader.bufferUp();
        byte[] buffer = new byte[reader.bufLength];
        reader.reader.read(buffer);
        assertEquals("Hello World!", new String(buffer, 0, reader.bufLength));

        // Test pos() method
        reader = new CharacterReader(new StringReader("Hello World!"));
        assertEquals(0, reader.pos());
        reader.consume();
        reader.consume();
        assertEquals(2, reader.pos());

        // Test isEmpty() method
        reader = new CharacterReader(new StringReader("Hello World!"));
        assertFalse(reader.isEmpty());
        reader.consumeToEnd();
        assertTrue(reader.isEmpty());

        // Test current() method
        reader = new CharacterReader(new StringReader("Hello World!"));
        assertEquals('H', reader.current());
        reader.consume();
        assertEquals('e', reader.current());

        // Test consume() method
        reader = new CharacterReader(new StringReader("Hello World!"));
        assertEquals('H', reader.consume());
        assertEquals('e', reader.consume());

        // Test unconsume() method
        reader = new CharacterReader(new StringReader("Hello World!"));
        reader.consume();
        reader.unconsume();
        assertEquals('H', reader.current());

        // Test advance() method
        reader = new CharacterReader(new StringReader("Hello World!"));
        reader.advance();
        assertEquals('e', reader.current());

        // Test mark() and rewindToMark() methods
        reader = new CharacterReader(new StringReader("Hello World!"));
        reader.mark();
        reader.consume();
        reader.rewindToMark();
        assertEquals('H', reader.current());

        // Test nextIndexOf() method with a single character
        reader = new CharacterReader(new StringReader("Hello World!"));
        assertEquals(7, reader.nextIndexOf('l'));

        // Test nextIndexOf() method with a String
        reader = new CharacterReader(new StringReader("Hello World!"));
        assertEquals(6, reader.nextIndexOf("ll"));

        // Test consumeTo() method with a single character
        reader = new CharacterReader(new StringReader("Hello World!"));
        assertEquals("llo", reader.consumeTo('l'));

        // Test consumeTo() method with a String
        reader = new CharacterReader(new StringReader("Hello World!"));
        assertEquals("llo", reader.consumeTo("ll"));

        // Test consumeToAny() method
        reader = new CharacterReader(new StringReader("Hello World!"));
        assertEquals("lloWorld!", reader.consumeToAny(new char[] {'l', 'o', 'W'}));

        // Test consumeToAnySorted() method
        reader = new CharacterReader(new StringReader("Hello World!"));
        assertEquals("lloWorld!", reader.consumeToAnySorted(new char[] {'o', 'l', 'W'}));

        // Test consumeData() method
        reader = new CharacterReader(new StringReader("&lt;html&gt;"));
        assertEquals("&lt;html", reader.consumeData());

        // Test consumeTagName() method
        reader = new CharacterReader(new StringReader("<title>My Title</title>"));
        assertEquals("title", reader.consumeTagName());

        // Test consumeToEnd() method
        reader = new CharacterReader(new StringReader("Hello World!"));
        assertEquals("Hello World!", reader.consumeToEnd());

        // Test consumeLetterSequence() method
        reader = new CharacterReader(new StringReader("Hello World!"));
        assertEquals("Hello", reader.consumeLetterSequence());

        // Test consumeLetterThenDigitSequence() method
        reader = new CharacterReader(new StringReader("Hello123World!"));
        assertEquals("Hello123", reader.consumeLetterThenDigitSequence());

        // Test consumeHexSequence() method
        reader = new CharacterReader(new StringReader("&x0A"));
        assertEquals("\u000A", reader.consumeHexSequence());

        // Test consumeDigitSequence() method
        reader = new CharacterReader(new StringReader("12345"));
        assertEquals("12345", reader.consumeDigitSequence());

        // Test matches() method with a single character
        reader = new CharacterReader(new StringReader("Hello World!"));
        assertTrue(reader.matches('l'));
        assertFalse(reader.matches('k'));

        // Test matches() method with a String
        reader = new CharacterReader(new StringReader("Hello World!"));
        assertTrue(reader.matches("ll"));
        assertFalse(reader.matches("kk"));

        // Test matchesIgnoreCase() method with a String
        reader = new CharacterReader(new StringReader("Hello World!"));
        assertTrue(reader.matchesIgnoreCase("ll"));
        assertFalse(reader.matchesIgnoreCase("kk"));

        // Test matchesAny() method with a single character
        reader = new CharacterReader(new StringReader("Hello World!"));
        assertTrue(reader.matchesAny('l'));
        assertFalse(reader.matchesAny('k'));

        // Test matchesAny() method with an array of characters
        reader = new CharacterReader(new StringReader("Hello World!"));
        assertTrue(reader.matchesAny(new char[] {'l', 'o'}));
        assertFalse(reader.matchesAny(new char[] {'k', 'o'}));

        // Test matchesLetter() method
        reader = new CharacterReader(new StringReader("Hello World!"));
        assertTrue(reader.matchesLetter());
        assertFalse(reader.matchesDigit());

        // Test matchConsume() method with a single character
        reader = new CharacterReader(new StringReader("Hello World!"));
        assertEquals("llo", reader.matchConsume("ll"));

        // Test matchConsume() method with a String
        reader = new CharacterReader(new StringReader("Hello World!"));
        assertEquals("llo", reader.matchConsume("ll"));

        // Test containsIgnoreCase() method
        reader = new CharacterReader(new StringReader("<html><head><title>My Title</title></head><body><p>Hello World!</p></body></html>"));
        assertTrue(reader.containsIgnoreCase("title"));
        assertTrue(reader.containsIgnoreCase("p"));
        assertFalse(reader.containsIgnoreCase("kkk"));
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() throws IOException {
        // Test for IOException when reading from a failing Reader
        CharacterReader reader = new CharacterReader(new MyIOExceptionReader());
        reader.consume();
    }

    static class MyIOExceptionReader extends StringReader {
        @Override
        public int read(char[] cbuf, int off, int len) throws IOException {
            throw new IOException();
        }
    }

    @Test
    public void testStringCache() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World!"));
        String cachedString1 = reader.consumeTo("ll").intern();
        String cachedString2 = reader.consumeTo("ll").intern();
        assertTrue(Arrays.equals(cachedString1.getBytes(), cachedString2.getBytes()));
    }
}