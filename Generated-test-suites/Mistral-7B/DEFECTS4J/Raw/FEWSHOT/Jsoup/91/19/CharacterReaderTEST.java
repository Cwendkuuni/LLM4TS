package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.UncheckedIOException;

import java.io.IOException;
import java.util.Arrays;

public class CharacterReaderTest {

    @Test
    public void testIsEmpty() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        assertTrue(reader.isEmpty());

        reader.mark();
        reader.advance();
        assertFalse(reader.isEmpty());
        reader.rewindToMark();

        reader = new CharacterReader(new StringReader("foo"));
        assertFalse(reader.isEmpty());

        reader.bufferUp();
        reader.unconsume();
        assertTrue(reader.isEmptyNoBufferUp());
        reader.advance();
        assertFalse(reader.isEmpty());

        reader = new CharacterReader(new StringReader("foo\n"));
        assertFalse(reader.isEmpty());
        reader.consume();
        assertFalse(reader.isEmpty());
        reader.bufferUp();
        assertTrue(reader.isEmptyNoBufferUp());
    }

    @Test
    public void testCurrent() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertEquals('f', reader.current());
        reader.advance();
        assertEquals('o', reader.current());
        reader.unconsume();
        assertEquals('o', reader.current());

        reader = new CharacterReader(new StringReader("\r\n"));
        assertEquals('\r', reader.current());
        reader.advance();
        assertEquals('\n', reader.current());
        reader.unconsume();
        assertEquals('\n', reader.current());

        reader = new CharacterReader(new StringReader(""));
        assertNull(reader.current());

        reader = new CharacterReader(new StringReader("foo\nbar"));
        assertEquals('f', reader.current());
        reader.advance();
        reader.advance();
        reader.advance();
        assertEquals('b', reader.current());
    }

    @Test
    public void testConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        reader.consume();
        assertEquals('f', reader.current());
        reader.consume();
        assertEquals('o', reader.current());
        reader.consume();
        assertEquals('o', reader.current());
        reader.consume();
        assertNull(reader.current());

        reader = new CharacterReader(new StringReader("foo\nbar"));
        reader.consume();
        assertEquals('f', reader.current());
        reader.consume();
        reader.consume();
        reader.consume();
        assertEquals('\n', reader.current());
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        assertEquals('b', reader.current());
        reader.consume();
        assertNull(reader.current());
    }

    @Test
    public void testUnconsume() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        reader.consume();
        reader.unconsume();
        assertEquals('f', reader.current());

        reader = new CharacterReader(new StringReader("foo\nbar"));
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.unconsume();
        reader.unconsume();
        reader.unconsume();
        assertEquals('\n', reader.current());
    }

    @Test
    public void testAdvance() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        reader.advance();
        reader.advance();
        assertEquals('o', reader.current());

        reader = new CharacterReader(new StringReader("foo\nbar"));
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        reader.advance();
        assertEquals('b', reader.current());

        reader = new CharacterReader(new StringReader(""));
        reader.advance();
        assertNull(reader.current());
    }

    @Test
    public void testMark() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        reader.mark();
        reader.advance();
        reader.advance();
        reader.rewindToMark();
        assertEquals('f', reader.current());

        reader = new CharacterReader(new StringReader("foo\nbar"));
        reader.mark();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.consume();
        reader.rewindToMark();
        assertEquals('\n', reader.current());

        reader = new CharacterReader(new StringReader(""));
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        assertNull(reader.current());
    }

    @Test(expected = UncheckedIOException.class)
    public void testIOException() {
        CharacterReader reader = new CharacterReader(new StringReader("") {
            @Override
            public int read(char[] cbuf, int offset, int length) {
                throw new IOException("test");
            }
        });
        reader.consume();
    }

    @Test
    public void testNextIndexOf() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertEquals(0, reader.nextIndexOf('f'));
        assertEquals(1, reader.nextIndexOf('o'));
        assertEquals(1, reader.nextIndexOf("o"));
        assertEquals(2, reader.nextIndexOf('o'));
        assertEquals(-1, reader.nextIndexOf('x'));

        reader = new CharacterReader(new StringReader("foo\nbar"));
        assertEquals(0, reader.nextIndexOf('f'));
        assertEquals(4, reader.nextIndexOf('\n'));
        assertEquals(5, reader.nextIndexOf('b'));
        assertEquals(6, reader.nextIndexOf('a'));
        assertEquals(7, reader.nextIndexOf('r'));
        assertEquals(-1, reader.nextIndexOf('z'));

        reader = new CharacterReader(new StringReader(""));
        assertEquals(-1, reader.nextIndexOf('x'));
    }

    @Test
    public void testNextIndexOfSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertEquals(0, reader.nextIndexOf("oo"));
        assertEquals(-1, reader.nextIndexOf("xx"));

        reader = new CharacterReader(new StringReader("foo\nbar"));
        assertEquals(0, reader.nextIndexOf("oo"));
        assertEquals(4, reader.nextIndexOf("\nbar"));
        assertEquals(-1, reader.nextIndexOf("xx"));

        reader = new CharacterReader(new StringReader(""));
        assertEquals(-1, reader.nextIndexOf("xx"));
    }

    @Test
    public void testConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertEquals("o", reader.consumeTo('o').toString());
        assertEquals("foo", reader.consumeToEnd().toString());

        reader = new CharacterReader(new StringReader("foo\nbar"));
        assertEquals("foo\n", reader.consumeTo('\n').toString());
        assertEquals("foo\nbar", reader.consumeToEnd().toString());

        reader = new CharacterReader(new StringReader(""));
        assertEquals("", reader.consumeToEnd().toString());
    }

    @Test
    public void testConsumeToSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertEquals("oo", reader.consumeTo("oo").toString());
        assertEquals("foo", reader.consumeToEnd().toString());

        reader = new CharacterReader(new StringReader("foo\nbar"));
        assertEquals("foo\n", reader.consumeTo("\n").toString());
        assertEquals("foo\nbar", reader.consumeToEnd().toString());

        reader = new CharacterReader(new StringReader(""));
        assertEquals("", reader.consumeToEnd().toString());
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertEquals("o", reader.consumeToAny(new char[]{'o'}).toString());
        assertEquals("foo", reader.consumeToEnd().toString());

        reader = new CharacterReader(new StringReader("foo\nbar"));
        assertEquals("foo\n", reader.consumeToAny(new char[]{'\\n'}).toString());
        assertEquals("foo\nbar", reader.consumeToEnd().toString());

        reader = new CharacterReader(new StringReader(""));
        assertEquals("", reader.consumeToEnd().toString());

        reader = new CharacterReader(new StringReader("quux"));
        assertEquals("quux", reader.consumeToAny(new char[]{'x','u','u'}).toString());
        assertEquals("quux", reader.consumeToEnd().toString());
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertEquals("foo", reader.consumeToAnySorted(new char[]{'f','o'}).toString());
        assertEquals("foo", reader.consumeToEnd().toString());

        reader = new CharacterReader(new StringReader("foo\nbar"));
        assertEquals("foo\n", reader.consumeToAnySorted(new char[]{'\\n'}).toString());
        assertEquals("foo\nbar", reader.consumeToEnd().toString());

        reader = new CharacterReader(new StringReader(""));
        assertEquals("", reader.consumeToEnd().toString());

        reader = new CharacterReader(new StringReader("quux"));
        assertEquals("quux", reader.consumeToAnySorted(new char[]{'u','u','x'}).toString());
        assertEquals("quux", reader.consumeToEnd().toString());
    }

    @Test
    public void testConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&<null"));
        assertEquals("&<null", reader.consumeData().toString());

        reader = new CharacterReader(new StringReader("foo"));
        assertEquals("foo", reader.consumeData().toString());

        reader = new CharacterReader(new StringReader("foo\nbar"));
        assertEquals("foo\nbar", reader.consumeData().toString());

        reader = new CharacterReader(new StringReader(""));
        assertEquals("", reader.consumeData().toString());
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("  <tag>"));
        assertEquals("<tag>", reader.consumeTagName().toString());

        reader = new CharacterReader(new StringReader("\t<tag>\n"));
        assertEquals("<tag>", reader.consumeTagName().toString());

        reader = new CharacterReader(new StringReader(""));
        assertEquals("", reader.consumeTagName().toString());

        reader = new CharacterReader(new StringReader("<title>\n<style>"));
        assertEquals("title", reader.consumeTagName().toString());
        assertEquals("style", reader.consumeTagName().toString());
    }

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertEquals("foo", reader.toString());

        reader = new CharacterReader(new StringReader("foo\nbar"));
        assertEquals("foo\nbar", reader.toString());

        reader = new CharacterReader(new StringReader(""));
        assertEquals("", reader.toString());
    }

    @Test
    public void testMatching() {
        CharacterReader reader = new CharacterReader(new StringReader("foo"));
        assertFalse(reader.matches('x'));
        assertTrue(reader.matches('f'));
        assertTrue(reader.matches('o'));

        reader = new CharacterReader(new StringReader("foo\nbar"));
        assertFalse(reader.matches('x'));
        assertTrue(reader.matches('f'));
        assertTrue(reader.matches('o'));
        assertTrue(reader.matches('\n'));
        assertTrue(reader.matches('r'));
        assertTrue(reader.matches('b'));

        reader = new CharacterReader(new StringReader(""));
        assertFalse(reader.matches('x'));

        reader = new CharacterReader(new StringReader("foo\n<tag>bar"));
        assertTrue(reader.matches("foo"));
        assertTrue(reader.matches('\n'));
        assertFalse(reader.matches("<tag>"));
        assertTrue(reader.matches("bar"));

        reader = new CharacterReader(new StringReader(""));
        assertFalse(reader.matches(""));
        assertFalse(reader.matches("x"));

        reader = new CharacterReader(new StringReader("foo\n<tag>bar"));
        assertTrue(reader.matchesIgnoreCase("FOO"));
        assertTrue(reader.matchesIgnoreCase("FOO\nBAR"));
        assertFalse(reader.matchesIgnoreCase("TAG"));

        reader = new CharacterReader(new StringReader("foo\n<tag>bar"));
        assertTrue(reader.matchesAny(new char[]{'f','o'}));
        assertTrue(reader.matchesAny(new char[]{'o','\\n'}));
        assertFalse(reader.matchesAny(new char[]{'t','a','g'}));

        reader = new CharacterReader(new StringReader("foo\n<tag>bar"));
        assertTrue(reader.matchesAnySorted(new char[]{'o','f'}));
        assertTrue(reader.matchesAnySorted(new char[]{'n','\\n'}));
        assertFalse(reader.matchesAnySorted(new char[]{'t','g','a'}));

        reader = new CharacterReader(new StringReader("foo\n<tag>bar"));
        assertTrue(reader.matchesLetter());
        assertTrue(reader.matchesLetterThenDigitSequence());
        assertFalse(reader.matchesDigitSequence());
        assertTrue(reader.matchesHexSequence());

        reader = new CharacterReader(new StringReader("foo\n<tag>bar"));
        assertFalse(reader.matchesLetterSequence());
        assertFalse(reader.matchesDigitSequence());
        assertFalse(reader.matchesHexSequence());
    }
}