package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

@RunWith(MockitoJUnitRunner.class)
public class LexerTest {

    @Test
    public void testGetLineNumber() {
        ExtendedBufferedReader mockReader = mock(ExtendedBufferedReader.class);
        when(mockReader.getLineNumber()).thenReturn(1L);

        CSVFormat format = CSVFormat.DEFAULT;
        Lexer lexer = new Lexer(format, mockReader);

        assertEquals(1L, lexer.getLineNumber());
    }

    @Test
    public void testReadEscape() {
        ExtendedBufferedReader mockReader = mock(ExtendedBufferedReader.class);
        when(mockReader.read()).thenReturn((int) '\\');

        CSVFormat format = CSVFormat.DEFAULT;
        Lexer lexer = new Lexer(format, mockReader);

        try {
            int escapedChar = lexer.readEscape();
            assertEquals('\\', escapedChar);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTrimTrailingSpaces() {
        StringBuilder buffer = new StringBuilder("test   ");
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);

        lexer.trimTrailingSpaces(buffer);

        assertEquals("test", buffer.toString());
    }

    @Test
    public void testReadEndOfLine() {
        ExtendedBufferedReader mockReader = mock(ExtendedBufferedReader.class);
        when(mockReader.lookAhead()).thenReturn((int) '\n');
        when(mockReader.read()).thenReturn((int) '\r');

        CSVFormat format = CSVFormat.DEFAULT;
        Lexer lexer = new Lexer(format, mockReader);

        try {
            assertTrue(lexer.readEndOfLine('\r'));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testIsWhitespace() {
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);

        assertTrue(lexer.isWhitespace(' '));
        assertFalse(lexer.isWhitespace(','));
    }

    @Test
    public void testIsStartOfLine() {
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);

        assertTrue(lexer.isStartOfLine('\n'));
        assertTrue(lexer.isStartOfLine('\r'));
        assertTrue(lexer.isStartOfLine('\uffff'));
        assertFalse(lexer.isStartOfLine('a'));
    }

    @Test
    public void testIsEndOfFile() {
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);

        assertTrue(lexer.isEndOfFile(ExtendedBufferedReader.END_OF_STREAM));
        assertFalse(lexer.isEndOfFile('a'));
    }

    @Test
    public void testIsDelimiter() {
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);

        assertTrue(lexer.isDelimiter(','));
        assertFalse(lexer.isDelimiter(';'));
    }

    @Test
    public void testIsEscape() {
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);

        assertTrue(lexer.isEscape('\\'));
        assertFalse(lexer.isEscape('/'));
    }

    @Test
    public void testIsQuoteChar() {
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);

        assertTrue(lexer.isQuoteChar('"'));
        assertFalse(lexer.isQuoteChar('\''));
    }

    @Test
    public void testIsCommentStart() {
        Lexer lexer = new Lexer(CSVFormat.DEFAULT, null);

        assertTrue(lexer.isCommentStart('#'));
        assertFalse(lexer.isCommentStart('/'));
    }
}