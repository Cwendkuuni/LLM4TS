import org.apache.commons.csv.Constants;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LexerTest {

    private Lexer lexer;
    private ExtendedBufferedReader mockReader;
    private CSVFormat format;

    @Before
    public void setUp() {
        format = new CSVFormat(CSVFormat.DEFAULT.withDelimiter(',').withHeader());
        mockReader = mock(ExtendedBufferedReader.class);
        lexer = new Lexer(format, mockReader);
    }

    @Test
    public void testGetLineNumber() throws IOException {
        when(mockReader.getLineNumber()).thenReturn(1);
        assertEquals(1, lexer.getLineNumber());
    }

    @Test
    public void testReadEscapeCR() throws IOException {
        when(mockReader.read()).thenReturn(Constants.CR);
        assertEquals(Constants.CR, lexer.readEscape());
    }

    @Test
    public void testReadEscapeLF() throws IOException {
        when(mockReader.read()).thenReturn(Constants.LF);
        assertEquals(Constants.LF, lexer.readEscape());
    }

    @Test(expected = IOException.class)
    public void testReadEscapeInvalidCharacter() throws IOException {
        when(mockReader.read()).thenReturn('X');
        lexer.readEscape();
    }

    @Test
    public void testReadEscapeNullEscapeCharacter() throws IOException {
        format.setEscape(null);
        assertEquals(Lexer.DISABLED, lexer.escape);
    }

    @Test
    public void testReadEscapeNullQuoteCharacter() throws IOException {
        format.setQuoteChar(null);
        assertEquals(Lexer.DISABLED, lexer.quoteChar);
    }

    @Test(expected = IOException.class)
    public void testReadEscapeEndOfStream() throws IOException {
        when(mockReader.read()).thenThrow(IOException.class);
        lexer.readEscape();
    }

    @Test
    public void testIsWhitespace() {
        assertTrue(lexer.isWhitespace(' '));
        assertTrue(lexer.isWhitespace('\t'));
        assertTrue(lexer.isWhitespace('\n'));
        assertFalse(lexer.isWhitespace(lexer.delimiter));
    }

    @Test
    public void testIsStartOfLine() {
        assertFalse(lexer.isStartOfLine(lexer.delimiter));
        assertTrue(lexer.isStartOfLine(Constants.LF));
        assertTrue(lexer.isStartOfLine(Constants.CR));
        assertTrue(lexer.isStartOfLine(Constants.UNDEFINED));
    }

    @Test
    public void testIsEndOfFile() {
        assertFalse(lexer.isEndOfFile(lexer.delimiter));
        assertFalse(lexer.isEndOfFile(Constants.CR));
        assertFalse(lexer.isEndOfFile(Constants.LF));
        assertFalse(lexer.isEndOfFile(Constants.UNDEFINED));
        assertTrue(lexer.isEndOfFile(Constants.END_OF_STREAM));
    }

    @Test
    public void testIsDelimiter() {
        assertFalse(lexer.isDelimiter(' '));
        assertFalse(lexer.isDelimiter('\n'));
        assertFalse(lexer.isDelimiter('\t'));
        assertFalse(lexer.isDelimiter(Constants.CR));
        assertFalse(lexer.isDelimiter(Constants.LF));
        assertTrue(lexer.isDelimiter(lexer.delimiter));
    }

    @Test
    public void testIsEscape() {
        assertFalse(lexer.isEscape(' '));
        assertFalse(lexer.isEscape('\n'));
        assertFalse(lexer.isEscape('\t'));
        assertFalse(lexer.isEscape(Constants.CR));
        assertFalse(lexer.isEscape(Constants.LF));
        assertFalse(lexer.isEscape(lexer.delimiter));
        assertTrue(lexer.isEscape(lexer.escape));
    }

    @Test
    public void testIsQuoteChar() {
        assertFalse(lexer.isQuoteChar(' '));
        assertFalse(lexer.isQuoteChar('\n'));
        assertFalse(lexer.isQuoteChar('\t'));
        assertFalse(lexer.isQuoteChar(lexer.delimiter));
        assertFalse(lexer.isQuoteChar(lexer.escape));
        assertTrue(lexer.isQuoteChar(lexer.quoteChar));
    }

    @Test
    public void testIsCommentStart() {
        assertFalse(lexer.isCommentStart(' '));
        assertFalse(lexer.isCommentStart('\n'));
        assertFalse(lexer.isCommentStart('\t'));
        assertFalse(lexer.isCommentStart(lexer.delimiter));
        assertFalse(lexer.isCommentStart(lexer.escape));
        assertFalse(lexer.isCommentStart(lexer.quoteChar));
        assertTrue(lexer.isCommentStart(lexer.commmentStart));
    }
}