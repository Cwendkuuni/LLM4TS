package org.apache.commons.csv;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Before;
import org.junit.Test;

public class LexerTest {

    private ExtendedBufferedReader mockReader;
    private StringBuilder mockBuffer;
    private Lexer lexer;

    @Before
    public void setup() throws Exception {
        mockReader = mock(ExtendedBufferedReader.class);
        mockBuffer = new StringBuilder();
        lexer = new Lexer(new CSVFormat(), mockReader) {
            @Override
            Token nextToken(Token reusableToken) throws IOException {
                return null;
            }
        };
    }

    @Test
    public void testIsStartOfLine() {
        when(mockReader.getLastChar()).thenReturn(LF);
        assertTrue(lexer.isStartOfLine(LF));

        when(mockReader.getLastChar()).thenReturn(CR);
        assertTrue(lexer.isStartOfLine(CR));

        when(mockReader.getLastChar()).thenReturn('A');
        assertFalse(lexer.isStartOfLine('A'));

        when(mockReader.getLastChar()).thenReturn(delimiter);
        assertFalse(lexer.isStartOfLine(delimiter));
    }

    @Test
    public void testIsEndOfFile() throws IOException {
        when(mockReader.getLastChar()).thenReturn(UNDEFINED);
        assertFalse(lexer.isEndOfFile(UNDEFINED));

        when(mockReader.getLastChar()).thenReturn(END_OF_STREAM);
        assertTrue(lexer.isEndOfFile(END_OF_STREAM));
    }

    @Test
    public void testIsDelimiter() throws IOException {
        assertTrue(lexer.isDelimiter(delimiter));
        assertFalse(lexer.isDelimiter('A'));
    }

    @Test
    public void testIsEscape() throws IOException {
        assertFalse(lexer.isEscape(escape));
        assertFalse(lexer.isEscape('A'));
    }

    @Test
    public void testIsQuoteChar() throws IOException {
        assertFalse(lexer.isQuoteChar(quoteChar));
        assertFalse(lexer.isQuoteChar('A'));
    }

    @Test
    public void testIsCommentStart() throws IOException {
        assertFalse(lexer.isCommentStart(commmentStart));
        assertFalse(lexer.isCommentStart('A'));
    }

    @Test
    public void testIsWhitespace() throws Exception {
        String testString = "  hello world  ";
        lexer.in = new ExtendedBufferedReader(new StringReader(testString));

        for (int i = 0; i < testString.length(); i++) {
            int c = testString.charAt(i);
            assertTrue(lexer.isWhitespace(c));
        }
    }

    @Test(expected = IOException.class)
    public void testReadEndOfLineThrowsIOException() throws IOException {
        when(mockReader.getLastChar()).thenReturn(END_OF_STREAM);
        lexer.readEndOfLine(-1);
    }

    @Test
    public void testReadEndOfLineReturnsTrue() throws IOException {
        when(mockReader.getLastChar()).thenReturn(LF);
        assertTrue(lexer.readEndOfLine(LF));

        when(mockReader.getLastChar()).thenReturn(CR);
        assertTrue(lexer.readEndOfLine(CR));

        when(mockReader.getLastChar()).thenReturn(CR);
        when(mockReader.lookAhead()).thenReturn(LF);
        assertTrue(lexer.readEndOfLine(CR));

        when(mockReader.getLastChar()).thenReturn(LF);
        when(mockReader.lookAhead()).thenReturn(CR);
        assertTrue(lexer.readEndOfLine(LF));
    }

    @Test
    public void testReadEndOfLineReturnsFalse() throws IOException {
        when(mockReader.getLastChar()).thenReturn('A');
        assertFalse(lexer.readEndOfLine('A'));

        when(mockReader.getLastChar()).thenReturn(delimiter);
        assertFalse(lexer.readEndOfLine(delimiter));
    }

    @Test
    public void testTrimTrailingSpaces() throws IOException {
        mockBuffer.append(" foo bar   ");
        lexer.trimTrailingSpaces(mockBuffer);
        assertEquals(" foo bar", mockBuffer.toString());
    }
}