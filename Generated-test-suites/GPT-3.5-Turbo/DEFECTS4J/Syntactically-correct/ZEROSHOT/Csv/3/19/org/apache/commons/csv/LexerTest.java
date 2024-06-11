import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import static org.mockito.Mockito.*;

public class LexerTest {
    private CSVFormat format;
    private ExtendedBufferedReader in;
    private Lexer lexer;

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
        when(in.getLineNumber()).thenReturn(10L);

        long lineNumber = lexer.getLineNumber();

        assertEquals(10L, lineNumber);
    }

    @Test
    public void testReadEscape() throws IOException {
        when(in.read()).thenReturn((int) 't');

        int escapedChar = lexer.readEscape();

        assertEquals((int) '\t', escapedChar);
    }

    @Test(expected = IOException.class)
    public void testReadEscapeWithEOF() throws IOException {
        when(in.read()).thenReturn(Lexer.END_OF_STREAM);

        lexer.readEscape();
    }

    @Test
    public void testTrimTrailingSpaces() {
        StringBuilder buffer = new StringBuilder("test  ");

        lexer.trimTrailingSpaces(buffer);

        assertEquals("test", buffer.toString());
    }

    @Test
    public void testReadEndOfLine() throws IOException {
        when(in.lookAhead()).thenReturn((int) '\n');

        boolean endOfLine = lexer.readEndOfLine((int) '\r');

        assertTrue(endOfLine);
    }

    @Test
    public void testIsWhitespace() {
        boolean whitespace = lexer.isWhitespace((int) ' ');

        assertTrue(whitespace);
    }

    @Test
    public void testIsStartOfLine() {
        boolean startOfLine = lexer.isStartOfLine(Lexer.UNDEFINED);

        assertTrue(startOfLine);
    }

    @Test
    public void testIsEndOfFile() {
        boolean endOfFile = lexer.isEndOfFile(Lexer.END_OF_STREAM);

        assertTrue(endOfFile);
    }

    @Test
    public void testIsDelimiter() {
        when(format.getDelimiter()).thenReturn(',');

        boolean delimiter = lexer.isDelimiter((int) ',');

        assertTrue(delimiter);
    }

    @Test
    public void testIsEscape() {
        when(format.getEscape()).thenReturn('\\');

        boolean escape = lexer.isEscape((int) '\\');

        assertTrue(escape);
    }

    @Test
    public void testIsQuoteChar() {
        when(format.getQuoteChar()).thenReturn('"');

        boolean quoteChar = lexer.isQuoteChar((int) '"');

        assertTrue(quoteChar);
    }

    @Test
    public void testIsCommentStart() {
        when(format.getCommentStart()).thenReturn('#');

        boolean commentStart = lexer.isCommentStart((int) '#');

        assertTrue(commentStart);
    }
}