import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LexerTest {

    private Lexer lexer;

    @Before
    public void setUp() {
        lexer = new Lexer(format, in);
    }

    @Test
    public void testGetLineNumber() {
        long lineNumber = lexer.getLineNumber();
        assertEquals(0, lineNumber);
    }

    @Test
    public void testReadEscape() {
        int escapeChar = lexer.readEscape();
        assertEquals(0, escapeChar);
    }

    @Test
    public void testTrimTrailingSpaces() {
        StringBuilder buffer = new StringBuilder("   test   ");
        lexer.trimTrailingSpaces(buffer);
        assertEquals("   test", buffer.toString());
    }

    @Test
    public void testReadEndOfLine() {
        boolean endOfLine = lexer.readEndOfLine(10);
        assertTrue(endOfLine);
    }

    @Test
    public void testIsWhitespace() {
        boolean whitespace = lexer.isWhitespace(32);
        assertTrue(whitespace);
    }

    @Test
    public void testIsStartOfLine() {
        boolean startOfLine = lexer.isStartOfLine(10);
        assertTrue(startOfLine);
    }

    @Test
    public void testIsEndOfFile() {
        boolean endOfFile = lexer.isEndOfFile(10);
        assertFalse(endOfFile);
    }

    @Test
    public void testIsDelimiter() {
        boolean delimiter = lexer.isDelimiter(44);
        assertFalse(delimiter);
    }

    @Test
    public void testIsEscape() {
        boolean escape = lexer.isEscape(92);
        assertFalse(escape);
    }

    @Test
    public void testIsQuoteChar() {
        boolean quoteChar = lexer.isQuoteChar(34);
        assertFalse(quoteChar);
    }

    @Test
    public void testIsCommentStart() {
        boolean commentStart = lexer.isCommentStart(35);
        assertFalse(commentStart);
    }

    @Test
    public void testNextToken() {
        Token reusableToken = new Token();
        Token nextToken = lexer.nextToken(reusableToken);
        assertNotNull(nextToken);
    }
}