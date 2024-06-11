import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.Token;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class LexerTest {
    private CSVFormat format;
    private ExtendedBufferedReader reader;
    private Lexer lexer;

    @Before
    public void setup() {
        format = mock(CSVFormat.class);
        reader = mock(ExtendedBufferedReader.class);
        lexer = new LexerImpl(format, reader);
    }

    @Test
    public void testGetLineNumber() {
        when(reader.getLineNumber()).thenReturn(10L);
        assertEquals(10L, lexer.getLineNumber());
    }

    @Test
    public void testReadEscapeWithValidEscapeSequence() throws IOException {
        when(reader.read()).thenReturn((int) '\\', (int) 'n');
        int result = lexer.readEscape();
        assertEquals('\n', result);
        verify(reader, times(2)).read();
    }

    @Test(expected = IOException.class)
    public void testReadEscapeWithInvalidEscapeSequence() throws IOException {
        when(reader.read()).thenReturn((int) '\\', (int) 'x');
        lexer.readEscape();
    }

    @Test
    public void testTrimTrailingSpacesWithNoSpaces() {
        StringBuilder buffer = new StringBuilder("hello,world");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("hello,world", buffer.toString());
    }

    @Test
    public void testTrimTrailingSpacesWithSpaces() {
        StringBuilder buffer = new StringBuilder("hello,world   ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("hello,world", buffer.toString());
    }

    @Test
    public void testReadEndOfLineWithLF() throws IOException {
        when(reader.lookAhead()).thenReturn((int) '\n');
        when(reader.read()).thenReturn((int) '\n');
        boolean result = lexer.readEndOfLine((int) '\n');
        assertEquals(true, result);
        verify(reader).lookAhead();
        verify(reader).read();
    }

    @Test
    public void testReadEndOfLineWithCR() throws IOException {
        when(reader.lookAhead()).thenReturn((int) '\n');
        when(reader.read()).thenReturn((int) '\r');
        boolean result = lexer.readEndOfLine((int) '\r');
        assertEquals(true, result);
        verify(reader).lookAhead();
        verify(reader).read();
    }

    @Test
    public void testReadEndOfLineWithCRLF() throws IOException {
        when(reader.lookAhead()).thenReturn((int) '\n');
        when(reader.read()).thenReturn((int) '\r');
        boolean result = lexer.readEndOfLine((int) '\r');
        assertEquals(true, result);
        verify(reader).lookAhead();
        verify(reader).read();
    }

    @Test
    public void testReadEndOfLineWithOtherCharacter() throws IOException {
        when(reader.lookAhead()).thenReturn((int) '\n');
        boolean result = lexer.readEndOfLine((int) 'a');
        assertEquals(false, result);
    }

    @Test
    public void testIsWhitespaceWithWhitespaceCharacter() {
        boolean result = lexer.isWhitespace((int) ' ');
        assertEquals(true, result);
    }

    @Test
    public void testIsWhitespaceWithNonWhitespaceCharacter() {
        boolean result = lexer.isWhitespace((int) 'a');
        assertEquals(false, result);
    }

    @Test
    public void testIsStartOfLineWithLineTerminator() {
        boolean result = lexer.isStartOfLine((int) '\n');
        assertEquals(true, result);
    }

    @Test
    public void testIsStartOfLineWithCarriageReturn() {
        boolean result = lexer.isStartOfLine((int) '\r');
        assertEquals(true, result);
    }

    @Test
    public void testIsStartOfLineWithUndefined() {
        boolean result = lexer.isStartOfLine((int) '\uffff');
        assertEquals(true, result);
    }

    @Test
    public void testIsStartOfLineWithOtherCharacter() {
        boolean result = lexer.isStartOfLine((int) 'a');
        assertEquals(false, result);
    }

    @Test
    public void testIsEndOfFileWithEndOfStream() {
        boolean result = lexer.isEndOfFile((int) '\uffff');
        assertEquals(true, result);
    }

    @Test
    public void testIsEndOfFileWithOtherCharacter() {
        boolean result = lexer.isEndOfFile((int) 'a');
        assertEquals(false, result);
    }

    @Test
    public void testIsDelimiterWithDelimiterCharacter() {
        when(format.getDelimiter()).thenReturn(',');
        boolean result = lexer.isDelimiter((int) ',');
        assertEquals(true, result);
    }

    @Test
    public void testIsDelimiterWithNonDelimiterCharacter() {
        when(format.getDelimiter()).thenReturn(',');
        boolean result = lexer.isDelimiter((int) ';');
        assertEquals(false, result);
    }

    @Test
    public void testIsEscapeWithEscapeCharacter() {
        when(format.getEscape()).thenReturn('\\');
        boolean result = lexer.isEscape((int) '\\');
        assertEquals(true, result);
    }

    @Test
    public void testIsEscapeWithNonEscapeCharacter() {
        when(format.getEscape()).thenReturn('\\');
        boolean result = lexer.isEscape((int) '/');
        assertEquals(false, result);
    }

    @Test
    public void testIsQuoteCharWithQuoteCharacter() {
        when(format.getQuoteChar()).thenReturn('"');
        boolean result = lexer.isQuoteChar((int) '"');
        assertEquals(true, result);
    }

    @Test
    public void testIsQuoteCharWithNonQuoteCharacter() {
        when(format.getQuoteChar()).thenReturn('"');
        boolean result = lexer.isQuoteChar((int) '\'');
        assertEquals(false, result);
    }

    @Test
    public void testIsCommentStartWithCommentStartCharacter() {
        when(format.getCommentStart()).thenReturn('#');
        boolean result = lexer.isCommentStart((int) '#');
        assertEquals(true, result);
    }

    @Test
    public void testIsCommentStartWithNonCommentStartCharacter() {
        when(format.getCommentStart()).thenReturn('#');
        boolean result = lexer.isCommentStart((int) '/');
        assertEquals(false, result);
    }

    private class LexerImpl extends Lexer {
        LexerImpl(CSVFormat format, ExtendedBufferedReader in) {
            super(format, in);
        }

        @Override
        Token nextToken(Token reusableToken) throws IOException {
            return null;
        }
    }
}