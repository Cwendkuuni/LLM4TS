import org.junit.Test;
import static org.junit.Assert.*;

public class LexerTest {

    @Test
    public void testGetLineNumber() {
        Lexer lexer = new Lexer(format, in);
        long lineNumber = lexer.getLineNumber();
        // assert the initial line number
        assertEquals(0, lineNumber);
    }

    @Test
    public void testReadEscape() {
        try {
            Lexer lexer = new Lexer(format, in);
            int escapedChar = lexer.readEscape();
            // assert the escaped character
            assertEquals(expectedChar, escapedChar);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testTrimTrailingSpaces() {
        Lexer lexer = new Lexer(format, in);
        StringBuilder buffer = new StringBuilder("test string ");
        lexer.trimTrailingSpaces(buffer);
        // assert the trimmed string
        assertEquals("test string", buffer.toString());
    }

    @Test
    public void testReadEndOfLine() {
        try {
            Lexer lexer = new Lexer(format, in);
            boolean isEndOfLine = lexer.readEndOfLine(c);
            // assert if the character represents end of line
            assertEquals(expectedResult, isEndOfLine);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testIsWhitespace() {
        Lexer lexer = new Lexer(format, in);
        boolean isWhitespace = lexer.isWhitespace(c);
        // assert if the character is a whitespace character
        assertEquals(expectedResult, isWhitespace);
    }

    @Test
    public void testIsStartOfLine() {
        Lexer lexer = new Lexer(format, in);
        boolean isStartOfLine = lexer.isStartOfLine(c);
        // assert if the character is at the start of a line
        assertEquals(expectedResult, isStartOfLine);
    }

    @Test
    public void testIsEndOfFile() {
        Lexer lexer = new Lexer(format, in);
        boolean isEndOfFile = lexer.isEndOfFile(c);
        // assert if the character indicates end of file
        assertEquals(expectedResult, isEndOfFile);
    }

    @Test
    public void testIsDelimiter() {
        Lexer lexer = new Lexer(format, in);
        boolean isDelimiter = lexer.isDelimiter(c);
        // assert if the character is a delimiter
        assertEquals(expectedResult, isDelimiter);
    }

    @Test
    public void testIsEscape() {
        Lexer lexer = new Lexer(format, in);
        boolean isEscape = lexer.isEscape(c);
        // assert if the character is an escape character
        assertEquals(expectedResult, isEscape);
    }

    @Test
    public void testIsQuoteChar() {
        Lexer lexer = new Lexer(format, in);
        boolean isQuoteChar = lexer.isQuoteChar(c);
        // assert if the character is a quote character
        assertEquals(expectedResult, isQuoteChar);
    }

    @Test
    public void testIsCommentStart() {
        Lexer lexer = new Lexer(format, in);
        boolean isCommentStart = lexer.isCommentStart(c);
        // assert if the character is a comment start character
        assertEquals(expectedResult, isCommentStart);
    }

    @Test
    public void testNextToken() {
        try {
            Lexer lexer = new Lexer(format, in);
            Token reusableToken = new Token();
            Token nextToken = lexer.nextToken(reusableToken);
            // assert the next token
            assertEquals(expectedToken, nextToken);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
}