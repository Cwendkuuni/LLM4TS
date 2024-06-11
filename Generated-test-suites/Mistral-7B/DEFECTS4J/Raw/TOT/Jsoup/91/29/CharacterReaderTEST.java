package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterReaderTest {

    private CharacterReader reader;
    private static final String TEST_DATA = "This is a test for CharacterReader class.\r\n<html>\r\n<head>\r\n<title>Test Title</title>\r\n</head>\r\n<body>\r\n<p>Test Paragraph</p>\r\n</body>\r\n</html>";

    @Before
    public void setUp() {
        reader = new CharacterReader(new StringReader(TEST_DATA));
    }

    @Test
    public void testIsEmpty() {
        assertTrue(reader.isEmpty());
        reader.bufferUp();
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testPos() {
        assertEquals(0, reader.pos());
        reader.bufferUp();
        reader.advance();
        assertEquals(1, reader.pos());
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
        assertEquals('s', reader.consume());
        assertEquals('t', reader.consume());
        assertEquals(' ', reader.consume());
        assertEquals('f', reader.consume());
        assertEquals('o', reader.consume());
        assertEquals('r', reader.consume());
        assertEquals(' ', reader.consume());
        assertEquals('C', reader.consume());
        assertEquals('h', reader.consume());
        assertEquals('a', reader.consume());
        assertEquals('r', reader.consume());
        assertEquals('a', reader.consume());
        assertEquals('c', reader.consume());
        assertEquals('t', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals('r', reader.consume());
        assertEquals(' ', reader.consume());
        assertEquals('<', reader.consume());
        assertEquals('h', reader.consume());
        assertEquals('t', reader.consume());
        assertEquals('m', reader.consume());
        assertEquals('l', reader.consume());
        assertEquals('>', reader.consume());
        assertEquals(' ', reader.consume());
        assertEquals('<', reader.consume());
        assertEquals('h', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals('a', reader.consume());
        assertEquals('d', reader.consume());
        assertEquals(' ', reader.consume());
        assertEquals('t', reader.consume());
        assertEquals('i', reader.consume());
        assertEquals('t', reader.consume());
        assertEquals('l', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals(' ', reader.consume());
        assertEquals('/', reader.consume());
        assertEquals('b', reader.consume());
        assertEquals('o', reader.consume());
        assertEquals('d', reader.consume());
        assertEquals('y', reader.consume());
        assertEquals('>', reader.consume());
        assertEquals(' ', reader.consume());
        assertEquals('S', reader.consume());
        assertEquals('c', reader.consume());
        assertEquals('r', reader.consume());
        assertEquals('i', reader.consume());
        assertEquals('p', reader.consume());
        assertEquals(' ', reader.consume());
        assertEquals('t', reader.consume());
        assertEquals(' ', reader.consume());
        assertEquals('i', reader.consume());
        assertEquals('t', reader.consume());
        assertEquals('l', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals(' ', reader.consume());
        assertEquals('>', reader.consume());
        assertEquals(' ', reader.consume());
        assertEquals('</', reader.consume());
        assertEquals('t', reader.consume());
        assertEquals('i', reader.consume());
        assertEquals('t', reader.consume());
        assertEquals('l', reader.consume());
        assertEquals('e', reader.consume());
        assertEquals('>', reader.consume());
        assertEquals(' ', reader.consume());
        assertEquals('>', reader.consume());
        assertEquals(' ', reader.consume());
        assertEquals('B', reader.consume());
        assertEquals('o', reader.consume());
        assertEquals('d', reader.consume());
        assertEquals('y', reader.consume());
        assertEquals(' ', reader.consume());
        assertEquals('>', reader.consume());
        assertEquals(' ', reader.consume());
        assertEquals('E', reader.consume());
        assertEquals('n', reader.consume());
        assertEquals('d', reader.consume());
        assertEquals(' ', reader.consume());
        assertEquals('o', reader.consume());
        assertEquals('f', reader.consume());
        assertEquals(' ', reader.consume());
        assertEquals('F', reader.consume());
        assertEquals('i', reader.consume());
        assertEquals('n', reader.consume());
        assertEquals('i', reader.consume());
        assertEquals('s', reader.consume());
        assertEquals('h', reader.consume());
        assertEquals(' ', reader.consume());
        assertEquals('i', reader.consume());
        assertEquals('s', reader.consume());
        assertEquals(' ', reader.consume());
        assertEquals('a', reader.consume());
        assertEquals(' ', reader.consume());
        assertEquals('t', reader.consume());
        assertEquals(' ', reader.consume());
        assertEquals(' ', reader.consume());
        assertEquals(CharacterReader.EOF, reader.consume());
    }

    @Test
    public void testConsumeTo() {
        assertEquals("This is a test for CharacterReader class.", reader.consumeTo("This is a test for CharacterReader class.").trim());
        assertEquals("test", reader.consumeTo("test").trim());
        assertEquals("Title", reader.consumeTo("<title>Title</title>").trim());
        assertEquals("", reader.consumeToAny(' ').trim());
        assertEquals("Test Paragraph", reader.consumeTo("<p>Test Paragraph</p>").trim());
    }

    @Test
    public void testConsumeToAnySorted() {
        assertEquals("<", reader.consumeToAnySorted(new char[]{'<', 'h', 't', 'm', 'l'}).trim());
        assertEquals("", reader.consumeToAnySorted(new char[]{' ', 'a', 'b', 'c'}).trim());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() {
        reader = new CharacterReader(new StringReader(""));
        reader.consume();
    }
}