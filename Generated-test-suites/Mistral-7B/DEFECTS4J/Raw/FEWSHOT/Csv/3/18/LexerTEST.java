package org.apache.commons.csv;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.csv.Constants;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.Token;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class LexerTest {

    private Lexer lexer;
    private static final String TEST_DATA = "foo,bar\n" +
            "baz,\"quoted,value\"\n" +
            "--comment\n" +
            "\t\n" +
            "12345\n";

    @Before
    public void setUp() {
        InputStream inputStream = new ByteArrayInputStream(TEST_DATA.getBytes());
        CSVFormat format = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces(true)
                .withIgnoreEmptyLines(true);
        this.lexer = new Lexer(format, new ExtendedBufferedReader(inputStream));
    }

    @Test
    public void testIsStartOfLine() {
        for (int i = 0; i < TEST_DATA.length(); i++) {
            int c = TEST_DATA.charAt(i);
            assertTrue(lexer.isStartOfLine(c) || lexer.isEndOfFile(c));
        }
        assertFalse(lexer.isStartOfLine('\n'));
        assertFalse(lexer.isStartOfLine('x'));
    }

    @Test
    public void testIsEndOfFile() {
        assertTrue(lexer.isEndOfFile(Constants.END_OF_STREAM));
        assertFalse(lexer.isEndOfFile('x'));
        assertFalse(lexer.isEndOfFile('a'));
    }

    @Test
    public void testIsDelimiter() {
        assertTrue(lexer.isDelimiter(lexer.delimiter));
        assertFalse(lexer.isDelimiter('x'));
    }

    @Test
    public void testIsEscape() {
        assertFalse(lexer.isEscape('x'));
        assertFalse(lexer.isEscape('\n'));
        assertTrue(lexer.isEscape(lexer.escape));
    }

    @Test
    public void testIsQuoteChar() {
        assertFalse(lexer.isQuoteChar('x'));
        assertFalse(lexer.isQuoteChar('\n'));
        assertTrue(lexer.isQuoteChar(lexer.quoteChar));
    }

    @Test
    public void testIsWhitespace() {
        for (char c : Constants.WHITESPACE_CHARS) {
            assertTrue(lexer.isWhitespace(c));
        }
        assertFalse(lexer.isWhitespace('\n'));
        assertFalse(lexer.isWhitespace('x'));
    }

    @Test
    public void testIsCommentStart() {
        assertFalse(lexer.isCommentStart('x'));
        assertFalse(lexer.isCommentStart('\n'));
        assertTrue(lexer.isCommentStart(lexer.commmentStart));
    }

    @Test
    public void testReadEndOfLine() throws IOException {
        assertTrue(lexer.readEndOfLine(Constants.LF));
        assertTrue(lexer.readEndOfLine(Constants.CR));
        assertFalse(lexer.readEndOfLine('x'));
        assertFalse(lexer.readEndOfLine('a'));
    }

    @Test
    public void testNextToken() throws IOException {
        StringBuilder tokenBuilder = new StringBuilder();
        Token token = new Token();

        int c = in.read();
        while (c != Constants.END_OF_STREAM) {
            if (lexer.isStartOfLine(c)) {
                tokenBuilder.setLength(0);
                if (token.isSet()) {
                    System.out.println("Token: " + token.toString());
                    token = new Token();
                }
            } else {
                tokenBuilder.append((char) c);
                lexer.nextToken(token);
            }
            c = in.read();
        }

        assertNotNull(token.getValue());
        assertEquals(TEST_DATA, token.getValue());
    }

    @Test
    public void testTrimTrailingSpaces() throws IOException {
        StringBuilder buffer = new StringBuilder("foo\tbar\n");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("foo\nbar", buffer.toString());
    }
}