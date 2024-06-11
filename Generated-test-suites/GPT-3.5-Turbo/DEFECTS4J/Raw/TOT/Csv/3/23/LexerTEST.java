import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LexerTest {

    @Test
    void testGetLineNumber() {
        ExtendedBufferedReader reader = mock(ExtendedBufferedReader.class);
        when(reader.getLineNumber()).thenReturn(5L);
        CSVFormat format = mock(CSVFormat.class);
        Lexer lexer = new Lexer(format, reader);

        long lineNumber = lexer.getLineNumber();

        Assertions.assertEquals(5L, lineNumber);
    }

    @Test
    void testIsWhitespace() {
        CSVFormat format = mock(CSVFormat.class);
        Lexer lexer = new Lexer(format, null);

        boolean result = lexer.isWhitespace(' ');

        Assertions.assertTrue(result);
    }

    @Test
    void testIsStartOfLine() {
        CSVFormat format = mock(CSVFormat.class);
        Lexer lexer = new Lexer(format, null);

        boolean result = lexer.isStartOfLine('\n');

        Assertions.assertTrue(result);
    }

    @Test
    void testIsEndOfFile() {
        CSVFormat format = mock(CSVFormat.class);
        Lexer lexer = new Lexer(format, null);

        boolean result = lexer.isEndOfFile(-1);

        Assertions.assertTrue(result);
    }

    @Test
    void testIsDelimiter() {
        CSVFormat format = mock(CSVFormat.class);
        Lexer lexer = new Lexer(format, null);
        when(format.getDelimiter()).thenReturn(',');

        boolean result = lexer.isDelimiter(',');

        Assertions.assertTrue(result);
    }

    @Test
    void testIsEscape() {
        CSVFormat format = mock(CSVFormat.class);
        Lexer lexer = new Lexer(format, null);
        when(format.getEscape()).thenReturn('\\');

        boolean result = lexer.isEscape('\\');

        Assertions.assertTrue(result);
    }

    @Test
    void testIsQuoteChar() {
        CSVFormat format = mock(CSVFormat.class);
        Lexer lexer = new Lexer(format, null);
        when(format.getQuoteChar()).thenReturn('\"');

        boolean result = lexer.isQuoteChar('\"');

        Assertions.assertTrue(result);
    }

    @Test
    void testIsCommentStart() {
        CSVFormat format = mock(CSVFormat.class);
        Lexer lexer = new Lexer(format, null);
        when(format.getCommentStart()).thenReturn('#');

        boolean result = lexer.isCommentStart('#');

        Assertions.assertTrue(result);
    }

    @Test
    void testReadEscape_valid() throws IOException {
        ExtendedBufferedReader reader = mock(ExtendedBufferedReader.class);
        when(reader.read()).thenReturn((int) 'n');
        when(reader.getLastChar()).thenReturn('n');
        CSVFormat format = mock(CSVFormat.class);
        Lexer lexer = new Lexer(format, reader);

        int escapedChar = lexer.readEscape();

        Assertions.assertEquals((int) 'n', escapedChar);
    }

    @Test
    void testReadEscape_invalid_eof() {
        ExtendedBufferedReader reader = mock(ExtendedBufferedReader.class);
        when(reader.read()).thenReturn(-1);
        CSVFormat format = mock(CSVFormat.class);
        Lexer lexer = new Lexer(format, reader);

        Assertions.assertThrows(IOException.class, lexer::readEscape);
    }

    @Test
    void testReadEscape_notValid() throws IOException {
        ExtendedBufferedReader reader = mock(ExtendedBufferedReader.class);
        when(reader.read()).thenReturn((int) '-');
        when(reader.getLastChar()).thenReturn('-');
        CSVFormat format = mock(CSVFormat.class);
        Lexer lexer = new Lexer(format, reader);

        int escapedChar = lexer.readEscape();

        Assertions.assertEquals((int) '-', escapedChar);
    }

    @Test
    void testTrimTrailingSpaces() {
        StringBuilder buffer = new StringBuilder("example  ");
        CSVFormat format = mock(CSVFormat.class);
        Lexer lexer = new Lexer(format, null);

        lexer.trimTrailingSpaces(buffer);

        Assertions.assertEquals("example", buffer.toString());
    }

    @Test
    void testReadEndOfLine_LF() throws IOException {
        ExtendedBufferedReader reader = mock(ExtendedBufferedReader.class);
        when(reader.read()).thenReturn((int) '\n');
        when(reader.lookAhead()).thenReturn(-1);
        CSVFormat format = mock(CSVFormat.class);
        Lexer lexer = new Lexer(format, reader);

        boolean isEndOfLine = lexer.readEndOfLine('\n');

        Assertions.assertTrue(isEndOfLine);
    }

    @Test
    void testReadEndOfLine_CRLF() throws IOException {
        ExtendedBufferedReader reader = mock(ExtendedBufferedReader.class);
        when(reader.read()).thenReturn((int) '\r');
        when(reader.lookAhead()).thenReturn((int) '\n');
        CSVFormat format = mock(CSVFormat.class);
        Lexer lexer = new Lexer(format, reader);

        boolean isEndOfLine = lexer.readEndOfLine('\r');

        Assertions.assertTrue(isEndOfLine);
    }

    @Test
    void testIsDelimiter_delimiterChar() {
        CSVFormat format = mock(CSVFormat.class);
        Lexer lexer = new Lexer(format, null);
        when(format.getDelimiter()).thenReturn(',');

        boolean result = lexer.isDelimiter(',');

        Assertions.assertTrue(result);
    }

    @Test
    void testIsDelimiter_notDelimiterChar() {
        CSVFormat format = mock(CSVFormat.class);
        Lexer lexer = new Lexer(format, null);
        when(format.getDelimiter()).thenReturn(',');

        boolean result = lexer.isDelimiter(';');

        Assertions.assertFalse(result);
    }

    @Test
    void testIsEscape_escapeChar() {
        CSVFormat format = mock(CSVFormat.class);
        Lexer lexer = new Lexer(format, null);
        when(format.getEscape()).thenReturn('\\');

        boolean result = lexer.isEscape('\\');

        Assertions.assertTrue(result);
    }

    @Test
    void testIsEscape_notEscapeChar() {
        CSVFormat format = mock(CSVFormat.class);
        Lexer lexer = new Lexer(format, null);
        when(format.getEscape()).thenReturn('\\');

        boolean result = lexer.isEscape('/');

        Assertions.assertFalse(result);
    }

    @Test
    void testIsQuoteChar_quoteChar() {
        CSVFormat format = mock(CSVFormat.class);
        Lexer lexer = new Lexer(format, null);
        when(format.getQuoteChar()).thenReturn('\"');

        boolean result = lexer.isQuoteChar('\"');

        Assertions.assertTrue(result);
    }

    @Test
    void testIsQuoteChar_notQuoteChar() {
        CSVFormat format = mock(CSVFormat.class);
        Lexer lexer = new Lexer(format, null);
        when(format.getQuoteChar()).thenReturn('\"');

        boolean result = lexer.isQuoteChar('\'');

        Assertions.assertFalse(result);
    }

    @Test
    void testIsCommentStart_commentStartChar() {
        CSVFormat format = mock(CSVFormat.class);
        Lexer lexer = new Lexer(format, null);
        when(format.getCommentStart()).thenReturn('#');

        boolean result = lexer.isCommentStart('#');

        Assertions.assertTrue(result);
    }

    @Test
    void testIsCommentStart_notCommentStartChar() {
        CSVFormat format = mock(CSVFormat.class);
        Lexer lexer = new Lexer(format, null);
        when(format.getCommentStart()).thenReturn('#');

        boolean result = lexer.isCommentStart('@');

        Assertions.assertFalse(result);
    }

    @Test
    void testNextToken() throws IOException {
        ExtendedBufferedReader reader = mock(ExtendedBufferedReader.class);
        when(reader.getLastChar()).thenReturn('a');
        CSVFormat format = mock(CSVFormat.class);
        Lexer lexer = new Lexer(format, reader);
        Token token = new Token();

        Token nextToken = lexer.nextToken(token);

        Assertions.assertEquals(token, nextToken);
    }
}