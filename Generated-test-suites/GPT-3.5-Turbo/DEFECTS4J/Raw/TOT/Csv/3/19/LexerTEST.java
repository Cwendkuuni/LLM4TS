import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Before;
import org.junit.Test;

public class LexerTest {

    private CSVFormat format;
    private ExtendedBufferedReader in;
    private Lexer lexer;

    @Before
    public void setup() throws IOException {
        format = mock(CSVFormat.class);
        in = mock(ExtendedBufferedReader.class);
        lexer = new Lexer(format, in);
    }

    @Test
    public void testGetLineNumber() {
        long lineNumber = 5;
        in.setLineNumber(lineNumber);
        assertEquals(lineNumber, lexer.getLineNumber());
    }

    @Test
    public void testReadEscape() throws IOException {
        int escapeChar = '\\';
        in.read();
        in.setLastChar(escapeChar);
        assertEquals(escapeChar, lexer.readEscape());
    }

    @Test(expected = IOException.class)
    public void testReadEscapeInvalidChar() throws IOException {
        int escapeChar = '\\';
        in.read();
        in.setLastChar(escapeChar);
        lexer.readEscape();
    }

    @Test
    public void testTrimTrailingSpaces() {
        StringBuilder buffer = new StringBuilder("test ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("test", buffer.toString());
    }

    @Test
    public void testIsWhitespace() {
        int whitespaceChar = ' ';
        assertEquals(true, lexer.isWhitespace(whitespaceChar));
    }

    @Test
    public void testIsStartOfLine() {
        int startOfLineChar = '\n';
        assertEquals(true, lexer.isStartOfLine(startOfLineChar));
    }

    @Test
    public void testIsEndOfFile() {
        int endOfFileChar = -1;
        assertEquals(true, lexer.isEndOfFile(endOfFileChar));
    }

    @Test
    public void testIsDelimiter() {
        int delimiterChar = ',';
        assertEquals(true, lexer.isDelimiter(delimiterChar));
    }

    @Test
    public void testIsEscape() {
        int escapeChar = '\\';
        assertEquals(true, lexer.isEscape(escapeChar));
    }

    @Test
    public void testIsQuoteChar() {
        int quoteChar = '\"';
        assertEquals(true, lexer.isQuoteChar(quoteChar));
    }

    @Test
    public void testIsCommentStart() {
        int commentStartChar = '#';
        assertEquals(true, lexer.isCommentStart(commentStartChar));
    }
}