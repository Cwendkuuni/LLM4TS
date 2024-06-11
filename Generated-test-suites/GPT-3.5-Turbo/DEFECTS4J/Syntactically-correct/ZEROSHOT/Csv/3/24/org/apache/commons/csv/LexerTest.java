import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

public class LexerTest {

    private Lexer lexer;

    @Mock
    private CSVFormat csvFormat;

    @Mock
    private ExtendedBufferedReader reader;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        lexer = new Lexer(csvFormat, reader);
    }

    @Test
    public void testGetLineNumber() {
        when(reader.getLineNumber()).thenReturn(5L);
        long lineNumber = lexer.getLineNumber();
        assertEquals(5L, lineNumber);
    }

    @Test
    public void testReadEscape() throws IOException {
        when(reader.read()).thenReturn((int) '\\', (int) 'n');
        int escapedChar = lexer.readEscape();
        assertEquals('\n', escapedChar);
    }

    @Test(expected = IOException.class)
    public void testReadEscapeInvalidChar() throws IOException {
        when(reader.read()).thenReturn((int) '\\', (int) 'x');
        lexer.readEscape();
    }

    @Test
    public void testTrimTrailingSpaces() {
        StringBuilder buffer = new StringBuilder("trim spaces  ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("trim spaces", buffer.toString());
    }

    @Test
    public void testReadEndOfLine() throws IOException {
        when(reader.lookAhead()).thenReturn((int) '\n');
        boolean isEndOfLine = lexer.readEndOfLine((int) '\r');
        assertTrue(isEndOfLine);
    }

    @Test
    public void testIsWhitespace() {
        boolean isWhitespace = lexer.isWhitespace((int) ' ');
        assertTrue(isWhitespace);
    }

    @Test
    public void testIsStartOfLine() {
        boolean isStartOfLine = lexer.isStartOfLine(UNDEFINED);
        assertTrue(isStartOfLine);
    }

    @Test
    public void testIsEndOfFile() {
        boolean isEndOfFile = lexer.isEndOfFile(END_OF_STREAM);
        assertTrue(isEndOfFile);
    }

    @Test
    public void testIsDelimiter() {
        when(csvFormat.getDelimiter()).thenReturn(',');
        boolean isDelimiter = lexer.isDelimiter((int) ',');
        assertTrue(isDelimiter);
    }

    @Test
    public void testIsEscape() {
        when(csvFormat.getEscape()).thenReturn('\\');
        boolean isEscape = lexer.isEscape((int) '\\');
        assertTrue(isEscape);
    }

    @Test
    public void testIsQuoteChar() {
        when(csvFormat.getQuoteChar()).thenReturn('"');
        boolean isQuoteChar = lexer.isQuoteChar((int) '"');
        assertTrue(isQuoteChar);
    }

    @Test
    public void testIsCommentStart() {
        when(csvFormat.getCommentStart()).thenReturn('#');
        boolean isCommentStart = lexer.isCommentStart((int) '#');
        assertTrue(isCommentStart);
    }
}