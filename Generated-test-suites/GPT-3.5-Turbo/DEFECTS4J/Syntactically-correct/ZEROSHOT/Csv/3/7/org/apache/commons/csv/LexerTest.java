import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class LexerTest {

    private Lexer lexer;

    @Before
    public void setup() {
        lexer = new Lexer(format, in) {
            @Override
            Token nextToken(Token reusableToken) throws IOException {
                // Implement the nextToken method
                return null;
            }
        };
    }

    @Test
    public void testGetLineNumber() {
        long lineNumber = lexer.getLineNumber();
        Assert.assertEquals(0, lineNumber);
    }

    @Test
    public void testReadEscape() throws IOException {
        int escape = lexer.readEscape();
        Assert.assertEquals(END_OF_STREAM, escape);
    }

    @Test
    public void testTrimTrailingSpaces() {
        StringBuilder buffer = new StringBuilder("  test  ");
        lexer.trimTrailingSpaces(buffer);
        Assert.assertEquals("  test", buffer.toString());
    }

    @Test
    public void testReadEndOfLine() throws IOException {
        boolean endOfLine = lexer.readEndOfLine(CR);
        Assert.assertTrue(endOfLine);
    }

    @Test
    public void testIsWhitespace() {
        boolean isWhitespace = lexer.isWhitespace(' ');
        Assert.assertTrue(isWhitespace);
    }

    @Test
    public void testIsStartOfLine() {
        boolean isStartOfLine = lexer.isStartOfLine(LF);
        Assert.assertTrue(isStartOfLine);
    }

    @Test
    public void testIsEndOfFile() {
        boolean isEndOfFile = lexer.isEndOfFile(END_OF_STREAM);
        Assert.assertTrue(isEndOfFile);
    }

    @Test
    public void testIsDelimiter() {
        boolean isDelimiter = lexer.isDelimiter(delimiter);
        Assert.assertTrue(isDelimiter);
    }

    @Test
    public void testIsEscape() {
        boolean isEscape = lexer.isEscape(escape);
        Assert.assertTrue(isEscape);
    }

    @Test
    public void testIsQuoteChar() {
        boolean isQuoteChar = lexer.isQuoteChar(quoteChar);
        Assert.assertTrue(isQuoteChar);
    }

    @Test
    public void testIsCommentStart() {
        boolean isCommentStart = lexer.isCommentStart(commmentStart);
        Assert.assertTrue(isCommentStart);
    }
}