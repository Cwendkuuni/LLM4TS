package org.apache.commons.csv;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.csv.Constants;
import org.apache.commons.csv.Lexer;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

public class LexerTest {

    @Test
    public void testIsStartOfLine() {
        Lexer lexer = Mockito.spy(new Lexer(CSVFormat.DEFAULT, Mockito.mock(ExtendedBufferedReader.class)));

        assertTrue(lexer.isStartOfLine(Constants.LF));
        assertTrue(lexer.isStartOfLine(Constants.CR));
        assertTrue(lexer.isStartOfLine(Constants.UNDEFINED));

        Mockito.doReturn(Constants.LF).when(Mockito.mock(ExtendedBufferedReader.class)).getLineEnd();
        assertTrue(lexer.isStartOfLine(Constants.UNDEFINED));

        Mockito.doReturn(Constants.CR).when(Mockito.mock(ExtendedBufferedReader.class)).getLineEnd();
        assertTrue(lexer.isStartOfLine(Constants.UNDEFINED));
    }

    @Test
    public void testIsEndOfFile() {
        Lexer lexer = Mockito.spy(new Lexer(CSVFormat.DEFAULT, Mockito.mock(ExtendedBufferedReader.class)));

        assertFalse(lexer.isEndOfFile(Constants.UNDEFINED));
        Mockito.doThrow(new IOException("Mock exception")).when(Mockito.mock(ExtendedBufferedReader.class)).read();
        assertTrue(lexer.isEndOfFile(Constants.UNDEFINED));
    }

    @Test
    public void testIsDelimiter() {
        Lexer lexer = Mockito.spy(new Lexer(CSVFormat.DEFAULT, Mockito.mock(ExtendedBufferedReader.class)));
        lexer.delimiter = ';';

        assertFalse(lexer.isDelimiter(','));
        assertTrue(lexer.isDelimiter(';'));
    }

    @Test
    public void testIsEscape() {
        Lexer lexer = Mockito.spy(new Lexer(CSVFormat.DEFAULT, Mockito.mock(ExtendedBufferedReader.class)));
        lexer.escape = '\\';

        assertFalse(lexer.isEscape(','));
        assertFalse(lexer.isEscape('\\'));
        assertTrue(lexer.isEscape('\u001E')); // Alt+End
        assertTrue(lexer.isEscape('\u001F')); // Del
    }

    @Test
    public void testIsQuoteChar() {
        Lexer lexer = Mockito.spy(new Lexer(CSVFormat.DEFAULT, Mockito.mock(ExtendedBufferedReader.class)));
        lexer.quoteChar = '\'';

        assertFalse(lexer.isQuoteChar('"'));
        assertFalse(lexer.isQuoteChar('\''));
        assertTrue(lexer.isQuoteChar('\''));
    }

    @Test
    public void testIsCommentStart() {
        Lexer lexer = Mockito.spy(new Lexer(CSVFormat.DEFAULT, Mockito.mock(ExtendedBufferedReader.class)));
        lexer.commmentStart = '#';

        assertFalse(lexer.isCommentStart('@'));
        assertFalse(lexer.isCommentStart('#'));
        assertTrue(lexer.isCommentStart('#'));
    }

    @Test
    public void testReadEndOfLine() {
        Lexer lexer = Mockito.spy(new Lexer(CSVFormat.DEFAULT, Mockito.mock(ExtendedBufferedReader.class)));

        Mockito.doReturn(Constants.CR).when(Mockito.mock(ExtendedBufferedReader.class)).read();
        Mockito.doReturn(null).when(Mockito.mock(ExtendedBufferedReader.class)).lookAhead();
        assertFalse(lexer.readEndOfLine(Constants.CR));

        Mockito.doReturn(Constants.CR).when(Mockito.mock(ExtendedBufferedReader.class)).lookAhead();
        Mockito.doReturn(Constants.LF).when(Mockito.mock(ExtendedBufferedReader.class)).read();
        assertTrue(lexer.readEndOfLine(Constants.CR));

        Mockito.doReturn(Constants.LF).when(Mockito.mock(ExtendedBufferedReader.class)).read();
        Mockito.doReturn(null).when(Mockito.mock(ExtendedBuffferedReader.class)).lookAhead();
        assertFalse(lexer.readEndOfLine(Constants.LF));
    }

    @Test
    public void testTrimTrailingSpaces() {
        StringBuilder buffer = new StringBuilder(" foo  \t\t bar");
        Lexer lexer = Mockito.mock(Lexer.class);

        lexer.trimTrailingSpaces(buffer);
        assertEquals("foo bar", buffer.toString());
    }
}