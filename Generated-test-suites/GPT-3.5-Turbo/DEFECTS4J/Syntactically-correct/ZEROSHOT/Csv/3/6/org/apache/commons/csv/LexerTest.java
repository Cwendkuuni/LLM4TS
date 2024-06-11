import static org.mockito.Mockito.*;

import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.Token;
import org.junit.Before;
import org.junit.Test;

public class LexerTest {

    private Lexer lexer;
    private CSVFormat format;
    private ExtendedBufferedReader in;

    @Before
    public void setUp() {
        format = mock(CSVFormat.class);
        in = mock(ExtendedBufferedReader.class);
        lexer = new Lexer(format, in) {
            @Override
            Token nextToken(Token reusableToken) throws IOException {
                return null;
            }
        };
    }

    @Test
    public void testGetLineNumber() {
        lexer.getLineNumber();
        verify(in, times(1)).getLineNumber();
    }

    @Test
    public void testReadEscape() throws IOException {
        lexer.readEscape();
        verify(in, times(1)).read();
    }

    @Test
    public void testTrimTrailingSpaces() {
        StringBuilder buffer = new StringBuilder();
        lexer.trimTrailingSpaces(buffer);
        verify(format, never()).getDelimiter();
    }

    @Test
    public void testReadEndOfLine() throws IOException {
        lexer.readEndOfLine(10);
        verify(in, times(1)).lookAhead();
        verify(in, times(1)).read();
    }

    @Test
    public void testIsWhitespace() {
        lexer.isWhitespace(32);
        verify(format, times(1)).getDelimiter();
        verify(format, never()).getEscape();
        verify(format, never()).getQuoteChar();
        verify(format, never()).getCommentStart();
    }

    @Test
    public void testIsStartOfLine() {
        lexer.isStartOfLine(10);
        verify(format, times(1)).getDelimiter();
        verify(format, never()).getEscape();
        verify(format, never()).getQuoteChar();
        verify(format, never()).getCommentStart();
    }

    @Test
    public void testIsEndOfFile() {
        lexer.isEndOfFile(3);
        verify(format, times(1)).getDelimiter();
        verify(format, never()).getEscape();
        verify(format, never()).getQuoteChar();
        verify(format, never()).getCommentStart();
    }

    @Test
    public void testIsDelimiter() {
        lexer.isDelimiter(44);
        verify(format, times(1)).getDelimiter();
        verify(format, never()).getEscape();
        verify(format, never()).getQuoteChar();
        verify(format, never()).getCommentStart();
    }

    @Test
    public void testIsEscape() {
        lexer.isEscape(92);
        verify(format, times(1)).getEscape();
        verify(format, never()).getDelimiter();
        verify(format, never()).getQuoteChar();
        verify(format, never()).getCommentStart();
    }

    @Test
    public void testIsQuoteChar() {
        lexer.isQuoteChar(34);
        verify(format, times(1)).getQuoteChar();
        verify(format, never()).getDelimiter();
        verify(format, never()).getEscape();
        verify(format, never()).getCommentStart();
    }

    @Test
    public void testIsCommentStart() {
        lexer.isCommentStart(35);
        verify(format, times(1)).getCommentStart();
        verify(format, never()).getDelimiter();
        verify(format, never()).getEscape();
        verify(format, never()).getQuoteChar();
    }
}