package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

public class LexerTest {
    @Mock
    private ExtendedBufferedReader mockReader;
    private CSVFormat format;
    private Lexer lexer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        format = CSVFormat.DEFAULT;
        lexer = new Lexer(format, mockReader) {
            @Override
            Token nextToken(Token reusableToken) throws IOException {
                return null;
            }
        };
    }

    @Test
    public void testGetLineNumber() {
        lexer.getLineNumber();
        // TODO: assert something
    }

    @Test
    public void testReadEscape() throws IOException {
        lexer.readEscape();
        // TODO: assert something
    }

    @Test
    public void testTrimTrailingSpaces() {
        StringBuilder buffer = new StringBuilder("test ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("test", buffer.toString());
    }

    @Test
    public void testReadEndOfLine() throws IOException {
        lexer.readEndOfLine('\n');
        // TODO: assert something
    }

    @Test
    public void testIsWhitespace() {
        lexer.isWhitespace(' ');
        // TODO: assert something
    }

    @Test
    public void testIsStartOfLine() {
        lexer.isStartOfLine('\n');
        // TODO: assert something
    }

    @Test
    public void testIsEndOfFile() {
        lexer.isEndOfFile(-1);
        // TODO: assert something
    }

    @Test
    public void testIsDelimiter() {
        lexer.isDelimiter(',');
        // TODO: assert something
    }

    @Test
    public void testIsEscape() {
        lexer.isEscape('\\');
        // TODO: assert something
    }

    @Test
    public void testIsQuoteChar() {
        lexer.isQuoteChar('"');
        // TODO: assert something
    }

    @Test
    public void testIsCommentStart() {
        lexer.isCommentStart('#');
        // TODO: assert something
    }
}