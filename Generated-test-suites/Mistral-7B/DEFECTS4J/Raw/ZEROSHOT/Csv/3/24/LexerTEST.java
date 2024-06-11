package org.apache.commons.csv.test;

import org.apache.commons.csv.Constants;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.Token;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.StringReader;

/**
 * Test class for {@link Lexer}
 */
public class LexerTest {

    private ExtendedBufferedReader mockReader;
    private CSVFormat format;
    private Lexer lexer;

    @Before
    public void setUp() {
        format = new CSVFormat(',', '\u001E', '\u001C', '\u0023');
        mockReader = mock(ExtendedBufferedReader.class);
        lexer = new Lexer(format, mockReader);
    }

    @Test
    public void testGetLineNumber() throws IOException {
        when(mockReader.getLineNumber()).thenReturn(1);
        assertEquals(1, lexer.getLineNumber());
    }

    @Test
    public void testReadEscape() throws IOException {
        when(mockReader.read()).thenReturn('r').thenReturn('\n');
        assertEquals(CR, lexer.readEscape());
        assertEquals('\n', mockReader.getLastChar());

        when(mockReader.read()).thenReturn('n');
        assertEquals(LF, lexer.readEscape());

        when(mockReader.read()).thenReturn('t');
        assertEquals(TAB, lexer.readEscape());

        when(mockReader.read()).thenReturn(Constants.BACKSPACE);
        assertEquals(BACKSPACE, lexer.readEscape());

        when(mockReader.read()).thenReturn(Constants.FF);
        assertEquals(FF, lexer.readEscape());

        when(mockReader.read()).thenReturn(Constants.CR);
        assertEquals(CR, lexer.readEscape());

        when(mockReader.read()).thenReturn(Constants.LF);
        assertEquals(LF, lexer.readEscape());

        when(mockReader.read()).thenReturn(Constants.TAB);
        assertEquals(TAB, lexer.readEscape());

        when(mockReader.read()).thenReturn(Constants.BACKSPACE);
        assertEquals(BACKSPACE, lexer.readEscape());

        when(mockReader.read()).thenReturn(Constants.FF);
        assertEquals(FF, lexer.readEscape());

        when(mockReader.read()).thenReturn(Constants.UNDEFINED);
        assertEquals(UNDEFINED, lexer.readEscape());

        when(mockReader.read()).thenReturn(END_OF_STREAM);
        try {
            lexer.readEscape();
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testTrimTrailingSpaces() throws IOException {
        StringBuilder buffer = new StringBuilder("  this is a test   ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("this is a test", buffer.toString());
    }

    @Test
    public void testIsStartOfLine() throws IOException {
        when(mockReader.read()).thenReturn(UNDEFINED);
        assertTrue(lexer.isStartOfLine(UNDEFINED));

        when(mockReader.read()).thenReturn(CR);
        assertTrue(lexer.isStartOfLine(CR));

        when(mockReader.read()).thenReturn(LF);
        assertTrue(lexer.isStartOfLine(LF));

        when(mockReader.read()).thenReturn('A');
        assertFalse(lexer.isStartOfLine('A'));
    }

    @Test
    public void testIsEndOfFile() throws IOException {
        when(mockReader.read()).thenReturn(UNDEFINED);
        assertFalse(lexer.isEndOfFile(UNDEFINED));

        when(mockReader.read()).thenThrow(IOException.class);
        assertTrue(lexer.isEndOfFile(UNDEFINED));

        when(mockReader.read()).thenReturn(END_OF_STREAM);
        assertTrue(lexer.isEndOfFile(END_OF_STREAM));

        when(mockReader.read()).thenReturn('A');
        assertFalse(lexer.isEndOfFile('A'));
    }

    @Test
    public void testIsDelimiter() throws IOException {
        when(mockReader.read()).thenReturn(',');
        assertTrue(lexer.isDelimiter(','));

        when(mockReader.read()).thenReturn('A');
        assertFalse(lexer.isDelimiter('A'));
    }

    @Test
    public void testIsEscape() throws IOException {
        when(mockReader.read()).thenReturn('\u001E');
        assertTrue(lexer.isEscape('\u001E'));

        when(mockReader.read()).thenReturn('A');
        assertFalse(lexer.isEscape('A'));
    }

    @Test
    public void testIsQuoteChar() throws IOException {
        when(mockReader.read()).thenReturn('\u001C');
        assertTrue(lexer.isQuoteChar('\u001C'));

        when(mockReader.read()).thenReturn('A');
        assertFalse(lexer.isQuoteChar('A'));
    }

    @Test
    public void testIsCommentStart() throws IOException {
        when(mockReader.read()).thenReturn('\u0023');
        assertTrue(lexer.isCommentStart('\u0023'));

        when(mockReader.read()).thenReturn('A');
        assertFalse(lexer.isCommentStart('A'));
    }

    @Test
    public void testNextToken() throws IOException {
        String testString = "\"field1\",field2,field3,\"field4\"";
        mockReader = new ExtendedBufferedReader(new StringReader(testString));
        Token token = new Token();

        lexer = new Lexer(format, mockReader);

        Token nextToken = lexer.nextToken(token);

        assertEquals(TokenType.STRING, nextToken.getType());
        assertEquals("field1", nextToken.getValue());

        nextToken = lexer.nextToken(token);

        assertEquals(TokenType.STRING, nextToken.getType());
        assertEquals("field2", nextToken.getValue());

        nextToken = lexer.nextToken(token);

        assertEquals(TokenType.NUMBER, nextToken.getType());
        assertEquals("field3", nextToken.getValue());

        nextToken = lexer.nextToken(token);

        assertEquals(TokenType.STRING, nextToken.getType());
        assertEquals("field4", nextToken.getValue());

        nextToken = lexer.nextToken(token);

        assertEquals(TokenType.END_OF_LINE, nextToken.getType());
    }
}