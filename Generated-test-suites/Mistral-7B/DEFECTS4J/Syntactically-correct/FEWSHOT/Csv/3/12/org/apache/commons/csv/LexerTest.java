package org.apache.commons.csv;

import static org.apache.commons.csv.Constants.BACKSPACE;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.END_OF_STREAM;
import static org.apache.commons.csv.Constants.FF;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import static org.apache.commons.csv.Constants.UNDEFINED;
import static org.apache.commons.csv.CSVFormat.DEFAULT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.Token;
import org.apache.commons.csv.util.ExtendedBufferedReader;
import org.junit.Test;

public class LexerTest {

    @Test
    public void testIsStartOfLine() throws IOException {
        Lexer lexer = createLexer();

        for (int i = 0; i < 256; i++) {
            assertTrue(lexer.isStartOfLine(UNDEFINED));
            assertTrue(lexer.isStartOfLine(CR));
            assertTrue(lexer.isStartOfLine(LF));
            assertFalse(lexer.isStartOfLine((char) i));
        }
    }

    @Test
    public void testIsEndOfFile() throws IOException {
        Lexer lexer = createLexer();

        for (int i = 0; i < 256; i++) {
            assertFalse(lexer.isEndOfFile(UNDEFINED));
            assertFalse(lexer.isEndOfFile(CR));
            assertFalse(lexer.isEndOfFile(LF));
            assertFalse(lexer.isEndOfFile((char) i));
        }

        ExtendedBufferedReader mockReader = mock(ExtendedBufferedReader.class);
        when(mockReader.read()).thenReturn(END_OF_STREAM);
        lexer = new Lexer(DEFAULT, mockReader) {
            @Override
            boolean readEndOfLine(int c) throws IOException {
                return true;
            }
        };
        assertTrue(lexer.isEndOfFile(UNDEFINED));
    }

    @Test
    public void testIsDelimiter() throws IOException {
        Lexer lexer = createLexer();
        Token token = new Token();
        lexer.nextToken(token);

        for (int i = 0; i < 256; i++) {
            assertFalse(lexer.isDelimiter(UNDEFINED));
            assertFalse(lexer.isDelimiter((char) i));
        }

        assertTrue(lexer.isDelimiter(lexer.delimiter));
    }

    @Test
    public void testIsQuoteChar() throws IOException {
        Lexer lexer = createLexer();

        for (int i = 0; i < 256; i++) {
            assertFalse(lexer.isQuoteChar(UNDEFINED));
            assertFalse(lexer.isQuoteChar((char) i));
        }

        assertTrue(lexer.isQuoteChar(lexer.quoteChar));
    }

    @Test
    public void testIsEscape() throws IOException {
        Lexer lexer = createLexer();

        for (int i = 0; i < 256; i++) {
            assertFalse(lexer.isEscape(UNDEFINED));
            assertFalse(lexer.isEscape((char) i));
        }

        assertTrue(lexer.isEscape(lexer.escape));
    }

    @Test
    public void testIsCommentStart() throws IOException {
        Lexer lexer = createLexer();

        for (int i = 0; i < 256; i++) {
            assertFalse(lexer.isCommentStart(UNDEFINED));
            assertFalse(lexer.isCommentStart((char) i));
        }

        assertTrue(lexer.isCommentStart(lexer.commmentStart));
    }

    @Test
    public void testIsWhitespace() throws IOException {
        Lexer lexer = createLexer();

        for (int i = 0; i < 256; i++) {
            if (Character.isWhitespace((char) i)) {
                assertTrue(lexer.isWhitespace(i));
            } else {
                assertFalse(lexer.isWhitespace(i));
            }
        }
    }

    @Test
    public void testTrimTrailingSpaces() throws IOException {
        StringBuilder buffer = new StringBuilder(" foo\t bar\n ");
        Lexer lexer = createLexer();

        lexer.trimTrailingSpaces(buffer);
        assertEquals("foo bar", buffer.toString());
    }

    @Test
    public void testReadEndOfLine() throws IOException {
        Lexer lexer = createLexer();
        int c = CR;

        assertTrue(lexer.readEndOfLine(c));
        assertEquals(CR, c);

        c = CR + LF;
        assertTrue(lexer.readEndOfLine(c));
        assertEquals(LF, c);

        assertFalse(lexer.readEndOfLine(LF));
        assertFalse(lexer.readEndOfLine(CR));
        assertFalse(lexer.readEndOfLine(UNDEFINED));
    }

    @Test
    public void testReadEscape() throws IOException {
        Lexer lexer = createLexer();

        assertEquals(CR, lexer.readEscape());
        assertEquals(LF, lexer.readEscape());
        assertEquals(TAB, lexer.readEscape());
        assertEquals(BACKSPACE, lexer.readEscape());
        assertEquals(FF, lexer.readEscape());
        assertEquals(UNDEFINED, lexer.readEscape());
        assertFalse(lexer.readEscape() > 0);
    }

    @Test(expected = IOException.class)
    public void testReadEscapeAtEndOfStream() throws IOException {
        ExtendedBufferedReader mockReader = mock(ExtendedBufferedReader.class);
        when(mockReader.read()).thenReturn(END_OF_STREAM);
        Lexer lexer = new Lexer(DEFAULT, mockReader) {
            @Override
            boolean readEndOfLine(int c) throws IOException {
                return true;
            }
        };
        lexer.readEscape();
    }

    private Lexer createLexer() throws IOException {
        return new Lexer(DEFAULT, new ExtendedBufferedReader(new InputStreamReader(LexerTest.class.getResourceAsStream("/test.csv"))));
    }
}