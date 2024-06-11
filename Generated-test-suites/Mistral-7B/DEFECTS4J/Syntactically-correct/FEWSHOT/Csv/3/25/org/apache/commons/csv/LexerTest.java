package org.apache.commons.csv;

import static org.apache.commons.csv.Constants.BACKSPACE;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.END_OF_STREAM;
import static org.apache.commons.csv.Constants.FF;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import static org.apache.commons.csv.Constants.UNDEFINED;
import static org.apache.commons.csv.Lexer.DISABLED;
import static org.apache.commons.csv.Lexer.isDelimiter;
import static org.apache.commons.csv.Lexer.isEscape;
import static org.apache.commons.csv.Lexer.isEndOfFile;
import static org.apache.commons.csv.Lexer.isQuoteChar;
import static org.apache.commons.csv.Lexer.isStartOfLine;
import static org.apache.commons.csv.Lexer.isWhitespace;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

public class LexerTest {

    @Test
    public void testIsDelimiter() {
        Lexer lexer = new DummyLexer(',');
        assertTrue(isDelimiter(lexer.delimiter));
        assertFalse(isDelimiter('a'));
        assertFalse(isDelimiter('\t'));
        assertFalse(isDelimiter('\r'));
        assertFalse(isDelimiter('\n'));
        assertFalse(isDelimiter('\f'));
        assertFalse(isDelimiter(BACKSPACE));
        assertFalse(isDelimiter(UNDEFINED));
        assertFalse(isDelimiter(DISABLED));
        assertFalse(isDelimiter(LF + CR));
    }

    @Test
    public void testIsEscape() {
        Lexer lexer = new DummyLexer('\\');
        assertTrue(isEscape(lexer.escape));
        assertFalse(isEscape('a'));
        assertFalse(isEscape('\t'));
        assertFalse(isEscape('\r'));
        assertFalse(isEscape('\n'));
        assertFalse(isEscape('\f'));
        assertFalse(isEscape(BACKSPACE));
        assertFalse(isEscape(UNDEFINED));
        assertFalse(isEscape(DISABLED));
        assertFalse(isEscape(','));
    }

    @Test
    public void testIsQuoteChar() {
        Lexer lexer = new DummyLexer('"');
        assertTrue(isQuoteChar(lexer.quoteChar));
        assertFalse(isQuoteChar('a'));
        assertFalse(isQuoteChar('\t'));
        assertFalse(isQuoteChar('\r'));
        assertFalse(isQuoteChar('\n'));
        assertFalse(isQuoteChar('\f'));
        assertFalse(isQuoteChar(BACKSPACE));
        assertFalse(isQuoteChar(UNDEFINED));
        assertFalse(isQuoteChar(DISABLED));
        assertFalse(isQuoteChar(','));
    }

    @Test
    public void testIsCommentStart() {
        Lexer lexer = new DummyLexer('#');
        assertTrue(isCommentStart(lexer.commmentStart));
        assertFalse(isCommentStart('a'));
        assertFalse(isCommentStart('\t'));
        assertFalse(isCommentStart('\r'));
        assertFalse(isCommentStart('\n'));
        assertFalse(isCommentStart('\f'));
        assertFalse(isCommentStart(BACKSPACE));
        assertFalse(isCommentStart(UNDEFINED));
        assertFalse(isCommentStart(DISABLED));
        assertFalse(isCommentStart(','));
    }

    @Test
    public void testIsWhitespace() {
        Lexer lexer = new DummyLexer(' ');
        assertTrue(isWhitespace(lexer.delimiter));
        assertTrue(isWhitespace(' '));
        assertTrue(isWhitespace('\t'));
        assertTrue(isWhitespace('\n'));
        assertTrue(isWhitespace('\r'));
        assertTrue(isWhitespace('\f'));
        assertFalse(isWhitespace(','));
        assertFalse(isWhitespace('\u0000'));
        assertFalse(isWhitespace(UNDEFINED));
        assertFalse(isWhitespace(DISABLED));
    }

    @Test
    public void testIsStartOfLine() {
        Lexer lexer = new DummyLexer('\n');
        assertTrue(isStartOfLine(UNDEFINED));
        assertTrue(isStartOfLine(lexer.delimiter));
        assertTrue(isStartOfLine('\r'));
        assertTrue(isStartOfLine('\n'));
        assertFalse(isStartOfLine('\f'));
        assertFalse(isStartOfLine('\t'));
        assertFalse(isStartOfLine(','));
        assertFalse(isStartOfLine(DISABLED));
        assertFalse(isStartOfLine(CR + LF));
    }

    @Test
    public void testIsEndOfFile() {
        Lexer lexer = new DummyLexer(UNDEFINED, new StringReader(""));
        assertTrue(isEndOfFile(UNDEFINED));
        assertFalse(isEndOfFile('\r'));
        assertFalse(isEndOfFile('\n'));
        assertFalse(isEndOfFile('\f'));
        assertFalse(isEndOfFile('\t'));
        assertFalse(isEndOfFile(','));
        assertFalse(isEndOfFile(BACKSPACE));
        assertFalse(isEndOfFile(LF + CR));
        assertTrue(isEndOfFile(END_OF_STREAM));
    }

    @Test
    public void testReadEscape() throws IOException {
        Lexer lexer = new DummyLexer('\\', new StringReader("r\n"));
        assertEquals(CR, lexer.readEscape());
        assertEquals(LF, lexer.in.read());
        assertEquals(LF, lexer.in.read());
        assertEquals(UNDEFINED, lexer.in.read());
        assertEquals(END_OF_STREAM, lexer.in.read());
    }

    @Test(expected = IOException.class)
    public void testReadEscapeEndOfStream() throws IOException {
        Lexer lexer = new DummyLexer('\\', new StringReader(""));
        lexer.readEscape();
    }

    @Test
    public void testTrimTrailingSpaces() {
        StringBuilder sb = new StringBuilder("  foo  ");
        assertEquals("foo", new Lexer(null, null).trimTrailingSpaces(sb).toString());
    }

    @Test
    public void testReadEndOfLine() throws IOException {
        Lexer lexer = new DummyLexer('\n', new StringReader("foo\r\nbar"));
        assertTrue(lexer.readEndOfLine(lexer.in.read()));
        assertTrue(lexer.readEndOfLine(lexer.in.read()));
        assertFalse(lexer.readEndOfLine(lexer.in.read()));
        assertFalse(lexer.readEndOfLine(lexer.in.read()));
    }

    @Test
    public void testNextToken() throws IOException {
        String input = "  foo, bar,\"baz\"\n";
        Lexer lexer = new DummyLexer(',', new StringReader(input));

        Token token = new Token();
        assertEquals(TokenType.TEXT, lexer.nextToken(token).getType());
        assertEquals("foo", token.getValue());

        token.reset();
        assertEquals(TokenType.DELIMITER, lexer.nextToken(token).getType());
        assertEquals(",", token.getValue());

        token.reset();
        assertEquals(TokenType.TEXT, lexer.nextToken(token).getType());
        assertEquals("bar", token.getValue());

        token.reset();
        assertEquals(TokenType.QUOTED_STRING, lexer.nextToken(token).getType());
        assertEquals("baz", token.getValue());

        token.reset();
        assertEquals(TokenType.END_OF_LINE, lexer.nextToken(token).getType());
        assertNull(token.getValue());

        token.reset();
        assertEquals(TokenType.END_OF_STREAM, lexer.nextToken(token).getType());
        assertNull(token.getValue());
    }
}

class DummyLexer extends Lexer {

    DummyLexer(char delimiter, CharSequence input) {
        super(null, null, delimiter, null, null, null, null, null, null, null, null, new ExtendedBufferedReader(input),
                null, null, null, null);
    }

    @Override
    abstract Token nextToken(Token reusableToken) throws IOException;

}