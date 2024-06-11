package org.apache.commons.csv;

import static org.apache.commons.csv.Constants.BACKSPACE;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.END_OF_STREAM;
import static org.apache.commons.csv.Constants.FF;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import static org.apache.commons.csv.Constants.UNDEFINED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.Token;

public class LexerTest {

    private static final String TEST_INPUT = "foo\tbar\nbaz\rqux,\nquux,\n--comment,\n";

    @Test
    public void testIsStartOfLine() throws IOException {
        Lexer lexer = createLexer();
        int c;
        for (int i = 0; i < TEST_INPUT.length(); i++) {
            c = lexer.in.read();
            assertTrue(lexer.isStartOfLine(c));
            if (c == UNDEFINED || (c == LF && lexer.in.lookAhead() == CR)) {
                break;
            }
        }
    }

    @Test
    public void testIsEndOfFile() throws IOException {
        Lexer lexer = createLexer();
        int c = -1;
        while ((c = lexer.in.read()) != END_OF_STREAM) {
            assertFalse(lexer.isEndOfFile(c));
        }
        assertTrue(lexer.isEndOfFile(c));
    }

    @Test
    public void testIsWhitespace() throws IOException {
        Lexer lexer = createLexer();
        int c;
        for (int i = 0; i < TEST_INPUT.length(); i++) {
            c = lexer.in.read();
            if (Character.isWhitespace((char) c)) {
                assertTrue(lexer.isWhitespace(c));
            } else {
                assertFalse(lexer.isWhitespace(c));
            }
        }
    }

    @Test
    public void testIsDelimiter() throws IOException {
        Lexer lexer = createLexer();
        assertEquals(lexer.delimiter, lexer.delimiter); // sanity check
        int c;
        for (int i = 0; i < TEST_INPUT.length(); i++) {
            c = lexer.in.read();
            if (c == lexer.delimiter) {
                assertTrue(lexer.isDelimiter(c));
            } else {
                assertFalse(lexer.isDelimiter(c));
            }
        }
    }

    @Test
    public void testIsQuoteChar() throws IOException {
        Lexer lexer = createLexer();
        assertEquals(lexer.quoteChar, lexer.quoteChar); // sanity check
        int c;
        for (int i = 0; i < TEST_INPUT.length(); i++) {
            c = lexer.in.read();
            if (c == lexer.quoteChar) {
                assertTrue(lexer.isQuoteChar(c));
            } else {
                assertFalse(lexer.isQuoteChar(c));
            }
        }
    }

    @Test
    public void testIsCommentStart() throws IOException {
        Lexer lexer = createLexerWithCommentStart();
        assertEquals(lexer.commmentStart, lexer.commmentStart); // sanity check
        int c;
        for (int i = 0; i < TEST_INPUT.length(); i++) {
            c = lexer.in.read();
            if (c == lexer.commmentStart) {
                assertTrue(lexer.isCommentStart(c));
            } else {
                assertFalse(lexer.isCommentStart(c));
            }
        }
    }

    @Test
    public void testReadEndOfLine() throws IOException {
        Lexer lexer = createLexer();
        int c;
        for (int i = 0; i < TEST_INPUT.length(); i++) {
            c = lexer.in.read();
            if (lexer.readEndOfLine(c)) {
                assertTrue(lexer.readEndOfLine(in.read()));
            }
            if (c == LF || c == CR) {
                break;
            }
        }
    }

    @Test
    public void testTrimTrailingSpaces() throws IOException {
        Lexer lexer = createLexer();
        StringBuilder buffer = new StringBuilder();
        String input = "foo  \tbar\n baz\n qux,\n quux,\n";
        while ((c = lexer.in.read()) != -1) {
            buffer.append((char) c);
        }
        lexer.trimTrailingSpaces(buffer);
        assertEquals("foo\tbar\nbaz\nqux,\nquux,\n", buffer.toString());
    }

    @Test
    public void testReadEscape() throws IOException {
        Lexer lexer = createLexer();
        int c;
        assertEquals(CR, lexer.readEscape());
        assertEquals(LF, lexer.readEscape());
        assertEquals(TAB, lexer.readEscape());
        assertEquals(BACKSPACE, lexer.readEscape());
        assertEquals(FF, lexer.readEscape());
        assertEquals(UNDEFINED, lexer.readEscape());
        // test invalid escape
        assertEquals(UNDEFINED, lexer.readEscape());
    }

    @Test
    public void testNextToken() throws IOException {
        Lexer lexer = createLexer();
        Token token = new Token(Token.TYPE_STRING, "");
        String input = "foo\tbar\nbaz\rqux,\nquux,\n--comment,\n";
        StringBuilder buffer = new StringBuilder();
        int c;
        while ((c = lexer.in.read()) != -1) {
            buffer.append((char) c);
            lexer.nextToken(token);
            if (token.getType() == Token.TYPE_STRING) {
                assertEquals(buffer.toString(), token.getValue());
                buffer.setLength(0);
            }
        }
        assertEquals(Token.TYPE_END_OF_STREAM, token.getType());
    }

    private Lexer createLexer() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(TEST_INPUT.getBytes());
        InputStreamReader inputStreamReader = new InputStreamReader(byteArrayInputStream);
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(inputStreamReader);
        CSVFormat csvFormat = new CSVFormat(new String[]{"foo"}, new String[]{}, null);
        return new Lexer(csvFormat, extendedBufferedReader);
    }

    private Lexer createLexerWithCommentStart() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(TEST_INPUT.getBytes());
        InputStreamReader inputStreamReader = new InputStreamReader(byteArrayInputStream);
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(inputStreamReader);
        CSVFormat csvFormat = new CSVFormat(new String[]{"foo"}, new String[]{}, "--");
        return new Lexer(csvFormat, extendedBufferedReader);
    }
}