import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LexerTest {

    private Lexer lexer;

    @Before
    public void setup() {
        lexer = new Lexer(format, in); // Create an instance of the Lexer using mock CSVFormat and ExtendedBufferedReader
    }

    @Test
    public void testGetLineNumber() {
        long lineNumber = lexer.getLineNumber();
        assertEquals(0, lineNumber); // Line number should be 0 as it is the start of the file
    }

    @Test
    public void testReadEscape() {
        try {
            int escapedChar = lexer.readEscape();
            assertEquals(LF, escapedChar); // Escape character 'n' should return LF
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testTrimTrailingSpaces() {
        StringBuilder buffer = new StringBuilder("Hello World  ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("Hello World", buffer.toString()); // Trailing spaces should be removed
    }

    @Test
    public void testReadEndOfLine_CRLF() {
        try {
            boolean isEndOfLine = lexer.readEndOfLine(CR);
            assertTrue(isEndOfLine); // CR followed by LF should be considered end of line
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testReadEndOfLine_LF() {
        try {
            boolean isEndOfLine = lexer.readEndOfLine(LF);
            assertTrue(isEndOfLine); // LF alone should be considered end of line
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testIsWhitespace() {
        boolean isWhitespace = lexer.isWhitespace(TAB);
        assertTrue(isWhitespace); // TAB should be considered whitespace character
    }

    @Test
    public void testIsStartOfLine() {
        boolean isStartOfLine = lexer.isStartOfLine(UNDEFINED);
        assertTrue(isStartOfLine); // UNDEFINED should be considered start of line
    }

    @Test
    public void testIsEndOfFile() {
        boolean isEndOfFile = lexer.isEndOfFile(END_OF_STREAM);
        assertTrue(isEndOfFile); // END_OF_STREAM should be considered end of file
    }

    @Test
    public void testIsDelimiter() {
        boolean isDelimiter = lexer.isDelimiter(delimiter);
        assertTrue(isDelimiter); // delimiter should be considered as delimiter character
    }

    @Test
    public void testIsEscape() {
        boolean isEscape = lexer.isEscape(escape);
        assertTrue(isEscape); // escape character should be considered as escape character
    }

    @Test
    public void testIsQuoteChar() {
        boolean isQuoteChar = lexer.isQuoteChar(quoteChar);
        assertTrue(isQuoteChar); // quote character should be considered as quote character
    }

    @Test
    public void testIsCommentStart() {
        boolean isCommentStart = lexer.isCommentStart(commmentStart);
        assertTrue(isCommentStart); // comment start character should be considered as comment start character
    }
}