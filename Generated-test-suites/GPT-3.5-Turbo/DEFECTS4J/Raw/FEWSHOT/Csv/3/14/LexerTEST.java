package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LexerTest {
    @Test
    public void testGetLineNumber() {
        Lexer lexer = new Lexer(null, null);
        assertEquals(0, lexer.getLineNumber());
    }

    @Test
    public void testReadEscape() throws Exception {
        Lexer lexer = new Lexer(null, null);
        assertEquals(Lexer.CR, lexer.readEscape());
        assertEquals(Lexer.LF, lexer.readEscape());
        assertEquals(Lexer.TAB, lexer.readEscape());
        assertEquals(Lexer.BACKSPACE, lexer.readEscape());
        assertEquals(Lexer.FF, lexer.readEscape());
        assertEquals('a', lexer.readEscape());
    }

    @Test(expected = IOException.class)
    public void testReadEscapeEndOfStream() throws Exception {
        Lexer lexer = new Lexer(null, null);
        lexer.readEscape();
    }

    @Test
    public void testTrimTrailingSpaces() {
        StringBuilder buffer = new StringBuilder("test   ");
        Lexer lexer = new Lexer(null, null);
        lexer.trimTrailingSpaces(buffer);
        assertEquals("test", buffer.toString());
    }

    @Test
    public void testReadEndOfLine() throws Exception {
        Lexer lexer = new Lexer(null, null);
        assertTrue(lexer.readEndOfLine(Lexer.LF));
        assertTrue(lexer.readEndOfLine(Lexer.CR));
        assertTrue(lexer.readEndOfLine(Lexer.CR));
        assertFalse(lexer.readEndOfLine(Lexer.BACKSPACE));
    }

    @Test
    public void testIsWhitespace() {
        Lexer lexer = new Lexer(null, null);
        assertTrue(lexer.isWhitespace(' '));
        assertTrue(lexer.isWhitespace('\t'));
        assertFalse(lexer.isWhitespace('a'));
    }

    @Test
    public void testIsStartOfLine() {
        Lexer lexer = new Lexer(null, null);
        assertTrue(lexer.isStartOfLine(Lexer.LF));
        assertTrue(lexer.isStartOfLine(Lexer.CR));
        assertTrue(lexer.isStartOfLine(Lexer.UNDEFINED));
        assertFalse(lexer.isStartOfLine('a'));
    }

    @Test
    public void testIsEndOfFile() {
        Lexer lexer = new Lexer(null, null);
        assertTrue(lexer.isEndOfFile(Lexer.END_OF_STREAM));
        assertFalse(lexer.isEndOfFile('a'));
    }

    @Test
    public void testIsDelimiter() {
        Lexer lexer = new Lexer(null, null);
        assertTrue(lexer.isDelimiter(','));
        assertFalse(lexer.isDelimiter('a'));
    }

    @Test
    public void testIsEscape() {
        Lexer lexer = new Lexer(null, null);
        assertTrue(lexer.isEscape('\\'));
        assertFalse(lexer.isEscape('a'));
    }

    @Test
    public void testIsQuoteChar() {
        Lexer lexer = new Lexer(null, null);
        assertTrue(lexer.isQuoteChar('\''));
        assertFalse(lexer.isQuoteChar('a'));
    }

    @Test
    public void testIsCommentStart() {
        Lexer lexer = new Lexer(null, null);
        assertTrue(lexer.isCommentStart('#'));
        assertFalse(lexer.isCommentStart('a'));
    }
}