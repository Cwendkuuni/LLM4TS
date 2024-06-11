import org.junit.Test;
import static org.junit.Assert.*;

public class LexerTest {

    @Test
    public void testGetLineNumber() {
        Lexer lexer = new Lexer();
        long lineNumber = lexer.getLineNumber();
        assertEquals(0, lineNumber);
    }

    @Test
    public void testReadEscape() {
        Lexer lexer = new Lexer();
        try {
            int escapedChar = lexer.readEscape();
            assertEquals(0, escapedChar);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testTrimTrailingSpaces() {
        Lexer lexer = new Lexer();
        StringBuilder buffer = new StringBuilder("   test   ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("   test", buffer.toString());
    }

    @Test
    public void testReadEndOfLine() {
        Lexer lexer = new Lexer();
        try {
            boolean isEndOfLine = lexer.readEndOfLine(LF);
            assertTrue(isEndOfLine);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testIsWhitespace() {
        Lexer lexer = new Lexer();
        boolean isWhitespace = lexer.isWhitespace(LF);
        assertTrue(isWhitespace);
    }

    @Test
    public void testIsStartOfLine() {
        Lexer lexer = new Lexer();
        boolean isStartOfLine = lexer.isStartOfLine(LF);
        assertTrue(isStartOfLine);
    }

    @Test
    public void testIsEndOfFile() {
        Lexer lexer = new Lexer();
        boolean isEndOfFile = lexer.isEndOfFile(END_OF_STREAM);
        assertTrue(isEndOfFile);
    }

    @Test
    public void testIsDelimiter() {
        Lexer lexer = new Lexer();
        boolean isDelimiter = lexer.isDelimiter(delimiter);
        assertTrue(isDelimiter);
    }

    @Test
    public void testIsEscape() {
        Lexer lexer = new Lexer();
        boolean isEscape = lexer.isEscape(escape);
        assertTrue(isEscape);
    }

    @Test
    public void testIsQuoteChar() {
        Lexer lexer = new Lexer();
        boolean isQuoteChar = lexer.isQuoteChar(quoteChar);
        assertTrue(isQuoteChar);
    }

    @Test
    public void testIsCommentStart() {
        Lexer lexer = new Lexer();
        boolean isCommentStart = lexer.isCommentStart(commmentStart);
        assertTrue(isCommentStart);
    }
}