package org.jsoup.parser;

import org.jsoup.UncheckedIOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {

    private CharacterReader reader;

    @Before
    public void setUp() {
        String testString = "This is a test string with <special> characters: & < > { } [ ] ^ _ ` ~ ! @ # $ % ^ & * ( ) _ + = { } [ ] | \\ / \t \n \r \f ";
        reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream(testString.getBytes())));
    }

    @After
    public void tearDown() {
        reader = null;
    }

    @Test
    public void testIsEmpty() {
        assertTrue(reader.isEmpty());
        reader.advance();
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testPos() {
        assertEquals(0, reader.pos());
        reader.advance();
        reader.advance();
        assertEquals(2, reader.pos());
    }

    @Test
    public void testConsume() {
        assertEquals('T', reader.consume());
        assertEquals('h', reader.consume());
        assertEquals('i', reader.consume());
        assertEquals('s', reader.consume());
        assertEquals(' ', reader.consume());
        assertEquals('i', reader.consume());
        assertEquals('s', reader.consume());
        assertEquals(' ', reader.consume());
        assertEquals('a', reader.consume());
        assertEquals(' ', reader.consume());
        assertEquals('t', reader.consume());
        assertEquals(' ', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals(' ', reader.consume());
        assertEquals('s', reader.consume());
        assertEquals('t', reader.consume());
        assertEquals('r', reader.consume());
        assertEquals('i', reader.consume());
        assertEquals('n', reader.consume());
        assertEquals('g', reader.consume());
        assertEquals(' ', reader.consume());
        assertEquals('<', reader.consume());
        assertEquals('s', reader.consume());
        assertEquals('p', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals('c', reader.consume());
        assertEquals('i', reader.consume());
        assertEquals('a', reader.consume());
        assertEquals('l', reader.consume());
        assertEquals('s', reader.consume());
        assertEquals('>', reader.consume());
        assertEquals(' ', reader.consume());
        assertEquals('c', reader.consume());
        assertEquals('h', reader.consume());
        assertEquals('a', reader.consume());
        assertEquals('r', reader.consume());
        assertEquals('a', reader.consume());
        assertEquals('c', reader.consume());
        assertEquals('t', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals('r', reader.consume());
        assertEquals('s', reader.consume());
        assertEquals(':', reader.consume());
        assertEquals(' ', reader.consume());
        assertEquals('&', reader.consume());
        assertEquals('<', reader.consume());
        assertEquals('/', reader.consume());
        assertEquals('t', reader.consume());
        assertEquals('i', reader.consume());
        assertEquals('t', reader.consume());
        assertEquals('l', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals('>', reader.consume());
        assertEquals(' ', reader.consume());
        assertEquals('>', reader.consume());
        assertEquals(' ', reader.consume());
        assertEquals('{', reader.consume());
        assertEquals(' ', reader.consume());
        assertEquals('}', reader.consume());
        assertEquals(' ', reader.consume());
        assertEquals('[', reader.consume());
        assertEquals(' ', reader.consume());
        assertEquals(']', reader.consume());
        assertEquals(' ', reader.consume());
        assertEquals('^', reader.consume());
        assertEquals(' ', reader.consume());
        assertEquals('_', reader.consume());
        assertEquals('`', reader.consume());
        assertEquals('~', reader.consume());
        assertEquals('!', reader.consume());
        assertEquals('@', reader.consume());
        assertEquals('#', reader.consume());
        assertEquals('$', reader.consume());
        assertEquals('%', reader.consume());
        assertEquals('^', reader.consume());
        assertEquals('&', reader.consume());
        assertEquals('*', reader.consume());
        assertEquals('(', reader.consume());
        assertEquals(')', reader.consume());
        assertEquals('_', reader.consume());
        assertEquals('+', reader.consume());
        assertEquals('=', reader.consume());
        assertEquals('{', reader.consume());
        assertEquals('}', reader.consume());
        assertEquals('[', reader.consume());
        assertEquals(']', reader.consume());
        assertEquals('|', reader.consume());
        assertEquals('\\', reader.consume());
        assertEquals('/', reader.consume());
        assertEquals('\t', reader.consume());
        assertEquals('\n', reader.consume());
        assertEquals('\r', reader.consume());
        assertEquals('\f', reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testCurrent() {
        assertEquals('T', reader.current());
        reader.advance();
        assertEquals('h', reader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() {
        reader.bufferUp();
        reader.consume();
        assertEquals('T', reader.current());
        reader.consume(); // this should throw an IOException
    }

    @Test
    public void testMatch() {
        assertTrue(reader.matches('T'));
        assertFalse(reader.matches('F'));
        assertTrue(reader.matches("This is a "));
        assertTrue(reader.matchesIgnoreCase("This is a "));
        assertFalse(reader.matchesIgnoreCase("This IS A "));
        assertTrue(reader.matchesAny('T', 'h'));
        assertTrue(reader.matchesAnySorted(new char[]{'T', 'h'}));
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesDigit());
        assertTrue(reader.matches("is a "));
        assertTrue(reader.matchesIgnoreCase("is a "));
        assertFalse(reader.matchesIgnoreCase("Is a "));
        assertFalse(reader.matches("is an"));
        assertTrue(reader.matchesAny(new char[]{'h', 'i'}));
        assertTrue(reader.matchesAnySorted(new char[]{'h', 'i'}));
        assertTrue(reader.matchesLetterThenDigitSequence());
        assertTrue(reader.matchesDigitSequence());
        assertTrue(reader.matchesHexSequence());
        assertFalse(reader.matches('ö'));
        assertFalse(reader.matches("ö"));
    }

    @Test
    public void testConsumeTo() {
        assertEquals("is a ", reader.consumeTo(" is a "));
        assertEquals("special", reader.consumeTo("special"));
        assertEquals("characters:", reader.consumeTo("characters:"));
        assertEquals("", reader.consumeTo(""));
        assertEquals("", reader.consumeToAny());
        assertEquals("", reader.consumeToAnySorted());
        assertEquals("", reader.consumeData());
        assertEquals("", reader.consumeTagName());
        assertEquals("", reader.consumeToEnd());
        assertEquals("This is a test string with <special> characters: & < > { } [ ] ^ _ ` ~ ! @ # $ % ^ & * ( ) _ + = { } [ ] | \\ / \\t \\n \\r \\f ", reader.consumeToEnd());
    }

    @Test
    public void testNextIndexOf() {
        assertEquals(10, reader.nextIndexOf('s'));
        assertEquals(15, reader.nextIndexOf("special"));
        assertEquals(-1, reader.nextIndexOf('ö'));
        assertEquals(-1, reader.nextIndexOf("ö"));
    }
}