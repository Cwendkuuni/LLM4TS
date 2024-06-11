package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class CharacterReaderTest {
    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals('a', reader.consume());
        assertEquals('b', reader.consume());
        assertEquals('c', reader.consume());
        assertEquals('d', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals('f', reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());

        reader = new CharacterReader("");
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());

        reader = new CharacterReader("abcdef");
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals('a', reader.current());
        reader.consume();
        assertEquals('b', reader.current());
        reader.consume();
        assertEquals('c', reader.current());
        reader.advance();
        assertEquals('d', reader.current());
        reader.advance();
        assertEquals('e', reader.current());
        reader.advance();
        assertEquals('f', reader.current());
        reader.advance();
        assertEquals(CharacterReader.EOF, reader.current());
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader("hello world");
        assertEquals(4, reader.nextIndexOf('l'));
        assertEquals(7, reader.nextIndexOf('w'));
        assertEquals(-1, reader.nextIndexOf('x'));

        reader = new CharacterReader("abracadabra");
        assertEquals(2, reader.nextIndexOf('a'));
        assertEquals(5, reader.nextIndexOf('d'));
        assertEquals(-1, reader.nextIndexOf('z'));
    }

    @Test
    public void testConsumeAsString() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("a", reader.consumeAsString());
        assertEquals("bc", reader.consumeAsString(2));
        assertEquals("def", reader.consumeAsString(3));

        reader = new CharacterReader("");
        assertNull(reader.consumeAsString());
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("a", reader.consumeTo('b').toString());
        assertEquals("bc", reader.consumeTo('d').toString());
        assertEquals("def", reader.consumeToEnd().toString());

        reader = new CharacterReader("--hello--");
        assertEquals("ello", reader.consumeTo('o').toString());
        assertEquals("ello--", reader.consumeToEnd().toString());

        reader = new CharacterReader("--123--");
        assertEquals("23", reader.consumeToAny('2', '3').toString());
        assertEquals("123--", reader.consumeToEnd().toString());

        reader = new CharacterReader("123<456>789");
        assertEquals("23<456>", reader.consumeToAnySorted('2', '3', '<').toString());
        assertEquals("123<456>789", reader.consumeToEnd().toString());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader("abcdef");
        assertEquals("abc", reader.consumeToAny('a', 'b', 'c').toString());
        assertEquals("def", reader.consumeToAny('d', 'e', 'f').toString());
        assertEquals("", reader.consumeToAny('g').toString());

        reader = new CharacterReader("--123--");
        assertEquals("123", reader.consumeToAny('1', '2', '3').toString());
        assertEquals("", reader.consumeToAny('4').toString());

        reader = new CharacterReader("123<456>789");
        assertEquals("123", reader.consumeToAnySorted('1', '2', '3').toString());
        assertEquals("789", reader.consumeToAnySorted('7', '8', '9').toString());
        assertEquals("", reader.consumeToAnySorted('0').toString());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader("<html><head><title>Test</title></head></html>");
        assertEquals("html", reader.consumeTagName().toString());
        assertEquals("head", reader.consumeTagName().toString());
        assertEquals("title", reader.consumeTagName().toString());
        assertEquals("Test", reader.consumeTagName().toString());
        assertEquals(CharacterReader.EOF, reader.consumeTagName().toString());
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader("&lt;p&gt;Hello&nbsp;world&lt;/p&gt;");
        assertEquals("&lt;p&gt;Hello", reader.consumeData().toString());
        assertEquals("world&lt;/p&gt;", reader.consumeData().toString());
        assertEquals(CharacterReader.EOF, reader.consumeData().toString());
    }
}