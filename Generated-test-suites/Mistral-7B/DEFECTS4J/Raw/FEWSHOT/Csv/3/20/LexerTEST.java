package org.apache.commons.csv;

import static org.apache.commons.csv.Constants.BACKSPACE;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.END_OF_STREAM;
import static org.apache.commons.csv.Constants.FF;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import static org.apache.commons.csv.Constants.UNDEFINED;
import static org.apache.commons.csv.Lexer.IS_START_OF_LINE;
import static org.apache.commons.csv.Lexer.IS_WHITESPACE;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LexerTest {

    @Test
    public void testIsStartOfLine() {
        final Lexer lexer = new MockLexer('\n');
        assertTrue(IS_START_OF_LINE(lexer.getLastChar()));
        final MockLexer lexerCR = new MockLexer('\r');
        assertTrue(IS_START_OF_LINE(lexerCR.getLastChar()));
        final MockLexer lexerLF = new MockLexer('\n');
        assertTrue(IS_START_OF_LINE(lexerLF.getLastChar()));
        final MockLexer lexerUNDEFINED = new MockLexer('\u0000');
        assertTrue(IS_START_OF_LINE(lexerUNDEFINED.getLastChar()));
    }

    @Test
    public void testIsEndOfFile() {
        final MockLexer lexer = new MockLexer('\n');
        assertFalse(lexer.isEndOfFile());
        final MockLexer lexerEndOfStream = new MockLexer(END_OF_STREAM);
        assertTrue(lexerEndOfStream.isEndOfFile());
    }

    @Test
    public void testIsDelimiter() {
        final Lexer lexer = new MockLexer('\t');
        assertFalse(lexer.isDelimiter('\t'));
        final MockLexer lexerDelimiter = new MockLexer(',');
        assertTrue(lexerDelimiter.isDelimiter(','));
    }

    @Test
    public void testIsEscape() {
        final Lexer lexer = new MockLexer('\n');
        assertFalse(lexer.isEscape('\n'));
        final MockLexer lexerEscape = new MockLexer('\\');
        assertTrue(lexerEscape.isEscape('\\'));
    }

    @Test
    public void testIsQuoteChar() {
        final Lexer lexer = new MockLexer('\n');
        assertFalse(lexer.isQuoteChar('\n'));
        final MockLexer lexerQuoteChar = new MockLexer('"');
        assertTrue(lexerQuoteChar.isQuoteChar('"'));
    }

    @Test
    public void testIsCommentStart() {
        final Lexer lexer = new MockLexer('\n');
        assertFalse(lexer.isCommentStart('\n'));
        final MockLexer lexerCommentStart = new MockLexer('#');
        assertTrue(lexerCommentStart.isCommentStart('#'));
    }

    @Test
    public void testIsWhitespace() {
        final Lexer lexerSpace = new MockLexer(' ');
        assertTrue(IS_WHITESPACE(lexerSpace.getLastChar()));
        final MockLexer lexerTab = new MockLexer('\t');
        assertTrue(IS_WHITESPACE(lexerTab.getLastChar()));
        final MockLexer lexerCr = new MockLexer('\r');
        assertTrue(IS_WHITESPACE(lexerCr.getLastChar()));
        final MockLexer lexerLf = new MockLexer('\n');
        assertTrue(IS_WHITESPACE(lexerLf.getLastChar()));
        final MockLexer lexerDelimiter = new MockLexer(',');
        assertFalse(IS_WHITESPACE(lexerDelimiter.getLastChar()));
    }

    @Test
    public void testReadEndOfLine() throws IOException {
        final MockLexer lexerLf = new MockLexer('\n');
        assertTrue(lexerLf.readEndOfLine(lexerLf.getLastChar()));
        final MockLexer lexerR = new MockLexer('\r');
        assertTrue(lexerR.readEndOfLine(lexerR.getLastChar()));
        final MockLexer lexerRn = new MockLexer('\r');
        assertTrue(lexerRn.readEndOfLine(lexerRn.getLastChar()));
        final MockLexer lexerCr = new MockLexer('\r');
        assertTrue(lexerCr.readEndOfLine(lexerCr.getLastChar()));
        final MockLexer lexerLfCr = new MockLexer('\n');
        assertTrue(lexerLfCr.readEndOfLine(lexerLfCr.getLastChar()));
    }

    @Test(expected = IOException.class)
    public void testReadEndOfLine_Eof() throws IOException {
        final MockLexer lexerEndOfStream = new MockLexer(END_OF_STREAM);
        lexerEndOfStream.readEndOfLine(lexerEndOfStream.getLastChar());
    }

    @Test
    public void testTrimTrailingSpaces() {
        final StringBuilder sb = new StringBuilder("   foo  ");
        new Lexer(null, null).trimTrailingSpaces(sb);
        assertEquals("foo", sb.toString());
    }
}

class MockLexer {
    private final int lastChar;

    MockLexer(final int c) {
        this.lastChar = c;
    }

    int getLastChar() {
        return lastChar;
    }
}