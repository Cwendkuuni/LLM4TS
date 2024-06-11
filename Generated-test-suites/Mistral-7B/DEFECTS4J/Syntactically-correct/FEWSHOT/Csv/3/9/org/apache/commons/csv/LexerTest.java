package org.apache.commons.csv;

import static org.apache.commons.csv.Constants.BACKSPACE;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.END_OF_STREAM;
import static org.apache.commons.csv.Constants.FF;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import static org.apache.commons.csv.Constants.UNDEFINED;
import static org.apache.commons.csv.Lexer.DISABLED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.Token;
import org.apache.commons.csv.util.StringBuilderFactory;
import org.junit.Before;
import org.junit.Test;

public class LexerTest {

    private ExtendedBufferedReader in;
    private Lexer lexer;
    private StringBuilderFactory stringBuilderFactory;

    @Before
    public void setUp() throws IOException {
        CSVFormat format = new CSVFormatBuilder() //
                .setDelimiter(';') //
                .setHeaderSeparator('-') //
                .setIgnoreSurroundingSpaces(true) //
                .setIgnoreEmptyLines(true) //
                .build();
        Reader reader = mock(Reader.class);
        when(reader.read()).thenReturn((char) 0);
        when(reader.read(new char[1])).thenAnswer(invocation -> {
            char[] charArray = invocation.getArgument(0);
            charArray[0] = invocation.getArgument(1);
            return charArray[0];
        });
        when(reader.readLine()).thenReturn(null);
        in = new ExtendedBufferedReader(reader);
        lexer = new Lexer(format, in) {
            @Override
            protected StringBuilderFactory getStringBuilderFactory() {
                if (stringBuilderFactory == null) {
                    stringBuilderFactory = new StringBuilderFactory();
                }
                return stringBuilderFactory;
            }
        };
    }

    @Test
    public void testIsStartOfLine() {
        for (int c = 0; c <= 255; c++) {
            boolean result = lexer.isStartOfLine(c);
            assertTrue("Expected true for char code " + c + " but got " + result, result || Character.isWhitespace((char) c));
        }
    }

    @Test
    public void testIsEndOfFile() {
        int c = END_OF_STREAM;
        boolean result = lexer.isEndOfFile(c);
        assertTrue("Expected true for EOF but got " + result, result);
    }

    @Test
    public void testIsDelimiter() {
        int c = lexer.delimiter;
        boolean result = lexer.isDelimiter(c);
        assertTrue("Expected true for delimiter " + c + " but got " + result, result);
    }

    @Test
    public void testIsEscape() {
        int c = lexer.escape;
        boolean result = lexer.isEscape(c);
        assertTrue("Expected true for escape character " + c + " but got " + result, result);
    }

    @Test
    public void testIsQuoteChar() {
        int c = lexer.quoteChar;
        boolean result = lexer.isQuoteChar(c);
        assertTrue("Expected true for quote character " + c + " but got " + result, result);
    }

    @Test
    public void testIsCommentStart() {
        int c = lexer.commmentStart;
        boolean result = lexer.isCommentStart(c);
        assertTrue("Expected true for comment start character " + c + " but got " + result, result == c == DISABLED);
    }

    @Test
    public void testIsWhitespace() {
        for (int c = 0; c <= 255; c++) {
            boolean result = lexer.isWhitespace(c);
            if ((Character.isWhitespace((char) c) || c == lexer.delimiter) && !lexer.isCommentStart(c) && !lexer.isQuoteChar(c) && c != DISABLED) {
                assertTrue("Expected true for char code " + c + " but got " + result, result);
            } else {
                assertFalse("Expected false for char code " + c + " but got " + result, result);
            }
        }
    }

    @Test
    public void testReadEndOfLine() throws IOException {
        int c = CR;
        assertTrue("Expected true for CR but got " + lexer.readEndOfLine(c), lexer.readEndOfLine(c));

        c = LF;
        assertTrue("Expected true for LF but got " + lexer.readEndOfLine(c), lexer.readEndOfLine(c));

        c = LF;
        assertTrue("Expected true for LF but got " + lexer.readEndOfLine(c), lexer.readEndOfLine(c));

        c = CR;
        assertTrue("Expected true for CR but got " + lexer.readEndOfLine(c), lexer.readEndOfLine(c));

        c = -1;
        assertFalse("Expected false for EOF but got " + lexer.readEndOfLine(c), lexer.readEndOfLine(c));

        c = 'a';
        assertFalse("Expected false for non line terminator but got " + lexer.readEndOfLine(c), lexer.readEndOfLine(c));
    }

    @Test
    public void testReadEscape() throws IOException {
        int c = CR;
        assertEquals("Unexpected escape character read instead of CR: " + c, CR, lexer.readEscape());

        c = LF;
        assertEquals("Unexpected escape character read instead of LF: " + c, LF, lexer.readEscape());

        c = TAB;
        assertEquals("Unexpected escape character read instead of TAB: " + c, TAB, lexer.readEscape());

        c = BACKSPACE;
        assertEquals("Unexpected escape character read instead of BACKSPACE: " + c, BACKSPACE, lexer.readEscape());

        c = FF;
        assertEquals("Unexpected escape character read instead of FF: " + c, FF, lexer.readEscape());

        c = UNDEFINED;
        assertNull("Expected null for undefined escape character but got " + c, lexer.readEscape());

        c = 'a';
        assertNull("Expected null for invalid escape character: " + c, lexer.readEscape());
    }

    @Test
    public void testNextToken() throws IOException {
        Token token = mock(Token.class);
        Token reusableToken = mock(Token.class);

        when(token.getTokens()).thenReturn(new String[] {"foo"});

        Token result = lexer.nextToken(reusableToken);

        assertEquals("Unexpected token type: " + result.getType(), Token.TYPE.STRING, result.getType());
        assertEquals("Unexpected string value: " + result.getValue(), "foo", result.getValue());

        result = lexer.nextToken(reusableToken);
        assertNull("Expected null for EOF but got " + result.getValue(), result.getValue());
    }
}