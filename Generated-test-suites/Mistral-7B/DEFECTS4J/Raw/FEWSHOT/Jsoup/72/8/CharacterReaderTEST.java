package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        CharacterReader cr = new CharacterReader(new StringReader("hello world"));
        assertEquals(0, cr.pos());
        assertEquals(Character.EOF, cr.current());
        assertFalse(cr.isEmpty());
    }

    @Test
    public void testBufferUp() throws IOException {
        CharacterReader cr = new CharacterReader(new StringReader("hello world"));
        cr.bufferUp();
        assertEquals(0, cr.pos());
        assertEquals(Character.EOF, cr.current());
        assertTrue(cr.isEmpty());

        cr.bufferUp();
        assertEquals(5, cr.pos());
        assertEquals('h', cr.current());
        assertFalse(cr.isEmpty());

        cr.bufferUp();
        assertEquals(12, cr.pos());
        assertEquals('w', cr.current());
        assertTrue(cr.isEmpty());
    }

    @Test
    public void testConsumeToEnd() {
        CharacterReader cr = new CharacterReader(new StringReader("hello world"));
        String s = cr.consumeToEnd();
        assertEquals("hello world", s);
    }

    @Test
    public void testConsumeLetterSequence() {
        CharacterReader cr = new CharacterReader(new StringReader("abc123def"));
        String s = cr.consumeLetterSequence();
        assertEquals("abc", s);

        cr.advance();
        s = cr.consumeLetterSequence();
        assertEquals("def", s);
    }

    @Test
    public void testConsumeLetterThenDigitSequence() {
        CharacterReader cr = new CharacterReader(new StringReader("abc123def"));
        String s = cr.consumeLetterThenDigitSequence();
        assertEquals("abc123", s);

        cr.advance();
        s = cr.consumeLetterThenDigitSequence();
        assertEquals("def", s);
    }

    @Test
    public void testConsumeData() {
        CharacterReader cr = new CharacterReader(new StringReader("&lt;<script src=\"script.js\"&gt;"));
        String s = cr.consumeData();
        assertEquals("&lt;<script src=\"script.js\"&gt;", s);
    }

    @Test
    public void testConsumeTagName() {
        CharacterReader cr = new CharacterReader(new StringReader("<html><head><title>Test</title></head>"));
        String s = cr.consumeTagName();
        assertEquals("html", s);

        cr.advance();
        s = cr.consumeTagName();
        assertEquals("head", s);

        cr.advance();
        s = cr.consumeTagName();
        assertEquals("title", s);
    }

    @Test
    public void testConsumeToAny() {
        CharacterReader cr = new CharacterReader(new StringReader("&<&>"));
        String s = cr.consumeToAny(Character.TYPED_ARRAYS);
        assertEquals("", s);

        cr.advance();
        s = cr.consumeToAny(Character.TYPED_ARRAYS);
        assertEquals("&", s);

        cr.advance();
        s = cr.consumeToAny(Character.TYPED_ARRAYS);
        assertEquals("<", s);

        cr.advance();
        s = cr.consumeToAny(Character.TYPED_ARRAYS);
        assertEquals("&", s);

        cr.advance();
        s = cr.consumeToAny(Character.TYPED_ARRAYS);
        assertEquals(">", s);
    }

    @Test
    public void testConsumeToAnySorted() {
        CharacterReader cr = new CharacterReader(new StringReader("&<&>"));
        String s = cr.consumeToAnySorted(new char[]{'<', '&'});
        assertEquals("&", s);

        cr.advance();
        s = cr.consumeToAnySorted(new char[]{'<', '&'});
        assertEquals("<", s);
    }

    @Test
    public void testConsumeHexSequence() {
        CharacterReader cr = new CharacterReader(new StringReader("&#x61; &#x6C; &#x65;"));
        String s = cr.consumeHexSequence();
        assertEquals("abc", s);
    }

    @Test
    public void testConsumeDigitSequence() {
        CharacterReader cr = new CharacterReader(new StringReader("12345"));
        String s = cr.consumeDigitSequence();
        assertEquals("12345", s);
    }

    @Test
    public void testMatches() {
        CharacterReader cr = new CharacterReader(new StringReader("hello world"));
        assertTrue(cr.matches('l'));
        assertTrue(cr.matches("ld"));
        assertFalse(cr.matches('x'));
    }

    @Test
    public void testMatchesIgnoreCase() {
        CharacterReader cr = new CharacterReader(new StringReader("hello world"));
        assertTrue(cr.matchesIgnoreCase("ld"));
        assertFalse(cr.matchesIgnoreCase("X"));
    }

    @Test
    public void testMatchesAny() {
        CharacterReader cr = new CharacterReader(new StringReader("hello world"));
        assertTrue(cr.matchesAny('l', 'd'));
        assertFalse(cr.matchesAny('x'));
    }

    @Test
    public void testMatchesAnySorted() {
        CharacterReader cr = new CharacterReader(new StringReader("hello world"));
        assertTrue(cr.matchesAnySorted(new char[]{'l', 'd'}));
        assertFalse(cr.matchesAnySorted(new char[]{'x'}));
    }

    @Test
    public void testNextIndexOf() throws IOException {
        CharacterReader cr = new CharacterReader(new StringReader("hello world"));
        assertEquals(6, cr.nextIndexOf(' '));
        assertEquals(7, cr.nextIndexOf("ld"));
        assertEquals(-1, cr.nextIndexOf("X"));

        cr.advance();
        assertEquals(7, cr.nextIndexOf(' '));
        assertEquals(8, cr.nextIndexOf("ld"));
        assertEquals(-1, cr.nextIndexOf("X"));

        cr.advance();
        assertEquals(8, cr.nextIndexOf(' '));
        assertEquals(9, cr.nextIndexOf("ld"));
        assertEquals(-1, cr.nextIndexOf("X"));
    }

    @Test
    public void testNextIndexOfSorted() throws IOException {
        CharacterReader cr = new CharacterReader(new StringReader("hello world"));
        assertEquals(6, cr.nextIndexOf(Arrays.asList(" ", "l", "d")));
        assertEquals(7, cr.nextIndexOf(Arrays.asList("ld")));
        assertEquals(-1, cr.nextIndexOf(Arrays.asList("X")));

        cr.advance();
        assertEquals(7, cr.nextIndexOf(Arrays.asList(" ", "l", "d")));
        assertEquals(8, cr.nextIndexOf(Arrays.asList("ld")));
        assertEquals(-1, cr.nextIndexOf(Arrays.asList("X")));

        cr.advance();
        assertEquals(8, cr.nextIndexOf(Arrays.asList(" ", "l", "d")));
        assertEquals(9, cr.nextIndexOf(Arrays.asList("ld")));
        assertEquals(-1, cr.nextIndexOf(Arrays.asList("X")));
    }
}